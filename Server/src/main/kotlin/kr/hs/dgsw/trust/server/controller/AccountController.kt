package kr.hs.dgsw.trust.server.controller

import kr.hs.dgsw.trust.server.data.dto.TokenDTO
import kr.hs.dgsw.trust.server.data.dto.toJsonObject
import kr.hs.dgsw.trust.server.data.response.JsonResponse
import kr.hs.dgsw.trust.server.exception.BadRequestException
import kr.hs.dgsw.trust.server.exception.ExistsException
import kr.hs.dgsw.trust.server.exception.UnauthenticatedException
import kr.hs.dgsw.trust.server.service.AccountService
import kr.hs.dgsw.trust.server.service.FileService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/account")
class AccountController(
    private val accountService: AccountService,
    private val fileService: FileService,
) {

    @PostMapping("/autoLogin")
    fun login(@RequestHeader (name="Authorization") token: String): String {
        val tokenDTO = accountService.login(token)
        return JsonResponse(
            "200",
            "로그인에 성공하였습니다.",
            tokenDTO.toJsonObject()
        ).returnJsonObject()
    }

    @PostMapping("/login")
    fun login(username: String, password: String) : String {
        val tokenDTO = accountService.login(username, password)
        return JsonResponse(
            "200",
            "로그인에 성공하였습니다.",
            tokenDTO.toJsonObject()
        ).returnJsonObject()
    }
    @PostMapping("/signUp")
    fun signUp(name: String,
               username: String,
               password: String,
               profileImage: MultipartFile?,
    ) : String {
        val profileImageStr = if (profileImage != null) {
            fileService.saveFile(profileImage)
        } else {
            "defaultUserProfile.png"
        }

        val tokenDTO = accountService.signUp(name, username, password, profileImageStr)
        return JsonResponse(
            "200",
            "회원가입에 성공하였습니다.",
            tokenDTO.toJsonObject()
        ).returnJsonObject()
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