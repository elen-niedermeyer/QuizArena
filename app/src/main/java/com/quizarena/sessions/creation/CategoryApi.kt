package com.quizarena.sessions.creation

import android.content.Context
import com.quizarena.R
import org.jetbrains.anko.doAsyncResult

class CategoryApi(val context: Context) {
    fun getAllCategories() : Array<String> {
        val response = doAsyncResult {
            val response = khttp.get(
                    url = context.getString(R.string.baseurl) + context.getString(R.string.endpoint_category)
            )
            return@doAsyncResult response
        }.get()

        val statusCode = response.statusCode

        if (statusCode == 200) {
            val json = response.jsonArray
            return Array(json.length(), { i -> json[i].toString()})
        } else {
            return arrayOf("default")
        }
    }
}