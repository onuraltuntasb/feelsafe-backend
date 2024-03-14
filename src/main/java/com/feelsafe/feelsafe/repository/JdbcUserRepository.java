package com.feelsafe.feelsafe.repository;

import com.feelsafe.feelsafe.Entity.User;
import com.feelsafe.feelsafe.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class JdbcUserRepository {

    private final JdbcTemplate jdbcTemplate;

    public Optional<Object> getUserByRefreshToken(String refreshToken) {

        Long userId = null;
        String idSql = "select user_id from refresh_token where token = ?";
        try {
            userId = jdbcTemplate.queryForObject(idSql, new Object[]{refreshToken},Long.class);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

        if(userId != null){
            String sql = "select * from users where id = ?";
            try {
                return jdbcTemplate.queryForObject(sql,  new Object[]{userId},
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


    public Optional<User> getUserByEmail(String email) {
        String sql = "select * from users where email = ?";
        try {
            return jdbcTemplate.queryForObject(sql , new Object[]{email},
                    (rs, rowNum) ->
                            Optional.of(new User(
                                    rs.getLong("id"),
                                    rs.getString("email"),
                                    rs.getString("password"),
                                    rs.getTimestamp("created_at"),
                                    rs.getTimestamp("updated_at")
                            ))
                                              );
        }catch (DataAccessException e){
            return Optional.empty();
        }

    }

    public User save(User user){
        try{

            String SQL = "insert into users (email,password,created_at,updated_at) values(?,?,?,?) ";

            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(con -> {
                PreparedStatement ps = con.prepareStatement(SQL, new String[]{"id"});
                ps.setString(1, user.getEmail());
                ps.setString( 2,user.getPassword());
                ps.setTimestamp( 3,user.getCreatedAt());
                ps.setTimestamp( 4,user.getUpdatedAt());
                return ps;
            }, keyHolder);

            Number key = keyHolder.getKey();
            user.setId((long) key);
            return user;

        }catch (DataAccessException e){
            throw new RuntimeException(e);
        }
    }
}
