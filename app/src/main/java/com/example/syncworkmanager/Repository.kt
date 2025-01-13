package com.example.syncworkmanager
import SyncDao
import androidx.lifecycle.LiveData
import com.example.syncworkmanager.Retrofit.RetrofitInstance
import com.example.syncworkmanager.db.Sync

class SyncRepository(private val syncDao: SyncDao) {

    val allSyncData: LiveData<List<Sync>> = syncDao.getAllSyncData()

    suspend fun insert(sync: Sync) {
        syncDao.insert(sync)
    }

    suspend fun fetchDataFromApi(id:Int): List<User> {
        return RetrofitInstance.api.fetchSyncData(id)
    }
}
