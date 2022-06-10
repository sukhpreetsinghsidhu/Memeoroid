package com.example.memeoroid

import android.content.Intent
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.memeoroid.roomdb.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_favorites.*

class FavoritesActivity : AppCompatActivity() {

    // variables necessary for recycler view to work
    lateinit var vm: DbViewModel
    var favoritesList = ArrayList<Meme>()
    lateinit var adapter: ListAdapter
    val limit = 10
    var offset =0
    var search =false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        supportActionBar?.hide()
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        val floatingBtn : FloatingActionButton = findViewById(R.id.floatingBtn)

        // initialize db viewmodel
        vm = DbViewModel(application)


        // tests for CRUD operations
//        vm.insertFavorite(Meme(null, "Top1", "Bottom1", "Drake Hotline Bling"))
//        vm.insertFavorite(Meme(null, "Top2", "Bottom2", "Drake Hotline Bling"))
//        vm.insertFavorite(Meme(null, "Top3", "Bottom3", "Drake Hotline Bling"))

        // get all favorite memes
        vm.allFavorites.observe(this) { favoritesList ->
            getFavorites(favoritesList)
            if (favoritesList.isEmpty()) {
                emptyListText.text = "LIST EMPTY"
            } else {
                emptyListText.text = ""
            }
        }

        // get reference to view to populate
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // create adapter with data source and assign adapter
        adapter = ListAdapter(favoritesList)
        recyclerView.adapter = adapter
//DAB CODE
        //LeftSwipe
        val swipeDelete = object : OnSwipeLeft(this){
            override fun onSwiped(
                viewHolder: RecyclerView.ViewHolder,
                direction: Int
            ) {
                vm.deleteFavorite(favoritesList.get(viewHolder.adapterPosition))
                adapter.deleteSwipedFavorite(viewHolder.adapterPosition)
            }
        }

        val toLeftTouchHelper = ItemTouchHelper(swipeDelete)
        toLeftTouchHelper.attachToRecyclerView(recyclerView)

        //RightSwipe
        val swipeUpdate = object : OnSwipeRight(this){
            override fun onSwiped(
                viewHolder: RecyclerView.ViewHolder,
                direction: Int
            ) {
                adapter.deleteSwipedFavorite(viewHolder.adapterPosition) //must change to update/edit, is currently delete
            }
        }

        val toRightTouchHelper = ItemTouchHelper(swipeUpdate)
        toRightTouchHelper.attachToRecyclerView(recyclerView)
//DAB CODE END
        //search bar functionality
        searchBar.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                // get string from the searchbar EditText
                var searchText = searchBar.text.toString()
                // if empty, get all favorite memes
                // else, send text to the viewmodel
                if (searchText != "") {
                    offset = 0
                    vm.search(searchText, limit, offset)
                    search =true
                } else {
                    vm.selectAllFavorites(10,0)
                    search =  false
                }
                // necessary to update recycler view after search
           // on search we will have to visibility gone for load more button.
                vm.allFavorites.observe(this@FavoritesActivity) { favoritesList ->
                    getFavorites(favoritesList)
                    if (favoritesList.isEmpty()) {
                        emptyListText.text = "LIST EMPTY"
                    } else {
                        emptyListText.text = ""
                    }
                    if(favoritesList.size <10){
                        LoadMore.visibility = View.GONE
                    }else{
                        LoadMore.visibility = View.VISIBLE
                    }
                    if(offset == 0 ){
                        Prevous.visibility = View.GONE
                    }else{
                        Prevous.visibility = View.VISIBLE
                    }
                }
            }
        })

        LoadMore.setOnClickListener {
            //Log.d("offset&Limit", "$offset, $limit")
            offset+=limit
            loadData()
        }
        Prevous.setOnClickListener {
            offset -= limit

            loadData()
        }

        addButton.setOnClickListener {
            val intent = Intent(this, NewActivity::class.java)
            startActivity(intent)
            Toast.makeText(applicationContext,"Redirecting to Create Meme", Toast.LENGTH_LONG).show()
        }

        floatingBtn.setOnClickListener{
            val myIntent = Intent(this, MainActivity::class.java)
            startActivity(myIntent)
            Toast.makeText(applicationContext,"Redirecting to Home Page", Toast.LENGTH_LONG).show()
        }

    }
fun loadData(){
    // Log.d("offset&Limit 2", "$offset, $limit")

    if(search){
        var searchText = searchBar.text.toString()
        vm.search(searchText, limit, offset)
    }else{
        vm.selectAllFavorites(limit,offset)
    }


    vm.allFavorites.observe(this){
        getFavorites(it)
        if(it.size <10){
            LoadMore.visibility = View.GONE
        }else{
            LoadMore.visibility = View.VISIBLE
        }
        if(offset == 0 ){
            Prevous.visibility = View.GONE
        }else{
            Prevous.visibility = View.VISIBLE
        }
    }
}
    // function used to refresh recycler view
    fun getFavorites(favoritesList: List<Meme>) {
        this.favoritesList.clear()
        this.favoritesList.addAll(favoritesList)
        adapter.notifyDataSetChanged()
    }
}