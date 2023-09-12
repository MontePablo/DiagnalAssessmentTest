package com.example.diagnalassessmenttest.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.diagnalassessmenttest.R
import com.example.diagnalassessmenttest.models.Item
import com.example.diagnalassessmenttest.utils.Utility
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ItemAdapter @Inject constructor(@ApplicationContext context: Context,util: Utility): RecyclerView.Adapter<ItemAdapter.ViewHolder>() {
     var util:Utility
     lateinit var context:Context
    init {
        this.util=util
        this.context=context
    }
    var items= arrayListOf<Item>()
    var highLightText:String?=null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.item,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }
    fun update(items:ArrayList<Item>, highLightedText:String?= null){
        this.items=items
        if(!highLightedText.isNullOrEmpty()){
            this.highLightText=highLightedText
        }
        notifyDataSetChanged()
    }
    fun clearList(){
        this.items.clear()
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val image=util.getImageFromAssets(items[position].image)
        if(image!=null)
            holder.image.setImageBitmap(image)
        else
            holder.image.setImageDrawable(context.resources.getDrawable(R.drawable.placeholder_for_missing_posters))
        holder.name.text=items[position].title
        if(!highLightText.isNullOrEmpty())
            util.setHighLightedText(holder.name,highLightText!!)
    }

    class ViewHolder(view: View):RecyclerView.ViewHolder(view) {
        var image=view.findViewById<ImageView>(R.id.item_image)
        var name=view.findViewById<TextView>(R.id.item_title)
    }
}