package com.example.tugaspraktikum2_pam

import kotlinx.coroutines.CoroutineDispatcher

interface Platform { val name: String }
expect fun getPlatform(): Platform
expect val ioDispatcher: CoroutineDispatcher
