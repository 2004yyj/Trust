package kr.hs.dgsw.trust.server.controller

import javassist.NotFoundException
import kr.hs.dgsw.trust.server.data.entity.Account
import kr.hs.dgsw.trust.server.data.entity.Liked
import kr.hs.dgsw.trust.server.data.entity.toHashMap
import kr.hs.dgsw.trust.server.data.response.JsonResponse
import kr.hs.dgsw.trust.server.exception.BadRequestException
import kr.hs.dgsw.trust.server.exception.UnauthenticatedException
import kr.hs.dgsw.trust.server.repository.AccountRepository
import kr.hs.dgsw.trust.server.repository.LikedRepository
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
    fun likedList(@PathVariable postId: Int): ArrayList<HashMap<String, Any?>> {
        val list = likedRepository.findByPostId(postId)
        val jsonList = ArrayList<HashMap<String, Any?>>()
        list.forEach { liked ->
            jsonList.add(getLikedToHashMap(liked))
        }

        return jsonList
    }

    @GetMapping("/liked/{postId}/save")
    fun saveLiked(@PathVariable postId: Int, username: String, password: String): HashMap<String, Any?> {
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

        return JsonResponse().returnResponse(
            "200",
            "좋아요를 성공적으로 추가하였습니다.",
            getLikedToHashMap(liked)
        )
    }

    @GetMapping("/liked/{id}/delete")
    fun deleteLiked(@PathVariable id: Int, username: String, password: String): HashMap<String, Any?> {
        return try {
            val liked = likedRepository.findById(id).orElseThrow()
            val account = accountRepository.findById(username).orElseThrow()

            val accountMatch = username == liked.username

            if (accountMatch && passwordEncoder.matches(password, account.password!!)) {
                likedRepository.deleteById(id)
                JsonResponse().returnResponse(
                    "200",
                    "글을 성공적으로 삭제하였습니다.",
                    getLikedToHashMap(liked)
                )
            } else {
                throw UnauthenticatedException("계정을 찾을 수 없습니다.")
            }
        } catch (e: BadRequestException) {
            throw BadRequestException("오류가 발생했습니다.")
        }
    }

    fun getLikedToHashMap(liked: Liked): HashMap<String, Any?> {
        val postMap = liked.toHashMap()
        postMap["account"] = findAccount(liked.username!!).toHashMap()
        postMap.remove("username")
        return postMap
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
    fun handler(error: BadRequestException): HashMap<String, Any?> {
        return JsonResponse().returnResponse("400", error.message.toString(), null)
    }

    @ExceptionHandler(value = [NotFoundException::class])
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handler(error: NotFoundException): HashMap<String, Any?> {
        return JsonResponse().returnResponse("404", error.message.toString(), null)
    }
}