package net.bhpachulski.tddcriteriaserver.network.util;

import java.io.File;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.eclipse.core.resources.IProject;

import net.bhpachulski.tddcriteriaserver.file.FileUtil;
import net.bhpachulski.tddcriteriaserver.model.FileType;
import net.bhpachulski.tddcriteriaserver.model.StudentFile;
import net.bhpachulski.tddcriteriaserver.model.TDDCriteriaProjectProperties;
import net.bhpachulski.tddcriteriaserver.model.TDDStage;
import net.bhpachulski.tddcriteriaserver.restclient.TDDCriteriaRestClient;

public class TDDCriteriaNetworkUtil {
	
	private FileUtil futil = new FileUtil();
	private TDDCriteriaRestClient restClient = new TDDCriteriaRestClient();

	public String getMacAddress () {
		InetAddress ip;
		try {
			ip = InetAddress.getLocalHost();
			NetworkInterface network = NetworkInterface.getByInetAddress(ip);	 
			byte[] mac = network.getHardwareAddress();
	 
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < mac.length; i++) {
				sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));		
			}

			return sb.toString();
		} catch (UnknownHostException | SocketException e) {
			throw new RuntimeException("Erro ao recuperar MACADRESS"); 	 
		} 
	}
	
	public void sendAllFiles (TDDCriteriaProjectProperties propertiesFile, IProject project) {
		if (propertiesFile.getCurrentStudent().getId() != 0) {
			Map<String, TDDStage> tddProjectStages = futil.readTDDStagesFile(project);
		
			sendFiles(FileType.JUNIT, tddProjectStages, project, propertiesFile);			
			sendFiles(FileType.SRC, tddProjectStages, project, propertiesFile);
			sendFiles(FileType.ECLEMMA, tddProjectStages, project, propertiesFile);
		}
	}
	
	private void sendFiles(FileType ft, Map<String, TDDStage> tddProjectStages, IProject project, TDDCriteriaProjectProperties propertiesFile) {
		for (File f : futil.getAllFiles (ft, project)) {
			if (!propertiesFile.getSentFiles().contains(new StudentFile(f.getName(), ft))) {

				//ignorando os décimos de segundo do arquivo
				TDDStage fileTDDStage = tddProjectStages.get(FilenameUtils.getBaseName(f.getName().substring(0, f.getName().length() - 1)));
				fileTDDStage = (fileTDDStage == null) ? TDDStage.NONE : fileTDDStage;
				
				StudentFile sf = restClient.sendStudentFile(propertiesFile, 
						project.getName(), futil.getFileAsName(ft, project, f.getName()), ft, fileTDDStage);			
				
				propertiesFile.setSentFile(sf);
			}
		}
	}
}
