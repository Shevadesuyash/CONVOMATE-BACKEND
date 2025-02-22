package com.net.backend.repository;

import com.net.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.username = :username")
    Collection<User> findAllByUsername(@Param("username") String username);

    User findByEmail(String email);

    User findByUsername(String username);
}
