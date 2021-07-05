package kr.hs.dgsw.trust.server.token

import org.springframework.security.config.annotation.SecurityConfigurerAdapter
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.DefaultSecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter


class JwtSecurityConfigAdapter(private val tokenProvider: TokenProvider) :
    SecurityConfigurerAdapter<DefaultSecurityFilterChain?, HttpSecurity>() {
    override fun configure(http: HttpSecurity) {
        val customFilter = JwtFilter(tokenProvider)
        http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter::class.java)
    }
}