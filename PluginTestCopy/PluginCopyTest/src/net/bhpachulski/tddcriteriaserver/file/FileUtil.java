package net.bhpachulski.tddcriteriaserver.file;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import net.bhpachulski.tddcriteriaserver.model.FileType;
import net.bhpachulski.tddcriteriaserver.model.Student;
import net.bhpachulski.tddcriteriaserver.model.TDDCriteriaProjectProperties;
import net.bhpachulski.tddcriteriaserver.model.TestSuiteSession;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.monitor.FileEntry;
import org.eclipse.core.resources.IProject;

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
	private static final String TDD_CRITERIA_ERROR_FOLDER = "errorLog";
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy_M_d_HH_mm_ss");	

	public FileUtil() {
		module = new JacksonXmlModule();
		module.setDefaultUseWrapper(false);
		xmlMapper = new XmlMapper(module);
	}

	public TDDCriteriaProjectProperties createProjectConfigFile(IProject p,
			Student student) throws InterruptedException {
		try {
			createFolderIfNotExists(p.getLocation().toOSString() + "/" + TDD_CRITERIA_CONFIG_FOLDER + "/" + FileType.JUNIT.getFolder());
			createFolderIfNotExists(p.getLocation().toOSString() + "/" + TDD_CRITERIA_CONFIG_FOLDER + "/" + TDD_CRITERIA_ERROR_FOLDER);			
			createFolderIfNotExists(p.getLocation().toOSString() + "/" + TDD_CRITERIA_CONFIG_FOLDER + "/" + FileType.ECLEMMA.getFolder());

			Thread.sleep(250);
			
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
			
			xmlMapper.writeValue(FileUtils.getFile(getTDDCriteriaConfigFilePath(p)), projectConfigFile);
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
	
	public boolean createFolderIfNotExists (String path) throws IOException {		
		File f = FileUtils.getFile(path);
		
		if (!f.exists()) 
			f.mkdirs();
		
		return true;
	}

	public String generateTrackFile(IProject p, TestSuiteSession tss) throws JsonGenerationException, JsonMappingException, IOException {
		JacksonXmlModule module = new JacksonXmlModule();

		module.setDefaultUseWrapper(false);
		ObjectMapper xmlMapper = new XmlMapper(module);

		String fileName = sdf.format(new Date()) + ".xml";
		String filePath = p.getLocation().toOSString() + "/" + TDD_CRITERIA_CONFIG_FOLDER + "/" + FileType.JUNIT.getFolder() + "/" + fileName;

		File f = FileUtils.getFile(filePath);
		
		xmlMapper.writeValue(f, tss);
		
		return fileName;
	}
	
	public String getTDDCriteriaConfigFilePath (IProject p) {
		return p.getLocation().toOSString()
		+ "/" + TDD_CRITERIA_CONFIG_FOLDER + "/" + TDD_CRITERIA_CONFIG_FILE + ".xml";
	}
	
	public File getFileAsName (FileType ft, IProject p, String name) {
		return new File(p.getLocation().toOSString() + "/" + TDD_CRITERIA_CONFIG_FOLDER + "/" + ft.getFolder() + "/" + name);
	}
	
	public List<File> getAllFiles (FileType ft, IProject p) {
		List<File> arquivos = Arrays.asList(new File(p.getLocation().toOSString() + "/" + TDD_CRITERIA_CONFIG_FOLDER + "/" + ft.getFolder() + "/").listFiles());
		
		return arquivos;
	}
	
	public void createTxtFile (IProject p, String value) {
		Writer writer = null;

		try {
		    writer = new BufferedWriter(new OutputStreamWriter(
		          new FileOutputStream(p.getLocation().toOSString()
		        			+ "/" + TDD_CRITERIA_CONFIG_FOLDER + "/" + TDD_CRITERIA_ERROR_FOLDER + "/" +"errorLog.txt"), "utf-8"));
		    writer.write(value);
		    
		    writer.close();
		} catch (IOException ex) {
			
		} 
	}

}
