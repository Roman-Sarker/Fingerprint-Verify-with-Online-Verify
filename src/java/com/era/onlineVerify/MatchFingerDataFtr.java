package com.era.onlineVerify;

import com.era.model.MatchFlagWithScore;
import era.dbconnectivity.*;
import com.futronictech.AnsiSDKLib;
import enroll.verify.FingerMatching;
import era.data.EnrollInformation;
import java.io.IOException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MatchFingerDataFtr {

    // blob to byte convert
    byte[] getByteDataFromBlob(Blob blob) {
        if (blob != null) {
            try {
                return blob.getBytes(1, (int) blob.length());
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }
// Matching Method

    public EnrollInformation matchingProcess(EnrollInformation enrollInformation,
                                             HashMap<String, byte[]> mapCusFinDat, 
                                             HashMap<Integer, Integer> agentNoHM) throws IOException {

        DBConnectionHandler dbConnectivity = new DBConnectionHandler();
        Connection con = dbConnectivity.getConnection();
        FingerMatching fingerMatching = new FingerMatching();
        AnsiSDKLib ansiLib = null; 
        float matchingScore = 0;
        try {
              System.out.println("Before initialize AnsiSDK");
              ansiLib  = new AnsiSDKLib();
              matchingScore = AnsiSDKLib.FTR_ANSISDK_MATCH_SCORE_HIGH_MEDIUM;
              System.out.println("After initialize AnsiSDK");
        } catch (Exception e) {
            System.out.println("AnsiSDKLib initialize error. Error is : "+ e.toString());
            e.printStackTrace();
        }
        
        
        // Information
        System.out.println("App User = " + enrollInformation.getApp_user().toString());  // Here customer finger are available.
        System.out.println("Customer Total Finger = " +mapCusFinDat.entrySet().size());
        //System.out.println("Total agent = " + beanAgentNo.getListStrTotalAgent().size()); // Here all of agent no available of that point.
        System.out.println("Total agent = " + agentNoHM.size()); // Here all of agent no available of that point.

        enrollInformation.setErrorFlag("N");
        
        try {
            //System.out.println("----------------Start Matching by FTR ----------------");
            for (Map.Entry<Integer, Integer> entryAgentNo : agentNoHM.entrySet()) {   // Loop for change agent
                System.out.println("Agent No : " + entryAgentNo.getKey());  // Each Agent No

                String selectSQL = "SELECT LINDEX,LTHUMB,RINDEX,RTHUMB,LMIDDLE,LRING,LLITTLE,"
                        + "RMIDDLE,RRING,RLITTLE from  Biotpl.FP_ENROLL where  CUST_NO = ?";
                PreparedStatement preparedStatement = con.prepareStatement(selectSQL);
                preparedStatement.setLong(1, entryAgentNo.getKey());

                ResultSet rs = preparedStatement.executeQuery();
                if (rs == null) {
                    System.out.println("No finger data are found for agent customer no - " + entryAgentNo.getKey() + ". ResultSet is null.");
                } else if (rs.next()) {   // Loop for get finger data of specific agent user.
                    // System.out.println("Finger Data are found.");

                    byte[] LINDEX = getByteDataFromBlob(rs.getBlob("LINDEX"));
                    byte[] LTHUMB = getByteDataFromBlob(rs.getBlob("LTHUMB"));
                    byte[] RINDEX = getByteDataFromBlob(rs.getBlob("RINDEX"));
                    byte[] RTHUMB = getByteDataFromBlob(rs.getBlob("RTHUMB"));

                    byte[] LMIDDLE = getByteDataFromBlob(rs.getBlob("LMIDDLE"));
                    byte[] LRING = getByteDataFromBlob(rs.getBlob("LRING"));
                    byte[] LLITTLE = getByteDataFromBlob(rs.getBlob("LLITTLE"));

                    byte[] RMIDDLE = getByteDataFromBlob(rs.getBlob("RMIDDLE"));
                    byte[] RRING = getByteDataFromBlob(rs.getBlob("RRING"));
                    byte[] RLITTLE = getByteDataFromBlob(rs.getBlob("RLITTLE"));

                    HashMap<String, byte[]> mapAgentFinDat = new HashMap<String, byte[]>();
                    if (LTHUMB != null && LTHUMB.length > 0) {
                        mapAgentFinDat.put("LTHUMB", LTHUMB);
                    }
                    if (LINDEX != null && LINDEX.length > 0) {
                        mapAgentFinDat.put("LINDEX", LINDEX);
                    }
                    if (LMIDDLE != null && LMIDDLE.length > 0) {
                        mapAgentFinDat.put("LMIDDLE", LMIDDLE);
                    }
                    if (LRING != null && LRING.length > 0) {
                        mapAgentFinDat.put("LRING", LRING);
                    }
                    if (LLITTLE != null && LLITTLE.length > 0) {
                        mapAgentFinDat.put("LLITTLE", LLITTLE);
                    }
                    if (RINDEX != null && RINDEX.length > 0) {
                        mapAgentFinDat.put("RINDEX", RINDEX);
                    }
                    if (RTHUMB != null && RTHUMB.length > 0) {
                        mapAgentFinDat.put("RTHUMB", RTHUMB);
                    }
                    if (RMIDDLE != null && RMIDDLE.length > 0) {
                        mapAgentFinDat.put("RMIDDLE", RMIDDLE);
                    }
                    if (RRING != null && RRING.length > 0) {
                        mapAgentFinDat.put("RRING", RRING);
                    }
                    if (RLITTLE != null && RLITTLE.length > 0) {
                        mapAgentFinDat.put("RLITTLE", RLITTLE);
                    }

                    //System.out.println("---Agent Finger Data---"); 
                    for (Map.Entry<String, byte[]> entryCustFp : mapCusFinDat.entrySet()) { // Loop change after matching customer finger one by one
                        
                        String agentNo = Integer.toString(entryAgentNo.getKey());
                        //System.out.println("Agent Finger :"+entryAgent.getKey()+"Finger Value: "+entryAgent.getValue());

                        for (Map.Entry<String, byte[]> entryAgentFP : mapAgentFinDat.entrySet()) { // Loop change on agent finger no
                            byte[] agentFp = entryAgentFP.getValue();
                            String agentFpName = entryAgentFP.getKey();
                            byte[] custFp = entryCustFp.getValue();
                            String custFpName = entryCustFp.getKey();
                            String custNo = enrollInformation.getName();
                            
                            MatchFlagWithScore matchFlagWithScore = new MatchFlagWithScore();
                            matchFlagWithScore = fingerMatching.FingerprintVerify(ansiLib,
                                                                                  custFp, 
                                                                                  agentFp, 
                                                                                  matchingScore);
                             // If Match       
                            if (matchFlagWithScore.getMatchFlag()) {
                                enrollInformation.setErrorFlag("S");
                                enrollInformation.setErrorMessage("Suspicious agent found.");
                                enrollInformation.setSuspiciousAgentNo(agentNo);
                                InsertMatchedAgent insertMatchedAgent = new InsertMatchedAgent();
                                insertMatchedAgent.insertMatchingInformationInDatabase(agentNo, custNo, agentFpName, custFpName, "S"); //Insert matched agent and customer info

                                System.out.println("Finger matched *.");
                                System.out.println("Online Match with Customer Finger = " + entryCustFp.getKey() + " and Agent Finger = " + entryAgentFP.getKey());

                                // System.out.println("Agent Finger :"+ entryAgent.getKey());
                                return enrollInformation;
                            } else {
                                //System.out.println("Matching with Customer Finger = "+entryCustFp.getKey()+" and Agent Finger = "+data.toString());
                                System.out.println("Online finger verify not match.");

                            }
                        }
                        //              beanCustNoFP = fingerMatching.fingerPrintIdentify(agentNo, entryCustFp.getValue(), mapAgentFinDat, beanCustNoFP);

                        System.out.println("Another finger of customer.");

                    }
                } // Loop for get finger data of specific agent user.

                System.out.println(" --------Another Agent--------- ");

            } // Loop for change agent customer

        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println(ex.getMessage());
            enrollInformation.setErrorFlag("Y");
            enrollInformation.setErrorMessage("Faced SQL Excetion in MatchFingerDataFtr class.");
        } finally {
            dbConnectivity.releaseConnection(con);
        }

        
        return enrollInformation;
    }

    // Remove null data....
    byte[][] getNonNullData(byte[][] data) {
        byte[][] nonNullData;
        int index = 0;

        for (int i = 0; i < data.length; i++) {
            if (data[i] != null) {
                ++index;
            }
        }

        nonNullData = new byte[index][];
        index = 0;
        for (int i = 0; i < data.length; i++) {
            if (data[i] != null) {
                nonNullData[index++] = data[i];
            }
        }
        return nonNullData;
    }
// main method only for testing purpose

    public static void main(String[] args) {
        //String matchingProcess = matchingProcess("S","ROMAN98");

    }

}
