package kr.hs.dgsw.data.network.service

import io.reactivex.Single
import kr.hs.dgsw.data.entity.AccountResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface AccountService {
    @POST("/account/login")
    fun postLogin(@Query("username") username: String,
                  @Query("password") password: String
    ) : Single<Response<kr.hs.dgsw.data.util.Response<AccountResponse>>>

    @Multipart
    @POST("/account/signUp")
    fun postSignUp(@Part("name") name: RequestBody,
                   @Part("username") username: RequestBody,
                   @Part("password") password: RequestBody,
                   @Part profileImage: MultipartBody.Part?
    ) : Single<Response<kr.hs.dgsw.data.util.Response<AccountResponse>>>
}