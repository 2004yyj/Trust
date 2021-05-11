package kr.hs.dgsw.trust.server.controller

import kr.hs.dgsw.trust.server.data.entity.Account
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
    fun login(username: String, password: String) : HashMap<String, Any?> {
        val account = Account()
        account.username = username
        account.password = password

        return if (isIdAndPwNotNull(account)) {
            if (isIdAndPwExist(account)) {
                account.name = getName(account)
                account.password = ""
                JsonResponse().returnResponse("200", "로그인에 성공하였습니다.", account)
            } else {
                throw UnauthenticatedException("아이디 또는 비밀번호가 잘못되었습니다.")
            }
        } else {
            throw BadRequestException("잘못된 값이 있습니다.")
        }
    }

    fun getName(account: Account) : String {
        val account = accountRepository.findById(account.username!!).orElseThrow()
        return account.name!!
    }

    fun isIdAndPwExist(account: Account) : Boolean {
        return try {
            val foundAccount = accountRepository.findById(account.username!!).get()
            passwordEncoder.matches(account.password!!, foundAccount.password)
        } catch (e: Exception) {
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
    ) : HashMap<String, Any?> {

        val account = Account()
        account.name = name
        account.username = username
        account.password = passwordEncoder.encode(password)

        return if (isAccountInfoNotNull(account)) {
            if (isNotIdExist(account)) {
                val filePath = if (profileImage != null) {
                    val file = fileService.saveFile(profileImage)
                    "/image/$file"
                } else {
                    "/image/defaultUserProfile.png"
                }

                account.profileImage = filePath

                accountRepository.save(account)
                JsonResponse().returnResponse("200", "회원가입에 성공하였습니다.", account)
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
        return try {
            accountRepository.findById(account.username!!).get()
            false
        } catch (e: Exception) {
            true
        }
    }

    @ExceptionHandler(value = [BadRequestException::class])
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handler(error: BadRequestException): HashMap<String, Any?> {
        return JsonResponse().returnResponse("400", error.message.toString(), null)
    }

    @ExceptionHandler(value = [UnauthenticatedException::class])
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    fun handler(error: UnauthenticatedException): HashMap<String, Any?> {
        return JsonResponse().returnResponse("401", error.message.toString(), null)
    }
}