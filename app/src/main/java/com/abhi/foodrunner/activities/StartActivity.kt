package com.abhi.foodrunner.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.abhi.foodrunner.R

class StartActivity : AppCompatActivity() {

    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        sharedPreferences =
            getSharedPreferences(getString(R.string.preferences_file), Context.MODE_PRIVATE)

        val handler = Handler()
        val runnable = {
            if (sharedPreferences.getBoolean("isLoggedIn", false)) {
                val intent = Intent(this,MainActivity::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
        }
        handler.postDelayed(runnable, 3000)
    }

    override fun onPause() {
        super.onPause()
        finish()
    }
}