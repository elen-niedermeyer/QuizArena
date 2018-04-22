package com.quizarena.sessions.creation

import android.content.Context
import com.quizarena.R
import org.jetbrains.anko.doAsyncResult

class CategoryApi(val context: Context) {
    /**
     * Executes GET request for all categories
     */
    fun getAllCategories() : Array<String> {
        val response = doAsyncResult {
            val response = khttp.get(
                    url = context.getString(R.string.baseurl) + context.getString(R.string.endpoint_category)
            )
            return@doAsyncResult response
        }.get()

        val statusCode = response.statusCode

        if (statusCode == 200) {
            // category request was successful
            val json = response.jsonArray
            return Array(json.length(), { i -> json[i].toString()})
        } else {
            // category request fails, return default
            return arrayOf("default")
        }
    }
}