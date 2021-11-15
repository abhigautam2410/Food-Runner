package com.abhi.foodrunner.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import com.abhi.foodrunner.R
import com.abhi.foodrunner.util.ConnectionManager
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import java.lang.Exception
import java.util.*

class RegisrationPage : AppCompatActivity() {
    lateinit var toolbar: Toolbar
    lateinit var etName: EditText
    lateinit var etEmail: EditText
    lateinit var etMobile: EditText
    lateinit var etDeliveryAddress: EditText
    lateinit var etPassword: EditText
    lateinit var etConformPassword: EditText
    lateinit var btnLogin: Button
    lateinit var sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_regisration_page)
        toolbar = findViewById(R.id.toolbar)
        etName = findViewById(R.id.etName)
        etEmail = findViewById(R.id.etEmail)
        etMobile = findViewById(R.id.etMobile)
        etDeliveryAddress = findViewById(R.id.etDeliveryAddress)
        etPassword = findViewById(R.id.etPassword)
        etConformPassword = findViewById(R.id.etConformPassword)
        btnLogin = findViewById(R.id.btnLogin)
        sharedPreferences =
            getSharedPreferences(getString(R.string.preferences_file), Context.MODE_PRIVATE)

        setSupportActionBar(toolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        btnLogin.setOnClickListener {
            if (etPassword.text.toString() != etConformPassword.text.toString()) {
                Toast.makeText(this, "password mismatch !!", Toast.LENGTH_LONG).show()
            } else {
                val requestQueue = Volley.newRequestQueue(this)
                val url = "http://13.235.250.119/v2/register/fetch_result"
                val params = JSONObject()
                params.put("name", etName.text.toString())
                params.put("mobile_number", etMobile.text.toString())
                params.put("password", etPassword.text.toString())
                params.put("address", etDeliveryAddress.text.toString())
                params.put("email", etEmail.text.toString())

                if (ConnectionManager().checkConnectivity(this)) {
                    val jsonObjectRequest = object : JsonObjectRequest(Method.POST, url, params,
                        Response.Listener {
                            try {
                                val dataOut = it.getJSONObject("data")
                                val success = dataOut.getBoolean("success")
                                if (success) {
                                    val data = dataOut.getJSONObject("data")
                                    sharedPreferences.edit().putBoolean("isLoggedIn", true).apply()
                                    sharedPreferences.edit()
                                        .putString("name", data.getString("name")).apply()
                                    sharedPreferences.edit()
                                        .putString("email", data.getString("email")).apply()
                                    sharedPreferences.edit()
                                        .putString("mobile_number", data.getString("mobile_number"))
                                        .apply()
                                    sharedPreferences.edit()
                                        .putString("address", data.getString("address")).apply()
                                    val intent = Intent(this, MainActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                } else {
                                    Toast.makeText(
                                        this,
                                        dataOut.getString("errorMessage"),
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
                            headers["Content-type"] = "application/json"
                            headers["token"] = "9bf534118365f1"
                            return headers
                        }
                    }
                    requestQueue.add(jsonObjectRequest)
                } else {
                    val dialog = AlertDialog.Builder(this)
                    dialog.setTitle("Network Error")
                    dialog.setMessage("Internet Not Found")
                    dialog.setPositiveButton("Open Setting") { _, _ ->
                        val intent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                        startActivity(intent)
                    }
                    dialog.setNegativeButton("Exit") { _, _ ->
                        finish()
                    }
                    dialog.create()
                    dialog.show()
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home)
            onBackPressed()
        return super.onOptionsItemSelected(item)
    }

}