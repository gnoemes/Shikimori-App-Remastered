package com.gnoemes.shikimori.presentation.view.rates.sort

import android.content.Context
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.databinding.DialogMenuBinding
import com.gnoemes.shikimori.entity.common.presentation.RateSort
import com.gnoemes.shikimori.presentation.view.base.fragment.BaseBottomSheetDialogFragment
import com.gnoemes.shikimori.utils.colorStateList
import com.gnoemes.shikimori.utils.dimenAttr
import com.gnoemes.shikimori.utils.withArgs
import kotlinx.parcelize.Parcelize

class RateSortDialog : BaseBottomSheetDialogFragment() {

    companion object {
        private const val SORTS_KEY = "SORTS_KEY"
        fun newInstance(sorts: List<Triple<RateSort, String, Boolean>>) = RateSortDialog()
                .withArgs {
                    val parcelableSorts = sorts.map { Sort(it.first, it.second, it.third) }.toTypedArray()
                    putParcelableArray(SORTS_KEY, parcelableSorts)
                }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        peekHeight = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            windowManager.currentWindowMetrics.bounds.width() - context.dimenAttr(android.R.attr.actionBarSize)
        } else {
            Point().let { point ->
                @Suppress("DEPRECATION")
                activity?.windowManager?.defaultDisplay?.getSize(point)
                point
            }.x - context.dimenAttr(android.R.attr.actionBarSize)
        }
    }

    private var _binding: DialogMenuBinding? = null
    private val binding: DialogMenuBinding? get() = _binding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = DialogMenuBinding.inflate(inflater, container, false)
        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val b = _binding ?: return

        @Suppress("DEPRECATION")
        val sorts = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelableArray(SORTS_KEY, Sort::class.java)?.map { it as Sort }!!
        } else {
            arguments?.getParcelableArray(SORTS_KEY)?.map { it as Sort }!!
        }

        b.toolbar.setTitle(R.string.sort)

        b.navView.apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                setItemBackgroundResource(R.drawable.selector_item_menu_background_accent)
                itemTextColor = context.colorStateList(R.color.selector_item_menu_text_color_accent)
            }
            setNavigationItemSelectedListener { menu ->
                (parentFragment as? RateSortCallback)?.onSortClicked(sorts.first { it.type.order == menu.itemId }.type)
                dismiss()
                true
            }
            menu.apply {
                val checkedId = sorts.find { it.isSelected }?.type?.order ?: 0
                sorts.forEachIndexed { index, sort -> add(0, sort.type.order, index, sort.text) }
                setGroupCheckable(0, true, true)
                setCheckedItem(checkedId)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    ///////////////////////////////////////////////////////////////////////////
    // GETTERS
    ///////////////////////////////////////////////////////////////////////////

    override fun getDialogLayout(): Int = R.layout.dialog_menu

    @Parcelize
    private data class Sort(
            val type: RateSort,
            val text: String,
            val isSelected: Boolean
    ) : Parcelable

    interface RateSortCallback {
        fun onSortClicked(sort: RateSort)
    }
}