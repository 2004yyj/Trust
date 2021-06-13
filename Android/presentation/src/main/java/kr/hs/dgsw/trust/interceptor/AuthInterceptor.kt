package kr.hs.dgsw.trust.interceptor

import kr.hs.dgsw.trust.ui.util.PreferenceHelper.token
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder().addHeader("Authorization", token?:"").build()
        return chain.proceed(request)
    }
}
