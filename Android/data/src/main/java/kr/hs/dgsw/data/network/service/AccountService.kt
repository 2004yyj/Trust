package kr.hs.dgsw.data.network.service

import io.reactivex.Single
import kr.hs.dgsw.data.entity.AccountResponse
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Query

interface AccountService {
    @POST("/account/login")
    fun postLogin(@Query("username") username: String,
                  @Query("password") password: String
    ) : Single<Response<kr.hs.dgsw.data.util.Response<AccountResponse>>>


    @POST("/account/signIn")
    fun postSignIn(@Query("name") name: String,
                   @Query("username") username: String,
                   @Query("password") password: String
    ) : Single<Response<kr.hs.dgsw.data.util.Response<AccountResponse>>>
}