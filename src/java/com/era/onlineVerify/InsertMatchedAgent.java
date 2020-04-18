
package com.era.onlineVerify;

import era.dbconnectivity.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author roman
 */
public class InsertMatchedAgent {
        public void insertMatchingInformationInDatabase(String agentNo, String cust_no,String AgentfingerName, String CustFingername,String s) {
        
        DBConnectionHandler dbConnectionHandler = new DBConnectionHandler();
        Connection conn = null;    
            try {
                conn = dbConnectionHandler.getConnection();
            if (conn == null) {
                return;
            }
            
                System.out.println("agentNo : "+ agentNo);
                System.out.println("cust_no : "+ cust_no);
                System.out.println("AgentfingerName : "+ AgentfingerName);
                System.out.println("CustFingername : "+ CustFingername);
                System.out.println("AgentStandard : "+ s);
                String status = "ON";
            
            
            
// Store matching log data to MATCHING_TEMPLATE_INFO table
            // Insert to BIOTPL.finger_match_report table.
            String sql = "INSERT INTO BIOTPL.finger_match_report(AGENT_CUST_NO, CUSTOMER_CUST_NO, CREATE_DATE, CREATE_BY, AGENT_FINGER, CUSTOMER_FINGER, CUSTOMER_FINGER_DATA,  STANDARD, STATUS, AGENT_POINT_ID, USER_TYPE)\n"
                        + "                                 VALUES("+ agentNo + "," + cust_no + ", sysdate,       NULL,  '" + AgentfingerName + "','" + CustFingername + "',  null,   '" + s + "','" + status + "', null, null)"; //"+vFPdata+"

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.executeUpdate();
        
            /*
            // Insert to BIOTPL.MATCHING_TEMPLATE_INFO table.
            String insertTableSQL = "INSERT INTO BIOTPL.MATCHING_TEMPLATE_INFO"
                    + "(AGENT_CUST_NO, CUSTOMER_CUST_NO, AGENT_FINGER,CUSTOMER_FINGER, STANDARD)"
                    + "VALUES (?,?,?,?,?)";
            PreparedStatement preparedStatement = conn.prepareStatement(insertTableSQL);
            preparedStatement.setString(1, agentNo);
            preparedStatement.setString(2, cust_no);
            preparedStatement.setString(3, AgentfingerName);
            preparedStatement.setString(4, CustFingername);
            preparedStatement.setString(5, AgentStandard);
            preparedStatement.executeUpdate();
            */
            
        } catch (SQLException ex) {
            Logger.getLogger(InsertMatchedAgent.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Error is : "+ ex.toString());
        }catch(Exception e){
            e.printStackTrace();
        }
         finally{
            dbConnectionHandler.releaseConnection(conn);
        }
    }
}
