package com.example.selfquarantine.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ArticlesListViewModel : ViewModel(){
    val title = MutableLiveData<String>().apply { value = "文章一覽" }
}