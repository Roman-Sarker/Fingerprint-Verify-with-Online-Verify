
package admin.servlet;
  
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Properties; 
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import resource.GetFile;

/**
 *
 * @author sultan
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/LoginServlet"})
public class LoginServlet extends HttpServlet {


    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet LoginServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet LoginServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String uNameFromDB = "", pwdFromDB = "", fName = "", lName = "", username, password;
        Properties prop = new Properties();
        InputStream inputStream = null;

        try {
            File file = GetFile.getFile("admin_Ba.properties");
            inputStream = new FileInputStream(file);
            prop.load(inputStream);
            uNameFromDB = prop.getProperty("username");
            pwdFromDB = prop.getProperty("password");
            fName = prop.getProperty("fName");
            lName = prop.getProperty("lName"); 
            inputStream.close();
        } catch (FileNotFoundException e) {
            uNameFromDB = "Era";
            pwdFromDB = "era@123";
            fName = "Era";
            lName = "InfoTech";
        } 
//        catch(Exception ex){
//            uNameFromDB = "Era";
//            pwdFromDB = "era@123";
//            fName = "Era";
//            lName = "InfoTech";
//        }
        

        username = request.getParameter("username");
        password = request.getParameter("password");
        String redirectURL = "admin/";

        if (username.equals(uNameFromDB) && password.equals(pwdFromDB)) {
            HttpSession session = request.getSession();
            session.setAttribute("LOGIN", "TRUE");
            session.setAttribute("fName", "Era");
            session.setAttribute("lName", "InfoTech");
            session.setAttribute("uName", username);
            response.sendRedirect(redirectURL + "home.jsp");
        } else {
            response.sendRedirect(redirectURL + "index.jsp?eMessage=username or password does not exist");
        }

        
    }

}
