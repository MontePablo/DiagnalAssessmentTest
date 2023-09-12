package com.example.diagnalassessmenttest.models

import com.google.gson.annotations.SerializedName

data class Item(
    @SerializedName("name")
    val title:String,
    @SerializedName("poster-image")
    val image:String
)
