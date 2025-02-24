package com.net.backend.service;

import com.net.backend.entity.User;
import com.net.backend.model.OtpData;
import com.net.backend.repository.UserRepository;
import com.net.backend.security.JwtTokenUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


//https://github.com/tericcabrel/blog-tutorials/blob/main/springboot-email/src/main/java/com/tericcabrel/mail/controllers/UserController.java
@Service
public class OtpService {

    private static final long EXPIRATION_TIME_MS = 5 * 60 * 1000; // 5 minutes
    private Map<String, OtpData> otpMap = new HashMap<>();

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    public static int generateOtp() {
        return (int) (Math.random() * 900000) + 100000;
    }

    public int generateOtpForEmail(String email) {
        int otp = generateOtp();
        long currentTime = System.currentTimeMillis();
        otpMap.put(email, new OtpData(otp, currentTime));
        return otp;
    }

    public ResponseEntity<?> verifyOtp(String email, int otp) {
        if (otpMap.containsKey(email)) {
            OtpData otpData = otpMap.get(email);
            if (otpData.getOtp() == otp && !isOtpExpired(otpData)) {
                otpMap.remove(email); // OTP verified, remove it

                // Fetch user details from the database
                User user = userRepository.findByEmail(email);
                if (ObjectUtils.isEmpty(user)) {
                    return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
                }

                // Authenticate the user
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(user.getUsername());
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);

                // Generate JWT token
                String jwtToken = jwtTokenUtil.generateToken(userDetails);

                // Return the JWT token to the client
                return ResponseEntity.ok().body(Map.of("token", jwtToken));
            }
        }
        return new ResponseEntity<>("Invalid or expired OTP", HttpStatus.BAD_REQUEST);
    }

    private boolean isOtpExpired(OtpData otpData) {
        return (System.currentTimeMillis() - otpData.getTimestamp()) > EXPIRATION_TIME_MS;
    }
}