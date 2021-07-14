package hello.condeliner.jwt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import hello.condeliner.jwt.utils.JwtAccessDeniedHandler;
import hello.condeliner.jwt.utils.JwtAuthenticationEntryPoint;
import hello.condeliner.jwt.utils.JwtSecurityConfig;
import hello.condeliner.jwt.utils.TokenProvider;
import lombok.RequiredArgsConstructor;

/**
 * 메소드 단위로 @PreAuthorize 검증 애노테이션을 사용하기 위해
 * EnableGlobalMethodSecurity 애노테이션 추가.
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Override
	public void configure(WebSecurity web) throws Exception {
        web.ignoring()
            .antMatchers(
                "/h2-console/**",
                "/favicon.ico"
            );
	}
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            // 토큰 방식을 사용하므로, csrf 설정 비활성화.
            .csrf().disable()

            // 예외처리 적용.
            .exceptionHandling()
            .authenticationEntryPoint(jwtAuthenticationEntryPoint)
            .accessDeniedHandler(jwtAccessDeniedHandler)

            // 데이터 확인을 위해 사용하고 있는 h2-console 설정 추가.
            .and()
            .headers()
            .frameOptions()
            .sameOrigin()

            // 세션을 사용하지 않기 때문에 세션 설정 'STATELESS' 로 설정.
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

            // 특정 URI 토큰 없이 호출이 가능하도록 설정.
            .and()
            .authorizeRequests()
            .antMatchers("/api/hello").permitAll()
            .antMatchers("/api/v1/members/authenticate").permitAll()
            .antMatchers("/api/v1/members/signup").permitAll()
            .anyRequest().authenticated()

            // 
            .and()
            .apply(new JwtSecurityConfig(tokenProvider));
    }
}
