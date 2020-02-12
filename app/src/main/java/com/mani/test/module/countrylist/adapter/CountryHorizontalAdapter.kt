package com.mani.test.module.countrylist.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.sayan.letterSectionedRecyclerView.R
import java.util.*

class CountryHorizontalAdapter internal constructor(private val mCountriesModelList: ArrayList<String>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_header_title, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val itemViewHolder = holder as ItemViewHolder
        itemViewHolder.nameTextview.text = mCountriesModelList[position]
    }

    override fun getItemCount(): Int {
        return mCountriesModelList.size
    }

    inner class ItemViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var nameTextview = itemView.findViewById(R.id.headerTitleTextview) as TextView
    }
}