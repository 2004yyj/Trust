package kr.hs.dgsw.trust.server.service

import javassist.NotFoundException
import kr.hs.dgsw.trust.server.data.vo.Liked
import kr.hs.dgsw.trust.server.exception.ExistsException
import kr.hs.dgsw.trust.server.exception.UnauthenticatedException
import kr.hs.dgsw.trust.server.repository.LikedRepository
import kr.hs.dgsw.trust.server.token.TokenProvider
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Service
import java.sql.Timestamp

@Service
class LikedService(
    private val likedRepository: LikedRepository,
    private val tokenProvider: TokenProvider
) {
    fun findAllByPostId(token: String, postId: Int): List<Liked> {
        val list = likedRepository.findAllByPostId(postId).orElseThrow {
            NotFoundException("글을 찾을 수 없습니다.")
        }
        if (token.isNotEmpty() && tokenProvider.validateToken(token)) {
            return list
        } else {
            throw UnauthenticatedException("세션이 만료되었습니다. 다시 로그인 해 주세요.")
        }
    }

    fun save(token: String, postId: Int): List<Liked> {
        if (token.isNotEmpty() && tokenProvider.validateToken(token)) {
            val liked = Liked()
            val username = (tokenProvider.getAuthentication(token).principal as User).username
            likedRepository.findByPostIdAndUsername(postId, username).orElseGet {
                liked.postId = postId
                liked.username = username
                liked.createdAt = Timestamp(System.currentTimeMillis())
                likedRepository.save(liked)
            }
            return likedRepository.findAllByPostId(postId).orElseThrow {
                UnauthenticatedException("오류가 발생했습니다.")
            }
        } else {
            throw UnauthenticatedException("세션이 만료되었습니다. 다시 로그인 해 주세요.")
        }
    }

    fun delete(token: String, postId: Int): List<Liked> {
        if (token.isNotEmpty() && tokenProvider.validateToken(token)) {

            val username = (tokenProvider.getAuthentication(token).principal as User).username

            val liked =
                likedRepository.findByPostIdAndUsername(postId, username).orElseThrow {
                    NotFoundException("좋아요가 존재하지 않습니다.")
                }

            if (username == liked.username) {
                likedRepository.deleteByPostIdAndUsername(postId, username)
            } else {
                throw UnauthenticatedException("계정을 찾을 수 없습니다.")
            }

            return likedRepository.findAllByPostId(postId).orElseThrow {
                UnauthenticatedException("오류가 발생했습니다.")
            }
        } else {
            throw UnauthenticatedException("세션이 만료되었습니다. 다시 로그인 해 주세요.")
        }
    }
}