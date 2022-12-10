package com.project.news.network

import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.HttpException

/*  if the input is in format
{
  detail :String
  result :T
}
*/

/*  Repository call
    fun sendPhone(phone: String, authViewModel: AuthViewModel) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = try{
                responseHandler.handleSuccess(authApi.sendPhoneNo(phone))
            } catch (e :Exception) {
                responseHandler.handleException(e)
            }
            authViewModel.phoneResponse.postValue(response)
        }
    }
*/

/*  Api call
    @GET(URLS.validate_phone)
    suspend fun sendPhoneNo(@Query("phone") phone :String) : Resource<String>
*/

data class Resource<T>(var status: Boolean, var detail: String?, var result: T?) {

    private var hasBeenHandled = false

    fun isResponseHandled() : Boolean {
        if (hasBeenHandled) {
            return hasBeenHandled
        } else {
            hasBeenHandled = true
            return false
        }
    }

    companion object {
        fun <T> success(data: Resource<T>): Resource<T> {
            data.status = true
            return data
        }
        fun <T> error(msg: String, data: T?): Resource<T> {
            return Resource(false, msg, data)
        }
    }
}

open class ResponseHandler {

    fun <T> handleSuccess(data: Resource<T>): Resource<T> {
        return Resource.success(data)
    }

    fun <T> handleException(e: Exception): Resource<T> {
        e.printStackTrace()
        return when (e) {
            is HttpException -> {
                var errorMessage = if (e.code() != 400 && e.code() != 403) {
                    getErrorMessage(e.code(), e)
                } else {
                    val body = e.response()?.errorBody()
                    JSONObject(getErrorJSON(body)).get("detail").toString()
                }
                Resource.error(errorMessage, null)
            }
            else -> Resource.error(getErrorMessage(Int.MAX_VALUE, e), null)
        }
    }

    private fun getErrorJSON(body: ResponseBody?): String {
        return try {
            body!!.string()
        } catch (e: Exception) {
            e.localizedMessage?:"Unknown Error"
        }
    }

    private fun getErrorMessage(code: Int, e: Exception): String {
        return when (code) {
            401 -> "Unauthorised"
            404 -> "Not found"
            400 -> e.message.toString()
            429 -> "Try after sometime"
            500 -> "Internal Server Error"
            else -> e.message.toString()
        }
    }
}