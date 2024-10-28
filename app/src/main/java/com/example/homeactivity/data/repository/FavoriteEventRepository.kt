package com.example.homeactivity.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.homeactivity.data.entity.FavoriteEvent
import com.example.homeactivity.data.room.AppDatabase
import com.example.homeactivity.data.room.FavoriteEventDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FavoriteEventRepository(application: Application) {

    private val mFavoriteEventDao: FavoriteEventDao = AppDatabase.getDatabase(application).FavoriteEventDao()

    fun getAllFavoriteEvents(): LiveData<List<FavoriteEvent>> = mFavoriteEventDao.getAllFavoriteEvents()

    fun getFavoriteEventById(id: String): LiveData<FavoriteEvent> = mFavoriteEventDao.getFavoriteEventById(id)

    // Menggunakan coroutine untuk melakukan operasi database
    suspend fun insert(favoriteEvent: FavoriteEvent) {
        withContext(Dispatchers.IO) {
            mFavoriteEventDao.insert(favoriteEvent)
        }
    }

    suspend fun delete(favoriteEvent: FavoriteEvent) {
        withContext(Dispatchers.IO) {
            mFavoriteEventDao.delete(favoriteEvent)
        }
    }

    suspend fun update(favoriteEvent: FavoriteEvent) {
        withContext(Dispatchers.IO) {
            mFavoriteEventDao.update(favoriteEvent)
        }
    }
}
