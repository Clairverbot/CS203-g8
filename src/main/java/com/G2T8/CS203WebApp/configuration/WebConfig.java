package com.G2T8.CS203WebApp.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    @Value("${react_origin}")
    private String crossOrigin;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins(crossOrigin).allowedMethods("GET", "POST","PUT", "DELETE");
    }

    // @Bean
    // public CorsFilter corsFilter() {
    // UrlBasedCorsConfigurationSource source = new
    // UrlBasedCorsConfigurationSource();
    // CorsConfiguration config = new CorsConfiguration();
    // config.setAllowCredentials(true);
    // config.addAllowedOrigin("http://localhost:3000");
    // config.addAllowedHeader("*");
    // config.addAllowedMethod("*");
    // source.registerCorsConfiguration("/**", config);
    // return new CorsFilter(source);
    // }
}
