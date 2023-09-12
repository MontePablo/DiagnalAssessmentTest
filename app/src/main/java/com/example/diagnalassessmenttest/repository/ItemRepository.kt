package com.example.diagnalassessmenttest.repository

import android.content.res.AssetManager
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.diagnalassessmenttest.models.Item
import com.example.diagnalassessmenttest.models.Response
import com.example.diagnalassessmenttest.utils.Utility
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ItemRepository @Inject constructor() {
    @Inject
    lateinit var util:Utility

    private val itemsLiveData= MutableLiveData<ArrayList<Item>>()
    val items: LiveData<ArrayList<Item>>
        get() = itemsLiveData
    operator fun invoke(pageno:Int){
        val jsonString=util.getJsonStringFromAssets("CONTENTLISTINGPAGE-PAGE${pageno}.json")
        val gson=Gson()
        val response=gson.fromJson(jsonString,Response::class.java)
        if(itemsLiveData.value!=null){
            val tempData = items.value
            tempData?.addAll(response.page.contentItems.content)
            itemsLiveData.postValue(tempData!!)
            Log.d("TAG","size "+itemsLiveData.value?.size.toString())
        }else{
            itemsLiveData.postValue(response.page.contentItems.content)
            Log.d("TAG","size "+itemsLiveData.value?.size.toString())
        }
    }
}