package com.adrian.Habits.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.adrian.Habits.model.UserEntity;

// Repository for UserEntity
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long>{
    boolean existsByUsername(String username);
}
