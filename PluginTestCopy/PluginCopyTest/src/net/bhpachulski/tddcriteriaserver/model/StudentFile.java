package net.bhpachulski.tddcriteriaserver.model;

import java.sql.Blob;
import java.util.Date;

/**
*
* @author bhpachulski
*/
public class StudentFile {
   
   private int id;
   private int studentId;
   private Blob file;
   private Date sentIn;
   
   private FileType type;

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

   public Date getSentIn() {
       return sentIn;
   }

   public void setSentIn(Date sentIn) {
       this.sentIn = sentIn;
   }

   public FileType getType() {
       return type;
   }

   public void setType(FileType type) {
       this.type = type;
   }
               
}
