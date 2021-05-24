package kr.hs.dgsw.trust.server.repository

import kr.hs.dgsw.trust.server.data.entity.Post
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface PostRepository : JpaRepository<Post, Int?> {
    fun findByUsername(username: String) : List<Post>
}