package id.ac.ui.cs.advprog.b10.petdaycare.auth.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    public void setup() {
        jwtService = new JwtService();
    }

    @Test
    public void testGenerateTokenAndExtractUsername() {
        UserDetails userDetails = getUserDetails();
        String token = jwtService.generateToken(userDetails);
        String username = jwtService.extractUsername(token);

        assertEquals(userDetails.getUsername(), username);
    }

    @Test
    public void testGenerateTokenWithExtraClaims() {
        UserDetails userDetails = getUserDetails();
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", "admin");

        String token = jwtService.generateToken(extraClaims, userDetails);
        Claims claims = Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token).getBody();

        assertEquals("admin", claims.get("role"));
        assertEquals(userDetails.getUsername(), claims.getSubject());
    }

    @Test
    public void testIsTokenValid() {
        UserDetails userDetails = getUserDetails();
        String token = jwtService.generateToken(userDetails);

        assertTrue(jwtService.isTokenValid(token, userDetails));
    }

    @Test
    public void testIsTokenExpired() {
        UserDetails userDetails = getUserDetails();
        String expiredToken = generateExpiredToken(userDetails);

        assertFalse(jwtService.isTokenValid(expiredToken, userDetails));
    }

    private UserDetails getUserDetails() {
        return User.withUsername("john.doe")
                .password("password")
                .roles("user")
                .build();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode("645367566B59703373367639792F423F4528482B4D6251655468576D5A713474");
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private String generateExpiredToken(UserDetails userDetails) {
        return Jwts.builder()
                .setClaims(new HashMap<>())
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis() - 1000 * 60 * 60)) // Set in the past
                .setExpiration(new Date(System.currentTimeMillis() - 1000)) // Expired token
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }
}
