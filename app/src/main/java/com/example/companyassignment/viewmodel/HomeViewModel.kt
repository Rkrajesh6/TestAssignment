package com.example.companyassignment.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.companyassignment.model.Todo
import com.example.companyassignment.repository.HomeRepository
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val mainRepository = HomeRepository()

    val successfulLiveData = mainRepository.successLiveData
    val failureLiveData = mainRepository.failureLiveData
    private var mutableList = MutableLiveData<List<Todo>>().apply { value = emptyList() }
    val listData: LiveData<List<Todo>> = mutableList
    fun getTodoList(){
        viewModelScope.launch { mainRepository.getTodoList()}
    }
}