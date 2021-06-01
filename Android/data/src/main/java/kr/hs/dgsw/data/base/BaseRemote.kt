package kr.hs.dgsw.data.base

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

    private fun checkError(response: retrofit2.Response<*>) {
        if (!response.isSuccessful) {
            val errorBody = JSONObject(response.errorBody()!!.toString())
            throw Throwable(errorBody.getString("message"))
        }
    }
}