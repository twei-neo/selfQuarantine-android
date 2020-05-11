package com.example.selfquarantine.view

import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
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
/*
* <meta name="description" content="是肥肥啦
肥肥這兩天都有發要重考醫學系的文章
結果收到數個站內信，說他是政大企管碩，他說連他都考不上醫學系了，更不用說我..
然後他說政大碩錄取率只有2%
還說什麼他不到三十歲已經買了好幾個房子，連醫生都要跟他租房子才能開診所@@
">
* */
        override fun onPostExecute(result: String?) {
            val lines = result?.split("\n") ?: return
            val titleFilter = Regex("<title>.*</title>", RegexOption.IGNORE_CASE)
            val contentFilter= Regex("<meta name=\"description\" content=\".*")
            var isContentStart = false

            for( line in lines){
                if(titleFilter.matches(line)){
                    var title = line
                    title=title.replace("<title>","")
                    title=title.replace("- 看板 Gossiping - 批踢踢實業坊</title>","")
                    viewModel.title.value = title
                }

                if(contentFilter.matches(line)){
                    var content = line.replace("<meta name=\"description\" content=\"", "")
                    isContentStart = true
                    viewModel.content.value = content
                } else if(isContentStart){
                    var content = line.replace("\">","")
                    if(content != line)
                        isContentStart = false
                    content += "\n\n"
                    viewModel.content.value += content
                }
            }
        }
    }
}