package com.example.memeoroid.roomdb

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

// ViewModel
// Near identical to the repository
// Like the MemeRepo, its job is to separate the data from the UI
class DbViewModel(app: Application) : AndroidViewModel(app) {
    private val repo: MemeRepo
    var allFavorites : LiveData<List<Meme>>
    lateinit var favoriteById : LiveData<Meme>

    init {
        repo = MemeRepo(app)
        allFavorites = repo.selectAllFavorites(10,0)!!
    }

    // Create
    fun insertFavorite(meme: Meme) = viewModelScope.launch {
        repo.insertFavorite(meme)
    }

    // Read
    fun selectAllFavorites(limit: Int,offset: Int) = viewModelScope.launch {
        allFavorites = repo.selectAllFavorites(limit , offset )!!
    }

    fun selectFavorite(key: Int, limit:Int, offset:Int) = viewModelScope.launch {
        favoriteById = repo.selectFavorite(key, limit, offset )!!
    }

    fun search(text: String) = viewModelScope.launch {
        allFavorites = repo.search(text)!!
    }

    // Update
    fun updateFavorite(meme: Meme) = viewModelScope.launch {
        repo.updateFavorite(meme)
    }

    // Delete
    fun deleteFavorite(meme: Meme) = viewModelScope.launch {
        repo.deleteFavorite(meme)
    }

    fun deleteAllFavorites() = viewModelScope.launch {
        repo.deleteAllFavorites()
    }
}