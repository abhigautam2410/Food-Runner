package com.abhi.foodrunner.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abhi.foodrunner.R
import com.abhi.foodrunner.adapter.HomeRecyclerAdapter
import com.abhi.foodrunner.modle.RestaurantInfo
import com.abhi.foodrunner.util.ConnectionManager
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import java.lang.Exception

class HomeFragment : Fragment() {

    lateinit var recyclerHome: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var homeRecyclerAdapter: HomeRecyclerAdapter
    lateinit var progressBar : RelativeLayout
    
    var dataRestaurant = arrayListOf<RestaurantInfo>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        progressBar = view.findViewById(R.id.progressBar)
        progressBar.visibility = View.VISIBLE

        recyclerHome = view.findViewById(R.id.recyclerHome)

        layoutManager = LinearLayoutManager(activity)


        val requestQueue = Volley.newRequestQueue(activity as Context)
        val url = "http://13.235.250.119/v2/restaurants/fetch_result"

        if (ConnectionManager().checkConnectivity(activity as Context)) {
            val jsonObjectRequest =
                object : JsonObjectRequest(Method.GET, url, null, Response.Listener {
                    val dataOut = it.getJSONObject("data")
                    val success = dataOut.getBoolean("success")
                    try {
                        if (success) {
                            progressBar.visibility=View.GONE
                            val data = dataOut.getJSONArray("data")
                            for (i in 0 until data.length()) {
                                val restaurantInfoJsonObject = data.getJSONObject(i)
                                val restaurantInfoObject = RestaurantInfo(
                                    restaurantInfoJsonObject.getString("id"),
                                    restaurantInfoJsonObject.getString("name"),
                                    restaurantInfoJsonObject.getString("rating"),
                                    restaurantInfoJsonObject.getString("cost_for_one"),
                                    restaurantInfoJsonObject.getString("image_url")
                                )
                                dataRestaurant.add(restaurantInfoObject)
                            }
                            homeRecyclerAdapter = HomeRecyclerAdapter(activity as Context,dataRestaurant)

                            recyclerHome.layoutManager = layoutManager
                            recyclerHome.adapter = homeRecyclerAdapter

                        } else {
                            Toast.makeText(
                                activity as Context,
                                "Some error occurred!!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }catch (e : Exception){
                        Toast.makeText(
                            activity as Context,
                            "Some unexpected error occurred!!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }, Response.ErrorListener {
                    Toast.makeText(
                        activity as Context,
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
            val dialog = AlertDialog.Builder(activity as Context)
            dialog.setTitle("Network Error")
            dialog.setMessage("Internet no Connected")
            dialog.setPositiveButton("OPEN SETTING") {text,listner ->
                val settingIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingIntent)
            }
            dialog.setNegativeButton("EXIT") { _, _ ->
                ActivityCompat.finishAffinity(activity as Activity)
            }
            dialog.create()
            dialog.show()
        }
        return view
    }
}