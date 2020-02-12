package com.mani.test.module.countrylist.model

import android.os.Parcel
import android.os.Parcelable

data class CountriesModel(var header: String? = "", var list: ArrayList<String>?) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.createStringArrayList()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(header)
        parcel.writeStringList(list)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CountriesModel> {
        override fun createFromParcel(parcel: Parcel): CountriesModel {
            return CountriesModel(parcel)
        }

        override fun newArray(size: Int): Array<CountriesModel?> {
            return arrayOfNulls(size)
        }
    }
}