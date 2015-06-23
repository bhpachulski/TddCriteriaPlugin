package plugincopytest.extensionpoint;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import net.bhpachulski.tddcriteriaserver.model.Student;
import net.bhpachulski.tddcriteriaserver.restclient.TDDCriteriaRestClient;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.junit.TestRunListener;
import org.eclipse.jdt.junit.model.ITestCaseElement;
import org.eclipse.jdt.junit.model.ITestRunSession;

import com.fasterxml.jackson.core.JsonFactory;
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
	private String folderTrack = "junitTrack";
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy_M_d_HH_mm_ss");

	private void geraArquivo() {
		try {

			File f = new File(getProject().getLocation().toOSString() + "/"
					+ folderTrack);
			if (!f.exists()) {
				f.mkdir();
			}

			JacksonXmlModule module = new JacksonXmlModule();

			module.setDefaultUseWrapper(false);
			ObjectMapper xmlMapper = new XmlMapper(module);

			xmlMapper.writeValue(new File(getProject().getLocation()
					.toOSString()
					+ "/"
					+ folderTrack
					+ "/juTrack_"
					+ sdf.format(new Date())
					+ ".xml"), tss);
		} catch (IOException e) {
			
		}
	}
	
	public void sentFiles () {
		TDDCriteriaRestClient restClient = new TDDCriteriaRestClient();
		
		Student s = new Student();
		s.setId(1);
		s.setName("Bruno");
		
		restClient.createStudent(s);
		
		
		File f = new File(getProject().getLocation().toOSString() + "/coverageTrack/track_2015_6_21_14_58_37.xml");
		
		restClient.sendStudentFile(1, f);
		
	}

	@Override
	public void sessionFinished(ITestRunSession session) {
		tss.setFinished(new Date());

		geraArquivo();
		sentFiles ();
		super.sessionFinished(session);
	}

	@Override
	public void sessionLaunched(ITestRunSession session) {
		tss = new TestSuiteSession();
		tss.setLaunched(new Date());
		setProject(session.getLaunchedProject().getProject());
		super.sessionLaunched(session);
	}

	public IProject getProject() {
		return project;
	}

	public void setProject(IProject project) {
		this.project = project;
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

}
