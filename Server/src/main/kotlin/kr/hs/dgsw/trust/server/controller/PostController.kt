package kr.hs.dgsw.trust.server.controller

import javassist.NotFoundException
import kr.hs.dgsw.trust.server.data.entity.*
import kr.hs.dgsw.trust.server.data.response.JsonResponse
import kr.hs.dgsw.trust.server.exception.BadRequestException
import kr.hs.dgsw.trust.server.exception.UnauthenticatedException
import kr.hs.dgsw.trust.server.repository.AccountRepository
import kr.hs.dgsw.trust.server.repository.PostRepository
import kr.hs.dgsw.trust.server.service.FileService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.sql.Timestamp


@RestController
class PostController(
    private val postRepository: PostRepository,
    private val accountRepository: AccountRepository,
    private val passwordEncoder : PasswordEncoder
) {

    @Autowired
    private lateinit var fileService: FileService

    @GetMapping("/post")
    fun getPostList(): ArrayList<HashMap<String, Any?>> {
        val list = postRepository.findAll()
        val jsonList = ArrayList<HashMap<String, Any?>>()

        list.forEach { post ->
            jsonList.add(getPostToHashMap(post))
        }

        return jsonList
    }

    @GetMapping("/post/{username}")
    fun getUserPostList(@PathVariable username: String): ArrayList<HashMap<String, Any?>> {
        val list = postRepository.findByUsername(username)
        val jsonList = ArrayList<HashMap<String, Any?>>()

        list.forEach { post ->
            jsonList.add(getPostToHashMap(post))
        }

        return jsonList
    }

    @PostMapping("/post/save")
    fun savePost(
        username: String,
        password: String,
        content: String,
        isAnonymous: Boolean,
        imageList: List<MultipartFile>?
    ): HashMap<String, Any?> {
        val post = Post()
        try {
            val account = accountRepository.findById(username).orElseThrow()
            val pathList = ArrayList<String>()

            if (username == account.username && passwordEncoder.matches(password, account.password)) {
                post.username =
                    if (!isAnonymous) {
                        username
                    } else {
                        passwordEncoder.encode(username)
                    }

                post.isAnonymous = isAnonymous
                post.createdAt = Timestamp(System.currentTimeMillis())
                post.content = content
                imageList?.forEach {
                    val file = fileService.saveFile(it)
                    val filePath = "/image/$file"
                    pathList.add(filePath)
                }
                post.imageList = pathList.toString()
                postRepository.save(post)
            } else {
                throw UnauthenticatedException("계정을 찾을 수 없습니다.")
            }
        } catch (e: BadRequestException) {
            throw BadRequestException("오류가 발생했습니다.")
        }

        return JsonResponse().returnResponse(
            "200",
            "글을 성공적으로 추가하였습니다.",
            getPostToHashMap(post)
        )
    }

    @PutMapping("/post/{postId}/update")
    fun updatePost(@PathVariable postId: Int, username: String, password: String, content: String?): HashMap<String, Any?> {
        return if (postRepository.existsById(postId)) {
            try {
                val post = postRepository.findById(postId).orElseThrow()
                val account = accountRepository.findById(username).orElseThrow()

                val accountMatch =
                    if (post.isAnonymous == true)
                        passwordEncoder.matches(username, post.username)
                    else
                        username == post.username

                if (accountMatch && passwordEncoder.matches(password, account.password!!)) {
                    post.content = if (!content.isNullOrEmpty()) content else post.content
                    postRepository.save(post)

                    JsonResponse().returnResponse(
                        "200",
                        "글을 성공적으로 업데이트 하였습니다.",
                        getPostToHashMap(post)
                    )
                } else {
                    throw UnauthenticatedException("계정을 찾을 수 없습니다.")
                }
            } catch (e: BadRequestException) {
                throw BadRequestException("오류가 발생했습니다.")
            }
        } else {
            throw NotFoundException("글을 찾을 수 없습니다.")
        }
    }

    @DeleteMapping("/post/{postId}/delete")
    fun deletePost(@PathVariable postId: Int, username: String, password: String): HashMap<String, Any?> {
        return if (postRepository.existsById(postId)) {
            try {
                val post = postRepository.findById(postId).orElseThrow()
                val account = accountRepository.findById(username).orElseThrow()

                val accountMatch =
                    if (post.isAnonymous == true)
                        passwordEncoder.matches(username, post.username)
                    else
                        username == post.username

                if (accountMatch && passwordEncoder.matches(password, account.password!!)) {
                    postRepository.deleteById(postId)
                    JsonResponse().returnResponse(
                        "200",
                        "글을 성공적으로 삭제하였습니다.",
                        getPostToHashMap(post)
                    )
                } else {
                    throw UnauthenticatedException("계정을 찾을 수 없습니다.")
                }
            } catch (e: BadRequestException) {
                throw BadRequestException("오류가 발생했습니다.")
            }
        } else {
            throw NotFoundException("글을 찾을 수 없습니다.")
        }
    }

    @GetMapping("/post/{postId}")
    fun getPost(@PathVariable postId: Int): Post {
        return try {
            postRepository.findById(postId).orElseThrow()
        } catch (e: NotFoundException) {
            throw NotFoundException("글을 찾을 수 없습니다.")
        }
    }

    fun getPostToHashMap(post: Post): HashMap<String, Any?> {
        val postMap = post.toHashMap()
        postMap["account"] = findAccount(post.username!!, post.isAnonymous!!).toHashMap()
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
