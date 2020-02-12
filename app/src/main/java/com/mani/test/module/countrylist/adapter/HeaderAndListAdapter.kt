package com.mani.test.module.countrylist.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mani.test.module.countrylist.model.CountriesModel
import com.sayan.letterSectionedRecyclerView.R
import java.util.*

class HeaderAndListAdapter internal constructor(private val mCountriesModelList: ArrayList<CountriesModel>, private val ctx: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_name, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val itemViewHolder = holder as ItemViewHolder

        itemViewHolder.nameTextview.text = mCountriesModelList[position].header

        itemViewHolder.nameTextview.setOnClickListener {
            if (mCountriesModelList[position].Grid_List == "L") {
                mCountriesModelList[position].Grid_List = "G"
                itemViewHolder.nameTextview.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.arrow_down_float, 0);
                val list = mCountriesModelList[position].list
                list!!.sort()
                itemViewHolder.list.setHasFixedSize(true)
                itemViewHolder.list.layoutManager = GridLayoutManager(ctx, 3)
                // itemViewHolder.list.layoutManager = LinearLayoutManager(ctx, LinearLayoutManager.HORIZONTAL, false)
                val mAdapter = CountryHorizontalAdapter(list)
                itemViewHolder.list.adapter = mAdapter
            } else {
                mCountriesModelList[position].Grid_List = "L"
                itemViewHolder.nameTextview.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.arrow_up_float, 0);
                val list = mCountriesModelList[position].list
                list!!.sort()
                itemViewHolder.list.setHasFixedSize(true)
                // itemViewHolder.list.layoutManager = GridLayoutManager(ctx, 2)
                itemViewHolder.list.layoutManager = LinearLayoutManager(ctx, LinearLayoutManager.HORIZONTAL, false)
                val mAdapter = CountryHorizontalAdapter(list)
                itemViewHolder.list.adapter = mAdapter
            }
        }

        val list = mCountriesModelList[position].list
        list!!.sort()
        itemViewHolder.list.setHasFixedSize(true)
        // itemViewHolder.list.layoutManager = GridLayoutManager(ctx, 2)
        itemViewHolder.list.layoutManager = LinearLayoutManager(ctx, LinearLayoutManager.HORIZONTAL, false)
        val mAdapter = CountryHorizontalAdapter(list)
        itemViewHolder.list.adapter = mAdapter
    }

    override fun getItemCount(): Int {
        return mCountriesModelList.size
    }

    inner class ItemViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var nameTextview = itemView.findViewById(R.id.nameTextview) as TextView
        internal var list = itemView.findViewById(R.id.rv_hor_list) as RecyclerView
    }
}