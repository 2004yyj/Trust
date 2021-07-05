package kr.hs.dgsw.trust.server.controller

import javassist.NotFoundException
import kr.hs.dgsw.trust.server.data.vo.CommentVO
import kr.hs.dgsw.trust.server.data.vo.toDTO
import kr.hs.dgsw.trust.server.data.vo.toJsonObject
import kr.hs.dgsw.trust.server.data.dto.AccountDTO
import kr.hs.dgsw.trust.server.data.dto.toJsonObject
import kr.hs.dgsw.trust.server.data.response.JsonResponse
import kr.hs.dgsw.trust.server.exception.UnauthenticatedException
import kr.hs.dgsw.trust.server.repository.PostRepository
import kr.hs.dgsw.trust.server.service.AccountService
import kr.hs.dgsw.trust.server.service.CommentService
import kr.hs.dgsw.trust.server.service.FileService
import kr.hs.dgsw.trust.server.token.TokenProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.configurationprocessor.json.JSONArray
import org.springframework.boot.configurationprocessor.json.JSONObject
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
class CommentController(
    private val commentService: CommentService,
    private val accountService: AccountService,
) {

    @Autowired
    private lateinit var fileService: FileService

    @GetMapping("/comment")
    fun getCommentList(
        @RequestHeader (name="Authorization") token: String,
        postId: Int
    ): String {
        val list = commentService.findAllComment(token, postId)
        val jsonList = JSONArray()
        list.forEach { comment ->
            jsonList.put(getCommentToObject(comment, token))
        }

        return JsonResponse(
            "200",
            "댓글을 성공적으로 가져왔습니다.",
            jsonList
        ).returnJsonObject()
    }

    @PostMapping("/comment/save")
    fun saveComment(
        @RequestHeader (name="Authorization") token: String,
        postId: Int,
        content: String,
        imageList: ArrayList<MultipartFile>?,
    ): String {
        val pathList = ArrayList<String>()

        imageList?.forEach {
            val file = fileService.saveFile(it)
            pathList.add(file)
        }

        commentService.saveComment(token, postId, content, pathList)

        val list = commentService.findAllComment(token, postId)
        val jsonList = JSONArray()
        list.forEach {
            jsonList.put(getCommentToObject(it, token))
        }

        return JsonResponse(
            "200",
            "댓글을 성공적으로 추가했습니다.",
            jsonList
        ).returnJsonObject()
    }

    @PutMapping("/comment/update")
    fun updateComment(
        @RequestHeader("Authorization")
        token: String,
        commentId: Int,
        content: String,
        deleteFileList: Array<String>?,
        updateFileList: ArrayList<MultipartFile>?
    ): String {

        val pathList = fileService.updateCommentFile(commentId, deleteFileList, updateFileList)

        val list = commentService.updateComment(token, commentId, content, pathList)

        val jsonList = JSONArray()
        list.forEach {
            jsonList.put(getCommentToObject(it, token))
        }

        return JsonResponse(
        "200",
        "댓글을 성공적으로 추가했습니다.",
        jsonList
        ).returnJsonObject()
    }

    @DeleteMapping("/comment/delete")
    fun deleteComment(
        @RequestHeader("Authorization")
        token: String,
        commentId: Int
    ): String {
        val comment = commentService.deleteComment(token, commentId)
        val imageJsonArray = JSONArray(comment.imageList)
        val pathList = ArrayList<String>()

        var i = 0
        while (i < imageJsonArray.length()) {
            pathList.add(imageJsonArray[i] as String)
            i++
        }

        pathList.forEach {
            fileService.deleteFileByName(it)
        }

        val list = commentService.findAllComment(token, commentId)
        val jsonList = JSONArray()
        list.forEach {
            jsonList.put(getCommentToObject(it, token))
        }

        return JsonResponse(
            "200",
            "댓글을 성공적으로 삭제했습니다.",
            jsonList
        ).returnJsonObject()
    }

    fun getCommentToObject(commentVO: CommentVO, token: String): JSONObject {
        val commentObject = commentVO.toJsonObject()
        commentObject.put("account", findAccount(token).toJsonObject())
        return commentObject
    }

    fun findAccount(token: String): AccountDTO {
        val account = accountService.getAccount(token)
        return account.toDTO()
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
}