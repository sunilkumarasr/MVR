package com.royalit.mvr.Authentication

import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.royalit.mvr.Config.ViewController
import com.royalit.mvr.Model.CreatePasswordRequest
import com.royalit.mvr.Model.CreatePasswordResponse
import com.royalit.mvr.R
import com.royalit.mvr.Retrofit.RetrofitClient
import com.royalit.mvr.databinding.ActivityCreateNewPasswordBinding
import com.royalit.mvr.databinding.ActivityForgotPasswordBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateNewPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateNewPasswordBinding

    lateinit var email:String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCreateNewPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        toolbarSetup()

        email= intent.getStringExtra("email").toString()

        binding.btnSignin.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            if(!ViewController.noInterNetConnectivity(applicationContext)){
                ViewController.showToast(applicationContext, "Please check your connection ")
            }else{
                createApi()
            }
        }
    }

    fun toolbarSetup(){
        val toolbar = binding.materialtoolbar
        setSupportActionBar(toolbar)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            title = "Create New Password"
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


    private fun createApi() {
        val password_=binding.editPassword.text?.trim().toString()
        val cpassword_=binding.editCPassword.text?.trim().toString()

        if(password_.isEmpty()){
            ViewController.showToast(applicationContext, "Enter Password")
            return
        }
        if(cpassword_.isEmpty()){
            ViewController.showToast(applicationContext, "Enter Conform Password")
            return
        }
        if(!password_.equals(cpassword_)){
            ViewController.showToast(applicationContext, "Enter password conform password not match")
            return
        }
        ViewController.showLoading(this@CreateNewPasswordActivity)
        val apiInterface = RetrofitClient.apiInterface
        val ceateP = CreatePasswordRequest(email, password_, cpassword_)

        apiInterface.createApi(ceateP).enqueue(object :
            Callback<CreatePasswordResponse> {
            override fun onResponse(call: Call<CreatePasswordResponse>, response: Response<CreatePasswordResponse>) {
                ViewController.hideLoading()
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse != null && loginResponse.status.equals("success")) {
                        ViewController.showToast(applicationContext, "success")
                        startActivity(Intent(this@CreateNewPasswordActivity, LoginActivity::class.java))
                    } else {
                        ViewController.showToast(applicationContext, "Wrong Email Address")
                    }
                } else {
                    ViewController.showToast(applicationContext, "Error: ${response.code()}")
                }
            }
            override fun onFailure(call: Call<CreatePasswordResponse>, t: Throwable) {
                ViewController.hideLoading()
                ViewController.showToast(applicationContext, "Try again: ${t.message}")
            }
        })

    }


    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this@CreateNewPasswordActivity, LoginActivity::class.java))
    }

}