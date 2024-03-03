package com.feelsafe.feelsafe.repository;

import com.feelsafe.feelsafe.model.Diary;
import com.feelsafe.feelsafe.model.RefreshToken;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class JdbcRefreshTokenRepository {
    private final JdbcTemplate jdbcTemplate;

    public Optional<RefreshToken> getByUserId(Long userId) {

        try {
            return jdbcTemplate.queryForObject("select * from refresh_token where user_id = ?",
                                               new Object[]{userId},
                                               (rs, rowNum) ->
                                                       Optional.of(new RefreshToken(
                                                               rs.getLong("id"),
                                                               rs.getLong("user_id"),
                                                               rs.getString("token"),
                                                               rs.getTimestamp("expiry_date")

                                                       ))
                                              );

        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

    }


    public Optional<RefreshToken> getByToken(String token) {

        try {
            return jdbcTemplate.queryForObject("select * from refresh_token where token = ?",
                                        new Object[]{token},
                                        (rs, rowNum) ->
                                                Optional.of(new RefreshToken(
                                                        rs.getLong("id"),
                                                        rs.getLong("user_id"),
                                                        rs.getString("token"),
                                                        rs.getTimestamp("expiry_date")

                                                ))
                               );

        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

    }

    public RefreshToken save(RefreshToken refreshToken) {

        try{
            jdbcTemplate.update(
                    "insert into refresh_token (user_id,token,expiry_date) values(?,?,?)",
                    refreshToken.getUserId(),refreshToken.getToken(),refreshToken.getExpiryDate());
            return refreshToken;
        }catch (DataAccessException e){
            throw new RuntimeException(e);
        }

    }

    public int deleteByToken(String token) {

        try{
            return jdbcTemplate.update(
                    "delete from refresh_token where token = ?",
                    token);
        }catch (DataAccessException e){
            throw new RuntimeException(e);
        }

    }

    public int deleteByUserId(Long userId) {

        try{
            return jdbcTemplate.update(
                    "delete from refresh_token where user_id = ?",
                    userId);
        }catch (DataAccessException e){
            throw new RuntimeException(e);
        }

    }
}
