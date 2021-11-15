package com.abhi.foodrunner.util

import android.content.Context
import android.net.ConnectivityManager

class ConnectionManager {
    fun checkConnectivity(context: Context) : Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        return if(connectivityManager.activeNetworkInfo?.isConnected != null)
            connectivityManager.activeNetworkInfo?.isConnected as Boolean
        else
            false

    }
}