package kr.hs.dgsw.domain.usecase.post

import io.reactivex.Single
import kr.hs.dgsw.domain.entity.Post
import kr.hs.dgsw.domain.repository.PostRepository
import okhttp3.MultipartBody
import javax.inject.Inject

class GetPostUseCase @Inject constructor(private val repository: PostRepository) {

    fun getAllPost() : Single<List<Post>> {
        return repository.getAllPost()
    }

    fun getAllPostByUsername(username: String) : Single<List<Post>> {
        return repository.getAllPostByUsername(username)
    }

    fun getPost(postId: Int) : Single<Post> {
        return repository.getPost(postId)
    }

}