package com.ferelin.notes.base

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.StringRes
import com.ferelin.notes.App
import com.ferelin.notes.di.component.AppComponent
import com.google.android.material.snackbar.Snackbar
import moxy.MvpAppCompatFragment

abstract class BaseFragment : MvpAppCompatFragment(), AppMvpView {

    val appComponent: AppComponent by lazy {
        (requireActivity().application as App).appComponent
    }

    override fun hideKeyboard(view: View) {
        val imm = requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun showKeyboard(view: View) {
        view.requestFocus()
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }

    override fun showMessage(@StringRes resId: Int) {
        val message = getString(requireView().findViewById(android.R.id.content))
        showMessage(message)
    }

    override fun showMessage(message: String) {
        Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).show()
    }
}