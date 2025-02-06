package com.royalit.mvr

import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.royalit.mvr.Adapters.MyTeamsAdapter
import com.royalit.mvr.Adapters.SaleAdapter
import com.royalit.mvr.Authentication.LoginActivity
import com.royalit.mvr.Config.Preferences
import com.royalit.mvr.Config.ViewController
import com.royalit.mvr.Model.MyTeamsData
import com.royalit.mvr.Model.MyTeamsModel
import com.royalit.mvr.Model.SaleData
import com.royalit.mvr.Model.SaleModel
import com.royalit.mvr.Retrofit.RetrofitClient
import com.royalit.mvr.databinding.ActivityAboutUsBinding
import com.royalit.mvr.databinding.ActivitySalesBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.Serializable

class SalesActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySalesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySalesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        toolbarSetup()

        if(!ViewController.noInterNetConnectivity(applicationContext)){
            ViewController.showToast(applicationContext, "Please check your connection ")
        }else{
            saleListApi()
        }

    }

    fun toolbarSetup(){
        val toolbar = binding.materialtoolbar
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            title = "Sales History"
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

    private fun saleListApi() {
        val userId = Preferences.loadStringValue(this@SalesActivity, Preferences.userId, "")

        ViewController.showLoading(this@SalesActivity)
        val apiInterface = RetrofitClient.apiInterface
        apiInterface.saleListApi(userId).enqueue(object : Callback<SaleModel> {
            override fun onResponse(call: Call<SaleModel>, response: Response<SaleModel>) {
                ViewController.hideLoading()
                if (response.isSuccessful) {
                    val rsp = response.body()
                    if (rsp != null) {
                        val translist = response.body()
                        if (translist != null) {
                            if (!translist.data.isNullOrEmpty()) {
                                binding.recyclerView.visibility = View.VISIBLE
                                binding.noData.visibility = View.GONE
                                saleListDataSet(translist.data!!)
                            }else{
                                binding.recyclerView.visibility = View.GONE
                                binding.noData.visibility = View.VISIBLE
                            }
                        }
                    }
                } else {
                    binding.recyclerView.visibility = View.GONE
                    binding.noData.visibility = View.VISIBLE
                }
            }
            override fun onFailure(call: Call<SaleModel>, t: Throwable) {
                binding.recyclerView.visibility = View.GONE
                binding.noData.visibility = View.VISIBLE
                ViewController.hideLoading()
                ViewController.showToast(this@SalesActivity, "Try again: ${t.message}")
            }

        })
    }

    private fun saleListDataSet(list: List<SaleData>) {
        binding.recyclerView.layoutManager = LinearLayoutManager(this@SalesActivity)
        binding.recyclerView.adapter = SaleAdapter(list) { item ->
            //Toast.makeText(activity, "Clicked: ${item.text}", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, SaleHistoryDetailsActivity::class.java)
            intent.putExtra("item", item as Serializable)
            startActivity(intent)
        }
    }

}