package kr.hs.dgsw.trust.server.handler

import lombok.extern.log4j.Log4j2
import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler

@Component
@Log4j2
class ChatHandler : TextWebSocketHandler() {

    companion object {
        val list = ArrayList<WebSocketSession>()
    }

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        val payload = message.payload
        println(payload)

        for (sess in list) {
            session.sendMessage(message)
        }
    }

    override fun afterConnectionEstablished(session: WebSocketSession) {
        try {
            list.add(session)
            println("${session.principal?.name} 접속")
        } catch (e: Exception) {
            println(e.message)
        }
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        try {
            list.remove(session)
            println("${session.principal?.name} 접속 해제")
        } catch (e: Exception) {
            println(e.message)
        }
    }
}