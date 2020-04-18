package era.dbconnectivity;
 
import com.era.admin.DBInfo;
import com.era.admin.GetDBInfo;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author roman
 */

public class DBConnectionHandler {

    Connection con = null;
    String errorMessge;

    public DBConnectionHandler() {
        errorMessge = "";
    }

    public Connection getConnection() {
        Connection con = null;

         DBInfo dbInfo = GetDBInfo.getDbInfo();
        if(dbInfo == null)
        {
             errorMessge = "Database Server Information is not available";
             return null;
        }
        
        String IPAdress = dbInfo.ip;  
        String PORT = dbInfo.portNo;
        String serviceName = dbInfo.serviceName;
        String URL = "jdbc:oracle:thin:@" + IPAdress + ":" + PORT + "/" + serviceName + "";
        String userName = dbInfo.userName;
        String password = dbInfo.password;  

     //   String URL = "jdbc:oracle:thin:@10.11.201.55:1525/iabdb";
      //  String userName = "biotpl";
      //  String password = "biotpl";
        
        
        //System.out.println("username: " + userName);
        //System.out.println("password: " + password);

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");  
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DBConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            con = DriverManager.getConnection(URL, userName, password);//database for xe      
        } catch (SQLException ex) {
            Logger.getLogger(DBConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            errorMessge = ex.getMessage();
        }  
        System.out.println(errorMessge);
        
        return con;
    }

    public boolean getConnectivityStatus() {
        DBConnectionHandler dbConnectionHandler = new DBConnectionHandler();
        Connection conn = dbConnectionHandler.getConnection();
        if (conn != null) {
            DBConnectionHandler.releaseConnection(conn);
            errorMessge = null;
            return true;
        } else {
            errorMessge = dbConnectionHandler.getErrorMessge();
            return false;
        }
    }

    public String getErrorMessge() {
        return errorMessge;
    }

    public static void releaseConnection(Connection con) {
        try {
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(DBConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        System.out.println("args = " + new DBConnectionHandler().getConnection());
    }

}
