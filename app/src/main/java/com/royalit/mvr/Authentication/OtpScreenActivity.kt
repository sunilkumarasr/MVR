package com.royalit.mvr.Authentication

import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import com.royalit.mvr.Config.ViewController
import com.royalit.mvr.Model.EmailRequest
import com.royalit.mvr.Model.ForgotEmailResponse
import com.royalit.mvr.Model.OTPRequest
import com.royalit.mvr.Model.OTPResponse
import com.royalit.mvr.R
import com.royalit.mvr.Retrofit.RetrofitClient
import com.royalit.mvr.databinding.ActivityCreateNewPasswordBinding
import com.royalit.mvr.databinding.ActivityForgotPasswordBinding
import com.royalit.mvr.databinding.ActivityOtpScreenBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OtpScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOtpScreenBinding

    lateinit var email:String

    fun EditText.showKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        this.requestFocus()
        imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityOtpScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        email= intent.getStringExtra("email").toString()

        toolbarSetup()
        setupOtpInputs()

        var count = 0
        fun setFocusable(){
            count++
            binding.otpDigit1.isFocusable = true
            binding.otpDigit1.isFocusableInTouchMode = true
            binding.otpDigit2.isFocusable = true
            binding.otpDigit2.isFocusableInTouchMode = true
            binding.otpDigit3.isFocusable = true
            binding.otpDigit3.isFocusableInTouchMode = true
            binding.otpDigit4.isFocusable = true
            binding.otpDigit4.isFocusableInTouchMode = true
        }
        binding.otpDigit1.setOnClickListener {
            if(count==0){
                setFocusable()
                binding.otpDigit1.requestFocus()
                binding.otpDigit1.showKeyboard()
            }
        }
        binding.otpDigit2.setOnClickListener {
            if(count==0){
                setFocusable()
                binding.otpDigit2.requestFocus()
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.otpDigit2.showKeyboard()
                },100)

            }
        }
        binding.otpDigit3.setOnClickListener {
            if(count==0){
                setFocusable()
                binding.otpDigit3.requestFocus()
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.otpDigit3.showKeyboard()
                },100)

            }
        }
        binding.otpDigit4.setOnClickListener {
            if(count==0){
                setFocusable()
                binding.otpDigit4.requestFocus()
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.otpDigit4.showKeyboard()
                },100)

            }
        }

        binding.otpDigit1.addTextChangedListener {
            if (it?.toString()?.length == 1) binding.otpDigit2.requestFocus()
        }
        binding.otpDigit2.addTextChangedListener {
            if (it?.toString()?.length == 1) binding.otpDigit3.requestFocus() else binding.otpDigit1.requestFocus()
        }
        binding.otpDigit3.addTextChangedListener {
            if (it?.toString()?.length == 1) binding.otpDigit4.requestFocus() else binding.otpDigit2.requestFocus()
        }
        binding.otpDigit4.addTextChangedListener {
            if ((it?.toString()?.length?:0)<1) binding.otpDigit3.requestFocus()
        }

        binding.txtResend.setOnClickListener{
            forgotApi()
        }

        binding.btnSignin.setOnClickListener{
            otpApi()
        }

    }

    private fun setupOtpInputs() {
        val otpInputs = listOf(
            binding.otpDigit1,
            binding.otpDigit2,
            binding.otpDigit3,
            binding.otpDigit4
        )

        otpInputs.forEachIndexed { index, editText ->
            editText.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    editText.background = if (s?.isNotEmpty() == true) {
                        ContextCompat.getDrawable(this@OtpScreenActivity, R.drawable.text_field_entered)
                    } else {
                        ContextCompat.getDrawable(this@OtpScreenActivity, R.drawable.text_field_default)
                    }

                    if (s?.length == 1 && index < otpInputs.size - 1) {
                        otpInputs[index + 1].requestFocus() // Move to the next input
                    } else if (s?.isEmpty() == true && index > 0) {
                        otpInputs[index - 1].requestFocus() // Move back to the previous input
                    }
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })
        }
    }
    fun toolbarSetup(){
        val toolbar = binding.materialtoolbar
        setSupportActionBar(toolbar)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            title = "Confirm Otp"
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

    private fun otpApi() {

        val pin1 = binding.otpDigit1.editableText.trim().toString()
        val pin2 = binding.otpDigit2.editableText.trim().toString()
        val pin3 = binding.otpDigit3.editableText.trim().toString()
        val pin4 = binding.otpDigit4.editableText.trim().toString()

        if(validateOtp()){
            if(!ViewController.noInterNetConnectivity(applicationContext)){
                ViewController.showToast(applicationContext, "Please check your connection ")
                return
            }else{
                ViewController.showLoading(this@OtpScreenActivity)

                val apiInterface = RetrofitClient.apiInterface
                val loginRequest = OTPRequest(email, "$pin1$pin2$pin3$pin4")

                apiInterface.otpApi(loginRequest).enqueue(object : Callback<OTPResponse> {
                    override fun onResponse(call: Call<OTPResponse>, response: Response<OTPResponse>) {
                        ViewController.hideLoading()
                        if (response.isSuccessful) {
                            val loginResponse = response.body()
                            if (loginResponse != null && loginResponse.status.equals("success")) {
                                startActivity(Intent(this@OtpScreenActivity,CreateNewPasswordActivity::class.java).apply {
                                    putExtra("email",email)
                                })
                                finish()
                            } else {
                                ViewController.showToast(applicationContext, "Otp Failed")
                            }
                        } else {
                            ViewController.showToast(applicationContext, "Error: ${response.code()}")
                        }
                    }

                    override fun onFailure(call: Call<OTPResponse>, t: Throwable) {
                        ViewController.hideLoading()
                        ViewController.showToast(applicationContext, "Try again: ${t.message}")
                    }
                })
            }
        }else{
            ViewController.showToast(applicationContext, "enter valid otp")
        }

    }

    private fun validateOtp():Boolean{
        val pin1 = binding.otpDigit1.editableText.trim().toString()
        val pin2 = binding.otpDigit2.editableText.trim().toString()
        val pin3 = binding.otpDigit3.editableText.trim().toString()
        val pin4 = binding.otpDigit4.editableText.trim().toString()
        return  pin1.isNotEmpty() && pin2.isNotEmpty() && pin3.isNotEmpty() && pin4.isNotEmpty()
    }

    //resend otp
    private fun forgotApi() {
        if(!ViewController.noInterNetConnectivity(applicationContext)){
            ViewController.showToast(applicationContext, "Please check your connection ")
            return
        }else{
            ViewController.showLoading(this@OtpScreenActivity)

            val apiInterface = RetrofitClient.apiInterface
            val forgotRequest = EmailRequest(email)

            apiInterface.forgotEmailApi(forgotRequest).enqueue(object :
                Callback<ForgotEmailResponse> {
                override fun onResponse(call: Call<ForgotEmailResponse>, response: Response<ForgotEmailResponse>) {
                    ViewController.hideLoading()
                    if (response.isSuccessful) {
                        val loginResponse = response.body()
                        if (loginResponse != null && loginResponse.status.equals("success")) {
                            ViewController.showToast(applicationContext, "Success")
                        } else {
                            ViewController.showToast(applicationContext, "Otp Sending failed")
                        }
                    } else {
                        ViewController.showToast(applicationContext, "Error: ${response.code()}")
                    }
                }
                override fun onFailure(call: Call<ForgotEmailResponse>, t: Throwable) {
                    ViewController.hideLoading()
                    ViewController.showToast(applicationContext, "Try again: ${t.message}")
                }
            })
        }
    }


}