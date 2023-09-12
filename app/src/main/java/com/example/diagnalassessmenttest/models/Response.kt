package com.example.diagnalassessmenttest.models

import com.google.gson.annotations.SerializedName

data class Response (
    val page: Page
)

data class Page (
    val title: String,
    @SerializedName("total-content-items")
    val totalContentItems: String,
    @SerializedName("page-num")
    val pageNum: String,
    @SerializedName("page-size")
    val pageSize: String,
    @SerializedName("content-items")
    val contentItems: ContentItems
)

data class ContentItems (
    val content: ArrayList<Item>
)