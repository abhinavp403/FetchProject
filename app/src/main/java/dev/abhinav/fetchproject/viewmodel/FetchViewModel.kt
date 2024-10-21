package dev.abhinav.fetchproject.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.abhinav.fetchproject.api.remote.FetchResponseItem
import dev.abhinav.fetchproject.repository.FetchRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FetchViewModel @Inject constructor(
    private val repository: FetchRepository
) : ViewModel() {

    private val _list = MutableStateFlow<List<FetchResponseItem>>(listOf())
    val list: StateFlow<List<FetchResponseItem>> = _list

    var loadError = mutableStateOf("")
    var isLoading = mutableStateOf(false)

    // Create a list from api response
    fun createList() {
        val list = mutableListOf<FetchResponseItem>()
        viewModelScope.launch {
            try {
                isLoading.value = true
                val response = repository.getResponse()
                response.forEach {
                    if (!it.name.isNullOrBlank()) {  // Skips items whose name is null or blank
                        list.add(it)
                    }
                }

                loadError.value = ""
                isLoading.value = false
                _list.value = list
            } catch (e: Exception) {
                loadError.value = e.message.toString()
                isLoading.value = false
            }
        }
    }

    // Return list of unique list ids retrieved from main list
    fun getListOfListIds(list: List<FetchResponseItem>) : List<Int> {
        return list.map { it.listId }.distinct().sorted()
    }

    // Return a sorted list of items belonging to a particular group of list Id
    // List is sorted using name
    fun groupByListId(listId: Int): List<FetchResponseItem> {
        return _list.value.filter {
            it.listId == listId
        }.sortedBy { it.name }
    }
}