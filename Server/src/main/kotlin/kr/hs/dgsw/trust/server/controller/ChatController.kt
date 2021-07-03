package kr.hs.dgsw.trust.server.controller

import kr.hs.dgsw.trust.server.data.dto.ChatMessageDTO
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.web.bind.annotation.RestController

@RestController
class ChatController(
    private val template: SimpMessagingTemplate
) {
    @MessageMapping("/chat/enter")
    fun enter(chatMessageDTO: ChatMessageDTO) {
        println(chatMessageDTO.writer)
        chatMessageDTO.message = "${chatMessageDTO.writer} 님이 입장하셨습니다."
        template.convertAndSend("/sub/chat/room/${chatMessageDTO.roomId}", chatMessageDTO)
    }

    @MessageMapping("/chat/message")
    fun message(chatMessageDTO: ChatMessageDTO) {
        template.convertAndSend("/sub/chat/room/${chatMessageDTO.roomId}", chatMessageDTO)
    }
}