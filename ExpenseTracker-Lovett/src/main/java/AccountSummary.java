import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.table.DefaultTableModel;

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
			
			String selectSQL = "select u.username, convert(u.startBal+ sum(coalesce(tranAmt,0)),char) trans "
					+ "from users as u  "
					+ "left join transactions as t on u.username=t.username "
					+ "where u.username=? and u.password=?"
					+ "group by u.username,u.startBal";
			
			preparedStatement = connection.prepareStatement(selectSQL);
			preparedStatement.setString(1, userName);
			preparedStatement.setString(2, password);
			
			ResultSet rs = preparedStatement.executeQuery() ;
			
			if(rs.next()) {
				//if the user exists and the password is correct give the summary 
				
				out.println("Current Balance: " + rs.getString("trans") + "<br>");
				
				connection.close();
				preparedStatement.close();
				
				String acctSql = "SELECT DATE_FORMAT(tranDate, '%d %b %Y') as Date, convert(tranAmt,char) as Amount, cmnt as Comment "
							+ " FROM transactions "
							+ "where username = ? "
							+ "order by tranDate desc "
							+ "limit 100";
				
				Connection con = null;
				PreparedStatement prepStat = null;
				
				DBConnection.getDBConnection();
				con = DBConnection.connection;	
				
				prepStat = con.prepareStatement(acctSql);
				prepStat.setString(1, userName);
		
				ResultSet rs1 = prepStat.executeQuery();
				
				if(rs1.next()) {
					//out.println("Date&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Amount&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Comment<br>");
					//The following is modified from https://www.c-sharpcorner.com/UploadFile/satyapriyanayak/display-data-from-database-through-servlet-and-jdbc/
					
					out.println("<html><body>");
					out.println("<table border=1 width=50% height=10%>");
					out.println("<tr><th>Date</th><th>Amount</th><th>Comment</th><tr>");
					
					String tDate = rs1.getString("Date").trim();
					String amt = rs1.getString("Amount").trim();
					String cmnt = rs1.getString("Comment").trim();	
					out.println("<tr><td>" + tDate + "</td><td align=\"right\">" + amt + "</td><td>" + cmnt + "</td></tr>");
					
					while(rs1.next()) {
						tDate = rs1.getString("Date").trim();
						amt = rs1.getString("Amount").trim();
						cmnt = rs1.getString("Comment").trim();	
						out.println("<tr><td>" + tDate + "</td><td align=\"right\">" + amt + "</td><td>" + cmnt + "</td></tr>");
					}
					out.println("</table>");  
		            out.println("</html></body>");
		            
					out.println("<a href=/ExpenseTracker-Lovett/NewUser.html>Add User</a> <br>");
					out.println("<a href=/ExpenseTracker-Lovett/AddTransactions.html>Add Transaction</a> <br>");
					out.println("<a href=/ExpenseTracker-Lovett/AccountSummary.html>Account Summary</a> <br>");
				} else {
					
					out.println(userName + " has no transactions.<br>");
					out.println("<a href=/ExpenseTracker-Lovett/NewUser.html>Add User</a> <br>");
					out.println("<a href=/ExpenseTracker-Lovett/AddTransactions.html>Add Transaction</a> <br>");
					out.println("<a href=/ExpenseTracker-Lovett/AccountSummary.html>Account Summary</a> <br>");
					
				}
				
				con.close();
				prepStat.close();
			} else {
				//if the user doesn't exist or the password is wrong give an error
				connection.close();
				preparedStatement.close();
				
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
