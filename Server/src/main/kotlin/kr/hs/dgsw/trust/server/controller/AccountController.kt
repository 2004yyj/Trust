package kr.hs.dgsw.trust.server.controller

import kr.hs.dgsw.trust.server.data.entity.Account
import kr.hs.dgsw.trust.server.data.entity.toJsonObject
import kr.hs.dgsw.trust.server.data.response.JsonResponse
import kr.hs.dgsw.trust.server.exception.BadRequestException
import kr.hs.dgsw.trust.server.exception.UnauthenticatedException
import kr.hs.dgsw.trust.server.repository.AccountRepository
import kr.hs.dgsw.trust.server.service.FileService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/account")
class AccountController(
    private val accountRepository: AccountRepository,
    private val passwordEncoder : PasswordEncoder,
) {

    @Autowired
    private lateinit var fileService: FileService

    @PostMapping("/login")
    fun login(username: String, password: String) : String {
        val account = Account()
        account.username = username
        account.password = password

        return if (isIdAndPwNotNull(account)) {
            if (isIdAndPwExist(account)) {
                val foundAccount = getAccount(account)
                account.name = foundAccount.name
                account.profileImage = foundAccount.profileImage
                JsonResponse(
                    "200",
                    "로그인에 성공하였습니다.",
                    account.toJsonObject()
                ).returnJsonObject()
            } else {
                throw UnauthenticatedException("아이디 또는 비밀번호가 잘못되었습니다.")
            }
        } else {
            throw BadRequestException("잘못된 값이 있습니다.")
        }
    }

    fun getAccount(account: Account): Account {
        return accountRepository.findById(account.username!!).orElseThrow()
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

    fun isIdAndPwNotNull(account: Account) : Boolean {
        val username : String? = account.username
        val password : String? = account.password
        return !(username.isNullOrEmpty() || password.isNullOrEmpty())
    }

    @PostMapping("/signUp")
    fun signUp(name: String,
               username: String,
               password: String,
               profileImage: MultipartFile?,
    ) : String {

        val account = Account()
        account.name = name
        account.username = username
        account.password = passwordEncoder.encode(password)

        return if (isAccountInfoNotNull(account)) {
            if (isNotIdExist(account)) {
                val filePath = if (profileImage != null) {
                    fileService.saveFile(profileImage)
                } else {
                    "defaultUserProfile.png"
                }
                account.profileImage = filePath

                accountRepository.save(account)
                JsonResponse(
                    "200",
                    "회원가입에 성공하였습니다.",
                    account.toJsonObject()
                ).returnJsonObject()
            } else {
                throw UnauthenticatedException("중복된 아이디가 있습니다.")
            }
        } else {
            throw BadRequestException("잘못된 값이 있습니다.")
        }
    }

    fun isAccountInfoNotNull(account: Account) : Boolean {
        val name: String? = account.name
        val username: String? = account.username
        val password: String? = account.password
        return !(name.isNullOrEmpty() || username.isNullOrEmpty() || password.isNullOrEmpty())
    }

    fun isNotIdExist(account: Account) : Boolean {
        return accountRepository.findById(account.username!!).isEmpty
    }

    @ExceptionHandler(value = [BadRequestException::class])
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handler(error: BadRequestException): String {
        return JsonResponse("400", error.message.toString(), null).returnJsonObject()
    }

    @ExceptionHandler(value = [UnauthenticatedException::class])
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    fun handler(error: UnauthenticatedException): String {
        return JsonResponse("401", error.message.toString(), null).returnJsonObject()
    }
}