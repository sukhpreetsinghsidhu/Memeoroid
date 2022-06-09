package com.example.memeoroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_surfmemes.*

class SurfMemesActivity : AppCompatActivity() {

    lateinit var memeImageView : ImageView

    var currentImageURL : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_surfmemes)

        memeImageView = findViewById(R.id.memeImageView)

        loadMeme()
        progressBar.visibility = View.INVISIBLE

        nextButton.setOnClickListener{
            loadMeme()
        }

        shareButton.setOnClickListener{
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT,"Checkout this meme $currentImageURL")
            val chooser = Intent.createChooser(intent,"Share this meme using- ")
            startActivity(chooser)
        }

    }

    fun loadMeme(){

        val queue = Volley.newRequestQueue(this)
        val url = "https://meme-api.herokuapp.com/gimme"

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url,null,
            Response.Listener{ response ->
                currentImageURL = response.getString("url")

                Picasso.with(this@SurfMemesActivity).load(currentImageURL).into(memeImageView)
                //Glide.with(this).load(currentImageURL).into(memeImageView)
                //println("in the response listnener")
            },
            Response.ErrorListener {
                Toast.makeText(this,"Something went wrong", Toast.LENGTH_LONG).show()
            })
        queue.add(jsonObjectRequest)
    }
}