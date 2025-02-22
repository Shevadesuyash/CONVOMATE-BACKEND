package com.net.backend.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmailData {
    private String username;
    private String name;
    private String email;
    private String otp;
    private String time;
}
