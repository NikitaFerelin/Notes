package com.ferelin.repository.db.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ferelin.repository.model.Note

@Database(entities = [Note::class], version = 1)
abstract class NotesDb : RoomDatabase() {

    abstract fun noteDao(): NoteDao

    companion object {
        private var mInstance: NotesDb? = null

        fun getDatabase(context: Context): NotesDb {
            if (mInstance == null) {
                mInstance = Room.databaseBuilder(context.applicationContext,
                    NotesDb::class.java,
                    "notes.note.db")
                    .build()
            }
            return mInstance!!
        }
    }
}