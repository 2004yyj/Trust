package kr.hs.dgsw.trust.server.controller

import javassist.NotFoundException
import kr.hs.dgsw.trust.server.data.entity.Account
import kr.hs.dgsw.trust.server.data.entity.Comment
import kr.hs.dgsw.trust.server.data.entity.toJsonObject
import kr.hs.dgsw.trust.server.data.response.JsonResponse
import kr.hs.dgsw.trust.server.exception.BadRequestException
import kr.hs.dgsw.trust.server.exception.UnauthenticatedException
import kr.hs.dgsw.trust.server.repository.AccountRepository
import kr.hs.dgsw.trust.server.repository.CommentRepository
import kr.hs.dgsw.trust.server.repository.PostRepository
import kr.hs.dgsw.trust.server.service.FileService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.configurationprocessor.json.JSONArray
import org.springframework.boot.configurationprocessor.json.JSONObject
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.sql.Timestamp

@RestController
class CommentController(
    private val commentRepository: CommentRepository,
    private val postRepository: PostRepository,
    private val accountRepository: AccountRepository,
    private val passwordEncoder: PasswordEncoder
) {

    @Autowired
    private lateinit var fileService: FileService

    @GetMapping("/comment")
    fun getCommentList(postId: Int): String {
        val list = commentRepository.findByPostId(postId)
        val jsonList = JSONArray()
        list.forEach { comment ->
            jsonList.put(getCommentToObject(comment))
        }

        return JsonResponse(
            "200",
            "댓글을 성공적으로 가져왔습니다.",
            jsonList
        ).returnJsonObject()
    }

    @PostMapping("/comment/save")
    fun saveComment(
        postId: Int,
        username: String,
        password: String,
        content: String,
        isAnonymous: Boolean,
        imageList: List<MultipartFile>?
    ): String {
        val comment = Comment()
        if (postRepository.existsById(postId)) {
            try {
                val account = accountRepository.findById(username).orElseThrow()
                val pathList = ArrayList<String>()

                if (username == account.username && passwordEncoder.matches(password, account.password)) {
                    comment.postId = postId
                    comment.username =
                        if (!isAnonymous) {
                            username
                        } else {
                            passwordEncoder.encode(username)
                        }
                    comment.isAnonymous = isAnonymous
                    comment.createdAt = Timestamp(System.currentTimeMillis())
                    comment.content = content
                    imageList?.forEach {
                        val file = fileService.saveFile(it)
                        pathList.add(file)
                    }
                    comment.imageList = JSONArray(pathList).toString()
                    commentRepository.save(comment)
                } else {
                    throw UnauthenticatedException("계정을 찾을 수 없습니다.")
                }
            } catch (e: BadRequestException) {
                throw BadRequestException("오류가 발생했습니다.")
            }
        } else {
            throw NotFoundException("글을 찾을 수 없습니다.")
        }

        val list = commentRepository.findByPostId(postId)
        val jsonList = JSONArray()
        list.forEach {
            jsonList.put(getCommentToObject(it))
        }

        return JsonResponse(
            "200",
            "댓글을 성공적으로 추가했습니다.",
            jsonList
        ).returnJsonObject()
    }

    @PutMapping("/comment/update")
    fun updateComment(
        commentId: Int,
        username: String,
        password: String,
        content: String,
        deleteFileList: Array<String>?,
        updateFileList: ArrayList<MultipartFile>?
    ): String {

        val comment =
            try {
                commentRepository.findById(commentId).orElseThrow()
            } catch (e: NoSuchElementException) {
                throw NotFoundException("댓글을 찾을 수 없습니다.")
            }
        val account =
            try {
                accountRepository.findById(username).orElseThrow()
            } catch (e: NoSuchElementException) {
                throw UnauthenticatedException("계정을 찾을 수 없습니다.")
            }

        if (commentRepository.existsById(commentId)) {
            val accountMatch =
                if (comment.isAnonymous == true)
                    passwordEncoder.matches(username, comment.username)
                else
                    username == comment.username

            if (accountMatch && passwordEncoder.matches(password, account.password!!)) {
                comment.content = content

                val imageJsonArray = JSONArray(comment.imageList)

                val pathList = ArrayList<String>()

                var i = 0
                while (i < imageJsonArray.length()) {
                    pathList.add(imageJsonArray[i] as String)
                    i++
                }

                deleteFileList?.forEach {
                    if (fileService.isFileExist(it)) {
                        fileService.deleteFileByName(it)
                        pathList.remove(it)
                    }
                }

                updateFileList?.forEach {
                    if (!it.originalFilename.isNullOrEmpty()) {
                        val fileName = fileService.saveFile(it)
                        pathList.add(fileName)
                    }
                }

                commentRepository.save(comment)
            } else {
                throw UnauthenticatedException("계정을 찾을 수 없습니다.")
            }
        } else {
            throw NotFoundException("댓글을 찾을 수 없습니다.")
        }

        val list = commentRepository.findByPostId(comment.postId!!)
        val jsonList = JSONArray()
        list.forEach {
            jsonList.put(getCommentToObject(it))
        }

        return JsonResponse(
            "200",
            "댓글을 성공적으로 추가했습니다.",
            jsonList
        ).returnJsonObject()
    }

    @DeleteMapping("/comment/delete")
    fun deleteComment(commentId: Int, username: String, password: String): String {

        val comment =
            try {
                commentRepository.findById(commentId).orElseThrow()
            } catch (e: NoSuchElementException) {
                throw NotFoundException("댓글을 찾을 수 없습니다.")
            }
        val account =
            try {
                accountRepository.findById(username).orElseThrow()
            } catch (e: NoSuchElementException) {
                throw UnauthenticatedException("계정을 찾을 수 없습니다.")
            }

        if (commentRepository.existsById(commentId)) {
            val accountMatch =
                if (comment.isAnonymous == true)
                    passwordEncoder.matches(username, comment.username)
                else
                    username == comment.username

            if (accountMatch && passwordEncoder.matches(password, account.password!!)) {

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

                comment.imageList = JSONArray(pathList).toString()

                commentRepository.deleteById(commentId)
            } else {
                throw UnauthenticatedException("계정을 찾을 수 없습니다.")
            }
        } else {
            throw NotFoundException("댓글을 찾을 수 없습니다.")
        }

        val list = commentRepository.findByPostId(comment.postId!!)
        val jsonList = JSONArray()
        list.forEach {
            jsonList.put(getCommentToObject(it))
        }

        return JsonResponse(
            "200",
            "댓글을 성공적으로 추가했습니다.",
            jsonList
        ).returnJsonObject()
    }

    fun getCommentToObject(comment: Comment): JSONObject {
        val commentObject = comment.toJsonObject()
        commentObject.put("accoount", findAccount(comment.username!!, comment.isAnonymous!!).toJsonObject())
        return commentObject
    }

    fun findAccount(username: String, isAnonymous: Boolean): Account {
        return try {
            val account = accountRepository.findById(username).orElseThrow()
            account.password = null
            account
        } catch (e: NoSuchElementException) {
            val account = Account()

            if (isAnonymous) {
                account.username = username
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
}