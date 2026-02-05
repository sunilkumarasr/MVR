package com.royalit.mvr

import android.graphics.PorterDuff
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.royalit.mvr.Adapters.TransactionsAdapter
import com.royalit.mvr.Config.Preferences
import com.royalit.mvr.Config.ViewController
import com.royalit.mvr.Model.TransactionsData
import com.royalit.mvr.Model.TransactionsModel
import com.royalit.mvr.databinding.ActivityTransactionsBinding
import com.royalit.mvr.Retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TransactionsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTransactionsBinding


    private fun updateSelection(selected: View, unselected1: View, unselected2: View, selectedTextColor: TextView, unselectedTextColor1: TextView, unselectedTextColor2: TextView) {
        selected.background = ContextCompat.getDrawable(this, R.drawable.border_with_black_radius)
        unselected1.background = ContextCompat.getDrawable(this, R.drawable.border_with_radius)
        unselected2.background = ContextCompat.getDrawable(this, R.drawable.border_with_radius)

        selectedTextColor.setTextColor(ContextCompat.getColor(this@TransactionsActivity, R.color.white))
        unselectedTextColor1.setTextColor(ContextCompat.getColor(this@TransactionsActivity, R.color.black))
        unselectedTextColor2.setTextColor(ContextCompat.getColor(this@TransactionsActivity, R.color.black))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityTransactionsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        toolbarSetup()


        if(!ViewController.noInterNetConnectivity(applicationContext)){
            ViewController.showToast(applicationContext, "Please check your connection ")
        }else{
            allTransactionsListApi()
        }

        binding.linearAll.setOnClickListener {
            updateSelection(binding.linearAll, binding.linearCompleted, binding.linearRejected, binding.txtAll, binding.txtCompleted, binding.txtReject)

            if(!ViewController.noInterNetConnectivity(applicationContext)){
                ViewController.showToast(applicationContext, "Please check your connection ")
            }else{
                allTransactionsListApi()
            }
        }
        binding.linearCompleted.setOnClickListener {
            updateSelection(binding.linearCompleted, binding.linearAll, binding.linearRejected, binding.txtCompleted, binding.txtAll, binding.txtReject)

            if(!ViewController.noInterNetConnectivity(applicationContext)){
                ViewController.showToast(applicationContext, "Please check your connection ")
            }else{
                completedTransactionsListApi()
            }
        }
        binding.linearRejected.setOnClickListener {
            updateSelection(binding.linearRejected, binding.linearAll, binding.linearCompleted, binding.txtReject, binding.txtAll, binding.txtCompleted)

            if(!ViewController.noInterNetConnectivity(applicationContext)){
                ViewController.showToast(applicationContext, "Please check your connection ")
            }else{
                rejectedTransactionsListApi()
            }
        }
    }

    fun toolbarSetup(){
        val toolbar = binding.materialtoolbar
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            title = "Transactions"
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

    private fun allTransactionsListApi() {
        val userId = Preferences.loadStringValue(this@TransactionsActivity, Preferences.userId, "")

        ViewController.showLoading(this@TransactionsActivity)
        val apiInterface = RetrofitClient.apiInterface
        apiInterface.allTransactionsListApi(userId).enqueue(object : Callback<TransactionsModel> {
            override fun onResponse(call: Call<TransactionsModel>, response: Response<TransactionsModel>) {
                ViewController.hideLoading()
                if (response.isSuccessful) {
                    val rsp = response.body()
                    if (rsp != null) {
                        val translist = response.body()
                        if (translist != null) {
                            if (!translist.data.isNullOrEmpty()) {
                                binding.recyclerView.visibility = View.VISIBLE
                                binding.noData.visibility = View.GONE
                                allTransactionsDataSet(translist.data!!)
                            }else{
                                binding.recyclerView.visibility = View.GONE
                                binding.noData.visibility = View.VISIBLE
                            }
                        }
                    }
                } else {
                    ViewController.showToast(this@TransactionsActivity, "Error: ${response.code()}")
                }
            }
            override fun onFailure(call: Call<TransactionsModel>, t: Throwable) {
                binding.recyclerView.visibility = View.GONE
                binding.noData.visibility = View.VISIBLE
                ViewController.hideLoading()
                ViewController.showToast(this@TransactionsActivity, "Try again: ${t.message}")
            }
        })

    }

    private fun completedTransactionsListApi() {
        val userId = Preferences.loadStringValue(this@TransactionsActivity, Preferences.userId, "")

        ViewController.showLoading(this@TransactionsActivity)
        val apiInterface = RetrofitClient.apiInterface
        apiInterface.completedTransactionsListApi(userId).enqueue(object : Callback<TransactionsModel> {
            override fun onResponse(call: Call<TransactionsModel>, response: Response<TransactionsModel>) {
                ViewController.hideLoading()
                if (response.isSuccessful) {
                    val rsp = response.body()
                    if (rsp != null) {
                        val translist = response.body()
                        if (translist != null) {
                            if (!translist.data.isNullOrEmpty()) {
                                binding.recyclerView.visibility = View.VISIBLE
                                binding.noData.visibility = View.GONE
                                allTransactionsDataSet(translist.data!!)
                            }else{
                                binding.recyclerView.visibility = View.GONE
                                binding.noData.visibility = View.VISIBLE
                            }
                        }
                    }
                } else {
                    ViewController.showToast(this@TransactionsActivity, "Error: ${response.code()}")
                }
            }
            override fun onFailure(call: Call<TransactionsModel>, t: Throwable) {
                binding.recyclerView.visibility = View.GONE
                binding.noData.visibility = View.VISIBLE
                ViewController.hideLoading()
                ViewController.showToast(this@TransactionsActivity, "Try again: ${t.message}")
            }
        })

    }

    private fun rejectedTransactionsListApi() {
        val userId = Preferences.loadStringValue(this@TransactionsActivity, Preferences.userId, "")

        ViewController.showLoading(this@TransactionsActivity)
        val apiInterface = RetrofitClient.apiInterface
        apiInterface.rejectedTransactionsListApi(userId).enqueue(object : Callback<TransactionsModel> {
            override fun onResponse(call: Call<TransactionsModel>, response: Response<TransactionsModel>) {
                ViewController.hideLoading()
                if (response.isSuccessful) {
                    val rsp = response.body()
                    if (rsp != null) {
                        val translist = response.body()
                        if (translist != null) {
                            if (!translist.data.isNullOrEmpty()) {
                                binding.recyclerView.visibility = View.VISIBLE
                                binding.noData.visibility = View.GONE
                                allTransactionsDataSet(translist.data!!)
                            }else{
                                binding.recyclerView.visibility = View.GONE
                                binding.noData.visibility = View.VISIBLE
                            }
                        }
                    }
                } else {
                    ViewController.showToast(this@TransactionsActivity, "Error: ${response.code()}")
                }
            }
            override fun onFailure(call: Call<TransactionsModel>, t: Throwable) {
                binding.recyclerView.visibility = View.GONE
                binding.noData.visibility = View.VISIBLE
                ViewController.hideLoading()
                ViewController.showToast(this@TransactionsActivity, "Try again: ${t.message}")
            }
        })

    }

    private fun allTransactionsDataSet(list: List<TransactionsData>) {
        binding.recyclerView.layoutManager = LinearLayoutManager(this@TransactionsActivity)
        binding.recyclerView.adapter = TransactionsAdapter(list) { item ->
            //Toast.makeText(activity, "Clicked: ${item.text}", Toast.LENGTH_SHORT).show()
        }
    }

}