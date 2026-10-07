// src/edu/ccrm/domain/Enrollment.java
package edu.ccrm.domain;

import java.time.LocalDate;

public class Enrollment {
    private Student student;
    private Course course;
    private LocalDate enrollmentDate;
    private Grade grade;
    
    public Enrollment(Student student, Course course) {
        this.student = student;
        this.course = course;
        this.enrollmentDate = LocalDate.now();
        this.grade = null;
    }
    
    public Student getStudent() { return student; }
    public Course getCourse() { return course; }
    public LocalDate getEnrollmentDate() { return enrollmentDate; }
    public Grade getGrade() { return grade; }
    
    public void setGrade(Grade grade) { this.grade = grade; }
    
    @Override
    public String toString() {
        return "Enrollment [student=" + student.getRegNo() + ", course=" + course.getCode() + 
               ", enrollmentDate=" + enrollmentDate + ", grade=" + grade + "]";
    }
}