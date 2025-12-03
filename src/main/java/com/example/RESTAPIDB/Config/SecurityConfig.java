package com.example.RESTAPIDB.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // 1. Define el filtro de seguridad (incluye la habilitación de CORS)
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // Habilitar CORS en la cadena de filtros de Spring Security
        // Usará la configuración definida en el Bean 'corsConfigurationSource()'
        http.cors(Customizer.withDefaults());

        // Deshabilitar CSRF (Cross-Site Request Forgery)
        // Esto es común y necesario para APIs REST sin estado.
        http.csrf(csrf -> csrf.disable());

        // Autorizar todas las solicitudes a cualquier ruta
        http.authorizeHttpRequests(auth -> auth.requestMatchers("/**").permitAll());

        return http.build();
    }

    // 2. Define la fuente de configuración de CORS
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        // Objeto que almacena la configuración de CORS
        CorsConfiguration configuration = new CorsConfiguration();

        // Configuración para permitir todos los puertos en localhost
        // [CÓDIGO CLAVE] Permite peticiones desde cualquier puerto en el dominio localhost
        // Nota: También podrías usar Collections.singletonList("http://localhost:*") si es compatible con tu versión de Spring.
        configuration.setAllowedOrigins(Collections.singletonList("http://localhost:[*]"));

        // Permite todos los métodos HTTP que probablemente usarás
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD"));

        // Permite todas las cabeceras en las solicitudes
        configuration.setAllowedHeaders(Collections.singletonList("*"));

        // Permite el envío de credenciales (cookies, cabeceras de autenticación)
        configuration.setAllowCredentials(true);

        // Define qué rutas aplicarán esta configuración de CORS
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Aplica esta configuración a TODAS las rutas de la aplicación
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}