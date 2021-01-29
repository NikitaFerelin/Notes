package com.ferelin.notes.ui.notes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.transition.*
import com.ferelin.notes.base.BaseFragment
import com.ferelin.notes.databinding.FragmentNotesBinding
import com.ferelin.notes.ui.pager.PagerFragmentDirections
import com.ferelin.repository.model.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NotesFragment : BaseFragment(), NotesMvpView {

    private lateinit var mPresenter: NotesPresenter<NotesMvpView>
    private lateinit var mBinding: FragmentNotesBinding

    private var mAdapter: NotesAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = FragmentNotesBinding.inflate(inflater, container, false)
        mPresenter = NotesPresenter<NotesMvpView>(requireContext()).apply {
            attachView(this@NotesFragment)
        }

        return mBinding.root
    }

    override fun setUp(view: View) {
        setupAdapter()

        mBinding.fab.setOnClickListener {
            mPresenter.onFabClicked()
        }
    }

    override fun getNote(position: Int): Note {
        return mAdapter!!.notes[position]
    }

    override fun onItemClicked(holder: NotesAdapter.NoteViewHolder, position: Int) {
        mPresenter.onNoteClicked(holder, position)
    }

    override fun moveToNoteInfo(
        holder: NotesAdapter.NoteViewHolder,
        title: String,
        content: String,
        date: String,
        color: String,
    ) {
        val action =
            PagerFragmentDirections.actionPagerFragmentToDetailsFragment(sDeleteNoteResponseKey, title, content, date, color)
        requireView().findNavController().navigate(action)

        requireParentFragment()
            .parentFragmentManager
            .setFragmentResultListener(sDeleteNoteResponseKey, this@NotesFragment) { _, _ ->
                lifecycleScope.launch(Dispatchers.IO) {
                    mPresenter.gotResultFromDetails(mAdapter!!.notes[mAdapter!!.lastClickedPosition])
                }
            }
    }

    override fun moveToCreateNote() {
        val action = PagerFragmentDirections.actionPagerFragmentToCreateFragment(sAddNoteResponseKey)
        requireView().findNavController().navigate(action)

        requireParentFragment().parentFragmentManager.setFragmentResultListener(sAddNoteResponseKey,
            this@NotesFragment) { _, bundle ->
            lifecycleScope.launch(Dispatchers.IO) {
                mPresenter.gotResultFromCreate(bundle)
            }
        }
    }

    override fun addNote(note: Note) {
        mAdapter!!.addNote(note)
    }

    override fun removeLastClickedNote() {
        mAdapter!!.removeNote()
    }

    override fun onDestroyView() {
        mPresenter.detachView()
        super.onDestroyView()
    }

    private fun setupAdapter() {
        if (mAdapter == null) {
            mAdapter = NotesAdapter(this).apply {
                lifecycleScope.launch(Dispatchers.IO) {
                    mPresenter.getNotes().collect {
                        withContext(Dispatchers.Main) {
                            setNotes(ArrayList(it))
                        }
                    }
                }
            }
        }
        mBinding.recyclerView.adapter = mAdapter
    }

    companion object {
        private const val sAddNoteResponseKey = "NOTES_ADD_RESPONSE_KEY_BUNDLE"
        private const val sDeleteNoteResponseKey = "NOTES_DELETE_RESPONSE_KEY_BUNDLE"
    }
}