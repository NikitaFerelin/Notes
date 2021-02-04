package com.ferelin.notes.ui.notes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.core.view.doOnPreDraw
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.transition.*
import com.ferelin.notes.R
import com.ferelin.notes.base.BaseFragment
import com.ferelin.notes.databinding.FragmentNotesBinding
import com.ferelin.repository.model.Note
import com.google.android.material.transition.MaterialElevationScale
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.take
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import moxy.presenter.ProvidePresenterTag

class NotesFragment : BaseFragment(), NotesMvpView, Filterable {

    @InjectPresenter
    lateinit var mPresenter: NotesPresenter

    @ProvidePresenterTag(presenterClass = NotesPresenter::class)
    fun provideDialogPresenterTag(): String = "Notes"

    @ProvidePresenter
    fun provideDialogPresenter() = appComponent.notesPresenter()

    private lateinit var mBinding: FragmentNotesBinding
    private var mAdapter: NotesAdapter? = null
    private var mFilter: NotesFilter? = null

    private var mNotesLoadJob: Job? = null
    private var mLastClickedNote: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupAnims()
        recoverLastClickedItem(savedInstanceState)
        mPresenter.onFragmentCreate()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mBinding = FragmentNotesBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSetUp()
    }

    override fun replaceWithDetailFragment(
        holder: NotesAdapter.NoteViewHolder,
        title: String,
        content: String,
        date: String,
        color: String,
    ) {
        val extras = FragmentNavigatorExtras(
            holder.binding.rootCardView to requireContext().resources.getString(R.string.transitionDetailsFrgCardView)
        )
        val action = NotesFragmentDirections.actionNotesFragmentToDetailsFragment(
            sDeleteNoteResponseKey,
            title,
            content,
            date,
            color,
            holder.binding.rootCardView.transitionName)
        findNavController().navigate(action, extras)
    }

    override fun replaceWithCreateFragment() {
        val action = NotesFragmentDirections.actionNotesFragmentToCreateFragment(sAddNoteResponseKey)
        findNavController().navigate(action)
    }

    override fun setNotes(items: List<Note>) {
        mAdapter!!.apply {
            invalidate()
            setNotes(ArrayList(items))
        }
    }

    override fun addNote(note: Note) {
        mAdapter!!.addNote(note)
    }

    override fun onItemClicked(holder: NotesAdapter.NoteViewHolder, note: Note) {
        mLastClickedNote = holder.adapterPosition
        mPresenter.onNoteClicked(holder, note)
    }

    override fun removeLastClickedNote() {
        mAdapter!!.removeNote(mLastClickedNote)
    }

    override fun getFilter(): Filter {
        return mFilter as Filter
    }

    override fun removeNoteFromFilter(note: Note) {
        mFilter!!.items.remove(note)
    }

    override fun addNoteToFilter(note: Note) {
        mFilter!!.items.add(0, note)
    }

    override fun triggerFilter() {
        mFilter!!.filter(mBinding.editTextSearch.text)
    }

    override fun filter(text: String) {
        mFilter!!.filter(text)
    }

    override fun setUpDetailFrgResultListener() {
        parentFragmentManager.setFragmentResultListener(sDeleteNoteResponseKey, this@NotesFragment) { _, _ ->
            lifecycleScope.launch(Dispatchers.IO) {
                delay(350)
                mPresenter.gotResultFromDetailsFrg(mAdapter!!.notes[mLastClickedNote])
            }
        }
    }

    override fun setUpCreateFrgResultListener() {
        parentFragmentManager.setFragmentResultListener(sAddNoteResponseKey, this@NotesFragment) { _, bundle ->
            lifecycleScope.launch(Dispatchers.IO) {
                delay(350)
                mPresenter.gotResultFromCreateFrg(bundle)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(sLastClickedNoteSavedKey, mLastClickedNote)
    }

    private fun initSetUp() {
        postponeAnim()
        setupRecyclerAdapter()
        setupListeners()
        setupFilter()
    }

    private fun setupAnims() {
        exitTransition = MaterialElevationScale(false).apply {
            duration = 250L
        }
        reenterTransition = MaterialElevationScale(true).apply {
            duration = 300L
        }
    }

    private fun setupRecyclerAdapter() {
        if (mAdapter == null) {
            mAdapter = NotesAdapter(this).apply {
                mNotesLoadJob = lifecycleScope.launch(Dispatchers.IO) {
                    mPresenter.getNotes().take(1).collect {
                        withContext(Dispatchers.Main) {
                            setNotes(ArrayList(it))
                        }
                    }
                }
                setHasStableIds(true)
            }
        }
        mBinding.recyclerView.adapter = mAdapter
    }

    private fun setupFilter() {
        if (mFilter == null) {
            lifecycleScope.launch(Dispatchers.IO) {
                mNotesLoadJob?.join()
                withContext(Dispatchers.Main) {
                    mFilter = NotesFilter(mAdapter!!.notes.toList(), onResultsPublished = {
                        mPresenter.onResultsPublished(it)
                    })
                    setupFilterListener()
                    mPresenter.onFilterInitialized(mBinding.editTextSearch.text)
                }
            }
        } else setupFilterListener()
    }

    private fun setupListeners() {
        mBinding.fab.setOnClickListener {
            mPresenter.onFabClicked()
        }
    }

    private fun setupFilterListener() {
        mBinding.editTextSearch.addTextChangedListener {
            mPresenter.onSearchTextChanged(isFieldFocused = mBinding.editTextSearch.isFocused, it)
        }
    }

    private fun postponeAnim() {
        postponeEnterTransition()
        view?.doOnPreDraw { startPostponedEnterTransition() }
    }

    private fun recoverLastClickedItem(savedInstance: Bundle?) {
        if (savedInstance != null) {
            val lastClickedNote = savedInstance[sLastClickedNoteSavedKey] as Int
            mLastClickedNote = lastClickedNote
        }
    }

    companion object {
        private const val sLastClickedNoteSavedKey = "RECOVER_LAST_CLICKED_NOTE_FROM_SAVED_INSTANCE"
        private const val sAddNoteResponseKey = "NOTES_ADD_RESPONSE_KEY_BUNDLE"
        private const val sDeleteNoteResponseKey = "NOTES_DELETE_RESPONSE_KEY_BUNDLE"
    }
}