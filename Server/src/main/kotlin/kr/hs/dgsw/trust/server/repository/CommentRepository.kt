package kr.hs.dgsw.trust.server.repository

import kr.hs.dgsw.trust.server.data.entity.Comment
import org.springframework.data.jpa.repository.JpaRepository

interface CommentRepository : JpaRepository<Comment, Int?> {
    fun findByPostId(postId: Int) : List<Comment>
    fun deleteAllByPostId(postId: Int)
}