package com.ferelin.repository.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.text.SimpleDateFormat
import java.util.*

@Entity
@Parcelize
data class Note(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val content: String,
    val date: String = SimpleDateFormat("d MMM HH:mm", Locale.getDefault()).format(Date()),
    val color: String,
    val preContent: String = if (content.length > 150) "${content.substring(0, 150)}..." else content,
    val time: String,
) : Parcelable