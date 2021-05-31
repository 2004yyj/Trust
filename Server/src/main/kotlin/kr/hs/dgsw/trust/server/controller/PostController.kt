package kr.hs.dgsw.trust.server.controller

import javassist.NotFoundException
import kr.hs.dgsw.trust.server.data.entity.*
import kr.hs.dgsw.trust.server.data.response.JsonResponse
import kr.hs.dgsw.trust.server.exception.BadRequestException
import kr.hs.dgsw.trust.server.exception.UnauthenticatedException
import kr.hs.dgsw.trust.server.repository.AccountRepository
import kr.hs.dgsw.trust.server.repository.CommentRepository
import kr.hs.dgsw.trust.server.repository.LikedRepository
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
class PostController(
    private val postRepository: PostRepository,
    private val commentRepository: CommentRepository,
    private val likedRepository: LikedRepository,
    private val accountRepository: AccountRepository,
    private val passwordEncoder : PasswordEncoder
) {

    @Autowired
    private lateinit var fileService: FileService

    @GetMapping("/post")
    fun getPostList(): String {
        val list = postRepository.findAll()
        val jsonList = JSONArray()

        list.forEach { post ->
            jsonList.put(getPostToObject(post))
        }

        return JsonResponse(
            "200",
            "글을 성공적으로 가져왔습니다.",
            jsonList
        ).returnJsonObject()
    }

    @GetMapping(path= ["/post"], params= ["username"])
    fun getUserPostList(username: String): String {
        val list = postRepository.findByUsername(username)
        val jsonList = JSONArray()

        list.forEach { post ->
            jsonList.put(getPostToObject(post))
        }

        return JsonResponse(
            "200",
            "글을 성공적으로 가져왔습니다.",
            jsonList
        ).returnJsonObject()
    }

    @GetMapping(path= ["/post"], params= ["postId"])
    fun getPost(postId: Int): String {
        return try {
            val post = postRepository.findById(postId).orElseThrow()

            JsonResponse(
                "200",
                "글을 성공적으로 삭제 하였습니다.",
                getPostToObject(post)
            ).returnJsonObject()

        } catch (e: NotFoundException) {
            throw NotFoundException("글을 찾을 수 없습니다.")
        }
    }

    @PostMapping("/post/save")
    fun savePost(
        username: String,
        password: String,
        content: String,
        isAnonymous: Boolean,
        imageList: ArrayList<MultipartFile>?
    ): String {
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
                    pathList.add(file)
                }
                post.imageList = JSONArray(pathList).toString()
                postRepository.save(post)
            } else {
                throw UnauthenticatedException("계정을 찾을 수 없습니다.")
            }
        } catch (e: BadRequestException) {
            throw BadRequestException("오류가 발생했습니다.")
        }

        return JsonResponse(
            "200",
            "글을 성공적으로 추가하였습니다.",
            getPostToObject(post)
        ).returnJsonObject()
    }

    @PutMapping("/post/update")
    fun updatePost(
        postId: Int,
        username: String,
        password: String,
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
        val account =
            try {
                accountRepository.findById(username).orElseThrow()
            } catch (e: NoSuchElementException) {
                throw UnauthenticatedException("계정을 찾을 수 없습니다.")
            }

        return if (postRepository.existsById(postId)) {

            val accountMatch =
                if (post.isAnonymous == true)
                    passwordEncoder.matches(username, post.username)
                else
                    username == post.username

            if (accountMatch && passwordEncoder.matches(password, account.password!!)) {
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
                    getPostToObject(post)
                ).returnJsonObject()
            } else {
                throw UnauthenticatedException("계정을 찾을 수 없습니다.")
            }
        } else {
            throw NotFoundException("글을 찾을 수 없습니다.")
        }
    }

    @DeleteMapping("/post/delete")
    fun deletePost(postId: Int, username: String, password: String): String {
        val post =
            try {
                postRepository.findById(postId).orElseThrow()
            } catch (e: NoSuchElementException) {
                throw NotFoundException("글을 찾을 수 없습니다.")
            }
        val account =
            try {
                accountRepository.findById(username).orElseThrow()
            } catch (e: NoSuchElementException) {
                throw UnauthenticatedException("계정을 찾을 수 없습니다.")
            }

        return if (postRepository.existsById(postId)) {
            val accountMatch =
                if (post.isAnonymous == true)
                    passwordEncoder.matches(username, post.username)
                else
                    username == post.username

            if (accountMatch && passwordEncoder.matches(password, account.password!!)) {
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
                    getPostToObject(post)
                ).returnJsonObject()

            } else {
                throw UnauthenticatedException("계정을 찾을 수 없습니다.")
            }
        } else {
            throw NotFoundException("글을 찾을 수 없습니다.")
        }
    }

    fun getPostToObject(post: Post): JSONObject {
        val postObject = post.toJsonObject()
        postObject.put("account", findAccount(post.username!!, post.isAnonymous!!).toJsonObject())
        return postObject
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
