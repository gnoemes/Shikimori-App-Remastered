package com.gnoemes.shikimori.presentation.view.series

import android.Manifest
import android.os.Build
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.AutoTransition
import androidx.transition.Fade
import androidx.transition.TransitionManager
import android.os.Environment
import android.provider.DocumentsContract
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.databinding.FragmentSeriesBinding
import com.gnoemes.shikimori.databinding.LayoutDefaultPlaceholdersBinding
import com.gnoemes.shikimori.databinding.LayoutSeriesEmptyAuthorsBinding
import com.gnoemes.shikimori.databinding.LayoutSeriesToolbarBinding
import com.gnoemes.shikimori.databinding.LayoutToolbarTransparentWithSearchBinding
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
import androidx.activity.result.contract.ActivityResultContracts
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class SeriesFragment : BaseFragment<SeriesPresenter, SeriesView>(),
        SeriesView,
        PlayerSelectDialog.Callback,
        ListDialogFragment.DialogCallback,
        EpisodesFragment.EpisodesCallback,
        SeriesDownloadDialog.SeriesDownloadCallback,
        HasAndroidInjector {

    private var _seriesBinding: FragmentSeriesBinding? = null
    private val seriesBinding: FragmentSeriesBinding? get() = _seriesBinding

    private var _toolbarTransparentBinding: LayoutToolbarTransparentWithSearchBinding? = null
    private val toolbarTransparentBinding: LayoutToolbarTransparentWithSearchBinding? get() = _toolbarTransparentBinding

    private var _seriesToolbarBinding: LayoutSeriesToolbarBinding? = null
    private val seriesToolbarBinding: LayoutSeriesToolbarBinding? get() = _seriesToolbarBinding

    private var _defaultPlaceholdersBinding: LayoutDefaultPlaceholdersBinding? = null
    private val defaultPlaceholdersBinding: LayoutDefaultPlaceholdersBinding? get() = _defaultPlaceholdersBinding

    private var _authorsLayoutBinding: LayoutSeriesEmptyAuthorsBinding? = null
    private val authorsLayoutBinding: LayoutSeriesEmptyAuthorsBinding? get() = _authorsLayoutBinding

    private val storagePermissionsLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions.values.all { it }) {
            getPresenter().onStoragePermissionsAccepted()
        }
    }

    private val folderChooserLauncher = registerForActivityResult(
        ActivityResultContracts.OpenDocumentTree()
    ) { uri ->
        if (uri != null) {
            val path = getPathFromTreeUri(uri)
            if (path != null) {
                putSetting(SettingsExtras.DOWNLOAD_FOLDER, path)
            }
            getPresenter().onStoragePermissionsAccepted()
        }
    }

    @Inject
    lateinit var imageLoader: ImageLoader

    @InjectPresenter
    lateinit var seriesPresenter: SeriesPresenter

    @Inject
    lateinit var childFragmentInjector: DispatchingAndroidInjector<Any>

    override fun androidInjector(): AndroidInjector<Any> = childFragmentInjector

    @ProvidePresenter
    fun providePresenter(): SeriesPresenter = presenterProvider.get().apply {
        localRouter = (parentFragment as RouterProvider).localRouter
        @Suppress("DEPRECATION")
        navigationData = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(SERIES_NAVIGATION_DATA, SeriesNavigationData::class.java)!!
        } else {
            arguments?.getParcelable(SERIES_NAVIGATION_DATA)!!
        }
    }

    companion object {
        fun newInstance(data: SeriesNavigationData) = SeriesFragment().withArgs { putParcelable(SERIES_NAVIGATION_DATA, data) }
        private const val SERIES_NAVIGATION_DATA = "SERIES_NAVIGATION_DATA"
    }

    private val defaultCorners by lazy { context!!.dimen(R.dimen.margin_big) }

    private val adapter by lazy { TranslationsAdapter(getPresenter()::onHostingClicked, getPresenter()::onMenuClicked) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _seriesBinding = FragmentSeriesBinding.inflate(inflater, container, false)
        return seriesBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _toolbarTransparentBinding = LayoutToolbarTransparentWithSearchBinding.bind(seriesBinding!!.root.findViewById(R.id.toolbarTransparentWithSearch))
        _seriesToolbarBinding = LayoutSeriesToolbarBinding.bind(seriesBinding!!.root.findViewById(R.id.seriesToolbar))
        _defaultPlaceholdersBinding = LayoutDefaultPlaceholdersBinding.bind(seriesBinding!!.holders)
        _authorsLayoutBinding = LayoutSeriesEmptyAuthorsBinding.bind(seriesBinding!!.root.findViewById(R.id.authorsLayout))

        with(toolbarTransparentBinding!!.toolbar) {
            inflateMenu(R.menu.menu_series)
            setOnMenuItemClickListener { getPresenter().onSearchClicked(); true }
            addBackButton { getPresenter().onBackPressed() }
        }

        toolbarTransparentBinding!!.searchToolbar.addBackButton { getPresenter().onSearchClose() }

        with(seriesBinding!!.recyclerView) {
            adapter = this@SeriesFragment.adapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(VerticalSpaceItemDecorator(context.dimen(R.dimen.margin_normal).toInt(), false))
            setHasFixedSize(true)
            addOnScrollListener(shadowScrollListener)
        }

        with(toolbarTransparentBinding!!.searchView) {
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
            toolbarTransparentBinding!!.searchView.findViewById<SearchView.SearchAutoComplete>(androidx.appcompat.R.id.search_src_text)?.apply {
                setPadding(0, 0, context.dp(8), 0)
                setHintTextColor(context.colorStateList(context.attr(R.attr.colorOnPrimarySecondary).resourceId))
            }
            toolbarTransparentBinding!!.searchView.findViewById<LinearLayout>(androidx.appcompat.R.id.search_edit_frame)?.apply {
                layoutParams = (layoutParams as? LinearLayout.LayoutParams)?.apply {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        marginStart = 0
                    }; leftMargin = 0
                }
            }
            toolbarTransparentBinding!!.searchView.findViewById<ImageView>(androidx.appcompat.R.id.search_close_btn)?.apply {
                setPadding(context!!.dp(12), 0, context!!.dp(12), 0)
                tint(context.colorAttr(R.attr.colorOnPrimary))
            }
        }

        defaultPlaceholdersBinding!!.emptyContentView.setText(R.string.episodes_not_found)
        defaultPlaceholdersBinding!!.networkErrorView.callback = { getPresenter().onRefresh() }
        defaultPlaceholdersBinding!!.networkErrorView.showButton()

        authorsLayoutBinding!!.root.gone()
        seriesToolbarBinding!!.translationBtn.gone()
        seriesBinding!!.progress.visible()

        seriesToolbarBinding!!.translationBtn.onClick { showTypes(true) }
        seriesToolbarBinding!!.voiceBtn.onClick { onTypeSelected(TranslationType.VOICE_RU) }
        seriesToolbarBinding!!.subtitlesBtn.onClick { onTypeSelected(TranslationType.SUB_RU) }
        seriesToolbarBinding!!.originalBtn.onClick { onTypeSelected(TranslationType.RAW) }

        seriesToolbarBinding!!.episodeChip.onClick { getPresenter().showEpisodes() }
        seriesToolbarBinding!!.sourceChangeBtn.onClick { showSources(true) }

        seriesToolbarBinding!!.mainSource.onClick { onSourceSelected(false) }
        seriesToolbarBinding!!.altSource.onClick { onSourceSelected(true) }

        authorsLayoutBinding!!.actionBtn.onClick { onSourceSelected(true) }
        seriesBinding!!.fab.onClick { getPresenter().onDiscussionClicked() }
        seriesToolbarBinding!!.nextEpisodeBtn.onClick { getPresenter().onNextEpisode() }
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
        TransitionManager.beginDelayedTransition(seriesToolbarBinding!!.motionLayout, AutoTransition())
        if (show && !seriesToolbarBinding!!.translationBtn.isVisible()) showTypes(false)
        seriesToolbarBinding!!.sourceChangeBtn.visibleIf { !show }
        if (seriesToolbarBinding!!.episodeChip.isEnabled) seriesToolbarBinding!!.episodeChip.visibleIf { !show }
        if (seriesToolbarBinding!!.nextEpisodeBtn.isEnabled) seriesToolbarBinding!!.nextEpisodeBtn.visibleIf { !show }
        seriesToolbarBinding!!.mainSource.visibleIf { show }
        seriesToolbarBinding!!.altSource.visibleIf { show }
    }

    private fun showTypes(show: Boolean) {
        TransitionManager.beginDelayedTransition(seriesToolbarBinding!!.motionLayout, AutoTransition())
        if (show && !seriesToolbarBinding!!.sourceChangeBtn.isVisible()) showSources(false)
        seriesToolbarBinding!!.translationBtn.visibleIf { !show }
        seriesToolbarBinding!!.voiceBtn.visibleIf { show }
        seriesToolbarBinding!!.subtitlesBtn.visibleIf { show }
        seriesToolbarBinding!!.originalBtn.visibleIf { show }
    }

    private val shadowScrollListener = object : RecyclerView.OnScrollListener() {
        private val shadowDefault by lazy { context!!.dimen(R.dimen.default_shadow) }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            if (recyclerView.layoutManager != null) {

                if (recyclerView.canScrollVertically(-1)) ViewCompat.setElevation(seriesToolbarBinding!!.appbar, shadowDefault)
                else ViewCompat.setElevation(seriesToolbarBinding!!.appbar, 0f)
            }
        }
    }

    override fun onDestroyView() {
        seriesBinding!!.recyclerView.removeOnScrollListener(shadowScrollListener)
        super.onDestroyView()
        _seriesBinding = null
        _toolbarTransparentBinding = null
        _seriesToolbarBinding = null
        _defaultPlaceholdersBinding = null
        _authorsLayoutBinding = null
    }

    override fun onPlayerSelected(playerType: PlayerType) {
        getPresenter().onPlayerSelected(playerType)
    }

    override fun dialogItemCallback(tag: String?, url: String) {
        if (tag == "QualityDialog") {
            getPresenter().onQualityChoosed(url)
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
        toolbarTransparentBinding?.toolbar?.title = title
    }

    override fun showEmptyAuthorsView(show: Boolean, isAlternative: Boolean) {
        val b = authorsLayoutBinding ?: return
        b.root.visibleIf { show }
        b.titleView.setText(R.string.series_empty_authors_title)
        b.descriptionView.setText(R.string.series_empty_authors_description)
        if (isAlternative) b.actionBtn.gone()
        else b.actionBtn.visible()
    }

    override fun showEmptySearchView() {
        val emptyItem = SeriesPlaceholderItem(R.string.translation_search_empty_title, R.string.translation_search_empty_desc)
        adapter.bindItems(mutableListOf(emptyItem))
    }

    override fun setEpisodeName(index: Int) {
        val b = seriesToolbarBinding ?: return
        val text = "# ".colorSpan(context!!.colorAttr(R.attr.colorSecondaryTransparent)).append("$index")
        b.episodeChip.text = text
    }

    override fun showNextEpisode(show: Boolean) {
        val b = seriesToolbarBinding ?: return
        b.nextEpisodeBtn.isEnabled = show
        b.nextEpisodeBtn.visibleIf { show }
    }

    override fun hideEpisodeName() {
        val b = seriesToolbarBinding ?: return
        b.episodeChip.isEnabled = false
        b.episodeChip.gone()
    }

    override fun showEpisodeLoading(show: Boolean) {
        val b = seriesToolbarBinding ?: return
        TransitionManager.beginDelayedTransition(b.motionLayout, Fade())
        if (b.episodeChip.isEnabled) b.episodeChip.visibleIf { !show }
        if (b.nextEpisodeBtn.isEnabled) b.nextEpisodeBtn.visibleIf { !show }
        b.sourceChangeBtn.visibleIf { !show }
    }

    override fun changeSource(isAlternative: Boolean) {
        val b = seriesToolbarBinding ?: return
        b.mainSource.setTextColor(context!!.colorAttr(if (isAlternative) R.attr.colorOnSurface else R.attr.colorSecondary))
        b.altSource.setTextColor(context!!.colorAttr(if (isAlternative) R.attr.colorSecondary else R.attr.colorOnSurface))
    }

    override fun setTranslationType(type: TranslationType) {
        val b = seriesToolbarBinding ?: return
        val icon = when (type) {
            TranslationType.VOICE_RU -> R.drawable.ic_voice
            TranslationType.SUB_RU -> R.drawable.ic_subs
            TranslationType.RAW -> R.drawable.ic_original
            else -> 0
        }

        if (icon != 0) {
            b.translationBtn.setIconResource(icon)
        }

        if (!b.translationBtn.isVisible()) b.translationBtn.visible()
    }

    override fun showSearchView() {
        val tb = toolbarTransparentBinding ?: return
        val sb = seriesToolbarBinding
        val b = seriesBinding
        TransitionManager.beginDelayedTransition(tb.appBarLayout, Fade())
        tb.searchToolbar.visible()
        tb.toolbar.gone()
        sb?.motionLayout?.gone()
        b?.backdrop?.radius = 0f
    }

    override fun onSearchClosed() {
        val tb = toolbarTransparentBinding ?: return
        val sb = seriesToolbarBinding
        val b = seriesBinding
        TransitionManager.beginDelayedTransition(tb.appBarLayout, Fade())
        sb?.motionLayout?.visible()
        tb.toolbar.visible()
        tb.searchToolbar.gone()
        b?.backdrop?.radius = defaultCorners
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

    override fun showTracksNotFoundError() {
        Toast.makeText(requireContext(), R.string.series_tracks_empty, Toast.LENGTH_SHORT).show()
    }

    override fun checkPermissions() {
        storagePermissionsLauncher.launch(
            arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        )
    }

    override fun showFolderChooserDialog() {
        folderChooserLauncher.launch(null)
    }

    private fun getPathFromTreeUri(uri: Uri): String? {
        if (DocumentsContract.isTreeUri(uri)) {
            val docId = DocumentsContract.getTreeDocumentId(uri)
            val parts = docId.split(":")
            if (parts.isNotEmpty() && parts[0] == "primary") {
                return Environment.getExternalStorageDirectory().absolutePath + "/" + parts.drop(1).joinToString(":")
            }
        }
        return null
    }

    override fun showQualityChooser(items: List<Pair<String, String>>) {
        val dialog = ListDialogFragment.newInstance()
        dialog.apply {
            setItems(items)
        }.show(childFragmentManager, "QualityDialog")
    }

    override fun showEmptyView() {
        val ab = authorsLayoutBinding ?: return
        val sb = seriesToolbarBinding
        val pb = defaultPlaceholdersBinding
        ab.root.visible()
        ab.actionBtn.gone()
        ab.titleView.setText(R.string.series_empty_episode_title)
        ab.descriptionView.setText(R.string.series_empty_episode_description)
        sb?.sourceChangeBtn?.gone()
        pb?.emptyContentView?.gone()
    }
    override fun showContent(show: Boolean) { seriesBinding?.recyclerView?.visibleIf { show } }
    override fun hideEmptyView() { defaultPlaceholdersBinding?.emptyContentView?.gone() }
    override fun showNetworkView() { defaultPlaceholdersBinding?.networkErrorView?.visible() }
    override fun hideNetworkView() { defaultPlaceholdersBinding?.networkErrorView?.gone() }
    override fun setBackground(image: Image) { seriesBinding?.let { imageLoader.setBlurredImage(it.backgroundImage, image.original, sampling = 2) } }
    override fun scrollToPosition(position: Int) { seriesBinding?.recyclerView?.scrollToPosition(position) }
    override fun showFab(show: Boolean) { if (show) seriesBinding?.fab?.show() else seriesBinding?.fab?.hide() }
    override fun onShowLoading() { seriesBinding?.progress?.visible() }
    override fun onHideLoading() { seriesBinding?.progress?.gone() }
    override fun onShowLightLoading() { seriesBinding?.progress?.visible() }
    override fun onHideLightLoading() { seriesBinding?.progress?.gone() }
}