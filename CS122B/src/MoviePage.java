

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

/**
 * Servlet implementation class MoviePage
 */
@WebServlet("/MoviePage")
public class MoviePage extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MoviePage() {
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
		
        String movie_id = request.getParameter("movie_id");
		
		String query = "SELECT me.id, me.title, me.year, me.director, me.star, me.genre, COALESCE(ROUND(me.rating, 1), 0), me.starId "
    			+ "FROM movies_extend AS me WHERE me.id ='" + movie_id + "';";
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
            
            // Declare our statement
            Statement statement = dbcon.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            
            // Perform the query
            ResultSet rs = statement.executeQuery(query);
            
            JsonArray movies = new JsonArray();
            HandleResultSet.parseRS(rs, movies);
            
            System.out.println(movies);
            out.write(movies.toString());
            
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
