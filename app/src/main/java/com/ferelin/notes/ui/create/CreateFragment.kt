package com.ferelin.notes.ui.create

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.transition.Slide
import com.ferelin.notes.R
import com.ferelin.notes.base.BaseFragment
import com.ferelin.notes.databinding.FragmentCreateNoteBinding
import com.ferelin.notes.utilits.ConstraintsSwitcher
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.transition.MaterialContainerTransform
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import moxy.presenter.ProvidePresenterTag

class CreateFragment : BaseFragment(), CreateMvpView, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    @InjectPresenter
    lateinit var mPresenter: CreatePresenter

    @ProvidePresenterTag(presenterClass = CreatePresenter::class)
    fun provideDialogPresenterTag(): String = "Create"

    @ProvidePresenter
    fun provideDialogPresenter() = appComponent.createPresenter()

    private lateinit var mBinding: FragmentCreateNoteBinding
    private val mArguments: CreateFragmentArgs by navArgs()

    private lateinit var mBottomSheetBehavior: BottomSheetBehavior<ViewGroup>
    private var mIsAcceptBtnLocked = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mBinding = FragmentCreateNoteBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSetUp()
        mPresenter.onViewPrepared(mArguments)
    }

    override fun setResult(arg: Bundle, responseKey: String) {
        setFragmentResult(responseKey, arg)
    }

    override fun setNote(title: String, content: String) {
        mBinding.apply {
            editTextTitle.setText(title)
            editTextContent.setText(content)
            editTextContent.setSelection(editTextContent.text.length)
        }
    }

    override fun setFocusToContentEdit() {
        mBinding.editTextContent.requestFocus()
    }

    override fun setSelectedColor(color: Int) {
        mBinding.viewColorIndicator.setBackgroundColor(color)
    }

    override fun setReminderTime(time: String) {
        mBinding.textViewReminderTime.text = time
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
        findNavController().popBackStack()
    }

    override fun changeIconConstraintsToDefault() {
        switchBottomSheetIconTo(R.id.viewSelectableDefault)
    }

    override fun changeIconConstraintsToRed() {
        switchBottomSheetIconTo(R.id.viewSelectableRed)
    }

    override fun changeIconConstraintsToGray() {
        switchBottomSheetIconTo(R.id.viewSelectableGray)
    }

    override fun changeIconConstraintsToOrange() {
        switchBottomSheetIconTo(R.id.viewSelectableOrange)
    }

    override fun hideKeyboard() {
        super.hideKeyboard(mBinding.editTextContent)
    }

    override fun showKeyboard() {
        super.showKeyboard(mBinding.editTextContent)
    }

    override fun showDatePickerDialog(year: Int, month: Int, day: Int) {
        DatePickerDialog(requireContext(), R.style.dialogPickers, this, year, month, day).show()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        mPresenter.gotResultFromDatePicker(year, month, dayOfMonth)
    }

    override fun showTimePickerDialog(hour: Int, minute: Int) {
        TimePickerDialog(requireContext(), R.style.dialogPickers, this, hour, minute, true).show()
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        mPresenter.gotResultFromTimePicker(hourOfDay, minute)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        lifecycleScope.launch(Dispatchers.IO) {
            mPresenter.onSaveInstanceState(mBinding.editTextTitle.text,
                mBinding.editTextContent.text,
                mBinding.textViewReminderTime.text)
        }
    }

    private fun initSetUp() {
        setUpTransitions()
        setUpClickListeners()
        setUpBottomSheet()
    }

    private fun setUpTransitions() {
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

    private fun setUpClickListeners() {
        mBinding.apply {
            editTextTitle.addTextChangedListener {
                mPresenter.onTextChanged(editTextContent.text, it, mIsAcceptBtnLocked)
            }
            editTextContent.addTextChangedListener {
                mPresenter.onTextChanged(it, editTextTitle.text, mIsAcceptBtnLocked)
            }
            imageViewAccept.setOnClickListener {
                lifecycleScope.launch(Dispatchers.Main) {
                    mPresenter.onAcceptBtnClicked(mIsAcceptBtnLocked, editTextTitle.text, editTextContent.text)
                }
            }
            imageViewButtonBack.setOnClickListener {
                mPresenter.onBackBtnClicked()
            }
            rootTimePicker.setOnClickListener {
                mPresenter.onAddReminderClicked()
            }
        }
    }

    private fun setUpBottomSheet() {
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
        ConstraintsSwitcher.switchOwner(mBinding.rootBottomSheet.root, R.id.imageViewAcceptIcon, newOwner)
    }
}