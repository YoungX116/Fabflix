import java.io.*;
import java.text.NumberFormat;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Log
 */
@WebServlet("/Log")
public class Log extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Log() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		response.setContentType("text/html");
		String query = request.getParameter("query");
        PrintWriter out = response.getWriter();
		
		out.println("total queries: " + big(average(query + ".txt")[0]) + "<br/>");
		out.println("total ts: " + big(average(query + ".txt")[3]) + "<br/>");
		out.println("total tj: " + big(average(query + ".txt")[4]) + "<br/>");
		
		out.println("average ts: " + big(average(query + ".txt")[1]) + "<br/>");
		out.println("average tj: " + big(average(query + ".txt")[2]) + "<br/>");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	public double[] average(String filename) {
		//String path = this.getServletContext().getRealPath("\\WEB-INF\\" + filename);
		String path ="/home/ubuntu/tomcat/webapps/CS122B/WEB-INF/" + filename;
		double[] average = new double[5];
		
		FileInputStream in = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		
		try {						
			in = new FileInputStream(path);
			isr = new InputStreamReader(in);
			br = new BufferedReader(isr);
			
			int count = 0;
			
			String[] ts_tj;
			double ts = 0;
			double tj = 0;
			
			String line = new String();
			while ((line = br.readLine()) != null) {
				ts_tj = line.split(",");
				ts += Double.parseDouble(ts_tj[0]);
				tj += Double.parseDouble(ts_tj[1]);
				count ++;	
			}	
			
			//calculate average ts and tj:
			average[0] = count;
			average[1] = ts / count;
			average[2] = tj / count;
			average[3] = ts;
			average[4] = tj;
			
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		
		return average;	
	}
	
	private static String big(double d) {
        NumberFormat nf = NumberFormat.getInstance();      
        nf.setGroupingUsed(false);      
        return nf.format(d);
    }

}