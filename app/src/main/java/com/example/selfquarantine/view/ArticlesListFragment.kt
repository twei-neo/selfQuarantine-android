package com.example.selfquarantine.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.example.selfquarantine.databinding.FragmentArticlesListBinding
import com.example.selfquarantine.databinding.FragmentBoardsListBinding
import com.example.selfquarantine.viewModel.ArticlesListViewModel
import com.example.selfquarantine.viewModel.BoardsListViewModel

class ArticlesListFragment : Fragment() {

    lateinit var binding : FragmentArticlesListBinding
    lateinit var viewModel : ArticlesListViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {

        binding = FragmentArticlesListBinding.inflate(inflater, container, false)
        initBinding()
        return binding.root
    }

    private fun initBinding(){
        viewModel = ViewModelProviders.of(this).get(ArticlesListViewModel::class.java)
        binding.handler = this
        binding.lifecycleOwner=this
        binding.viewModel = viewModel
    }

    fun onClickNext(view : View){
        val action = ArticlesListFragmentDirections.actionArticlesListFragmentToArticleFragment()
        view.findNavController().navigate(action)
    }
}