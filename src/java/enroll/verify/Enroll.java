/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enroll.verify;

import com.era.model.AgentCustoNo;
import com.era.model.UserFingerData;
import com.era.model.FingerDataWithName;
import era.data.EnrollInformation;
import era.data.FingerPrintVerification;
import era.dbconnectivity.DBConnectionHandler;
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement; 
import java.sql.ResultSet;
import java.sql.SQLException; 
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author roman
 */
public class Enroll {

    public EnrollInformation enrollFinger(EnrollInformation enrollInformation) {
 
    /*    UserFingerData customerFingerData = new UserFingerData(); 
        customerFingerData.setCustNo(Long.parseLong(enrollInformation.getName()));
        addDatatoUserFingerData(customerFingerData,"LINDEX",enrollInformation.getlIndex());
        addDatatoUserFingerData(customerFingerData,"LTHUMB",enrollInformation.getlThumb());
        addDatatoUserFingerData(customerFingerData,"LMIDDLE",enrollInformation.getlMiddle());
        addDatatoUserFingerData(customerFingerData,"LRING",enrollInformation.getlRing());
        addDatatoUserFingerData(customerFingerData,"LLITTLE",enrollInformation.getlLittle());
        addDatatoUserFingerData(customerFingerData,"RINDEX",enrollInformation.getrIndex());
        addDatatoUserFingerData(customerFingerData,"RTHUMB",enrollInformation.getrThumb());
        addDatatoUserFingerData(customerFingerData,"RMIDDLE",enrollInformation.getrMiddle());
        addDatatoUserFingerData(customerFingerData,"RRING",enrollInformation.getrRing());
        addDatatoUserFingerData(customerFingerData,"RLITTLE",enrollInformation.getrLittle());
           
        boolean matchingFlag = checkWhetherClientMatchesAgent(customerFingerData);

        if (matchingFlag) {
            enrollInformation.setErrorFlag("W");
            enrollInformation.setErrorMessage("Agent's Finger is matched with Customer");
            return enrollInformation;
        }   */

        try {
            DBConnectionHandler dbConnectionHandler = new DBConnectionHandler();
            Connection con = dbConnectionHandler.getConnection();
            System.out.println("con : "+ con);
            String sql = "INSERT INTO Biotpl.FP_ENROLL(CUST_NO,CREATE_DATE,"
                    + "CREATE_BY,DEVICE_ID,LINDEX,LTHUMB,RINDEX,RTHUMB,LMIDDLE,LRING,LLITTLE,"
                    + "RMIDDLE,RRING,RLITTLE,ENROLL_STATUS,FINGER1,FINGER2,STANDARD)"
                    + "VALUES (?,sysdate,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, Long.parseLong(enrollInformation.getName()));
            pstmt.setString(2, enrollInformation.getApp_user());
            pstmt.setString(3, enrollInformation.getSerial());

            pstmt.setBytes(4, enrollInformation.getlIndex());
            pstmt.setBytes(5, enrollInformation.getlThumb());
            pstmt.setBytes(6, enrollInformation.getrIndex());
            pstmt.setBytes(7, enrollInformation.getrThumb());

            pstmt.setBytes(8, enrollInformation.getlMiddle());
            pstmt.setBytes(9, enrollInformation.getlRing());
            pstmt.setBytes(10, enrollInformation.getlLittle());

            pstmt.setBytes(11, enrollInformation.getrMiddle());
            pstmt.setBytes(12, enrollInformation.getrRing());
            pstmt.setBytes(13, enrollInformation.getrLittle());
            pstmt.setString(14, "Y");
            pstmt.setString(15, "Y");
            pstmt.setString(16, "Y");
            pstmt.setString(17, "S");
            pstmt.executeUpdate();

            dbConnectionHandler.releaseConnection(con);
            enrollInformation.setErrorFlag("N");
            enrollInformation.setErrorMessage("Successfully Insert.");
            System.out.println("Finger Insert Successfully. "+enrollInformation.getErrorMessage());
            return enrollInformation;
        } catch (SQLException ex) {
            System.out.println("Exception raised. Finger does not enroll. Error is : "+ ex);
            Logger.getLogger(Enroll.class.getName()).log(Level.SEVERE, null, ex);
            enrollInformation.setErrorFlag("Y");
            enrollInformation.setErrorMessage(ex.getMessage().substring(0, 59));
            return enrollInformation;
        }
        
        //return enrollInformation;
    }

    private boolean checkWhetherClientMatchesAgent(UserFingerData customerFingerData) {
        List<Long> agentNumber = getAgentNumber(customerFingerData.getCustNo());
        List<FingerDataWithName> custFingerData = customerFingerData.getFingerDataWithName();
        boolean matchingFlag = false;
        
        FingerMatching fingerMatching = new FingerMatching();
        for (int i = 0; i < custFingerData.size(); i++) {
            for (int j = 0; j < agentNumber.size(); j++) {
                UserFingerData agentData = getAgentData(agentNumber.get(j)); 
                if(agentData == null)continue;
                else{
                    matchingFlag = fingerMatching.fingerPrintIdentify(customerFingerData.getCustNo()
                            , custFingerData.get(i).getFingerName(),
                                custFingerData.get(i).getFingerData(),agentData.getCustNo(), agentData.getFingerDataWithName());
                    if (matchingFlag) {
                        return true;
                    }
                }
            }
        }
        return matchingFlag;
    }

    private UserFingerData getAgentData(long agentNo) {
         
        UserFingerData agentData = new UserFingerData();
        agentData.setCustNo(agentNo);
                
        DBConnectionHandler dbConnectionHandler = new DBConnectionHandler();
        Connection conn = dbConnectionHandler.getConnection();
        
        byte[][] data = new byte[10][];
        try {

            String selectSQL = "SELECT LINDEX,LTHUMB,RINDEX,RTHUMB,LMIDDLE,LRING,LLITTLE,"
                    + "RMIDDLE,RRING,RLITTLE from  Biotpl.FP_ENROLL where  CUST_NO = ?";
            System.out.println(selectSQL);
            PreparedStatement preparedStatement = conn.prepareStatement(selectSQL);
            preparedStatement.setLong(1,agentNo);

            ResultSet rs = preparedStatement.executeQuery();
            if (rs == null) {
                System.out.println("Resultset is null");
            }

            
            if (rs.next()) {
                System.out.println("Yes we are done");
                byte[] LINDEX = getByteDataFromBlob(rs.getBlob("LINDEX"));
                addDatatoUserFingerData(agentData,"LINDEX",LINDEX);
                
                byte[] LTHUMB = getByteDataFromBlob(rs.getBlob("LTHUMB"));
                addDatatoUserFingerData(agentData,"LTHUMB",LTHUMB);
                
                byte[] RINDEX = getByteDataFromBlob(rs.getBlob("RINDEX"));
                addDatatoUserFingerData(agentData,"RINDEX",RINDEX);
                
                byte[] RTHUMB = getByteDataFromBlob(rs.getBlob("RTHUMB"));
                addDatatoUserFingerData(agentData,"RTHUMB",RTHUMB);

                byte[] LMIDDLE = getByteDataFromBlob(rs.getBlob("LMIDDLE"));
                addDatatoUserFingerData(agentData,"LMIDDLE",LMIDDLE);
                
                byte[] LRING = getByteDataFromBlob(rs.getBlob("LRING"));
                addDatatoUserFingerData(agentData,"LRING",LRING);
                
                byte[] LLITTLE = getByteDataFromBlob(rs.getBlob("LLITTLE"));
                addDatatoUserFingerData(agentData,"LLITTLE",LLITTLE);

                byte[] RMIDDLE = getByteDataFromBlob(rs.getBlob("RMIDDLE"));
                addDatatoUserFingerData(agentData,"RMIDDLE",RMIDDLE);
                
                byte[] RRING = getByteDataFromBlob(rs.getBlob("RRING"));
                addDatatoUserFingerData(agentData,"RRING",RRING);
                
                byte[] RLITTLE = getByteDataFromBlob(rs.getBlob("RLITTLE"));
                addDatatoUserFingerData(agentData,"RLITTLE",RLITTLE);
                  
                return agentData ; 
            }
        } catch (SQLException ex) {
            Logger.getLogger(FingerPrintVerification.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        } 
        
        dbConnectionHandler.releaseConnection(conn);
        return null;
    }
    
    void addDatatoUserFingerData(UserFingerData agentData,String fingerName , byte[] fingerData){
        if(fingerData != null){
            FingerDataWithName fingerDataWithName = new FingerDataWithName(fingerName,fingerData);
            agentData.addFingerDataWithName(fingerDataWithName); 
        }
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

    private List<Long> getAgentNumber(long custNo) {
        try {
            List<Long> list = new ArrayList<Long>();
            DBConnectionHandler dbConnectionHandler = new DBConnectionHandler();
            Connection conn = dbConnectionHandler.getConnection();
            
            CallableStatement cs = conn.prepareCall("begin BIOTPL.GET_CUSTOMER_LIST.GET_AGENT_CUST_NO(?,?,?); end;");
            cs.setLong(1, custNo);  //cs.setString(1, listStrCust.get(i));
            cs.setString(2, "S");
            cs.registerOutParameter(3, java.sql.Types.ARRAY, "CUS_NO_LIST");
            cs.execute();
            
            Array arrAgentNo = cs.getArray(3);
            Map map = conn.getTypeMap();
            map.put("CUSTOMER_NO_LIST", Class.forName("com.era.FingerCheck.AgentCustoNo"));
            Object[] values = (Object[]) arrAgentNo.getArray(); 
            
            for (int k = 0; k < values.length; k++) {
                AgentCustoNo agent = (AgentCustoNo) values[k];
                list.add(agent.agentCustNO);
            }
            
            dbConnectionHandler.releaseConnection(conn);
            return list;
        } catch (SQLException ex) {
            Logger.getLogger(Enroll.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Enroll.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private byte[][] getNonNullData(byte[][] data) {
        if (data == null) {
            return null;
        }
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

    public static void main(String[] args) {
        Enroll enroll = new Enroll();
        EnrollInformation enrollInformation = new EnrollInformation();
        enrollInformation.setName("208");
        enroll.enrollFinger(enrollInformation);
    }

}
