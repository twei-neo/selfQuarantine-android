package com.example.selfquarantine.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.example.selfquarantine.databinding.FragmentArticleBinding
import com.example.selfquarantine.databinding.WidgetTitleBarBinding
import com.example.selfquarantine.databinding.WidgetToolBarBinding
import com.example.selfquarantine.view.custom.ArticleAsyncTask
import com.example.selfquarantine.viewModel.ArticleViewModel


class ArticleFragment : Fragment() {

    private lateinit var binding: FragmentArticleBinding
    private lateinit var titleBarBinding: WidgetTitleBarBinding
    private lateinit var toolBarBinding: WidgetToolBarBinding
    private lateinit var viewModel: ArticleViewModel
    private lateinit var url : String
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentArticleBinding.inflate(inflater, container, false)
        initBinding()
        updateContent()
        return binding.root
    }

    private fun updateContent() {
        url = "https://www.ptt.cc/bbs/Gossiping/M.1589191497.A.DAB.html"
        ArticleAsyncTask(viewModel).execute(url)
    }

    private fun initBinding() {
        viewModel = ViewModelProviders.of(this).get(ArticleViewModel::class.java)
        binding.handler = this
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        titleBarBinding = binding.layoutTitleBar
        titleBarBinding.lifecycleOwner = this
        titleBarBinding.viewModel = viewModel

        toolBarBinding = binding.layoutToolBar
        toolBarBinding.handler = this
        toolBarBinding.lifecycleOwner = this
    }

    fun onClickBack(view : View){
        view.findNavController().popBackStack()
    }

    fun onClickShare(view : View){
        val intent = Intent(Intent.ACTION_SEND)
        intent.type="type/palin"
        intent.putExtra(Intent.EXTRA_SUBJECT, viewModel.title.value)
        intent.putExtra(Intent.EXTRA_TEXT, viewModel.title.value + "\n" +url)
        startActivity(Intent.createChooser(intent, viewModel.title.value))
    }

}