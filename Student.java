// src/edu/ccrm/domain/Student.java
package edu.ccrm.domain;

import java.util.ArrayList;
import java.util.List;

public class Student extends Person {
    private String regNo;
    private List<Enrollment> enrollments;
    
    public Student(String id, String regNo, String fullName, String email) {
        super(id, fullName, email);
        this.regNo = regNo;
        this.enrollments = new ArrayList<>();
    }
    
    @Override
    public String getDisplayInfo() {
        return "Student: " + regNo + " - " + fullName + " (" + email + ")";
    }
    
    public String getRegNo() { return regNo; }
    public void setRegNo(String regNo) { this.regNo = regNo; }
    
    public List<Enrollment> getEnrollments() { return enrollments; }
    
    public void addEnrollment(Enrollment enrollment) {
        enrollments.add(enrollment);
    }
    
    public void removeEnrollment(Enrollment enrollment) {
        enrollments.remove(enrollment);
    }
    
    public double calculateGPA() {
        if (enrollments.isEmpty()) return 0.0;
        
        double totalPoints = 0.0;
        int totalCredits = 0;
        
        for (Enrollment enrollment : enrollments) {
            if (enrollment.getGrade() != null) {
                totalPoints += enrollment.getGrade().getPoints() * enrollment.getCourse().getCredits();
                totalCredits += enrollment.getCourse().getCredits();
            }
        }
        
        return totalCredits > 0 ? totalPoints / totalCredits : 0.0;
    }
    
    @Override
    public String toString() {
        return "Student [regNo=" + regNo + ", fullName=" + fullName + ", email=" + email + 
               ", active=" + active + ", enrollments=" + enrollments.size() + "]";
    }
}