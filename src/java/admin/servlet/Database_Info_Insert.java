package admin.servlet;
 
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream; 
import java.util.Properties;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import resource.GetFile;


@WebServlet(name = "Database_Info_Insert", urlPatterns = {"/Database_Info_Insert"})
public class Database_Info_Insert extends HttpServlet {
   
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String ip = request.getParameter("ip");
        String portNo = request.getParameter("portNo");
        String serviceName = request.getParameter("serviceName");
        String userName = request.getParameter("userName");
        String password = request.getParameter("password");
        String redirectURL = "admin/";

        Properties prop = new Properties();
        try {
            File file = new File("dbInfo_Ba.properties");
            OutputStream outputStream = new FileOutputStream(file);
            prop.setProperty("ip", ip);
            prop.setProperty("portNo", portNo);
            prop.setProperty("serviceName", serviceName);
            prop.setProperty("userName", userName);
            prop.setProperty("password", password);
            prop.store(outputStream, null);
            outputStream.close();
            response.sendRedirect(redirectURL + "dbInfo.jsp?sMessage=Database connectivity information is successfully updated!");

        } catch (IOException ioe) {
            response.sendRedirect(redirectURL + "dbInfo.jsp?eMessage=" + ioe.getMessage()); 
            ioe.printStackTrace();
        } 
    }  
}
