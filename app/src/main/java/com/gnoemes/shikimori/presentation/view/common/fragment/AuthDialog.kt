package com.gnoemes.shikimori.presentation.view.common.fragment

import android.app.Dialog
import android.os.Bundle
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.presentation.view.base.fragment.MvpDialogFragment
import androidx.core.os.bundleOf

class AuthDialog : MvpDialogFragment() {

    companion object {
        const val AUTH_REQUEST_KEY = "auth_request"
        const val AUTH_ACTION_KEY = "auth_action"
        const val ACTION_SIGN_IN = "sign_in"
        const val ACTION_SIGN_UP = "sign_up"
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return MaterialAlertDialogBuilder(context!!).apply {
            setMessage(R.string.dialog_auth_message)
            setPositiveButton(R.string.common_sign_in) { _, _ ->
                parentFragmentManager.setFragmentResult(AUTH_REQUEST_KEY, bundleOf(AUTH_ACTION_KEY to ACTION_SIGN_IN))
            }
            setNegativeButton(R.string.common_sign_up) { _, _ ->
                parentFragmentManager.setFragmentResult(AUTH_REQUEST_KEY, bundleOf(AUTH_ACTION_KEY to ACTION_SIGN_UP))
            }
        }.create()
    }

    interface AuthCallback {
        fun onSignIn()
        fun onSignUp()
    }
}