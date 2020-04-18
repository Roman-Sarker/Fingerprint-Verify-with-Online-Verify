
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


@WebServlet(name = "AdminRegServlet", urlPatterns = {"/AdminRegServlet"})
public class AdminRegServlet extends HttpServlet {
   
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String fName = request.getParameter("firstname");
        String lName = request.getParameter("lastname");
        String userName = request.getParameter("uName");
        String password = request.getParameter("passwd");
        String confirmPassword = request.getParameter("con_passwd");

        if (fName == null || fName.equals("")) {
            response.sendRedirect("admin/newAdmin.jsp?eMessage=" + "First Name can not be blank!");
        } else if (lName == null || lName.equals("")) {
            response.sendRedirect("admin/newAdmin.jsp?eMessage=" + "Last Name can not be blank!");
        } else if (userName == null || userName.equals("")) {
            response.sendRedirect("admin/newAdmin.jsp?eMessage=" + "Email can not be blank!");
        } else if (password == null || password.equals("")) {
            response.sendRedirect("admin/newAdmin.jsp?eMessage=" + "password can not be blank!");
        } else if (confirmPassword == null || confirmPassword.equals("")) {
            response.sendRedirect("admin/newAdmin.jsp?eMessage=" + "confirm password can not be blank!");
        } else if (!password.equals(confirmPassword)) {
            response.sendRedirect("admin/newAdmin.jsp?eMessage=password does not match");
        } else { 

            Properties prop = new Properties();
            try {
                File file = new File("admin_Ba.properties");
                OutputStream outputStream = new FileOutputStream(file);
                prop.setProperty("username", userName);
                prop.setProperty("password", password);
                prop.setProperty("fName", fName);
                prop.setProperty("lName", lName);
                prop.store(outputStream, null);
                outputStream.close();
                response.sendRedirect("admin/newAdmin.jsp?sMessage=Successfully new admin created");
            } catch (IOException ioe) {
                response.sendRedirect("admin/newAdmin.jsp?eMessage=" + ioe.getMessage());
            }
        }
    }  
}
