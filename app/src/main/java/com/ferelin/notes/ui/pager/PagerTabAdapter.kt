package com.ferelin.notes.ui.pager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ferelin.notes.ui.notes.NotesFragment
import com.ferelin.notes.ui.settings.SettingsFragment

class PagerTabAdapter(fm: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fm, lifecycle) {

    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> NotesFragment()
            1 -> SettingsFragment()
            else -> throw NoSuchElementException("No fragment for view pager position $position")
        }
    }
}