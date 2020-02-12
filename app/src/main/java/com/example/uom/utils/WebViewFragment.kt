package com.example.uom.utils


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.fragment.app.Fragment
import com.example.uom.R

/**
 * A simple [Fragment] subclass.
 */
class WebViewFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_web_view, container, false)

        val webView = view.findViewById<WebView>(R.id.web_view)
        val url = arguments?.getString("url")

        webView.loadUrl(url)

        return view
    }


}
