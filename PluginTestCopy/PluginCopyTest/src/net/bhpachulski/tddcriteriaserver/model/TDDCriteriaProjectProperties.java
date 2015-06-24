package net.bhpachulski.tddcriteriaserver.model;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TDDCriteriaProjectProperties {

	private Date created = new Date();
	private Student currentStudent;
	private Map<String, List<String>> sentFiles = new HashMap<String, List<String>>();
	
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
	
	public Map<String, List<String>> getSentFiles() {
		return sentFiles;
	}
	
	public void setSentFiles(String ft, String sentFile) {
		if (this.sentFiles.containsKey(ft))
			this.sentFiles.get(ft).add(sentFile);
		else 
			this.sentFiles.put(ft, Arrays.asList(sentFile));
	}	
	
}
