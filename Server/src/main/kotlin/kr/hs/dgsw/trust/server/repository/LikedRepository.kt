package kr.hs.dgsw.trust.server.repository

import kr.hs.dgsw.trust.server.data.entity.Comment
import kr.hs.dgsw.trust.server.data.entity.Liked
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface LikedRepository : JpaRepository<Liked, Int?> {
    fun findByPostId(postId: Int) : List<Liked>
}