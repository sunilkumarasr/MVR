package com.royalit.mvr

import android.graphics.PorterDuff
import android.os.Bundle
import android.text.Html
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.royalit.mvr.Config.ViewController
import com.royalit.mvr.Model.AboutusResponse
import com.royalit.mvr.Model.ContactUsResponse
import com.royalit.mvr.Retrofit.RetrofitClient
import com.royalit.mvr.databinding.ActivityContactusBinding
import com.royalit.mvr.databinding.ActivityMyTeamBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ContactusActivity : AppCompatActivity() {

    private lateinit var binding: ActivityContactusBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityContactusBinding.inflate(layoutInflater)
        setContentView(binding.root)
        toolbarSetup()


        if(!ViewController.noInterNetConnectivity(applicationContext)){
            ViewController.showToast(applicationContext, "Please check your connection ")
        }else{
            contactUsApi()
        }


    }


    fun toolbarSetup(){
        val toolbar = binding.materialtoolbar
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            title = "Contact Us"
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


    private fun contactUsApi() {
        ViewController.showLoading(this@ContactusActivity)
        val apiInterface = RetrofitClient.apiInterface
        apiInterface.contactUsApi().enqueue(object : Callback<ContactUsResponse> {
            override fun onResponse(call: Call<ContactUsResponse>, response: Response<ContactUsResponse>) {
                ViewController.hideLoading()
                if (response.isSuccessful) {
                    val rsp = response.body()
                    if (rsp != null) {
                        binding.txtNumber.text = rsp.data?.mobile
                        binding.txtEmail.text = rsp.data?.mail
                        binding.txtAddress.text = rsp.data?.location
                    }
                } else {
                    ViewController.showToast(applicationContext, "Error: ${response.code()}")
                }
            }
            override fun onFailure(call: Call<ContactUsResponse>, t: Throwable) {
                ViewController.hideLoading()
                ViewController.showToast(applicationContext, "Try again: ${t.message}")
            }
        })
    }

}