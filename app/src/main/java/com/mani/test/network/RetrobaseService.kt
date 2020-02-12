package com.mani.test.network

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import com.mani.test.utils.CommonUtils
import com.mani.test.utils.PreferenceManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit

@SuppressLint("CheckResult")
class RetrobaseService {
    companion object {
        private var mProgressHUD: ProgressDialog? = null
        private var retrofit: Retrofit? = null
        private val REQUEST_TIMEOUT = 60
        private var okHttpClient: OkHttpClient? = null

//------------------------------------------ POST METHOD -------------------------------------------

        fun callPostRequest(context: Context, url: String, jsonObject: JSONObject, progressBarVisibility: Boolean,
                            responseListeners: ResponseListeners, requestId: Int): Disposable? {

            val disposable = callpostRequest(context, url, jsonObject, progressBarVisibility,
                    responseListeners, requestId)

            if (progressBarVisibility) {
                var progressBarShowing = false
                if (mProgressHUD != null) {
                    progressBarShowing = mProgressHUD!!.isShowing
                }

                if (!progressBarShowing) {
                    mProgressHUD = ProgressDialog.show(context as Activity, "Loading...", "Please Wait...")
                }
            }

            return disposable
        }

        private fun callpostRequest(context: Context, url: String, jsonObject: JSONObject, progressBarVisibility: Boolean, responseListeners: ResponseListeners, requestId: Int): Disposable? {
            val retrobaseService: Retrofit = getClient(context)!!
            val retrofitService: RetrofitService = retrobaseService.create(RetrofitService::class.java)

            val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonObject.toString())

            val observable = retrofitService.sendRequestPOST(CommonUtils.getHeaders(context), url, body).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
                val jsonString = it.body()?.string()
                if (jsonString!!.isNotEmpty()) {
                    try {
                        val jsonObjectReq = JSONObject(jsonString)
                        if (progressBarVisibility) mProgressHUD?.cancel()
                        responseListeners.onSuccess(requestId, jsonObjectReq.getJSONObject("response"))
                    } catch (e: Exception) {
                        e.printStackTrace()
                        if (progressBarVisibility) mProgressHUD?.cancel()
                        CommonUtils.showMessage(context, "Try again Later")
                    }
                } else {
                    if (progressBarVisibility) mProgressHUD?.cancel()
                    CommonUtils.showMessage(context, "Try again Later")
                }
            }, {
                if (progressBarVisibility) mProgressHUD?.cancel()
                responseListeners.onFailure(requestId, it)
            })

            return observable
        }

//------------------------------------------ GET METHOD --------------------------------------------

        fun callGetRequest(context: Context, url: String, progressBarVisibility: Boolean,
                           responseListeners: ResponseListeners, requestId: Int): Disposable? {

            val disposable = callgetRequest(context, url, progressBarVisibility,
                    responseListeners, requestId)

            if (progressBarVisibility) {
                var progressBarShowing = false
                if (mProgressHUD != null) {
                    progressBarShowing = mProgressHUD!!.isShowing
                }

                if (!progressBarShowing) {
                    mProgressHUD = ProgressDialog.show(context as Activity, "Loading...", "Please Wait...")
                }
            }

            return disposable
        }

        private fun callgetRequest(context: Context, url: String, progressBarVisibility: Boolean, responseListeners: ResponseListeners, requestId: Int): Disposable? {
            val retrobaseService: Retrofit = getClient(context)!!
            val retrofitService: RetrofitService = retrobaseService.create(RetrofitService::class.java)

            val headers = HashMap<String, String>()

            return retrofitService.sendRequestGET(headers, url).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        val jsonString = it.body()?.string()
                        if (jsonString != null) {
                            try {
                                val jsonObjectReq = JSONObject(jsonString)
                                if (progressBarVisibility) mProgressHUD?.cancel()
                                responseListeners.onSuccess(requestId, jsonObjectReq)
                            } catch (e: Exception) {
                                e.printStackTrace()
                                if (progressBarVisibility) mProgressHUD?.cancel()
                                CommonUtils.showMessage(context, "Try again Later")
                            }
                        } else {
                            if (progressBarVisibility) mProgressHUD?.cancel()
                            CommonUtils.showMessage(context, "Try again Later")
                        }
                    },
                            {
                                if (progressBarVisibility) mProgressHUD?.cancel()
                                responseListeners.onFailure(requestId, it)
                            })
        }

//------------------------------------- COMMON GET POST CALL ---------------------------------------

        private fun getClient(ctx: Context): Retrofit? {
            if (okHttpClient == null)
                initOkHttp()

            retrofit = Retrofit.Builder()
                    .baseUrl(PreferenceManager(ctx).getBaseURL())
                    .client(okHttpClient!!)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

            return retrofit
        }

        private fun initOkHttp() {
            val httpClient = OkHttpClient().newBuilder()
                    .connectTimeout(REQUEST_TIMEOUT.toLong(), TimeUnit.SECONDS)
                    .readTimeout(REQUEST_TIMEOUT.toLong(), TimeUnit.SECONDS)
                    .writeTimeout(REQUEST_TIMEOUT.toLong(), TimeUnit.SECONDS)

            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY

            httpClient.addInterceptor(interceptor)

            httpClient.addInterceptor { chain ->
                val original = chain.request()
                val requestBuilder = original.newBuilder()
                val request = requestBuilder.build()
                chain.proceed(request)
            }

            okHttpClient = httpClient.build()
        }
    }
//--------------------------------------------- END ------------------------------------------------
}