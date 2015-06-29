package net.bhpachulski.tddcriteriaserver.model;

import java.util.HashMap;
import java.util.Map;

public enum FileType {

	JUNIT(1, "junitTrack"), ECLEMMA(2, "coverageTrack");

    private final Integer id;
    private final String folder;
    
    private static Map<Integer, FileType> fileType;
    
    static {
        fileType = new HashMap<Integer, FileType>();
        
        for (FileType ift : values()) {
            fileType.put(ift.getId(), ift);
        }
    }

    private FileType(int id, String folder) {
        this.id = id;
        this.folder = folder;
    }

    public Integer getId () {
        return this.id;
    }
    
    public String getFolder() {
        return folder;
    }

    public static Map<Integer, FileType> getFileType() {
        return fileType;
    }
    
}