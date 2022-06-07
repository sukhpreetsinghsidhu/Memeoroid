package com.example.memeoroid.roomdb

import android.content.Context
import androidx.lifecycle.LiveData

// DB Data Repository
// Near identical to the DAO
// Like the DbViewModel, its job is to separate the data from the UI
class MemeRepo(context: Context) {
    var db:MemeDao? = AppDatabase.getInstance(context)?.memeDao()

    // Create
    fun insertFavorite(meme: Meme) {
        db?.insertFavorite(meme)
    }

    // Read
    fun selectAllFavorites(limit: Int, offset: Int) : LiveData<List<Meme>>? {
        return db?.selectAllFavorites(limit, offset)
    }

    fun selectFavorite(key: Int, limit :Int, offset: Int): LiveData<Meme>? {
        return db?.selectFavorite(key, limit , offset)
    }

    fun search(text: String, limit:Int, offset : Int) : LiveData<List<Meme>>? {
        return db?.search("%" + text + "%", limit, offset )
    }

    // Update
    fun updateFavorite(meme: Meme) {
        db?.updateFavorite(meme)
    }

    // Delete
    fun deleteFavorite(meme: Meme) {
        db?.deleteFavorite(meme)
    }

    fun deleteAllFavorites() {
        db?.deleteAllFavorites()
    }
}