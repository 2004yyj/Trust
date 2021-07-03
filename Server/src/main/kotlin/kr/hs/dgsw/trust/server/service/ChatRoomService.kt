package kr.hs.dgsw.trust.server.service

import javassist.NotFoundException
import kr.hs.dgsw.trust.server.data.dto.ChatRoomDTO
import kr.hs.dgsw.trust.server.data.vo.ChatRoomVO
import kr.hs.dgsw.trust.server.repository.ChatRoomRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class ChatRoomService(
    private val chatRoomRepository: ChatRoomRepository
) {
    fun findAll(): List<ChatRoomVO> {
        return chatRoomRepository.findAll()
    }

    fun findChatById(roomId: String): ChatRoomVO {
        return try {
            chatRoomRepository.findById(roomId).orElseThrow()
        } catch (e: Exception) {
            throw NotFoundException("채팅방을 찾을 수 없습니다.")
        }
    }

    fun postChatRoom(chatRoomDTO: ChatRoomDTO): ChatRoomVO {
        val chatRoomVO = chatRoomDTO.toVO()
        return chatRoomRepository.save(chatRoomVO)
    }
}