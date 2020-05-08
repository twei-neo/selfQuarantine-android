package com.example.selfquarantine.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.selfquarantine.databinding.FragmentBoardsListBinding

class BoardsListFragment : Fragment() {

    lateinit var binding : FragmentBoardsListBinding

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {

        binding = FragmentBoardsListBinding.inflate(inflater, container, false)
        initBinding()
        return binding.root
    }

    private fun initBinding(){
        binding.handler = this
        binding.lifecycleOwner=this
    }
}