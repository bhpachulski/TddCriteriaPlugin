package net.bhpachulski.tddcriteriaserver.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.core.resources.IProject;

@XmlRootElement
public class TestSuiteSession implements Serializable {
	
	@XmlElement()
	private Date launched;
	
	@XmlElement()
	private Date finished;
	
	@XmlElement()
	private String testRunName;
	
	private List<TestCase> testCases = new ArrayList<TestCase>();

	public Date getLaunched() {
		return launched;
	}

	public void setLaunched(Date launched) {
		this.launched = launched;
	}

	public Date getFinished() {
		return finished;
	}

	public void setFinished(Date finished) {
		this.finished = finished;
	}

	public String getTestRunName() {
		return testRunName;
	}

	public void setTestRunName(String testRunName) {
		this.testRunName = testRunName;
	}

	public List<TestCase> getTestCases() {
		return testCases;
	}

	public void setTestCases(List<TestCase> testCases) {
		this.testCases = testCases;
	}
	
	public void setTestCases(TestCase testCase) {
		this.testCases.add(testCase);
	}
}
