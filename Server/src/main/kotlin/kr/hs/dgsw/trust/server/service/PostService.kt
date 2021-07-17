package kr.hs.dgsw.trust.server.service

import javassist.NotFoundException
import kr.hs.dgsw.trust.server.data.vo.*
import kr.hs.dgsw.trust.server.data.dto.AccountDTO
import kr.hs.dgsw.trust.server.data.dto.PostDTO
import kr.hs.dgsw.trust.server.data.dto.toJsonObject
import kr.hs.dgsw.trust.server.exception.UnauthenticatedException
import kr.hs.dgsw.trust.server.repository.AccountRepository
import kr.hs.dgsw.trust.server.repository.LikedRepository
import kr.hs.dgsw.trust.server.repository.PostRepository
import kr.hs.dgsw.trust.server.token.TokenProvider
import org.springframework.boot.configurationprocessor.json.JSONArray
import org.springframework.boot.configurationprocessor.json.JSONObject
import org.springframework.security.core.userdetails.User
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.sql.Timestamp

@Service
class PostService(
    private val postRepository: PostRepository,
    private val accountRepository: AccountRepository,
    private val likedRepository: LikedRepository,
    private val tokenProvider: TokenProvider,
    private val encoder: PasswordEncoder
) {
    fun findAll(token: String): JSONArray {
        if (token.isNotEmpty() && tokenProvider.validateToken(token)) {
            val list = postRepository.findAll()
            val jsonList = JSONArray()
            list.forEach { post ->
                jsonList.put(getPostToObject(post.toDTO(), token))
            }
            return jsonList
        } else {
            throw UnauthenticatedException("세션이 만료되었습니다. 다시 로그인 해주세요.")
        }
    }

    fun findAllByUsername(token: String, queryUsername: String): JSONArray {
        if (token.isNotEmpty() && tokenProvider.validateToken(token)) {
            val list = postRepository.findByUsername(queryUsername)
            val jsonList = JSONArray()
            list.forEach { post ->
                jsonList.put(getPostToObject(post.toDTO(), token))
            }
            return jsonList
        } else {
            throw UnauthenticatedException("세션이 만료되었습니다. 다시 로그인 해주세요.")
        }
    }

    fun findPost(token: String, postId: Int): JSONObject {
        if (token.isNotEmpty() && tokenProvider.validateToken(token)) {
            try {
                val post = postRepository.findById(postId).orElseThrow()
                return getPostToObject(post.toDTO(), token)
            } catch (e: Exception) {
                throw NotFoundException("글을 찾을 수 없습니다.")
            }
        } else {
            throw UnauthenticatedException("세션이 만료되었습니다. 다시 로그인 해주세요.")
        }
    }

    private fun getPostToObject(postDTO: PostDTO, token: String): JSONObject {
        val postObject = postDTO.toJsonObject()
        val username = (tokenProvider.getAuthentication(token).principal as User).username
        postObject.put("account", findAccount(postDTO.username, postDTO.isAnonymous).toJsonObject())


        postObject.put(
            "admin",
            if (postDTO.isAnonymous) {
                encoder.matches(username, postDTO.username)
            } else {
                username == postDTO.username
            }
        )

        val likedList = findLikedList(postDTO.id)
        var isChecked = false
        likedList.forEach {
            if (username == it.username) {
                isChecked = true
            }
        }
        postObject.put("isChecked", isChecked)
        postObject.put("likedSize", likedList.size)
        return postObject
    }

    private fun findLikedList(postId: Int): List<Liked> {
        return try {
            likedRepository.findAllByPostId(postId).orElseThrow()
        } catch (e: NoSuchElementException) {
            throw NotFoundException("글을 찾을 수 없습니다.")
        }
    }

    private fun findAccount(username: String, isAnonymous: Boolean): AccountDTO {
        try {
            val account = accountRepository.findById(username).orElseThrow()
            return account.toDTO()
        } catch (e: Exception) {
            if (isAnonymous) {
                return AccountDTO("익명", "ANONYMOUS", "defaultUserProfile.png")
            }
            throw UnauthenticatedException("오류가 발생했습니다.")
        }
    }

    fun save(
        token: String,
        content: String,
        isAnonymous: Boolean,
        imagePathList: ArrayList<String>?,
        account: AccountVO,
    ) : JSONObject {
        val post = PostVO()
        if (token.isNotEmpty() && tokenProvider.validateToken(token)) {
            post.username = if (!isAnonymous) {
                account.username
            } else {
                encoder.encode(account.username)
            }
            post.isAnonymous = isAnonymous
            post.createdAt = Timestamp(System.currentTimeMillis())
            post.content = content
            post.imageList = JSONArray(imagePathList).toString()

            return getPostToObject(postRepository.save(post).toDTO(), token)
        } else {
            throw UnauthenticatedException("세션이 만료되었습니다. 다시 로그인 해주세요.")
        }
    }


    fun update(
        token: String,
        postId: Int,
        content: String?,
        isAnonymous: Boolean,
        pathList: ArrayList<String>?
    ): JSONObject {

        val postVO = try {
            postRepository.findById(postId).orElseThrow()
        } catch (e: Exception) {
            throw NotFoundException("글을 찾을 수 없습니다.")
        }

        if (token.isNotEmpty() && tokenProvider.validateToken(token)) {
            val username = (tokenProvider.getAuthentication(token).principal as User).username
            try {
                val validUsername = if (postVO.isAnonymous == false) {
                    val account = findAccount(username, postVO.toDTO().isAnonymous)
                    account.username == postVO.username
                } else {
                    encoder.matches(username, postVO.username)
                }

                if (validUsername) {
                    postVO.content = if (!content.isNullOrEmpty()) content else postVO.content
                    postVO.imageList = JSONArray(pathList).toString()
                    if (isAnonymous && postVO.isAnonymous == false) {
                        postVO.username = encoder.encode(username)
                    } else if (!isAnonymous && postVO.isAnonymous == true) {
                        postVO.username = username
                    }
                    postVO.isAnonymous = isAnonymous


                    postRepository.save(postVO)
                    return getPostToObject(postVO.toDTO(), token)
                } else {
                    throw UnauthenticatedException("오류가 발생하였습니다.")
                }
            } catch (e: Exception) {
                throw UnauthenticatedException("오류가 발생했습니다.")
            }
        } else {
            throw UnauthenticatedException("세션이 만료되었습니다. 다시 로그인 해주세요.")
        }
    }

    fun delete(token: String, postId: Int): String? {
        if (token.isNotEmpty() && tokenProvider.validateToken(token)) {
            val username = (tokenProvider.getAuthentication(token).principal as User).username
            if (postRepository.existsById(postId)) {
                val post = postRepository.findById(postId).orElseThrow()
                val validUsername = if (post.isAnonymous == false) {
                    val account = accountRepository.findById(username).orElseThrow()
                    account.username == post.username
                } else {
                    encoder.matches(username, post.username)
                }

                if (validUsername) {
                    postRepository.delete(post)
                    return post.imageList
                } else {
                    throw UnauthenticatedException("오류가 발생하였습니다.")
                }
            } else {
                throw NotFoundException("글을 찾을 수 없습니다.")
            }

        } else {
            throw UnauthenticatedException("세션이 만료되었습니다. 다시 로그인 해주세요.")
        }
    }
}