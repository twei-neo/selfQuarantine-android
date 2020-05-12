package com.example.selfquarantine.viewModel

import android.text.SpannableString
import android.text.SpannableStringBuilder
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ArticleViewModel : ViewModel() {
    val title = MutableLiveData<String>().apply { value = "" }
    val author = MutableLiveData<String>().apply { value = "" }
    val postDate = MutableLiveData<String>().apply { value = "" }
    val boardName = MutableLiveData<String>().apply { value = "" }
    val contentSpannableStringBuilder = MutableLiveData<SpannableStringBuilder>()
}