package kr.hs.dgsw.trust.server.data.dto

import kr.hs.dgsw.trust.server.data.vo.ChatRoomVO
import org.springframework.web.socket.WebSocketSession
import java.util.*
import kotlin.collections.HashSet

class ChatRoomDTO (
    private val name: String
) {
    private val roomId = UUID.randomUUID().toString()
    private val session = HashSet<WebSocketSession>()

    fun toVO(): ChatRoomVO {
        val chatRoomVO = ChatRoomVO()
        chatRoomVO.roomId = this.roomId
        chatRoomVO.session = this.session
        chatRoomVO.name = this.name
        return toVO()
    }
}