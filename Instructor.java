// src/edu/ccrm/domain/Instructor.java
package edu.ccrm.domain;

public class Instructor extends Person {
    private String department;
    
    public Instructor(String id, String fullName, String email, String department) {
        super(id, fullName, email);
        this.department = department;
    }
    
    @Override
    public String getDisplayInfo() {
        return "Instructor: " + fullName + " - " + department + " (" + email + ")";
    }
    
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    
    @Override
    public String toString() {
        return "Instructor [id=" + id + ", fullName=" + fullName + ", email=" + email + 
               ", department=" + department + "]";
    }
}