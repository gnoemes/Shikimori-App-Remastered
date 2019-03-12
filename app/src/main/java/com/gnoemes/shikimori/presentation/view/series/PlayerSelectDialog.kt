package com.gnoemes.shikimori.presentation.view.series

import android.app.Dialog
import android.os.Bundle
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.checkbox.checkBoxPrompt
import com.afollestad.materialdialogs.list.listItems
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

        return MaterialDialog(context!!).show {
            listItems(res = R.array.players) { _, index, _ ->
                val player = PlayerType.values()[index]
                callback?.onPlayerSelected(player)
            }
            checkBoxPrompt(res = R.string.common_remember_choice) {
                putSetting(SettingsExtras.IS_REMEMBER_PLAYER, !it)
            }
        }
    }

    interface Callback {
        fun onPlayerSelected(playerType: PlayerType)
    }
}