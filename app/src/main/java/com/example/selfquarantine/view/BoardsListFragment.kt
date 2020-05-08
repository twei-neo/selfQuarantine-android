package com.example.selfquarantine.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.selfquarantine.databinding.FragmentBoardsListBinding
import com.example.selfquarantine.viewModel.BoardsListViewModel

class BoardsListFragment : Fragment() {

    lateinit var binding : FragmentBoardsListBinding
    lateinit var viewModel : BoardsListViewModel

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {

        binding = FragmentBoardsListBinding.inflate(inflater, container, false)
        initBinding()
        return binding.root
    }

    private fun initBinding(){
        viewModel = ViewModelProviders.of(this).get(BoardsListViewModel::class.java)
        binding.handler = this
        binding.lifecycleOwner=this
        binding.viewModel = viewModel
    }
}