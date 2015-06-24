package net.bhpachulski.tddcriteriaserver.model;

public enum FileType {

	JUNIT(1, "junitTrack"), ECLEMMA(2, "coverageTrack");

    private final Integer id;
    private final String folder;

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
    
}