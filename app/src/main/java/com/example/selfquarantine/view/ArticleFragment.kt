package com.example.selfquarantine.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.selfquarantine.databinding.FragmentArticleBinding
import com.example.selfquarantine.viewModel.ArticleViewModel
import com.example.selfquarantine.viewModel.ArticlesListViewModel

class ArticleFragment : Fragment() {

    lateinit var binding : FragmentArticleBinding
    lateinit var viewModel : ArticleViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {

        binding = FragmentArticleBinding.inflate(inflater, container, false)
        initBinding()
        return binding.root
    }

    private fun initBinding(){
        viewModel = ViewModelProviders.of(this).get(ArticleViewModel::class.java)
        binding.handler = this
        binding.lifecycleOwner=this
        binding.viewModel = viewModel
    }
}