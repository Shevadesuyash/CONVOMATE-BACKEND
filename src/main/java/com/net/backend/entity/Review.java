package com.net.backend.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "subject", nullable = false)
    private String subject;

    @Column(name = "message", nullable = false, length = 1000)
    private String message;

    private LocalDateTime createdAt; // Timestamp for when the review was created

    // Pre-persist method to set the createdAt timestamp automatically
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

}