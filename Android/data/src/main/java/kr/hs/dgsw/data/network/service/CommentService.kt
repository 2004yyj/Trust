package kr.hs.dgsw.data.network.service

import io.reactivex.Single
import kr.hs.dgsw.data.entity.CommentResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface CommentService {

    @GET("/comment")
    fun getAllComment(@Query("postId") postId: Int) : Single<Response<kr.hs.dgsw.data.util.Response<List<CommentResponse>>>>

    @Multipart
    @POST("/comment/save")
    fun postComment(
            @Part("postId") postId: Int,
            @Part("username") username: RequestBody,
            @Part("password") password: RequestBody,
            @Part("content") content: RequestBody,
            @Part("isAnonymous") isAnonymous: Boolean,
            @Part imageList: List<MultipartBody.Part>?
    ) : Single<Response<kr.hs.dgsw.data.util.Response<List<CommentResponse>>>>

    @Multipart
    @PUT("/comment/update")
    fun updateComment(
            @Part("commentId") commentId: Int,
            @Part("username") username: RequestBody,
            @Part("password") password: RequestBody,
            @Part("content") content: RequestBody?,
            @Part("isAnonymous") isAnonymous: Boolean?,
            @Part imageList: List<MultipartBody.Part>?
    ) : Single<Response<kr.hs.dgsw.data.util.Response<List<CommentResponse>>>>

    @DELETE("/comment/delete")
    fun deleteComment(
            @Query("commentId") commentId: Int,
            @Query("username") username: String,
            @Query("password") password: String,
    ) : Single<Response<kr.hs.dgsw.data.util.Response<List<CommentResponse>>>>
}