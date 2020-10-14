package com.example.companyassignment.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.companyassignment.repository.DashboardRepository
import java.lang.IllegalArgumentException

class ViewModelFactory (private val repository: DashboardRepository) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DashboardViewModel::class.java)){
            return DashboardViewModel(repository) as T
        }
        throw IllegalArgumentException("Unkown viewmodel class")
    }

}