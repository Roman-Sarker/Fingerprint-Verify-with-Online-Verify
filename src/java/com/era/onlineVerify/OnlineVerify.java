
package com.era.onlineVerify;

import era.data.EnrollInformation;
import java.util.HashMap;

/**
 *
 * @author roman
 */
public class OnlineVerify {
    public EnrollInformation checkSuspicuousAgent(EnrollInformation enrollInformation){
        
        // Store customer FP data to HashMap
        HashMap<String, byte[]> mapCusFinDat = new HashMap<String, byte[]>();
                if (enrollInformation.getlThumb() != null && enrollInformation.getlThumb().length > 0) {
                    mapCusFinDat.put("LTHUMB", enrollInformation.getlThumb());
                }
                if (enrollInformation.getlIndex() != null && enrollInformation.getlIndex().length > 0) {
                    mapCusFinDat.put("LINDEX", enrollInformation.getlIndex());
                }
                if (enrollInformation.getlMiddle() != null && enrollInformation.getlMiddle().length > 0) {
                    mapCusFinDat.put("LMIDDLE", enrollInformation.getlMiddle());
                }
                if (enrollInformation.getlRing() != null && enrollInformation.getlRing().length > 0) {
                    mapCusFinDat.put("LRING", enrollInformation.getlRing());
                }
                if (enrollInformation.getlLittle() != null && enrollInformation.getlLittle().length > 0) {
                    mapCusFinDat.put("LLITTLE", enrollInformation.getlLittle());
                }
                if (enrollInformation.getrIndex() != null && enrollInformation.getrIndex().length > 0) {
                    mapCusFinDat.put("RINDEX", enrollInformation.getrIndex());
                }
                if (enrollInformation.getrThumb() != null && enrollInformation.getrThumb().length > 0) {
                    mapCusFinDat.put("RTHUMB", enrollInformation.getrThumb());
                }
                if (enrollInformation.getrMiddle() != null && enrollInformation.getrMiddle().length > 0) {
                    mapCusFinDat.put("RMIDDLE", enrollInformation.getrMiddle());
                }
                if (enrollInformation.getrRing() != null && enrollInformation.getrRing().length > 0) {
                    mapCusFinDat.put("RRING", enrollInformation.getrRing());
                }
                if (enrollInformation.getrLittle() != null && enrollInformation.getrLittle().length > 0) {
                    mapCusFinDat.put("RLITTLE", enrollInformation.getrLittle());
                }
                System.out.println("Total fingers of customer : " + mapCusFinDat.entrySet().size());
                
                
                // Collect Agents Finger Data
                
                AllAgentOfPoint allAgentOfPoint = new AllAgentOfPoint();
                enrollInformation = allAgentOfPoint.getAgentNo(enrollInformation, mapCusFinDat);
                
                
                
    
        return enrollInformation;
    }
    
}
