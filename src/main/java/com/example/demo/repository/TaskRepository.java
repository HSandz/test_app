package com.example.demo.repository;

import com.example.demo.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    
    // Find tasks by completion status
    List<Task> findByCompleted(Boolean completed);
    
    // Find tasks by title containing keyword (case insensitive)
    List<Task> findByTitleContainingIgnoreCase(String keyword);
}