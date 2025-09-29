package com.example.assignment_3.controller;

import com.example.assignment_3.dto.BlogRequest;
import com.example.assignment_3.entity.Blog;
import com.example.assignment_3.entity.User;
import com.example.assignment_3.service.BlogService;
import com.example.assignment_3.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/blogs")
public class BlogController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<Blog>> getAllBlogs() {
        List<Blog> blogs = blogService.getAllBlogs();
        return ResponseEntity.ok(blogs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Blog> getBlogById(@PathVariable Long id) {
        Optional<Blog> blog = blogService.getBlogById(id);
        return blog.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Blog>> getBlogsByUserId(@PathVariable Long userId) {
        List<Blog> blogs = blogService.getBlogsByUserId(userId);
        return ResponseEntity.ok(blogs);
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<Blog>> getMyBlogs() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        
        Optional<User> user = userService.getUserByUsername(username);
        if (user.isPresent()) {
            List<Blog> blogs = blogService.getBlogsByUser(user.get());
            return ResponseEntity.ok(blogs);
        }
        
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Blog> createBlog(@Valid @RequestBody BlogRequest blogRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        
        Optional<User> user = userService.getUserByUsername(username);
        if (user.isPresent()) {
            Blog blog = new Blog(blogRequest.getTitle(), blogRequest.getContent(), user.get());
            Blog savedBlog = blogService.createBlog(blog);
            return ResponseEntity.ok(savedBlog);
        }
        
        return ResponseEntity.badRequest().build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Blog> updateBlog(@PathVariable Long id, @Valid @RequestBody BlogRequest blogRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User currentUser = (User) authentication.getPrincipal();
        
        // Check if user is admin or owner of the blog
        boolean isAdmin = currentUser.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        
        boolean isOwner = blogService.isOwner(id, username);
        
        if (!isAdmin && !isOwner) {
            return ResponseEntity.status(403).build(); // Forbidden
        }

        try {
            Blog blogDetails = new Blog();
            blogDetails.setTitle(blogRequest.getTitle());
            blogDetails.setContent(blogRequest.getContent());
            
            Blog updatedBlog = blogService.updateBlog(id, blogDetails);
            return ResponseEntity.ok(updatedBlog);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> deleteBlog(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User currentUser = (User) authentication.getPrincipal();
        
        // Check if user is admin or owner of the blog
        boolean isAdmin = currentUser.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        
        boolean isOwner = blogService.isOwner(id, username);
        
        if (!isAdmin && !isOwner) {
            return ResponseEntity.status(403).build(); // Forbidden
        }

        try {
            blogService.deleteBlog(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}