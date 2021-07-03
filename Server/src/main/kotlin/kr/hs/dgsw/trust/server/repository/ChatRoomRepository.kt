package kr.hs.dgsw.trust.server.repository

import kr.hs.dgsw.trust.server.data.vo.ChatRoomVO
import org.springframework.data.jpa.repository.JpaRepository

interface ChatRoomRepository: JpaRepository<ChatRoomVO, String>