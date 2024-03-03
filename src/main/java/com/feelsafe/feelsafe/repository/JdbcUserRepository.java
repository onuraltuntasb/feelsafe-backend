package com.feelsafe.feelsafe.repository;

import com.feelsafe.feelsafe.exception.ResourceNotFoundException;
import com.feelsafe.feelsafe.model.Diary;
import com.feelsafe.feelsafe.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class JdbcUserRepository {

    private final JdbcTemplate jdbcTemplate;



    public Optional<Object> getUserByRefreshToken(String refreshToken) {

        Long userId = null;

        try {
            userId =  jdbcTemplate.queryForObject(
                    "select user_id from refresh_token where refresh_token = ?",
                    new Object[]{refreshToken},
                    Long.class
                                              );

        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

        if(userId != null){
            try {
                return jdbcTemplate.queryForObject(
                        "select * from users where user_id = ?",
                        new Object[]{userId},
                        (rs, rowNum) ->
                                Optional.of(new User(
                                        rs.getLong("id"),
                                        rs.getString("email"),
                                        rs.getString("password"),
                                        rs.getTimestamp("created_at"),
                                        rs.getTimestamp("updated_at")
                                ))
                                                  );
            } catch (DataAccessException e) {
                throw new RuntimeException(e);
            }
        }else{
            throw new ResourceNotFoundException("There is no userId!");
        }

    }

    public Optional<Object> getUserByEmail(String email) {

        try {
            return jdbcTemplate.queryForObject(
                    "select * from users where email = ?",
                    new Object[]{email},
                    (rs, rowNum) ->
                            Optional.of(new User(
                                    rs.getLong("id"),
                                    rs.getString("email"),
                                    rs.getString("password"),
                                    rs.getTimestamp("created_at"),
                                    rs.getTimestamp("updated_at")
                            ))
                                              );
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

    }

    public User save(User user){
        try{
            jdbcTemplate.update("insert into users (email,password,created_at,updated_at) values(?,?,?,?)",
                                user.getEmail(),user.getPassword(),user.getCreatedAt(),user.getUpdatedAt());

            return user;
        }catch (DataAccessException e){
            throw new RuntimeException(e);
        }
    }
}
