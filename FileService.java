// src/edu/ccrm/io/FileService.java
package edu.ccrm.io;

import edu.ccrm.config.AppConfig;
import edu.ccrm.domain.Student;
import edu.ccrm.domain.Course;
import edu.ccrm.domain.Instructor;
import edu.ccrm.domain.Semester;
import edu.ccrm.util.CourseCode;
import edu.ccrm.exception.FileImportException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileService {
    private final AppConfig config;
    
    public FileService() {
        this.config = AppConfig.getInstance();
    }
    
    public void exportStudents(List<Student> students, String filename) throws IOException {
        Path filePath = config.getDataDirectory().resolve(filename);
        List<String> lines = students.stream()
                .map(this::studentToCSV)
                .collect(Collectors.toList());
        
        Files.write(filePath, lines, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }
    
    public List<Student> importStudents(String filename) throws IOException, FileImportException {
        Path filePath = config.getDataDirectory().resolve(filename);
        if (!Files.exists(filePath)) {
            throw new FileImportException("File not found: " + filename);
        }
        
        List<Student> students = new ArrayList<>();
        List<String> lines = Files.readAllLines(filePath);
        
        for (int i = 0; i < lines.size(); i++) {
            try {
                String line = lines.get(i);
                if (line.trim().isEmpty()) continue;
                
                Student student = studentFromCSV(line);
                students.add(student);
            } catch (Exception e) {
                throw new FileImportException("Error parsing line " + (i + 1) + ": " + e.getMessage());
            }
        }
        
        return students;
    }
    
    public void exportCourses(List<Course> courses, String filename) throws IOException {
        Path filePath = config.getDataDirectory().resolve(filename);
        List<String> lines = courses.stream()
                .map(this::courseToCSV)
                .collect(Collectors.toList());
        
        Files.write(filePath, lines, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }
    
    public List<Course> importCourses(String filename, List<Instructor> instructors) throws IOException, FileImportException {
        Path filePath = config.getDataDirectory().resolve(filename);
        if (!Files.exists(filePath)) {
            throw new FileImportException("File not found: " + filename);
        }
        
        List<Course> courses = new ArrayList<>();
        List<String> lines = Files.readAllLines(filePath);
        
        for (int i = 0; i < lines.size(); i++) {
            try {
                String line = lines.get(i);
                if (line.trim().isEmpty()) continue;
                
                Course course = courseFromCSV(line, instructors);
                courses.add(course);
            } catch (Exception e) {
                throw new FileImportException("Error parsing line " + (i + 1) + ": " + e.getMessage());
            }
        }
        
        return courses;
    }
    
    private String studentToCSV(Student student) {
        return String.join(",",
                student.getId(),
                student.getRegNo(),
                student.getFullName(),
                student.getEmail(),
                String.valueOf(student.isActive()),
                student.getDateCreated().toString()
        );
    }
    
    private Student studentFromCSV(String csvLine) {
        String[] parts = csvLine.split(",");
        if (parts.length < 6) {
            throw new IllegalArgumentException("Invalid CSV format for student");
        }
        
        Student student = new Student(parts[0], parts[1], parts[2], parts[3]);
        student.setActive(Boolean.parseBoolean(parts[4]));
        // Note: dateCreated is set automatically in constructor
        return student;
    }
    
    private String courseToCSV(Course course) {
        return String.join(",",
                course.getCode().toString(),
                course.getTitle(),
                String.valueOf(course.getCredits()),
                course.getInstructor() != null ? course.getInstructor().getId() : "",
                course.getSemester().name(),
                course.getDepartment(),
                String.valueOf(course.isActive())
        );
    }
    
    private Course courseFromCSV(String csvLine, List<Instructor> instructors) {
        String[] parts = csvLine.split(",");
        if (parts.length < 7) {
            throw new IllegalArgumentException("Invalid CSV format for course");
        }
        
        CourseCode code = new CourseCode(parts[0]);
        String title = parts[1];
        int credits = Integer.parseInt(parts[2]);
        String instructorId = parts[3];
        Semester semester = Semester.valueOf(parts[4]);
        String department = parts[5];
        boolean active = Boolean.parseBoolean(parts[6]);
        
        Instructor instructor = instructors.stream()
                .filter(i -> i.getId().equals(instructorId))
                .findFirst()
                .orElse(null);
        
        Course course = new Course.Builder()
                .setCode(code)
                .setTitle(title)
                .setCredits(credits)
                .setInstructor(instructor)
                .setSemester(semester)
                .setDepartment(department)
                .build();
        
        course.setActive(active);
        return course;
    }
    
    public void backupData(String backupName) throws IOException {
        Path backupPath = config.getBackupDirectory().resolve(backupName);
        if (!Files.exists(backupPath)) {
            Files.createDirectories(backupPath);
        }
        
        // Copy all files from data directory to backup directory
        try (Stream<Path> paths = Files.walk(config.getDataDirectory())) {
            paths.filter(Files::isRegularFile)
                 .forEach(source -> {
                     try {
                         Path target = backupPath.resolve(config.getDataDirectory().relativize(source));
                         Files.createDirectories(target.getParent());
                         Files.copy(source, target, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                     } catch (IOException e) {
                         System.err.println("Error copying file: " + e.getMessage());
                     }
                 });
        }
    }
    
    public long getBackupSize(String backupName) throws IOException {
        Path backupPath = config.getBackupDirectory().resolve(backupName);
        if (!Files.exists(backupPath)) {
            return 0;
        }
        
        return calculateDirectorySize(backupPath);
    }
    
    // Recursive method to calculate directory size
    private long calculateDirectorySize(Path path) throws IOException {
        if (!Files.exists(path)) {
            return 0;
        }
        
        if (Files.isRegularFile(path)) {
            return Files.size(path);
        }
        
        try (Stream<Path> paths = Files.list(path)) {
            return paths.mapToLong(p -> {
                try {
                    if (Files.isDirectory(p)) {
                        return calculateDirectorySize(p);
                    } else {
                        return Files.size(p);
                    }
                } catch (IOException e) {
                    System.err.println("Error calculating size: " + e.getMessage());
                    return 0;
                }
            }).sum();
        }
    }
}