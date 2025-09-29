package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.Course;
import com.example.demo.service.CourseService;

@Controller
@RequestMapping("/courses")
@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping
    public String listCourses(Model model, @RequestParam(required = false) String search) {
        List<Course> courses;
        
        if (search != null && !search.trim().isEmpty()) {
            courses = courseService.searchCoursesByTitle(search);
            model.addAttribute("search", search);
        } else {
            courses = courseService.getAllCourses();
        }
        
        model.addAttribute("courses", courses);
        return "courses/list";
    }

    @GetMapping("/{id}")
    public String courseDetail(@PathVariable Long id, Model model) {
        Course course = courseService.getCourseById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        model.addAttribute("course", course);
        return "courses/detail";
    }

    @GetMapping("/search")
    public String searchCourses(@RequestParam(required = false) String title,
                               @RequestParam(required = false) String instructor,
                               @RequestParam(required = false) Double maxPrice,
                               Model model) {
        List<Course> courses;
        
        if (title != null && !title.trim().isEmpty()) {
            courses = courseService.searchCoursesByTitle(title);
        } else if (instructor != null && !instructor.trim().isEmpty()) {
            courses = courseService.searchCoursesByInstructor(instructor);
        } else if (maxPrice != null && maxPrice > 0) {
            courses = courseService.findCoursesByMaxPrice(maxPrice);
        } else {
            courses = courseService.getAllCourses();
        }
        
        model.addAttribute("courses", courses);
        model.addAttribute("title", title);
        model.addAttribute("instructor", instructor);
        model.addAttribute("maxPrice", maxPrice);
        
        return "courses/search";
    }
}