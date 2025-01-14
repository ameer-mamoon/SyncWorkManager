package com.example.syncworkmanager

import AppDatabase
import android.app.Application

class MyApplication : Application() {
    val database by lazy { AppDatabase.getDatabase(this) }
}