package com.UnidosCorazones.demo.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        // ¡ADVERTENCIA! Esto es obsoleto y muy inseguro.
        // Esto le dice a Spring que las contraseñas NO están codificadas.
        //return NoOpPasswordEncoder.getInstance();
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Order(1)
    public SecurityFilterChain adminFilterChain(HttpSecurity http) throws Exception {

        http
                .securityMatcher("/admin/**", "/login-admin", "/admin-login-process") // <--- IMPORTANTE: Solo aplica aquí
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/login-admin", "/css/**", "/js/**", "/img/**").permitAll()
                        .requestMatchers("/admin/**").hasAnyAuthority("ADMINISTRADOR", "ROLE_ADMINISTRADOR") // Ajusta según como guardes el rol en BD
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login-admin") // Tu vista HTML de admin
                        .loginProcessingUrl("/admin-login-process") // El th:action del form admin
                        .defaultSuccessUrl("/admin/campanias", true)
                        .failureUrl("/login-admin?error") // <--- Si falla admin, vuelve al login de admin
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/admin/logout")
                        .logoutSuccessUrl("/login-admin?logout")
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                );

        return http.build();
    }

    /**
     * CADENA 2: CONFIGURACIÓN PARA VOLUNTARIOS Y PÚBLICO
     * Se ejecuta para todo lo que no atrapó la cadena anterior.
     */
    @Bean
    public SecurityFilterChain voluntarioFilterChain(HttpSecurity http) throws Exception {

        http
                // No ponemos securityMatcher, así que atrapa todo lo demás
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/", "/inicio", "/css/**", "/js/**", "/img/**", "/public/**","/donaciones/**").permitAll()
                        .requestMatchers("/login-voluntario","/registro-voluntario").permitAll() // La página combinada
                        .requestMatchers("/voluntario/**").hasAnyAuthority("VOLUNTARIO", "ROLE_VOLUNTARIO")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login-voluntario") // Tu vista HTML combinada (Login/Registro)
                        .loginProcessingUrl("/voluntario-login-process") // El th:action del form voluntario
                        .defaultSuccessUrl("/voluntario/inicio", true)
                        .failureUrl("/login-voluntario?error") // <--- Si falla voluntario, vuelve al registro/login voluntario
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/voluntario/logout")
                        .logoutSuccessUrl("/login-voluntario?logout")
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                );

        return http.build();
    }
}
