package plugincopytest.extensionpoint;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import net.bhpachulski.tddcriteriaserver.file.FileUtil;
import net.bhpachulski.tddcriteriaserver.model.FileType;
import net.bhpachulski.tddcriteriaserver.model.Student;
import net.bhpachulski.tddcriteriaserver.model.TDDCriteriaProjectProperties;
import net.bhpachulski.tddcriteriaserver.restclient.TDDCriteriaRestClient;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.junit.TestRunListener;
import org.eclipse.jdt.junit.model.ITestCaseElement;
import org.eclipse.jdt.junit.model.ITestRunSession;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpMediaType;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.MultipartContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.http.json.JsonHttpContent;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.Maps;
import com.google.gson.Gson;

import plugincopytest.model.TestCase;
import plugincopytest.model.TestSuiteSession;

public class JUnitReportTestRunListener extends TestRunListener {

	private TestSuiteSession tss;
	private IProject project;
	private FileUtil futil = new FileUtil();
	
	private TDDCriteriaProjectProperties prop;
	
	public void verifyProjectProperties () {
		try {
			if (futil.projectFileExists(getProject())) {
				setProp(futil.getPropertiesFileAsObject(getProject()));
			} else {
				//macadress
				Student student = new TDDCriteriaRestClient().createStudent(new Student("TestBruno"));
				setProp(futil.createProjectConfigFile(getProject(), student));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("ECLIPSE ERROR ?");
		}				
	}

	@Override
	public void sessionFinished(ITestRunSession session) {
		tss.setFinished(new Date());

		try {			
			String trackFileName = futil.generateTrackFile(getProject(), tss);
			new TDDCriteriaRestClient().sendStudentFile(getProp().getCurrentStudent().getId(), futil.getJUnitFileAsName(getProject(), trackFileName));
			getProp().setSentFile(FileType.JUNIT.getFolder() + "/" + trackFileName); 
			
			sendEclemmaFiles(); 			
			
			futil.updateProjectConfigFile(getProject(), getProp());
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("ECLIPSE ERROR ?");
		}
		
		super.sessionFinished(session);
	}

	private void sendEclemmaFiles() {
		for (File f : futil.getAllCoverageFiles (getProject())) {
			if (!getProp().getSentFiles().contains(FileType.ECLEMMA.getFolder() + "/" + f.getName())) {
				new TDDCriteriaRestClient().sendStudentFile(getProp().getCurrentStudent().getId(), futil.getEclemmaFileAsName(getProject(), f.getName()));
				getProp().setSentFile(FileType.ECLEMMA.getFolder() + "/" + f.getName());
			}
		}
	}

	@Override
	public void sessionLaunched(ITestRunSession session) {
		tss = new TestSuiteSession();
		tss.setLaunched(new Date());
		setProject(session.getLaunchedProject().getProject());
		
		if (prop == null)
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
//		tc.setPackageName(testCaseElement.g);

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
