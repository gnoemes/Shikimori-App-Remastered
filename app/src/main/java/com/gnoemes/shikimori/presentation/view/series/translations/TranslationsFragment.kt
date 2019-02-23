package com.gnoemes.shikimori.presentation.view.series.translations

import android.Manifest
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
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
import com.gnoemes.shikimori.entity.app.domain.AppExtras
import com.gnoemes.shikimori.entity.app.domain.SettingsExtras
import com.gnoemes.shikimori.entity.series.domain.PlayerType
import com.gnoemes.shikimori.entity.series.domain.TranslationType
import com.gnoemes.shikimori.entity.series.presentation.SeriesPlaceholderItem
import com.gnoemes.shikimori.entity.series.presentation.TranslationViewModel
import com.gnoemes.shikimori.entity.series.presentation.TranslationsNavigationData
import com.gnoemes.shikimori.presentation.presenter.series.translations.TranslationsPresenter
import com.gnoemes.shikimori.presentation.view.base.fragment.RouterProvider
import com.gnoemes.shikimori.presentation.view.common.fragment.DescriptionDialogFragment
import com.gnoemes.shikimori.presentation.view.common.fragment.ListDialogFragment
import com.gnoemes.shikimori.presentation.view.series.BaseSeriesFragment
import com.gnoemes.shikimori.presentation.view.series.PlayerSelectDialog
import com.gnoemes.shikimori.presentation.view.series.translations.adapter.TranslationsAdapter
import com.gnoemes.shikimori.utils.*
import com.gnoemes.shikimori.utils.widgets.VerticalSpaceItemDecorator
import com.kotlinpermissions.KotlinPermissions
import com.lapism.searchview.SearchView
import kotlinx.android.synthetic.main.fragment_base_series.*
import kotlinx.android.synthetic.main.fragment_translations.*
import kotlinx.android.synthetic.main.layout_default_placeholders.*
import kotlinx.android.synthetic.main.layout_toolbar_transparent.*
import kotlinx.android.synthetic.main.layout_translations_toolbar.*

class TranslationsFragment : BaseSeriesFragment<TranslationsPresenter, TranslationsView>(),
        TranslationsView, PlayerSelectDialog.Callback, ListDialogFragment.DialogCallback {

    @InjectPresenter
    lateinit var translationsPresenter: TranslationsPresenter

    @ProvidePresenter
    fun providePresenter(): TranslationsPresenter {
        return presenterProvider.get().apply {
            localRouter = (parentFragment as RouterProvider).localRouter
            navigationData = arguments?.getParcelable(AppExtras.ARGUMENT_TRANSLATIONS_DATA)!!
        }
    }

    companion object {
        fun newInstance(data: TranslationsNavigationData) = TranslationsFragment().withArgs { putParcelable(AppExtras.ARGUMENT_TRANSLATIONS_DATA, data) }
    }

    private val adapter by lazy { TranslationsAdapter(getPresenter()::onHostingClicked, getPresenter()::onMenuClicked) }

    private val defaultCorners by lazy { context!!.dimen(R.dimen.margin_big) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configureSearchView()

        toolbar?.apply {
            setTitle(R.string.translation_authors)
            inflateMenu(R.menu.menu_translations)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.item_search -> getPresenter().onSearchClicked()
                }; true
            }
        }

        with(recyclerView) {
            adapter = this@TranslationsFragment.adapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(VerticalSpaceItemDecorator(context.dimen(R.dimen.margin_normal).toInt(), true))
            setHasFixedSize(true)
            addOnScrollListener(shadowScrollListener)
        }

        emptyContentView.setText(R.string.translations_nothing)
        emptyContentView.hideButton()

        networkErrorView.callback = { getPresenter().onRefresh() }
        networkErrorView.showButton()

        titleView.gone()
        fab.onClick { getPresenter().onDiscussionClicked() }

        translationBtn.onClick { showTypes(true) }
        voiceBtn.onClick { onTypeSelected(TranslationType.VOICE_RU) }
        subtitlesBtn.onClick { onTypeSelected(TranslationType.SUB_RU) }
        originalBtn.onClick { onTypeSelected(TranslationType.RAW) }
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

    private fun configureSearchView() {
        searchView.findViewById<CardView>(R.id.cardView).apply {
            (layoutParams as FrameLayout.LayoutParams).setMargins(0, 0, 0, 0)
            radius = 0f
        }
        searchView.hint = context!!.getString(R.string.translation_author_hint)
        searchView.setHeight(56f)
        searchView.setShadow(true)
        searchView.setArrowOnly(true)
        searchView.shouldClearOnClose = true
        val defaultPadding = context?.dimen(R.dimen.margin_big)?.toInt() ?: 0
        searchView.findViewById<LinearLayout>(R.id.linearLayout).setPadding(0, 0, defaultPadding, 0)
        searchView.findViewById<ImageView>(R.id.imageView_arrow_back).apply {
            (layoutParams as LinearLayout.LayoutParams).apply { width = LinearLayout.LayoutParams.WRAP_CONTENT; height = LinearLayout.LayoutParams.WRAP_CONTENT }
            setPadding(defaultPadding, defaultPadding, 0, defaultPadding)
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.close(true)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                getPresenter().onQueryChanged(newText)
                return true
            }
        })

        searchView.setOnOpenCloseListener(object : SearchView.OnOpenCloseListener {
            var first = true
            override fun onOpen(): Boolean = true

            override fun onClose(): Boolean {

                if (!first) getPresenter().onSearchClosed()
                first = false
                return true
            }
        })
    }

    private fun onTypeSelected(newType: TranslationType) {
        getPresenter().onTypeChanged(newType)
        showTypes(false)
    }

    private fun showTypes(show: Boolean) {
        TransitionManager.beginDelayedTransition(motionLayout, AutoTransition())
        translationTypeLabel.visibleIf { !show }
        translationBtn.visibleIf { !show }
        voiceBtn.visibleIf { show }
        subtitlesBtn.visibleIf { show }
        originalBtn.visibleIf { show }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        recyclerView.removeOnScrollListener(shadowScrollListener)
    }

    override fun onPlayerSelected(playerType: PlayerType) {
        getPresenter().onPlayerSelected(playerType)
    }

    override fun dialogItemCallback(tag: String?, url: String) {
        getPresenter().onTrackForDownloadSelected(url)
    }

    ///////////////////////////////////////////////////////////////////////////
    // GETTERS
    ///////////////////////////////////////////////////////////////////////////

    override fun getPresenter(): TranslationsPresenter = translationsPresenter

    override fun getFragmentLayout(): Int = R.layout.fragment_translations

    ///////////////////////////////////////////////////////////////////////////
    // MVP
    ///////////////////////////////////////////////////////////////////////////

    override fun showData(newItems: List<TranslationViewModel>) {
        adapter.bindItems(newItems)
    }

    override fun showContent(show: Boolean) {
        recyclerView.visibleIf { show }
    }

    override fun setEpisodeName(index: Int) {
        val text = String.format(context!!.getString(R.string.episode_number), index)
        translationToolbar.title = text
    }

    override fun setTranslationType(type: TranslationType) {
        val textAndIcon = when (type) {
            TranslationType.VOICE_RU -> Pair(R.string.translation_voice, R.drawable.ic_voice)
            TranslationType.SUB_RU -> Pair(R.string.translation_subtitles, R.drawable.ic_subs)
            TranslationType.RAW -> Pair(R.string.translation_original, R.drawable.ic_original)
            else -> null
        }

        textAndIcon?.let {
            translationTypeLabel.setText(it.first)
            translationBtn.setImageResource(it.second)
        }
    }

    override fun showSearchView() {
        searchView.open(true)
        TransitionManager.beginDelayedTransition(appbar, Fade())
        translationToolbar.gone()
        backdrop.radius = 0f
    }

    override fun onSearchClosed() {
        TransitionManager.beginDelayedTransition(appbar, Fade())
        translationToolbar.visible()
        backdrop.radius = defaultCorners
    }

    override fun showSearchEmpty() {
        val emptyItem = SeriesPlaceholderItem(R.string.translation_search_empty_title, R.string.translation_search_empty_desc)
        adapter.bindItems(mutableListOf(emptyItem))
    }

    override fun showEmptyView() {
        emptyContentView.visible()
    }

    override fun hideEmptyView() {
        emptyContentView.gone()
    }

    override fun showNetworkView() {
        networkErrorView.visible()
    }

    override fun hideNetworkView() {
        networkErrorView.gone()
    }

    override fun scrollToPosition(position: Int) {
        (recyclerView.layoutManager  as? LinearLayoutManager)?.scrollToPositionWithOffset(position, 0)
    }

    override fun showPlayerDialog() {
        val dialog = PlayerSelectDialog.newInstance()
        dialog.show(childFragmentManager, "PlayerSelect")
    }

    override fun showDownloadDialog(items: List<Pair<String, String>>) {
        val dialog = ListDialogFragment.newInstance()
        dialog.apply {
            setItems(items)
        }.show(childFragmentManager, "DownloadDialog")
    }

    override fun showAuthorDialog(author: String) {
        val positiveText = context?.getString(R.string.common_understand)
        DescriptionDialogFragment.newInstance(titleRes = R.string.translation_authors, text = author, positiveText = positiveText)
                .show(childFragmentManager, "AuthorsDialog")
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

    override fun onShowLoading() = progress.visible()
    override fun onHideLoading() = progress.gone()
    override fun onShowLightLoading() = progress.visible()
    override fun onHideLightLoading() = progress.gone()
}