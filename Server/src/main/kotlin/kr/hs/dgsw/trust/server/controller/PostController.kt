package kr.hs.dgsw.trust.server.controller

import com.google.gson.Gson
import kr.hs.dgsw.trust.server.data.entity.*
import kr.hs.dgsw.trust.server.repository.AccountRepository
import kr.hs.dgsw.trust.server.repository.CommentRepository
import kr.hs.dgsw.trust.server.repository.LikedRepository
import kr.hs.dgsw.trust.server.repository.PostRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/post")
class PostController(
    private val postRepository: PostRepository,
    private val commentRepository: CommentRepository,
    private val likedRepository: LikedRepository,
    private val accountRepository: AccountRepository
    ) {

    fun toJson(data: Any): String? {
        val gson = Gson()
        return gson.toJson(data)
    }

    @GetMapping
    fun getPost(): String? {
        val list = postRepository.findAll()
        val jsonList = ArrayList<HashMap<String, Any?>>()

        list.forEach { post ->
            jsonList.add(getPostToHashMap(post))
        }

        return toJson(jsonList)
    }

    fun getPostToHashMap(post: Post): HashMap<String, Any?> {
        val postMap = post.toHashMap()
        postMap["account"] = findAccount(post.username!!)
        postMap.remove("username")
        return postMap
    }

    @GetMapping("/comment/{postId}")
    fun getComment(@PathVariable postId: Int): String? {
        val list = commentRepository.findByPostId(postId)
        val jsonList = ArrayList<HashMap<String, Any?>>()
        list.forEach { comment ->
            jsonList.add(getCommentToHashMap(comment))
        }

        return toJson(jsonList)
    }

    fun getCommentToHashMap(comment: Comment): HashMap<String, Any?> {
        val postMap = comment.toHashMap()
        postMap["account"] = findAccount(comment.username!!)
        postMap.remove("username")
        return postMap
    }

    @GetMapping("/liked/{postId}")
    fun getLiked(@PathVariable postId: Int): String? {
        val list = likedRepository.findByPostId(postId)
        val jsonList = ArrayList<HashMap<String, Any?>>()
        list.forEach { liked ->
            jsonList.add(getLikedToHashMap(liked))
        }

        return toJson(jsonList)
    }

    fun getLikedToHashMap(liked: Liked): HashMap<String, Any?> {
        val postMap = liked.toHashMap()
        postMap["account"] = findAccount(liked.username!!)
        postMap.remove("username")
        return postMap
    }

    fun findAccount(username: String): Account {
        return try {
            val account = accountRepository.findById(username).orElseThrow()
            account.password = null
            account
        } catch (e: NoSuchElementException) {
            val account = Account()
            account.username = "DELETED"
            account.name = "삭제된 계정"
            account.profileImage = "/image/defaultUserProfile.png"
            account
        }
    }
}
