package kr.hs.dgsw.trust.server.util

import kr.hs.dgsw.trust.server.token.TokenProvider
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder

fun tokenGenerator(
    username: String,
    password: String,
    amb: AuthenticationManagerBuilder,
    tp: TokenProvider
): String {
    val authenticationToken = UsernamePasswordAuthenticationToken(username, password)
    val authentication: Authentication = amb.getObject().authenticate(authenticationToken)

    SecurityContextHolder.getContext().authentication = authentication

    return tp.createToken(authentication)
}