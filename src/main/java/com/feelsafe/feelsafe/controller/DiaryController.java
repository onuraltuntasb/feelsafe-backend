package com.feelsafe.feelsafe.controller;

import com.feelsafe.feelsafe.model.Diary;
import com.feelsafe.feelsafe.repository.JdbcDiaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DiaryController {

    private final JdbcDiaryRepository jdbcDiaryRepository;

    @GetMapping("/hello")
    public ResponseEntity<String> getHello() {
        return new ResponseEntity<>("Hello!", HttpStatus.OK);
    }

    @PostMapping("/diary/save")
    public ResponseEntity<String> saveDiary(@RequestBody Diary diary) {

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Diary sDiary = new Diary();
        sDiary.setContent("day1");
        sDiary.setHeader("content1");
        sDiary.setCreatedAt(timestamp);
        sDiary.setUpdatedAt(timestamp);

        jdbcDiaryRepository.save(sDiary);

        return new ResponseEntity<>("Tutorial was created successfully.", HttpStatus.CREATED);
    }

}
