package kr.hs.dgsw.trust.server.repository

import kr.hs.dgsw.trust.server.data.entity.Comment
import kr.hs.dgsw.trust.server.data.entity.Liked
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface LikedRepository : JpaRepository<Liked, Int?> {
    fun findAllByPostId(postId: Int) : List<Liked>
    fun deleteAllByPostId(postId: Int)
    fun existsByUsernameAndPostId(username: String, postId: Int) : Boolean
}