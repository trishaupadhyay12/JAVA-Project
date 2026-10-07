// src/edu/ccrm/domain/Course.java
package edu.ccrm.domain;

import edu.ccrm.util.CourseCode;

public class Course {
    private CourseCode code; // Immutable value object
    private String title;
    private int credits;
    private Instructor instructor;
    private Semester semester;
    private String department;
    private boolean active;
    
    // Using Builder pattern
    private Course(Builder builder) {
        this.code = builder.code;
        this.title = builder.title;
        this.credits = builder.credits;
        this.instructor = builder.instructor;
        this.semester = builder.semester;
        this.department = builder.department;
        this.active = true;
    }
    
    // Builder class
    public static class Builder {
        private CourseCode code;
        private String title;
        private int credits;
        private Instructor instructor;
        private Semester semester;
        private String department;
        
        public Builder setCode(CourseCode code) {
            this.code = code;
            return this;
        }
        
        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }
        
        public Builder setCredits(int credits) {
            this.credits = credits;
            return this;
        }
        
        public Builder setInstructor(Instructor instructor) {
            this.instructor = instructor;
            return this;
        }
        
        public Builder setSemester(Semester semester) {
            this.semester = semester;
            return this;
        }
        
        public Builder setDepartment(String department) {
            this.department = department;
            return this;
        }
        
        public Course build() {
            return new Course(this);
        }
    }
    
    // Getters
    public CourseCode getCode() { return code; }
    public String getTitle() { return title; }
    public int getCredits() { return credits; }
    public Instructor getInstructor() { return instructor; }
    public Semester getSemester() { return semester; }
    public String getDepartment() { return department; }
    public boolean isActive() { return active; }
    
    public void setActive(boolean active) { this.active = active; }
    public void setInstructor(Instructor instructor) { this.instructor = instructor; }
    
    @Override
    public String toString() {
        return "Course [code=" + code + ", title=" + title + ", credits=" + credits + 
               ", instructor=" + (instructor != null ? instructor.getFullName() : "None") + 
               ", semester=" + semester + ", department=" + department + "]";
    }
}