package com.royalit.mvr

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.royalit.mvr.Authentication.LoginActivity
import com.royalit.mvr.Config.Preferences

class SplashScreen : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        Handler(Looper.getMainLooper()).postDelayed({
            // Start new activity
            val loginCheck = Preferences.loadStringValue(applicationContext, Preferences.LOGINCHECK, "")

            if (loginCheck.equals("Login")) {
                startActivity(Intent(this@SplashScreen, NavigationActivity::class.java))
            }else{
                startActivity(Intent(this@SplashScreen, LoginActivity::class.java))
            }
        }, 3000)// 3000 milliseconds = 3 seconds

    }


}