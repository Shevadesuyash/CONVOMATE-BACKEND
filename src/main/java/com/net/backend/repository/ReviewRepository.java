package com.net.backend.repository;


import com.net.backend.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    // Fetch the latest 10 reviews in descending order by ID
    List<Review> findTop10ByOrderByIdDesc();

}