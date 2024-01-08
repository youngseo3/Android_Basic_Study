package com.youngseo3.infrean.communityapp.contentsList

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.widget.Toast
import com.youngseo3.infrean.communityapp.R

class ContentShowActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content_show)

        val getUrl = intent.getStringExtra("webUrl")

        val webView: WebView = findViewById(R.id.webView)
        webView.loadUrl(getUrl.toString())
    }
}