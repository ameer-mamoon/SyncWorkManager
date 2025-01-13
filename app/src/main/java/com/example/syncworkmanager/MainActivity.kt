package com.example.syncworkmanager

import MyAdapter
import android.database.DatabaseUtils
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

        val dao = AppDatabase.getDatabase(applicationContext).syncDao()
        val repo = Repository(dao)
        val factory = MainViewModelProvider(repo)

        val viewModel = ViewModelProvider(this, factory).get(MainViewModel::class.java)

        // default
        viewModel.insert(Sync(id++, getTimeStamp(),null))

        viewModel.allSyncData.observe(this) { syncList ->
            if(syncList[0].updatedAt == null)
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




    }
}

fun getTimeStamp(): String {
    val currentTime = ZonedDateTime.now(ZoneOffset.UTC)  // Get current time in UTC
    val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")  // Define the time format (24-hour format)
    return currentTime.format(formatter)  // Format and return the time as a string

}


