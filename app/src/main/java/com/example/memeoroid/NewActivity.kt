package com.example.memeoroid

import android.content.Intent
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
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

    lateinit var imageSelected : String

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
                    imageSelected = position.toString()
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

            val topTemp = topText.text.toString()
            val bottomTemp = bottomText.text.toString()

            println("top text ${topTemp}")
            println("bottom text ${bottomTemp}")

            var intent = Intent(this, CustomMemeDisplayActivity::class.java)
            intent.putExtra("imageSelected",imageSelected)
            intent.putExtra("topText",topTemp)
            intent.putExtra("bottomText",bottomTemp)
            startActivity(intent)

        }
    }
}



