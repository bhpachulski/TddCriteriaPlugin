package net.bhpachulski.tddcriteriaserver.project.util;

import org.eclipse.core.resources.IProject;

import net.bhpachulski.tddcriteriaserver.exception.TDDCriteriaException;
import net.bhpachulski.tddcriteriaserver.file.FileUtil;
import net.bhpachulski.tddcriteriaserver.model.Student;
import net.bhpachulski.tddcriteriaserver.model.TDDCriteriaProjectProperties;
import net.bhpachulski.tddcriteriaserver.network.util.TDDCriteriaNetworkUtil;
import net.bhpachulski.tddcriteriaserver.restclient.TDDCriteriaRestClient;

public class TDDCriteriaProjectUtil {
	
	private FileUtil futil = new FileUtil();
	private TDDCriteriaRestClient restClient = new TDDCriteriaRestClient();
	private TDDCriteriaNetworkUtil networkUtil = new TDDCriteriaNetworkUtil();
	
	public TDDCriteriaProjectProperties verifyProjectProperties (IProject project) {
		try {
			if (futil.projectFileExists(project)) {
				return futil.getPropertiesFileAsObject(project);
			} else {
				Student student = restClient.createStudent(new Student(networkUtil.getMacAddress()));
				return futil.createProjectConfigFile(project, student);
			}
		} catch (Exception e) {
			throw new TDDCriteriaException(project);
		}				
	}
}
