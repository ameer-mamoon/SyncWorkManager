package com.example.syncworkmanager.Retrofit
import com.example.syncworkmanager.User
import com.example.syncworkmanager.db.Sync
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface SyncApi {

    @GET("users/{id}")
    suspend fun fetchSyncData(@Path("id") id:Int): Response<User>
}
