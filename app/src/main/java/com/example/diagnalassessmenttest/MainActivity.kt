package com.example.diagnalassessmenttest

import android.content.res.Configuration
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AbsListView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.example.diagnalassessmenttest.adapters.ItemAdapter
import com.example.diagnalassessmenttest.databinding.ActivityMainBinding
import com.example.diagnalassessmenttest.extention.observe
import com.example.diagnalassessmenttest.models.Item
import com.example.diagnalassessmenttest.utils.Utility
import com.example.diagnalassessmenttest.viewModels.ItemViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    var isScrolling=false
    var currentPage=1

    lateinit var itemsBckup:ArrayList<Item>
    @Inject
    lateinit var util:Utility

    lateinit var mBinding:ActivityMainBinding
    @Inject
    lateinit var mAdapter:ItemAdapter
    @Inject
    lateinit var mAdapterForSearch:ItemAdapter
    private val mViewModel: ItemViewModel by viewModels()
    lateinit var mLayoutManager: GridLayoutManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        window.statusBarColor=getColor(R.color.black)

        mViewModel.apply {
            observe(items,::onUpdateLiveData)
        }
        mLayoutManager= GridLayoutManager(applicationContext,3)

        mBinding.apply {
            backBtn.setOnClickListener(View.OnClickListener {
                super.onBackPressed()
            })
            recyclerView.apply {
                adapter=mAdapter
                layoutManager=mLayoutManager
            }
            searchBtn.setOnClickListener(View.OnClickListener {
                util.hideViews(arrayListOf(searchBtn,backBtn,pageTitle))
                util.showViews(arrayListOf(searchCancelBtn,searchBar))
                mBinding.recyclerView.adapter=mAdapterForSearch
            })
            searchCancelBtn.setOnClickListener(View.OnClickListener {
                util.showViews(arrayListOf(searchBtn,backBtn,pageTitle))
                util.hideViews(arrayListOf(searchCancelBtn,searchBar))
                recyclerView.adapter=mAdapter
                util.closeKeyboard()

            })
            searchBar.addTextChangedListener(object:TextWatcher{
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable?) {
                    searchFunc(s.toString())
                }
            })
        }

        autoSendPageNo()
        lazyLoading()
    }

    private fun searchFunc(s: String) {
        CoroutineScope(Dispatchers.IO).launch {
            if (s!!.length>2){
                val items=itemsBckup.filter { it.title.contains(s) }
                super.runOnUiThread(Runnable {
                    mAdapterForSearch.update(items as ArrayList<Item>,s)
                })
            }else{
                super.runOnUiThread(Runnable {
                    mAdapterForSearch.clearList()
                })
            }
        }
    }

    private fun onUpdateLiveData(items: ArrayList<Item>?) {
        mAdapter.update(items!!)
        itemsBckup=items
    }

    fun autoSendPageNo(){
        if(currentPage<4) {
            mViewModel.getData(currentPage)
            currentPage++
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        mBinding.apply {
            val searchLayout= searchBar.layoutParams as ConstraintLayout.LayoutParams
            val titleBarLayout= titleBar.layoutParams as ConstraintLayout.LayoutParams

            if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                mLayoutManager.spanCount=5
                searchLayout.matchConstraintPercentHeight=0.9F
                titleBarLayout.matchConstraintPercentHeight=0.16F
            } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
                mLayoutManager.spanCount=3
                searchLayout.matchConstraintPercentHeight=0.6F
                titleBarLayout.matchConstraintPercentHeight=0.10F
            }
            searchBar.layoutParams=searchLayout
            titleBar.layoutParams=titleBarLayout
        }

    }

    fun lazyLoading(){
        mBinding.recyclerView.addOnScrollListener(object: OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState==AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
                    isScrolling=true
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visibleItems=mLayoutManager.childCount
                val totalItems=mLayoutManager.itemCount
                val scrolledOutItems=mLayoutManager.findFirstVisibleItemPosition()
                if(isScrolling && (visibleItems+scrolledOutItems)>=(totalItems-5)){
                    autoSendPageNo()
                }
            }
        })
    }
}