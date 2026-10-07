// src/edu/ccrm/service/StudentService.java
package edu.ccrm.service;

import edu.ccrm.domain.Student;
import edu.ccrm.domain.Enrollment;
import edu.ccrm.domain.Course;
import edu.ccrm.exception.DuplicateEnrollmentException;
import edu.ccrm.exception.MaxCreditLimitExceededException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StudentService implements Searchable<Student> {
    private List<Student> students;
    private static final int MAX_CREDITS_PER_SEMESTER = 18;
    
    public StudentService() {
        this.students = new ArrayList<>();
    }
    
    public void addStudent(Student student) {
        students.add(student);
    }
    
    public Student getStudentById(String id) {
        return students.stream()
                .filter(s -> s.getId().equals(id) && s.isActive())
                .findFirst()
                .orElse(null);
    }
    
    public Student getStudentByRegNo(String regNo) {
        return students.stream()
                .filter(s -> s.getRegNo().equals(regNo) && s.isActive())
                .findFirst()
                .orElse(null);
    }
    
    public List<Student> getAllStudents() {
        return students.stream()
                .filter(Student::isActive)
                .collect(Collectors.toList());
    }
    
    public void updateStudent(Student student) {
        // In a real application, we would update the student in the database
        // For this demo, we'll just replace the student in the list
        students.removeIf(s -> s.getId().equals(student.getId()));
        students.add(student);
    }
    
    public void deactivateStudent(String id) {
        Student student = getStudentById(id);
        if (student != null) {
            student.setActive(false);
        }
    }
    
    public void enrollStudentInCourse(Student student, Course course) 
            throws DuplicateEnrollmentException, MaxCreditLimitExceededException {
        
        // Check if already enrolled
        boolean alreadyEnrolled = student.getEnrollments().stream()
                .anyMatch(e -> e.getCourse().getCode().equals(course.getCode()) 
                        && e.getCourse().getSemester() == course.getSemester());
        
        if (alreadyEnrolled) {
            throw new DuplicateEnrollmentException("Student is already enrolled in this course for the semester");
        }
        
        // Check credit limit
        int currentCredits = student.getEnrollments().stream()
                .filter(e -> e.getCourse().getSemester() == course.getSemester())
                .mapToInt(e -> e.getCourse().getCredits())
                .sum();
        
        if (currentCredits + course.getCredits() > MAX_CREDITS_PER_SEMESTER) {
            throw new MaxCreditLimitExceededException(
                    "Credit limit exceeded. Current: " + currentCredits + 
                    ", Attempting to add: " + course.getCredits() + 
                    ", Max: " + MAX_CREDITS_PER_SEMESTER);
        }
        
        // Create and add enrollment
        Enrollment enrollment = new Enrollment(student, course);
        student.addEnrollment(enrollment);
    }
    
    public void unenrollStudentFromCourse(Student student, Course course) {
        student.getEnrollments().removeIf(e -> 
                e.getCourse().getCode().equals(course.getCode()) && 
                e.getCourse().getSemester() == course.getSemester());
    }
    
    @Override
    public List<Student> search(String query) {
        String searchTerm = query.toLowerCase();
        return students.stream()
                .filter(s -> s.isActive() && 
                        (s.getFullName().toLowerCase().contains(searchTerm) ||
                         s.getRegNo().toLowerCase().contains(searchTerm) ||
                         s.getEmail().toLowerCase().contains(searchTerm)))
                .collect(Collectors.toList());
    }
}