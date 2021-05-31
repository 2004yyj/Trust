package kr.hs.dgsw.domain.usecase.post

import io.reactivex.Single
import kr.hs.dgsw.domain.entity.Post
import kr.hs.dgsw.domain.repository.PostRepository
import okhttp3.MultipartBody
import javax.inject.Inject

class PostPostUseCase @Inject constructor(private val repository: PostRepository) {
    fun postPost(
            username: String,
            password: String,
            content: String,
            isAnonymous: Boolean,
            imageList: List<MultipartBody.Part>?
    ): Single<Post> {
        return repository.postPost(username, password, isAnonymous, content, imageList)
    }
}