package kr.hs.dgsw.data.repository

import io.reactivex.Single
import kr.hs.dgsw.data.datasource.AccountDataSource
import kr.hs.dgsw.data.datasource.PostDataSource
import kr.hs.dgsw.domain.entity.Account
import kr.hs.dgsw.domain.entity.Post
import kr.hs.dgsw.domain.repository.AccountRepository
import kr.hs.dgsw.domain.repository.PostRepository
import okhttp3.MultipartBody
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(private val postDataSource: PostDataSource) : PostRepository {
    override fun getAllPost(): Single<List<Post>> {
        return postDataSource.getAllPost()
    }

    override fun getPost(postId: Int): Single<Post> {
        return postDataSource.getPost(postId)
    }

    override fun getAllPostByUsername(username: String): Single<List<Post>> {
        return postDataSource.getAllPostByUsername(username)
    }

    override fun postPost(username: String, password: String, isAnonymous: Boolean, content: String, imageList: List<MultipartBody.Part>?): Single<Post> {
        return postDataSource.postPost(username, password, isAnonymous, content, imageList)
    }

    override fun updatePost(postId: Int, username: String, password: String, isAnonymous: Boolean?, content: String?, deleteFileList: List<String>?, updateFileList: List<MultipartBody.Part>?): Single<Post> {
        return postDataSource.updatePost(postId, username, password, isAnonymous, content, deleteFileList, updateFileList)
    }

    override fun deletePost(postId: Int, username: String, password: String): Single<Post> {
        return postDataSource.deletePost(postId, username, password)
    }


}