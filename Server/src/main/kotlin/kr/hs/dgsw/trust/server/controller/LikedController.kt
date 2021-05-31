package kr.hs.dgsw.trust.server.controller

import javassist.NotFoundException
import kr.hs.dgsw.trust.server.data.entity.Account
import kr.hs.dgsw.trust.server.data.entity.Liked
import kr.hs.dgsw.trust.server.data.entity.toJsonObject
import kr.hs.dgsw.trust.server.data.response.JsonResponse
import kr.hs.dgsw.trust.server.exception.BadRequestException
import kr.hs.dgsw.trust.server.exception.UnauthenticatedException
import kr.hs.dgsw.trust.server.repository.AccountRepository
import kr.hs.dgsw.trust.server.repository.LikedRepository
import org.springframework.boot.configurationprocessor.json.JSONArray
import org.springframework.boot.configurationprocessor.json.JSONObject
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*
import java.sql.Timestamp

@RestController
class LikedController(
    private val likedRepository: LikedRepository,
    private val accountRepository: AccountRepository,
    private val passwordEncoder: PasswordEncoder
) {
    @GetMapping("/liked/{postId}")
    fun likedList(@PathVariable postId: Int): String {
        val list = likedRepository.findByPostId(postId)
        val jsonList = JSONArray()
        list.forEach { liked ->
            jsonList.put(getLikedToObject(liked))
        }

        return JsonResponse(
            "200",
            "좋아요를 성공적으로 가져왔습니다.",
            jsonList
        ).returnJsonObject()
    }

    @PostMapping("/liked/{postId}/save")
    fun saveLiked(@PathVariable postId: Int, username: String, password: String): String {
        val liked = Liked()
        try {
            val account = accountRepository.findById(username).orElseThrow()

            if (username == account.username && passwordEncoder.matches(password, account.password)) {
                liked.postId = postId
                liked.username = username
                liked.createdAt = Timestamp(System.currentTimeMillis())
                likedRepository.save(liked)
            } else {
                throw UnauthenticatedException("계정을 찾을 수 없습니다.")
            }
        } catch (e: BadRequestException) {
            throw BadRequestException("오류가 발생했습니다.")
        }

        return JsonResponse(
            "200",
            "좋아요를 성공적으로 추가하였습니다.",
            getLikedToObject(liked)
        ).returnJsonObject()

    }

    @DeleteMapping("/liked/{likedId}/delete")
    fun deleteLiked(@PathVariable likedId: Int, username: String, password: String): JsonResponse<JSONObject> {
        return try {
            val liked = likedRepository.findById(likedId).orElseThrow()
            val account = accountRepository.findById(username).orElseThrow()

            val accountMatch = username == liked.username

            if (accountMatch && passwordEncoder.matches(password, account.password!!)) {
                likedRepository.deleteById(likedId)
                JsonResponse("200",
                    "좋아요를 성공적으로 삭제하였습니다.",
                    getLikedToObject(liked)
                )

            } else {
                throw UnauthenticatedException("계정을 찾을 수 없습니다.")
            }
        } catch (e: BadRequestException) {
            throw BadRequestException("오류가 발생했습니다.")
        }
    }

    fun getLikedToObject(liked: Liked): JSONObject {
        val likedObject = liked.toJsonObject()
        likedObject.put("account", findAccount(liked.username!!).toJsonObject())
        return likedObject
    }

    fun findAccount(username: String): Account {
        return try {
            val account = accountRepository.findById(username).orElseThrow()
            account.password = null
            account
        } catch (e: NoSuchElementException) {
            val account = Account()

            if (username == "UnknownAccount") {
                account.username = "Unknown"
                account.name = "익명"
            } else {
                account.username = "DELETED"
                account.name = "삭제된 계정"
            }
            account.profileImage = "/image/defaultUserProfile.png"
            account
        }
    }

    @ExceptionHandler(value = [BadRequestException::class])
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handler(error: BadRequestException): String {
        return JsonResponse("400", error.message.toString(), null).returnJsonObject()
    }

    @ExceptionHandler(value = [NotFoundException::class])
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handler(error: NotFoundException): String {
        return JsonResponse("404", error.message.toString(), null).returnJsonObject()
    }
}