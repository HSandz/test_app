package com.example.assignment_3.repository;

import com.example.assignment_3.entity.Blog;
import com.example.assignment_3.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {
    List<Blog> findByUser(User user);
    List<Blog> findByUserId(Long userId);
    List<Blog> findByTitleContainingIgnoreCase(String title);
}