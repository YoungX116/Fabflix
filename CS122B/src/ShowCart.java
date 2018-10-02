

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * Servlet implementation class ShowCart
 */
@WebServlet("/ShowCart")
public class ShowCart extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ShowCart() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		
		HttpSession session = request.getSession(true);
		User user = (User)session.getAttribute("user");
		JsonArray jsonArray = new JsonArray();
		if (user.cart != null) {
			Iterator it = user.cart.entrySet().iterator();
			
			while (it.hasNext()) {
				Map.Entry entry = (Map.Entry) it.next();
	            String key = entry.getKey().toString();  
	            String value = entry.getValue().toString();  
	            
	            JsonObject jsonObject = new JsonObject();
	            jsonObject.addProperty("movie", key);
	            jsonObject.addProperty("quantity", value);
	            jsonArray.add(jsonObject);
			}	
		}
		out.write(jsonArray.toString());
		System.out.println(jsonArray.toString());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
