package com.mani.test.module.countrylist

import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.mani.test.module.countrylist.adapter.HeaderAndListAdapter
import com.mani.test.module.countrylist.model.CountriesModel
import com.mani.test.module.countrylist.model.Model
import com.mani.test.network.NullStringToEmptyAdapterFactory
import com.mani.test.network.ResponseListeners
import com.mani.test.network.RetrobaseService
import com.mani.test.utils.Constants.Companion.ContactList
import com.mani.test.utils.Constants.Companion.PostData
import com.mani.test.utils.Constants.Companion.noDataFound
import com.mani.test.utils.PreferenceManager
import com.mani.test.utils.URLConstants.Companion.ContactList_Url
import com.mani.test.utils.URLConstants.Companion.PostMethod_Url
import com.sayan.letterSectionedRecyclerView.R
import com.sayan.letterSectionedRecyclerView.databinding.ActivityMainBinding
import org.json.JSONObject

class MainActivity : AppCompatActivity(), ResponseListeners {
    private var list = ArrayList<Model>()
    lateinit var layoutBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        layoutBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        getservicecall()
    }

    private fun getservicecall() {
        PreferenceManager(this).saveBaseUrl("https://api.androidhive.info/")
        RetrobaseService.callGetRequest(this, ContactList_Url, true, this, ContactList)
    }

    private fun postservicecall() {
        PreferenceManager(this).saveBaseUrl("https://api.androidhive.info/")
        val jsonObject = JSONObject()
        RetrobaseService.callPostRequest(this, PostMethod_Url, jsonObject, true, this, PostData)
    }

    override fun onSuccess(requestId: Int, jsonObject: JSONObject) {
        when (requestId) {
            PostData -> {
                Log.e("----->POST", "" + jsonObject)
            }

            ContactList -> {
                Log.e("----->GET", "" + jsonObject)
                val data = jsonObject.getJSONArray("contacts").toString()

                list = GsonBuilder().registerTypeAdapterFactory(NullStringToEmptyAdapterFactory()).create().fromJson<ArrayList<Model>>(data, object : TypeToken<ArrayList<Model>>() {}.type)

                /*
                val mCountries: ArrayList<String> = ArrayList()
                for (i in list) {
                    mCountries.add(i.name!!)
                }
                */

                // GET Data From Array File
                val mCountries = applicationContext.resources.getStringArray(R.array.countries)

                val countriesModels = ArrayList<CountriesModel>()
                var c = 'A'
                while (c <= 'Z') {
                    val country = ArrayList<String>()
                    for (mCountry in mCountries) {
                        if (c == mCountry.first()) {
                            country.add(mCountry)
                        }
                    }
                    if (country.isNotEmpty()) {
                        countriesModels.add(CountriesModel(c.toString(), country, "L"))
                    }
                    ++c
                }

                if (countriesModels.isNotEmpty()) {
                    layoutBinding.rvVerList.setHasFixedSize(true)
                    // layoutBinding.rvVerList.layoutManager = GridLayoutManager(this, 2)
                    layoutBinding.rvVerList.layoutManager = LinearLayoutManager(this)
                    val mAdapter = HeaderAndListAdapter(countriesModels, this)
                    layoutBinding.rvVerList.adapter = mAdapter
                } else {
                    layoutBinding.rvVerList.visibility = GONE
                    layoutBinding.tvNodata.visibility = VISIBLE
                    layoutBinding.tvNodata.text = noDataFound
                }
            }
        }
    }

    override fun onFailure(requestId: Int, error: Throwable) {

    }
}