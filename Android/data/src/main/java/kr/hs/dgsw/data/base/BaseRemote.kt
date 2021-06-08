package kr.hs.dgsw.data.base

import android.util.Log
import com.google.gson.Gson
import io.reactivex.functions.Function
import kr.hs.dgsw.data.network.service.AccountService
import kr.hs.dgsw.data.util.Response
import org.json.JSONObject

abstract class BaseRemote<SV> {
    abstract val service: SV

    fun <T> getResponse() : Function<retrofit2.Response<Response<T>>, T> {
        return Function {
            checkError(it)
            it.body()!!.data
        }
    }

    fun <T> getMessage() : Function<retrofit2.Response<Response<T>>, String> {
        return Function {
            checkError(it)
            it.body()!!.message
        }
    }

    private fun <T> checkError(response: retrofit2.Response<Response<T>>) {
        if (!response.isSuccessful) {
            val gson = Gson()
            val errorBody = gson.fromJson(response.errorBody()!!.charStream(), Response::class.java)
            throw Throwable(errorBody.message)
        }
    }
}