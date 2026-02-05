package com.royalit.mvr.Account

import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.royalit.mvr.Config.Preferences
import com.royalit.mvr.Config.ViewController
import com.royalit.mvr.Model.ProfileModel
import com.royalit.mvr.R
import com.royalit.mvr.Retrofit.RetrofitClient
import com.royalit.mvr.databinding.ActivityProfileBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        toolbarSetup()


        binding.edit.setOnClickListener{
            val intent = Intent(this, EditprofileActivity::class.java)
            startActivity(intent)
        }

        getProfileApi()
    }

    fun toolbarSetup(){
        val toolbar = binding.materialtoolbar
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            title = "My Profile"
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

    private fun getProfileApi() {
        val userId = Preferences.loadStringValue(this@MyProfileActivity, Preferences.userId, "")

        Log.e("userId_", userId.toString())

        ViewController.showLoading(this@MyProfileActivity)
        val apiInterface = RetrofitClient.apiInterface
        apiInterface.getProfileApi(userId).enqueue(object :
            Callback<ProfileModel> {
            override fun onResponse(call: Call<ProfileModel>, response: Response<ProfileModel>) {
                ViewController.hideLoading()
                if (response.isSuccessful) {
                    val rsp = response.body()
                    if (rsp != null) {
                        val translist = response.body()
                        if (translist != null) {
                            binding.txtName.text = translist.user?.name ?:""
                            binding.txtEmailid.text = translist.user?.email ?:""
                            binding.txtFullName.setText(translist.user?.name ?: "")
                            binding.txtFullEmail.setText(translist.user?.email ?: "")
                            binding.txtMobile.setText(translist.user?.phone ?: "")
                            binding.txtAddress.setText(translist.user?.address ?: "")
                            binding.txtCity.setText(translist.user?.city ?: "")
                            Glide.with(this@MyProfileActivity)
                                .load(translist.user?.image ?: "")
                                .placeholder(R.drawable.logo)
                                .error(R.drawable.profileimg)
                                .into(binding.profilePic)
                        }
                    }
                } else {
                    ViewController.showToast(this@MyProfileActivity, "Error: ${response.code()}")
                }
            }
            override fun onFailure(call: Call<ProfileModel>, t: Throwable) {
                ViewController.hideLoading()
                ViewController.showToast(this@MyProfileActivity, "Try again: ${t.message}")
            }
        })
    }

}