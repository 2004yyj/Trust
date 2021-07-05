package kr.hs.dgsw.trust.server.configuration

import kr.hs.dgsw.trust.server.token.JwtAccessDeniedHandler
import kr.hs.dgsw.trust.server.token.JwtAuthenticationEntryPoint
import kr.hs.dgsw.trust.server.token.JwtSecurityConfigAdapter
import kr.hs.dgsw.trust.server.token.TokenProvider
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.filter.CorsFilter


@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class SecurityConfig(
    private val tokenProvider: TokenProvider,
    private val corsFilter: CorsFilter,
    private val jwtAuthenticationEntryPoint: JwtAuthenticationEntryPoint,
    private val jwtAccessDeniedHandler: JwtAccessDeniedHandler
) : WebSecurityConfigurerAdapter() {

    override fun configure(web: WebSecurity) {
        web.ignoring()
            .antMatchers(
                "/h2-console/**", "/favicon.ico", "/error"
            )
    }

    @Throws(Exception::class)
    override fun configure(httpSecurity: HttpSecurity) {
        httpSecurity // token을 사용하는 방식이기 때문에 csrf를 disable합니다.
            .httpBasic().disable() // REST API만을 고려, 기본 설정 해제
            .csrf().disable()
            .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter::class.java)
            .exceptionHandling()
            .authenticationEntryPoint(jwtAuthenticationEntryPoint)
            .accessDeniedHandler(jwtAccessDeniedHandler) // enable h2-console
            .and()
            .headers()
            .frameOptions()
            .sameOrigin() // 세션을 사용하지 않기 때문에 STATELESS로 설정
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests() // 요청에 대한 사용권한 체크
            .antMatchers("/admin/**").hasRole("ADMIN")
            .antMatchers("/user/**").hasRole("USER")
            .anyRequest().permitAll() // 나머지 요청은 누구나 접근 가능
            .and()
            .apply(JwtSecurityConfigAdapter(tokenProvider))
    }
}
