package com.youngseo3.infrean.communityapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import com.youngseo3.infrean.communityapp.R

class StoreFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_store, container, false)

        val webView: WebView = view.findViewById(R.id.webview_store)
        webView.loadUrl("https://www.inflearn.com/")
        return view
    }

}