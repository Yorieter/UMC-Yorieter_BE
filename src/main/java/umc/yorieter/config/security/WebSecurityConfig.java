package umc.yorieter.config.security;

import umc.yorieter.config.security.jwt.JwtAccessDeniedHandler;
import umc.yorieter.config.security.jwt.JwtAuthenticationEntryPoint;
import umc.yorieter.config.security.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@Component
public class WebSecurityConfig {  // 스프링 시큐리티 구성요소 설정 클래스 (JWT 사용지원을 위한 구성 또한 포함)


    private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, HttpSecurity httpSecurity) throws Exception {  // HTTP 보안 설정을 구성하는 메소드
        http
                .httpBasic(httpBasic -> {
                    httpBasic.disable();
                })
                .csrf(csrf -> {
                    csrf.disable();
                })
                .sessionManagement((sessionManagement) -> {
                    sessionManagement
                            .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })

                .exceptionHandling(exceptionHandling -> {
                    exceptionHandling
                            .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                            .accessDeniedHandler(jwtAccessDeniedHandler);
                })

                .authorizeHttpRequests((authorizeRequests) ->
                        authorizeRequests
                                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                                .requestMatchers("/**").permitAll()  // 데모 버전 용도
                                // .requestMatchers("/", "/login", "/signup", "/swagger-ui/**", "/health").permitAll()  // (Prod Ver.)
                                .anyRequest().authenticated()
                )


                .apply(new JwtSecurityConfig(tokenProvider));



        return http.build();
    }

}