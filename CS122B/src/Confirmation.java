

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Iterator;
import java.util.Map;
import java.util.Date;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import com.google.gson.JsonObject;

/**
 * Servlet implementation class Confirmation
 */
@WebServlet("/Confirmation")
public class Confirmation extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Confirmation() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		PrintWriter out = response.getWriter();
        
        HttpSession session = request.getSession(true);
		User user = (User)session.getAttribute("user");
	
//		String loginUser = "mytestuser";
//        String loginPasswd = "mypassword";
//        String loginUrl = "jdbc:mysql://localhost:3306/moviedb?serverTimezone=UTC";
		
        String lastname = request.getParameter("lastname");
		String firstname = request.getParameter("firstname");
		
		String customer = "SELECT id FROM customers WHERE firstName = '" +
        		firstname + "' and lastName='" +
        		lastname + "';";
        System.out.println(customer);
        
        try {
        	Context initCtx = new InitialContext();
			if (initCtx == null)
                out.println("initCtx is NULL");
			
			Context envCtx = (Context) initCtx.lookup("java:comp/env");
			if (envCtx == null)
                out.println("envCtx is NULL");
			
			// Look up our data source
            DataSource ds = (DataSource) envCtx.lookup("jdbc/masterDB");
            
            //the following commented lines are direct connections without pooling
            //Class.forName("org.gjt.mm.mysql.Driver");
            //Class.forName("com.mysql.jdbc.Driver").newInstance();         
            //Connection connection = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
            
            if (ds == null)
                out.println("ds is null.");
            
            Connection dbcon = ds.getConnection();
            if (dbcon == null)
                out.println("dbcon is null.");
            
            Statement searchCustomer = dbcon.createStatement();
            Statement searchMovie = dbcon.createStatement(); 
            Statement update = dbcon.createStatement();
            
            ResultSet rs_c = searchCustomer.executeQuery(customer);
            
            int customerId = 0;
            while (rs_c.next()) {
            	customerId = rs_c.getInt(1);
            }
            
            Iterator it = user.cart.entrySet().iterator();
    		
    		while (it.hasNext()) {
    			Map.Entry entry = (Map.Entry) it.next();
                String key = entry.getKey().toString();  
                int value = (int)(entry.getValue()); 
                
                String movie = "SELECT id FROM movies WHERE title ='" + key + "';";
                System.out.println(movie);
                ResultSet rs_m = searchCustomer.executeQuery(movie);
                
                String movie_id = "";
                while (rs_m.next()) {
                	movie_id = rs_m.getString(1);
                }
                System.out.println(movie_id);
                
                Date time= new java.sql.Date(new java.util.Date().getTime());
                System.out.println(time);
                for (int i=0 ; i<value; i++) {
                String insert = "INSERT INTO sales VALUES(0," + 
                		customerId + ", '" +
                		movie_id + "', '" +
                		time + "');";
                System.out.println(insert);
                update.executeUpdate(insert);
                }  
              
    		}   
    		JsonObject responseJsonObject = new JsonObject();
			responseJsonObject.addProperty("status", "success");
			responseJsonObject.addProperty("message", "success");
			user.cart.clear();
			
			response.getWriter().write(responseJsonObject.toString());
        	
    		rs_c.close();
        	searchCustomer.close();
        	searchMovie.close();
        	update.close();        	
        	dbcon.close();
        
        } catch (Exception e) {
            out.println("<HTML>" + "<HEAD><TITLE>" + "Error" + "</TITLE></HEAD>\n<BODY>"
                    + "<P>SQL error in doPost: " + e.getMessage() + "</P></BODY></HTML>");
            return;
        }
        out.close();
	}

}