package com.example.myshop.config;

import com.example.myshop.filter.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Override
    public void configure(HttpSecurity http) throws  Exception {
        http .cors().and() // cấu hình CORS để cho phép các yêu cầu từ các nguồn khác nhau
                .csrf().disable() // tắt báo vệ CSRF (thích hợp khi sử dụng JWT)
                .authorizeRequests()
                    .antMatchers("/api/v1/auth/**").permitAll() // Cho phép tất cả các yêu cầu đến đường dẫn ... mà không cần xác thực
                    .anyRequest().authenticated() // Yêu cầu xác thực cho tất cả các yêu cầu khác
                .and()
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)  // Thiết lập chế độ STETELESS nghĩa là không lưu trữ thông tin phiên
                .and()
                // đảm bảo răng yêu cầu được xác thực trước khi đến bộ lọc xác thực mặc đinh
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);  // Thêm bộ lọc JWT vào chuỗi bộ lọc bảo mật,
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        // Cấu hình cho phép tất cả các header, origin và methods. Điều này có thể cần điều chỉnh lại để bảo mật tốt hơn bằng
        // cách chỉ định tên miền cụ thể
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedHeader("*");  // cho phép tất cả các header
        configuration.addAllowedOrigin("*");  // cho phép tất cả các tên miền
        configuration.addAllowedMethod("*");  // cho phép tất cả các phương thức HTTP
        configuration.setMaxAge(1800L);       // Thời gian cache cho preflight request
        // Cung cấp cấu hình CORS cho các đường dẫn cụ thể
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Đăng ký cấu hình CORS cho tất cả các đường dẫn
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Sử dụng Bcrypt để mã hóa mật khẩu
        return new BCryptPasswordEncoder();
    }
}