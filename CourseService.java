// src/edu/ccrm/service/CourseService.java
package edu.ccrm.service;

import edu.ccrm.domain.Course;
import edu.ccrm.domain.Instructor;
import edu.ccrm.domain.Semester;
import edu.ccrm.util.CourseCode;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CourseService implements Searchable<Course> {
    private List<Course> courses;
    
    public CourseService() {
        this.courses = new ArrayList<>();
    }
    
    public void addCourse(Course course) {
        courses.add(course);
    }
    
    public Course getCourseByCode(CourseCode code) {
        return courses.stream()
                .filter(c -> c.getCode().equals(code) && c.isActive())
                .findFirst()
                .orElse(null);
    }
    
    public List<Course> getAllCourses() {
        return courses.stream()
                .filter(Course::isActive)
                .collect(Collectors.toList());
    }
    
    public void updateCourse(Course course) {
        courses.removeIf(c -> c.getCode().equals(course.getCode()));
        courses.add(course);
    }
    
    public void deactivateCourse(CourseCode code) {
        Course course = getCourseByCode(code);
        if (course != null) {
            course.setActive(false);
        }
    }
    
    public List<Course> getCoursesByInstructor(Instructor instructor) {
        return courses.stream()
                .filter(c -> c.isActive() && c.getInstructor() != null && 
                             c.getInstructor().getId().equals(instructor.getId()))
                .collect(Collectors.toList());
    }
    
    public List<Course> getCoursesByDepartment(String department) {
        return courses.stream()
                .filter(c -> c.isActive() && c.getDepartment() != null && 
                             c.getDepartment().equalsIgnoreCase(department))
                .collect(Collectors.toList());
    }
    
    public List<Course> getCoursesBySemester(Semester semester) {
        return courses.stream()
                .filter(c -> c.isActive() && c.getSemester() == semester)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Course> search(String query) {
        String searchTerm = query.toLowerCase();
        return courses.stream()
                .filter(c -> c.isActive() && 
                        (c.getTitle().toLowerCase().contains(searchTerm) ||
                         c.getCode().toString().toLowerCase().contains(searchTerm) ||
                         (c.getDepartment() != null && c.getDepartment().toLowerCase().contains(searchTerm)) ||
                         (c.getInstructor() != null && c.getInstructor().getFullName().toLowerCase().contains(searchTerm))))
                .collect(Collectors.toList());
    }
    
    // Additional utility methods
    public List<Course> getCoursesByCredits(int minCredits, int maxCredits) {
        return courses.stream()
                .filter(c -> c.isActive() && c.getCredits() >= minCredits && c.getCredits() <= maxCredits)
                .collect(Collectors.toList());
    }
    
    public int getTotalActiveCourses() {
        return (int) courses.stream()
                .filter(Course::isActive)
                .count();
    }
    
    public int getTotalCoursesByDepartment(String department) {
        return (int) courses.stream()
                .filter(c -> c.isActive() && c.getDepartment() != null && 
                             c.getDepartment().equalsIgnoreCase(department))
                .count();
    }
    
    // Method to demonstrate stream operations with aggregation
    public void displayCourseStatistics() {
        long totalCourses = courses.stream().filter(Course::isActive).count();
        long csCourses = courses.stream()
                .filter(c -> c.isActive() && "Computer Science".equalsIgnoreCase(c.getDepartment()))
                .count();
        long mathCourses = courses.stream()
                .filter(c -> c.isActive() && "Mathematics".equalsIgnoreCase(c.getDepartment()))
                .count();
        
        System.out.println("Course Statistics:");
        System.out.println("Total Active Courses: " + totalCourses);
        System.out.println("Computer Science Courses: " + csCourses);
        System.out.println("Mathematics Courses: " + mathCourses);
        
        // Demonstrate stream aggregation with averaging
        double avgCredits = courses.stream()
                .filter(Course::isActive)
                .mapToInt(Course::getCredits)
                .average()
                .orElse(0.0);
        
        System.out.println("Average Credits per Course: " + String.format("%.2f", avgCredits));
    }
}