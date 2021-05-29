package kr.hs.dgsw.trust.server.controller

import javassist.NotFoundException
import kr.hs.dgsw.trust.server.data.entity.Account
import kr.hs.dgsw.trust.server.data.entity.Comment
import kr.hs.dgsw.trust.server.data.entity.toHashMap
import kr.hs.dgsw.trust.server.data.response.JsonResponse
import kr.hs.dgsw.trust.server.exception.BadRequestException
import kr.hs.dgsw.trust.server.exception.UnauthenticatedException
import kr.hs.dgsw.trust.server.repository.AccountRepository
import kr.hs.dgsw.trust.server.repository.CommentRepository
import kr.hs.dgsw.trust.server.service.FileService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.sql.Timestamp

@RestController
class CommentController(
    private val commentRepository: CommentRepository,
    private val accountRepository: AccountRepository,
    private val passwordEncoder: PasswordEncoder
) {

    @Autowired
    private lateinit var fileService: FileService

    @GetMapping("/comment/{postId}")
    fun getCommentList(@PathVariable postId: Int): ArrayList<HashMap<String, Any?>> {
        val list = commentRepository.findByPostId(postId)
        val jsonList = ArrayList<HashMap<String, Any?>>()
        list.forEach { comment ->
            jsonList.add(getCommentToHashMap(comment))
        }

        return jsonList
    }

    @PostMapping("/comment/{postId}/save")
    fun saveComment(
        @PathVariable postId: Int,
        username: String,
        password: String,
        content: String,
        isAnonymous: Boolean,
        imageList: List<MultipartFile>?
    ): HashMap<String, Any?> {
        val comment = Comment()
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
                    val filePath = "/image/$file"
                    pathList.add(filePath)
                }
                comment.imageList = pathList.toString()
                commentRepository.save(comment)
            } else {
                throw UnauthenticatedException("계정을 찾을 수 없습니다.")
            }
        } catch (e: BadRequestException) {
            throw BadRequestException("오류가 발생했습니다.")
        }

        return JsonResponse().returnResponse(
            "200",
            "댓글을 성공적으로 추가하였습니다.",
            getCommentToHashMap(comment)
        )
    }

    @PutMapping("/comment/{commentId}/update")
    fun updateComment(@PathVariable commentId: Int, username: String, password: String, content: String): HashMap<String, Any?> {
        return if (commentRepository.existsById(commentId)) {
            try {
                val comment = commentRepository.findById(commentId).orElseThrow()
                val account = accountRepository.findById(username).orElseThrow()

                val accountMatch =
                    if (comment.isAnonymous == true)
                        passwordEncoder.matches(username, comment.username)
                    else
                        username == comment.username

                if (accountMatch && passwordEncoder.matches(password, account.password!!)) {
                    comment.content = content
                    commentRepository.save(comment)

                    JsonResponse().returnResponse(
                        "200",
                        "댓글을 성공적으로 업데이트 하였습니다.",
                        getCommentToHashMap(comment)
                    )
                } else {
                    throw UnauthenticatedException("계정을 찾을 수 없습니다.")
                }
            } catch (e: BadRequestException) {
                throw BadRequestException("오류가 발생했습니다.")
            }
        } else {
            throw NotFoundException("댓글을 찾을 수 없습니다.")
        }
    }

    @DeleteMapping("/comment/{commentId}/delete")
    fun deleteComment(@PathVariable commentId: Int, username: String, password: String): HashMap<String, Any?> {
        return if (commentRepository.existsById(commentId)) {
            try {
                val comment = commentRepository.findById(commentId).orElseThrow()
                val account = accountRepository.findById(username).orElseThrow()

                val accountMatch =
                    if (comment.isAnonymous == true)
                        passwordEncoder.matches(username, comment.username)
                    else
                        username == comment.username

                if (accountMatch && passwordEncoder.matches(password, account.password!!)) {
                    commentRepository.deleteById(commentId)
                    JsonResponse().returnResponse(
                        "200",
                        "댓글을 성공적으로 삭제하였습니다.",
                        getCommentToHashMap(comment)
                    )
                } else {
                    throw UnauthenticatedException("계정을 찾을 수 없습니다.")
                }

            } catch (e: BadRequestException) {
                throw BadRequestException("오류가 발생했습니다.")
            }
        } else {
            throw NotFoundException("댓글을 찾을 수 없습니다.")
        }
    }

    fun getCommentToHashMap(comment: Comment): HashMap<String, Any?> {
        val postMap = comment.toHashMap()
        postMap["account"] = findAccount(comment.username!!, comment.isAnonymous!!).toHashMap()
        postMap.remove("username")
        return postMap
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