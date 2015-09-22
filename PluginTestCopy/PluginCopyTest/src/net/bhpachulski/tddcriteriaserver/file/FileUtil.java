package net.bhpachulski.tddcriteriaserver.file;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.bhpachulski.tddcriteriaserver.model.FileType;
import net.bhpachulski.tddcriteriaserver.model.Student;
import net.bhpachulski.tddcriteriaserver.model.TDDCriteriaProjectProperties;
import net.bhpachulski.tddcriteriaserver.model.TDDStage;
import net.bhpachulski.tddcriteriaserver.model.TestSuiteSession;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
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
	private static final String SRC_FOLDER = "src";
	private static final String TDD_CRITERIA_STAGE_FILE = TDD_CRITERIA_CONFIG_FOLDER
			+ File.separator + "tddStageTrack.txt";

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy_M_d_HH_mm_ss");

	public FileUtil() {
		module = new JacksonXmlModule();
		module.setDefaultUseWrapper(false);
		xmlMapper = new XmlMapper(module);
	}

	public TDDCriteriaProjectProperties createProjectConfigFile(IProject p,
			TDDCriteriaProjectProperties prop) throws InterruptedException {
		try {
			createFolderIfNotExists(p.getLocation().toOSString() + "/"
					+ TDD_CRITERIA_CONFIG_FOLDER + "/"
					+ FileType.JUNIT.getFolder());
			createFolderIfNotExists(p.getLocation().toOSString() + "/"
					+ TDD_CRITERIA_CONFIG_FOLDER + "/"
					+ TDD_CRITERIA_ERROR_FOLDER);
			createFolderIfNotExists(p.getLocation().toOSString() + "/"
					+ TDD_CRITERIA_CONFIG_FOLDER + "/"
					+ FileType.ECLEMMA.getFolder());
			createFolderIfNotExists(p.getLocation().toOSString() + "/"
					+ TDD_CRITERIA_CONFIG_FOLDER + "/"
					+ FileType.SRC.getFolder());

			Thread.sleep(250);

			xmlMapper.writeValue(new File(getTDDCriteriaConfigFilePath(p)),
					prop);

			return prop;
		} catch (Exception e) {
			System.out.println("AQUI MERMO");
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

			xmlMapper.writeValue(
					FileUtils.getFile(getTDDCriteriaConfigFilePath(p)),
					projectConfigFile);
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

	public boolean createFolderIfNotExists(String path) throws IOException {
		File f = FileUtils.getFile(path);

		if (!f.exists())
			f.mkdirs();

		return true;
	}

	public String generateJUnitTrackFile(IProject p, TestSuiteSession tss)
			throws JsonGenerationException, JsonMappingException, IOException {
		JacksonXmlModule module = new JacksonXmlModule();

		module.setDefaultUseWrapper(false);
		ObjectMapper xmlMapper = new XmlMapper(module);

		String fileName = sdf.format(new Date()) + ".xml";
		String filePath = p.getLocation().toOSString() + "/"
				+ TDD_CRITERIA_CONFIG_FOLDER + "/" + FileType.JUNIT.getFolder()
				+ "/" + fileName;

		File f = FileUtils.getFile(filePath);

		xmlMapper.writeValue(f, tss);

		return fileName;
	}

	public String getTDDCriteriaConfigFilePath(IProject p) {
		return p.getLocation().toOSString() + "/" + TDD_CRITERIA_CONFIG_FOLDER
				+ "/" + TDD_CRITERIA_CONFIG_FILE + ".xml";
	}

	public File getFileAsName(FileType ft, IProject p, String name) {
		return new File(p.getLocation().toOSString() + "/"
				+ TDD_CRITERIA_CONFIG_FOLDER + "/" + ft.getFolder() + "/"
				+ name);
	}

	public List<File> getAllFiles(FileType ft, IProject p) {
		List<File> arquivos = Arrays.asList(new File(p.getLocation()
				.toOSString()
				+ "/"
				+ TDD_CRITERIA_CONFIG_FOLDER
				+ "/"
				+ ft.getFolder() + "/").listFiles());

		return arquivos;
	}

	public void createTxtFile(IProject p, String value) {
		Writer writer = null;

		try {
			writer = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(p.getLocation()
							.toOSString()
							+ "/"
							+ TDD_CRITERIA_CONFIG_FOLDER
							+ "/"
							+ TDD_CRITERIA_ERROR_FOLDER
							+ "/"
							+ "errorLog.txt"), "utf-8"));
			writer.write(value);

			writer.close();
		} catch (IOException ex) {

		}
	}

	public void generateSrcTrackFile(IProject p) throws IOException,
			ArchiveException {
		String fileName = sdf.format(new Date()) + ".zip";
		String zipFilePath = p.getLocation().toOSString() + File.separator
				+ TDD_CRITERIA_CONFIG_FOLDER + File.separator
				+ FileType.SRC.getFolder() + "/" + fileName;

		File srcFolder = new File(p.getLocation().toOSString() + File.separator
				+ SRC_FOLDER);
		File zipFile = new File(zipFilePath);

		addFilesToZip(srcFolder, zipFile);
	}

	private void addFilesToZip(File source, File destination)
			throws IOException, ArchiveException {
		OutputStream archiveStream = new FileOutputStream(destination);
		ArchiveOutputStream archive = new ArchiveStreamFactory()
				.createArchiveOutputStream(ArchiveStreamFactory.ZIP,
						archiveStream);

		Collection<File> fileList = FileUtils.listFiles(source, null, true);

		for (File file : fileList) {
			String entryName = getEntryName(source, file);
			ZipArchiveEntry entry = new ZipArchiveEntry(entryName);
			archive.putArchiveEntry(entry);

			BufferedInputStream input = new BufferedInputStream(
					new FileInputStream(file));

			IOUtils.copy(input, archive);
			input.close();
			archive.closeArchiveEntry();
		}

		archive.finish();
		archiveStream.close();
	}

	private String getEntryName(File source, File file) throws IOException {
		int index = source.getAbsolutePath().length() + 1;
		String path = file.getCanonicalPath();

		return path.substring(index);
	}

	public Map<String, TDDStage> readTDDStagesFile(IProject p) {

		Map<String, TDDStage> stages = new HashMap<String, TDDStage>();

		try (BufferedReader br = new BufferedReader(new FileReader(p
				.getLocation().toOSString()
				+ File.separator
				+ TDD_CRITERIA_STAGE_FILE))) {

			String currentLine;

			while ((currentLine = br.readLine()) != null) {
				String[] valores = currentLine.trim().split(":");

				// ignorando os décimos de segundo do arquivo
				stages.put(
						valores[0].trim().substring(0,
								valores[0].trim().length() - 1),
						TDDStage.getStageByString(valores[1]));
			}

		} catch (IOException e) {
			// não foi possível ler/encontrar o arquivo
			System.out.println("não foi possível ler/encontrar o arquivo");
		}

		return stages;
	}

}
