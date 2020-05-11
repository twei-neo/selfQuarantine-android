package com.example.selfquarantine.view.custom

import android.os.AsyncTask
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.selfquarantine.viewModel.ArticleViewModel
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

class ArticleAsyncTask(val viewModel: ArticleViewModel)  : AsyncTask<String, String, String>() {
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