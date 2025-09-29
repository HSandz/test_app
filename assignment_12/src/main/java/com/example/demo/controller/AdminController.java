package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.entity.Course;
import com.example.demo.service.UserService;
import com.example.demo.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private CourseService courseService;

    @GetMapping
    public String adminDashboard(Model model) {
        List<User> users = userService.getAllUsers();
        List<Course> courses = courseService.getAllCourses();
        
        model.addAttribute("users", users);
        model.addAttribute("courses", courses);
        model.addAttribute("totalUsers", users.size());
        model.addAttribute("totalCourses", courses.size());
        
        return "admin/dashboard";
    }

    @GetMapping("/users")
    public String manageUsers(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "admin/users";
    }

    @PostMapping("/users/{id}/enable")
    public String enableUser(@PathVariable Long id) {
        userService.enableUser(id);
        return "redirect:/admin/users";
    }

    @PostMapping("/users/{id}/disable")
    public String disableUser(@PathVariable Long id) {
        userService.disableUser(id);
        return "redirect:/admin/users";
    }

    @PostMapping("/users/{id}/delete")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/admin/users";
    }

    @GetMapping("/courses")
    public String manageCourses(Model model) {
        List<Course> courses = courseService.getAllCourses();
        model.addAttribute("courses", courses);
        return "admin/courses";
    }

    @GetMapping("/courses/new")
    public String newCourseForm(Model model) {
        model.addAttribute("course", new Course());
        return "admin/course-form";
    }

    @PostMapping("/courses")
    public String createCourse(@ModelAttribute Course course) {
        courseService.saveCourse(course);
        return "redirect:/admin/courses";
    }

    @GetMapping("/courses/{id}/edit")
    public String editCourseForm(@PathVariable Long id, Model model) {
        Course course = courseService.getCourseById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        model.addAttribute("course", course);
        return "admin/course-form";
    }

    @PostMapping("/courses/{id}")
    public String updateCourse(@PathVariable Long id, @ModelAttribute Course course) {
        course.setId(id);
        courseService.updateCourse(course);
        return "redirect:/admin/courses";
    }

    @PostMapping("/courses/{id}/delete")
    public String deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return "redirect:/admin/courses";
    }
}