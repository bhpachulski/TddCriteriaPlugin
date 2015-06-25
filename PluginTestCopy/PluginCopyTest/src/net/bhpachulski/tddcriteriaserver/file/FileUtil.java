package net.bhpachulski.tddcriteriaserver.file;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.bhpachulski.tddcriteriaserver.model.FileType;
import net.bhpachulski.tddcriteriaserver.model.Student;
import net.bhpachulski.tddcriteriaserver.model.TDDCriteriaProjectProperties;
import net.bhpachulski.tddcriteriaserver.restclient.TDDCriteriaRestClient;

import org.apache.commons.io.FileUtils;
import org.eclipse.core.resources.IProject;

import plugincopytest.model.TestSuiteSession;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class FileUtil {

	private JacksonXmlModule module;
	private ObjectMapper xmlMapper;
	
	private static final String TDD_CRITERIA_CONFIG_FOLDER = "tddCriteria"; 
	private static final String TDD_CRITERIA_CONFIG_FILE = "tddCriteriaProjectProperties";
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy_M_d_HH_mm_ss");	

	public FileUtil() {
		module = new JacksonXmlModule();
		module.setDefaultUseWrapper(false);
		xmlMapper = new XmlMapper(module);
	}

	public TDDCriteriaProjectProperties createProjectConfigFile(IProject p,
			Student student) {
		try {			
			createFolderIfNotExists(p.getLocation().toOSString() + "/" + TDD_CRITERIA_CONFIG_FOLDER);
			createFolderIfNotExists(p.getLocation().toOSString() + "/" + TDD_CRITERIA_CONFIG_FOLDER + "/" + FileType.JUNIT.getFolder());
			createFolderIfNotExists(p.getLocation().toOSString() + "/" + TDD_CRITERIA_CONFIG_FOLDER + "/" + FileType.ECLEMMA.getFolder());

			TDDCriteriaProjectProperties prop = new TDDCriteriaProjectProperties();
			prop.setCurrentStudent(student);

			xmlMapper.writeValue(new File(getTDDCriteriaConfigFilePath(p)), prop);

			return prop;
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("ECLIPSE ERROR ?");
		}
	}
	
	public void updateProjectConfigFile(IProject p,
			TDDCriteriaProjectProperties projectConfigFile) {
		try {			
			File arquivoAntigo = new File(getTDDCriteriaConfigFilePath(p));
			
			if (arquivoAntigo.isFile())
				arquivoAntigo.delete();
			
			xmlMapper.writeValue(new File(getTDDCriteriaConfigFilePath(p)), projectConfigFile);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("ECLIPSE ERROR ?");
		}
	}

	public boolean projectFileExists(IProject p) {
		File f = new File(getTDDCriteriaConfigFilePath(p));

		return f.isFile();
	}

	public TDDCriteriaProjectProperties getPropertiesFileAsObject(IProject p) 
			throws JsonParseException, JsonMappingException, IOException {
		File f = new File(getTDDCriteriaConfigFilePath(p));
		return xmlMapper.readValue(f, TDDCriteriaProjectProperties.class);
	}
	
	public void createFolderIfNotExists (String path) throws IOException {
		File f = new File(path);
		if (!f.exists()) 
			FileUtils.forceMkdir(f);
				
	}

	public String generateTrackFile(IProject p, TestSuiteSession tss) throws JsonGenerationException, JsonMappingException, IOException {
		JacksonXmlModule module = new JacksonXmlModule();

		module.setDefaultUseWrapper(false);
		ObjectMapper xmlMapper = new XmlMapper(module);

		String fileName = sdf.format(new Date()) + ".xml";
		String filePath = p.getLocation().toOSString() + "/" + TDD_CRITERIA_CONFIG_FOLDER + "/" + FileType.JUNIT.getFolder() + "/" + fileName;

		xmlMapper.writeValue(new File(filePath), tss);
		
		return fileName;
	}
	
	public String getTDDCriteriaConfigFilePath (IProject p) {
		return p.getLocation().toOSString()
		+ "/" + TDD_CRITERIA_CONFIG_FOLDER + "/" + TDD_CRITERIA_CONFIG_FILE + ".xml";
	}
	
	public File getJUnitFileAsName (IProject p, String name) {
		return new File(p.getLocation().toOSString() + "/" + TDD_CRITERIA_CONFIG_FOLDER + "/" + FileType.JUNIT.getFolder() + "/" + name);
	}

}
