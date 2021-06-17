package kr.hs.dgsw.trust.server.repository

import kr.hs.dgsw.trust.server.data.dto.PostVO
import org.springframework.data.jpa.repository.JpaRepository

interface PostRepository : JpaRepository<PostVO, Int?> {
    fun findByUsername(username: String) : List<PostVO>
}