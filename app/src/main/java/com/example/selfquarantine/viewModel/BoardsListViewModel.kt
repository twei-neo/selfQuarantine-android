package com.example.selfquarantine.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BoardsListViewModel : ViewModel() {
    val title = MutableLiveData<String>().apply { value = "熱門看板" }
}