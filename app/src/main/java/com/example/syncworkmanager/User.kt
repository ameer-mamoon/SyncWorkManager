package com.example.syncworkmanager

data class User(
    val id: Int,
    val email: String,
    val password: String,
    val name: String,
    val role: String,
    val avatar: String,
    val creationAt: String,
    val updatedAt: String
)
