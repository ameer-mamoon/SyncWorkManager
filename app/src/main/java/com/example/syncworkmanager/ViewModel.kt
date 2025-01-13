package com.example.syncworkmanager

import android.app.Application
import androidx.lifecycle.*
import com.example.syncworkmanager.db.Sync
import kotlinx.coroutines.launch

class SyncViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: SyncRepository
    val allSyncData: LiveData<List<Sync>>

    init {
        val syncDao = AppDatabase.getDatabase(application).syncDao()
        repository = SyncRepository(syncDao)
        allSyncData = repository.allSyncData
    }

    fun insert(sync: Sync) = viewModelScope.launch {
        repository.insert(sync)
    }

    fun fetchDataFromApi(id:Int) = viewModelScope.launch {
            val data = repository.fetchDataFromApi(id)
    }
}
