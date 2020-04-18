package admin.servlet;

import era.dbconnectivity.DBConnectionHandler;
import java.io.IOException; 
import java.sql.Connection;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "Database_Connectivity_Check", urlPatterns = {"/Database_Connectivity_Check"})
public class Database_Connectivity_Check extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        DBConnectionHandler dbConnectionHandler = new DBConnectionHandler();
        Connection conn = dbConnectionHandler.getConnection();
        String redirectURL = "admin/";

        if (conn == null) {
            response.sendRedirect(redirectURL + "dbInfo.jsp?dbeMessage=" + dbConnectionHandler.getErrorMessge());
        } else {
            response.sendRedirect(redirectURL + "dbInfo.jsp?dbsMessage=DB Connection is Successful");
            DBConnectionHandler.releaseConnection(conn);
        } 
    }
}
