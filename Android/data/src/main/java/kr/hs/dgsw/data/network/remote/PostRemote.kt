package kr.hs.dgsw.data.network.remote

import com.google.gson.Gson
import io.reactivex.Single
import kr.hs.dgsw.data.entity.AccountResponse
import kr.hs.dgsw.data.entity.PostResponse
import kr.hs.dgsw.data.mapper.toEntity
import kr.hs.dgsw.data.network.service.AccountService
import kr.hs.dgsw.data.network.service.PostService
import kr.hs.dgsw.data.util.Response
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class PostRemote @Inject constructor(
        private val postService: PostService,
        private val gson: Gson
) {

    fun getAllPost(): Single<List<PostResponse>> {
        return postService.getAllPost().map {
            if (it.isSuccessful) {
                it.body()!!.data
            } else {
                val errorBody = gson.fromJson(it.errorBody()!!.charStream(), Response::class.java)
                throw Throwable(errorBody.message)
            }
        }
    }

    fun getPost(postId: Int): Single<PostResponse> {
        return postService.getPost(postId).map {
            if (it.isSuccessful) {
                it.body()!!.data
            } else {
                val errorBody = gson.fromJson(it.errorBody()!!.charStream(), Response::class.java)
                throw Throwable(errorBody.message)
            }
        }
    }

    fun getAllPostByUsername(username: String): Single<List<PostResponse>> {
        return postService.getAllPostByUsername(username).map {
            if (it.isSuccessful) {
                it.body()!!.data
            } else {
                val errorBody = gson.fromJson(it.errorBody()!!.charStream(), Response::class.java)
                throw Throwable(errorBody.message)
            }
        }
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

        return postService.postPost(usernameBody, passwordBody, isAnonymousBody, contentBody, imageList).map {
            if (it.isSuccessful) {
                it.body()!!.data
            } else {
                val errorBody = gson.fromJson(it.errorBody()!!.charStream(), Response::class.java)
                throw Throwable(errorBody.message)
            }
        }
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

        return postService.updatePost(postId, usernameBody, passwordBody, isAnonymousBody, contentBody, deleteFileList, updateFileList).map {
            if (it.isSuccessful) {
                it.body()!!.data
            } else {
                val errorBody = gson.fromJson(it.errorBody()!!.charStream(), Response::class.java)
                throw Throwable(errorBody.message)
            }
        }
    }

    fun deletePost(
            postId: Int,
            username: String,
            password: String,
    ): Single<PostResponse> {

        return postService.deletePost(postId, username, password).map {
            if (it.isSuccessful) {
                it.body()!!.data
            } else {
                val errorBody = gson.fromJson(it.errorBody()!!.charStream(), Response::class.java)
                throw Throwable(errorBody.message)
            }
        }
    }

}