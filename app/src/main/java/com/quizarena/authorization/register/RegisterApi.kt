package com.quizarena.authorization.register

import android.content.Context
import android.util.Log
import android.widget.Toast
import org.jetbrains.anko.custom.async
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.net.URL
import android.net.ConnectivityManager
import android.net.NetworkInfo


class RegisterApi {

    // bad request
    fun registerRequest(context: Context, name: String, password: String, displayName: String) {
        doAsync {
            val response = khttp.post(
                    url = "http://192.168.0.15:5000/user",
                    json = mapOf("user" to "value1", "password" to "valuen"))
            println(response)
        }
    }
}

