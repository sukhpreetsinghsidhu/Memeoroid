package com.example.memeoroid

import android.content.Intent
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.TypedValue
import android.view.Gravity
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.memeoroid.retrofit.ApiViewModel
import com.example.memeoroid.retrofit.RetroApiInterface
import com.example.memeoroid.retrofit.TemplateRepo
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_custom_meme_display.*
import kotlinx.android.synthetic.main.activity_new.*

class CustomMemeDisplayActivity : AppCompatActivity() {

    var currentImageURL : String? = null

    lateinit var topText : String
    lateinit var bottomText : String

    lateinit var vm: ApiViewModel
    var urls: MutableList<String> = ArrayList()

    lateinit var bitmapOriginal : Bitmap

    lateinit var imageView : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_meme_display)

        imageView = findViewById(R.id.imageView)



        val imageSelected = intent.getStringExtra("imageSelected")

        val inter = RetroApiInterface.create()
        val repo = TemplateRepo(inter)
        vm = ApiViewModel(repo)

        vm.getAllTemplates()

        vm.templateList.observe(this) {
            //println(it)
            for (item in it) {
                urls.add(item.image)
            }


            Picasso.with(this@CustomMemeDisplayActivity).load(urls[imageSelected?.toInt()!!]).into(imageView)

            generateMemeText()
        }
        saveButton.setOnClickListener{
            savingImageToGallery()
        }

        shareButton.setOnClickListener{
//            val intent = Intent(Intent.ACTION_SEND)
//            intent.type = "text/plain"
//            intent.putExtra(Intent.EXTRA_TEXT,"Checkout this meme $currentImageURL")
//            val chooser = Intent.createChooser(intent,"Share this meme using- ")
//            startActivity(chooser)
        }

        favoritesButton.setOnClickListener{

        }
    }

    private fun saveFileName(title:String): Uri {
        val bitmap = (imageView.drawable as BitmapDrawable).bitmap
        val savedImageURL = MediaStore.Images.Media.insertImage(contentResolver, bitmap, title,
            "Image of $title")
        return Uri.parse(savedImageURL)
    }

    fun savingImageToGallery(){
        val filename : String = String.format("%d.png",System.currentTimeMillis())
        imageView.buildDrawingCache()
        val uri = saveFileName("$filename")
        imageView.setImageURI(uri)
        Toast.makeText(applicationContext, "File Saved", Toast.LENGTH_LONG).show()
    }

    private fun addBackgroundToImage(firstImage: Bitmap, secondImage: Bitmap): Bitmap{

        val result = Bitmap.createBitmap(secondImage.width, (secondImage.height*2).toInt(), firstImage.config)
        val canvas = Canvas(result)

        canvas.drawBitmap(firstImage, 0f, 0f, null)
        canvas.drawBitmap(secondImage, 0f, ((secondImage.height*2)/4).toFloat(), null)
        return result
    }

    private fun generateMemeImage(){

        val backgroundImage = BitmapFactory.decodeResource(resources, R.drawable.whitebackground)
        val memeImage = (imageView.drawable as BitmapDrawable).bitmap

        bitmapOriginal = addBackgroundToImage(backgroundImage,memeImage)
        imageView.setImageBitmap(bitmapOriginal)
    }

    private fun generateMemeText(){

        generateMemeImage()

        topText = intent.getStringExtra("topText").toString()
        bottomText = intent.getStringExtra("bottomText").toString()

        val fontColor = Color.BLACK
        var fontSize :Float

        if(topText.length<20 && bottomText.length<15){
            fontSize = 100f
        } else
            fontSize = 50f

        val newBitmap = bitmapOriginal.copy(bitmapOriginal.config,true)
        val newCanvas = Canvas(newBitmap)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        val bmHeight= newBitmap.height
        val bmWidth = newBitmap.width

        val xTop = 0
        val yTop = 0

        val xBottom = 0
        val yBottom = bmHeight- (bmHeight/4)

        val left = 0
        val top = 0
        val right = bmWidth
        val bottom = (bmHeight)

        var topTextView = TextView(this)
        topTextView.layout
        topTextView.layout(left,top, right, bottom)
        topTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize)
        topTextView.setTextColor(fontColor)
        topTextView.text = topText
        topTextView.isDrawingCacheEnabled = true
        topTextView.gravity =  Gravity.CENTER_HORIZONTAL

        newCanvas.drawBitmap(topTextView.drawingCache, xTop.toFloat(),yTop.toFloat(),paint)

        var bottomTextView = TextView(this)
        bottomTextView.layout
        bottomTextView.layout(left, top, right, bottom)
        bottomTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize)
        bottomTextView.setTextColor(fontColor)
        bottomTextView.text = bottomText
        bottomTextView.isDrawingCacheEnabled = true
        bottomTextView.gravity= Gravity.CENTER_HORIZONTAL

        newCanvas.drawBitmap(bottomTextView.drawingCache, xBottom.toFloat(),yBottom.toFloat(),paint)

        imageView.setImageBitmap(newBitmap)
    }
}