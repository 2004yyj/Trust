package kr.hs.dgsw.domain.usecase.account

import io.reactivex.Single
import kr.hs.dgsw.domain.entity.Account
import kr.hs.dgsw.domain.repository.AccountRepository
import okhttp3.MultipartBody
import javax.inject.Inject

class PostSignUpUseCase @Inject constructor(private val repository: AccountRepository) {

    fun postSignUp(name: String, username: String, password: String, profileImage: MultipartBody.Part?) : Single<Account> {
        return repository.postSignUp(name, username, password, profileImage)
    }

}