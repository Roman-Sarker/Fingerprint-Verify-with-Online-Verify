package enroll.verify;

/**
 *
 * @author root
 */
import com.era.model.FingerDataWithName;
import com.era.model.MatchFlagWithScore;
import com.futronictech.AnsiSDKLib;
import era.dbconnectivity.DBConnectionHandler;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Base64;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import resource.GetFile;
import java.util.Properties; 

/**
 *
 * @author roman
 */
public class FingerMatching {

    MatchFlagWithScore matchFlagWithScore = new MatchFlagWithScore();
    public boolean fingerPrintIdentify(long cust_no, String fingerName,byte[] fingerData, long agentNo, List<FingerDataWithName> agentFingerData) {

        boolean matchingFlag = false;
        
        int i = 0, dataLength = agentFingerData.size();
        while (i < dataLength) {
            String agentFingername = agentFingerData.get(i).getFingerName();
            byte[] agentFingerByteData = agentFingerData.get(i).getFingerData();
            matchingFlag = fingerprintVerify(fingerData, agentFingerByteData);
            if (matchingFlag) {
                insertMatchingInformationInDatabase(fingerData, cust_no, fingerName, agentNo, agentFingername);
                break;
            }
            ++i;
        }
        return matchingFlag;
    }

    private boolean fingerprintVerify(byte[] custFingerData, byte[] agentFingerData) {

        return false;
    }

    public MatchFlagWithScore fingerPrintIdentify(String cust_no, byte[] fingerData, byte[][] fingerEnrollData) throws IOException {
       // MatchFlagWithScore matchFlagWithScore = new MatchFlagWithScore();
        
      //System.out.println("fingerprintidentify method is called");
        //boolean matchingFlag = false;
        AnsiSDKLib ansi_lib = new AnsiSDKLib();
        float matchingScore = getMatchingScore(ansi_lib);
        String fingerName = "";
        int i = 0, dataLength = fingerEnrollData.length;
        
        while (i < dataLength) {
            //matchingFlag = FingerprintVerify(ansi_lib, fingerData, fingerEnrollData[i], matchingScore);
            // Start Matching
            System.out.println("Finger Data DB "+i+" : "+ Base64.getEncoder().encode(fingerEnrollData[i]));
            matchFlagWithScore = FingerprintVerify(ansi_lib, 
                                                   fingerData, 
                                                   fingerEnrollData[i], 
                                                   matchingScore);
            if (matchFlagWithScore.getMatchFlag()) {
                if(i == 0)
                {fingerName = "RTHUMB";}
                else if(i == 1)
                {fingerName = "LTHUMB";}
                else if(i == 2)
                {fingerName = "RINDEX";}
                else if(i == 3)
                {fingerName = "LINDEX";}
                else if(i == 4)
                {fingerName = "LMIDDLE";}
                else if(i == 5)
                {fingerName = "LRING";}
                else if(i == 6)
                {fingerName = "LLITTLE";}
                else if(i == 7)
                {fingerName = "RMIDDLE";}
                else if(i == 8)
                {fingerName = "RRING";}
                else if(i == 9)
                {fingerName = "RLITTLE";}
                else
                    {System.out.println("No Finger is Match.");}
                
                matchFlagWithScore.setFingerName(fingerName);
                //System.out.println("Match finger is :"+fingerName.toString());
                break;
            }
            ++i;
        }
        return matchFlagWithScore;
    }
    public float getMatchingScore(AnsiSDKLib ansi_lib) throws IOException{
        String matchingNumber = null;
        float ms = 0 ;
        
        try {
            Properties prop = new Properties();
            InputStream inputStream = null;
            File file = GetFile.getFile("MatchScore_Sha.properties");
            inputStream = new FileInputStream(file);
            prop.load(inputStream);
            matchingNumber = prop.getProperty("matchScore");
            inputStream.close();
            
        } catch (FileNotFoundException e) {
            matchingNumber = "121";
        }
        
        if(matchingNumber.equals("37")){
            ms = AnsiSDKLib.FTR_ANSISDK_MATCH_SCORE_LOW;
        }
        else if(matchingNumber.equals("65")){
            ms = AnsiSDKLib.FTR_ANSISDK_MATCH_SCORE_LOW_MEDIUM;
        }
        else if(matchingNumber.equals("93")){
            ms = AnsiSDKLib.FTR_ANSISDK_MATCH_SCORE_MEDIUM;
        }
        else if(matchingNumber.equals("121")){
            ms = AnsiSDKLib.FTR_ANSISDK_MATCH_SCORE_HIGH_MEDIUM;
        }
        else if(matchingNumber.equals("146")){
            ms = AnsiSDKLib.FTR_ANSISDK_MATCH_SCORE_HIGH;
        }
        else if(matchingNumber.equals("189")){
            ms = AnsiSDKLib.FTR_ANSISDK_MATCH_SCORE_VERY_HIGH;
        }
        else
        {ms = AnsiSDKLib.FTR_ANSISDK_MATCH_SCORE_HIGH_MEDIUM;}
        //System.out.println("Match Score = " + ms);
    return ms;
    }
    
    // Main Matching Method
    public MatchFlagWithScore FingerprintVerify(AnsiSDKLib ansi_lib,
                                                byte[] dataFromUser, 
                                                byte[] dataFromDB, 
                                                float matchingScore) throws IOException {
        float[] matchResult = new float[1];
        //float matchingScore = getMatchingScore(ansi_lib);
        //float mMatchScoreHighMedium = AnsiSDKLib.FTR_ANSISDK_MATCH_SCORE_HIGH_MEDIUM;
        //MatchFlagWithScore matchFlagWithScore = new MatchFlagWithScore();
        //boolean matchingFlag = false;
/*
        dataFromUser[10] = 0x00;
        dataFromUser[11] = 0x4D;
        dataFromUser[12] = 0x00;
        dataFromUser[13] = 0x03;
        dataFromUser[14] = 0x00;
        dataFromUser[15] = 0x00;
*/
        if (dataFromUser == null || dataFromDB == null) {
            System.out.println("null data found");
            matchFlagWithScore.setMatchFlag(false);
            return matchFlagWithScore;
        } else if (dataFromUser.length > 500 || dataFromDB.length > 500) {
            System.out.println("abnormal data found");
            System.out.println("dataFromUser length is " + dataFromUser.length);
            System.out.println("dataFromDB length is " + dataFromDB.length);
            matchFlagWithScore.setMatchFlag(false);
            return matchFlagWithScore;
        }
        //System.out.println("matchResult[0] = " + matchResult[0]);
        // Matching Here
      System.out.println("Before Matching");
        if (ansi_lib.MatchTemplates(dataFromDB, dataFromUser, matchResult) && matchResult[0] > matchingScore) {
        
                matchFlagWithScore.setMatchFlag(true);
                matchFlagWithScore.setUserGivenMatchScore(String.valueOf(matchingScore));
                matchFlagWithScore.setMatchScore( String.valueOf(matchResult[0]));
            
        } else {
            int lastError = ansi_lib.GetErrorCode();
            System.out.println("***----Error Code : "+lastError+". Error message : "+ansi_lib.GetErrorMessage());    
            String error = String.format("Verification failed. Error: %s.", ansi_lib.GetErrorMessage());
            System.out.println(error);
            if (lastError == AnsiSDKLib.FTR_ERROR_EMPTY_FRAME
                    || lastError == AnsiSDKLib.FTR_ERROR_NO_FRAME
                    || lastError == AnsiSDKLib.FTR_ERROR_MOVABLE_FINGER) {
            error = String.format("Matching failed. Error: %s.", ansi_lib.GetErrorMessage());
            System.out.println(error);
                
                matchFlagWithScore.setMatchFlag(false);
            } else {
                error = String.format("Verification failed. Error is : "+ ansi_lib.GetErrorMessage());
                System.out.println(error);
                matchFlagWithScore.setMatchFlag(false);
            }
        }
        return matchFlagWithScore;
    }

    private void insertMatchingInformationInDatabase(byte[] custFingerData, long cust_no,
            String fingerName, long agentNo, String agentFingername) {
        try {
            DBConnectionHandler dbConnectionHandler = new DBConnectionHandler();
            Connection conn = dbConnectionHandler.getConnection();
            if (conn == null) {
                return;
            }
// Store matching log data to MATCHING_TEMPLATE_INFO table
            String insertTableSQL = "INSERT INTO MATCHING_TEMPLATE_INFO"
                    + "(AGENT_CUST_NO, CUSTOMER_CUST_NO, CREATE_DATE,AGENT_FINGER,CUSTOMER_FINGER"
                    + ", CUSTOMER_FINGER_DATA,STANDARD,) VALUES"
                    + "(?,?,sysdate,?,?,?,?)";
            
            PreparedStatement preparedStatement = conn.prepareStatement(insertTableSQL);
            preparedStatement.setLong(1, agentNo);
            preparedStatement.setLong(2, cust_no);
            preparedStatement.setString(3, agentFingername);
            preparedStatement.setString(4, fingerName);
            preparedStatement.setBytes(6, custFingerData);
            preparedStatement.setString(7, "S");
            preparedStatement.executeUpdate();

            dbConnectionHandler.releaseConnection(conn);
        } catch (SQLException ex) {
            Logger.getLogger(FingerMatching.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
