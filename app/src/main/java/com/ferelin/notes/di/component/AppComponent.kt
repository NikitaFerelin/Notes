package com.ferelin.notes.di.component

import android.content.Context
import com.ferelin.notes.di.module.AppModule
import com.ferelin.notes.ui.create.CreatePresenter
import com.ferelin.notes.ui.details.DetailsPresenter
import com.ferelin.notes.ui.notes.NotesPresenter
import com.ferelin.repository.db.DataManagerHelper
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun dataManager(): DataManagerHelper

    fun createPresenter(): CreatePresenter
    fun detailsPresenter(): DetailsPresenter
    fun notesPresenter(): NotesPresenter
}