package kr.hs.dgsw.trust.server.controller

import javassist.NotFoundException
import kr.hs.dgsw.trust.server.data.entity.*
import kr.hs.dgsw.trust.server.data.response.JsonResponse
import kr.hs.dgsw.trust.server.exception.BadRequestException
import kr.hs.dgsw.trust.server.repository.AccountRepository
import kr.hs.dgsw.trust.server.repository.CommentRepository
import kr.hs.dgsw.trust.server.repository.LikedRepository
import kr.hs.dgsw.trust.server.repository.PostRepository
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.sql.Timestamp


@RestController
@RequestMapping("/board")
class BoardController(
    private val postRepository: PostRepository,
    private val commentRepository: CommentRepository,
    private val likedRepository: LikedRepository,
    private val accountRepository: AccountRepository,
    ) {

    @GetMapping("/post/{postId}")
    fun getPost(@PathVariable postId: Int?): Post {
        return try {
            postRepository.findById(postId!!).orElseThrow()
        } catch (e: NotFoundException) {
            throw NotFoundException("글을 찾을 수 없습니다.")
        }
    }

    @GetMapping("/post/list")
    fun getPostList(): ArrayList<HashMap<String, Any?>> {
        val list = postRepository.findAll()
        val jsonList = ArrayList<HashMap<String, Any?>>()

        list.forEach { post ->
            jsonList.add(getPostToHashMap(post))
        }

        return jsonList
    }

    @GetMapping("/post")
    fun savePost(username: String?, content: String?): HashMap<String, Any?> {
        val post = Post()
        return try {
            post.username = username!!
            post.createdAt = Timestamp(System.currentTimeMillis())
            post.content = content!!
            println(post)
            postRepository.save(post)
            JsonResponse().returnResponse(
                "200",
                "글을 성공적으로 추가하였습니다.",
                getPostToHashMap(post)
            )
        } catch (e: BadRequestException) {
            throw BadRequestException("오류가 발생했습니다.")
        }
    }

    @PutMapping("/post/{postId}/update")
    fun updatePost(@PathVariable postId: Int?, username: String?, password: String?, content: String?): HashMap<String, Any?> {
        return if (postRepository.existsById(postId!!)) {
            try {
                val post = postRepository.findById(postId).orElseThrow()
                post.content = content
                postRepository.deleteById(postId)
                JsonResponse().returnResponse(
                    "200",
                    "글을 성공적으로 삭제하였습니다.",
                    post
                )
            } catch (e: BadRequestException) {
                throw BadRequestException("오류가 발생했습니다.")
            }
        } else {
            throw NotFoundException("글을 찾을 수 없습니다.")
        }
    }

    @DeleteMapping("/post/{postId}/delete")
    fun deletePost(@PathVariable postId: Int?, username: String?, password: String?): HashMap<String, Any?> {
        return if (postRepository.existsById(postId!!)) {
            try {
                val post = postRepository.findById(postId).orElseThrow()
                postRepository.deleteById(postId)
                JsonResponse().returnResponse(
                    "200",
                    "글을 성공적으로 삭제하였습니다.",
                    post
                )
            } catch (e: BadRequestException) {
                throw BadRequestException("오류가 발생했습니다.")
            }
        } else {
            throw NotFoundException("글을 찾을 수 없습니다.")
        }
    }

    fun getPostToHashMap(post: Post): HashMap<String, Any?> {
        val postMap = post.toHashMap()
        postMap["account"] = findAccount(post.username!!).toHashMap()
        postMap.remove("username")
        return postMap
    }

    @GetMapping("/comment/{postId}/list")
    fun getCommentList(@PathVariable postId: Int): ArrayList<HashMap<String, Any?>> {
        val list = commentRepository.findByPostId(postId)
        val jsonList = ArrayList<HashMap<String, Any?>>()
        list.forEach { comment ->
            jsonList.add(getCommentToHashMap(comment))
        }

        return jsonList
    }

    @GetMapping("/comment/{postId}/save")
    fun saveComment(@PathVariable postId: Int, username: String?, content: String?): HashMap<String, Any?> {
        val comment = Comment()
        try {
            comment.postId = postId
            comment.username = username
            comment.createdAt = Timestamp(System.currentTimeMillis())
            comment.content = content
            commentRepository.save(comment)
        } catch (e: BadRequestException) {
            throw BadRequestException("오류가 발생했습니다.")
        }

        return JsonResponse().returnResponse(
            "200",
            "좋아요를 성공적으로 추가하였습니다.",
            getCommentToHashMap(comment)
        )
    }

    @PutMapping("/comment/{commentId}/update")
    fun updateComment(@PathVariable commentId: Int?, username: String?, password: String?, content: String?): HashMap<String, Any?> {
        return if (postRepository.existsById(commentId!!)) {
            try {
                val comment = postRepository.findById(commentId).orElseThrow()
                comment.content = content
                postRepository.deleteById(commentId)
                JsonResponse().returnResponse(
                    "200",
                    "글을 성공적으로 삭제하였습니다.",
                    comment
                )
            } catch (e: BadRequestException) {
                throw BadRequestException("오류가 발생했습니다.")
            }
        } else {
            throw NotFoundException("글을 찾을 수 없습니다.")
        }
    }

    @DeleteMapping("/comment/{commentId}/delete")
    fun deleteComment(@PathVariable commentId: Int?, username: String?, password: String?): HashMap<String, Any?> {
        return if (postRepository.existsById(commentId!!)) {
            try {
                val comment = postRepository.findById(commentId).orElseThrow()
                postRepository.deleteById(commentId)
                JsonResponse().returnResponse(
                    "200",
                    "글을 성공적으로 삭제하였습니다.",
                    comment
                )
            } catch (e: BadRequestException) {
                throw BadRequestException("오류가 발생했습니다.")
            }
        } else {
            throw NotFoundException("글을 찾을 수 없습니다.")
        }
    }

    fun getCommentToHashMap(comment: Comment): HashMap<String, Any?> {
        val postMap = comment.toHashMap()
        postMap["account"] = findAccount(comment.username!!).toHashMap()
        postMap.remove("username")
        return postMap
    }

    @GetMapping("/liked/{postId}")
    fun likedList(@PathVariable postId: Int): ArrayList<HashMap<String, Any?>> {
        val list = likedRepository.findByPostId(postId)
        val jsonList = ArrayList<HashMap<String, Any?>>()
        list.forEach { liked ->
            jsonList.add(getLikedToHashMap(liked))
        }

        return jsonList
    }

    @GetMapping("/liked")
    fun saveLiked(postId: Int, username: String?): HashMap<String, Any?> {
        val liked = Liked()
        try {
            liked.postId = postId
            liked.username = username!!
            liked.createdAt = Timestamp(System.currentTimeMillis())
            likedRepository.save(liked)
        } catch (e: BadRequestException) {
            throw BadRequestException("오류가 발생했습니다.")
        }

        return JsonResponse().returnResponse(
            "200",
            "좋아요를 성공적으로 추가하였습니다.",
            getLikedToHashMap(liked)
        )
    }

    fun getLikedToHashMap(liked: Liked): HashMap<String, Any?> {
        val postMap = liked.toHashMap()
        postMap["account"] = findAccount(liked.username!!).toHashMap()
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

            if (username == "UnknownAccount") {
                account.username = "Unknown"
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