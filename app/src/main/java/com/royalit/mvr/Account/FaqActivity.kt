package com.royalit.mvr.Account

import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.royalit.mvr.Adapters.FaqAdapter
import com.royalit.mvr.Config.ViewController
import com.royalit.mvr.Model.FaqModel
import com.royalit.mvr.Model.FaqModelData
import com.royalit.mvr.R
import com.royalit.mvr.Retrofit.RetrofitClient
import com.royalit.mvr.databinding.ActivityAboutUsBinding
import com.royalit.mvr.databinding.ActivityFaqBinding

class FaqActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFaqBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityFaqBinding.inflate(layoutInflater)
        setContentView(binding.root)
        toolbarSetup()

        if(!ViewController.noInterNetConnectivity(this@FaqActivity)){
            ViewController.showToast(this@FaqActivity, "Please check your connection ")
            return
        }else {
            faqListApi()
        }

    }

    fun toolbarSetup() {
        val toolbar = binding.materialtoolbar
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            title = "FAQ'S"
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


    private fun faqListApi() {
        ViewController.showLoading(this@FaqActivity)
        val apiInterface = RetrofitClient.apiInterface
        apiInterface.faqListApi().enqueue(object : retrofit2.Callback<FaqModel> {
            override fun onResponse(
                call: retrofit2.Call<FaqModel>,
                response: retrofit2.Response<FaqModel>
            ) {
                ViewController.hideLoading()
                if (response.isSuccessful) {
                    val rsp = response.body()
                    if (rsp != null && rsp.data != null && rsp.data.isNotEmpty()) {
                        // Now you can directly access the 'data' field of the FaqModel
                        val allFaqData = rsp.data
                        FaqDataSet(allFaqData) // Pass the data to the adapter
                    } else {
                        ViewController.showToast(this@FaqActivity, "No FAQ data found.")
                    }
                } else {
                    ViewController.showToast(
                        this@FaqActivity,
                        "Error: ${response.code()}"
                    )
                }
            }

            override fun onFailure(call: retrofit2.Call<FaqModel>, t: Throwable) {
                Log.e("cat_error", t.message.toString())
                ViewController.hideLoading()
                ViewController.showToast(this@FaqActivity, "Try again: ${t.message}")
            }
        })
    }

    private fun FaqDataSet(faqlist: List<FaqModelData>) {
        binding.recyclerView.layoutManager = LinearLayoutManager(this@FaqActivity)
        binding.recyclerView.adapter = FaqAdapter(faqlist) { item ->
            // Handle item click if necessary
        }
    }




}

