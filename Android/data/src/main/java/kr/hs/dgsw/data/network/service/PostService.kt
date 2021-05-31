package kr.hs.dgsw.data.network.service

import io.reactivex.Single
import kr.hs.dgsw.data.entity.AccountResponse
import kr.hs.dgsw.data.entity.PostResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface PostService {

    @GET("/post")
    fun getAllPost() : Single<Response<kr.hs.dgsw.data.util.Response<List<PostResponse>>>>

    @Multipart
    @POST("/post/save")
    fun postPost(@Part("username") username: RequestBody,
                  @Part("password") password: RequestBody,
                  @Part("content") content: RequestBody,
                  @Part("isAnonymous") isAnonymoust: RequestBody,
                  @Part imageList: List<MultipartBody.Part>?
    ) : Single<Response<kr.hs.dgsw.data.util.Response<PostResponse>>>

    @Multipart
    @PUT("/post/{postId}/update")
    fun putPost(@Path("postId") postId: Int,
                 @Part("username") username: RequestBody,
                 @Part("password") password: RequestBody,
                 @Part("content") content: RequestBody?,
                 @Part("isAnonymous") isAnonymoust: RequestBody?,
                 @Part imageList: List<MultipartBody.Part>?
    ) : Single<Response<kr.hs.dgsw.data.util.Response<PostResponse>>>
}