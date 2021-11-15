package com.abhi.foodrunner.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.MenuItem
import android.view.View
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abhi.foodrunner.R
import com.abhi.foodrunner.adapter.HomeRecyclerAdapter
import com.abhi.foodrunner.adapter.RestaurantsDetailsRecyclerAdapter
import com.abhi.foodrunner.modle.RestaurantDetailInfo
import com.abhi.foodrunner.modle.RestaurantInfo
import com.abhi.foodrunner.util.ConnectionManager
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import java.lang.Exception

class RestaurantsDetails : AppCompatActivity() {
    lateinit var restaurantDetailRecycler : RecyclerView
    lateinit var progressBar : RelativeLayout
    lateinit var layoutManager : RecyclerView.LayoutManager
    lateinit var restaurantsDetailsRecyclerAdapter : RestaurantsDetailsRecyclerAdapter
    lateinit var toolbar: Toolbar

    var dataRestaurantsDetails = arrayListOf<RestaurantDetailInfo>()
    var restaurantId="0"
    var restaurantsName="Restaurants Name"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurants_details)

        progressBar = findViewById(R.id.progressBar)
        progressBar.visibility=View.VISIBLE

        restaurantDetailRecycler = findViewById(R.id.restaurantDetailRecycler)
        layoutManager = LinearLayoutManager(this)

        if(intent!=null){
            restaurantId=intent.getStringExtra("id").toString()
            restaurantsName=intent.getStringExtra("name").toString()
        }

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title=restaurantsName
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        val requestQueue = Volley.newRequestQueue(this)
        val url = "http://13.235.250.119/v2/restaurants/fetch_result/${restaurantId}"

        if (ConnectionManager().checkConnectivity(this)) {
            val jsonObjectRequest =
                object : JsonObjectRequest(Method.GET, url, null, Response.Listener {
                    val dataOut = it.getJSONObject("data")
                    val success = dataOut.getBoolean("success")
                    try {
                        if (success) {
                            progressBar.visibility=View.GONE
                            val data = dataOut.getJSONArray("data")
                            for (i in 0 until data.length()) {
                                val restaurantDetailInfoJsonObject = data.getJSONObject(i)
                                val restaurantDetailInfoObject = RestaurantDetailInfo(
                                    restaurantDetailInfoJsonObject.getString("id"),
                                    restaurantDetailInfoJsonObject.getString("name"),
                                    restaurantDetailInfoJsonObject.getString("cost_for_one"),
                                    restaurantDetailInfoJsonObject.getString("restaurant_id"),
                                )
                                dataRestaurantsDetails.add(restaurantDetailInfoObject)
                            }
                            restaurantsDetailsRecyclerAdapter = RestaurantsDetailsRecyclerAdapter(dataRestaurantsDetails)
                            restaurantDetailRecycler.layoutManager = layoutManager
                            restaurantDetailRecycler.adapter = restaurantsDetailsRecyclerAdapter

                        } else {
                            Toast.makeText(
                                this,
                                "Some error occurred!!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }catch (e : Exception){
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
            dialog.setMessage("Internet no Connected")
            dialog.setPositiveButton("OPEN SETTING") {text,listner ->
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val itemId = item.itemId
        if(itemId == android.R.id.home)
            onBackPressed()
        return super.onOptionsItemSelected(item)
    }
}