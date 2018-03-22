package com.unieatsdev.unieats

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.*
import com.unieatsdev.unieats.RecyclerItemClickListener.OnItemClickListener
import kotlinx.android.synthetic.main.fragment_rest_list.*

/**
 * Created by Ramshad on 3/11/18.
 */
class FragmentRestList : Fragment() {

    private lateinit var databaseRestaurantInformation: DatabaseReference
    private var restaurantList: MutableList<RestaurantInformation> = mutableListOf()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_rest_list, container, false)

    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var adapter = RestRecyclerAdapter(activity.baseContext, restaurantList)
        rvRestList.layoutManager = LinearLayoutManager(activity)

        databaseRestaurantInformation = FirebaseDatabase.getInstance().getReference("RestaurantInformation")

        databaseRestaurantInformation.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                restaurantList.clear()
                for (restaurantSnapshot in dataSnapshot.children) {
                    val restaurantInformation = restaurantSnapshot.getValue(RestaurantInformation::class.java)
                    restaurantInformation?.let { restaurantList.add(it) }
                }

                if (restaurantList.isNotEmpty()) {
                    rvRestList.adapter = adapter
                    adapter.notifyDataSetChanged()
                }

            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })

        rvRestList.addOnItemTouchListener(object : RecyclerItemClickListener(context,
                OnItemClickListener { view, position ->

                    Log.d("FragmentRestList", "list clicked !")
                    val intent = Intent(activity.baseContext, RestaurantDetailActivity::class.java)
                    var bundle = Bundle()

                    val restaurantInformation = restaurantList[position]
                    intent.putExtra("RestaurantInfo", restaurantInformation)
                    startActivity(intent)
                })
        {})
    }
}