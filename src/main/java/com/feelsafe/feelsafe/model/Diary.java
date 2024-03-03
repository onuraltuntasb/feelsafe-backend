package com.feelsafe.feelsafe.model;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Setter
@Getter
public class Diary {
    private Long id;
    private String header;
    private String content;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
