package net.bhpachulski.tddcriteriaserver.exception;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.eclipse.core.resources.IProject;

import net.bhpachulski.tddcriteriaserver.file.FileUtil;

public class TDDCriteriaException extends RuntimeException {
	
	private FileUtil fu = new FileUtil();

	public TDDCriteriaException(IProject p) {
		super();
		
		StringWriter sw = new StringWriter();
		this.printStackTrace(new PrintWriter(sw));
		String exceptionAsString = sw.toString();
		
		fu.createTxtFile(p, exceptionAsString);
	}	
	
	
	
}
