package com.example.eslatmalar

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

@Suppress("DEPRECATION")
class FirstSplashPageActivity : AppCompatActivity() {

    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    private val splashTimeOut: Long = 3000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_first_splash_page)

        sharedPreferencesHelper = SharedPreferencesHelper(this)

        Handler().postDelayed({
            startActivity(nextPage())
            finish()
        }, splashTimeOut)
    }

    private fun nextPage(): Intent {
        val isLogin = sharedPreferencesHelper.getUser()
        return if (isLogin?.isLogin == true) {
            val intent = Intent(this, MainActivity::class.java)
            finish()
            intent
        } else {
            val intent = Intent(this, SignUpPageActivity::class.java)
            finish()
            intent
        }
    }
}