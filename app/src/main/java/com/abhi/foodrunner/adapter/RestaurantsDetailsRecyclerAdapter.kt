package com.abhi.foodrunner.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.abhi.foodrunner.R
import com.abhi.foodrunner.modle.RestaurantDetailInfo

class RestaurantsDetailsRecyclerAdapter(var restaurantDetailInfo: ArrayList<RestaurantDetailInfo>) :
    RecyclerView.Adapter<RestaurantsDetailsRecyclerAdapter.RestaurantsDetailViewHolder>() {
    class RestaurantsDetailViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtNumber: TextView = view.findViewById(R.id.txtNumber)
        val txtDishName: TextView = view.findViewById(R.id.txtDishName)
        val txtPrice: TextView = view.findViewById(R.id.txtPrice)
        val btnAdd: Button = view.findViewById(R.id.btnAdd)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantsDetailViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_restaurants_details_single_row, parent, false)
        return RestaurantsDetailViewHolder(view)
    }

    override fun onBindViewHolder(holder: RestaurantsDetailViewHolder, position: Int) {
        var p=position
        holder.txtNumber.text=(++p).toString()
        holder.txtDishName.text = restaurantDetailInfo[position].name
        holder.txtPrice.text= "Rs.${restaurantDetailInfo[position].cost_for_one}"
    }

    override fun getItemCount(): Int {
        return restaurantDetailInfo.size
    }
}