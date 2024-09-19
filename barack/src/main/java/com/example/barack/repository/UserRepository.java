package com.example.barack.repository;
import com.example.barack.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    
    //find user by username
    Optional<User> findByUsername (String username);
    
    //find user by email
    Optional<User> findByEmail (String email);
}
