package com.feelsafe.feelsafe.repository;

import com.feelsafe.feelsafe.Entity.User;
import com.feelsafe.feelsafe.exception.ResourceNotFoundException;
import com.feelsafe.feelsafe.model.Diary;
import com.feelsafe.feelsafe.service.CryptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcDiaryRepository {

    private final JdbcTemplate jdbcTemplate;
    private final CryptionService cryptionService;
    private final JdbcUserRepository jdbcUserRepository;

    public Optional<Diary> save(Diary diary) {

        String sql = "insert into diary (user_id, header, content, created_at, updated_at)" +
                " values (?,?,?,?,?)";
        try {
            jdbcTemplate.update(sql,
                                diary.getId(), diary.getHeader(), diary.getContent(), diary.getCreatedAt(),
                                diary.getUpdatedAt()
                               );
            return Optional.of(diary);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public Diary update(Diary diary) {

        String sql = "update diary set header = ?, content = ?, updated_at = ? where id = ? and user_id = ?";

        try {
            jdbcTemplate.update(sql,
                                diary.getHeader(), diary.getContent(), diary.getUpdatedAt(),
                                diary.getId(),diary.getUserId()
                               );
            return diary;
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public int deleteByIdAndUserId(Long id,Long userId) {
        String sql = "delete from diary where id = ? and user_id = ?";
        try {
            return jdbcTemplate.update(sql, id, userId);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Diary> getByUserIdAndId(Long userId,Long id) {
        String sql = "select * from diary where user_id = ? and id = ?";
        try {
            return jdbcTemplate.queryForObject(sql,
                                               new Object[]{userId,id},
                                               (rs, rowNum) ->
                                                       Optional.of(new Diary(
                                                               rs.getLong("id"),
                                                               rs.getLong("user_id"),
                                                               rs.getString("header"),
                                                               rs.getString("content"),
                                                               rs.getTimestamp("created_at"),
                                                               rs.getTimestamp("updated_at")
                                                       ))
                                              );
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

    }


    public List<Diary> getAllWithUserId(Long userId, String rt) {

        User user = (User) jdbcUserRepository.getUserByRefreshToken(rt)
        .orElseThrow(() -> new ResourceNotFoundException("User not found with this rt : " + rt));

        String sql = "select * from diary where user_id=?";

        try {
            return jdbcTemplate.query(sql,
                    new Object[]{userId},
                    (rs, rowNum) -> {
                        String header = null;
                        String content = null;
                        header = cryptionService.decrypt(rs.getString("header"), user.getPassword());
                        content = cryptionService.decrypt(rs.getString("content"), user.getPassword());
                        return new Diary(
                                rs.getLong("id"),
                                rs.getLong("user_id"),
                                header,
                                content,
                                rs.getTimestamp("created_at"),
                                rs.getTimestamp("updated_at")
                        );
                    }
                                     );
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
