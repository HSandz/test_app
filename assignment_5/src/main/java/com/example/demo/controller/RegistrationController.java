package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.service.UserService;

@Controller
public class RegistrationController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user, 
                              @RequestParam("confirmPassword") String confirmPassword,
                              BindingResult result, 
                              Model model,
                              RedirectAttributes redirectAttributes) {
        
        // Validate form data
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            model.addAttribute("error", "Username is required");
            return "register";
        }
        
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            model.addAttribute("error", "Password is required");
            return "register";
        }
        
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            model.addAttribute("error", "Email is required");
            return "register";
        }
        
        if (!user.getPassword().equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match");
            return "register";
        }
        
        if (user.getPassword().length() < 6) {
            model.addAttribute("error", "Password must be at least 6 characters long");
            return "register";
        }

        // Check if username already exists
        if (userService.existsByUsername(user.getUsername())) {
            model.addAttribute("error", "Username already exists");
            return "register";
        }

        // Check if email already exists
        if (userService.existsByEmail(user.getEmail())) {
            model.addAttribute("error", "Email already exists");
            return "register";
        }

        try {
            // Get USER role (default role for new registrations)
            Role userRole = roleRepository.findByName("USER")
                    .orElseThrow(() -> new RuntimeException("USER role not found"));
            
            // Create new user with USER role
            User newUser = new User(user.getUsername(), user.getPassword(), user.getEmail());
            newUser.addRole(userRole);
            newUser.setEnabled(true);
            
            userService.saveUser(newUser);
            
            redirectAttributes.addFlashAttribute("success", "Registration successful! You can now login.");
            return "redirect:/login";
            
        } catch (Exception e) {
            model.addAttribute("error", "Registration failed. Please try again.");
            return "register";
        }
    }
}