package com.unieatsdev.unieats

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.squareup.picasso.Callback
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso

/**
 * Created by Ramshad on 3/13/18.
 */
class RestRecyclerAdapter(_context: Context, _list: MutableList<RestaurantInformation> = mutableListOf<RestaurantInformation>()) :
        RecyclerView.Adapter<RestRecyclerAdapter.MyViewHolder>() {

    val myContext = _context
    var list = _list


    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MyViewHolder {
        var v = LayoutInflater.from(myContext).inflate(R.layout.rest_list_recycler_content, parent, false)
        var myViewHolder : MyViewHolder= MyViewHolder(v)
        return myViewHolder
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder?, position: Int) {
        holder?.pbRestList?.visibility = View.VISIBLE
        val restaurantInformation = list.get(position)
        holder?.tvRestListTitle?.setText(restaurantInformation.getRestName())
        holder?.tvRestListDesc?.setText(restaurantInformation.getFoodAvailable())
        holder?.tvRestListCost?.setText("Avg Cost: " + "$" + restaurantInformation.getRestAvgPrice() + " per person")

        Picasso.with(myContext)
                .load(restaurantInformation.getRestImageName())
                .placeholder(R.drawable.placeholder2)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(holder?.ivRestListImage, object : Callback {
                    override fun onSuccess() {
                        holder?.pbRestList?.visibility = View.GONE
                    }

                    override fun onError() {
                        // Try again online if cache failed
                        Picasso.with(myContext)
                                .load(restaurantInformation.getRestImageName())
                                .placeholder(R.drawable.placeholder2)
                                .into(holder?.ivRestListImage, object : Callback {
                                    override fun onSuccess() {
                                        holder?.pbRestList?.visibility = View.GONE
                                    }

                                    override fun onError() {

                                    }
                                })
                    }
                })
    }


    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var tvRestListTitle: TextView = view.findViewById(R.id.tvRestListTitle)
        var tvRestListDesc: TextView = view.findViewById(R.id.tvRestListDesc)
        var tvRestListCost: TextView = view.findViewById(R.id.tvRestListCost)
        var ivRestListImage: ImageView = view.findViewById(R.id.ivRestListImage)
        var pbRestList: ProgressBar = view.findViewById(R.id.pbRestList)

    }
}