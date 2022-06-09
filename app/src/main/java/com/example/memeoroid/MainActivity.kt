package com.example.memeoroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        createMeme.setOnClickListener(){
            var intent = Intent(this, NewActivity::class.java)
            startActivity(intent)
        }

        favorites.setOnClickListener(){
            var intent = Intent(this, FavoritesActivity::class.java)
            startActivity(intent)
        }

        surfMeme.setOnClickListener(){
            var intent = Intent(this, SurfMemesActivity::class.java)
            startActivity(intent)
        }
    }
}