package com.net.backend.model;

import lombok.Data;

@Data
public class ReviewForm {
    private String name;
    private String email;
    private String subject;
    private String message;

}