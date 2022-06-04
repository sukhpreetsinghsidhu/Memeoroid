package com.example.memeoroid.retrofit

data class MemeTemplate( var id: String,
                         var name: String,
                         var url: String,
                         var width: Int,
                         var height: Int,
                         var box_count: Int) {
}