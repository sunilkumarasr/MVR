package com.royalit.mvr.Authentication

import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.text.Html
import android.util.Patterns
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.royalit.mvr.Config.ViewController
import com.royalit.mvr.Model.RegisterRequest
import com.royalit.mvr.Model.RegisterResponse
import com.royalit.mvr.R
import com.royalit.mvr.Retrofit.RetrofitClient
import com.royalit.mvr.databinding.ActivityForgotPasswordBinding
import com.royalit.mvr.databinding.ActivitySignupBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        toolbarSetup()

        binding.txtHaveAccount.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        binding.btnSignin.setOnClickListener{
            if(!ViewController.noInterNetConnectivity(applicationContext)){
                ViewController.showToast(applicationContext, "Please check your connection ")
            }else{
                registerApi()
            }
        }


    }
    fun toolbarSetup(){
        val toolbar = binding.materialtoolbar
        setSupportActionBar(toolbar)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            title = "Sign Up"

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


    private fun registerApi() {

        val name_=binding.editName.text.toString()
        val mobileNumber_=binding.editPhone.text?.trim().toString()
        val email = binding.editEmail.text?.trim().toString()
        val city=binding.editCity.text?.trim().toString()
        val address=binding.editAddress.text?.trim().toString()
        val ReferId=binding.editReferId.text?.trim().toString()
        val password=binding.editPassword.text?.trim().toString()
        val cpassword=binding.editCPassword.text?.trim().toString()

        if(name_.isEmpty()){
            ViewController.showToast(applicationContext, "Enter name")
            return
        }
        if(mobileNumber_.isEmpty()){
            ViewController.showToast(applicationContext, "Enter mobile number")
            return
        }
        if(email.isEmpty()){
            ViewController.showToast(applicationContext, "Enter Email")
            return
        }
        if(city.isEmpty()){
            ViewController.showToast(applicationContext, "Enter City")
            return
        }
        if(address.isEmpty()){
            ViewController.showToast(applicationContext, "Enter Address")
            return
        }
        if(password.isEmpty()){
            ViewController.showToast(applicationContext, "Enter Password")
            return
        }
        if(cpassword.isEmpty()){
            ViewController.showToast(applicationContext, "Enter Confirm Password")
            return
        }
        if(password!=cpassword){
            ViewController.showToast(applicationContext, "password and confirm password not match")
            return
        }
        if (!validateEmail(email)) {
            ViewController.showToast(applicationContext, "Enter Valid Email")
            return
        }
        if (!validateMobileNumber(mobileNumber_)) {
            ViewController.showToast(applicationContext, "Enter Valid mobile number")
            return
        }

        ViewController.showLoading(this@SignupActivity)

        val apiInterface = RetrofitClient.apiInterface
        val registerRequest = RegisterRequest(name_, mobileNumber_, email, city, address ,ReferId, password)

        apiInterface.registerApi(registerRequest).enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                ViewController.hideLoading()
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse != null && loginResponse.status.equals("success")) {
                        ViewController.showToast(applicationContext, "success please Login")
                        startActivity(Intent(this@SignupActivity, LoginActivity::class.java))
                    } else {
                        if (loginResponse != null) {
                            Toast.makeText(applicationContext, Html.fromHtml(loginResponse.message.toString(), Html.FROM_HTML_MODE_LEGACY), Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    ViewController.showToast(applicationContext, "Error: ${response.code()}")
                }
            }
            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                ViewController.hideLoading()
                ViewController.showToast(applicationContext, "Try again: ${t.message}")
            }
        })

    }

    private fun validateMobileNumber(mobile: String): Boolean {
        val mobilePattern = "^[6-9][0-9]{9}\$"
        return Patterns.PHONE.matcher(mobile).matches() && mobile.matches(Regex(mobilePattern))
    }

    private fun validateEmail(email: String): Boolean {
        val emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
        return email.matches(Regex(emailPattern))
    }



}