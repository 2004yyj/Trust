package kr.hs.dgsw.trust.server.controller

import javassist.NotFoundException
import kr.hs.dgsw.trust.server.data.entity.Account
import kr.hs.dgsw.trust.server.data.entity.Liked
import kr.hs.dgsw.trust.server.data.entity.toJsonObject
import kr.hs.dgsw.trust.server.data.response.JsonResponse
import kr.hs.dgsw.trust.server.exception.ExistsException
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
    @GetMapping("/liked")
    fun likedList(postId: Int): String {
        val list = likedRepository.findAllByPostId(postId).orElseThrow()
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

    @PostMapping("/liked/save")
    fun saveLiked(postId: Int, username: String, password: String): String {
        val liked = Liked()
        try {
            val account = accountRepository.findById(username).orElseThrow()

            if (username == account.username && passwordEncoder.matches(password, account.password)) {

                try {
                    likedRepository.findByPostIdAndUsername(postId, username).orElseThrow()
                    throw ExistsException("좋아요가 이미 존재합니다.")
                } catch (e: NoSuchElementException) {
                    liked.postId = postId
                    liked.username = username
                    liked.createdAt = Timestamp(System.currentTimeMillis())
                    likedRepository.save(liked)
                }

            } else {
                throw UnauthenticatedException("계정을 찾을 수 없습니다.")
            }
        } catch (e: NoSuchElementException) {
            throw UnauthenticatedException("계정을 찾을 수 없습니다.")
        }

        val list = likedRepository.findAllByPostId(postId).orElseThrow()
        val jsonList = JSONArray()
        list.forEach {
            jsonList.put(getLikedToObject(it))
        }

        return JsonResponse(
            "200",
            "좋아요를 성공적으로 추가했습니다.",
            jsonList
        ).returnJsonObject()
    }

    @DeleteMapping("/liked/delete")
    fun deleteLiked(postId: Int, username: String, password: String): String {

        val liked =
            try {
                likedRepository.findByPostIdAndUsername(postId, username).orElseThrow()
            } catch (e: NoSuchElementException) {
                throw NotFoundException("좋아요가 존재하지 않습니다.")
            }

        val account =
            try {
                accountRepository.findById(username).orElseThrow()
            } catch (e: NoSuchElementException) {
                throw UnauthenticatedException("계정을 찾을 수 없습니다.")
            }

        val accountMatch = username == liked.username

        if (accountMatch && passwordEncoder.matches(password, account.password!!)) {
            likedRepository.deleteByPostIdAndUsername(postId, username)
        } else {
            throw UnauthenticatedException("계정을 찾을 수 없습니다.")
        }

        val list = likedRepository.findAllByPostId(postId).orElseThrow()
        val jsonList = JSONArray()
        list.forEach {
            jsonList.put(getLikedToObject(it))
        }

        return JsonResponse(
            "200",
            "좋아요를 성공적으로 삭제했습니다.",
            jsonList
        ).returnJsonObject()
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

    @ExceptionHandler(value = [UnauthenticatedException::class])
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    fun handler(error: UnauthenticatedException): String {
        return JsonResponse("401", error.message.toString(), null).returnJsonObject()
    }

    @ExceptionHandler(value = [NotFoundException::class])
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handler(error: NotFoundException): String {
        return JsonResponse("404", error.message.toString(), null).returnJsonObject()
    }

    @ExceptionHandler(value = [ExistsException::class])
    @ResponseStatus(HttpStatus.CONFLICT)
    fun handler(error: ExistsException): String {
        return JsonResponse("409", error.message.toString(), null).returnJsonObject()
    }
}