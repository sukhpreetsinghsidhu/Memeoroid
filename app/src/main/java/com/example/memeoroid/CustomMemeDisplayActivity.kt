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
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.memeoroid.retrofit.ApiViewModel
import com.example.memeoroid.retrofit.RetroApiInterface
import com.example.memeoroid.retrofit.TemplateRepo
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_custom_meme_display.*

//This activity page displays the curated meme from the new meme page
//Top and bottom text are fetched along with the selected image id
//The custom meme is generated by adding a white background to the image
//Then the text is added to the new image
class CustomMemeDisplayActivity : AppCompatActivity() {

    //variables to fetch the user input values to add to the image
    private lateinit var topText : String
    private lateinit var bottomText : String

    //variable to access the api to fetch the selected image
    private lateinit var vm: ApiViewModel
    private var urls: MutableList<String> = ArrayList()

    //the app uses bitmaps to generate the custom memes
    //bitmaps - a digital image composed of a matrix of dots
    //its a map/board/blank canvas where you can draw on to
    //the app uses it to display an image with an background and
    // add text to the top and bottom of the image
    private lateinit var bitmapOriginal : Bitmap

    //to add the image/bitmap image/custom meme for display
    private lateinit var imageView : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_meme_display)

        //Hides the app title and the system notifications top bar
        supportActionBar?.hide()
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        //Floating button redirects to the home page
        val floatingBtn : FloatingActionButton = findViewById(R.id.floatingBtn)

        imageView = findViewById(R.id.imageView)

        //assigning the selected image id from the previous page to a var to fetch from api json object
        val imageSelected = intent.getStringExtra("imageSelected")

        //variable to access the api json object to fetch the image
        val inter = RetroApiInterface.create()
        val repo = TemplateRepo(inter)
        vm = ApiViewModel(repo)

        vm.getAllTemplates()

        vm.templateList.observe(this) { for (item in it) { urls.add(item.image) }

            //using picasso to load image from the api into the image view component
            //picasso is a library used to load images from an external source
            Picasso.with(this@CustomMemeDisplayActivity).load(urls[imageSelected?.toInt()!!]).into(imageView)

            //generating a meme to be displayed
            generateMemeText()
        }

        //saves the displayed image into the gallery
        saveButton.setOnClickListener{
            savingImageToGallery()
            Toast.makeText(applicationContext,"Meme saved to Gallery", Toast.LENGTH_LONG).show()
        }

        //redirect back to create meme page                    ******************** need to rename the button id in the xml
        favoritesButton.setOnClickListener{
            val intent = Intent(this, NewActivity::class.java)
            startActivity(intent)
            Toast.makeText(applicationContext,"Redirecting Back to Create Meme", Toast.LENGTH_LONG).show()
        }

        floatingBtn.setOnClickListener{
            val myIntent = Intent(this, MainActivity::class.java)
            startActivity(myIntent)
            Toast.makeText(applicationContext,"Redirecting to Home Page", Toast.LENGTH_LONG).show()
        }
    }

    //generate the file name to be used to save image to gallery
    private fun saveFileName(title:String): Uri {
        val bitmap = (imageView.drawable as BitmapDrawable).bitmap
        val savedImageURL = MediaStore.Images.Media.insertImage(contentResolver, bitmap, title,
            "Image of $title")
        return Uri.parse(savedImageURL)
    }

    //save the generated meme to the phone gallery
    private fun savingImageToGallery(){
        val filename : String = String.format("%d.png",System.currentTimeMillis())
        imageView.buildDrawingCache()
        val uri = saveFileName(filename)
        imageView.setImageURI(uri)
        Toast.makeText(applicationContext, "File Saved", Toast.LENGTH_LONG).show()
    }

    //creates a bitmap to add two images the meme and a background image
    private fun addBackgroundToImage(firstImage: Bitmap, secondImage: Bitmap): Bitmap{

        //bitmap width will be the width of the selected meme since no space required on the sides
        //height will be twice the height of the selected meme to ensure enough space to add text
        //and to maintain new image's proportion and aspect ratio
        val result = Bitmap.createBitmap(secondImage.width, (secondImage.height*2), firstImage.config)
        val canvas = Canvas(result)

        //position the background and the meme image onto the background
        // and leave some space on top and bottom
        //meme placed in the middle of the background to ensure equal space on top and bottom
        canvas.drawBitmap(firstImage, 0f, 0f, null)
        canvas.drawBitmap(secondImage, 0f, ((secondImage.height*2)/4).toFloat(), null)
        return result
    }

    //Adds a white background to the selected image
    private fun generateMemeImage(){

        //fetch the white background image from the project resource folder
        val backgroundImage = BitmapFactory.decodeResource(resources, R.drawable.whitebackground)
        val memeImage = (imageView.drawable as BitmapDrawable).bitmap

        //calling the above function and passing the background image and the selected meme
        bitmapOriginal = addBackgroundToImage(backgroundImage,memeImage)

        //retrieving the new created image with the white background
        // and assigning it to the imageview component
        imageView.setImageBitmap(bitmapOriginal)
    }

    private fun generateMemeText(){

        //calling the above function to create a new image with a white background
        // and selected image on top of it
        generateMemeImage()

        //fetching the user input text from the previous activity page
        topText = intent.getStringExtra("topText").toString()
        bottomText = intent.getStringExtra("bottomText").toString()

        //assigning the meme text a font color
        val fontColor = Color.BLACK

        //assigning the meme text a font size
        //if less than 20 chars input is given the text size will be bigger size 100f
        //if more than 20 chars input is given the text size will be smaller in size 50f
        val fontSize :Float = if(topText.length<20 && bottomText.length<15){
            100f
        } else
            50f

        //fetch the new created bitmap image with the background
        // which is assigned to the imageview currently
        val newBitmap = bitmapOriginal.copy(bitmapOriginal.config,true)

        //creating a canvas class object and giving it the bitmap image
        //canvas because it allows us to draw on to a bitmap image
        val newCanvas = Canvas(newBitmap)

        //creating a paint class object
        //paint because allows to draw text on bitmaps
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        //assigning the new bitmap the same dimensions as the previously created bitmap
        val bmHeight= newBitmap.height
        val bmWidth = newBitmap.width

        //variables to position top textview with the text on the image
        val xTop = 0
        val yTop = 0

        //variable to position the bottom textview with the text on the image
        val xBottom = 0
        val yBottom = bmHeight- (bmHeight/4)

        //variables to position the text within the textview component
        val left = 0
        val top = 0
        val right = bmWidth
        val bottom = (bmHeight)

        //creating a textview on the top portion of the image to add top text
        // and assign the user input text to that region
        val topTextView = TextView(this)
        topTextView.layout
        topTextView.layout(left,top, right, bottom)
        topTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize)
        topTextView.setTextColor(fontColor)
        topTextView.text = topText
        topTextView.isDrawingCacheEnabled = true
        topTextView.gravity =  Gravity.CENTER_HORIZONTAL

        //position the textview to the top region of the bitmap
        newCanvas.drawBitmap(topTextView.drawingCache, xTop.toFloat(),yTop.toFloat(),paint)

        //creating a textview on the bottom portion of the image to add top text
        // and assign the user input text to that region
        val bottomTextView = TextView(this)
        bottomTextView.layout
        bottomTextView.layout(left, top, right, bottom)
        bottomTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize)
        bottomTextView.setTextColor(fontColor)
        bottomTextView.text = bottomText
        bottomTextView.isDrawingCacheEnabled = true
        bottomTextView.gravity= Gravity.CENTER_HORIZONTAL

        //position the textview to the bottom region of the bitmap
        newCanvas.drawBitmap(bottomTextView.drawingCache, xBottom.toFloat(),yBottom.toFloat(),paint)

        //assign the new bitmap with the text added to it on the imageView component
        imageView.setImageBitmap(newBitmap)
    }
}