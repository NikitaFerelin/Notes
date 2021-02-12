package com.ferelin.notes.ui.details

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ferelin.notes.R
import com.ferelin.notes.base.BaseFragment
import com.ferelin.notes.databinding.FragmentDetailsBinding
import com.google.android.material.transition.MaterialContainerTransform
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import moxy.presenter.ProvidePresenterTag

class DetailsFragment : BaseFragment(), DetailsMvpView {

    @InjectPresenter
    lateinit var mPresenter: DetailsPresenter

    @ProvidePresenterTag(presenterClass = DetailsPresenter::class)
    fun provideDialogPresenterTag(): String = "Details"

    @ProvidePresenter
    fun provideDialogPresenter() = appComponent.detailsPresenter()

    private lateinit var mBinding: FragmentDetailsBinding
    private val mArguments: DetailsFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupTransitions()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mBinding = FragmentDetailsBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.apply {
            imageViewButtonBack.setOnClickListener {
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
        mBinding.textViewReminderTime.text = date
    }

    override fun setColor(color: Int) {
        mBinding.viewColorIndicator.setBackgroundColor(color)
    }

    override fun setReminder(time: String) {
        mBinding.textViewReminderTime.text = time
    }

    override fun hideReminder() {
        mBinding.rootReminder.visibility = View.GONE
    }

    private fun setupTransitions() {
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.navHostContainer
            duration = 300L
            scrimColor = Color.TRANSPARENT
        }
    }
}