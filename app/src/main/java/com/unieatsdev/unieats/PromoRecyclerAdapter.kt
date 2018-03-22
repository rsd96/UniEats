package com.unieatsdev.unieats

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Callback
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso

/**
 * Created by Ramshad on 3/22/18.
 */
class PromoRecyclerAdapter(_context: Context, _list: MutableList<PromoInfo> = mutableListOf<PromoInfo>()) :
        RecyclerView.Adapter<PromoRecyclerAdapter.MyViewHolder>() {

    val myContext = _context
    var list = _list


    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MyViewHolder {
        var v = LayoutInflater.from(myContext).inflate(R.layout.promo_list_content, parent, false)
        var myViewHolder: MyViewHolder = MyViewHolder(v)
        return myViewHolder
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder?, position: Int) {

        val promoInfo = list[position]
        holder?.tvPromoRest?.text = promoInfo.restaurantName
        holder?.tvPromoOffer?.text = promoInfo.restaurantPromo
        holder?.tvPromoWhen?.text = promoInfo.restaurantPromotionTimings
        holder?.tvPromoWhere?.text = promoInfo.restaurantLocation

        Picasso.with(myContext)
                .load(promoInfo.restaurantPromotionImage)
                .placeholder(R.drawable.placeholder2)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(holder?.ivPromoMain, object : Callback {
                    override fun onSuccess() {

                    }

                    override fun onError() {
                        // Try again online if cache failed
                        Picasso.with(myContext)
                                .load(promoInfo.restaurantPromotionImage)
                                .placeholder(R.drawable.placeholder2)
                                .into(holder?.ivPromoMain, object : Callback {
                                    override fun onSuccess() {

                                    }

                                    override fun onError() {

                                    }
                                })
                    }
                })
    }


    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var tvPromoRest: TextView = view.findViewById(R.id.tvPromoRest)
        var tvPromoOffer: TextView = view.findViewById(R.id.tvRedeemOffer)
        var tvPromoWhen: TextView = view.findViewById(R.id.tvPromoWhen)
        var tvPromoWhere: TextView = view.findViewById(R.id.tvPromoWhere)
        var ivPromoMain: ImageView = view.findViewById(R.id.ivRedeem)

    }
}
