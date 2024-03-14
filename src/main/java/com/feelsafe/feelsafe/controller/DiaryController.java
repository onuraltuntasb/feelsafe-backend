package com.feelsafe.feelsafe.controller;

import com.feelsafe.feelsafe.Entity.User;
import com.feelsafe.feelsafe.exception.ResourceNotFoundException;
import com.feelsafe.feelsafe.model.Diary;
import com.feelsafe.feelsafe.repository.JdbcDiaryRepository;
import com.feelsafe.feelsafe.repository.JdbcUserRepository;
import com.feelsafe.feelsafe.service.CryptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DiaryController {

    private final JdbcDiaryRepository jdbcDiaryRepository;
    private final JdbcUserRepository jdbcUserRepository;
    private final CryptionService cryptionService;

    @GetMapping("/hello")
    public ResponseEntity<?> hello(){
        return ResponseEntity.ok("hello");
    }

    @PostMapping("/diary/save")
    public ResponseEntity<?> saveDiary(@RequestBody Diary diary, @CookieValue(name = "feelsafe-rt-cookie",
            defaultValue = "emptyOrNull") String feelsafeRtCookie)
           {

        String rt = feelsafeRtCookie.substring(0, feelsafeRtCookie.indexOf("email"));

        User user = (User) jdbcUserRepository.getUserByRefreshToken(rt)
        .orElseThrow(() -> new ResourceNotFoundException("User not found with this refresh token : " + rt));


        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Diary sDiary = new Diary();
        sDiary.setId(user.getId());
        sDiary.setHeader(cryptionService.encrypt(diary.getHeader(),user.getPassword()));
        sDiary.setContent(cryptionService.encrypt(diary.getContent(),user.getPassword()));
        sDiary.setCreatedAt(timestamp);
        sDiary.setUpdatedAt(timestamp);

        jdbcDiaryRepository.save(sDiary);

        return ResponseEntity.ok(diary);
    }

    @PutMapping("/diary/update")
    public ResponseEntity<?> updateDiary(@RequestBody Diary diary, @CookieValue(name = "feelsafe-rt-cookie",
            defaultValue = "emptyOrNull") String feelsafeRtCookie)
            {

        String rt = feelsafeRtCookie.substring(0, feelsafeRtCookie.indexOf("email"));

        User user = (User) jdbcUserRepository.getUserByRefreshToken(rt)
        .orElseThrow(() -> new ResourceNotFoundException("User not found with this refresh token : " + rt));

        Diary gDiary = jdbcDiaryRepository.getByUserIdAndId(user.getId(), diary.getId())
       .orElseThrow(() -> new ResourceNotFoundException("Diary not found with this userId and id : "));

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        gDiary.setUpdatedAt(timestamp);
        gDiary.setId(diary.getId());
        gDiary.setHeader(cryptionService.encrypt(diary.getHeader(),user.getPassword()));
        gDiary.setContent(cryptionService.encrypt(diary.getContent(),user.getPassword()));

        jdbcDiaryRepository.update(gDiary);

        return ResponseEntity.ok(diary);
    }

    @DeleteMapping("/diary/delete")
    public ResponseEntity<?> deleteDiary(@RequestBody Diary diary, @CookieValue(name = "feelsafe-rt-cookie",
            defaultValue = "emptyOrNull") String feelsafeRtCookie)
    {

        String rt = feelsafeRtCookie.substring(0, feelsafeRtCookie.indexOf("email"));

        User user = (User) jdbcUserRepository.getUserByRefreshToken(rt)
        .orElseThrow(() -> new ResourceNotFoundException("User not found with this refresh token : " + rt));

        jdbcDiaryRepository.deleteByIdAndUserId(diary.getId(),user.getId());

        return ResponseEntity.ok(diary);
    }

    @GetMapping("/diary/get-all-users")
    public ResponseEntity<?> getAllWithUserId(@CookieValue(name = "feelsafe-rt-cookie",
            defaultValue = "emptyOrNull") String feelsafeRtCookie){

        String rt = feelsafeRtCookie.substring(0, feelsafeRtCookie.indexOf("email"));

        User user = (User) jdbcUserRepository.getUserByRefreshToken(rt)
        .orElseThrow(() -> new ResourceNotFoundException("User not found with this refresh token : " + rt));

        List<Diary> diaryList = jdbcDiaryRepository.getAllWithUserId(user.getId(),rt);

        return ResponseEntity.ok(diaryList);
    }

}
