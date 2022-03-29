package com.example.android_advanced_kotlin.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android_advanced_kotlin.R
import com.example.android_advanced_kotlin.activity.adapter.PosterAdapter
import com.example.android_advanced_kotlin.activity.model.Poster
import com.example.android_advanced_kotlin.activity.model.PosterResp
import com.example.android_advanced_kotlin.activity.network.async.AsyncHttp
import com.example.android_advanced_kotlin.activity.network.retrofit.RetrofitHttp
import com.example.android_advanced_kotlin.activity.network.volley.VolleyHandler
import com.example.android_advanced_kotlin.activity.network.volley.VolleyHttp
import com.example.android_advanced_kotlin.activity.utils.Logger
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NetworkActivity : BaseActivity(){
    lateinit var recyclerView: RecyclerView
    var posters = ArrayList<Poster>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_network)
        initViews()
    }

    fun initViews(){
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.setLayoutManager(GridLayoutManager(this, 1))
        val b_floating: FloatingActionButton = findViewById(R.id.b_floating)

        apiPosterList()
    }

    fun refreshAdapter(posters: ArrayList<Poster>) {
        val adapter = PosterAdapter(this, posters)
        recyclerView.setAdapter(adapter)
    }

    fun dialogPoster(poster: Poster?) {
        AlertDialog.Builder(this)
            .setTitle("Delete Poster")
            .setMessage("Are you sure you want to delete this poster?")
            .setPositiveButton(
                android.R.string.yes
            ) { dialog, which -> apiPosterDelete(poster!!) }
            .setNegativeButton(android.R.string.no, null)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show()
    }

    fun apiPostList(){

        AsyncHttp.get(AsyncHttp.API_LIST_POST, AsyncHttp.paramsEmpty(), object: AsyncHttpResponseHandler(){
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?) {

            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?, error: Throwable?) {

            }
        })

        val poster = Poster(1, 1, "PDP", "Online")

        AsyncHttp.post(this, AsyncHttp.API_CREATE_POST, AsyncHttp.paramsCreate(poster), object: AsyncHttpResponseHandler(){
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?) {

            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?, error: Throwable?) {

            }
        })



    }
    private fun apiPosterList() {
        VolleyHttp.get(VolleyHttp.API_LIST_POST, VolleyHttp.paramsEmpty(),object : VolleyHandler {
            override fun onSuccess(response: String?) {
                val postArray = Gson().fromJson(response, Array<Poster>::class.java)
                posters.clear()
                posters.addAll(postArray)
                refreshAdapter(posters)
            }

            override fun onError(error: String?) {
                Log.d("@@@",error!!)
            }
        })
    }

    private fun apiPosterDelete(poster: Poster) {

        VolleyHttp.del(VolleyHttp.API_DELETE_POST + poster.id,object : VolleyHandler {
            override fun onSuccess(response: String?) {
                Log.d("@@@",response!!)
                apiPosterList()
            }

            override fun onError(error: String?) {
                Log.d("@@@",error!!)
            }
        })
    }

    fun workWithRetrofit(){

        RetrofitHttp.posterService.listPost().enqueue(object : Callback<ArrayList<PosterResp>> {
            override fun onResponse(call: Call<ArrayList<PosterResp>>, response: Response<ArrayList<PosterResp>>) {
                Logger.d("@@@", response.body().toString())
            }

            override fun onFailure(call: Call<ArrayList<PosterResp>>, t: Throwable) {
                Logger.e("@@@", t.message.toString())
            }
        })


        val poster = Poster(1, 1, "PDP", "Online")

        RetrofitHttp.posterService.createPost(poster).enqueue(object : Callback<PosterResp> {
            override fun onResponse(call: Call<PosterResp>, response: Response<PosterResp>) {
                Log.d("@@@", response.body().toString())
            }

            override fun onFailure(call: Call<PosterResp>, t: Throwable) {
                Log.d("@@@", t.message.toString())
            }
        })

        RetrofitHttp.posterService.updatePost(poster.id, poster).enqueue(object : Callback<PosterResp> {
            override fun onResponse(call: Call<PosterResp>, response: Response<PosterResp>) {
                Log.d("@@@", response.body().toString())
            }

            override fun onFailure(call: Call<PosterResp>, t: Throwable) {
                Log.d("@@@", t.message.toString())
            }
        })

        RetrofitHttp.posterService.deletePost(poster.id).enqueue(object : Callback<PosterResp> {
            override fun onResponse(call: Call<PosterResp>, response: Response<PosterResp>) {
                Log.d("@@@", "" + response.body())
            }

            override fun onFailure(call: Call<PosterResp>, t: Throwable) {
                Log.d("@@@", "" + t.message)
            }
        })
    }

    fun workWithVolley(){

//        VolleyHttp.get(VolleyHttp.API_LIST_POST, VolleyHttp.paramsEmpty(),object : VolleyHandler {
//            override fun onSuccess(response: String?) {
//                Logger.d("VolleyHttp",response!!)
//                tv_text.text = response
//            }
//
//            override fun onError(error: String?) {
//                Logger.e("VolleyHttp",error!!)
//            }
//        })



        val poster = Poster(1, 1, "PDP", "Online")
        VolleyHttp.post(VolleyHttp.API_CREATE_POST, VolleyHttp.paramsCreate(poster),object :
            VolleyHandler {
            override fun onSuccess(response: String?) {
                Logger.d("@@@",response!!)
            }

            override fun onError(error: String?) {
                Logger.d("@@@",error!!)
            }
        })

        VolleyHttp.put(VolleyHttp.API_UPDATE_POST + poster.id, VolleyHttp.paramsUpdate(poster),object :
            VolleyHandler {
            override fun onSuccess(response: String?) {
                Log.d("@@@",response!!)
            }

            override fun onError(error: String?) {
                Log.d("@@@",error!!)
            }
        })

        VolleyHttp.del(VolleyHttp.API_DELETE_POST + poster.id,object : VolleyHandler {
            override fun onSuccess(response: String?) {
                Log.d("@@@",response!!)
            }

            override fun onError(error: String?) {
                Log.d("@@@",error!!)
            }
        })

    }
}