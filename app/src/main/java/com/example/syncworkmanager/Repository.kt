package com.example.syncworkmanager
import SyncDao
import androidx.lifecycle.LiveData
import com.example.syncworkmanager.Retrofit.RetrofitInstance
import com.example.syncworkmanager.db.Sync

class Repository(private val syncDao: SyncDao) {

    val allSyncData: LiveData<List<Sync>> = syncDao.getAllSyncData()

    suspend fun insert(sync: Sync) {
        syncDao.insert(sync)
    }

    suspend fun fetchDataFromApi(id:Int): User {
        return RetrofitInstance.api.fetchSyncData(id).body()!!
    }
}
