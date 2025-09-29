package com.example.assignment_3.service;

import com.example.assignment_3.entity.Blog;
import com.example.assignment_3.entity.User;
import com.example.assignment_3.repository.BlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BlogService {

    @Autowired
    private BlogRepository blogRepository;

    public List<Blog> getAllBlogs() {
        return blogRepository.findAll();
    }

    public Optional<Blog> getBlogById(Long id) {
        return blogRepository.findById(id);
    }

    public List<Blog> getBlogsByUser(User user) {
        return blogRepository.findByUser(user);
    }

    public List<Blog> getBlogsByUserId(Long userId) {
        return blogRepository.findByUserId(userId);
    }

    public List<Blog> searchBlogsByTitle(String title) {
        return blogRepository.findByTitleContainingIgnoreCase(title);
    }

    public Blog createBlog(Blog blog) {
        return blogRepository.save(blog);
    }

    public Blog updateBlog(Long id, Blog blogDetails) {
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Blog not found with id: " + id));
        
        blog.setTitle(blogDetails.getTitle());
        blog.setContent(blogDetails.getContent());
        
        return blogRepository.save(blog);
    }

    public void deleteBlog(Long id) {
        blogRepository.deleteById(id);
    }

    public boolean isOwner(Long blogId, String username) {
        Optional<Blog> blog = blogRepository.findById(blogId);
        return blog.isPresent() && blog.get().getUser().getUsername().equals(username);
    }
}