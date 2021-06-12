package kr.hs.dgsw.trust.server.controller

import kr.hs.dgsw.trust.server.data.dto.TokenDTO
import kr.hs.dgsw.trust.server.data.dto.toJsonObject
import kr.hs.dgsw.trust.server.data.entity.Account
import kr.hs.dgsw.trust.server.data.entity.toJsonObject
import kr.hs.dgsw.trust.server.data.response.JsonResponse
import kr.hs.dgsw.trust.server.exception.BadRequestException
import kr.hs.dgsw.trust.server.exception.ExistsException
import kr.hs.dgsw.trust.server.exception.UnauthenticatedException
import kr.hs.dgsw.trust.server.service.AccountService
import kr.hs.dgsw.trust.server.service.FileService
import kr.hs.dgsw.trust.server.token.JwtFilter
import kr.hs.dgsw.trust.server.token.TokenProvider
import kr.hs.dgsw.trust.server.util.tokenGenerator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.core.userdetails.User
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/account")
class AccountController(
    private val accountService: AccountService,
    private val authenticationManagerBuilder: AuthenticationManagerBuilder,
    private val tokenProvider: TokenProvider
) {

    @PostMapping("/autoLogin")
    fun login(@RequestHeader (name="Authorization") token: String): String {
        return if (tokenProvider.validateToken(token)) {
            val authentication = tokenProvider.getAuthentication(token)
            val user = authentication.principal as User
            val account = accountService.getAccount(user.username)

            JsonResponse(
                "200",
                "로그인에 성공하였습니다.",
                account.toJsonObject()
            ).returnJsonObject()
        } else {
            throw UnauthenticatedException("유효하지 않은 토큰입니다.")
        }
    }

    @PostMapping("/login")
    fun login(username: String, password: String) : String {
        val account = Account()
        account.username = username
        account.password = password

        return if (isIdAndPwNotNull(account)) {
            if (accountService.isIdAndPwExist(account)) {

                val jwt = tokenGenerator(username, password, authenticationManagerBuilder, tokenProvider)

//                val httpHeaders = HttpHeaders()
//                httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer $jwt")
                
                JsonResponse(
                    "200",
                    "로그인에 성공하였습니다.",
                    TokenDTO(jwt, username).toJsonObject()
                ).returnJsonObject()
            } else {
                throw UnauthenticatedException("아이디 또는 비밀번호가 잘못되었습니다.")
            }
        } else {
            throw NullPointerException("빈칸이 없는지 확인해 주세요.")
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
        return if (isAccountInfoNotNull(name, username, password)) {

            accountService.signUp(name, username, password, profileImage)

            val jwt = tokenGenerator(username, password, authenticationManagerBuilder, tokenProvider)

            JsonResponse(
                "200",
                "회원가입에 성공하였습니다.",
                TokenDTO(jwt, username).toJsonObject()
            ).returnJsonObject()
        } else {
            throw NullPointerException("빈칸이 없는지 확인해 주세요.")
        }
    }

    fun isAccountInfoNotNull(name: String, username: String, password: String) : Boolean {
        return !(name.isNullOrEmpty() || username.isNullOrEmpty() || password.isNullOrEmpty())
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

    @ExceptionHandler(value = [ExistsException::class])
    @ResponseStatus(HttpStatus.CONFLICT)
    fun handler(error: ExistsException): String {
        return JsonResponse("409", error.message.toString(), null).returnJsonObject()
    }

    @ExceptionHandler(value = [NullPointerException::class])
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handler(error: NullPointerException): String {
        return JsonResponse("400", error.message.toString(), null).returnJsonObject()
    }
}