package kr.hs.dgsw.data.network.service

import io.reactivex.Single
import kr.hs.dgsw.data.entity.TokenResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface AccountService {
    @POST("/account/login")
    fun postLogin(@Query("username") username: String,
                  @Query("password") password: String
    ) : Single<Response<kr.hs.dgsw.data.util.Response<TokenResponse>>>

    @Multipart
    @POST("/account/signUp")
    fun postSignUp(@Part("name") name: RequestBody,
                   @Part("username") username: RequestBody,
                   @Part("password") password: RequestBody,
                   @Part profileImage: MultipartBody.Part?
    ) : Single<Response<kr.hs.dgsw.data.util.Response<TokenResponse>>>

    @POST("/account/autoLogin")
    fun postAutoLogin(
    ): Single<Response<kr.hs.dgsw.data.util.Response<TokenResponse>>>
}