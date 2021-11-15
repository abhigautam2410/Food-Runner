package com.abhi.foodrunner.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.abhi.foodrunner.R
import com.abhi.foodrunner.util.ConnectionManager
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import java.lang.Exception

class LoginActivity : AppCompatActivity() {
    lateinit var etMobile: EditText
    lateinit var etPassword: EditText
    lateinit var btnLogin: Button
    lateinit var txtForgotPassword: TextView
    lateinit var txtSignUpReques: TextView
    lateinit var sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        etMobile = findViewById(R.id.etMobile)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        txtForgotPassword = findViewById(R.id.txtForgotPassword)
        txtSignUpReques = findViewById(R.id.txtSingUpRequest)
        sharedPreferences = getSharedPreferences(getString(R.string.preferences_file), Context.MODE_PRIVATE)

        btnLogin.setOnClickListener {
            val requestQueue = Volley.newRequestQueue(this)
            val url = "http://13.235.250.119/v2/login/fetch_result"
            val jsonParams = JSONObject()
            jsonParams.put("mobile_number",etMobile.text.toString())
            jsonParams.put("password",etPassword.text.toString())
            if (ConnectionManager().checkConnectivity(this)) {
                val jsonObjectRequest =
                    object : JsonObjectRequest(Method.POST, url, jsonParams, Response.Listener {
                        try {
                            val dataOut = it.getJSONObject("data")
                            val success = dataOut.getBoolean("success")
                            if(success){
                                val data = dataOut.getJSONObject("data")
                                sharedPreferences.edit().putBoolean("isLoggedIn",true).apply()
                                sharedPreferences.edit().putString("user_id",data.getString("user_id")).apply()
                                sharedPreferences.edit().putString("name",data.getString("name")).apply()
                                sharedPreferences.edit().putString("email",data.getString("email")).apply()
                                sharedPreferences.edit().putString("mobile_number",data.getString("mobile_number")).apply()
                                sharedPreferences.edit().putString("address",data.getString("address")).apply()
                                val intent = Intent(this,MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            }else{
                                Toast.makeText(
                                    this,
                                    "Some error occurred!!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } catch (e: Exception) {
                            Toast.makeText(
                                this,
                                "Some unexpected error occurred!!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }, Response.ErrorListener {
                        Toast.makeText(
                            this,
                            "volley error occurred!!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }) {
                        override fun getHeaders(): MutableMap<String, String> {
                            val headers = HashMap<String,String>()
                            headers.put("token","9bf534118365f1")
                            return headers
                        }
                    }
                requestQueue.add(jsonObjectRequest)
            } else {
                val dialog = AlertDialog.Builder(this)
                dialog.setTitle("Network Error")
                dialog.setMessage("Internet no Connected")
                dialog.setPositiveButton("OPEN SETTING") { text, listner ->
                    val settingIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                    startActivity(settingIntent)
                }
                dialog.setNegativeButton("EXIT") { _, _ ->
                    ActivityCompat.finishAffinity(this)
                }
                dialog.create()
                dialog.show()
            }
        }

        txtSignUpReques.setOnClickListener {
            val intent = Intent(this,RegisrationPage::class.java)
            startActivity(intent)
        }

        txtForgotPassword.setOnClickListener {
            val intent = Intent(this,ForgotPassword::class.java)
            startActivity(intent)
        }
    }

}