package com.ferelin.providers

import com.ferelin.notes.utilits.CoroutineContextProvider
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

class TestingCoroutineContextProvider : CoroutineContextProvider() {

    override val MAIN: CoroutineContext
        get() = Dispatchers.Unconfined

    override val IO: CoroutineContext
        get() = Dispatchers.Unconfined

    override val DEFAULT: CoroutineContext
        get() = Dispatchers.Unconfined
}