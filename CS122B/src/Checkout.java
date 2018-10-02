

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import com.google.gson.JsonObject;
/**
 * Servlet implementation class Checkout
 */
@WebServlet("/Checkout")
public class Checkout extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Checkout() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doGet(request, response);
		PrintWriter out = response.getWriter();
		
//		String loginUser = "mytestuser";
//        String loginPasswd = "mypassword";
//        String loginUrl = "jdbc:mysql://localhost:3306/moviedb?serverTimezone=UTC";
        
        String lastname = request.getParameter("lastname");
		String firstname = request.getParameter("firstname");
		String creditnumber = request.getParameter("creditnumber");
		String expiredate = request.getParameter("expiredate");
		System.out.println("NAME: " + lastname);
		
		String query = "SELECT * FROM creditcards WHERE id='" + creditnumber 
        		+ "' AND firstName='" + firstname 
        		+ "' AND lastName='" + lastname
        		+ "' AND expiration='" + expiredate + "';";
		System.out.println(query);
		
		try {
			Context initCtx = new InitialContext();
			if (initCtx == null)
                out.println("initCtx is NULL");
			
			Context envCtx = (Context) initCtx.lookup("java:comp/env");
			if (envCtx == null)
                out.println("envCtx is NULL");
			
			// Look up our data source
            DataSource ds = (DataSource) envCtx.lookup("jdbc/TestDB");
            
            //the following commented lines are direct connections without pooling
            //Class.forName("org.gjt.mm.mysql.Driver");
            //Class.forName("com.mysql.jdbc.Driver").newInstance();         
            //Connection connection = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
            
            if (ds == null)
                out.println("ds is null.");
            
            Connection dbcon = ds.getConnection();
            if (dbcon == null)
                out.println("dbcon is null.");

            Statement statement = dbcon.createStatement();
       
            ResultSet rs = statement.executeQuery(query);
            System.out.println(rs);
            
            JsonObject responseJsonObject = new JsonObject();
            if (rs.next()) {
        	    responseJsonObject.addProperty("status", "success");
    			responseJsonObject.addProperty("message", "success");
    			responseJsonObject.addProperty("lastname", lastname);
    			responseJsonObject.addProperty("firstname", firstname);
            }  else {
    			responseJsonObject.addProperty("status", "fail");
            }
    			
            out.write(responseJsonObject.toString());
            System.out.println(responseJsonObject.toString());
            
            rs.close();
            statement.close();
            dbcon.close();
            
        } catch (Exception e) {
            out.println("<HTML>" + "<HEAD><TITLE>" + "Login: Error" + "</TITLE></HEAD>\n<BODY>"
                    + "<P>SQL error in doPost: " + e.getMessage() + "</P></BODY></HTML>");
            return;
        }
		out.close();
	}

}
