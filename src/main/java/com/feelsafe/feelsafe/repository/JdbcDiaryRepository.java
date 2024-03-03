package com.feelsafe.feelsafe.repository;

import com.feelsafe.feelsafe.model.Diary;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class JdbcDiaryRepository  {

    private final JdbcTemplate jdbcTemplate;

    public Diary save(Diary diary) {

        try {
            jdbcTemplate.update("insert into diary (header, content, created_at, updated_at) values (?,?,?,?)",
                                diary.getHeader(), diary.getContent(), diary.getCreatedAt(), diary.getUpdatedAt()
                               );
            return diary;
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

    }

    public Diary update(Diary diary) {

        try {
            jdbcTemplate.update("update diary set header = ?, content = ?, updated_at = ? where id = ?",
                                diary.getHeader(), diary.getContent(), diary.getUpdatedAt()
                               );
            return diary;
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }


    }

    public int deleteById(Long id) {

        try {
            return jdbcTemplate.update(
                    "delete diary where id = ?",
                    id
                                      );
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }


    }
}
