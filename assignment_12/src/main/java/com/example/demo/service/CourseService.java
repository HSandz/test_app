package com.example.demo.service;

import com.example.demo.entity.Course;
import com.example.demo.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Optional<Course> getCourseById(Long id) {
        return courseRepository.findById(id);
    }

    public Course saveCourse(Course course) {
        return courseRepository.save(course);
    }

    public Course updateCourse(Course course) {
        return courseRepository.save(course);
    }

    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }

    public List<Course> searchCoursesByTitle(String title) {
        return courseRepository.findByTitleContainingIgnoreCase(title);
    }

    public List<Course> searchCoursesByInstructor(String instructor) {
        return courseRepository.findByInstructorContainingIgnoreCase(instructor);
    }

    public List<Course> findCoursesByMaxPrice(double maxPrice) {
        return courseRepository.findByPriceLessThanEqual(maxPrice);
    }

    public Course createCourse(String title, String description, String instructor, int duration, double price) {
        Course course = new Course(title, description, instructor, duration, price);
        return saveCourse(course);
    }

    public boolean existsById(Long id) {
        return courseRepository.existsById(id);
    }

    public long count() {
        return courseRepository.count();
    }
}