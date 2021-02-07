package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
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

    private val mTestTitle = "title"
    private val mTestContent = "content"
    private val mTestColor = "#000000"

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        mPreferencesHelper = AppPreferences(context, "TestNoteAppPreferences")
    }

    @Test
    fun correctSaveNote() {
        runBlocking {
            mPreferencesHelper.saveLastNotePreferences(mTestTitle, mTestContent, mTestColor)
            mPreferencesHelper.getLastNotePreferences().first().also {
                Assert.assertEquals(mTestTitle, it[AppPreferences.sTitleBundleKey]!!)
                Assert.assertEquals(mTestContent, it[AppPreferences.sContentBundleKey]!!)
                Assert.assertEquals(mTestColor, it[AppPreferences.sColorBundleKey]!!)
            }
        }
    }

    @Test
    fun getNoteWhenNoData() {
        runBlocking {
            mPreferencesHelper.getLastNotePreferences().first().also {
                Assert.assertEquals("", it[AppPreferences.sTitleBundleKey]!!)
                Assert.assertEquals("", it[AppPreferences.sContentBundleKey]!!)
                Assert.assertEquals("", it[AppPreferences.sColorBundleKey]!!)
            }
        }
    }

    /*@Test TODO IOException: Unable to rename
    fun correctClearNote() {
        runBlocking {
            mPreferencesHelper.saveLastNotePreferences(mTestTitle, mTestContent, mTestColor)
            mPreferencesHelper.clearLastNote()
            mPreferencesHelper.getLastNotePreferences().first().also {
                Assert.assertEquals("", it[AppPreferences.sTitleBundleKey]!!)
                Assert.assertEquals("", it[AppPreferences.sContentBundleKey]!!)
                Assert.assertEquals("", it[AppPreferences.sColorBundleKey]!!)
            }
        }
    }*/
}