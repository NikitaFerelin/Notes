package com.ferelin.notes.ui.details

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.AttrRes
import androidx.core.content.res.use
import androidx.fragment.app.setFragmentResult
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ferelin.notes.R
import com.ferelin.notes.base.BaseFragment
import com.ferelin.notes.databinding.FragmentDetailsBinding
import com.google.android.material.transition.MaterialContainerTransform

class DetailsFragment : BaseFragment(), DetailsMvpView {

    private lateinit var mPresenter: DetailsPresenter<DetailsMvpView>
    private lateinit var mBinding: FragmentDetailsBinding
    private val mArguments: DetailsFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.navHostContainer
            duration = 300L
            scrimColor = Color.TRANSPARENT
            setAllContainerColors(requireContext().themeColor(R.attr.colorPrimaryDark))
        }
    }

    fun Context.themeColor(
        @AttrRes themeAttrId: Int,
    ): Int {
        return obtainStyledAttributes(
            intArrayOf(themeAttrId)
        ).use {
            it.getColor(0, Color.MAGENTA)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mBinding = FragmentDetailsBinding.inflate(inflater, container, false)
        mPresenter = DetailsPresenter<DetailsMvpView>(requireContext()).apply {
            attachView(this@DetailsFragment)
        }
        return mBinding.root
    }

    override fun setUp(view: View) {

        with(mBinding) {
            imageViewBack.setOnClickListener {
                mPresenter.onBackBtnClicked()
            }
            imageViewDelete.setOnClickListener {
                mPresenter.onDeleteClicked()
            }
        }
        mPresenter.onViewPrepared(mArguments)
    }

    override fun dismiss() {
        findNavController().popBackStack()
    }

    override fun setResult(bundle: Bundle, responseKey: String) {
        setFragmentResult(responseKey, bundle)
    }

    override fun setTitle(title: String) {
        mBinding.textViewTitle.text = title
    }

    override fun setContent(content: String) {
        mBinding.textViewContent.text = content
    }

    override fun setDate(date: String) {
        mBinding.textViewDate.text = date
    }

    override fun setColor(color: Int) {
        mBinding.viewColorIndicator.setBackgroundColor(color)
    }

    override fun onDestroy() {
        mPresenter.detachView()
        super.onDestroy()
    }
}