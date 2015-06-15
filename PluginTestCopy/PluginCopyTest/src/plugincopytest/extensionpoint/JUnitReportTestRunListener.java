package plugincopytest.extensionpoint;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.junit.TestRunListener;
import org.eclipse.jdt.junit.model.ITestCaseElement;
import org.eclipse.jdt.junit.model.ITestRunSession;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import plugincopytest.model.TestCase;
import plugincopytest.model.TestSuiteSession;

public class JUnitReportTestRunListener extends TestRunListener {

	private TestSuiteSession tss = new TestSuiteSession();
	private IProject project;
	private String folderTrack = "junitTrack";
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy_M_d_HH_mm_ss");

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

	@Override
	public void sessionFinished(ITestRunSession session) {
		tss.setFinished(new Date());

		geraArquivo();
		super.sessionFinished(session);
	}

	@Override
	public void sessionLaunched(ITestRunSession session) {
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
