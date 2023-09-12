package com.example.diagnalassessmenttest.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.diagnalassessmenttest.models.Item
import com.example.diagnalassessmenttest.repository.ItemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ItemViewModel @Inject constructor(
    private val itemRepository: ItemRepository,
) : ViewModel() {
    val items: LiveData<ArrayList<Item>>
        get() = itemRepository.items
    fun getData(pageno:Int) {
        viewModelScope.launch {
            itemRepository.invoke(pageno)
        }
    }

}