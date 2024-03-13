

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
 * Servlet implementation class AddTransaction
 */
@WebServlet("/AddTransaction")
public class AddTransaction extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddTransaction() {
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
		String cmnt = request.getParameter("comment");
		String tranDate = request.getParameter("date");
		Double transAmt = Double.parseDouble(request.getParameter("transAmt"));
				
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
				
				//if the user exists and the password is correct log the transaction 
				String insertSql = "INSERT INTO transactions (id,username, tranDate, tranAmt, cmnt) "
						+ "values (default, ?, ?, ?, ?)";
				
				Connection con = null;
				PreparedStatement prepStat = null;
				
				DBConnection.getDBConnection();
				con = DBConnection.connection;	
				
				prepStat = con.prepareStatement(insertSql);
				prepStat.setString(1, userName);
				prepStat.setString(2, tranDate);
				prepStat.setDouble(3, transAmt);
				prepStat.setString(4, cmnt);
				
				prepStat.execute();
				con.close();
				
				out.println("Transaction of "+ transAmt+" Logged for " + userName + "<br>" );
				
				out.println("<a href=/ExpenseTracker-Lovett/NewUser.html>Add User</a> <br>");
				out.println("<a href=/ExpenseTracker-Lovett/AddTransactions.html>Add Transaction</a> <br>");
				out.println("<a href=/ExpenseTracker-Lovett/AccountSummary.html>Account Summary</a> <br>");
				
			} else {
				//if the user doesn't exist or the password is wrong give an error
				out.println("Username (" + userName + ") or password incorrect <br>");
				connection.close();
				
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
