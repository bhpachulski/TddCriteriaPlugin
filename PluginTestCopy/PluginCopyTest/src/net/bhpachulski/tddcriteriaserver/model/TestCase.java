package net.bhpachulski.tddcriteriaserver.model;

import org.eclipse.jdt.junit.model.ITestElement.FailureTrace;

public class TestCase {
	
	private String className;
	private String methodName;
	private String packageName;
	
	private boolean failed = false;
	
	private FailureTrace failDetail;
	
	

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public boolean isFailed() {
		return failed;
	}

	public void setFailed(boolean failed) {
		this.failed = failed;
	}

	public FailureTrace getFailDetail() {
		return failDetail;
	}

	public void setFailDetail(FailureTrace failDetail) {
		this.failDetail = failDetail;
	}
	
	

}
