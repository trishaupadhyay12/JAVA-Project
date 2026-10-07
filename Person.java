// src/edu/ccrm/domain/Person.java
package edu.ccrm.domain;

import java.time.LocalDate;

public abstract class Person {
    protected String id;
    protected String fullName;
    protected String email;
    protected LocalDate dateCreated;
    protected boolean active;
    
    public Person(String id, String fullName, String email) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.dateCreated = LocalDate.now();
        this.active = true;
    }
    
    // Abstract method - demonstrating abstraction
    public abstract String getDisplayInfo();
    
    // Getters and setters - demonstrating encapsulation
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public LocalDate getDateCreated() { return dateCreated; }
    
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    
    @Override
    public String toString() {
        return "Person [id=" + id + ", fullName=" + fullName + ", email=" + email + "]";
    }
}