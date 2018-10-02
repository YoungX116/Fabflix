

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

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * Servlet implementation class StarPage
 */
@WebServlet("/StarPage")
public class StarPage extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public StarPage() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
//		String loginUser = "mytestuser";
//		String loginPasswd = "mypassword";
//		String loginUrl = "jdbc:mysql://localhost:3306/moviedb?serverTimezone=UTC";
		
		response.setContentType("application/json"); // Response mime type
		
		// Output stream to STDOUT
        PrintWriter out = response.getWriter();
		
        String star_id = request.getParameter("star_id");
		
		String query = "SELECT * FROM (SELECT * FROM movies_star_extend AS mse WHERE mse.starId='" + star_id + "') AS tmp1 " + 
						"LEFT JOIN (SELECT id, IFNULL(birthyear, 0) FROM stars) AS tmp2 ON tmp1.starId = tmp2.id;";
		System.out.println("query: " + query);
		
		
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
            
            // Declare statement
            Statement statement = dbcon.createStatement();
            
            // Perform the query
            ResultSet rs = statement.executeQuery(query);
            
            JsonArray star = new JsonArray();
            
            while (rs.next()) {
            	JsonObject responseJsonObject = new JsonObject();
            	
            	String movie_id = rs.getString(1);
            	String movie_title = rs.getString(2);
            	String star_name = rs.getString(6);
            	int birth_year = rs.getInt(8);
            	
            	responseJsonObject.addProperty("movie_id", movie_id);
            	responseJsonObject.addProperty("movie_title", movie_title);
            	responseJsonObject.addProperty("star_name", star_name);
            	responseJsonObject.addProperty("birth_year", birth_year);
            	
            	star.add(responseJsonObject);
            	
            }	
         
            System.out.println(star.toString());
            out.write(star.toString());
            
            rs.close();
            statement.close();
            dbcon.close();
		} catch (Exception e) {
        	out.println("<HTML>" + "<HEAD><TITLE>" + "MovieDB: Error" + "</TITLE></HEAD>\n<BODY>"
                    + "<P>SQL error in doGet: " + e.getMessage() + "</P></BODY></HTML>");
            return;
        }
		out.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
