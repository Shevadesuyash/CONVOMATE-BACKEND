package com.net.backend.service;

import com.net.backend.entity.User;
import com.net.backend.model.EmailData;
import com.net.backend.model.UserData;
import com.net.backend.repository.UserRepository;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OtpService otpService;

    @Autowired
    private EmailService emailService;


    // Register a new user (dummy logic for now)
    public ResponseEntity<String> registerUser(UserData userData) {
        User userDetails = userRepository.findByEmail(userData.getEmail());
        if (ObjectUtils.isEmpty(userDetails)) {
            User newUser = User.builder().username(userData.getUsername()).name(userData.getName()).email(userData.getEmail()).build();
            try {
                userRepository.save(newUser);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                return new ResponseEntity<>("Fail to save user. Contact support", HttpStatus.GATEWAY_TIMEOUT);
            }
            return new ResponseEntity<>("User successfully saved", HttpStatus.OK);
        }
        return new ResponseEntity<>("Already registered , Please Login !", HttpStatus.OK);
    }

    // Fetch user profile by username (dummy logic for now)
    public User getUserProfile(String username) {
        return userRepository.findByUsername(username);
    }


    // Handle logout logic (dummy for now)
    public String logoutUser(String username) {
        return "User " + username + " has been logged out";
    }

    public ResponseEntity<?> generateOtp(String email) throws MessagingException, UnsupportedEncodingException {
        User user = userRepository.findByEmail(email);
        if (!ObjectUtils.isEmpty(user)) {
            int otp = otpService.generateOtpForEmail(email);
            EmailData data = EmailData.builder().
                    username(user.getUsername()).
                    name(user.getName()).
                    email(email).
                    otp(String.valueOf(otp)).
                    time(String.valueOf(System.currentTimeMillis())).
                    build();
            return emailService.sendEmail(data);

        }
        return new ResponseEntity<>("User not found. Please register first !!", HttpStatus.GATEWAY_TIMEOUT);
    }
}
