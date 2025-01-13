import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.syncworkmanager.db.Sync

@Dao
interface SyncDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(sync: Sync)

    @Query("SELECT * FROM sync")
    fun getAllSyncData(): LiveData<List<Sync>>
}
