package kr.hs.dgsw.trust.server.data.vo

import kr.hs.dgsw.trust.server.data.dto.ChatRoomDTO
import org.springframework.boot.configurationprocessor.json.JSONObject
import org.springframework.web.socket.WebSocketSession
import javax.persistence.*

@Entity(name = "chat_room")
class ChatRoomVO {
    @Id
    var roomId: String? = null
    @Column(name = "name_")
    var name: String? = null
    @Lob
    var session: Set<WebSocketSession>? = null
}

fun ChatRoomVO.toJsonObject(): JSONObject {
    val jsonObject = JSONObject()
    jsonObject.put("roomId", roomId)
    jsonObject.put("name", name)
    jsonObject.put("session", session)
    return jsonObject
}