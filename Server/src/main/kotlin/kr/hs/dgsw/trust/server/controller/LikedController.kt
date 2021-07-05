package kr.hs.dgsw.trust.server.controller

import javassist.NotFoundException
import kr.hs.dgsw.trust.server.data.vo.*
import kr.hs.dgsw.trust.server.data.dto.AccountDTO
import kr.hs.dgsw.trust.server.data.dto.toJsonObject
import kr.hs.dgsw.trust.server.data.response.JsonResponse
import kr.hs.dgsw.trust.server.exception.ExistsException
import kr.hs.dgsw.trust.server.exception.UnauthenticatedException
import kr.hs.dgsw.trust.server.service.AccountService
import kr.hs.dgsw.trust.server.service.LikedService
import org.springframework.boot.configurationprocessor.json.JSONArray
import org.springframework.boot.configurationprocessor.json.JSONObject
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
class LikedController(
    private val likedService: LikedService,
    private val accountService: AccountService
) {
    @GetMapping("/liked")
    fun likedList(@RequestHeader("Authorization") token: String, postId: Int): String {
        val list = likedService.findAllByPostId(token, postId)
        val jsonList = JSONArray()
        list.forEach {
            jsonList.put(getLikedToObject(it, token))
        }
        return JsonResponse(
            "200",
            "좋아요를 성공적으로 가져왔습니다.",
            jsonList
        ).returnJsonObject()
    }

    @PostMapping("/liked/save")
    fun saveLiked(@RequestHeader("Authorization") token: String, postId: Int): String {
        val list = likedService.save(token, postId)

        val jsonList = JSONArray()
        list.forEach {
            jsonList.put(getLikedToObject(it, token))
        }

        return JsonResponse(
            "200",
            "좋아요를 성공적으로 추가했습니다.",
            jsonList
        ).returnJsonObject()
    }

    @DeleteMapping("/liked/delete")
    fun deleteLiked(@RequestHeader("Authorization") token: String, postId: Int): String {
        val list = likedService.delete(token, postId)
        val jsonList = JSONArray()
        list.forEach {
            jsonList.put(getLikedToObject(it, token))
        }

        return JsonResponse(
            "200",
            "좋아요를 성공적으로 삭제했습니다.",
            jsonList
        ).returnJsonObject()
    }

    fun getLikedToObject(liked: Liked, token: String): JSONObject {
        val likedObject = liked.toJsonObject()
        likedObject.put("account", findAccount(token).toJsonObject())
        return likedObject
    }

    fun findAccount(token: String): AccountDTO {
        val accountVO = accountService.getAccount(token)
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