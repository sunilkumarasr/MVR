package com.royalit.mvr.Account

import android.graphics.PorterDuff
import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.royalit.mvr.Retrofit.RetrofitClient
import com.royalit.mvr.databinding.ActivityProjectDetailsWebBinding

class ProjectDetailsWebActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProjectDetailsWebBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProjectDetailsWebBinding.inflate(layoutInflater)
        setContentView(binding.root)
        toolbarSetup()

        val pid = intent.getStringExtra("pid")
        val imageUrl = intent.getStringExtra("imageUrl")

        Glide.with(this)  // "this" refers to the context (Activity or Fragment)
            .load(imageUrl)  // Image URL
            .into(binding.zoomageImage)


//        binding.webview.settings.javaScriptEnabled = true
//        binding.webview.webViewClient = object : WebViewClient() {
//            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
//                view?.loadUrl(request?.url.toString())
//                return true
//            }
//        }
//        binding.webview.loadUrl(RetrofitClient.plot_details +pid)

    }

    fun toolbarSetup(){
        val toolbar = binding.materialtoolbar
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            title = "Project Details"
        }

        // Set Navigation Icon Color
        toolbar.navigationIcon?.let { icon ->
            icon.setColorFilter(
                resources.getColor(android.R.color.white, theme),
                PorterDuff.Mode.SRC_ATOP
            )
        }

        // Set Navigation Click Listener
        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    override fun onBackPressed() {
        if (binding.webview.canGoBack()) {
            binding.webview.goBack()
        } else {
            super.onBackPressed()
        }
    }

}