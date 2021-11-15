package com.abhi.foodrunner.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.EditText
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

class ForgotPassword : AppCompatActivity() {
    lateinit var etMobile: EditText
    lateinit var etEmail: EditText
    lateinit var btnNext: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        etMobile = findViewById(R.id.etMobile)
        etEmail = findViewById(R.id.etEmail)
        btnNext = findViewById(R.id.btnNext)

        btnNext.setOnClickListener {
            val requestQueue = Volley.newRequestQueue(this)
            val url = "http://13.235.250.119/v2/forgot_password/fetch_result"
            val jsonParams = JSONObject()
            jsonParams.put("mobile_number", etMobile.text.toString())
            jsonParams.put("email", etEmail.text.toString())
            if (ConnectionManager().checkConnectivity(this)) {
                val jsonObjectRequest =
                    object : JsonObjectRequest(Method.POST, url, jsonParams, Response.Listener {
                        try {
                            val data = it.getJSONObject("data")
                            val success = data.getBoolean("success")
                            if (success) {
                                val intent = Intent(this,ResetPassword::class.java)
                                intent.putExtra("mobile_number",etMobile.text.toString())
                                startActivity(intent)
                            } else {
                                Toast.makeText(
                                    this,
                                    data.getString("errorMessage"),
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
                            val headers = HashMap<String, String>()
                            headers.put("token", "9bf534118365f1")
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
    }
}