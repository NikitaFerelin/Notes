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
import com.ferelin.notes.base.BaseFragment
import com.ferelin.notes.databinding.FragmentNotesBinding
import com.ferelin.repository.model.Note
import com.google.android.material.transition.MaterialElevationScale
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect

class NotesFragment : BaseFragment(), NotesMvpView, Filterable {

    private lateinit var mBinding: FragmentNotesBinding

    private var mPresenter: NotesPresenter<NotesMvpView>? = null
    private var mAdapter: NotesAdapter? = null
    private var mFilter: NotesFilter? = null

    private var mNotesLoadJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupAnims()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = FragmentNotesBinding.inflate(inflater, container, false)
        if (mPresenter == null) {
            mPresenter = NotesPresenter(requireContext())
        }
        mPresenter!!.attachView(this)

        return mBinding.root
    }

    override fun setUp(view: View) {
        postponeAnim()
        setupRecyclerAdapter()
        setupListeners()
        setupFilter()
    }

    override fun replaceWithDetailFragment(
        holder: NotesAdapter.NoteViewHolder,
        title: String,
        content: String,
        date: String,
        color: String,
    ) {
        val extras = FragmentNavigatorExtras(holder.binding.rootCardView to "cardViewDetailsTransitionName")
        val action = NotesFragmentDirections.actionNotesFragmentToDetailsFragment(
            sDeleteNoteResponseKey,
            title,
            content,
            date,
            color,
            holder.binding.rootCardView.transitionName)

        findNavController().navigate(action, extras)
        setupDetailFrgResultListener()
    }

    override fun replaceWithCreateFragment() {
        val action = NotesFragmentDirections.actionNotesFragmentToCreateFragment(sAddNoteResponseKey)
        findNavController().navigate(action)
        setupCreateFrgResultListener()
    }

    override fun addNote(note: Note) {
        mAdapter!!.addNote(note)
    }

    override fun getNote(position: Int): Note {
        return mAdapter!!.notes[position]
    }

    override fun onItemClicked(holder: NotesAdapter.NoteViewHolder, position: Int) {
        mPresenter!!.onNoteClicked(holder, position)
    }

    override fun removeLastClickedNote() {
        mAdapter!!.removeNote()
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

    override fun onDestroyView() {
        mPresenter!!.detachView()
        super.onDestroyView()
    }

    private fun setupDetailFrgResultListener() {
        parentFragmentManager.setFragmentResultListener(sDeleteNoteResponseKey, this@NotesFragment) { _, _ ->
            lifecycleScope.launch(Dispatchers.IO) {
                delay(350)
                mPresenter!!.gotResultFromDetails(mAdapter!!.notes[mAdapter!!.lastClickedPosition])
            }
        }
    }

    private fun setupCreateFrgResultListener() {
        parentFragmentManager.setFragmentResultListener(sAddNoteResponseKey, this@NotesFragment) { _, bundle ->
            lifecycleScope.launch(Dispatchers.IO) {
                delay(350)
                mPresenter!!.gotResultFromCreate(bundle)
            }
        }
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
                    mPresenter!!.getNotes().collect {
                        withContext(Dispatchers.Main) {
                            setNotes(ArrayList(it))
                            cancel()
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
                        mAdapter!!.invalidate()
                        mAdapter!!.setNotes(ArrayList(it))
                    })
                    setupFilterListener()
                }
            }
        } else setupFilterListener()
    }

    private fun setupListeners() {
        mBinding.apply {
            fab.setOnClickListener {
                mPresenter!!.onFabClicked()
            }

            imageViewCloseSearch.setOnClickListener {
                // TODO ANIM
                /*mAdapter!!.setNotes(ArrayList(mPresenter!!.originalNotes))
                mBinding.editTextSearch.setText("")
                hideKeyboard()*/
            }
        }
    }

    private fun setupFilterListener() {
        mBinding.editTextSearch.addTextChangedListener {
            if (mBinding.editTextSearch.isFocused) {
                mFilter!!.onTextChanged(it.toString())
            }
        }
    }

    private fun postponeAnim() {
        postponeEnterTransition()
        view?.doOnPreDraw { startPostponedEnterTransition() }
    }

    companion object {
        private const val sAddNoteResponseKey = "NOTES_ADD_RESPONSE_KEY_BUNDLE"
        private const val sDeleteNoteResponseKey = "NOTES_DELETE_RESPONSE_KEY_BUNDLE"
    }
}