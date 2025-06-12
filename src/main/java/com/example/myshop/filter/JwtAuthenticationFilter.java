package com.example.myshop.filter;

import com.example.myshop.exception.TokenExpiredException;
import com.example.myshop.exception.TokenInvalidException;
import com.example.myshop.response.ErrorResponse;
import com.example.myshop.service.InvalidTokenService;
import com.example.myshop.service.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

@Component
@Log4j2
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final static String BEARER = "Bearer ";
    private final static String AUTHORIZATION_HEADER = "Authorization";
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private JwtService jwtService;
    @Autowired
    private InvalidTokenService invalidTokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("(doFilterInternal) request: {}, response: {}, filterChain: {}", request, response, filterChain);
        String accessToken = request.getHeader(AUTHORIZATION_HEADER);
        if (Objects.isNull(accessToken) || !accessToken.startsWith(BEARER)) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwtToken = accessToken.substring(7);
        try {
            // Kiểm tra xem người dùng đã được xác thực chưa
            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                // Kiểm tra xem token có bị vô hiệu hóa không
                if (invalidTokenService.isTokenInvalid(jwtToken)) {
                    log.error("Invalid token by visible: {}", jwtToken);
                    respondWithJson(response, HttpStatus.UNAUTHORIZED, "Token is invalid");
                    return;
                }
                // Lấy thông tin người dùng từ token
                String userid = jwtService.getSubjectFromToken(jwtToken);
                String username = jwtService.getUsernameFromToken(jwtToken);
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        userid, username, new ArrayList<>()
                );
                // Thiết lập xác thực cho SecurityContext
                // Cho phép các phần khác của ứng dụng truy cập thông tin người dùng đã xác thực
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
            filterChain.doFilter(request, response);
        } catch (TokenInvalidException e) {
            log.error("Invalid token: {}", e.getMessage());
            respondWithJson(response, HttpStatus.UNAUTHORIZED, "Token is invalid");
        } catch (TokenExpiredException e) {
            log.error("Expired token: {}", e.getMessage());
            respondWithJson(response, HttpStatus.UNAUTHORIZED, "Token is expired");
        }
    }

    private void respondWithJson(HttpServletResponse response, HttpStatus status, String message) throws IOException {
        response.setStatus(status.value());
        response.setContentType("application/json");

        ErrorResponse errorResponse = new ErrorResponse(String.valueOf(status.value()),message);
        String jsonResponse = objectMapper.writeValueAsString(errorResponse);

        response.getWriter().write(jsonResponse);
        response.getWriter().flush();
    }
}
