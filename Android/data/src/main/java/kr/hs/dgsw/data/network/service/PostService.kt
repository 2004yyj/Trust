package kr.hs.dgsw.data.network.service

import io.reactivex.Single
import kr.hs.dgsw.data.entity.PostResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface PostService {

    @GET("/post")
    fun getAllPost() : Single<Response<kr.hs.dgsw.data.util.Response<List<PostResponse>>>>

    @GET("/post")
    fun getPost(@Query("postId") postId: Int) : Single<Response<kr.hs.dgsw.data.util.Response<PostResponse>>>

    @GET("/post")
    fun getAllPostByUsername(@Query("username") username: String) : Single<Response<kr.hs.dgsw.data.util.Response<List<PostResponse>>>>

    @Multipart
    @POST("/post/save")
    fun postPost(
            @Part("username") username: RequestBody,
            @Part("password") password: RequestBody,
            @Part("isAnonymous") isAnonymous: RequestBody,
            @Part("content") content: RequestBody,
            @Part imageList: List<MultipartBody.Part>?
    ) : Single<Response<kr.hs.dgsw.data.util.Response<PostResponse>>>

    @Multipart
    @PUT("/post/{postId}/update")
    fun updatePost(
            @Path("postId") postId: Int,
            @Part("username") username: RequestBody,
            @Part("password") password: RequestBody,
            @Part("isAnonymous") isAnonymous: RequestBody?,
            @Part("content") content: RequestBody?,
            @Part("deleteFileList") deleteFileList: List<String>?,
            @Part updateImageList: List<MultipartBody.Part>?
    ) : Single<Response<kr.hs.dgsw.data.util.Response<PostResponse>>>

    @DELETE("/post/{postId}/delete")
    fun deletePost(
            @Path("postId") postId: Int,
            @Query("username") username: String,
            @Query("password") password: String,
    ) : Single<Response<kr.hs.dgsw.data.util.Response<PostResponse>>>
}