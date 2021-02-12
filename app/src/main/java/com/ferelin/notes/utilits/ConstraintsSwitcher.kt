package com.ferelin.notes.utilits

import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet

object ConstraintsSwitcher {

    fun switchOwner(root: ConstraintLayout, target: Int, newParent: Int) {
        ConstraintSet().apply {
            clone(root)
            connect(target, ConstraintSet.TOP, newParent, ConstraintSet.TOP)
            connect(target, ConstraintSet.BOTTOM, newParent, ConstraintSet.BOTTOM)
            connect(target, ConstraintSet.START, newParent, ConstraintSet.START)
            connect(target, ConstraintSet.END, newParent, ConstraintSet.END)
            applyTo(root)
        }
    }
}