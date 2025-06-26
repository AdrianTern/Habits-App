package com.adrian.Habits.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.adrian.Habits.model.UserEntity;

// Repository for UserEntity
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long>{
    // Find by username
    Optional<UserEntity> findByUsername(String username);

    boolean existsByUsername(String username);
}
