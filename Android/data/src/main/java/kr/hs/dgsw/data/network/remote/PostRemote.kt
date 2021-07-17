package kr.hs.dgsw.data.network.remote

import io.reactivex.Single
import kr.hs.dgsw.data.base.BaseRemote
import kr.hs.dgsw.data.entity.PostResponse
import kr.hs.dgsw.data.network.service.PostService
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
            isAnonymous: Boolean,
            content: String,
            imageList: List<MultipartBody.Part>?
    ): Single<PostResponse> {
        val textType = "text/plain".toMediaType()
        val contentBody = content.toRequestBody(textType)
        val isAnonymousBody = isAnonymous.toString().toRequestBody()

        return service.postPost(isAnonymousBody, contentBody, imageList).map(getResponse())
    }

    fun updatePost(
            postId: Int,
            isAnonymous: Boolean?,
            content: String?,
            deleteFileList: List<String>?,
            updateFileList: List<MultipartBody.Part>?
    ): Single<List<PostResponse>> {
        val textType = "text/plain".toMediaType()
        val contentBody: RequestBody? = content?.toRequestBody(textType)
        val isAnonymousBody = isAnonymous.toString().toRequestBody()

        return service.updatePost(postId, isAnonymousBody, contentBody, deleteFileList, updateFileList).map(getResponse())
    }

    fun deletePost(
            postId: Int,
    ): Single<List<PostResponse>> {

        return service.deletePost(postId).map(getResponse())
    }

}