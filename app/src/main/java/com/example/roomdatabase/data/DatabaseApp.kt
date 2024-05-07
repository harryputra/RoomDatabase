package com.example.roomdatabase.data

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.MutableLiveData
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.roomdatabase.data.dao.UserDao
import com.example.roomdatabase.data.entity.User

@Database(entities = [User::class], version = 4)
abstract class AppDatabase : RoomDatabase() {

    private val mIsDatabaseCreated = MutableLiveData<Boolean>()

    companion object {
        @Volatile
        private var sInstance: AppDatabase? = null

        @VisibleForTesting
        const val DATABASE_NAME = "my_database1"

        fun getInstance(context: Context): AppDatabase {
            return sInstance ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME
                )
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            val database = getInstance(context)
                            database.setDatabaseCreated()
                        }
                    })
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()
                sInstance = instance
                instance.updateDatabaseCreated(context.applicationContext)
                instance
            }
        }
    }

    abstract fun userDao(): UserDao

    private fun setDatabaseCreated() {
        mIsDatabaseCreated.postValue(true)
    }

    private fun updateDatabaseCreated(context: Context) {
        if (context.getDatabasePath(DATABASE_NAME).exists()) {
            setDatabaseCreated()
        }
    }
}