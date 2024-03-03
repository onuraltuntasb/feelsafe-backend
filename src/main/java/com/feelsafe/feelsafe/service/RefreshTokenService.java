package com.feelsafe.feelsafe.service;

import com.feelsafe.feelsafe.exception.TokenCustomException;
import com.feelsafe.feelsafe.model.RefreshToken;
import com.feelsafe.feelsafe.repository.JdbcRefreshTokenRepository;
import com.feelsafe.feelsafe.repository.JdbcUserRepository;
import com.feelsafe.feelsafe.security.JwtProperties;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {
    JwtProperties jwtProperties = new JwtProperties();

    private final String jwtRefreshExpirationSecond = jwtProperties.getJwtRefreshExpirationSecond();
    private final JdbcRefreshTokenRepository jdbcRefreshTokenRepository;
    private final JdbcUserRepository jdbcUserRepository;

    public RefreshTokenService(JdbcRefreshTokenRepository jdbcRefreshTokenRepository,
                               JdbcUserRepository jdbcUserRepository) {
        this.jdbcRefreshTokenRepository = jdbcRefreshTokenRepository;
        this.jdbcUserRepository = jdbcUserRepository;
    }


    public Optional<RefreshToken> getByToken(String token) {
        return jdbcRefreshTokenRepository.getByToken(token);
    }

    public RefreshToken createRefreshToken(Long userId) {
        RefreshToken refreshToken = new RefreshToken();
        //System.out.println("refreshTokenDurationMs expire when : " + jwtRefreshExpirationSecond);
        refreshToken.setUserId(userId);
        refreshToken.setExpiryDate(
                Timestamp.from(Instant.now().plusSeconds(Long.parseLong(jwtRefreshExpirationSecond))));
        refreshToken.setToken(UUID.randomUUID().toString());

        refreshToken = jdbcRefreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    public RefreshToken verifyExpiration(RefreshToken token){
        if (token.getExpiryDate().compareTo(Timestamp.from(Instant.now())) < 0) {
            jdbcRefreshTokenRepository.deleteByToken(token.getToken());

            throw new TokenCustomException(token.getToken(),
                                           "Refresh token was expired. Please make a new sign in request"
            );
        }

        return token;
    }

    @Transactional
    public void deleteByUserId(Long userId) {
        jdbcRefreshTokenRepository.deleteByUserId(userId);
    }

}
