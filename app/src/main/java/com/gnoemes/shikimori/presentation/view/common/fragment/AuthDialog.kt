package com.gnoemes.shikimori.presentation.view.common.fragment

import android.app.Dialog
import android.os.Bundle
import com.afollestad.materialdialogs.MaterialDialog
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.presentation.view.base.fragment.MvpDialogFragment

class AuthDialog : MvpDialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return MaterialDialog(context!!)
                .show {
                    message(R.string.dialog_auth_message)
                    positiveButton(res = R.string.common_sign_in) { target?.onSignIn() }
                    negativeButton(res = R.string.common_sign_up) { target?.onSignUp() }
                }
    }

    private val target: AuthCallback?
        get() = (targetFragment as? AuthCallback)

    interface AuthCallback {
        fun onSignIn()
        fun onSignUp()
    }
}