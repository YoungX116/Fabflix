

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

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
 * Servlet implementation class SearchSuggestion
 */
@WebServlet("/SearchSuggestion")
public class SearchSuggestion extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SearchSuggestion() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    /*
     * 
     * Match the query against Marvel and DC heros and return a JSON response.
     * 
     * For example, if the query is "super":
     * The JSON response look like this:
     * [
     * 	{ "value": "Superman", "data": { "category": "dc", "heroID": 101 } },
     * 	{ "value": "Supergirl", "data": { "category": "dc", "heroID": 113 } }
     * ]
     * 
     * The format is like this because it can be directly used by the 
     *   JSON auto complete library this example is using. So that you don't have to convert the format.
     *   
     * The response contains a list of suggestions.
     * In each suggestion object, the "value" is the item string shown in the dropdown list,
     *   the "data" object can contain any additional information.
     * 
     * 
     */

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
//		String loginUser = "mytestuser";
//        String loginPasswd = "mypassword";
//        String loginUrl = "jdbc:mysql://localhost:3306/moviedb?serverTimezone=UTC";
		
        // Output stream to STDOUT
        PrintWriter out = response.getWriter();
        
        String query = request.getParameter("query");
		 
        String ftSearch = HandleTokenizer.tokenizer(query);
        
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

			//PreparedStatement search = null;
			//String searchMovie = null;
			//String searchStar = null;
			
			StringBuffer searchMovie = new StringBuffer();
			StringBuffer searchStar = new StringBuffer();
			
			// get full-text search sentence use "OR" to connect the full-text search and fuzzy search
			searchMovie.append("SELECT * FROM movies WHERE MATCH(title) AGAINST (? IN BOOLEAN MODE);");
			searchStar.append("SELECT * FROM stars WHERE MATCH(name) AGAINST (? IN BOOLEAN MODE);");
	
			JsonArray jsonArray = new JsonArray();
			
			// return the empty json array if query is null or empty
			if (query == null || query.trim().isEmpty()) {
				response.getWriter().write(jsonArray.toString());
				return;
			}	
		
			PreparedStatement stmt1 = dbcon.prepareStatement(searchMovie.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			stmt1.setString(1, ftSearch);
			ResultSet rs1 = stmt1.executeQuery();
			
			while (rs1.next()) {
				String id = rs1.getString(1);
				String result = new String(rs1.getString(2).getBytes("UTF-8"), "ISO-8859-1");
				jsonArray.add(generateJsonObject(result, "Movie", id));
			}
			
			PreparedStatement stmt2 = dbcon.prepareStatement(searchStar.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			stmt2.setString(1, ftSearch);
			ResultSet rs2 = stmt2.executeQuery();
					
			while (rs2.next()) {
				String id = rs2.getString(1);
				String result = new String(rs2.getString(2).getBytes("UTF-8"), "ISO-8859-1");
				jsonArray.add(generateJsonObject(result, "Star", id));
			}
			
			while (jsonArray.size() > 10) {
				jsonArray.remove(jsonArray.size()-1);
			}
			
			System.out.println(jsonArray.toString());
			out.write(jsonArray.toString());
			
			rs1.close();
			rs2.close();
            stmt1.close();
            stmt2.close();
            dbcon.close();
		} catch (Exception e) {
			System.out.println(e);
			response.sendError(500, e.getMessage());
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
	
	private static JsonObject generateJsonObject(String result, String category, String id) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("value", result);
		
		JsonObject additionalDataJsonObject = new JsonObject();
		additionalDataJsonObject.addProperty("category", category);
		additionalDataJsonObject.addProperty("ID", id);
		
		jsonObject.add("data", additionalDataJsonObject);
		return jsonObject;
	}

}
