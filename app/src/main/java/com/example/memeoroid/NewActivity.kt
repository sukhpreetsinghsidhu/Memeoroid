package com.example.memeoroid

import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.memeoroid.retrofit.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_new.*


class NewActivity : AppCompatActivity() {

    lateinit var vm: ApiViewModel
    var templateList: List<MemeTemplate> = listOf<MemeTemplate>();
    var descriptions: MutableList<String> = ArrayList()
    var urls: MutableList<String> = ArrayList()

    lateinit var topText : EditText
    lateinit var bottomText : EditText

    lateinit var bitmapOriginal : Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new)

        topText = findViewById(R.id.editTopText)
        bottomText = findViewById(R.id.editBottomText)

        val inter = RetroApiInterface.create()
        val repo = TemplateRepo(inter)
        vm = ApiViewModel(repo)

        vm.getAllTemplates()

        vm.templateList.observe(this) {
            //println(it)
            for (item in it) {
                descriptions.add(item.name)
                urls.add(item.image)
            }
            //println(descriptions)
            //println(urls)
            var dropdownAdapter: ArrayAdapter<String> = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, descriptions)
            dropdown.adapter = dropdownAdapter
            dropdown.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>, view: View?,
                    position: Int, id: Long
                ) {
                    //Log.v("item", parent.getItemAtPosition(position) as String)
                    Picasso.with(this@NewActivity).load(urls[position]).into(imageView)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    Picasso.with(this@NewActivity).load(urls[0]).into(imageView)
                }
            })
        }

        resetButton.setOnClickListener() {
            editTopText.text.clear()
            editBottomText.text.clear()
            dropdown.setSelection(0)
            Picasso.with(this@NewActivity).load(urls[0]).into(imageView)
        }

        createMemeButton.setOnClickListener(){

            generateMemeText()
            savingImageToGallery()
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

    private fun loadWhiteBackground(firstImage: Bitmap, secondImage: Bitmap): Bitmap{

        val result = Bitmap.createBitmap(secondImage.width, (secondImage.height*2).toInt(), firstImage.config)
        val canvas = Canvas(result)

        canvas.drawBitmap(firstImage, 0f, 0f, null)
        canvas.drawBitmap(secondImage, 0f, ((secondImage.height*2)/4).toFloat(), null)
        return result
    }

    private fun generateMemeImage(){

        val backgroundImage = BitmapFactory.decodeResource(resources, R.drawable.whitebackground)
        val memeImage = (imageView.drawable as BitmapDrawable).bitmap

        bitmapOriginal = loadWhiteBackground(backgroundImage,memeImage)
        imageView.setImageBitmap(bitmapOriginal)
    }

    private fun generateMemeText(){

        generateMemeImage()

        val fontColor = Color.BLACK
        var fontSize :Float

        if(topText.text.length<20 && bottomText.text.length<15){
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
        topTextView.text = topText.text
        topTextView.isDrawingCacheEnabled = true
        topTextView.gravity =  Gravity.CENTER_HORIZONTAL

        newCanvas.drawBitmap(topTextView.drawingCache, xTop.toFloat(),yTop.toFloat(),paint)

        var bottomTextView = TextView(this)
        bottomTextView.layout
        bottomTextView.layout(left, top, right, bottom)
        bottomTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize)
        bottomTextView.setTextColor(fontColor)
        bottomTextView.text = bottomText.text
        bottomTextView.isDrawingCacheEnabled = true
        bottomTextView.gravity= Gravity.CENTER_HORIZONTAL

        newCanvas.drawBitmap(bottomTextView.drawingCache, xBottom.toFloat(),yBottom.toFloat(),paint)

        imageView.setImageBitmap(newBitmap)
    }
}