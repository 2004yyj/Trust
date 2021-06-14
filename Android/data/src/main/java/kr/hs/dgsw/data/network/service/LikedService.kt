package kr.hs.dgsw.data.network.service

import io.reactivex.Single
import kr.hs.dgsw.data.entity.LikedResponse
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface LikedService {

    @GET("/liked")
    fun getAllLiked(
            @Query("postId") postId: Int
    ) : Single<Response<kr.hs.dgsw.data.util.Response<List<LikedResponse>>>>

    @POST("/liked/save")
    fun postLiked(
            @Query("postId") postId: Int,
    ) : Single<Response<kr.hs.dgsw.data.util.Response<List<LikedResponse>>>>

    @DELETE("/liked/delete")
    fun deleteLiked(
            @Query("postId") postId: Int,
    ) : Single<Response<kr.hs.dgsw.data.util.Response<List<LikedResponse>>>>
}