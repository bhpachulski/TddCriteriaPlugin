package net.bhpachulski.tddcriteriaserver.model;

import java.sql.Blob;

/**
 *
 * @author bhpachulski
 */
public class StudentFile {
    
    private int id;
    private int studentId;
    private Blob file;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public Blob getFile() {
        return file;
    }

    public void setFile(Blob file) {
        this.file = file;
    }
    
    
            
}
