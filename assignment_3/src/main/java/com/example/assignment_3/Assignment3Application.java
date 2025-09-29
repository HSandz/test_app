package com.example.assignment_3;

import com.example.assignment_3.entity.Role;
import com.example.assignment_3.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Assignment3Application implements CommandLineRunner {

	@Autowired
	private UserService userService;

	public static void main(String[] args) {
		SpringApplication.run(Assignment3Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// Create default admin user if not exists
		if (!userService.existsByUsername("admin")) {
			userService.registerUser("admin", "admin@example.com", "password", Role.ADMIN);
			System.out.println("Default admin user created: admin/password");
		}

		// Create default regular user if not exists
		if (!userService.existsByUsername("user")) {
			userService.registerUser("user", "user@example.com", "password", Role.USER);
			System.out.println("Default user created: user/password");
		}
	}
}
