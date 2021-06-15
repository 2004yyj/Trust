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
import kr.hs.dgsw.trust.server.token.TokenProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.configurationprocessor.json.JSONArray
import org.springframework.boot.configurationprocessor.json.JSONObject
import org.springframework.http.HttpStatus
import org.springframework.security.core.userdetails.User
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.sql.Timestamp

@RestController
class CommentController(
    private val commentRepository: CommentRepository,
    private val postRepository: PostRepository,
    private val accountRepository: AccountRepository,
    private val tokenProvider: TokenProvider
) {

    @Autowired
    private lateinit var fileService: FileService

    @GetMapping("/comment")
    fun getCommentList(
        @RequestHeader("Authorization")
        token: String,
        postId: Int
    ): String {
        if (token.isNotEmpty() && tokenProvider.validateToken(token)) {
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
        } else {
            throw UnauthenticatedException("세션이 만료되었습니다. 다시 로그인 해 주세요.")
        }
    }

    @PostMapping("/comment/save")
    fun saveComment(
        @RequestHeader("Authorization")
        token: String,
        postId: Int,
        content: String,
        imageList: List<MultipartFile>?
    ): String {
        val comment = Comment()
        if (token.isNotEmpty() && tokenProvider.validateToken(token)) {
            if (postRepository.existsById(postId)) {
                try {
                    val username = (tokenProvider.getAuthentication(token).principal as User).username

                    val pathList = ArrayList<String>()
                    comment.postId = postId
                    comment.username = username
                    comment.createdAt = Timestamp(System.currentTimeMillis())
                    comment.content = content

                    imageList?.forEach {
                        val file = fileService.saveFile(it)
                        pathList.add(file)
                    }

                    comment.imageList = JSONArray(pathList).toString()
                    commentRepository.save(comment)

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

                } catch (e: BadRequestException) {
                    throw BadRequestException("오류가 발생했습니다.")
                }
            } else {
                throw NotFoundException("글을 찾을 수 없습니다.")
            }
        } else {
            throw UnauthenticatedException("세션이 만료되었습니다. 다시 로그인 해 주세요.")
        }
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
        if (token.isNotEmpty() && tokenProvider.validateToken(token)) {
            val comment =
                try {
                    commentRepository.findById(commentId).orElseThrow()
                } catch (e: NoSuchElementException) {
                    throw NotFoundException("댓글을 찾을 수 없습니다.")
                }

            if (commentRepository.existsById(commentId)) {
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
                throw UnauthenticatedException("세션이 만료되었습니다. 다시 로그인 해 주세요.")
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
        } else {
            throw UnauthenticatedException("세션이 만료되었습니다. 다시 로그인 해 주세요.")
        }
    }

    @DeleteMapping("/comment/delete")
    fun deleteComment(
        @RequestHeader("Authorization")
        token: String,
        commentId: Int
    ): String {
        if (token.isNotEmpty() && tokenProvider.validateToken(token)) {
            val comment =
                try {
                    commentRepository.findById(commentId).orElseThrow()
                } catch (e: NoSuchElementException) {
                    throw NotFoundException("댓글을 찾을 수 없습니다.")
                }

            if (commentRepository.existsById(commentId)) {
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
        } else {
            throw UnauthenticatedException("세션이 만료되었습니다. 다시 로그인 해 주세요.")
        }
    }

    fun getCommentToObject(comment: Comment): JSONObject {
        val commentObject = comment.toJsonObject()
        commentObject.put("account", findAccount(comment.username!!).toJsonObject())
        return commentObject
    }

    fun findAccount(username: String): Account {
        val account = accountRepository.findById(username).orElseThrow()
        account.password = null
        return account
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