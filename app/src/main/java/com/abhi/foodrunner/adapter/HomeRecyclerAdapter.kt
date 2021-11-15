package com.abhi.foodrunner.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.abhi.foodrunner.modle.RestaurantInfo
import com.abhi.foodrunner.R
import com.abhi.foodrunner.activities.RestaurantsDetails
import com.squareup.picasso.Picasso

class HomeRecyclerAdapter(val context : Context, var RestaurantsInfo : ArrayList<RestaurantInfo>) : RecyclerView.Adapter<HomeRecyclerAdapter.HomeViewHolder>() {
    class HomeViewHolder(view:View) : RecyclerView.ViewHolder(view){
        val imgRestaurant : ImageView = view.findViewById(R.id.imgRestaurant)
        val txtRestaurantName : TextView = view.findViewById(R.id.txtRestaurantName)
        val imgAddToFav : ImageView = view.findViewById(R.id.imgAddToFav)
        val txtRestaurantPrice : TextView = view.findViewById(R.id.txtRestaurantPrice)
        val txtRating : TextView = view.findViewById(R.id.txtRating)
        val llContent : LinearLayout = view.findViewById(R.id.llContent)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_home_single_row,parent,false)
        return HomeViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.txtRestaurantName.text = RestaurantsInfo[position].name
        holder.txtRestaurantPrice.text = RestaurantsInfo[position].cost_for_one
        holder.txtRating.text = RestaurantsInfo[position].rating
        Picasso.get().load(RestaurantsInfo[position].image_url).error(R.drawable.default_restaurant_image).into(holder.imgRestaurant)

        holder.llContent.setOnClickListener {
            val intent = Intent(context,RestaurantsDetails::class.java)
            intent.putExtra("id",RestaurantsInfo[position].id)
            intent.putExtra("name",RestaurantsInfo[position].name)
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return RestaurantsInfo.size
    }
}