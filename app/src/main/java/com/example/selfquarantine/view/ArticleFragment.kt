package com.example.selfquarantine.view

import android.content.Intent
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
import androidx.navigation.findNavController
import com.example.selfquarantine.databinding.FragmentArticleBinding
import com.example.selfquarantine.databinding.WidgetTitleBarBinding
import com.example.selfquarantine.databinding.WidgetToolBarBinding
import com.example.selfquarantine.viewModel.ArticleViewModel
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

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
        AsyncTaskHandle().execute(url)
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

    inner class AsyncTaskHandle : AsyncTask<String, String, String>() {
        @RequiresApi(Build.VERSION_CODES.KITKAT)
        override fun doInBackground(vararg url: String?): String {

            var html = ""
            val connection = URL(url[0]).openConnection() as HttpURLConnection
            val instance = android.webkit.CookieManager.getInstance()
            instance.setAcceptCookie(true);
            instance.setCookie("https://www.ptt.cc", "Set-Cookie: over18=1; path=/")
            connection.setRequestProperty("Cookie", instance.getCookie("https://www.ptt.cc"));
            connection.requestMethod = "GET"
            connection.addRequestProperty("Content-Type", "application/json; charset=UTF-8")
            connection.doOutput = true

            try {
                connection.connect()
                html =
                    connection.inputStream.use { it.reader().use { reader -> reader.readText() } }
            } catch (e: Exception) {
                Log.d("ArticleFragment.kt", e.toString())
            } finally {
                connection.disconnect()
            }
            return html
        }

        override fun onPostExecute(result: String?) {
            val lines = result?.split("\n") ?: return
            val contentFilter = Regex("<meta name=\"description\" content=\".*")
            val mainContentFilter =
                Regex(".*<div id=\"main-content\" class=\"bbs-screen bbs-content\">.*")
            var isContentStart = false

            for (line in lines) {
                if (mainContentFilter.matches(line)) {
                    val mainContent = line.split("</span>")
                    for (content in mainContent)
                        Log.d("ArticleFragment.kt.con", content)
                    viewModel.author.value =
                        mainContent[1].replace("<span class=\"article-meta-value\">", "")
                    viewModel.boardName.value =
                        mainContent[3].replace("<span class=\"article-meta-value\">", "")
                    viewModel.title.value =
                        mainContent[5].replace("<span class=\"article-meta-value\">", "")
                    viewModel.postDate.value =
                        mainContent[7].replace("<span class=\"article-meta-value\">", "")
                }

                if (contentFilter.matches(line)) {
                    val content = line.replace("<meta name=\"description\" content=\"", "")
                    isContentStart = true
                    viewModel.content.value = content
                } else if (isContentStart) {
                    var content = line.replace("\">", "")
                    if (content != line)
                        isContentStart = false
                    content += "\n\n"
                    viewModel.content.value += content
                }
            }
        }
    }
}