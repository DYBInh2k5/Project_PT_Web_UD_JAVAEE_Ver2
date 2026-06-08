package com.bookstore.config;

import com.bookstore.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

// Cấu hình bảo mật Spring Security: phân quyền, login, logout
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    // Mã hóa mật khẩu bằng BCrypt
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Xác thực dùng UserDetailsService + PasswordEncoder
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    // Cấu hình phân quyền URL, form login, logout
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(config -> config
                .requestMatchers("/admin/**").hasRole("ADMIN")           // Chỉ admin
                .requestMatchers("/cart/**").hasAnyRole("CUSTOMER", "ADMIN")
                .requestMatchers("/order/**").hasAnyRole("CUSTOMER", "ADMIN")
                .requestMatchers("/register", "/login", "/css/**", "/js/**", "/images/**", "/").permitAll()
                .requestMatchers("/shop/**", "/api/**").permitAll()      // Public
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/")
                .failureUrl("/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .permitAll()
            )
            .exceptionHandling(handler -> handler
                .accessDeniedPage("/login?accessDenied=true")
            );
        return http.build();
    }
}
