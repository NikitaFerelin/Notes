package com.ferelin.notes.ui.create

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.transition.Slide
import com.ferelin.notes.R
import com.ferelin.notes.base.BaseFragment
import com.ferelin.notes.databinding.FragmentCreateNoteBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.transition.MaterialContainerTransform
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CreateFragment : BaseFragment(), CreateMvpView {

    private lateinit var mPresenter: CreatePresenter<CreateMvpView>
    private lateinit var mBinding: FragmentCreateNoteBinding
    private lateinit var mBottomSheetBehavior: BottomSheetBehavior<ViewGroup>

    private val mArguments: CreateFragmentArgs by navArgs()
    private var mIsAcceptBtnLocked = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mBinding = FragmentCreateNoteBinding.inflate(inflater, container, false)
        mPresenter = CreatePresenter<CreateMvpView>(requireContext()).apply {
            attachView(this@CreateFragment)
        }
        return mBinding.root
    }

    override fun setUp(view: View) {
        setupTransitions()
        setupEditFields()
        setupImageViews()
        setupBottomSheet()

        lifecycleScope.launch(Dispatchers.IO) {
            mPresenter.onViewPrepared(mArguments)
        }
    }

    override fun setResult(arg: Bundle, responseKey: String) {
        setFragmentResult(responseKey, arg)
    }

    override fun setNote(title: String, content: String) {
        mBinding.apply {
            editTextTitle.setText(title)
            editTextContent.setText(content)
        }
    }

    override fun setFocusToContentEdit() {
        mBinding.editTextContent.requestFocus()
        showKeyboard(mBinding.editTextContent)
    }

    override fun setSelectedColor(color: Int) {
        mBinding.viewColorIndicator.setBackgroundColor(color)
    }

    override fun lockAcceptButton() {
        mBinding.imageViewAccept.setImageResource(R.drawable.ic_check_locked)
        mIsAcceptBtnLocked = true
    }

    override fun unlockAcceptButton() {
        mBinding.imageViewAccept.setImageResource(R.drawable.ic_check_unlocked)
        mIsAcceptBtnLocked = false
    }

    override fun expandBottomSheet() {
        mBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun collapseBottomSheet() {
        mBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    override fun dismiss() {
        hideKeyboard()
        findNavController().popBackStack()
    }

    override fun selectedColorIconToDefault() {
        switchBottomSheetIconTo(R.id.viewSelectableDefault)
    }

    override fun selectedColorIconToRed() {
        switchBottomSheetIconTo(R.id.viewSelectableRed)
    }

    override fun selectedColorIconToGray() {
        switchBottomSheetIconTo(R.id.viewSelectableGray)
    }

    override fun selectedColorIconToOrange() {
        switchBottomSheetIconTo(R.id.viewSelectableOrange)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        lifecycleScope.launch(Dispatchers.IO) {
            mPresenter.onSaveInstanceState(mBinding.editTextTitle.text, mBinding.editTextContent.text)
        }
    }

    override fun onDestroyView() {
        mPresenter.detachView()
        super.onDestroyView()
    }

    private fun setupTransitions() {
        enterTransition = MaterialContainerTransform().apply {
            startView = requireParentFragment().requireView().findViewById(R.id.fab)
            endView = mBinding.root
            duration = 300L
            scrimColor = Color.TRANSPARENT
        }

        returnTransition = Slide().apply {
            duration = 250L
            addTarget(R.id.rootCardView)
        }
    }

    private fun setupEditFields() {
        mBinding.apply {
            editTextTitle.addTextChangedListener {
                mPresenter.onTextChanged(editTextContent.text, it, mIsAcceptBtnLocked)
            }
            editTextContent.addTextChangedListener {
                mPresenter.onTextChanged(it, editTextTitle.text, mIsAcceptBtnLocked)
            }
        }
    }

    private fun setupImageViews() {
        mBinding.apply {
            imageViewAccept.setOnClickListener {
                lifecycleScope.launch(Dispatchers.Main) {
                    mPresenter.onAcceptBtnClicked(mIsAcceptBtnLocked, editTextTitle.text, editTextContent.text)
                }
            }
            imageViewBack.setOnClickListener {
                mPresenter.onBackBtnClicked()
            }
        }
    }

    private fun setupBottomSheet() {
        mBottomSheetBehavior = BottomSheetBehavior.from(mBinding.rootBottomSheet.root)

        mBinding.rootBottomSheet.apply {
            textViewHint.setOnClickListener {
                mPresenter.onBottomSheetClicked(mBottomSheetBehavior.state)
            }
            viewSelectableDefault.setOnClickListener {
                mPresenter.onDefaultColorClicked()
            }
            viewSelectableOrange.setOnClickListener {
                mPresenter.onOrangeColorClicked()
            }
            viewSelectableGray.setOnClickListener {
                mPresenter.onGrayColorClicked()
            }
            viewSelectableRed.setOnClickListener {
                mPresenter.onRedColorClicked()
            }
        }
    }

    private fun switchBottomSheetIconTo(newOwner: Int) {
        ConstraintSet().apply {
            clone(mBinding.rootBottomSheet.root)
            connect(R.id.imageViewAcceptIcon, ConstraintSet.TOP, newOwner, ConstraintSet.TOP)
            connect(R.id.imageViewAcceptIcon, ConstraintSet.BOTTOM, newOwner, ConstraintSet.BOTTOM)
            connect(R.id.imageViewAcceptIcon, ConstraintSet.START, newOwner, ConstraintSet.START)
            connect(R.id.imageViewAcceptIcon, ConstraintSet.END, newOwner, ConstraintSet.END)
            applyTo(mBinding.rootBottomSheet.root)
        }
    }
}