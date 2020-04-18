package finger.servlet;

import era.data.CheckUserAndDevice;
import era.data.EnrollStatusFromDB; 
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.json.JSONObject; 

/**
 *
 * @author sultan
 */
@WebServlet(name = "EnrollStatus", urlPatterns = {"/EnrollStatus"})
public class EnrollStatus extends HttpServlet {

  
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet EnrollStatus</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet EnrollStatus at " + request.getContextPath() + "</h1>");
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
 
            String name = request.getParameter("name");   
                System.out.println("Delete Cust No : "+ name);
            Long nameCust = Long.parseLong(name);

            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
            JSONObject json = new JSONObject();

            EnrollStatusFromDB enrollStatusFromDB = new EnrollStatusFromDB();
            Map<String, Object> map = new HashMap<String, Object>();
            
            CheckUserAndDevice checkUserAndDevice =  enrollStatusFromDB.getEnrollStatus(nameCust); 
            if(checkUserAndDevice.isEnrollStatus()){
                map.put("enrollStatus","E");
                map.put("standard",checkUserAndDevice.getStandard());
            }
            else{
                map.put("enrollStatus","NE");
                map.put("standard",checkUserAndDevice.getStandard());
            }
            map.put("errorFlag", "N");
            map.put("errorMessage", "No error Message");

            list.add(map);
            json.put("enrollStatus", list);
            
            //System.out.println("EnrollNodes = " + list);
            response.addHeader("Access-Control-Allow-Headers", "Content-Type");
            response.addHeader("Access-Control-Allow-Origin", "*");
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().print(json.toString());
            //System.out.println(json.toString());
            response.getWriter().flush();
            // System.out.println("Response Completed... ");
        } catch (IOException | JSONException ex) {
            Logger.getLogger(EnrollStatus.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
