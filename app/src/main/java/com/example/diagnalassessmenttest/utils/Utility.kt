package com.example.diagnalassessmenttest.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import com.example.diagnalassessmenttest.MainActivity
import com.example.diagnalassessmenttest.R
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.IOException
import javax.inject.Inject


class Utility @Inject constructor (@ApplicationContext context: Context) {
    val context:Context
    init {
        this.context=context
    }
    fun getImageFromAssets(fileName:String): Bitmap? {
        var bitmapImg:Bitmap?
        try {
            val inputStream=context.assets.open(fileName)
            bitmapImg=BitmapFactory.decodeStream(inputStream)
        }catch (e:IOException){
            e.printStackTrace()
            return null;
        }
        return bitmapImg
    }
    fun getJsonStringFromAssets(fileName: String?): String? {
        var jsonString: String = try {
            context.assets.open(fileName!!).bufferedReader().use { it.readText() }
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }
        return jsonString
    }
     fun hideViews(list: ArrayList<View>) {
        for(view in list){
            view.visibility= View.GONE
        }
    }
     fun showViews(list: ArrayList<View>) {
        for(view in list){
            view.visibility= View.VISIBLE
        }
    }
    fun setHighLightedText(tv: TextView, textToHighlight: String) {
        val tvt = tv.text.toString()
        var ofe = tvt.indexOf(textToHighlight, 0)
        val wordToSpan: Spannable = SpannableString(tv.text)
        var ofs = 0
        while (ofs < tvt.length && ofe != -1) {
            ofe = tvt.indexOf(textToHighlight, ofs)
            if (ofe == -1) break else {
                // set color here
                wordToSpan.setSpan(
                    ForegroundColorSpan(context.resources.getColor(R.color.highlight)),
                    ofe,
                    ofe + textToHighlight.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                tv.setText(wordToSpan, TextView.BufferType.SPANNABLE)
            }
            ofs = ofe + 1
        }
    }
    fun closeKeyboard() {
        val imm: InputMethodManager =
            context.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)

    }
}