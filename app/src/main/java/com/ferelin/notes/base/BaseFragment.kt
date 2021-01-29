package com.ferelin.notes.base

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment(), MvpView {

    private var mActivity: BaseActivity? = null
    val baseActivity: BaseActivity?
        get() = mActivity

    abstract fun setUp(view: View)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUp(view)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BaseActivity) {
            mActivity = context
        }
    }

    override fun onDetach() {
        mActivity = null
        super.onDetach()
    }

    override fun hideKeyboard() {
        mActivity?.hideKeyboard()
    }

    override fun showKeyboard(view: View) {
        mActivity?.showKeyboard(view)
    }

    override fun showMessage(resId: Int) {
        mActivity?.showMessage(resId)
    }

    override fun showMessage(message: String) {
        mActivity?.showMessage(message)
    }
}