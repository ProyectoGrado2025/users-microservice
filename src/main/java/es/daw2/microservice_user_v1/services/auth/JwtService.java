package es.daw2.microservice_user_v1.services.auth;

import java.sql.Date;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;


@Service
public class JwtService {

    @Value("${security.jwt.expiration-in-minutes}")
    private Long EXPIRATION_IN_MINUTES;

    @Value("${security.jwt.secret-key}")
    private String SECRET_KEY;

    public String generateToken(UserDetails user, Map<String, Object> extraClaims) {
        
        Date issuedAt = new Date(System.currentTimeMillis());
        Date expiration = new Date( (EXPIRATION_IN_MINUTES * 60 * 1000) + issuedAt.getTime() );
        String jwt = Jwts.builder()
                        .header()
                            .type("JWT")
                            .and()

                        .subject(user.getUsername())
                        .issuedAt(issuedAt)
                        .expiration(expiration)
                        .claims(extraClaims)

                        .signWith(generateKey(), Jwts.SIG.HS256)
                        .compact();
        return jwt;
    }

    private SecretKey generateKey() {

        byte[] passwordDecoded = Decoders.BASE64.decode(SECRET_KEY);

        System.out.println(new String(passwordDecoded) );
        return Keys.hmacShaKeyFor(passwordDecoded);
    }

    public String extractEmail(String jwt) {
        return extractAllClaims(jwt).getSubject();
    }

    private Claims extractAllClaims(String jwt) {
        return Jwts
                .parser()
                .verifyWith( generateKey() )
                .build()
                .parseSignedClaims(jwt)
                .getPayload();
    }

    public String extractJwtFromRequest(HttpServletRequest request) {
        // 1. Obtener el encabezado Http llamado "Authorization"
        String authHeader = request.getHeader("Authorization");
        if(!StringUtils.hasText(authHeader) || !authHeader.startsWith("Bearer ")){
            return null;
        }

        // 2. Obtener el Token JWT desde el encabezado
        return authHeader.split(" ")[1];
        
    }

    public java.util.Date extractExpiration(String jwt) {
        return extractAllClaims(jwt).getExpiration();
    }

}
