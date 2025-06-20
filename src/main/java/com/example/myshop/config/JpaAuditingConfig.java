package com.example.myshop.config;

import com.example.myshop.security.CustomUserDetails;
import com.google.gson.Gson;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
@Log4j2
public class JpaAuditingConfig {
    private static final Gson gson = new Gson();

    @Bean
    public AuditorAware<Integer> auditorProvider() {
        return () -> {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            log.info("(JpaAuditingConfig) {}", gson.toJson(auth));
            if (auth == null || !auth.isAuthenticated() || auth.getPrincipal().equals("anonymousUser")) {
                throw new RuntimeException("Bạn cần đăng nhập để bình luận");
//                return Optional.empty();
            }

            // Giả sử user của bạn implement UserDetails và có getId()
            log.info("(JpaAuditingConfig) getPrincipal {}", gson.toJson(auth.getPrincipal()));
            return Optional.of(Integer.parseInt( (String) auth.getPrincipal()));
        };
    }
}
