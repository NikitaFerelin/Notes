package com.ferelin.notes.base

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar
import moxy.MvpAppCompatFragment

abstract class BaseFragment : MvpAppCompatFragment(), AppMvpView {

    override fun hideKeyboard(context: Context, view: View) {
        val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun showKeyboard(context: Context, view: View) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }

    override fun showMessage(root: View, @StringRes resId: Int) {
        val message = getString(resId)
        showMessage(root, message)
    }

    override fun showMessage(root: View, message: String) {
        Snackbar.make(root, message, Snackbar.LENGTH_SHORT).show()
        /*View sbView = snackbar.getView();
        TextView textView = (TextView) sbView
                .findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(ContextCompat.getColor(this, R.color.white));
        snackbar.show();*/
    }
}