package com.example.android_advanced_kotlin.activity.network.retrofit.services

import com.example.android_advanced_kotlin.activity.model.Poster
import com.example.android_advanced_kotlin.activity.model.PosterResp
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*


interface PosterService {

    @Headers(
        "Content-type:application/json"
    )

    @GET("posts")
    fun listPost(): Call<ArrayList<PosterResp>>

    @GET("posts/{id}")
    fun singlePost(@Path("id") id: Int): Call<PosterResp>

    @POST("posts")
    fun createPost(@Body post: Poster): Call<PosterResp>

    @PUT("posts/{id}")
    fun updatePost(@Path("id") id: Int, @Body post: Poster): Call<PosterResp>

    @DELETE("posts/{id}")
    fun deletePost(@Path("id") id: Int): Call<PosterResp>

    @Multipart
    @POST("v1/images/upload")
    fun uploadImage(@Part file: MultipartBody.Part, @Part("sub_id") sub_id: RequestBody): Call<ResponseBody>

}