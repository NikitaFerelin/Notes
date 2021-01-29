package com.ferelin.repository.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.*

@Entity
data class Note(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo val title: String,
    @ColumnInfo val content: String,
    @ColumnInfo val date: String = SimpleDateFormat("d MMM HH:mm", Locale.getDefault()).format(Date()),
    @ColumnInfo val color: String,
    val preContent: String = if (content.length > 250) content.substring(0, 250) else content,
)