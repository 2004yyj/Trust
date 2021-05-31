package kr.hs.dgsw.domain.usecase.post

import io.reactivex.Single
import kr.hs.dgsw.domain.entity.Post
import kr.hs.dgsw.domain.repository.PostRepository
import okhttp3.MultipartBody
import javax.inject.Inject

class DeletePostUseCase @Inject constructor(private val repository: PostRepository) {
    fun deletePost(
            postId: Int,
            username: String,
            password: String
    ): Single<Post> {
        return repository.deletePost(postId, username, password)
    }
}