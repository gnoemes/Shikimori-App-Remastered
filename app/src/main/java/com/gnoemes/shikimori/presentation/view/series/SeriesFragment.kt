package com.gnoemes.shikimori.presentation.view.series

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.AutoTransition
import androidx.transition.Fade
import androidx.transition.TransitionManager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.files.folderChooser
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.app.domain.SettingsExtras
import com.gnoemes.shikimori.entity.common.domain.Image
import com.gnoemes.shikimori.entity.series.domain.PlayerType
import com.gnoemes.shikimori.entity.series.domain.TranslationType
import com.gnoemes.shikimori.entity.series.domain.Video
import com.gnoemes.shikimori.entity.series.presentation.*
import com.gnoemes.shikimori.presentation.presenter.series.SeriesPresenter
import com.gnoemes.shikimori.presentation.presenter.series.download.SeriesDownloadDialog
import com.gnoemes.shikimori.presentation.view.base.fragment.BaseFragment
import com.gnoemes.shikimori.presentation.view.base.fragment.RouterProvider
import com.gnoemes.shikimori.presentation.view.common.fragment.DescriptionDialogFragment
import com.gnoemes.shikimori.presentation.view.common.fragment.ListDialogFragment
import com.gnoemes.shikimori.presentation.view.series.episodes.EpisodesFragment
import com.gnoemes.shikimori.presentation.view.series.translations.adapter.TranslationsAdapter
import com.gnoemes.shikimori.utils.*
import com.gnoemes.shikimori.utils.images.ImageLoader
import com.gnoemes.shikimori.utils.widgets.VerticalSpaceItemDecorator
import com.kotlinpermissions.KotlinPermissions
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.fragment_series.*
import kotlinx.android.synthetic.main.layout_default_placeholders.*
import kotlinx.android.synthetic.main.layout_series_empty_authors.*
import kotlinx.android.synthetic.main.layout_series_toolbar.*
import kotlinx.android.synthetic.main.layout_toolbar_transparent_with_search.*
import javax.inject.Inject

class SeriesFragment : BaseFragment<SeriesPresenter, SeriesView>(),
        SeriesView,
        PlayerSelectDialog.Callback,
        ListDialogFragment.DialogCallback,
        EpisodesFragment.EpisodesCallback,
        SeriesDownloadDialog.SeriesDownloadCallback,
        HasSupportFragmentInjector {

    @Inject
    lateinit var imageLoader: ImageLoader

    @InjectPresenter
    lateinit var seriesPresenter: SeriesPresenter

    @Inject
    lateinit var childFragmentInjector: DispatchingAndroidInjector<Fragment>

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = childFragmentInjector

    @ProvidePresenter
    fun providePresenter(): SeriesPresenter = presenterProvider.get().apply {
        localRouter = (parentFragment as RouterProvider).localRouter
        navigationData = arguments?.getParcelable(SERIES_NAVIGATION_DATA)!!
    }

    companion object {
        fun newInstance(data: SeriesNavigationData) = SeriesFragment().withArgs { putParcelable(SERIES_NAVIGATION_DATA, data) }
        private const val SERIES_NAVIGATION_DATA = "SERIES_NAVIGATION_DATA"
    }

    private val defaultCorners by lazy { context!!.dimen(R.dimen.margin_big) }

    private val adapter by lazy { TranslationsAdapter(getPresenter()::onHostingClicked, getPresenter()::onMenuClicked) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater.inflate(getFragmentLayout(), container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(toolbar) {
            inflateMenu(R.menu.menu_series)
            setOnMenuItemClickListener { getPresenter().onSearchClicked(); true }
            addBackButton { getPresenter().onBackPressed() }
        }

        searchToolbar.addBackButton { getPresenter().onSearchClose() }

        with(recyclerView) {
            adapter = this@SeriesFragment.adapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(VerticalSpaceItemDecorator(context.dimen(R.dimen.margin_normal).toInt(), false))
            setHasFixedSize(true)
            addOnScrollListener(shadowScrollListener)
        }

        with(searchView) {
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    hideSoftInput()
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    getPresenter().onQueryChanged(newText)
                    return true
                }
            })
            findViewById<SearchView.SearchAutoComplete>(R.id.search_src_text)?.apply {
                setPadding(0, 0, context.dp(8), 0)
                setHintTextColor(AppCompatResources.getColorStateList(context, context.attr(R.attr.colorOnPrimarySecondary).resourceId))
            }
            findViewById<LinearLayout>(R.id.search_edit_frame)?.apply {
                layoutParams = (layoutParams as? LinearLayout.LayoutParams)?.apply {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        marginStart = 0
                    }; leftMargin = 0
                }
            }
            findViewById<ImageView>(R.id.search_close_btn)?.apply {
                setPadding(context!!.dp(12), 0, context!!.dp(12), 0)
                tint(context.colorAttr(R.attr.colorOnPrimary))
            }
        }

        emptyContentView.setText(R.string.episodes_not_found)
        networkErrorView.callback = { getPresenter().onRefresh() }
        networkErrorView.showButton()

        authorsLayout.gone()
        translationBtn.gone()
        progress.visible()

        translationBtn.onClick { showTypes(true) }
        voiceBtn.onClick { onTypeSelected(TranslationType.VOICE_RU) }
        subtitlesBtn.onClick { onTypeSelected(TranslationType.SUB_RU) }
        originalBtn.onClick { onTypeSelected(TranslationType.RAW) }

        episodeChip.onClick { getPresenter().showEpisodes() }
        sourceChangeBtn.onClick { showSources(true) }

        mainSource.onClick { onSourceSelected(false) }
        altSource.onClick { onSourceSelected(true) }

        actionBtn.onClick { onSourceSelected(true) }
        fab.onClick { getPresenter().onDiscussionClicked() }
    }

    private fun onTypeSelected(newType: TranslationType) {
        getPresenter().onTypeChanged(newType)
        showTypes(false)
    }

    private fun onSourceSelected(isAlternative: Boolean) {
        getPresenter().onSourceChanged(isAlternative)
        showSources(false)
    }

    private fun showSources(show: Boolean) {
        TransitionManager.beginDelayedTransition(motionLayout, AutoTransition())
        if (show && !translationBtn.isVisible()) showTypes(false)
        sourceChangeBtn.visibleIf { !show }
        if (episodeChip.isEnabled) episodeChip.visibleIf { !show }
        mainSource.visibleIf { show }
        altSource.visibleIf { show }
    }

    private fun showTypes(show: Boolean) {
        TransitionManager.beginDelayedTransition(motionLayout, AutoTransition())
        if (show && !sourceChangeBtn.isVisible()) showSources(false)
        translationBtn.visibleIf { !show }
        voiceBtn.visibleIf { show }
        subtitlesBtn.visibleIf { show }
        originalBtn.visibleIf { show }
    }

    private val shadowScrollListener = object : RecyclerView.OnScrollListener() {
        private val shadowDefault by lazy { context!!.dimen(R.dimen.default_shadow) }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            if (recyclerView.layoutManager != null) {

                if (recyclerView.canScrollVertically(-1)) ViewCompat.setElevation(appbar, shadowDefault)
                else ViewCompat.setElevation(appbar, 0f)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        recyclerView.removeOnScrollListener(shadowScrollListener)
    }

    override fun onPlayerSelected(playerType: PlayerType) {
        getPresenter().onPlayerSelected(playerType)
    }

    override fun dialogItemCallback(tag: String?, action: String) {
        if (tag == "QualityDialog") {
            getPresenter().onQualityChoosed(action)
        }
    }

    override fun onDownload(url: String, video: Video) = getPresenter().onTrackForDownloadSelected(url, video)
    override fun onShare(url: String) = getPresenter().onShare(url)

    override fun onRateCreated(id: Long) = getPresenter().onRateCreated(id)
    override fun onEpisodeSelected(episodeId: Long, episode: Int, isAlternative: Boolean) =
            getPresenter().onEpisodeSelected(episodeId, episode, isAlternative)

    ///////////////////////////////////////////////////////////////////////////
    // GETTERS
    ///////////////////////////////////////////////////////////////////////////

    override fun getPresenter(): SeriesPresenter = seriesPresenter

    override fun getFragmentLayout(): Int = R.layout.fragment_series

    ///////////////////////////////////////////////////////////////////////////
    // MVP
    ///////////////////////////////////////////////////////////////////////////

    override fun showData(newItems: List<TranslationViewModel>) {
        adapter.bindItems(newItems)
    }

    override fun setTitle(title: String) {
        toolbar?.title = title
    }

    override fun showEmptyAuthorsView(show: Boolean, isAlternative: Boolean) {
        authorsLayout.visibleIf { show }
        titleView.setText(R.string.series_empty_authors_title)
        descriptionView.setText(R.string.series_empty_authors_description)
        if (isAlternative) actionBtn.gone()
        else actionBtn.visible()
    }

    override fun showEmptySearchView() {
        val emptyItem = SeriesPlaceholderItem(R.string.translation_search_empty_title, R.string.translation_search_empty_desc)
        adapter.bindItems(mutableListOf(emptyItem))
    }

    override fun setEpisodeName(index: Int) {
        val text = "# ".colorSpan(context!!.colorAttr(R.attr.colorSecondaryTransparent)).append("$index")
        episodeChip.text = text
    }

    override fun hideEpisodeName() {
        episodeChip.isEnabled = false
        episodeChip.gone()
    }

    override fun showEpisodeLoading(show: Boolean) {
        if (episodeChip.isEnabled) episodeChip.visibleIf { !show }
        sourceChangeBtn.visibleIf { !show }
    }

    override fun changeSource(isAlternative: Boolean) {
        mainSource.setTextColor(context!!.colorAttr(if (isAlternative) R.attr.colorOnSurface else R.attr.colorSecondary))
        altSource.setTextColor(context!!.colorAttr(if (isAlternative) R.attr.colorSecondary else R.attr.colorOnSurface))
    }

    override fun setTranslationType(type: TranslationType) {
        val icon = when (type) {
            TranslationType.VOICE_RU -> R.drawable.ic_voice
            TranslationType.SUB_RU -> R.drawable.ic_subs
            TranslationType.RAW -> R.drawable.ic_original
            else -> 0
        }

        if (icon != 0) {
            translationBtn.setIconResource(icon)
        }

        if (!translationBtn.isVisible()) translationBtn.visible()
    }

    override fun showSearchView() {
        TransitionManager.beginDelayedTransition(appBarLayout, Fade())
        searchToolbar.visible()
        toolbar.gone()
        motionLayout.gone()
        backdrop.radius = 0f
    }

    override fun onSearchClosed() {
        TransitionManager.beginDelayedTransition(appBarLayout, Fade())
        motionLayout.visible()
        toolbar.visible()
        searchToolbar.gone()
        backdrop.radius = defaultCorners
    }

    override fun showPlayerDialog() {
        val dialog = PlayerSelectDialog.newInstance()
        dialog.show(childFragmentManager, "PlayerSelect")
    }

    override fun showDownloadDialog(title: String, items: List<SeriesDownloadItem>) {
        val dialog = SeriesDownloadDialog.newInstance(title, items)
        dialog.show(childFragmentManager, "DownloadDialog")
    }

    override fun showAuthorDialog(author: String) {
        DescriptionDialogFragment.newInstance(titleRes = R.string.translation_authors, text = author)
                .show(childFragmentManager, "AuthorsDialog")
    }

    override fun showEpisodesDialog(data: EpisodesNavigationData) {
        val dialog = EpisodesFragment.newInstance(data)
        dialog.show(childFragmentManager, "EpisodeDialog")
    }

    override fun checkPermissions() {
        KotlinPermissions.with(activity!!)
                .permissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .onAccepted { getPresenter().onStoragePermissionsAccepted() }
                .ask()
    }

    override fun showFolderChooserDialog() {
        MaterialDialog(context!!).show {
            folderChooser(
                    allowFolderCreation = true,
                    emptyTextRes = R.string.download_folder_empty,
                    folderCreationLabel = R.string.download_new_folder)
            { _, file ->
                putSetting(SettingsExtras.DOWNLOAD_FOLDER, file.absolutePath)
            }
            positiveButton { getPresenter().onStoragePermissionsAccepted() }
        }
    }

    override fun showQualityChooser(items: List<Pair<String, String>>) {
        val dialog = ListDialogFragment.newInstance()
        dialog.apply {
            setItems(items)
        }.show(childFragmentManager, "QualityDialog")
    }

    override fun showEmptyView() {
        authorsLayout.visible()
        actionBtn.gone()
        titleView.setText(R.string.series_empty_episode_title)
        descriptionView.setText(R.string.series_empty_episode_description)
        sourceChangeBtn.gone()
    }

    override fun showContent(show: Boolean) = recyclerView.visibleIf { show }
    override fun hideEmptyView() = emptyContentView.gone()
    override fun showNetworkView() = networkErrorView.visible()
    override fun hideNetworkView() = networkErrorView.gone()
    override fun setBackground(image: Image) = imageLoader.setBlurredImage(backgroundImage, image.original, sampling = 2)
    override fun scrollToPosition(position: Int) = recyclerView.scrollToPosition(position)
    override fun hideFab() = fab.hide()
    override fun onShowLoading() = progress.visible()
    override fun onHideLoading() = progress.gone()
    override fun onShowLightLoading() = progress.visible()
    override fun onHideLightLoading() = progress.gone()
}