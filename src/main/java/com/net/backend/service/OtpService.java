package com.net.backend.service;

import com.net.backend.model.OtpData;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


//https://github.com/tericcabrel/blog-tutorials/blob/main/springboot-email/src/main/java/com/tericcabrel/mail/controllers/UserController.java
@Service
public class OtpService {
    private static final long EXPIRATION_TIME_MS = 5 * 60 * 1000; // 5 minutes
    private Map<String, OtpData> otpMap = new HashMap<>();

    public static int generateOtp() {
        return (int) (Math.random() * 900000) + 100000;
    }

    public String generateOtpForEmail(String email) {
        int otp = generateOtp();
        long currentTime = System.currentTimeMillis();
        otpMap.put(email, new OtpData(otp, currentTime));
        return "Your OTP for email verification is: " + otp;
    }

    public boolean verifyOtp(String email, int otp) {
        if (otpMap.containsKey(email)) {
            OtpData otpData = otpMap.get(email);
            if (otpData.getOtp() == otp && !isOtpExpired(otpData)) {
                otpMap.remove(email); // OTP verified, remove it
                return true;
            }
        }
        return false;
    }

    public void removeOtp(String email) {
        otpMap.remove(email);
    }

    public int getOtp(String email) {
        return otpMap.getOrDefault(email, new OtpData(-1, 0)).getOtp();
    }

    public boolean isOtpExpired(String email, int otpValidityDuration) {
        OtpData otpData = otpMap.get(email);
        if (otpData != null) {
            long expirationTime = otpData.getTimestamp() + otpValidityDuration * 60000;
            return System.currentTimeMillis() > expirationTime;
        }
        return true;
    }

    private boolean isOtpExpired(OtpData otpData) {
        return (System.currentTimeMillis() - otpData.getTimestamp()) > EXPIRATION_TIME_MS;
    }

    public String sendOtpToMail(String email, String message) {
        // Implement sending OTP via email
        return "OTP sent successfully!";
    }


}
