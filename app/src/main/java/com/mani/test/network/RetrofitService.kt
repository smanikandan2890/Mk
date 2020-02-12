package com.mani.test.network

import io.reactivex.Observable
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface RetrofitService {
    @POST
    fun sendRequestPOST(@HeaderMap paramMap: Map<String, String>, @Url url: String, @Body jsonObject: RequestBody): Observable<Response<ResponseBody>>

    @GET
    fun sendRequestGET(@HeaderMap paramMap: Map<String, String>, @Url url: String): Observable<Response<ResponseBody>>
}