package com.example.android_advanced_kotlin.activity.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.android_advanced_kotlin.R
import com.example.android_advanced_kotlin.activity.NetworkActivity
import com.example.android_advanced_kotlin.activity.model.Poster

class PosterAdapter(var activity: NetworkActivity, var items: ArrayList<Poster>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_poster_list, parent, false)
        return PosterViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val poster: Poster = items[position]
        if (holder is PosterViewHolder) {
            val ll_poster = holder.ll_poster
            ll_poster.setOnLongClickListener {
                activity.dialogPoster(poster)
                false
            }
            val tv_title = holder.tv_title
            val tv_body = holder.tv_body
            tv_title.setText(poster.title.toUpperCase())
            tv_body.setText(poster.body)
        }
    }

    inner class PosterViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
        var ll_poster: LinearLayout
        var tv_title: TextView
        var tv_body: TextView

        init {
            ll_poster = view.findViewById(R.id.ll_poster)
            tv_title = view.findViewById(R.id.tv_title)
            tv_body = view.findViewById(R.id.tv_body)
        }
    }
}