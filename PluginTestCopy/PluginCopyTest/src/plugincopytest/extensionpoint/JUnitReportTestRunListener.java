package plugincopytest.extensionpoint;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.junit.TestRunListener;
import org.eclipse.jdt.junit.model.ITestCaseElement;
import org.eclipse.jdt.junit.model.ITestRunSession;

import plugincopytest.model.TestCase;
import plugincopytest.model.TestSuiteSession;
 
public class JUnitReportTestRunListener extends TestRunListener {

	private TestSuiteSession tss = new TestSuiteSession();
	private String folderTrack = "junitTrack";
	
	private void geraArquivo (String s) {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy_M_d_HH_mm_ss");
		
		try {
			
			File f = new File(tss.getProject().getLocation().toOSString() + "/" + folderTrack);
			if (!f.exists()) {
				f.mkdir();
			}	
			
			Writer writer = null;
			writer = new BufferedWriter(new OutputStreamWriter(
			    new FileOutputStream(tss.getProject().getLocation().toOSString() + "/" + folderTrack
			    + "/juTrack_" + sdf.format(new Date()) + ".txt", true), "utf-8"));
			    writer.write(s);
			    writer.close();
			  } catch (Exception ee) {
				  System.out.println("ERRO AQUI ! " + s);
		  }
	}

	@Override
	public void sessionFinished(ITestRunSession session) {
		tss.setFinished(new Date());
		
		super.sessionFinished(session);
	}

	@Override
	public void sessionLaunched(ITestRunSession session) {		
		tss.setLaunched(new Date());
		tss.setProject(session.getLaunchedProject().getProject());
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
		
		
		super.testCaseFinished(testCaseElement);
	}

	@Override
	public void testCaseStarted(ITestCaseElement testCaseElement) {
		super.testCaseStarted(testCaseElement);
	}

}
