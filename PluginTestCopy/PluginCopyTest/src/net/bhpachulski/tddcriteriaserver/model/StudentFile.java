package net.bhpachulski.tddcriteriaserver.model;

import java.io.InputStream;
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
	private String fileName;
	private String projectName;
	private FileType type;
	
	private InputStream fileIs;

	

	public StudentFile(String fileName, FileType type) {
		this.fileName = fileName;
		this.type = type;
	}

	public StudentFile() {}

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

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public InputStream getFileIs() {
		return fileIs;
	}

	public void setFileIs(InputStream fileIs) {
		this.fileIs = fileIs;
	}
	
	public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((fileName == null) ? 0 : fileName.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StudentFile other = (StudentFile) obj;
		if (fileName == null) {
			if (other.fileName != null)
				return false;
		} else if (!fileName.equals(other.fileName))
			return false;
		if (type != other.type)
			return false;
		return true;
	}
}
