package kr.hs.dgsw.trust.server.controller

import kr.hs.dgsw.trust.server.data.dto.ChatRoomDTO
import kr.hs.dgsw.trust.server.data.response.JsonResponse
import kr.hs.dgsw.trust.server.data.vo.ChatRoomVO
import kr.hs.dgsw.trust.server.data.vo.toJsonObject
import kr.hs.dgsw.trust.server.service.ChatRoomService
import org.springframework.boot.configurationprocessor.json.JSONArray
import org.springframework.boot.configurationprocessor.json.JSONObject
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class ChatRoomController(
    private val chatRoomService: ChatRoomService
) {
    @GetMapping("/chat/rooms")
    fun getList(): JsonResponse<JSONArray> {
        val list = chatRoomService.findAll()
        val jsonList = JSONArray()
        list.forEach {
            jsonList.put(it.toJsonObject())
        }
        return JsonResponse("200", "채팅방 가져오기 성공", jsonList)
    }

    @PostMapping("/chat/room")
    fun postChat(chatRoomDTO: ChatRoomDTO): JsonResponse<JSONObject> {
        val chatRoomVO = chatRoomService.postChatRoom(chatRoomDTO).toJsonObject()
        return JsonResponse("200", "채팅방 추가하기 성공", chatRoomVO)
    }

    @GetMapping("/chat/room")
    fun getChat(roomId: String): JsonResponse<ChatRoomVO> {
        val chatRoomVO = chatRoomService.findChatById(roomId)
        return JsonResponse("200", "채팅방 가져오기 성공", chatRoomVO)
    }
}