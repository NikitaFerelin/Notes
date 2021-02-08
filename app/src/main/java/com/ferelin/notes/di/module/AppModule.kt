package com.ferelin.notes.di.module

import android.content.Context
import com.ferelin.notes.ui.create.CreatePresenter
import com.ferelin.notes.ui.details.DetailsPresenter
import com.ferelin.notes.ui.notes.NotesPresenter
import com.ferelin.notes.utilits.CoroutineContextProvider
import com.ferelin.repository.db.AppDataManager
import com.ferelin.repository.db.DataManagerHelper
import com.ferelin.repository.db.prefs.AppPreferences
import com.ferelin.repository.db.prefs.PreferencesHelper
import com.ferelin.repository.db.room.AppNotesDb
import com.ferelin.repository.db.room.NotesDb
import com.ferelin.repository.db.room.NotesDbHelper
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    @Singleton
    fun provideDataManagerHelper(appDataManager: AppDataManager): DataManagerHelper {
        return appDataManager
    }

    @Provides
    fun providePreferencesHelper(appPreferences: AppPreferences): PreferencesHelper {
        return appPreferences
    }

    @Provides
    fun provideNotesDbHelper(appNotesDb: AppNotesDb): NotesDbHelper {
        return appNotesDb
    }

    @Provides
    fun provideNotesDb(context: Context): NotesDb {
        return NotesDb.getDatabase(context)
    }

    @Provides
    fun provideDataStoreName(): String {
        return "NoteAppPreferences"
    }

    @Provides
    fun provideCoroutineContext(): CoroutineContextProvider {
        return CoroutineContextProvider()
    }

    // PRESENTERS

    @Provides
    fun provideCreatePresenter(
        context: Context,
        appDataManager: AppDataManager,
        coroutineProvider: CoroutineContextProvider,
    ): CreatePresenter {
        return CreatePresenter(context, appDataManager, coroutineProvider)
    }

    @Provides
    fun provideDetailsPresenter(context: Context): DetailsPresenter {
        return DetailsPresenter(context)
    }

    @Provides
    fun provideNotesPresenter(
        appDataManager: AppDataManager,
        coroutineContextProvider: CoroutineContextProvider,
    ): NotesPresenter {
        return NotesPresenter(appDataManager, coroutineContextProvider)
    }
}