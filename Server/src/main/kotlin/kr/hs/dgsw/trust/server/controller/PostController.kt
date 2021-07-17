package kr.hs.dgsw.trust.server.controller

import javassist.NotFoundException
import kr.hs.dgsw.trust.server.data.response.JsonResponse
import kr.hs.dgsw.trust.server.exception.UnauthenticatedException
import kr.hs.dgsw.trust.server.service.AccountService
import kr.hs.dgsw.trust.server.service.FileService
import kr.hs.dgsw.trust.server.service.PostService
import org.springframework.boot.configurationprocessor.json.JSONArray
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import kotlin.collections.ArrayList

@RestController
class PostController(
    private val postService: PostService,
    private val accountService: AccountService,
    private val fileService: FileService,
) {
    @GetMapping("/post")
    fun getPostList(@RequestHeader (name="Authorization") token: String): String {
        val jsonList = postService.findAll(token)
        return JsonResponse(
            "200",
            "글을 성공적으로 가져왔습니다.",
            jsonList
        ).returnJsonObject()
    }

    @GetMapping(path= ["/post"], params= ["username"])
    fun getUserPostList(@RequestHeader (name="Authorization") token: String, username: String): String {
        val jsonList = postService.findAllByUsername(token, username)
        return JsonResponse(
            "200",
            "글을 성공적으로 가져왔습니다.",
            jsonList
        ).returnJsonObject()
    }

    @GetMapping(path= ["/post"], params= ["postId"])
    fun getPost(@RequestHeader (name="Authorization") token: String, postId: Int): String {
        val post = postService.findPost(token, postId)
        return JsonResponse(
            "200",
            "글을 성공적으로 가져왔습니다.",
            post,
        ).returnJsonObject()
    }

    @PostMapping("/post/save")
    fun savePost(
        @RequestHeader (name="Authorization")
        token: String,
        content: String,
        isAnonymous: Boolean,
        imageList: ArrayList<MultipartFile>?
    ): String {

        val account = accountService.getAccount(token)
        val imagePathList = ArrayList<String>()
        println(imageList?.size)
        imageList?.forEach {
            val file = fileService.saveFile(it)
            imagePathList.add(file)
        }
        val post = postService.save(token, content, isAnonymous, imagePathList, account)

        return JsonResponse(
            "200",
            "글을 성공적으로 추가하였습니다.",
            post
        ).returnJsonObject()
    }

    @PutMapping("/post/update")
    fun updatePost(
        @RequestHeader (name="Authorization")
        token: String,
        postId: Int,
        content: String?,
        isAnonymous: Boolean,
        deleteFileList: Array<String>?,
        updateFileList: ArrayList<MultipartFile>?
    ): String {
        val pathList = fileService.updatePostFile(postId, deleteFileList, updateFileList)
        postService.update(token, postId, content, isAnonymous, pathList)

        val postList = postService.findAll(token)
        return JsonResponse(
            "200",
            "글을 성공적으로 업데이트 하였습니다.",
            postList,
        ).returnJsonObject()
    }

    @DeleteMapping("/post/delete")
    fun deletePost(@RequestHeader (name="Authorization") token: String, postId: Int): String {
        val post = postService.delete(token, postId)
        with(post) {
            val imageList = JSONArray(this)
            val imageArrayList = ArrayList<String>()
            var i = 0
            while(i < imageList.length()) {
                imageArrayList.add(imageList[i] as String)
                i++
            }
            imageArrayList.forEach {
                fileService.deleteFileByName(it)
            }
        }

        val postList = postService.findAll(token)
        return JsonResponse(
            "200",
            "글을 성공적으로 삭제하였습니다.",
            postList,
        ).returnJsonObject()
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
