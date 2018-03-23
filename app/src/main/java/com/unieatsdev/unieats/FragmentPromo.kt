package com.unieatsdev.unieats

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.*
import com.squareup.picasso.Callback
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import com.unieatsdev.unieats.RecyclerItemClickListener.OnItemClickListener
import kotlinx.android.synthetic.main.fragment_promo.*

/**
 * Created by Ramshad on 3/11/18.
 */
class FragmentPromo : Fragment() {

    private lateinit var databasePromo: DatabaseReference
    private var promoList: MutableList<PromoInfo> = mutableListOf()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_promo, container, false)

    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var adapter = PromoRecyclerAdapter(activity.baseContext, promoList)
        rvPromoList.layoutManager = LinearLayoutManager(activity)

        databasePromo = FirebaseDatabase.getInstance().getReference("RestaurantPromotionInformation")

        databasePromo.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                promoList.clear()
                for (promoSnapshot in dataSnapshot.children) {
                    val promoInformation = promoSnapshot.getValue(PromoInfo::class.java)
                    promoInformation?.let { promoList.add(it) }
                }

                if (promoList.isNotEmpty()) {
                    rvPromoList.adapter = adapter
                    adapter.notifyDataSetChanged()
                }

            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })

        rvPromoList.addOnItemTouchListener(object : RecyclerItemClickListener(context,
                OnItemClickListener { v, position ->

                    Log.d("FragmentPromoList", "list clicked !")
                    var dialog = Dialog(activity)
                    dialog.setContentView(R.layout.promo_redeem)
                    var tvRedeemRestTitle: TextView = dialog.findViewById(R.id.tvRedeemRestTitle)
                    var ivRedeem: ImageView = dialog.findViewById(R.id.ivRedeem)
                    var tvRedeemOffer: TextView = dialog.findViewById(R.id.tvRedeemOffer)
                    var btnRedeemGetDir: Button = dialog.findViewById(R.id.btnRedeemGetDirection)
                    var lat: String = ""
                    var long: String = ""

                    tvRedeemRestTitle.text = promoList[position].restaurantName
                    tvRedeemOffer.text = promoList[position].restaurantPromo
                    btnRedeemGetDir.setOnClickListener { view ->
                        FirebaseDatabase.getInstance().getReference("RestaurantLocation").addValueEventListener(object : ValueEventListener{

                            override fun onDataChange(snapshot: DataSnapshot) {
                                for (x in snapshot.children) {
                                    if( x.child("title").value == promoList[position].restaurantName) {
                                        lat = x.child("latitude").value.toString()
                                        long = x.child("longitude").value.toString()
                                    }
                                }

                                if (lat.isNotEmpty() && long.isNotEmpty()) {
                                    val gmmIntentUri = Uri.parse("http://maps.google.com/maps?saddr=&daddr=$lat,$long&mode=w")
                                    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                                    mapIntent.`package`= "com.google.android.apps.maps"
                                    startActivity(mapIntent)
                                }
                            }

                            override fun onCancelled(p0: DatabaseError?) {
                            }

                        })
                    }

                    Picasso.with(activity)
                            .load(promoList[position].popUpPromotionImage)
                            .networkPolicy(NetworkPolicy.OFFLINE)
                            .into(ivRedeem, object : Callback {
                                override fun onSuccess() {

                                }

                                override fun onError() {
                                    // Try again online if cache failed
                                    Picasso.with(activity)
                                            .load(promoList[position].popUpPromotionImage)
                                            .into(ivRedeem, object : Callback {
                                                override fun onSuccess() {
                                                }

                                                override fun onError() {

                                                }
                                            })
                                }
                            })

                    dialog.window.setLayout(view?.measuredWidth!! - 50, WindowManager.LayoutParams.WRAP_CONTENT)
                    dialog.show()

                    Toast.makeText(activity, "Show to Redeem", Toast.LENGTH_LONG).show()


                })
        {})
    }
}