package kr.hs.dgsw.trust.server.service

import kr.hs.dgsw.trust.server.data.dto.AccountVO
import kr.hs.dgsw.trust.server.data.dto.AuthorityVO
import kr.hs.dgsw.trust.server.data.dto.TokenDTO
import kr.hs.dgsw.trust.server.exception.ExistsException
import kr.hs.dgsw.trust.server.exception.UnauthenticatedException
import kr.hs.dgsw.trust.server.repository.AccountRepository
import kr.hs.dgsw.trust.server.token.TokenProvider
import kr.hs.dgsw.trust.server.util.tokenGenerator
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.core.userdetails.User
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AccountService(
    private val accountRepository: AccountRepository,
    private val authenticationManagerBuilder: AuthenticationManagerBuilder,
    private val tokenProvider: TokenProvider,
    private val passwordEncoder: PasswordEncoder
) {

    @Transactional(readOnly = true)
    fun signUp(
        name: String,
        username: String,
        password: String,
        profileImage: String?
    ): TokenDTO {
        if (isSignUpInfoNotNull(name, username, password)) {
            if (accountRepository.findById(username).orElse(null) != null) {
                throw ExistsException("이미 가입되어 있는 유저입니다.")
            }

            val authority = AuthorityVO()
            authority.authorityName = "ROLE_USER"

            val accountVO = AccountVO().apply {
                this.name = name
                this.username = username
                this.password = password
                this.profileImage = profileImage
                this.activated = true
                this.authorities = setOf(authority)
            }
            accountRepository.save(accountVO)

            val jwt = tokenGenerator(username, password, authenticationManagerBuilder, tokenProvider)
            return TokenDTO(jwt, username)
        } else {
            throw NullPointerException("빈칸이 없는지 확인해주세요.")
        }
    }

    private fun isSignUpInfoNotNull(name: String, username: String, password: String) : Boolean {
        return !(name.isEmpty() || username.isEmpty() || password.isEmpty())
    }

    @Transactional(readOnly = true)
    fun login(token: String) : TokenDTO {
        if (token.isNotEmpty() && tokenProvider.validateToken(token)) {
            val username = (tokenProvider.getAuthentication(token).principal as User).username
            return TokenDTO(token, username)
        } else {
            throw UnauthenticatedException("세션이 만료되었습니다. 다시 로그인 해주세요.")
        }
    }

    @Transactional(readOnly = true)
    fun login(username: String, password: String): TokenDTO {
        val account = getAccount(username)

        if (isLoginInfoNotNull(username, password) &&
            matchPassword(password, account.password!!)
        ) {
            val jwt = tokenGenerator(username, password, authenticationManagerBuilder, tokenProvider)
            return TokenDTO(jwt, username)
        } else {
            throw UnauthenticatedException("아이디 또는 비밀번호가 잘못되었습니다.")
        }
    }

    fun getAccount(username: String): AccountVO {
        try {
            return accountRepository.findById(username).orElseThrow()
        } catch (e: Exception) {
            throw UnauthenticatedException("계정을 찾을 수 없습니다.")
        }
    }

    private fun matchPassword(rawPassword: String, encodedPassword: String) : Boolean {
        return passwordEncoder.matches(rawPassword, encodedPassword)
    }

    private fun isLoginInfoNotNull(username: String, password: String) : Boolean {
        return !(username.isEmpty() || password.isEmpty())
    }
}