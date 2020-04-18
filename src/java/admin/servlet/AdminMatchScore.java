
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
 * @author root
 */
@WebServlet(name = "AdminMatchScore", urlPatterns = {"/AdminMatchScore"})
public class AdminMatchScore extends HttpServlet {

 
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet AdminMatchScore</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet AdminMatchScore at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String matchScore = request.getParameter("matchValue");
        if (matchScore == "0" || matchScore.equals("0")) {
            response.sendRedirect("admin/matchingScore.jsp?eMessage=" + "Select a matching score.");
        }
        else{
            Properties prop = new Properties();
            try {
                File file = new File("MatchScore_Ba.properties");
                OutputStream outputStream = new FileOutputStream(file);
                prop.setProperty("matchScore", matchScore);
                prop.store(outputStream, null);
                outputStream.close();
                response.sendRedirect("admin/matchingScore.jsp?sMessage=Successfully changed matching score.");
            } catch (IOException ioe) {
                response.sendRedirect("admin/matchingScore.jsp?eMessage=" + ioe.getMessage());
            }
        }
    }

   
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
