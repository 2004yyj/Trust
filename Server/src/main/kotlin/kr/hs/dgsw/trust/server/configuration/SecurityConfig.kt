package kr.hs.dgsw.trust.server.configuration

import lombok.AllArgsConstructor
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy

@Configuration
@AllArgsConstructor
@EnableWebSecurity
class SecurityConfig(): WebSecurityConfigurerAdapter() {
    override fun configure(http: HttpSecurity?) {
        http
            ?.httpBasic()?.disable() // REST API만을 고려, 기본 설정 해제
            ?.csrf()?.disable() // csrf 사용 X
            ?.sessionManagement()?.sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 토큰 기반 인증이므로 세션도 사용 X
            ?.and()
            ?.authorizeRequests() // 요청에 대한 사용권한 체크
            ?.antMatchers("/admin/**")?.hasRole("ADMIN")
            ?.antMatchers("/user/**")?.hasRole("MEMBER")
            ?.anyRequest()?.permitAll() // 나머지 요청은 누구나 접근 가능
    }

    override fun configure(web: WebSecurity?) {
        super.configure(web)
        web?.ignoring()?.antMatchers("/css/**", "/js/**", "/img/**", "/lib/*8", "/static/**")
    }

}