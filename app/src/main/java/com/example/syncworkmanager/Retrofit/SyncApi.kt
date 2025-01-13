package com.example.syncworkmanager.Retrofit
import com.example.syncworkmanager.User
import com.example.syncworkmanager.db.Sync
import retrofit2.http.POST
import retrofit2.http.Path

interface SyncApi {
    @POST("{id}")
    suspend fun fetchSyncData(@Path("id")  id:Int): List<User> // Update the return type as per API response
}
