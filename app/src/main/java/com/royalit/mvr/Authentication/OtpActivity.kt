package com.royalit.mvr.Authentication

import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.royalit.mvr.R
import com.royalit.mvr.databinding.ActivityOtpScreenBinding

class OtpActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOtpScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        toolbarSetup()
        setupOtpInputs()
        binding.btnSignin.setOnClickListener{
            val intent = Intent(this, CreateNewPasswordActivity::class.java)
            startActivity(intent)
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
                        ContextCompat.getDrawable(this@OtpActivity, R.drawable.text_field_entered)
                    }else {
                        ContextCompat.getDrawable(this@OtpActivity, R.drawable.text_field_default)
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


}