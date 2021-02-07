package com.ferelin.repository.db.room

import androidx.room.*
import com.ferelin.repository.model.Note
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(note: Note)

    @Query("SELECT * FROM Note")
    fun getAll(): Flow<List<Note>>

    @Query("SELECT * FROM Note WHERE id IN (:id)")
    fun get(id: Int): Flow<Note>

    @Update
    fun update(note: Note)

    @Query("DELETE FROM note WHERE id = :id")
    fun delete(id: Int)

    @Delete
    fun delete(note: Note)
}