package com.ferelin.notes.utilits

import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

open class CoroutineContextProvider {

    open val MAIN: CoroutineContext
        get() = Dispatchers.Main

    open val IO: CoroutineContext
        get() = Dispatchers.IO

    open val DEFAULT: CoroutineContext
        get() = Dispatchers.Default
}