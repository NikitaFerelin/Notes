package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.provider.TestDataProvider
import com.ferelin.repository.db.prefs.AppPreferences
import com.ferelin.repository.db.prefs.PreferencesHelper
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@Config(manifest = Config.NONE)
@RunWith(RobolectricTestRunner::class)
class PreferencesHelperTest {

    private lateinit var mPreferencesHelper: PreferencesHelper

    private val mTestNote = TestDataProvider().defaultNote

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        mPreferencesHelper = AppPreferences(context, "TestNoteAppPreferences")
    }

    @Test
    fun saveAndGetNote(): Unit = runBlocking {
        mPreferencesHelper.saveLastNotePreferences(mTestNote.title, mTestNote.content, mTestNote.color, mTestNote.time)
        mPreferencesHelper.getLastNotePreferences().first().also {
            Assert.assertEquals(mTestNote.title, it[AppPreferences.TITLE_BUNDLE_KEY]!!)
            Assert.assertEquals(mTestNote.content, it[AppPreferences.CONTENT_BUNDLE_KEY]!!)
            Assert.assertEquals(mTestNote.color, it[AppPreferences.COLOR_BUNDLE_KEY]!!)
            Assert.assertEquals(mTestNote.time, it[AppPreferences.TIME_BUNDLE_KEY]!!)
        }
    }

    @Test
    fun getNoteWhenNoSource(): Unit = runBlocking {
        mPreferencesHelper.getLastNotePreferences().first().also {
            Assert.assertEquals("", it[AppPreferences.TITLE_BUNDLE_KEY]!!)
            Assert.assertEquals("", it[AppPreferences.CONTENT_BUNDLE_KEY]!!)
            Assert.assertEquals("", it[AppPreferences.COLOR_BUNDLE_KEY]!!)
            Assert.assertEquals("", it[AppPreferences.TIME_BUNDLE_KEY]!!)
        }
    }

    /*@Test TODO IOException Unable to rename ????
    fun correctClearNote() {
        val note = mTestDataProvider.defaultNote

        mPreferencesHelper.saveLastNotePreferences(note.title, note.content, note.color)
        mPreferencesHelper.clearLastNote()
        mPreferencesHelper.getLastNotePreferences().first().also {
            Assert.assertEquals("", it[AppPreferences.sTitleBundleKey]!!)
            Assert.assertEquals("", it[AppPreferences.sContentBundleKey]!!)
            Assert.assertEquals("", it[AppPreferences.sColorBundleKey]!!)
        }
    }*/
}