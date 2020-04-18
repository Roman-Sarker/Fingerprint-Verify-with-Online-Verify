package com.era.onlineVerify;




import era.dbconnectivity.*;
import java.sql.Connection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import era.data.EnrollInformation;
import java.util.HashMap;
import com.era.onlineVerify.*;
/**
 *
 * @author roman
 */
public class AllAgentOfPoint {

    public EnrollInformation getAgentNo(EnrollInformation enrollInformation, HashMap<String, byte[]> mapCusFinDat) {

        HashMap<Integer, Integer> strAgentHM = new HashMap<Integer, Integer>();
        DBConnectionHandler dbConnectivity = new DBConnectionHandler();
        String standard = "S";
        Connection con = null;
        CallableStatement cs = null;
        try {

            con = dbConnectivity.getConnection();
            Array arrCusNo = null;

            try {
                cs = con.prepareCall("begin BIOTPL.GET_AGENT_CUST_NO_ONLINE(?,?,?); end;");
                cs.setString(1, enrollInformation.getApp_user()); // Cust No  //cs.setString(1, listStrCust.get(i));
                cs.setString(2, standard);         // Standard
                cs.registerOutParameter(3, java.sql.Types.ARRAY, "CUS_NO_LIST"); // Agent No of that cust no
                cs.execute();
                arrCusNo = cs.getArray(3);
            } catch (Exception e) {
                System.out.println("Exception raised to call 'BIOTPL.GET_AGENT_CUST_NO_ONLINE()' package. Error is : " + e.toString());
                cs.close();
            }

            Map map = con.getTypeMap();
            map.put("CUSTOMER_NO_LIST", Class.forName("era.dbconnectivity.TestArr"));
            Object[] values = (Object[]) arrCusNo.getArray(); /// Customer No. here.

            for (int i = 0; i < values.length; i++) {
                TestArr a = (TestArr) values[i];
                //listStrAgent.add(a.attrOne);
                strAgentHM.put(a.attrOne, a.attrTwo);
            }

            System.out.println("Agents No : " + strAgentHM.size());
            if (strAgentHM.size() >= 1) {

                // beanAgentNo.setListStrTotalAgent(listStrAgent);
                System.out.println("Total Agent of Point = " + strAgentHM.size());
                try {
                    MatchFingerDataFtr matchFingerData = new MatchFingerDataFtr();

                    // *** Go for matching ***
                    if ("S".equals(standard) || standard.equals("S")) {
                        System.out.println("==== Start online matching ====");
                        enrollInformation = matchFingerData.matchingProcess(enrollInformation, mapCusFinDat, strAgentHM);

                    }

                } catch (Exception e) {
                    enrollInformation.setErrorFlag("Y");
                    enrollInformation.setErrorMessage("Online matching failed. Error is: " + e.getMessage().toString());
                    System.out.println("Online matching failed. Error is: " + e.getMessage().toString());
                }
            } else {
                System.out.println("No agent found in this outlet.");
                enrollInformation.setErrorFlag("Y");
                enrollInformation.setErrorMessage("No standard agent found.");
                return enrollInformation;
            }

            // Calling to matching process--------------------------------------------------------------
        } catch (SQLException sEx) {
            enrollInformation.setErrorFlag("Y");
            enrollInformation.setErrorMessage("Faced SQL Exception. Error is : "+sEx.toString());
            System.out.println("Faced SQL Exception. Error is : " + sEx.getMessage().toString());
            return enrollInformation;

        } catch (Exception ex) {
            enrollInformation.setErrorFlag("Y");
            enrollInformation.setErrorMessage("Exception faced at online verifying. Error is: " + ex.toString());
            System.out.println("Exception faced at online verifying. Error is: " + ex.getMessage().toString());
            return enrollInformation;
        } finally {
            dbConnectivity.releaseConnection(con);

        }

        return enrollInformation;
    }

// main method only for testing purpose.
    public static void main(String[] args) {
        AllAgentOfPoint allAgentOfPoint = new AllAgentOfPoint();
       // BeanCustNoFP beanCustNoFP = new BeanCustNoFP();
       // BeanCustNoFP beanCustNoFPResult = allAgentOfPoint.getAgentNo(beanCustNoFP, "S");
       // System.out.println(beanCustNoFPResult);
    }
}
