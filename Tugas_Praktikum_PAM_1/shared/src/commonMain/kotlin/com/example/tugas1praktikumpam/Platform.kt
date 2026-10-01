package com.example.tugas1praktikumpam

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform