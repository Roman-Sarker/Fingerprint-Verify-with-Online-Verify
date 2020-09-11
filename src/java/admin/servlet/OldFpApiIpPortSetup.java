
package admin.servlet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Properties;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author roman
 */
@WebServlet(name = "OldFpApiIpPortSetup", urlPatterns = {"/OldFpApiIpPortSetup"})
public class OldFpApiIpPortSetup extends HttpServlet {

    
    

    
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String ip = request.getParameter("IP");
        String port = request.getParameter("PORT");

        if (ip == null || ip.equals("")) {
            response.sendRedirect("admin/SetOldFpApiIp.jsp?eMessage=" + "IP can not be blank!");
        } else if (port == null || port.equals("")) {
            response.sendRedirect("admin/SetOldFpApiIp.jsp?eMessage=" + "Port can not be blank!");
        } else { 

            Properties prop = new Properties();
            try {
                File file = new File("OldFpApiIpPort_Ba.properties");
                OutputStream outputStream = new FileOutputStream(file);
                prop.setProperty("IP", ip);
                prop.setProperty("PORT", port);
                prop.store(outputStream, null);
                outputStream.close();
                response.sendRedirect("admin/SetOldFpApiIp.jsp?sMessage=Successfully IP and Port Created");
            } catch (IOException ioe) {
                response.sendRedirect("admin/SetOldFpApiIp.jsp?eMessage=" + ioe.getMessage());
            }
        }
    }

    

}
