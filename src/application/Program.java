package application;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import db.DB;
import db.DbException;

public class Program {

	public static void main(String[] args) {
		
		Connection connection = null;
		Statement statement = null;
		
		try {
			/**
			 * Creates a connection with database
			 */
			connection = DB.createConnection();
			
			/**
			 * Blocks the commitment of a transaction if an error occurs
			 */
			connection.setAutoCommit(false);
			
			/**
			 * Creates a statement to execute queries
			 */
			statement = connection.createStatement();
			
			int rows_1 = statement.executeUpdate("UPDATE seller SET BaseSalary = 2000 WHERE DepartmentId = 1");
			
			/**
			 * A fake error for testing
			 *
			 * int x = 1;
			 * if(x < 2)
			 *	throw new SQLException("Fake Error");
			*/
			
			int rows_2 = statement.executeUpdate("UPDATE seller SET BaseSalary = 4000 WHERE DepartmentId = 2");
			
			/**
			 * Commits the transaction if no errors occurs 
			 */
			connection.commit();
			
			/**
			 * Prints the rows affected in database
			 */
			System.out.println("Rows updated: " + rows_1);
			System.out.println("Rows updated: " + rows_2);
			
		} catch (SQLException e) {
			try {
				connection.rollback();
				throw new DbException("Transaction rolled back caused by: " + e.getMessage());
				
			} catch (SQLException e1) {
				throw new DbException("Rollback error because: " + e1.getMessage());
			}
		}
		finally {
			
		}
		
	}
}
