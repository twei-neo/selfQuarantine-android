package com.example.selfquarantine.view.custom

import android.graphics.Color
import android.os.AsyncTask
import android.os.Build
import android.text.*
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import com.example.selfquarantine.viewModel.ArticleViewModel
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

class ArticleAsyncTask(val viewModel: ArticleViewModel) : AsyncTask<String, String, String>() {
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun doInBackground(vararg url: String?): String {
        var html = ""
        val connection = URL(url[0]).openConnection() as HttpURLConnection
        val instance = android.webkit.CookieManager.getInstance()
        instance.setAcceptCookie(true)
        instance.setCookie("https://www.ptt.cc", "Set-Cookie: over18=1; path=/")
        connection.setRequestProperty("Cookie", instance.getCookie("https://www.ptt.cc"))
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
        val mainContentFilter =
            Regex(".*<div id=\"main-content\" class=\"bbs-screen bbs-content\">.*")
        val ipFilter = Regex(".*<span class=\"f2\">※ 發信站: .*")
        var isContentStart = false
        viewModel.contentSpannableStringBuilder.value = SpannableStringBuilder()

        for (line in lines) {
            if (mainContentFilter.matches(line)) {
                setTitleBar(line)
                isContentStart = true
                continue
            }

            if(ipFilter.matches(line)){
                isContentStart = false
                setIp(line)
            }

            if(isContentStart){
                setContent(line)
            }
        }
    }

    private fun setContent(line: String) {
        if(isLinkContained(line)){
            appendLink(line)
        } else {
            viewModel.contentSpannableStringBuilder.value!!.append (line + "\n")
        }
    }

    private fun setTitleBar(line: String) {
        val mainContent = line.split("</span>")
        viewModel.author.value =
            mainContent[1].replace("<span class=\"article-meta-value\">", "")
        viewModel.boardName.value =
            mainContent[3].replace("<span class=\"article-meta-value\">", "")
        viewModel.title.value =
            mainContent[5].replace("<span class=\"article-meta-value\">", "")
        viewModel.postDate.value =
            mainContent[7].replace("<span class=\"article-meta-value\">", "")
    }
    private fun setIp(line : String){
        val contentSize = viewModel.contentSpannableStringBuilder.value!!.length
        var ip = line.replace("<span class=\"f2\">", "")
        ip = ip.replace("發信站: 批踢踢實業坊(ptt.cc),","發信站:批踢踢實業坊,")
        viewModel.contentSpannableStringBuilder.value!!.append(ip)
        viewModel.contentSpannableStringBuilder.value!!.setSpan(ForegroundColorSpan(Color.GREEN), contentSize, contentSize+ip.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    }

    private fun isLinkContained(line : String) : Boolean{
        val linkFilter = Regex(".*<a href=\".*\".*>.*</a>")
        return linkFilter.matches(line)
    }


    private fun appendLink(tagA : String) {
        var link = tagA.replace("<a href=\"","")
        var endOfLink = 0
        for (i in 0 .. link.length){
            if(link[i] == '"'){
                endOfLink = i
                break
            }
        }
        link = link.substring(0,endOfLink)
        val startPosition = viewModel.contentSpannableStringBuilder.value!!.length
        viewModel.contentSpannableStringBuilder.value!!.append(link)

        val clickableSpan = object : ClickableSpan(){
            override fun onClick(widget: View) {
                //TODO
            }
        }

        viewModel.contentSpannableStringBuilder.value!!.setSpan(clickableSpan,startPosition, startPosition+link.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    }


}