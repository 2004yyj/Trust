package kr.hs.dgsw.trust.server.controller

import javassist.NotFoundException
import kr.hs.dgsw.trust.server.data.dto.*
import kr.hs.dgsw.trust.server.data.entity.AccountDTO
import kr.hs.dgsw.trust.server.data.entity.toJsonObject
import kr.hs.dgsw.trust.server.data.response.JsonResponse
import kr.hs.dgsw.trust.server.exception.ExistsException
import kr.hs.dgsw.trust.server.exception.UnauthenticatedException
import kr.hs.dgsw.trust.server.repository.AccountRepository
import kr.hs.dgsw.trust.server.repository.LikedRepository
import kr.hs.dgsw.trust.server.service.AccountService
import kr.hs.dgsw.trust.server.service.LikedService
import kr.hs.dgsw.trust.server.token.TokenProvider
import org.springframework.boot.configurationprocessor.json.JSONArray
import org.springframework.boot.configurationprocessor.json.JSONObject
import org.springframework.http.HttpStatus
import org.springframework.security.core.userdetails.User
import org.springframework.web.bind.annotation.*
import java.sql.Timestamp

@RestController
class LikedController(
    private val likedRepository: LikedRepository,
    private val accountService: AccountService,
    private val tokenProvider: TokenProvider
) {
    @GetMapping("/liked")
    fun likedList(@RequestHeader("Authorization") token: String, postId: Int): String {
        if (token.isNotEmpty() && tokenProvider.validateToken(token)) {
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
        } else {
            throw UnauthenticatedException("세션이 만료되었습니다. 다시 로그인 해 주세요.")
        }
    }

    @PostMapping("/liked/save")
    fun saveLiked(@RequestHeader("Authorization") token: String, postId: Int): String {
        val liked = Liked()
        if (token.isNotEmpty() && tokenProvider.validateToken(token)) {
            val username = (tokenProvider.getAuthentication(token).principal as User).username

            try {
                likedRepository.findByPostIdAndUsername(postId, username).orElseThrow()
                throw ExistsException("좋아요가 이미 존재합니다.")
            } catch (e: NoSuchElementException) {
                liked.postId = postId
                liked.username = username
                liked.createdAt = Timestamp(System.currentTimeMillis())
                likedRepository.save(liked)
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
        } else {
            throw UnauthenticatedException("세션이 만료되었습니다. 다시 로그인 해 주세요.")
        }
    }

    @DeleteMapping("/liked/delete")
    fun deleteLiked(@RequestHeader("Authorization") token: String, postId: Int): String {

        if (token.isNotEmpty() && tokenProvider.validateToken(token)) {

            val username = (tokenProvider.getAuthentication(token).principal as User).username

            val liked =
                try {
                    likedRepository.findByPostIdAndUsername(postId, username).orElseThrow()
                } catch (e: NoSuchElementException) {
                    throw NotFoundException("좋아요가 존재하지 않습니다.")
                }

            if (username == liked.username) {
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
        } else {
            throw UnauthenticatedException("세션이 만료되었습니다. 다시 로그인 해 주세요.")
        }
    }

    fun getLikedToObject(liked: Liked): JSONObject {
        val likedObject = liked.toJsonObject()
        likedObject.put("account", findAccount(liked.username!!).toJsonObject())
        return likedObject
    }

    fun findAccount(username: String): AccountDTO {
        val accountVO = accountService.getAccount(username)
        return accountVO.toDTO()
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