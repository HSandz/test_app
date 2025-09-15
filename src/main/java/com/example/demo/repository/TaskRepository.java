package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    
    // Find tasks by completion status
    List<Task> findByCompleted(Boolean completed);
    
    // Find tasks by title containing keyword (case insensitive)
    List<Task> findByTitleContainingIgnoreCase(String keyword);
}