package com.example.loginapirest.mainModule.model

import android.util.Log
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.example.loginapirest.LoginApplication
import com.example.loginapirest.common.entities.User
import com.example.loginapirest.common.utils.Constants
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import org.json.JSONObject

class MainInteractor {

    fun findUserById(id: Int, callback: (User) -> Unit) {
        val url = "${Constants.BASE_URL}${Constants.API_PATH}${Constants.USERS_PATH}/$id"

        val request = JsonObjectRequest(Request.Method.GET, url, null, {
            val json = it.getJSONObject("data")
            val user =
                GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                    .create().fromJson(json.toString(), User::class.java)

            val supportJson = it.optJSONObject(Constants.SUPPORT_PROPERTY)
            val text = supportJson.optString(Constants.TEXT_PROPERTY)
            val url = supportJson.optString(Constants.URL_PROPERTY)

            user.text = text
            user.url = url

            callback(user)
        }, {
            it.printStackTrace()
        })
        LoginApplication.reqResAPI.addToRequestQueue(request)
    }


    fun loginUser(
        user: User, onSuccess: (String, Int) -> Unit,
        onError: (Int) -> Unit
    ) {
        authenticateUser(Constants.LOGIN_PATH, user, onSuccess, onError)
    }

    fun registerUser(
        user: User, onSuccess: (String, Int) -> Unit, onError: (Int) -> Unit
    ) {
        authenticateUser(Constants.REGISTER_PATH, user, onSuccess, onError)
    }

    private fun authenticateUser(
        path: String,
        user: User,
        onSuccess: (String, Int) -> Unit,
        onError: (Int) -> Unit
    ) {
        val jsonParams = JSONObject()
        jsonParams.put(Constants.EMAIL_PARAM, user.email)
        jsonParams.put(Constants.PASSWORD_PARAM, user.password)

        val url = Constants.BASE_URL + Constants.API_PATH + path
        val jsonObjectRequest =
            object : JsonObjectRequest(
                Method.POST, url, jsonParams,
                { res ->
                    val id = res.optInt(Constants.ID_PROPERTY, 0)
                    val token = res.optString(Constants.TOKEN_PROPERTY, "")
                    onSuccess(token, id)
                }, {
                    it.printStackTrace()
                    onError(it.networkResponse.statusCode)
                }
            ) {
                override fun getHeaders(): MutableMap<String, String> {
                    val params = HashMap<String, String>()
                    params["Content-Type"] = "application/json"
                    return params
                }
            }
        LoginApplication.reqResAPI.addToRequestQueue(jsonObjectRequest)
    }

}