package kr.hs.dgsw.data.datasource

import io.reactivex.Single
import kr.hs.dgsw.data.base.BaseDataSource
import kr.hs.dgsw.data.base.NoCacheDataSource
import kr.hs.dgsw.data.mapper.toEntity
import kr.hs.dgsw.data.network.remote.PostRemote
import kr.hs.dgsw.domain.entity.Post
import okhttp3.MultipartBody
import javax.inject.Inject

class PostDataSource @Inject constructor(
        override val remote: PostRemote,
): NoCacheDataSource<PostRemote>() {

    fun getAllPost() : Single<List<Post>> {
        return remote.getAllPost().map { postResponseList ->
            val postList = ArrayList<Post>()
            postResponseList.forEach {
                postList.add(it.toEntity())
            }
            postList
        }
    }

    fun getAllPostByUsername(username: String) : Single<List<Post>> {
        return remote.getAllPostByUsername(username).map { postResponseList ->
            val postList = ArrayList<Post>()
            postResponseList.forEach {
                postList.add(it.toEntity())
            }
            postList
        }
    }

    fun getPost(postId: Int) : Single<Post> {
        return remote.getPost(postId).map { it.toEntity() }
    }

    fun postPost(
            username: String,
            password: String,
            isAnonymous: Boolean,
            content: String,
            imageList: List<MultipartBody.Part>?
    ) : Single<Post> {
        return remote.postPost(username, password, isAnonymous, content, imageList).map { it.toEntity() }
    }

    fun updatePost(
            postId: Int,
            username: String,
            password: String,
            isAnonymous: Boolean?,
            content: String?,
            deleteFileList: List<String>?,
            updateFileList: List<MultipartBody.Part>?
    ) : Single<Post> {
        return remote.updatePost(postId, username, password, isAnonymous, content, deleteFileList, updateFileList).map { it.toEntity() }
    }

    fun deletePost(
            postId: Int,
            username: String,
            password: String
    ) : Single<Post> {
        return remote.deletePost(postId, username, password).map { it.toEntity() }
    }

}