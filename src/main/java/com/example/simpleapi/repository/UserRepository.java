package com.example.simpleapi.repository;

import com.example.simpleapi.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}
