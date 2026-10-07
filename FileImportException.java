// src/edu/ccrm/exception/FileImportException.java
package edu.ccrm.exception;

public class FileImportException extends Exception {
    public FileImportException(String message) {
        super(message);
    }
    
    public FileImportException(String message, Throwable cause) {
        super(message, cause);
    }
}