package com.net.backend.model;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OtpData {
    private int otp;
    private long timestamp;
}