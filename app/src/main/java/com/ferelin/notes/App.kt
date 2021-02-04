package com.ferelin.notes

import android.app.Application
import com.ferelin.notes.di.component.AppComponent
import com.ferelin.notes.di.component.DaggerAppComponent

class App : Application() {

    val appComponent: AppComponent by lazy {
        initializeComponent()
    }

    private fun initializeComponent(): AppComponent {
        return DaggerAppComponent.factory().create(applicationContext)
    }
}