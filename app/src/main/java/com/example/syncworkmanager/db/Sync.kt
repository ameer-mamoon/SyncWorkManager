package com.example.syncworkmanager.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sync")
class Sync (
    @PrimaryKey(autoGenerate = true) val id: Int=0,
    val requestedAt:String,
    val updatedAt:String?=null
)