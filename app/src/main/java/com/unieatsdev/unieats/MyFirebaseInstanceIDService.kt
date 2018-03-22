package com.unieatsdev.unieats

import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService



/**
 * Created by Ramshad on 3/20/18.
 */

class MyFirebaseInstanceIDService: FirebaseInstanceIdService()  {

    override fun onTokenRefresh() {
        // Get updated InstanceID token.
        val refreshedToken = FirebaseInstanceId.getInstance().token
        Log.d(TAG, "Refreshed token: " + refreshedToken!!)

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken)

    }

    private fun sendRegistrationToServer(refreshedToken: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}