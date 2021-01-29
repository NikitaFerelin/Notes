package com.ferelin.notes.ui.notes

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.ImageViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.ferelin.notes.databinding.ItemNoteBinding
import com.ferelin.notes.utilits.ColorTransformer
import com.ferelin.repository.model.Note

class NotesAdapter(private val mClickListener: AdaptiveClickListener) : RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {

    private var mNotes = ArrayList<Note>()
    val notes: ArrayList<Note>
        get() = mNotes

    private var mLastClickedPosition = -1
    val lastClickedPosition: Int
        get() = mLastClickedPosition

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.apply {
            bind(mNotes[position])
            binding.root.setOnClickListener {
                mLastClickedPosition = position
                mClickListener.onItemClicked(holder, position)
            }
        }
    }

    override fun getItemCount(): Int {
        return mNotes.size
    }

    fun setNotes(notes: ArrayList<Note>) {
        mNotes = notes
        notifyDataSetChanged()
    }

    fun addNote(note: Note) {
        mNotes.add(0, note)
        notifyItemInserted(0)
    }

    fun removeNote(position: Int = mLastClickedPosition) {
        mNotes.removeAt(position)
        notifyItemRemoved(position)
    }

    class NoteViewHolder private constructor(val binding: ItemNoteBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Note) {
            with(binding) {
                textViewContent.text = item.preContent
                textViewTitle.text = item.title
                textViewDate.text = item.date
                textViewContent.transitionName = "content_${item.id}"
                textViewTitle.transitionName = "title_${item.id}"
                textViewDate.transitionName = "date_${item.id}"
                val adaptiveColor = ColorTransformer.fromStringToInt(item.color)
                ImageViewCompat.setImageTintList(imageViewColorIndicator, ColorStateList.valueOf(adaptiveColor))
            }
        }

        companion object {
            fun from(parent: ViewGroup): NoteViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = ItemNoteBinding.inflate(inflater, parent, false)
                return NoteViewHolder(binding)
            }
        }
    }
}