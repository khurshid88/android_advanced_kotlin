package com.example.android_advanced_kotlin.activity.network.async

import android.content.Context
import com.example.android_advanced_kotlin.activity.model.Poster
import com.example.android_advanced_kotlin.activity.network.volley.VolleyHttp
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.loopj.android.http.RequestParams
import java.io.File
import java.io.FileNotFoundException

class AsyncHttp {
    companion object {
        val IS_TESTER = true
        val SERVER_DEVELOPMENT = "https://jsonplaceholder.typicode.com/"
        val SERVER_PRODUCTION = "https://6221f0dc666291106a17fcb5.mockapi.io/"
        private val client = getAsyncHttpClient()

        private fun getAsyncHttpClient(): AsyncHttpClient {
            val client = AsyncHttpClient()
            client.addHeader("Content-type", "application/json;charset=utf-8")
            return client
        }

        private fun server(url: String): String {
            if (IS_TESTER)
                return SERVER_DEVELOPMENT + url
            return SERVER_PRODUCTION + url
        }

        fun get(url: String, params: RequestParams, responseHandler: AsyncHttpResponseHandler) {
            client.get(server(url), params, responseHandler)
        }

        fun post(context: Context, url: String, body: RequestParams, responseHandler: AsyncHttpResponseHandler) {
            client.post(context, server(url), body, responseHandler)
        }

        fun put(context: Context, url: String, body: RequestParams, responseHandler: AsyncHttpResponseHandler) {
            client.post(context, server(url), body, responseHandler)
        }

        fun del(url: String, responseHandler: AsyncHttpResponseHandler) {
            client.delete(server(url), responseHandler)
        }

        /**
         *  Request Api`s
         */

        var API_LIST_POST = "posts"
        var API_SINGLE_POST = "posts/" //id
        var API_CREATE_POST = "posts"
        var API_UPDATE_POST = "posts/" //id
        var API_DELETE_POST = "posts/" //id

        /**
         *  Request Param`s
         */

        fun paramsEmpty(): RequestParams {
            return RequestParams()
        }

        fun paramsCreate(poster: Poster): RequestParams {
            val params = RequestParams()
            params.put("title", poster.title)
            params.put("body", poster.body)
            params.put("userId", poster.userId)
            return params
        }

        fun paramsUpdate(poster: Poster): RequestParams {
            val params = RequestParams()
            params.put("id", poster.id)
            params.put("title", poster.title)
            params.put("body", poster.body)
            params.put("userId", poster.userId)
            return params
        }

        fun paramsMultipart(file: File): RequestParams {
            val params = RequestParams()
            try {
                params.put("file", file)
            } catch (e: FileNotFoundException) {
            }
            return params
        }

    }
}