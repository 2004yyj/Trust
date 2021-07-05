package kr.hs.dgsw.trust.server.service

import javassist.NotFoundException
import kr.hs.dgsw.trust.server.data.vo.*
import kr.hs.dgsw.trust.server.data.dto.AccountDTO
import kr.hs.dgsw.trust.server.data.dto.PostDTO
import kr.hs.dgsw.trust.server.data.dto.toJsonObject
import kr.hs.dgsw.trust.server.data.response.JsonResponse
import kr.hs.dgsw.trust.server.exception.BadRequestException
import kr.hs.dgsw.trust.server.exception.UnauthenticatedException
import kr.hs.dgsw.trust.server.repository.AccountRepository
import kr.hs.dgsw.trust.server.repository.CommentRepository
import kr.hs.dgsw.trust.server.repository.LikedRepository
import kr.hs.dgsw.trust.server.repository.PostRepository
import kr.hs.dgsw.trust.server.token.TokenProvider
import org.springframework.boot.configurationprocessor.json.JSONArray
import org.springframework.boot.configurationprocessor.json.JSONObject
import org.springframework.security.core.userdetails.User
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.sql.Timestamp
import javax.servlet.http.HttpServletRequest

@Service
class CommentService(
    private val commentRepository: CommentRepository,
    private val postRepository: PostRepository,
    private val tokenProvider: TokenProvider,
) {
    fun findAllComment(
        token: String,
        postId: Int
    ): List<CommentVO> {
        if (token.isNotEmpty() && tokenProvider.validateToken(token)) {
            return commentRepository.findByPostId(postId)
        } else {
            throw UnauthenticatedException("세션이 만료되었습니다. 다시 로그인 해 주세요.")
        }
    }

    fun saveComment(
        token: String,
        postId: Int,
        content: String,
        imageList: ArrayList<String>?,
    ): CommentVO {
        val comment = CommentVO()
        if (token.isNotEmpty() && tokenProvider.validateToken(token)) {
            if (postRepository.existsById(postId)) {
                try {
                    val username = (tokenProvider.getAuthentication(token).principal as User).username

                    comment.postId = postId
                    comment.username = username
                    comment.createdAt = Timestamp(System.currentTimeMillis())
                    comment.content = content
                    comment.imageList = JSONArray(imageList).toString()

                    return commentRepository.save(comment)
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

    fun updateComment(
        token: String,
        commentId: Int,
        content: String,
        imageList: ArrayList<String>?
    ): List<CommentVO> {
        if (token.isNotEmpty() && tokenProvider.validateToken(token)) {
            val comment =
                try {
                    commentRepository.findById(commentId).orElseThrow()
                } catch (e: NoSuchElementException) {
                    throw NotFoundException("댓글을 찾을 수 없습니다.")
                }

            if (commentRepository.existsById(commentId)) {
                comment.content = content
                comment.imageList = JSONArray(imageList).toString()
                commentRepository.save(comment)
            } else {
                throw UnauthenticatedException("세션이 만료되었습니다. 다시 로그인 해 주세요.")
            }
            return commentRepository.findByPostId(comment.postId!!)
        } else {
            throw UnauthenticatedException("세션이 만료되었습니다. 다시 로그인 해 주세요.")
        }
    }

    fun deleteComment(
        token: String,
        commentId: Int
    ): CommentVO {
        if (token.isNotEmpty() && tokenProvider.validateToken(token)) {
            val comment =
                try {
                    commentRepository.findById(commentId).orElseThrow()
                } catch (e: NoSuchElementException) {
                    throw NotFoundException("댓글을 찾을 수 없습니다.")
                }

            if (commentRepository.existsById(commentId)) {
                commentRepository.deleteById(commentId)
            } else {
                throw NotFoundException("댓글을 찾을 수 없습니다.")
            }

            return comment
        } else {
            throw UnauthenticatedException("세션이 만료되었습니다. 다시 로그인 해 주세요.")
        }
    }
}