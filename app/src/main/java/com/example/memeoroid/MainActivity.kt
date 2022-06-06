package com.example.memeoroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var testString: String

    private val viewModel : TestViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // for testing purposes, go right to the Favorite Activity from Main
        var intent = Intent(this, FavoritesActivity::class.java)
        startActivity(intent)

        Log.d("MainActivity", "Test String from MainActivity: $testString")
        viewModel
    }
}