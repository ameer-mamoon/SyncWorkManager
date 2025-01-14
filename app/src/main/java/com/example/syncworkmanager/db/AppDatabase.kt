import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.syncworkmanager.db.Sync

@Database(entities = [Sync::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun syncDao(): SyncDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                try
                {
                    val instance = Room.databaseBuilder(
                        context,
                        AppDatabase::class.java,
                        "SyncDatabase"
                    ).build()
                    INSTANCE = instance
                    instance
                } catch (e:Exception)
                {
                    Log.e("DatabaseError", "Error initializing database: ${e.message}", e)
                    throw e // Re-throw after logging to avoid masking critical issues
                }

            }
        }
    }
}
