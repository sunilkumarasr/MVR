package com.royalit.mvr.Account

import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Patterns
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.royalit.mvr.Config.ViewController
import com.royalit.mvr.Model.HelpSupportResponse
import com.royalit.mvr.NavigationActivity
import com.royalit.mvr.R
import com.royalit.mvr.Retrofit.RetrofitClient
import com.royalit.mvr.databinding.ActivityContactusBinding
import com.royalit.mvr.databinding.ActivityHelpBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HelpActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHelpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHelpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        toolbarSetup()

        binding.btnUpdate.setOnClickListener {
            if(!ViewController.noInterNetConnectivity(applicationContext)){
                ViewController.showToast(applicationContext, "Please check your connection ")
            }else{
                helpAndSupportApi()
            }
        }

    }

    fun toolbarSetup(){
        val toolbar = binding.materialtoolbar
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            title = "Help & Support"
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

    private fun helpAndSupportApi() {
        val name_ = binding.editName.text?.trim().toString()
        val mobile_ = binding.editMobile.text?.trim().toString()
        val email_ = binding.editEmail.text?.trim().toString()
        val message_ = binding.editMessage.text?.trim().toString()


        if(name_.isEmpty()){
            ViewController.showToast(applicationContext, "Enter Name")
            return
        }
        if(mobile_.isEmpty()){
            ViewController.showToast(applicationContext, "Enter Mobile Number")
            return
        }
        if(email_.isEmpty()){
            ViewController.showToast(applicationContext, "Enter Email")
            return
        }
        if(message_.isEmpty()){
            ViewController.showToast(applicationContext, "Enter Message")
            return
        }
        if (!validateMobileNumber(mobile_)) {
            ViewController.showToast(applicationContext, "Enter Valid Mobile Number")
            return
        }
        if (!validateEmail(email_)) {
            ViewController.showToast(applicationContext, "Enter Valid Email")
            return
        }


        val apiInterface = RetrofitClient.apiInterface
        apiInterface.helpAndSupportApi(name_, email_, mobile_, message_).enqueue(object : Callback<HelpSupportResponse> {
            override fun onResponse(call: Call<HelpSupportResponse>, response: Response<HelpSupportResponse>) {
                ViewController.hideLoading()
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse != null && loginResponse.status == "true") {
                        ViewController.showToast(applicationContext, "Help and Support Enquiry Submitted Successfully")
                        startActivity(Intent(this@HelpActivity, NavigationActivity::class.java))
                    } else {
                        loginResponse?.message?.let {
                            ViewController.showToast(applicationContext, it)
                        }
                    }
                } else {
                    ViewController.showToast(applicationContext, "Error: ${response.code()}")
                }
            }
            override fun onFailure(call: Call<HelpSupportResponse>, t: Throwable) {
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