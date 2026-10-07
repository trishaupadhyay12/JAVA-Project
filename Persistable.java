// src/edu/ccrm/io/Persistable.java
package edu.ccrm.io;

public interface Persistable {
    String toCSV();
    void fromCSV(String csvLine);
}