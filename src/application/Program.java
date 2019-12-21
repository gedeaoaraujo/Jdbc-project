package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;

import db.DB;
import db.DbException;

public class Program {

	public static void main(String[] args) {
		
//		SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy");
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		
		try {
			connection = DB.createConnection();
			/*
			preparedStatement = connection.prepareStatement(
					"INSERT INTO seller "
					+ "(Name, Email, BirthDate, BaseSalary, DepartmentId) "
					+"VALUES "
					+ "(?, ?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);
			
			preparedStatement.setString(1, "Jack Sparrow");
			preparedStatement.setString(2, "sparrow@gmail.com");
			preparedStatement.setDate(3, new Date(date.parse("28/05/1993").getTime()));
			preparedStatement.setInt(4, 1500);
			preparedStatement.setInt(5, 2);
			*/
			
			preparedStatement = connection.prepareStatement(
					"INSERT INTO department (Name) VALUES ('D1'),('D2')",
					Statement.RETURN_GENERATED_KEYS);
			
			int rows = preparedStatement.executeUpdate();
			
			if(rows > 0) {
				ResultSet resultSet = preparedStatement.getGeneratedKeys();
				while(resultSet.next()) {
					int id = resultSet.getInt(1);
					System.out.println("Done! id = " + id);
				}
			}
			else {
				System.out.println("No rows affected");
			}
			
		} 
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		/*
		catch(ParseException e) {
			throw new DbException(e.getMessage());
		}
		*/
		finally {
			DB.closeStatement(preparedStatement);
			DB.closeConnection();
		}
	}
}
