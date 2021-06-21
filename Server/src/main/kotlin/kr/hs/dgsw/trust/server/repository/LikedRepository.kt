package kr.hs.dgsw.trust.server.repository

import kr.hs.dgsw.trust.server.data.dto.Liked
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*
import javax.transaction.Transactional

interface LikedRepository : JpaRepository<Liked, Int?> {
    fun findByPostIdAndUsername(postId: Int, username: String) : Optional<Liked>
    fun findAllByPostId(postId: Int) : Optional<List<Liked>>
    fun deleteAllByPostId(postId: Int)

    @Transactional
    fun deleteByPostIdAndUsername(postId: Int, username: String)
}