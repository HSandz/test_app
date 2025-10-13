package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.demo.entity.Course;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Only initialize if database is empty
        if (roleRepository.count() == 0) {
            initializeRoles();
        }
        
        if (userRepository.count() == 0) {
            initializeUsers();
        }
        
        if (courseRepository.count() == 0) {
            initializeCourses();
        }
    }

    private void initializeRoles() {
        Role userRole = new Role("USER");
        Role adminRole = new Role("ADMIN");
        
        roleRepository.save(userRole);
        roleRepository.save(adminRole);
        
        System.out.println("Initialized roles in database");
    }

    private void initializeUsers() {
        Role userRole = roleRepository.findByName("USER").orElseThrow();
        Role adminRole = roleRepository.findByName("ADMIN").orElseThrow();

        // Create demo user
        User demoUser = new User("user", passwordEncoder.encode("password"), "user@example.com");
        demoUser.addRole(userRole);
        demoUser.setEnabled(true);
        userRepository.save(demoUser);

        // Create demo admin
        User demoAdmin = new User("admin", passwordEncoder.encode("password"), "admin@example.com");
        demoAdmin.addRole(userRole);
        demoAdmin.addRole(adminRole);
        demoAdmin.setEnabled(true);
        userRepository.save(demoAdmin);
        
        System.out.println("Initialized demo users in database");
    }

    private void initializeCourses() {
        Course course1 = new Course("Introduction to Spring Boot", 
                "Learn the basics of Spring Boot framework", 
                "John Doe", 40, 299.99);
        
        Course course2 = new Course("Advanced Java Programming", 
                "Deep dive into advanced Java concepts", 
                "Jane Smith", 60, 499.99);
        
        Course course3 = new Course("Web Development with React", 
                "Build modern web applications with React", 
                "Bob Johnson", 45, 399.99);
        
        Course course4 = new Course("Database Design and SQL", 
                "Learn database design principles and SQL", 
                "Alice Brown", 35, 249.99);

        courseRepository.save(course1);
        courseRepository.save(course2);
        courseRepository.save(course3);
        courseRepository.save(course4);
        
        System.out.println("Initialized demo courses in database");
    }
}