package com.feelsafe.feelsafe.model;

import lombok.*;

import java.sql.Timestamp;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Diary {
    private Long id;
    private Long userId;
    private String header;
    private String content;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
