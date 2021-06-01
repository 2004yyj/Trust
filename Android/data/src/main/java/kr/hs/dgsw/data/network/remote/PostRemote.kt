package kr.hs.dgsw.data.network.remote

import com.google.gson.Gson
import io.reactivex.Single
import kr.hs.dgsw.data.base.BaseRemote
import kr.hs.dgsw.data.entity.PostResponse
import kr.hs.dgsw.data.network.service.PostService
import kr.hs.dgsw.data.util.Response
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class PostRemote @Inject constructor(
        override val service: PostService,
) : BaseRemote<PostService>() {

    fun getAllPost(): Single<List<PostResponse>> {
        return service.getAllPost().map(getResponse())
    }

    fun getPost(postId: Int): Single<PostResponse> {
        return service.getPost(postId).map(getResponse())
    }

    fun getAllPostByUsername(username: String): Single<List<PostResponse>> {
        return service.getAllPostByUsername(username).map(getResponse())
    }

    fun postPost(
            username: String,
            password: String,
            isAnonymous: Boolean,
            content: String,
            imageList: List<MultipartBody.Part>?
    ): Single<PostResponse> {
        val textType = "text/plain".toMediaType()
        val usernameBody = username.toRequestBody(textType)
        val passwordBody = password.toRequestBody(textType)
        val contentBody = content.toRequestBody(textType)
        val isAnonymousBody = isAnonymous.toString().toRequestBody()

        return service.postPost(usernameBody, passwordBody, isAnonymousBody, contentBody, imageList).map(getResponse())
    }

    fun updatePost(
            postId: Int,
            username: String,
            password: String,
            isAnonymous: Boolean?,
            content: String?,
            deleteFileList: List<String>?,
            updateFileList: List<MultipartBody.Part>?
    ): Single<PostResponse> {
        val textType = "text/plain".toMediaType()
        val usernameBody = username.toRequestBody(textType)
        val passwordBody = password.toRequestBody(textType)
        val contentBody: RequestBody? = content?.toRequestBody(textType)
        val isAnonymousBody = isAnonymous.toString().toRequestBody()

        return service.updatePost(postId, usernameBody, passwordBody, isAnonymousBody, contentBody, deleteFileList, updateFileList).map(getResponse())
    }

    fun deletePost(
            postId: Int,
            username: String,
            password: String,
    ): Single<PostResponse> {

        return service.deletePost(postId, username, password).map(getResponse())
    }

}