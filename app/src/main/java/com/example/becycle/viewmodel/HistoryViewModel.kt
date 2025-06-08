package com.example.becycle.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.becycle.data.misc.BecycleRepository
import com.example.becycle.data.local.entity.HistoryEntity

class HistoryViewModel(private val repository: BecycleRepository): ViewModel() {

//    fun getLocalHistory(): LiveData<List<HistoryEntity>> = repository.getLocalHistory()
}
