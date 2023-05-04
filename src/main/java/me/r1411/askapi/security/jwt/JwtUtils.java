package me.r1411.askapi.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import me.r1411.askapi.model.User;
import me.r1411.askapi.security.userdetails.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

@Component
public class JwtUtils {
    @Value("${jwt.token.valid_time}")
    private Long validTime;

    private final UserDetailsServiceImpl userDetailsService;
    private final SecretKey signingKey;

    @Autowired
    public JwtUtils(UserDetailsServiceImpl userDetailsService, @Value("${jwt.token.secret}") String secret) {
        this.userDetailsService = userDetailsService;
        this.signingKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public Map.Entry<String, Date> createToken(User user) {
        Claims claims = Jwts.claims().setSubject(user.getUsername());
        claims.put("role", user.getRole());

        Date creationDate = new Date();
        Date expirationDate = new Date(creationDate.getTime() + validTime);

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(creationDate)
                .setExpiration(expirationDate)
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();

        return Map.entry(token, expirationDate);
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, token, userDetails.getAuthorities());
    }

    public String getUsername(String token) {
        JwtParser parser = Jwts.parserBuilder().setSigningKey(signingKey).build();
        return parser.parseClaimsJws(token).getBody().getSubject();
    }

    public boolean isTokenValid(String token) {
        try {
            JwtParser parser = Jwts.parserBuilder().setSigningKey(signingKey).build();
            Jws<Claims> claims = parser.parseClaimsJws(token);

            return claims.getBody().getExpiration().after(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }


}
