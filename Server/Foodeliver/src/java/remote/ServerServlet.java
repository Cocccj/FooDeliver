package remote;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ServerServlet
 */
@WebServlet("/ServerServlet")
public class ServerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	// JDBC driver name and database URL
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_RUL = "jdbc:mysql://localhost:3306/Foodelivery";
		
	// Database credentials
	static final String USER = "root";
	static final String PASSWORD = "";
	
	private static Connection conn = null;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServerServlet() {
    	try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_RUL, USER, PASSWORD);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter writer = new PrintWriter(response.getOutputStream());
    	// parse parameters
    	String sql = request.getParameter("sql");
    	String method = request.getParameter("method");
    	System.out.println(sql);
    	System.out.println(method);
    	
    	// prepare sql
    	Statement stmt;
		try {
			stmt = conn.createStatement();
			
			// decide methods
	    	if (method.equals("read")){
	    		// execute read operation
	    		StringBuilder sb = new StringBuilder();
	    		ResultSet rs = stmt.executeQuery(sql);
	    		ResultSetMetaData rsmd = rs.getMetaData();
	    		int columnNumber = rsmd.getColumnCount();
	    		while (rs.next()){
	    			for (int i = 1; i <= columnNumber; i++){
	    				sb.append(rs.getString(i)).append("\t");
	    			}
	    			sb.append("\n");
	    		}
	    		writer.write(sb.toString());
	    	} else {
	    		// execute update operation
	    		stmt.executeUpdate(sql);
	    		writer.write("True");
	    	}
		} catch (SQLException e) {
			writer.write("False");
			e.printStackTrace();
		} finally {
                    if (writer != null) {
                        writer.close();
                    }
                }
	}
}