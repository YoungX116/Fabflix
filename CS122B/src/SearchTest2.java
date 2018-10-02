

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;

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
 * Servlet implementation class NormalSearch
 */
@WebServlet("/SearchTest2")
public class SearchTest2 extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SearchTest2() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String loginUser = "mytestuser";
        String loginPasswd = "mypassword";
        String loginUrl = "jdbc:mysql://localhost:3306/moviedb?serverTimezone=UTC";
		long startTime = System.nanoTime();
		
        response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		 
		String original = request.getParameter("query");
		String sorting = "rating";
		String order = "DESC";
		String ftSearch = HandleTokenizer.tokenizer(original);
		
		JsonObject searchingCondition = new JsonObject();
		searchingCondition.addProperty("query", original);
		
		String orderBy = "";
        if (order.equals("DESC")) {
        	orderBy = sorting + " DESC";
        }
        else {
        	orderBy = sorting;
        }
        
        String query = "SELECT me.id, me.title, me.year, me.director, me.star, me.genre, COALESCE(ROUND(me.rating, 1), 0), me.starId " +
        			"FROM movies_extend AS me WHERE MATCH(title) AGAINST (? IN BOOLEAN MODE) ORDER BY me." + orderBy + ";";
        System.out.println(query);
				 		 
		 try {
			long tjStart = System.nanoTime();
			 
			//Class.forName("org.gjt.mm.mysql.Driver");
			Class.forName("com.mysql.jdbc.Driver").newInstance();         
			Connection connection = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
			
			long tjEnd = System.nanoTime();
            long tj = tjEnd - tjStart;
			// Declare our statement
			//Statement statement = dbcon.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			
			PreparedStatement stmt = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			stmt.setString(1, ftSearch);
			ResultSet rs = stmt.executeQuery();
			
			JsonArray movies = new JsonArray();
			HandleResultSet.parseRS(rs, movies);
			movies.add(searchingCondition);
			
			System.out.println(movies);
			out.write(movies.toString());
			
			rs.close();
			stmt.close();
			connection.close();
			
			long endTime = System.nanoTime();
			long elapsedTime = endTime - startTime;
			
			File file = new File("/home/ubuntu/tomcat/webapps/CS122B/WEB-INF/log2.txt");
			PrintStream ps = new PrintStream(new FileOutputStream(file, true), true);
			ps.append(Long.toString(elapsedTime) + "," + Long.toString(tj)+"\n"); 
			
		 } catch(Exception e) {
            out.println("<HTML>" + "<HEAD><TITLE>" + "FullTestSearch: Error" + "</TITLE></HEAD><BODY>"
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