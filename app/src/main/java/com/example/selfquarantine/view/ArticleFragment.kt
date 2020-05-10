package com.example.selfquarantine.view

import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.selfquarantine.databinding.FragmentArticleBinding
import com.example.selfquarantine.viewModel.ArticleViewModel
import java.lang.Exception
import java.net.CookieManager
import java.net.HttpCookie
import java.net.HttpURLConnection
import java.net.URL

class ArticleFragment : Fragment() {

    private lateinit var binding : FragmentArticleBinding
    private lateinit var viewModel : ArticleViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {

        binding = FragmentArticleBinding.inflate(inflater, container, false)
        initBinding()
        updateContent()
        return binding.root
    }

    private fun updateContent() {
        val url = "https://www.ptt.cc/bbs/Gossiping/M.1588941469.A.E64.html"
        AsyncTaskHandle().execute(url)
    }

    private fun initBinding(){
        viewModel = ViewModelProviders.of(this).get(ArticleViewModel::class.java)
        binding.handler = this
        binding.lifecycleOwner=this
        binding.viewModel = viewModel
    }

    inner class AsyncTaskHandle : AsyncTask<String, String, String>(){
        @RequiresApi(Build.VERSION_CODES.KITKAT)
        override fun doInBackground(vararg url: String?): String {

            var html = ""
            val connection = URL(url[0]).openConnection() as HttpURLConnection
            val instance = android.webkit.CookieManager.getInstance()
            instance.setAcceptCookie(true);
            instance.setCookie("https://www.ptt.cc", "Set-Cookie: over18=1; path=/")
            connection.setRequestProperty("Cookie",instance.getCookie("https://www.ptt.cc"));
            connection.requestMethod="GET"
            connection.addRequestProperty("Content-Type", "application/json; charset=UTF-8")
            connection.doOutput = true

            try{
                connection.connect()
                html = connection.inputStream.use { it.reader().use{reader -> reader.readText()} }
            } catch (e : Exception){
                Log.d("ArticleFragment.kt", e.toString())
            } finally {
                connection.disconnect()
            }
            return html
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            binding.tvContent.text = result
        }
    }
}