package com.royalit.mvr.Account

import android.graphics.PorterDuff
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.royalit.mvr.Adapters.MyTeamsAdapter
import com.royalit.mvr.Adapters.TransactionsAdapter
import com.royalit.mvr.Config.Preferences
import com.royalit.mvr.Config.ViewController
import com.royalit.mvr.Model.MyTeamsData
import com.royalit.mvr.Model.MyTeamsModel
import com.royalit.mvr.Model.TransactionsData
import com.royalit.mvr.Model.TransactionsModel
import com.royalit.mvr.R
import com.royalit.mvr.Retrofit.RetrofitClient
import com.royalit.mvr.databinding.ActivityMyTeamBinding
import com.royalit.mvr.databinding.ActivityNavigationBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyTeamActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyTeamBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyTeamBinding.inflate(layoutInflater)
        setContentView(binding.root)
        toolbarSetup()


        if(!ViewController.noInterNetConnectivity(applicationContext)){
            ViewController.showToast(applicationContext, "Please check your connection ")
        }else{
            myTeamsListApi()
        }

    }


    fun toolbarSetup(){
        val toolbar = binding.materialtoolbar
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            title = "My Team"
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

    private fun myTeamsListApi() {
        val empId = Preferences.loadStringValue(this@MyTeamActivity, Preferences.empId, "")

        ViewController.showLoading(this@MyTeamActivity)
        val apiInterface = RetrofitClient.apiInterface
        apiInterface.myTeamsListApi(empId).enqueue(object : Callback<MyTeamsModel> {
            override fun onResponse(call: Call<MyTeamsModel>, response: Response<MyTeamsModel>) {
                ViewController.hideLoading()
                if (response.isSuccessful) {
                    val rsp = response.body()
                    if (rsp != null) {
                        val translist = response.body()
                        if (translist != null) {
                            if (!translist.data.isNullOrEmpty()) {
                                binding.recyclerView.visibility = View.VISIBLE
                                binding.noData.visibility = View.GONE
                                myTeamsListDataSet(translist.data!!)
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
            override fun onFailure(call: Call<MyTeamsModel>, t: Throwable) {
                binding.recyclerView.visibility = View.GONE
                binding.noData.visibility = View.VISIBLE
                ViewController.hideLoading()
                ViewController.showToast(this@MyTeamActivity, "Try again: ${t.message}")
            }

        })
    }
    private fun myTeamsListDataSet(list: List<MyTeamsData>) {
        binding.recyclerView.layoutManager = LinearLayoutManager(this@MyTeamActivity)
        binding.recyclerView.adapter = MyTeamsAdapter(list) { item ->
            //Toast.makeText(activity, "Clicked: ${item.text}", Toast.LENGTH_SHORT).show()
        }
    }

}