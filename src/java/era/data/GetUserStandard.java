package era.data;

import era.dbconnectivity.DBConnectionHandler;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author roman
 */
public class GetUserStandard {

    public String getStandard(String cusNo) {
        String standard = null;
        DBConnectionHandler dbConnectionHandler = new DBConnectionHandler();
        Connection con = null;

        String selectSQL = "SELECT STANDARD from  BIOTPL.FP_ENROLL where CUST_NO = ?";
        //System.out.println("cusNo : " + cusNo);
        try {
            con = dbConnectionHandler.getConnection();

            PreparedStatement preparedStatement = con.prepareStatement(selectSQL);
            preparedStatement.setLong(1, Long.parseLong(cusNo));
            //System.out.println("PreparedStatement clear.");

            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                standard = rs.getString("STANDARD");
                //System.out.println("Standard : "+standard);
            }

        } catch (Exception e) {
            System.out.println("Fingerprint strandard retrive problem. Error is :" + e.toString());
        } finally {
            DBConnectionHandler.releaseConnection(con);
        }

        return standard;
    }

    public static void main(String[] args) {
        GetUserStandard getUserStandard = new GetUserStandard();
        System.out.println("Standard : " + getUserStandard.getStandard("2090785"));
    }
}
