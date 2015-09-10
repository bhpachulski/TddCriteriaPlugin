package net.bhpachulski.tddcriteriaserver.model;

import java.util.HashMap;
import java.util.Map;
import static net.bhpachulski.tddcriteriaserver.model.FileType.values;

/**
 *
 * @author bhpachulski
 */
public enum TDDStage {

    RED, GREEN, REFACTOR, NONE;
    
    private static Map<String, TDDStage> tddStage;
    
    static {
        tddStage = new HashMap<String, TDDStage>();
        
        for (TDDStage tdd : values()) {
            tddStage.put(tdd.toString(), tdd);
        }
    }
    
    public static TDDStage getStageByString (String s) {
        return tddStage.get(s);
    }
    
}
