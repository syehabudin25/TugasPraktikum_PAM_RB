package com.example.tugaspraktikum2_pam

import android.os.Build

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()
actual val ioDispatcher: kotlinx.coroutines.CoroutineDispatcher
    get() = kotlinx.coroutines.Dispatchers.IO
