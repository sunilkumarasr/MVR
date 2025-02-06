package com.royalit.mvr.Authentication

import android.app.NativeActivity
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.royalit.mvr.Config.Preferences
import com.royalit.mvr.Config.ViewController
import com.royalit.mvr.Model.LoginRequest
import com.royalit.mvr.Model.LoginResponse
import com.royalit.mvr.NavigationActivity
import com.royalit.mvr.Retrofit.RetrofitClient
import com.royalit.mvr.databinding.ActivityLoginBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSignin.setOnClickListener{
            if(!ViewController.noInterNetConnectivity(applicationContext)){
                ViewController.showToast(applicationContext, "Please check your connection ")
            }else{
                loginApi()
            }
        }
        binding.txtForgotpass.setOnClickListener{
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }
        binding.txtDonthaveAccount.setOnClickListener{
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }

    }


    private fun loginApi() {
        val mobile=binding.editEmail.text?.trim().toString()
        val password_=binding.editPassword.text?.trim().toString()

        if(mobile.isEmpty()){
            ViewController.showToast(applicationContext, "Enter Mobile Number")
            return
        }
        if(password_.isEmpty()){
            ViewController.showToast(applicationContext, "Enter password")
            return
        }

        if (!validateMobileNumber(mobile)) {
            ViewController.showToast(applicationContext, "Enter Valid Mobile Number")
        }else{
            ViewController.showLoading(this@LoginActivity)

            val apiInterface = RetrofitClient.apiInterface
            val loginRequest = LoginRequest(mobile, password_)

            apiInterface.loginApi(loginRequest).enqueue(object : Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    ViewController.hideLoading()
                    if (response.isSuccessful) {
                        val loginResponse = response.body()
                        if (loginResponse != null && loginResponse.status.equals("success")) {
                            Preferences.saveStringValue(applicationContext, Preferences.userId,
                                loginResponse.user?.id.toString()
                            )
                            Preferences.saveStringValue(applicationContext, Preferences.name,
                                loginResponse.user?.name.toString()
                            )
                            startActivity(Intent(this@LoginActivity, NavigationActivity::class.java))
                            finish()
                        } else {
                            if (loginResponse != null) {
                                ViewController.showToast(applicationContext, loginResponse.message.toString())
                            }
                        }
                    } else {
                        ViewController.showToast(applicationContext, "Error: ${response.code()}")
                    }
                }
                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    ViewController.hideLoading()
                    ViewController.showToast(applicationContext, "Try again: ${t.message}")
                }
            })

        }
    }


    private fun validateMobileNumber(mobile: String): Boolean {
        val mobilePattern = "^[6-9][0-9]{9}\$"
        return Patterns.PHONE.matcher(mobile).matches() && mobile.matches(Regex(mobilePattern))
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }



}