package com.unieatsdev.unieats

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.database.*
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
                    dialog.show()

                    Toast.makeText(activity, "Show to Redeem", Toast.LENGTH_LONG).show()


                })
        {})
    }
}