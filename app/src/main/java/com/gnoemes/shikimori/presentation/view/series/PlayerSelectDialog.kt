package com.gnoemes.shikimori.presentation.view.series

import android.app.Dialog
import android.os.Bundle
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.ListView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.app.domain.SettingsExtras
import com.gnoemes.shikimori.entity.series.domain.PlayerType
import com.gnoemes.shikimori.presentation.view.base.fragment.MvpDialogFragment
import com.gnoemes.shikimori.utils.putSetting

class PlayerSelectDialog : MvpDialogFragment() {

    private var callback: Callback? = null

    companion object {
        fun newInstance() = PlayerSelectDialog()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        callback = parentFragment as? Callback

        val players = resources!!.getStringArray(R.array.players)
        val checkBox = CheckBox(context!!).apply { setText(R.string.common_remember_choice) }
        val listView = ListView(context!!).apply {
            adapter = ArrayAdapter(context!!, android.R.layout.simple_list_item_1, players)
            divider = null
        }
        val layout = LinearLayout(context!!).apply {
            orientation = LinearLayout.VERTICAL
            addView(listView, LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f))
            addView(checkBox)
        }

        val dialog = MaterialAlertDialogBuilder(context!!).apply {
            setView(layout)
        }.create()

        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val player = PlayerType.values()[position]
            putSetting(SettingsExtras.IS_REMEMBER_PLAYER, !checkBox.isChecked)
            callback?.onPlayerSelected(player)
            dialog.dismiss()
        }

        return dialog
    }

    interface Callback {
        fun onPlayerSelected(playerType: PlayerType)
    }
}