package com.royalit.mvr.Account

import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.royalit.mvr.Adapters.NotificationAdapter
import com.royalit.mvr.Config.ViewController
import com.royalit.mvr.Model.FaqModel
import com.royalit.mvr.Model.NotificationData
import com.royalit.mvr.Model.NotificationModel
import com.royalit.mvr.R
import com.royalit.mvr.Retrofit.RetrofitClient
import com.royalit.mvr.databinding.ActivityNotificationBinding
import com.royalit.mvr.databinding.ActivityProfileBinding

class NotificationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNotificationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        toolbarSetup()

        if(!ViewController.noInterNetConnectivity(applicationContext)){
            ViewController.showToast(applicationContext, "Please check your connection ")
        }else{
            NotificationsListApi()
        }

    }

    fun toolbarSetup(){
        val toolbar = binding.materialtoolbar
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            title = "Notifications"
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

    private fun NotificationsListApi() {
        ViewController.showLoading(this@NotificationActivity)
        val apiInterface = RetrofitClient.apiInterface
        apiInterface.NotificationsListApi().enqueue(object : retrofit2.Callback<NotificationModel> {
            override fun onResponse(
                call: retrofit2.Call<NotificationModel>,
                response: retrofit2.Response<NotificationModel>
            ) {
                ViewController.hideLoading()
                if (response.isSuccessful) {
                    val rsp = response.body()
                    if (rsp != null && rsp.data != null && rsp.data.isNotEmpty()) {
                        val allFaqData = rsp.data
                        NotificationDataSet(allFaqData)
                    } else {
                        ViewController.showToast(this@NotificationActivity, "No data found.")
                    }
                } else {
                    ViewController.showToast(
                        this@NotificationActivity,
                        "Error: ${response.code()}"
                    )
                }
            }

            override fun onFailure(call: retrofit2.Call<NotificationModel>, t: Throwable) {
                Log.e("cat_error", t.message.toString())
                ViewController.hideLoading()
                ViewController.showToast(this@NotificationActivity, "Try again: ${t.message}")
            }
        })
    }
    private fun NotificationDataSet(joblist: List<NotificationData>) {
        binding.recyclerView.layoutManager = LinearLayoutManager(this@NotificationActivity)
        binding.recyclerView.adapter = NotificationAdapter(joblist) { item ->
            //Toast.makeText(activity, "Clicked: ${item.text}", Toast.LENGTH_SHORT).show()
        }
    }

}