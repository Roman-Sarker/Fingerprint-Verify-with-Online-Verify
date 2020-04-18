package era.data;

import com.era.model.MatchFlagWithScore;
import enroll.verify.FingerMatching;
import era.dbconnectivity.DBConnectionHandler;
import java.io.IOException;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FingerPrintVerification {

    String errorMessage = "";

    public FingerPrintVerification() {

    }

    public List<LoginModel> deleteFinger(LoginModel loginModel) {
        System.out.println("Calling 'procedure_call_for_login' for delete finger Row of "+loginModel.getCustno());
        return procedure_call_for_login(loginModel);
    }

    byte[] getByteDataFromBlob(Blob blob) {
        if (blob != null) {
            try {
                return blob.getBytes(1, (int) blob.length());
            } catch (SQLException ex) {
                Logger.getLogger(FingerPrintVerification.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

    public List<LoginModel> fingerVerfyFromTemplate(LoginModel loginModel) throws IOException {

        List<LoginModel> list = new ArrayList<LoginModel>();
        LoginModel model = new LoginModel();

        DBConnectionHandler dbConnectionHandler = new DBConnectionHandler();
        Connection con = null;
        Long cust_number = null;
        try {
            con = dbConnectionHandler.getConnection();
            cust_number = Long.parseLong(loginModel.getName());
        } catch (NumberFormatException e) {
            loginModel.setErrorFlag("Y");
            loginModel.setErrorMessage("customer number is not"
                    + " found . Error Message is " + e.getMessage());
            list.add(loginModel);
            //DBConnectionHandler.releaseConnection(con);
            return list;
        }finally{
            DBConnectionHandler.releaseConnection(con);
        }
        
        if (cust_number == null) {
            loginModel.setErrorFlag("Y");
            loginModel.setErrorMessage("customer number is not"
                    + " found.Contact with our system administrator");
            list.add(loginModel);
            //DBConnectionHandler.releaseConnection(con);
            return list;
        }

        byte[][] data = new byte[10][];
        try {
            con = dbConnectionHandler.getConnection();
            
            String selectSQL = "SELECT LINDEX,LTHUMB,RINDEX,RTHUMB,LMIDDLE,LRING,LLITTLE,"
                    + "RMIDDLE,RRING,RLITTLE from  Biotpl.FP_ENROLL where  CUST_NO = ?";
            //System.out.println(selectSQL);
            PreparedStatement preparedStatement = con.prepareStatement(selectSQL);
            preparedStatement.setLong(1, cust_number);
            //System.out.println("PreparedStatement clear.");

            ResultSet rs = preparedStatement.executeQuery();
//            if (rs.next() == false) {
//                System.out.println("Resultset is null. No finger data are found for this Customer No - "+cust_number);
//                System.out.println("Number of Row  = " + rs.getRow());
//            }
            if (rs == null) {
                System.out.println("No finger data are found for customer - " + cust_number + ". ResultSet is null.");
            }
            if (rs.next()) {
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

//                data[0] = LINDEX;
//                data[1] = LTHUMB;
//                data[2] = RINDEX;
//                data[3] = RTHUMB;
                data[0] = RTHUMB;
                data[1] = LTHUMB;
                data[2] = RINDEX;
                data[3] = LINDEX;

                data[4] = LMIDDLE;
                data[5] = LRING;
                data[6] = LLITTLE;

                data[7] = RMIDDLE;
                data[8] = RRING;
                data[9] = RLITTLE;
                data = getNonNullData(data);
                // System.out.println("Data Length = " + data.length);
            }
        } catch (SQLException ex) {
            Logger.getLogger(FingerPrintVerification.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }finally{
            DBConnectionHandler.releaseConnection(con);
        }

        if (data == null || data.length == 0) {
            loginModel.setErrorFlag("Y");
            //loginModel.setErrorMessage("Fingerprint data is not" + " found for customer number " + loginModel.getCustno());
            loginModel.setErrorMessage("Fingerprint data is not" + " found for customer number " + cust_number);
            list.add(loginModel);
            DBConnectionHandler.releaseConnection(con);
            return list;
        } else {
            boolean flag_to_check_null_data = false;
            for (int i = 0; i < data.length; i++) {
                if (data[i] == null) {
                    flag_to_check_null_data = true;
                    break;
                }
            }
            if (flag_to_check_null_data) {
                loginModel.setErrorFlag("Y");
                loginModel.setErrorMessage("Fingerprint data is null"
                        + " for customer number " + loginModel.getCustno());
                list.add(loginModel);
                DBConnectionHandler.releaseConnection(con);
                return list;
            }
        }

        //System.out.println("Futronic is now trying to match customer number "+loginModel.getName());    
        FingerMatching fingerMatching = new FingerMatching();

        MatchFlagWithScore matchFlagWithScore = new MatchFlagWithScore();
        matchFlagWithScore = fingerMatching.fingerPrintIdentify(loginModel.getName(), loginModel.getFingerBytes(), data); // data is finger data from Database, And loginModel.getFingerBytes() is finger data from UI And loginModel.getName() is the customer no 
        //Boolean matchingFlag = fingerMatching.fingerPrintIdentify(loginModel.getName(),loginModel.getFingerBytes(), data);

        //System.out.println("Customer number "+loginModel.getName()+" matching result "+matchingFlag+" Futronic");
        System.out.println("Matching Flag : " + matchFlagWithScore);
        if (matchFlagWithScore.getMatchFlag()) {

            // Current Date and Time...
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            System.out.println("======================== Matched with ISO file (" + dateFormat.format(date) + ") ========================="); //2016/11/16 12:08:43

            loginModel.setErrorFlag("N");
            loginModel.setErrorMessage(" ");

            System.out.println("User defined match score : " + matchFlagWithScore.getUserGivenMatchScore());
            System.out.println("SDK Return match score : " + matchFlagWithScore.getMatchScore());
            loginModel.setMatchingScore(matchFlagWithScore.getMatchScore());
            System.out.println("Finger Name :" + matchFlagWithScore.getFingerName());
            loginModel.setFingerName(matchFlagWithScore.getFingerName().toString());
            System.out.println("Operation Type is " + loginModel.getpOperationType());
            System.out.println("App User is " + loginModel.getAppuser());
            System.out.println("Device ID is " + loginModel.getpDeviceId());
            System.out.println("Session ID is " + loginModel.getpSessionId());
            System.out.println("Cust Type is " + loginModel.getpCustType());
            System.out.println("Amount is " + loginModel.getpAmount());
            System.out.println("Cust No is " + loginModel.getCustno());

            list = procedure_call_for_login(loginModel); // Match data store in log table.
            //DBConnectionHandler.releaseConnection(con);
            System.out.println("Verification Successed.");
            return list;
        } else {
            loginModel.setErrorFlag("Y");
            loginModel.setErrorMessage("Verification Failed");
            System.out.println("Verification Failed.");
            list.add(loginModel);
            DBConnectionHandler.releaseConnection(con);
            return list;
        }
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

    public List<LoginModel> procedure_call_for_login(LoginModel loginModel) {
        List<LoginModel> list = new ArrayList<LoginModel>();
        DBConnectionHandler dbConnectionHandler = new DBConnectionHandler();
        Connection con = dbConnectionHandler.getConnection();
        if (con == null) {
            LoginModel model = new LoginModel();
            model.setErrorFlag("Y");
            model.setErrorMessage(dbConnectionHandler.getErrorMessge());
            list.add(model);
            return list;
        } else {
            try {
                Parameter_Fixed_Verify param_fixed_verify = new Parameter_Fixed_Verify();
                String opr_type = loginModel.getpOperationType();
                    // = param_fixed_verify.getParameter(opr_type, true);
                // System.out.println("OPR_TYPE is " + opr_type);

                String cust_type = loginModel.getpCustType();
                // System.out.println("cust_type is " + cust_type);

                String appuser = loginModel.getAppuser();
                // System.out.println("appUser is " + appuser);

                String pLogiD = loginModel.getpLogId();
                //  pLogiD = param_fixed_verify.getParameter(pLogiD, true);
                // System.out.println("pLogiD is " + pLogiD);
//System.out.println("Before calling procedure : Finger Name :"+loginModel.getFingerName());
//System.out.println("Matching Score :"+loginModel.getMatchingScore());

                CallableStatement stmt = con.prepareCall("{CALL BIOTPL.dfn_process_finger_data(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
                stmt.setString(1, opr_type);
                stmt.setLong(2, Long.parseLong(loginModel.getName()));
                stmt.setString(3, appuser);
                stmt.setString(4, pLogiD);
                stmt.setInt(5, Integer.parseInt(loginModel.getCustno()));
                stmt.setString(6, loginModel.getpAmount());
                stmt.setString(7, loginModel.getpSessionId());
                stmt.setString(8, cust_type);
                stmt.setString(9, "W");
                stmt.setString(10, loginModel.getpDeviceId());
                //stmt.setString(11, loginModel.getFingerName());
                //stmt.setString(12, loginModel.getMatchingScore());
                stmt.registerOutParameter(11, java.sql.Types.VARCHAR);
                stmt.registerOutParameter(12, java.sql.Types.VARCHAR);
                stmt.registerOutParameter(13, java.sql.Types.VARCHAR);
                stmt.registerOutParameter(14, java.sql.Types.VARCHAR);
                stmt.registerOutParameter(15, java.sql.Types.VARCHAR);
                stmt.execute();

                LoginModel model = new LoginModel();
                //System.out.println("ex error Flag = " + stmt.getString(11));
                String errorFlag = stmt.getString(13);
                errorFlag = errorFlag.trim();
                String errorMessage = stmt.getString(14);

                model.setErrorFlag(errorFlag);
                //System.out.println("Error Flag and Message Before if condition :" + errorFlag + ". " + errorMessage);
                if (errorMessage != null && !errorMessage.isEmpty()) {
                    model.setErrorMessage(errorMessage);
                } else {
                    model.setErrorMessage("Verification Successed.");
                }

                list.add(model);

                dbConnectionHandler.releaseConnection(con);
            } catch (SQLException ex) {
                Logger.getLogger(FingerPrintVerification.class.getName()).log(Level.SEVERE, null, ex);
                ex.printStackTrace();
            }

        }

        return list;
    }

    public static void main(String[] arg) throws IOException {
        FingerPrintVerification fingerPrintVerification = new FingerPrintVerification();

        LoginModel loginModel = new LoginModel();
        loginModel.setName("151");

        String str = "Rk1SACAyMAAAAADMAAABQAHgAMUAxQEAAAA8HUCXAMB5AECKANt3AIC5ANEEAECMAPkDAEBiANP3AIB6AG36AEBuAS9sAEBgAQsAAECjAKgLAEBTAUByAIBLAP9sAIBxASf6AIB4AEneAICKAF1uAEC3AIULAIB9AE5kAEBXAUJrAIB0AQx5AIBtAUZ3AEC2AHiNAIAaAPjpAICeAKsAAEAvAOXpAECmAHgBAICQAH1wAICQAO2AAIAuAO5rAECqAJOAAEAJARbmAAAA";
        byte[][] fingerBytes = new byte[1][];//javax.xml.bind.DatatypeConverter.parseBase64Binary(str);
        fingerBytes[0] = Base64.getDecoder().decode(str);
        loginModel.setFingerBytes(fingerBytes[0]);
        str = "Rk1SACAyMAAAAADMAAABQAHgAMUAxQNt3AIC5ANEEAECMAPkDAEBiANP3AIB6AG36AEBuAS9sAEBgAQsAAECjAKgLAEBTAUByAIBLAP9sAIBxASf6AIB4AEneAICKAF1uAEC3AIULAIB9AE5kAEBXAUJrAIB0AQx5AIBtAUZ3AEC2AHiNAIAaAPjpAICeAKsAAEAvAOXpAECmAHgBAICQAH1wAICQAO2AAIAuAO5rAECqAJOAAEAJARbmAAAA";
        fingerBytes[0] = Base64.getDecoder().decode(str);
        FingerMatching fingerMatching = new FingerMatching();

        MatchFlagWithScore matchFlagWithScore = new MatchFlagWithScore();
//        boolean matchingFlag = fingerMatching.fingerPrintIdentify("",loginModel.getFingerBytes(), fingerBytes);
        matchFlagWithScore = fingerMatching.fingerPrintIdentify("", loginModel.getFingerBytes(), fingerBytes);

        //   JSGFPLib fplib = new JSGFPLib();
        //   fplib.Init(SGFDxDeviceName.SG_DEV_AUTO);
        //   fplib.SetTemplateFormat(SGFDxTemplateFormat.TEMPLATE_FORMAT_ANSI378);
        // fingerPrintVerification.fingerVerfyFromTemplate(loginModel);
        //  boolean flag = fingerPrintVerification.fingerprintVerify(fplib, fingerBytes, fingerBytes);
        // System.out.println("result " + flag);
        //   fplib.Close();
        //fingerPrintVerification.fingerVerfyFromTemplate(loginModel);
    }
}
