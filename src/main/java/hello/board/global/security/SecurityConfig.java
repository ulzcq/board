package hello.board.global.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity //HttpSecurity DI
public class SecurityConfig {

    /**
     * Spring security 설정
     * TODO: 나중에 스프링 시큐리티 공부하고? 사용
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
                .csrf().disable()
                .formLogin().disable() //formLogin 인증 비활성화
                .httpBasic().disable(); //httpBasic 인증 비활성화
//                .authorizeHttpRequests((authz) -> authz
//                        .anyRequest().authenticated()); // 어떤 HTTP 요청에도 보안검사

        return http.build();
    }

    /**
     * 패스워드인코더 Bean 등록
     * 스트링부트가 기본적으로 제공/권장하는 bcrypt 전략 사용
     */
    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}
