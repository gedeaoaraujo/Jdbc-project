package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import db.DB;
import db.DbIntegrityException;

public class Program {

	public static void main(String[] args) {
		
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		
		try {
			connection = DB.createConnection();
			preparedStatement = connection.prepareStatement(
					"DELETE FROM department "
					+ "WHERE "
					+ "Id = ?"
					);
			
			preparedStatement.setInt(1, 2);
			
			int rows = preparedStatement.executeUpdate();
			
			System.out.println("Rows affected: " + rows);
			
		} catch (SQLException e) {
			throw new DbIntegrityException(e.getMessage());
		}
		finally {
			DB.closeConnection();
		}
		
	}
}
