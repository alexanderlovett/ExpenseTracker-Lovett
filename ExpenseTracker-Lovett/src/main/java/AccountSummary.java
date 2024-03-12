

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
 * Servlet implementation class AccountSummary
 */
@WebServlet("/AccountSummary")
public class AccountSummary extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AccountSummary() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String userName = request.getParameter("userName");
		String password = request.getParameter("password");
				
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			DBConnection.getDBConnection();
			connection = DBConnection.connection;
			
			String selectSQL = "Select username from users where username = ? and password = ?";
			preparedStatement = connection.prepareStatement(selectSQL);
			preparedStatement.setString(1, userName);
			preparedStatement.setString(2, password);
			
			ResultSet rs = preparedStatement.executeQuery() ;
			
			if(rs.next()) {
				connection.close();
				preparedStatement.close();
				
				//if the user exists and the password is correct log the transaction 
				String acctSql = "SELECT * FROM transactions where username = ? order by tranDate";
				
				Connection con = null;
				PreparedStatement prepStat = null;
				
				DBConnection.getDBConnection();
				con = DBConnection.connection;	
				
				prepStat = con.prepareStatement(acctSql);
				prepStat.setString(1, userName);
		
				ResultSet rs1 = prepStat.executeQuery();
				
				if(rs1.next()) {
					while(rs1.next()) {
						
						
					}
					
					out.println("<a href=/ExpenseTracker-Lovett/NewUser.html>Add User</a> <br>");
					out.println("<a href=/ExpenseTracker-Lovett/AddTransactions.html>Add Transaction</a> <br>");
					out.println("<a href=/ExpenseTracker-Lovett/AccountSummary.html>Account Summary</a> <br>");
				} else {
					
					out.println(userName + "has no transactions.");
					out.println("<a href=/ExpenseTracker-Lovett/NewUser.html>Add User</a> <br>");
					out.println("<a href=/ExpenseTracker-Lovett/AddTransactions.html>Add Transaction</a> <br>");
					out.println("<a href=/ExpenseTracker-Lovett/AccountSummary.html>Account Summary</a> <br>");
					
				}
				
				
				
				con.close();
				
				
			} else {
				//if the user doesn't exist or the password is wrong give an error
				connection.close();
				out.println("Username (" + userName + ") or password incorrect <br>");
				
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
