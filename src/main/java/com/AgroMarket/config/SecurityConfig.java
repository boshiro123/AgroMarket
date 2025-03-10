package com.AgroMarket.config;

import com.AgroMarket.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final AuthService authService;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf
            .ignoringRequestMatchers("/cart/**", "/admin/orders/*/status", "/admin/products/*", "/api/newsletter/**"))
        .authorizeHttpRequests(authorize -> authorize
            .requestMatchers("/", "/home", "/register", "/products/**", "/categories/**", "/images/**", "/css/**",
                "/js/**", "/api/newsletter/**", "/uploads/**", "/images/**")
            .permitAll()
            .requestMatchers("/admin/**").hasRole("ADMIN")
            .requestMatchers("/cart/**").authenticated()
            .requestMatchers("/orders/**").authenticated()
            .anyRequest().authenticated())
        .formLogin(form -> form
            .loginPage("/login")
            .successHandler(authenticationSuccessHandler())
            .permitAll())
        .logout(logout -> logout
            .logoutSuccessUrl("/")
            .permitAll());

    return http.build();
  }

  @Bean
  public AuthenticationSuccessHandler authenticationSuccessHandler() {
    return (request, response, authentication) -> {
      if (authentication.getAuthorities().stream()
          .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
        response.sendRedirect("/admin");
      } else {
        response.sendRedirect("/");
      }
    };
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return NoOpPasswordEncoder.getInstance();
  }

  @Bean
  public DaoAuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(authService);
    authProvider.setPasswordEncoder(passwordEncoder());
    return authProvider;
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
    return config.getAuthenticationManager();
  }
}