package net.bhpachulski.tddcriteriaserver.extensionpoint;

import java.io.File;
import java.util.Date;
import java.util.Map;

import net.bhpachulski.tddcriteriaserver.exception.TDDCriteriaException;
import net.bhpachulski.tddcriteriaserver.file.FileUtil;
import net.bhpachulski.tddcriteriaserver.model.FileType;
import net.bhpachulski.tddcriteriaserver.model.Student;
import net.bhpachulski.tddcriteriaserver.model.StudentFile;
import net.bhpachulski.tddcriteriaserver.model.TDDCriteriaProjectProperties;
import net.bhpachulski.tddcriteriaserver.model.TDDStage;
import net.bhpachulski.tddcriteriaserver.model.TestCase;
import net.bhpachulski.tddcriteriaserver.model.TestSuiteSession;
import net.bhpachulski.tddcriteriaserver.network.util.TDDCriteriaNetworkUtil;
import net.bhpachulski.tddcriteriaserver.project.util.TDDCriteriaProjectUtil;
import net.bhpachulski.tddcriteriaserver.restclient.TDDCriteriaRestClient;

import org.apache.commons.io.FilenameUtils;
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
	private TDDCriteriaProjectUtil projectUtil = new TDDCriteriaProjectUtil();
	
	private TDDCriteriaProjectProperties tddCriteriaPropertiesFile;

	@Override
	public void sessionFinished(ITestRunSession session) {
		tss.setFinished(new Date());

		try {			
			futil.generateJUnitTrackFile(getProject(), tss);
			futil.generateSrcTrackFile(getProject());

			// só envia para o servidor se o aluno foi registrado, ou seja, seu id está definido
			if (getCriteriaProjectPropertiesFile().getCurrentStudent().getId() != 0) {
				Thread.sleep(1000);
				
				Map<String, TDDStage> tddProjectStages = futil.readTDDStagesFile(getProject());
			
				sendFiles(FileType.JUNIT, tddProjectStages);			
				sendFiles(FileType.SRC, tddProjectStages);
				sendFiles(FileType.ECLEMMA, tddProjectStages);
			}
			
		} catch (Exception e) {
			throw new TDDCriteriaException(getProject());
		} finally {
			futil.updateProjectConfigFile(getProject(), 
					getCriteriaProjectPropertiesFile());
		}
		
		super.sessionFinished(session);
	}
	
	private void sendFiles(FileType ft, Map<String, TDDStage> tddProjectStages) {
		for (File f : futil.getAllFiles (ft, getProject())) {
			if (!getCriteriaProjectPropertiesFile().getSentFiles().contains(new StudentFile(f.getName(), ft))) {

				//ignorando os décimos de segundo do arquivo
				TDDStage fileTDDStage = tddProjectStages.get(FilenameUtils.getBaseName(f.getName().substring(0, f.getName().length() - 1)));
				fileTDDStage = (fileTDDStage == null) ? TDDStage.NONE : fileTDDStage;
				
				StudentFile sf = restClient.sendStudentFile(getCriteriaProjectPropertiesFile(), 
						getProject().getName(), futil.getFileAsName(ft, getProject(), f.getName()), ft, fileTDDStage);			
				
				getCriteriaProjectPropertiesFile().setSentFile(sf);
			}
		}
	}

	@Override
	public void sessionLaunched(ITestRunSession session) {
		tss = new TestSuiteSession();
		tss.setLaunched(new Date());
		setProject(session.getLaunchedProject().getProject());
		
		setProjectProperties(projectUtil.verifyProjectProperties (getProject()));
		
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

	public TDDCriteriaProjectProperties getCriteriaProjectPropertiesFile() {
		return tddCriteriaPropertiesFile;
	}

	public void setProjectProperties(TDDCriteriaProjectProperties prop) {
		this.tddCriteriaPropertiesFile = prop;
	}
	
	public IProject getProject() {
		return project;
	}

	public void setProject(IProject project) {
		this.project = project;
	}

}
