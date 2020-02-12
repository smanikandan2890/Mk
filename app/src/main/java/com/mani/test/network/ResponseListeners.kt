package com.mani.test.network

import org.json.JSONObject

interface ResponseListeners{
    fun onSuccess(requestId:Int, jsonObject: JSONObject)
    fun onFailure(requestId:Int, error: Throwable)
}