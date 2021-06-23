package kr.hs.dgsw.trust.server.repository

import kr.hs.dgsw.trust.server.data.vo.CommentVO
import org.springframework.data.jpa.repository.JpaRepository

interface CommentRepository : JpaRepository<CommentVO, Int?> {
    fun findByPostId(postId: Int) : List<CommentVO>
    fun deleteAllByPostId(postId: Int)
}