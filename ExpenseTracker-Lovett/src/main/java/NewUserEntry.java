

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class NewUserEntry
 */
@WebServlet("/NewUserEntry")
public class NewUserEntry extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public NewUserEntry() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String userName = request.getParameter("userName");
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String email = request.getParameter("email");
		String phone = request.getParameter("phone");
		String address = request.getParameter("address");
		String password = request.getParameter("password");
		Double startBal = Double.parseDouble(request.getParameter("startBal"));
				
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			DBConnection.getDBConnection();
			connection = DBConnection.connection;
			
			String selectSQL = "Select username from users where username = ?";
			preparedStatement = connection.prepareStatement(selectSQL);
			preparedStatement.setString(1, userName);
			
			ResultSet rs = preparedStatement.executeQuery() ;
			
			if(rs.next()) {
				out.println(userName + " is already taken. Please pick another <br>");
				connection.close();
				preparedStatement.close();
				
				out.println("<a href=/ExpenseTracker-Lovett/NewUser.html>Add User</a> <br>");
				out.println("<a href=/ExpenseTracker-Lovett/AddTransactions.html>Add Transaction</a> <br>");
				out.println("<a href=/ExpenseTracker-Lovett/AccountSummary.html>Account Summary</a> <br>");
				
			} else {
				out.println(userName + " is available <br>");
				connection.close();
				
				String insertSql = "INSERT INTO users (id,username, firstName, lastName, email, phone, address, password, startBal) "
						+ "values (default, ?, ?, ?, ?, ?, ?, ?, ?)";
				
				Connection con = null;
				PreparedStatement prepStat = null;
				
				DBConnection.getDBConnection();
				con = DBConnection.connection;	
				
				prepStat = con.prepareStatement(insertSql);
				prepStat.setString(1, userName);
				prepStat.setString(2, firstName);
				prepStat.setString(3, lastName);
				prepStat.setString(4, email);
				prepStat.setString(5, phone);
				prepStat.setString(6, address);
				prepStat.setString(7, password);
				prepStat.setDouble(8, startBal);
				prepStat.execute();
				con.close();
				prepStat.close();
				out.println(userName + " added <br>");					
				out.println("<a href=/ExpenseTracker-Lovett/NewUser.html>Add User</a> <br>");
				out.println("<a href=/ExpenseTracker-Lovett/AddTransactions.html>Add Transaction</a> <br>");
				out.println("<a href=/ExpenseTracker-Lovett/AccountSummary.html>Account Summary</a> <br>");
				
			}
			
		} catch (SQLException se) {
	         se.printStackTrace();
	      } catch (Exception e) {
	         e.printStackTrace();
	      } finally {
	         try {
	            if (preparedStatement != null)
	               preparedStatement.close();
	         } catch (SQLException se2) {
	         }
	         try {
	            if (connection != null)
	               connection.close();
	         } catch (SQLException se) {
	            se.printStackTrace();
	         }
	      }
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
