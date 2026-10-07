// src/edu/ccrm/cli/CLIMenu.java
package edu.ccrm.cli;

import edu.ccrm.domain.*;
import edu.ccrm.service.StudentService;
import edu.ccrm.service.CourseService;
import edu.ccrm.io.FileService;
import edu.ccrm.exception.DuplicateEnrollmentException;
import edu.ccrm.exception.MaxCreditLimitExceededException;
import edu.ccrm.exception.FileImportException;
import edu.ccrm.util.CourseCode;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class CLIMenu {
    private final Scanner scanner;
    private final StudentService studentService;
    private final CourseService courseService;
    private final FileService fileService;
    private List<Instructor> instructors;
    
    public CLIMenu() {
        this.scanner = new Scanner(System.in);
        this.studentService = new StudentService();
        this.courseService = new CourseService();
        this.fileService = new FileService();
        initializeSampleData();
    }
    
    private void initializeSampleData() {
        // Create some sample instructors
        instructors = List.of(
            new Instructor("I001", "Dr. Smith", "smith@university.edu", "Computer Science"),
            new Instructor("I002", "Prof. Johnson", "johnson@university.edu", "Mathematics"),
            new Instructor("I003", "Dr. Saurav prasad", "saurav.p@university.edu", "Chemistry"),
            new Instructor("I004", "Dr. Avirup das", "avirup@university.edu", "Physics"),
            new Instructor("I005", "Dr. Hemalakshmi", "hemalakshmi@university.edu", "Python"),
            new Instructor("I006", "Dr. Vinod Bhatt", "Vinod.bhatt@university.edu", "professional English")
            
        );
        
        // Create some sample courses
        try {
            Course cs101 = new Course.Builder()
                    .setCode(new CourseCode("CS-101"))
                    .setTitle("Introduction to Programming")
                    .setCredits(3)
                    .setInstructor(instructors.get(0))
                    .setSemester(Semester.FALL)
                    .setDepartment("Computer Science")
                    .build();
            
            Course math201 = new Course.Builder()
                    .setCode(new CourseCode("MTH-201"))
                    .setTitle("Calculus I")
                    .setCredits(4)
                    .setInstructor(instructors.get(1))
                    .setSemester(Semester.FALL)
                    .setDepartment("Mathematics")
                    .build();
            
            Course phys301 = new Course.Builder()
                    .setCode(new CourseCode("PHY-301"))
                    .setTitle("Classical Mechanics")
                    .setCredits(3)
                    .setInstructor(instructors.get(2))
                    .setSemester(Semester.SPRING)
                    .setDepartment("Physics")
                    .build();
            
            courseService.addCourse(cs101);
            courseService.addCourse(math201);
            courseService.addCourse(phys301);
            
        } catch (IllegalArgumentException e) {
            System.err.println("Error creating sample courses: " + e.getMessage());
        }
    }
    
    public void start() {
        int choice;
        mainMenuLoop: // Label for the main menu loop
        while (true) {
            displayMainMenu();
            choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1:
                    manageStudents();
                    break;
                case 2:
                    manageCourses();
                    break;
                case 3:
                    manageEnrollments();
                    break;
                case 4:
                    manageGrades();
                    break;
                case 5:
                    importExportData();
                    break;
                case 6:
                    backupOperations();
                    break;
                case 7:
                    generateReports();
                    break;
                case 8:
                    displayJavaPlatformInfo();
                    break;
                case 9:
                    System.out.println("Exiting...");
                    break mainMenuLoop; // Using labeled break
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
            
            if (choice != 9) {
                System.out.println("\nPress Enter to continue...");
                scanner.nextLine();
            }
        }
    }
    
    private void displayMainMenu() {
        System.out.println("\n=== MAIN MENU ===");
        System.out.println("1. Manage Students");
        System.out.println("2. Manage Courses");
        System.out.println("3. Manage Enrollments");
        System.out.println("4. Manage Grades");
        System.out.println("5. Import/Export Data");
        System.out.println("6. Backup Operations");
        System.out.println("7. Generate Reports");
        System.out.println("8. Display Java Platform Info");
        System.out.println("9. Exit");
    }
    
    private void manageStudents() {
        int choice;
        do {
            System.out.println("\n=== STUDENT MANAGEMENT ===");
            System.out.println("1. Add Student");
            System.out.println("2. List All Students");
            System.out.println("3. Search Students");
            System.out.println("4. Update Student");
            System.out.println("5. Deactivate Student");
            System.out.println("6. View Student Transcript");
            System.out.println("7. Back to Main Menu");
            
            choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1:
                    addStudent();
                    break;
                case 2:
                    listStudents();
                    break;
                case 3:
                    searchStudents();
                    break;
                case 4:
                    updateStudent();
                    break;
                case 5:
                    deactivateStudent();
                    break;
                case 6:
                    viewStudentTranscript();
                    break;
                case 7:
                    System.out.println("Returning to main menu...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 7);
    }
    
    private void addStudent() {
        System.out.println("\n--- Add New Student ---");
        String id = getStringInput("Student ID: ");
        String regNo = getStringInput("Registration Number: ");
        String fullName = getStringInput("Full Name: ");
        String email = getStringInput("Email: ");
        
        Student student = new Student(id, regNo, fullName, email);
        studentService.addStudent(student);
        System.out.println("Student added successfully!");
    }
    
    private void listStudents() {
        System.out.println("\n--- All Students ---");
        List<Student> students = studentService.getAllStudents();
        if (students.isEmpty()) {
            System.out.println("No students found.");
        } else {
            students.forEach(student -> System.out.println(student.getDisplayInfo()));
        }
    }
    
    private void searchStudents() {
        System.out.println("\n--- Search Students ---");
        String query = getStringInput("Enter search term: ");
        List<Student> results = studentService.search(query);
        
        if (results.isEmpty()) {
            System.out.println("No students found matching your search.");
        } else {
            System.out.println("Search results:");
            results.forEach(student -> System.out.println(student.getDisplayInfo()));
        }
    }
    
    private void updateStudent() {
        System.out.println("\n--- Update Student ---");
        String regNo = getStringInput("Enter student registration number: ");
        Student student = studentService.getStudentByRegNo(regNo);
        
        if (student == null) {
            System.out.println("Student not found.");
            return;
        }
        
        System.out.println("Current details: " + student.getDisplayInfo());
        String newName = getStringInput("New full name (press Enter to keep current): ");
        String newEmail = getStringInput("New email (press Enter to keep current): ");
        
        if (!newName.isEmpty()) {
            student.setFullName(newName);
        }
        if (!newEmail.isEmpty()) {
            student.setEmail(newEmail);
        }
        
        studentService.updateStudent(student);
        System.out.println("Student updated successfully!");
    }
    
    private void deactivateStudent() {
        System.out.println("\n--- Deactivate Student ---");
        String regNo = getStringInput("Enter student registration number: ");
        Student student = studentService.getStudentByRegNo(regNo);
        
        if (student == null) {
            System.out.println("Student not found.");
            return;
        }
        
        studentService.deactivateStudent(student.getId());
        System.out.println("Student deactivated successfully!");
    }
    
    private void viewStudentTranscript() {
        System.out.println("\n--- Student Transcript ---");
        String regNo = getStringInput("Enter student registration number: ");
        Student student = studentService.getStudentByRegNo(regNo);
        
        if (student == null) {
            System.out.println("Student not found.");
            return;
        }
        
        System.out.println("\n=== TRANSCRIPT ===");
        System.out.println("Student: " + student.getFullName());
        System.out.println("Reg No: " + student.getRegNo());
        System.out.println("Email: " + student.getEmail());
        System.out.println("Overall GPA: " + String.format("%.2f", student.calculateGPA()));
        System.out.println("\n--- Enrollments ---");
        
        if (student.getEnrollments().isEmpty()) {
            System.out.println("No enrollments found.");
        } else {
            student.getEnrollments().forEach(enrollment -> {
                Course course = enrollment.getCourse();
                String gradeInfo = enrollment.getGrade() != null ? 
                        "Grade: " + enrollment.getGrade() + " (" + enrollment.getGrade().getPoints() + ")" : 
                        "Grade: Not assigned";
                
                System.out.printf("%s - %s (%d credits) - %s - %s%n",
                        course.getCode(), course.getTitle(), course.getCredits(),
                        course.getSemester(), gradeInfo);
            });
        }
    }
    
    private void manageCourses() {
        int choice;
        do {
            System.out.println("\n=== COURSE MANAGEMENT ===");
            System.out.println("1. Add Course");
            System.out.println("2. List All Courses");
            System.out.println("3. Search Courses");
            System.out.println("4. Update Course");
            System.out.println("5. Deactivate Course");
            System.out.println("6. Back to Main Menu");
            
            choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1:
                    addCourse();
                    break;
                case 2:
                    listCourses();
                    break;
                case 3:
                    searchCourses();
                    break;
                case 4:
                    updateCourse();
                    break;
                case 5:
                    deactivateCourse();
                    break;
                case 6:
                    System.out.println("Returning to main menu...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 6);
    }
    
    private void addCourse() {
        System.out.println("\n--- Add New Course ---");
        
        try {
            String codeStr = getStringInput("Course Code (format: ABC-123): ");
            CourseCode code = new CourseCode(codeStr);
            String title = getStringInput("Course Title: ");
            int credits = getIntInput("Credits: ");
            
            System.out.println("Available Instructors:");
            for (int i = 0; i < instructors.size(); i++) {
                System.out.println((i + 1) + ". " + instructors.get(i).getDisplayInfo());
            }
            
            int instructorChoice = getIntInput("Select instructor (1-" + instructors.size() + "): ");
            if (instructorChoice < 1 || instructorChoice > instructors.size()) {
                System.out.println("Invalid instructor selection.");
                return;
            }
            Instructor instructor = instructors.get(instructorChoice - 1);
            
            System.out.println("Available Semesters:");
            for (int i = 0; i < Semester.values().length; i++) {
                System.out.println((i + 1) + ". " + Semester.values()[i].getDisplayName());
            }
            
            int semesterChoice = getIntInput("Select semester (1-" + Semester.values().length + "): ");
            if (semesterChoice < 1 || semesterChoice > Semester.values().length) {
                System.out.println("Invalid semester selection.");
                return;
            }
            Semester semester = Semester.values()[semesterChoice - 1];
            
            String department = getStringInput("Department: ");
            
            Course course = new Course.Builder()
                    .setCode(code)
                    .setTitle(title)
                    .setCredits(credits)
                    .setInstructor(instructor)
                    .setSemester(semester)
                    .setDepartment(department)
                    .build();
            
            courseService.addCourse(course);
            System.out.println("Course added successfully!");
            
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private void listCourses() {
        System.out.println("\n--- All Courses ---");
        List<Course> courses = courseService.getAllCourses();
        if (courses.isEmpty()) {
            System.out.println("No courses found.");
        } else {
            courses.forEach(course -> System.out.println(course.toString()));
        }
    }
    
    private void searchCourses() {
        System.out.println("\n--- Search Courses ---");
        String query = getStringInput("Enter search term: ");
        List<Course> results = courseService.search(query);
        
        if (results.isEmpty()) {
            System.out.println("No courses found matching your search.");
        } else {
            System.out.println("Search results:");
            results.forEach(course -> System.out.println(course.toString()));
        }
    }
    
    private void updateCourse() {
        System.out.println("\n--- Update Course ---");
        String codeStr = getStringInput("Enter course code: ");
        
        try {
            CourseCode code = new CourseCode(codeStr);
            Course course = courseService.getCourseByCode(code);
            
            if (course == null) {
                System.out.println("Course not found.");
                return;
            }
            
            System.out.println("Current details: " + course.toString());
            
            // For simplicity, we'll just allow changing the instructor
            System.out.println("Available Instructors:");
            for (int i = 0; i < instructors.size(); i++) {
                System.out.println((i + 1) + ". " + instructors.get(i).getDisplayInfo());
            }
            
            int instructorChoice = getIntInput("Select new instructor (1-" + instructors.size() + ", 0 to keep current): ");
            if (instructorChoice > 0 && instructorChoice <= instructors.size()) {
                Instructor newInstructor = instructors.get(instructorChoice - 1);
                course.setInstructor(newInstructor);
            }
            
            courseService.updateCourse(course);
            System.out.println("Course updated successfully!");
            
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private void deactivateCourse() {
        System.out.println("\n--- Deactivate Course ---");
        String codeStr = getStringInput("Enter course code: ");
        
        try {
            CourseCode code = new CourseCode(codeStr);
            courseService.deactivateCourse(code);
            System.out.println("Course deactivated successfully!");
            
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private void manageEnrollments() {
        int choice;
        do {
            System.out.println("\n=== ENROLLMENT MANAGEMENT ===");
            System.out.println("1. Enroll Student in Course");
            System.out.println("2. Unenroll Student from Course");
            System.out.println("3. View Student Enrollments");
            System.out.println("4. Back to Main Menu");
            
            choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1:
                    enrollStudent();
                    break;
                case 2:
                    unenrollStudent();
                    break;
                case 3:
                    viewStudentEnrollments();
                    break;
                case 4:
                    System.out.println("Returning to main menu...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 4);
    }
    
    private void enrollStudent() {
        System.out.println("\n--- Enroll Student in Course ---");
        String regNo = getStringInput("Enter student registration number: ");
        Student student = studentService.getStudentByRegNo(regNo);
        
        if (student == null) {
            System.out.println("Student not found.");
            return;
        }
        
        String codeStr = getStringInput("Enter course code: ");
        
        try {
            CourseCode code = new CourseCode(codeStr);
            Course course = courseService.getCourseByCode(code);
            
            if (course == null) {
                System.out.println("Course not found.");
                return;
            }
            
            studentService.enrollStudentInCourse(student, course);
            System.out.println("Student enrolled successfully!");
            
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (DuplicateEnrollmentException e) {
            System.out.println("Enrollment error: " + e.getMessage());
        } catch (MaxCreditLimitExceededException e) {
            System.out.println("Enrollment error: " + e.getMessage());
        }
    }
    
    private void unenrollStudent() {
        System.out.println("\n--- Unenroll Student from Course ---");
        String regNo = getStringInput("Enter student registration number: ");
        Student student = studentService.getStudentByRegNo(regNo);
        
        if (student == null) {
            System.out.println("Student not found.");
            return;
        }
        
        String codeStr = getStringInput("Enter course code: ");
        
        try {
            CourseCode code = new CourseCode(codeStr);
            Course course = courseService.getCourseByCode(code);
            
            if (course == null) {
                System.out.println("Course not found.");
                return;
            }
            
            studentService.unenrollStudentFromCourse(student, course);
            System.out.println("Student unenrolled successfully!");
            
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private void viewStudentEnrollments() {
        System.out.println("\n--- View Student Enrollments ---");
        String regNo = getStringInput("Enter student registration number: ");
        Student student = studentService.getStudentByRegNo(regNo);
        
        if (student == null) {
            System.out.println("Student not found.");
            return;
        }
        
        System.out.println("Enrollments for " + student.getFullName() + ":");
        if (student.getEnrollments().isEmpty()) {
            System.out.println("No enrollments found.");
        } else {
            student.getEnrollments().forEach(enrollment -> {
                Course course = enrollment.getCourse();
                System.out.printf("%s - %s (%d credits) - %s - Enrolled: %s%n",
                        course.getCode(), course.getTitle(), course.getCredits(),
                        course.getSemester(), enrollment.getEnrollmentDate());
            });
        }
    }
    
    private void manageGrades() {
        int choice;
        do {
            System.out.println("\n=== GRADE MANAGEMENT ===");
            System.out.println("1. Assign Grade to Student");
            System.out.println("2. View Student Grades");
            System.out.println("3. Back to Main Menu");
            
            choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1:
                    assignGrade();
                    break;
                case 2:
                    viewStudentGrades();
                    break;
                case 3:
                    System.out.println("Returning to main menu...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 3);
    }
    
    private void assignGrade() {
        System.out.println("\n--- Assign Grade to Student ---");
        String regNo = getStringInput("Enter student registration number: ");
        Student student = studentService.getStudentByRegNo(regNo);
        
        if (student == null) {
            System.out.println("Student not found.");
            return;
        }
        
        String codeStr = getStringInput("Enter course code: ");
        
        try {
            CourseCode code = new CourseCode(codeStr);
            
            // Find the enrollment
            Enrollment enrollment = student.getEnrollments().stream()
                    .filter(e -> e.getCourse().getCode().equals(code))
                    .findFirst()
                    .orElse(null);
            
            if (enrollment == null) {
                System.out.println("Student is not enrolled in this course.");
                return;
            }
            
            System.out.println("Available Grades:");
            for (int i = 0; i < Grade.values().length; i++) {
                Grade grade = Grade.values()[i];
                System.out.println((i + 1) + ". " + grade.getLetter() + " (" + grade.getPoints() + " points)");
            }
            
            int gradeChoice = getIntInput("Select grade (1-" + Grade.values().length + "): ");
            if (gradeChoice < 1 || gradeChoice > Grade.values().length) {
                System.out.println("Invalid grade selection.");
                return;
            }
            
            Grade grade = Grade.values()[gradeChoice - 1];
            enrollment.setGrade(grade);
            
            System.out.println("Grade assigned successfully!");
            
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private void viewStudentGrades() {
        // This is similar to viewStudentTranscript but focused on grades
        viewStudentTranscript();
    }
    
    private void importExportData() {
        int choice;
        do {
            System.out.println("\n=== IMPORT/EXPORT DATA ===");
            System.out.println("1. Export Students to CSV");
            System.out.println("2. Import Students from CSV");
            System.out.println("3. Export Courses to CSV");
            System.out.println("4. Import Courses from CSV");
            System.out.println("5. Back to Main Menu");
            
            choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1:
                    exportStudentsToCSV();
                    break;
                case 2:
                    importStudentsFromCSV();
                    break;
                case 3:
                    exportCoursesToCSV();
                    break;
                case 4:
                    importCoursesFromCSV();
                    break;
                case 5:
                    System.out.println("Returning to main menu...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 5);
    }
    
    private void exportStudentsToCSV() {
        try {
            String filename = getStringInput("Enter filename (e.g., students.csv): ");
            fileService.exportStudents(studentService.getAllStudents(), filename);
            System.out.println("Students exported successfully to " + filename);
        } catch (IOException e) {
            System.out.println("Error exporting students: " + e.getMessage());
        }
    }
    
    private void importStudentsFromCSV() {
        try {
            String filename = getStringInput("Enter filename (e.g., students.csv): ");
            List<Student> importedStudents = fileService.importStudents(filename);
            
            // Add imported students to the service
            for (Student student : importedStudents) {
                studentService.addStudent(student);
            }
            
            System.out.println("Successfully imported " + importedStudents.size() + " students.");
        } catch (IOException | FileImportException e) {
            System.out.println("Error importing students: " + e.getMessage());
        }
    }
    
    private void exportCoursesToCSV() {
        try {
            String filename = getStringInput("Enter filename (e.g., courses.csv): ");
            fileService.exportCourses(courseService.getAllCourses(), filename);
            System.out.println("Courses exported successfully to " + filename);
        } catch (IOException e) {
            System.out.println("Error exporting courses: " + e.getMessage());
        }
    }
    
    private void importCoursesFromCSV() {
        try {
            String filename = getStringInput("Enter filename (e.g., courses.csv): ");
            List<Course> importedCourses = fileService.importCourses(filename, instructors);
            
            // Add imported courses to the service
            for (Course course : importedCourses) {
                courseService.addCourse(course);
            }
            
            System.out.println("Successfully imported " + importedCourses.size() + " courses.");
        } catch (IOException | FileImportException e) {
            System.out.println("Error importing courses: " + e.getMessage());
        }
    }
    
    private void backupOperations() {
        int choice;
        do {
            System.out.println("\n=== BACKUP OPERATIONS ===");
            System.out.println("1. Create Backup");
            System.out.println("2. Show Backup Size");
            System.out.println("3. Back to Main Menu");
            
            choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1:
                    createBackup();
                    break;
                case 2:
                    showBackupSize();
                    break;
                case 3:
                    System.out.println("Returning to main menu...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 3);
    }
    
    private void createBackup() {
        try {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String backupName = "backup_" + timestamp;
            
            fileService.backupData(backupName);
            System.out.println("Backup created successfully: " + backupName);
            
            // Show backup size
            long size = fileService.getBackupSize(backupName);
            System.out.println("Backup size: " + size + " bytes");
            
        } catch (IOException e) {
            System.out.println("Error creating backup: " + e.getMessage());
        }
    }
    
    private void showBackupSize() {
        String backupName = getStringInput("Enter backup folder name: ");
        
        try {
            long size = fileService.getBackupSize(backupName);
            System.out.println("Backup size: " + size + " bytes");
        } catch (IOException e) {
            System.out.println("Error getting backup size: " + e.getMessage());
        }
    }
    
    private void generateReports() {
        int choice;
        do {
            System.out.println("\n=== REPORTS ===");
            System.out.println("1. GPA Distribution");
            System.out.println("2. Top Students");
            System.out.println("3. Back to Main Menu");
            
            choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1:
                    generateGPADistribution();
                    break;
                case 2:
                    generateTopStudents();
                    break;
                case 3:
                    System.out.println("Returning to main menu...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 3);
    }
    
    private void generateGPADistribution() {
        System.out.println("\n--- GPA Distribution Report ---");
        
        List<Student> students = studentService.getAllStudents();
        
        // Using Stream API to calculate GPA distribution
        long excellent = students.stream()
                .filter(s -> s.calculateGPA() >= 9.0)
                .count();
        
        long good = students.stream()
                .filter(s -> s.calculateGPA() >= 7.5 && s.calculateGPA() < 9.0)
                .count();
        
        long average = students.stream()
                .filter(s -> s.calculateGPA() >= 6.0 && s.calculateGPA() < 7.5)
                .count();
        
        long belowAverage = students.stream()
                .filter(s -> s.calculateGPA() > 0 && s.calculateGPA() < 6.0)
                .count();
        
        long noGPA = students.stream()
                .filter(s -> s.calculateGPA() == 0)
                .count();
        
        System.out.println("GPA Distribution:");
        System.out.println("Excellent (9.0+): " + excellent + " students");
        System.out.println("Good (7.5-8.9): " + good + " students");
        System.out.println("Average (6.0-7.4): " + average + " students");
        System.out.println("Below Average (0.1-5.9): " + belowAverage + " students");
        System.out.println("No GPA (0.0): " + noGPA + " students");
    }
    
    private void generateTopStudents() {
        System.out.println("\n--- Top Students Report ---");
        
        List<Student> students = studentService.getAllStudents();
        
        // Using Stream API to find top students by GPA
        List<Student> topStudents = students.stream()
                .filter(s -> s.calculateGPA() > 0)
                .sorted((s1, s2) -> Double.compare(s2.calculateGPA(), s1.calculateGPA()))
                .limit(5)
                .collect(Collectors.toList());
        
        if (topStudents.isEmpty()) {
            System.out.println("No students with GPA data available.");
        } else {
            System.out.println("Top 5 Students by GPA:");
            for (int i = 0; i < topStudents.size(); i++) {
                Student student = topStudents.get(i);
                System.out.printf("%d. %s - GPA: %.2f%n", 
                        i + 1, student.getFullName(), student.calculateGPA());
            }
        }
    }
    
    private void displayJavaPlatformInfo() {
        System.out.println("\n=== JAVA PLATFORM INFORMATION ===");
        System.out.println("Java Version: " + System.getProperty("java.version"));
        System.out.println("JVM Version: " + System.getProperty("java.vm.version"));
        System.out.println("Runtime Name: " + System.getProperty("java.runtime.name"));
        
        System.out.println("\n--- Java Platform Comparison ---");
        System.out.println("Java SE (Standard Edition):");
        System.out.println("  - Desktop and server applications");
        System.out.println("  - Core Java platform");
        System.out.println("  - Includes all basic libraries and APIs");
        
        System.out.println("\nJava EE (Enterprise Edition):");
        System.out.println("  - Enterprise-level applications");
        System.out.println("  - Web services, distributed computing");
        System.out.println("  - Built on top of Java SE");
        
        System.out.println("\nJava ME (Micro Edition):");
        System.out.println("  - Mobile and embedded devices");
        System.out.println("  - Limited resources environment");
        System.out.println("  - Subset of Java SE functionality");
        
        System.out.println("\nThis application uses Java SE (Standard Edition)");
    }
    
    // Utility methods for input handling
    private String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
    
    private int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }
}