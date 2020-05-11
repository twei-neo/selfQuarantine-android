package com.example.selfquarantine.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ArticleViewModel : ViewModel() {
    val title = MutableLiveData<String>().apply { value = "" }
    val author = MutableLiveData<String>().apply { value = "" }
    val postDate = MutableLiveData<String>().apply { value = "" }
    val boardName = MutableLiveData<String>().apply { value = "" }
    val content = MutableLiveData<String>().apply { value = "" }
}