package com.quizarena.notifications

import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService

class InstanceIdService : FirebaseInstanceIdService() {

    private val TAG = "InstanceIdService"

    override fun onTokenRefresh() {
        Log.d(TAG, "Refreshed token: " + getToken())
    }

    fun getToken(): String? {
        return FirebaseInstanceId.getInstance().token
    }
}