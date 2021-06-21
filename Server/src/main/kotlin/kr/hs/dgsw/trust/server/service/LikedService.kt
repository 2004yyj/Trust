package kr.hs.dgsw.trust.server.service

import kr.hs.dgsw.trust.server.repository.LikedRepository
import org.springframework.stereotype.Service

@Service
class LikedService(
    private val likedRepository: LikedRepository
) {
}