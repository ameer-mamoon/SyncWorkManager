package com.example.syncworkmanager

import android.content.Context
import androidx.lifecycle.*
import com.example.syncworkmanager.db.Sync
import kotlinx.coroutines.launch

class MainViewModel(private val repository: Repository) : ViewModel() {

    val allSyncData: LiveData<List<Sync>>

    init {
        allSyncData = repository.allSyncData
    }

    fun insert(sync: Sync) = viewModelScope.launch {
        repository.insert(sync)
    }

    suspend fun fetchDataFromApi(id:Int):List<User> {
         return repository.fetchDataFromApi(id)
    }

}
