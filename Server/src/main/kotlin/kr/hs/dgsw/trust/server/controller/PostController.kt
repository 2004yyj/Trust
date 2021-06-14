package kr.hs.dgsw.trust.server.controller

import javassist.NotFoundException
import kr.hs.dgsw.trust.server.data.entity.*
import kr.hs.dgsw.trust.server.data.response.JsonResponse
import kr.hs.dgsw.trust.server.exception.BadRequestException
import kr.hs.dgsw.trust.server.exception.UnauthenticatedException
import kr.hs.dgsw.trust.server.repository.CommentRepository
import kr.hs.dgsw.trust.server.repository.LikedRepository
import kr.hs.dgsw.trust.server.repository.PostRepository
import kr.hs.dgsw.trust.server.service.AccountService
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
import kotlin.NoSuchElementException
import kotlin.collections.ArrayList

@RestController
class PostController(
    private val postRepository: PostRepository,
    private val commentRepository: CommentRepository,
    private val likedRepository: LikedRepository,
    private val accountService: AccountService,
    private val tokenProvider: TokenProvider,
    private val encoder: PasswordEncoder
) {

    @Autowired
    private lateinit var fileService: FileService

    @GetMapping("/post")
    fun getPostList(@RequestHeader (name="Authorization") token: String): String {

        if (token.isNotEmpty() && tokenProvider.validateToken(token)) {

            val username = (tokenProvider.getAuthentication(token).principal as User).username
            val account = accountService.getAccount(username)

            val list = postRepository.findAll()
            val jsonList = JSONArray()

            list.forEach { post ->
                jsonList.put(getPostToObject(post, account))
            }

            return JsonResponse(
                "200",
                "글을 성공적으로 가져왔습니다.",
                jsonList
            ).returnJsonObject()
        } else {
            throw UnauthenticatedException("세션이 만료되었습니다. 다시 로그인 해주세요.")
        }
    }

    @GetMapping(path= ["/post"], params= ["username"])
    fun getUserPostList(@RequestHeader (name="Authorization") token: String, username: String): String {
        if (token.isNotEmpty() && tokenProvider.validateToken(token)) {

            val usernameAuth = (tokenProvider.getAuthentication(token).principal as User).username
            val account = accountService.getAccount(usernameAuth)

            val list = postRepository.findByUsername(username)
            val jsonList = JSONArray()

            list.forEach { post ->
                jsonList.put(getPostToObject(post, account))
            }

            return JsonResponse(
                "200",
                "글을 성공적으로 가져왔습니다.",
                jsonList
            ).returnJsonObject()
        } else {
            throw UnauthenticatedException("세션이 만료되었습니다. 다시 로그인 해주세요.")
        }
    }

    @GetMapping(path= ["/post"], params= ["postId"])
    fun getPost(@RequestHeader (name="Authorization") token: String, postId: Int): String {
        return try {

            val username = (tokenProvider.getAuthentication(token).principal as User).username
            val account = accountService.getAccount(username)

            val post = postRepository.findById(postId).orElseThrow()

            JsonResponse(
                "200",
                "글을 성공적으로 가져왔습니다.",
                getPostToObject(post, account)
            ).returnJsonObject()

        } catch (e: NotFoundException) {
            throw NotFoundException("글을 찾을 수 없습니다.")
        }
    }

    @PostMapping("/post/save")
    fun savePost(
        @RequestHeader (name="Authorization")
        token: String,
        content: String,
        isAnonymous: Boolean,
        imageList: ArrayList<MultipartFile>?
    ): String {
        val post = Post()

        if (token.isNotEmpty() && tokenProvider.validateToken(token)) {
            val username = (tokenProvider.getAuthentication(token).principal as User).username
            val account = accountService.getAccount(username)
            try {
                val imagePathList = ArrayList<String>()
                post.username = if (!isAnonymous) {
                    username
                } else {
                    encoder.encode(username)
                }
                post.isAnonymous = isAnonymous
                post.createdAt = Timestamp(System.currentTimeMillis())
                post.content = content
                imageList?.forEach {
                    val file = fileService.saveFile(it)
                    imagePathList.add(file)
                }
                post.imageList = JSONArray(imagePathList).toString()
                postRepository.save(post)
            } catch (e: BadRequestException) {
                throw BadRequestException("오류가 발생했습니다.")
            }

            return JsonResponse(
                "200",
                "글을 성공적으로 추가하였습니다.",
                getPostToObject(post, account)
            ).returnJsonObject()
        } else {
            throw UnauthenticatedException("세션이 만료되었습니다. 다시 로그인 해주세요.")
        }
    }

    @PutMapping("/post/update")
    fun updatePost(
        @RequestHeader (name="Authorization")
        token: String,
        postId: Int,
        content: String?,
        deleteFileList: Array<String>?,
        updateFileList: ArrayList<MultipartFile>?
    ): String {
        val post =
            try {
                postRepository.findById(postId).orElseThrow()
            } catch (e: NoSuchElementException) {
                throw NotFoundException("글을 찾을 수 없습니다.")
            }
        val account = if (token.isNotEmpty() && tokenProvider.validateToken(token)) {
            val username = (tokenProvider.getAuthentication(token).principal as User).username
            accountService.getAccount(username)
        } else {
            throw UnauthenticatedException("세션이 만료되었습니다. 다시 로그인 해주세요.")
        }

        return if (postRepository.existsById(postId)) {

            val validUsername = if (post.isAnonymous == false) {

                account.username == post.username
            } else {
                encoder.matches(account.password, post.username)
            }

            if (validUsername) {
                post.content = if (!content.isNullOrEmpty()) content else post.content

                val imageJsonArray = JSONArray(post.imageList)

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

                post.imageList = JSONArray(pathList).toString()

                postRepository.save(post)

                JsonResponse(
                    "200",
                    "글을 성공적으로 업데이트 하였습니다.",
                    getPostToObject(post, account)
                ).returnJsonObject()
            } else {
                throw UnauthenticatedException("오류가 발생하였습니다.")
            }
        } else {
            throw NotFoundException("글을 찾을 수 없습니다.")
        }
    }

    @DeleteMapping("/post/delete")
    fun deletePost(@RequestHeader (name="Authorization") token: String, postId: Int): String {
        val post =
            try {
                postRepository.findById(postId).orElseThrow()
            } catch (e: NoSuchElementException) {
                throw NotFoundException("글을 찾을 수 없습니다.")
            }
        val account =
            try {
                val username = (tokenProvider.getAuthentication(token).principal as User).username
                accountService.getAccount(username)
            } catch (e: NoSuchElementException) {
                throw UnauthenticatedException("세션이 만료되었습니다. 다시 로그인 해주세요.")
            }

        return if (postRepository.existsById(postId)) {
            val validUsername = if (post.isAnonymous == false) {
                account.username == post.username
            } else {
                encoder.matches(account.password, post.username)
            }

            if (validUsername) {

                postRepository.deleteById(postId)
                likedRepository.deleteAllByPostId(postId)
                commentRepository.deleteAllByPostId(postId)

                val imageJsonArray = JSONArray(post.imageList)

                val pathList = ArrayList<String>()

                var i = 0
                while (i < imageJsonArray.length()) {
                    pathList.add(imageJsonArray[i] as String)
                    i++
                }

                pathList.forEach {
                    fileService.deleteFileByName(it)
                }

                post.imageList = JSONArray(pathList).toString()

                postRepository.delete(post)

                JsonResponse(
                    "200",
                    "글을 성공적으로 삭제 하였습니다.",
                    null
                ).returnJsonObject()

            } else {
                throw UnauthenticatedException("계정을 찾을 수 없습니다.")
            }
        } else {
            throw NotFoundException("글을 찾을 수 없습니다.")
        }
    }

    fun getPostToObject(post: Post, account: Account): JSONObject {
        val postObject = post.toJsonObject()
        postObject.put("account", findAccount(post.username!!, post.isAnonymous!!).toJsonObject())

        val likedList = findLikedList(post.id!!)
        var isChecked = false

        likedList.forEach {
            if (account.username == it.username) {
                isChecked = true
            }
        }
        postObject.put("isChecked", isChecked)
        postObject.put("likedSize", likedList.size)

        return postObject
    }

    fun findLikedList(postId: Int): List<Liked> {
        return try {
            likedRepository.findAllByPostId(postId).orElseThrow()
        } catch (e: NoSuchElementException) {
            throw NotFoundException("글을 찾을 수 없습니다.")
        }
    }

    fun findAccount(username: String, isAnonymous: Boolean): Account {
        return try {
            val account = accountService.getAccount(username)
            account.password = null
            account
        } catch (e: NoSuchElementException) {
            val account = Account()

            if (isAnonymous) {
                account.username = "anonymous"
                account.name = "익명"
            } else {
                account.username = "DELETED"
                account.name = "삭제된 계정"
            }
            account.profileImage = "defaultUserProfile.png"
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
