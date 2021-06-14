package kr.hs.dgsw.domain.repository

import io.reactivex.Single
import kr.hs.dgsw.domain.entity.Post
import okhttp3.MultipartBody

interface PostRepository {

    fun getAllPost() : Single<List<Post>>

    fun getPost(postId: Int) : Single<Post>

    fun getAllPostByUsername(username: String) : Single<List<Post>>

    fun postPost(
            isAnonymous: Boolean,
            content: String,
            imageList: List<MultipartBody.Part>?
    ) : Single<Post>

    fun updatePost(
            postId: Int,
            isAnonymous: Boolean?,
            content: String?,
            deleteFileList: List<String>?,
            updateFileList: List<MultipartBody.Part>?
    ) : Single<Post>

    fun deletePost(
            postId: Int,
    ) : Single<Post>

}