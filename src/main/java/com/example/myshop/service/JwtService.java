package com.example.myshop.service;

import com.example.myshop.exception.TokenExpiredException;
import com.example.myshop.exception.TokenInvalidException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
@Log4j2
public class JwtService {

    private final String KEY = "shopsport@2023";
    private final long EXPIRE_TIME_REFRESH_TOKEN = 3600000;

    private String generateToken(int subject, Map<String, Object> claims, long tokenLifeTime) {
        log.info("(generateToken) start");
        log.info("(date): {}", new Date(System.currentTimeMillis() + tokenLifeTime));
        return Jwts.builder()
                .setSubject(String.valueOf(subject))  // Đặt subject là ID của người dùng
                .claim("claims", claims) // Thêm các claims vào token
                .setIssuedAt(new Date(System.currentTimeMillis()))  // Thời gian phát hành
                .setExpiration(new Date(System.currentTimeMillis() + tokenLifeTime))  // Thời gian hết hạn
                .signWith(SignatureAlgorithm.HS256, KEY)  // Ký token với thuật toán HS256 và khóa bí mật
                .compact();  // Tạo token
    }

    public String generateAccessToken(int userId, Map<String, Object> claims) {
        log.info("(generateAccessToken) start");
        return generateToken(userId, claims, EXPIRE_TIME_REFRESH_TOKEN);
    }

    public String generateRefreshToken(int userId, String username) {
        log.info("(generateRefreshToken) start");
        Map<String, Object> claims = new HashMap<String, Object>();
        claims.put("username", username);
        return generateToken(userId, claims, EXPIRE_TIME_REFRESH_TOKEN);
    }

    public String getSubjectFromToken(String token) {
        log.info("(getSubjectFromToken)");
        // Kiểm tra token có hợp lệ hay hết hạn hay không
        validateToken(token);
        return getClaims(token, KEY).getSubject();
    }

    public String getUsernameFromToken(String token) {
        validateToken(token);
        log.info("(getUsernameFromToken) start");
        Map<String, Object> claims = (Map<String, Object>) getClaims(token, KEY).get("claims");
        return String.valueOf(claims.get("username"));
    }

    public void validateToken(String token) {
        log.info("(validateToken)");
        // check valid token
        if (!isValidToken(token)) {
            log.error("(validateToken) ====> TokenInvalidException");
            throw new TokenInvalidException();
        }
        if (isExpiredToken(token)) {
            log.error("(validateToken) ====> TokenExpiredException");
            throw new TokenExpiredException();
        }
    }

    /**
     * Phương thức kiểm tra tính hợp lệ của token
     * @param token
     * @return true nếu hợp lệ, và false nếu không hợp lệ
     */
    public Boolean isValidToken(String token) {
        try {
            // Jwts.parser: tạo một đối tượng parser để phân tích JWT
            // setSigningKey: thiết lập khóa ký (signing key) cho việc xác thực. Khóa này phải khớp với khóa được
            //                 sử dụng để ký token khi nó được tạo ra
            // parserClaimsJws(toke): phân tích token và xác thực chữ ký của nó. Nếu token hợp lệ, phương thức này sẽ
            //                        trả về một đối tượng chứa các claims(thông tin) trong token
            Jwts.parser().setSigningKey(KEY).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            // JwtException: đây là ngoại lệ được ném ra khi có vấn đề với token, chẳng hạn như token không hợp lệ,
            //               đã hết hạn hoặc bị thay đổi
            // IllegalArgumentException: ngoại lệ này có thể xảy ra nếu token không được đúng định dạng
            return false;
        }
    }

    public Boolean isExpiredToken(String token) {
        //getExpiration(): lấy thời gian hết hạn của token
        return getClaims(token, KEY).getExpiration().before(new Date());
    }

    /**
     * Phương thức lấy thông tin ở trong token
     * @param token
     * @param secretKey
     * @return trả về đối tượng chứa thông tin token
     */
    private Claims getClaims(String token, String secretKey) {
        log.info("(getClaims)");
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }
}
