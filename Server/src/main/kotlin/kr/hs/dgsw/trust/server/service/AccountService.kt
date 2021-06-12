package kr.hs.dgsw.trust.server.service

import kr.hs.dgsw.trust.server.data.entity.Account
import kr.hs.dgsw.trust.server.data.entity.Authority
import kr.hs.dgsw.trust.server.data.response.JsonResponse
import kr.hs.dgsw.trust.server.exception.ExistsException
import kr.hs.dgsw.trust.server.exception.UnauthenticatedException
import kr.hs.dgsw.trust.server.repository.AccountRepository
import org.apache.catalina.security.SecurityUtil
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.multipart.MultipartFile
import java.util.*

@Service
class AccountService(
    private val accountRepository: AccountRepository,
    private val fileService: FileService,
    private val passwordEncoder: PasswordEncoder
) {

    @Transactional
    fun signUp(name: String,
               username: String,
               password: String,
               profileImage: MultipartFile?
    ): Account {
        if (accountRepository.findById(username).orElse(null) != null) {
            throw ExistsException("이미 가입되어 있는 유저입니다.")
        }

        //빌더 패턴의 장점
        val authority = Authority()
        authority.authorityName = "ROLE_USER"

        val account: Account = Account().apply {
            this.username = username
            this.password = passwordEncoder.encode(password)
            this.name = name
            this.profileImage = if (profileImage != null) {
                fileService.saveFile(profileImage)
            } else {
                "defaultUserProfile.png"
            }
            this.authorities = setOf(authority)
            this.activated = true
        }

        return accountRepository.save(account)
    }

    @Transactional(readOnly = true)
    fun getAccount(username: String): Account {
        return accountRepository.findById(username).orElseThrow()
    }

    fun isIdAndPwExist(account: Account) : Boolean {
        val isPresent = accountRepository.findById(account.username!!).isPresent
        return if (isPresent) {
            val foundAccount = accountRepository.findById(account.username!!).get()
            passwordEncoder.matches(account.password!!, foundAccount.password)
        } else {
            false
        }
    }
}