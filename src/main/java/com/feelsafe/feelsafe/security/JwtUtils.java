package com.feelsafe.feelsafe.security;

import com.feelsafe.feelsafe.exception.TokenCustomException;
import io.jsonwebtoken.Claims;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@NoArgsConstructor
@Component
public class JwtUtils {
    JwtProperties jwtProperties = new JwtProperties();
    private final String jwtSecret = jwtProperties.getJwtSecret();
    private final String jwtExpirationsMs = jwtProperties.getJwtExpirationMs();

    public String extractUsername(String token) {return extractClaim(token, Claims::getSubject);}

    public Date extractExpiration(String token) {return extractClaim(token, Claims::getExpiration);}

    public String getString(String name, Claims claims){
        Object v = claims.get(name); return v != null ? String.valueOf(v) : null;
    }

    public String getAuthorityClaim(String token){
        Claims claims = extractAllClaims(token.substring(7));
        String authorities = getString("authorities",claims);

        String st = authorities.substring(12);

        return st.substring(0,st.length() - 2);

    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Claims extractAllClaims(String token) {

        Claims claims = null;

        try {
            claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            throw new TokenCustomException(token, "Jwt token is expired");
        } catch (MalformedJwtException e) {
            throw new TokenCustomException(token, "Jwt token is malformed");
        } catch (SignatureException e) {
            throw new TokenCustomException(token, "Jwt token signature exception");
        } catch (Exception e) {
            throw new TokenCustomException(token, e.getMessage());
        }

        return claims;
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>(); return createToken(claims, userDetails);
    }

    public String createToken(Map<String, Object> claims, UserDetails userDetails) {

        System.out.println("token expire when : " + jwtExpirationsMs);
        System.out.println(
                "token signingKey when : " + jwtSecret);

        return Jwts
                .builder()
                .setClaims(claims)
                //NOTE: passed user email to userDetail's username
                .setSubject(userDetails.getUsername())
                //TODO authorities
                .claim("authorities", userDetails.getAuthorities())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(jwtExpirationsMs)))
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();
    }

    public Boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token); return (username.equals(userDetails.getUsername()) &&
                !isTokenExpired(token));
    }

    public Boolean isTokenValid(String token, String email) {
        final String username = extractUsername(token); return (username.equals(email) && !isTokenExpired(token));
    }
}
