package com.ferelin.notes.di.module

import android.content.Context
import com.ferelin.notes.ui.create.CreatePresenter
import com.ferelin.notes.ui.details.DetailsPresenter
import com.ferelin.notes.ui.notes.NotesPresenter
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


    // PRESENTERS

    @Provides
    fun provideCreatePresenter(context: Context, appDataManager: AppDataManager): CreatePresenter {
        return CreatePresenter(context, appDataManager)
    }

    @Provides
    fun provideDetailsPresenter(context: Context): DetailsPresenter {
        return DetailsPresenter(context)
    }

    @Provides
    fun provideNotesPresenter(appDataManager: AppDataManager): NotesPresenter {
        return NotesPresenter(appDataManager)
    }
}