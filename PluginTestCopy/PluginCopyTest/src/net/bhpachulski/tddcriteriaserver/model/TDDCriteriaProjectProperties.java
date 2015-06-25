package net.bhpachulski.tddcriteriaserver.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

public class TDDCriteriaProjectProperties {

	private Date created = new Date();
	private Student currentStudent;
	private List<String> sentFiles = new ArrayList<String>();
	
	public Date getCreated() {
		return created;
	}
	
	public void setCreated(Date created) {
		this.created = created;
	}
	
	public Student getCurrentStudent() {
		return currentStudent;
	}
	
	public void setCurrentStudent(Student currentStudent) {
		this.currentStudent = currentStudent;
	}
	
	public List<String> getSentFiles() {
		return sentFiles;
	}	
	
	public void setSentFiles(List<String> sentFiles) {
		this.sentFiles = sentFiles;
	}

	public void setSentFile(String sentFile) {
		if (sentFiles == null)
			sentFiles = new ArrayList<String>();
		
		if (!this.sentFiles.contains(sentFile))
			this.sentFiles.add(sentFile);
	}	
	
}
