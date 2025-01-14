package com.example.syncworkmanager

import AppDatabase
import MyAdapter
import android.content.Context
import android.database.DatabaseUtils
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.Data
import com.example.syncworkmanager.databinding.ActivityMainBinding
import com.example.syncworkmanager.db.Sync
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope.coroutineContext
import kotlinx.coroutines.launch
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.ZoneOffset
import kotlin.coroutines.coroutineContext
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.example.syncworkmanager.Retrofit.RetrofitInstance
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var recyclerView: RecyclerView
    var list:MutableList<User>? = null
    var id = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        recyclerView = binding.recyclerView
        list = mutableListOf()

        val data = User(
            1, "Item 2", "Item 3", "Item 4", "Item 5",
            "Item 6", "Item 7", "Item 8"
        )
        list!!.add(data)



        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val adapter = MyAdapter(list!!)
        recyclerView.adapter = adapter

//        val dao = (application as MyApplication).database.syncDao()
        /*
        val dao = AppDatabase.getDatabase(applicationContext).syncDao()
        val repo = Repository(dao)
        val factory = MainViewModelProvider(repo)

        val viewModel = ViewModelProvider(this, factory).get(MainViewModel::class.java)

        // default
        viewModel.insert(Sync(requestedAt =  getTimeStamp(), updatedAt = null))

        viewModel.allSyncData.observe(this) { syncData ->
            // Looking last row of sync table
            if(syncData[syncData.size-1].updatedAt == null)
            {
                // API call
                CoroutineScope(Dispatchers.IO).launch {
                    val requestedTimeStamp = getTimeStamp()
                    val apiUser:List<User> = viewModel.fetchDataFromApi(id)

                    // updating recycler view via UI thread
                    CoroutineScope(Dispatchers.Main).launch{
                        val adapter = recyclerView.adapter as MyAdapter
                        adapter.updateData(apiUser[0])
                    }

                    viewModel.insert(Sync(id++,requestedTimeStamp, getTimeStamp()))
                }
            }
        }
        */

//        CoroutineScope(Dispatchers.IO).launch {
//            val res = RetrofitInstance.api.fetchSyncData(1)
//            if(res.isSuccessful)
//            {
//                Log.d("Result:Testing","Result:"+res.body().toString())
//            } else
//            {
//                Log.d("Result:Testing","Result:"+res.errorBody().toString())
//            }
//
//        }


        // Work Manager
        scheduleFetchUserWork(applicationContext,recyclerView,1)

    }
}

fun scheduleFetchUserWork(context: Context,recyclerView: RecyclerView,id:Int) {
    val inputData = Data.Builder()
        .putInt("id", id)
        .build()

    val workRequest: WorkRequest = OneTimeWorkRequestBuilder<BackgroundTask>()
        .setInitialDelay(10, TimeUnit.SECONDS) // delay of 10 seconds
        .setInputData(inputData)
        .build()

    WorkManager.getInstance(context).enqueue(workRequest)

    // Observe the work completion and re-schedule with incremented `id`
    WorkManager.getInstance(context).getWorkInfoByIdLiveData(workRequest.id).observeForever { workInfo ->
        if (workInfo != null && workInfo.state == WorkInfo.State.SUCCEEDED) {
            val outputData = workInfo.outputData
            val usersJson = outputData.getString("usersJson")

            if (usersJson != null) {
                // Deserialize JSON back into a list of User objects
                val userType = object : TypeToken<User>() {}.type
                val users: List<User> = Gson().fromJson(usersJson, userType)

                // Update the RecyclerView with the fetched user data
                val adapter = recyclerView.adapter as MyAdapter
                adapter.updateData(users[0])
                Log.d("Result: ","List updated")

            val newId = id + 1
            scheduleFetchUserWork(context,recyclerView,newId) // Schedule the next work with incremented id

        }


        }


    }



}


fun getTimeStamp(): String {
    val currentTime = ZonedDateTime.now(ZoneOffset.UTC)  // Get current time in UTC
    val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")  // Define the time format (24-hour format)
    return currentTime.format(formatter)  // Format and return the time as a string

}


