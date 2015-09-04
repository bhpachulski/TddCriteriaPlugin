package net.bhpachulski.tddcriteriaserver.extensionpoint;

import java.io.File;
import java.util.Date;

import net.bhpachulski.tddcriteriaserver.exception.TDDCriteriaException;
import net.bhpachulski.tddcriteriaserver.file.FileUtil;
import net.bhpachulski.tddcriteriaserver.model.FileType;
import net.bhpachulski.tddcriteriaserver.model.Student;
import net.bhpachulski.tddcriteriaserver.model.StudentFile;
import net.bhpachulski.tddcriteriaserver.model.TDDCriteriaProjectProperties;
import net.bhpachulski.tddcriteriaserver.model.TestCase;
import net.bhpachulski.tddcriteriaserver.model.TestSuiteSession;
import net.bhpachulski.tddcriteriaserver.network.util.TDDCriteriaNetworkUtil;
import net.bhpachulski.tddcriteriaserver.restclient.TDDCriteriaRestClient;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.junit.TestRunListener;
import org.eclipse.jdt.junit.model.ITestCaseElement;
import org.eclipse.jdt.junit.model.ITestRunSession;

public class JUnitReportTestRunListener extends TestRunListener {

	private TestSuiteSession tss;
	private IProject project;
	private FileUtil futil = new FileUtil();
	private TDDCriteriaNetworkUtil networkUtil = new TDDCriteriaNetworkUtil();
	private TDDCriteriaRestClient restClient = new TDDCriteriaRestClient();
	
	private TDDCriteriaProjectProperties prop;
	
	public void verifyProjectProperties () {
		try {
			if (futil.projectFileExists(getProject())) {
				setProp(futil.getPropertiesFileAsObject(getProject()));
			} else {
				Student student = restClient.createStudent(new Student(networkUtil.getMacAddress()));
				setProp(futil.createProjectConfigFile(getProject(), student));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new TDDCriteriaException(getProject());
		}				
	}

	@Override
	public void sessionFinished(ITestRunSession session) {
		tss.setFinished(new Date());

		try {			
			futil.generateJUnitTrackFile(getProject(), tss);
			futil.generateSrcTrackFile(getProject());
			
			Thread.sleep(250);
			
			sendFiles(FileType.JUNIT);
			sendFiles(FileType.ECLEMMA);
			sendFiles(FileType.SRC);
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new TDDCriteriaException(getProject());
		} finally {
			futil.updateProjectConfigFile(getProject(), getProp());
		}
		
		super.sessionFinished(session);
	}
	
	private void sendFiles(FileType ft) {
		for (File f : futil.getAllFiles (ft, getProject())) {
			if (!getProp().getSentFiles().contains(new StudentFile(f.getName(), ft))) {				
				StudentFile sf = restClient.sendStudentFile(getProp().getCurrentStudent().getId(), getProject().getName(), futil.getFileAsName(ft, getProject(), f.getName()), ft);			
				
				getProp().setSentFile(sf);
			}
		}
	}

	@Override
	public void sessionLaunched(ITestRunSession session) {
		tss = new TestSuiteSession();
		tss.setLaunched(new Date());
		setProject(session.getLaunchedProject().getProject());
		
		verifyProjectProperties ();
		
		super.sessionLaunched(session);
	}

	@Override
	public void sessionStarted(ITestRunSession session) {
		super.sessionStarted(session);
	}

	@Override
	public void testCaseFinished(ITestCaseElement testCaseElement) {
		TestCase tc = new TestCase();
		tc.setClassName(testCaseElement.getTestClassName());
		tc.setMethodName(testCaseElement.getTestMethodName());

		if (testCaseElement.getFailureTrace() != null) {
			tc.setFailDetail(testCaseElement.getFailureTrace());
			tc.setFailed(true);
		}
		
		tss.setTestCases(tc);
		super.testCaseFinished(testCaseElement);
	}

	@Override
	public void testCaseStarted(ITestCaseElement testCaseElement) {
		super.testCaseStarted(testCaseElement);
	}

	public TDDCriteriaProjectProperties getProp() {
		return prop;
	}

	public void setProp(TDDCriteriaProjectProperties prop) {
		this.prop = prop;
	}
	
	public IProject getProject() {
		return project;
	}

	public void setProject(IProject project) {
		this.project = project;
	}

}
