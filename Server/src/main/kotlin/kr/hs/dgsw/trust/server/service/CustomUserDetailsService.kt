package kr.hs.dgsw.trust.server.service

import kr.hs.dgsw.trust.server.data.entity.Account
import kr.hs.dgsw.trust.server.repository.AccountRepository
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.util.stream.Collectors

@Component("userDetailsService")
class CustomUserDetailsService(private val accountRepository: AccountRepository) : UserDetailsService {

    @Transactional
    override fun loadUserByUsername(username: String): UserDetails {
        return accountRepository.findById(username)
            .map { account -> createAccount(account) }
            .orElseThrow { UsernameNotFoundException("$username -> 데이터베이스에서 찾을 수 없습니다.") }
    }

    private fun createAccount(account: Account): User {
        val grantedAuthorities: MutableList<SimpleGrantedAuthority>? = account.authorities?.stream()
            ?.map { authority -> SimpleGrantedAuthority(authority.authorityName) }
            ?.collect(Collectors.toList())
        return User(
            account.username,
            account.password,
            grantedAuthorities
        )
    }
}