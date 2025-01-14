package com.example.syncworkmanager

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import com.example.syncworkmanager.Retrofit.RetrofitInstance
import com.google.gson.Gson
import retrofit2.HttpException
import retrofit2.Response

class BackgroundTask(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {


    override suspend fun doWork(): Result {
        return try {
            val userId = inputData.getInt("id", 1) // Default value 1 if no 'id' provided

            val response = RetrofitInstance.api.fetchSyncData(userId)

            if(response!!.isSuccessful && response.body() != null && userId <= 8)
            {
                Log.d("Result: response",response.body().toString())
                    // Serialize user list to JSON
                    val user = response.body()
                    val usersJson = Gson().toJson(user)

                    // Pass the JSON string as output
                    val outputData = Data.Builder()
                        .putString("usersJson", usersJson)
                        .putInt("id",userId+1)
                        .build()

                    Result.success(outputData)

            }
            else
            {
                Log.d("Result: Error", "Failed to fetch user: ${response?.errorBody()?.string()}")
                Result.retry()
            }

        } catch (e: HttpException) {
            Result.retry()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}
