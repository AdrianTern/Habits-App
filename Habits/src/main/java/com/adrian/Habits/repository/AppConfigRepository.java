package com.adrian.Habits.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.adrian.Habits.model.AppConfig;

@Repository
public interface AppConfigRepository extends JpaRepository<AppConfig, String> {

}