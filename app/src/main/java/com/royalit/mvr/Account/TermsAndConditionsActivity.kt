package com.royalit.mvr.Account

import android.graphics.PorterDuff
import android.os.Bundle
import android.text.Html
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.royalit.mvr.Config.ViewController
import com.royalit.mvr.Model.AboutusResponse
import com.royalit.mvr.Model.TermsAndConditionsResponse
import com.royalit.mvr.R
import com.royalit.mvr.Retrofit.RetrofitClient
import com.royalit.mvr.databinding.ActivityAboutUsBinding
import com.royalit.mvr.databinding.ActivityTermsAndConditionsBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TermsAndConditionsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTermsAndConditionsBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTermsAndConditionsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        toolbarSetup()

        if(!ViewController.noInterNetConnectivity(applicationContext)){
            ViewController.showToast(applicationContext, "Please check your connection ")
        }else{
            termsAndConditionsApi()
        }

    }

    fun toolbarSetup(){
        val toolbar = binding.materialtoolbar
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            title = "Terms & Conditions"
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

    private fun termsAndConditionsApi() {
        ViewController.showLoading(this@TermsAndConditionsActivity)
        val apiInterface = RetrofitClient.apiInterface
        apiInterface.termsAndConditionsApi().enqueue(object : Callback<TermsAndConditionsResponse> {
            override fun onResponse(call: Call<TermsAndConditionsResponse>, response: Response<TermsAndConditionsResponse>) {
                ViewController.hideLoading()
                if (response.isSuccessful) {
                    val rsp = response.body()
                    if (rsp != null) {
                        binding.txtTerms.text = Html.fromHtml(rsp.data?.content ?: "", Html.FROM_HTML_MODE_LEGACY)
                    }
                } else {
                    ViewController.showToast(applicationContext, "Error: ${response.code()}")
                }
            }
            override fun onFailure(call: Call<TermsAndConditionsResponse>, t: Throwable) {
                ViewController.hideLoading()
                ViewController.showToast(applicationContext, "Try again: ${t.message}")
            }
        })
    }

}