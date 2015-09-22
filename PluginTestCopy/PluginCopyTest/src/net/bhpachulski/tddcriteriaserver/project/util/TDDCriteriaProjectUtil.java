package net.bhpachulski.tddcriteriaserver.project.util;

import java.io.IOException;

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
				
				TDDCriteriaProjectProperties projectConfigFile = futil.getPropertiesFileAsObject(project);
				
				if (projectConfigFile.getCurrentStudent().getId() == 0) {
					Student student = findStudentId(projectConfigFile);
					projectConfigFile.setCurrentStudent(student);
					futil.updateProjectConfigFile(project, projectConfigFile);
				}
				
				return projectConfigFile;
			} else {
				TDDCriteriaProjectProperties criteriaProperties = new TDDCriteriaProjectProperties();
				Student student = findStudentId(criteriaProperties);
				criteriaProperties.setCurrentStudent(student);
				return futil.createProjectConfigFile(project, criteriaProperties);
			}
		} catch (Exception e) {
			throw new TDDCriteriaException(project);
		}				
	}

	private Student findStudentId(TDDCriteriaProjectProperties criteriaProperties) {
		Student student;				
		try {				
			student = restClient.createStudent(criteriaProperties, new Student(networkUtil.getMacAddress()));				
		} catch (Exception e) {
			//será criado projeto offline
			student = new Student(networkUtil.getMacAddress());
		}
		return student;
	}
}
