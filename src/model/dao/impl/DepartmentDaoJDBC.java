package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentDaoJDBC implements DepartmentDao{

	private Connection connection;
	
	public DepartmentDaoJDBC(Connection connection) {
		this.connection = connection;
	}

	/**
	 * This method inserts a specified department
	 * and sets the name of his with the name of the object
	 * @param department is the object
	 */
	@Override
	public void insert(Department department) {
		PreparedStatement statement = null;
		
		try {
			/**
			 * This query inserts a new department
			 */
			statement = connection.prepareStatement(
					"INSERT INTO department "
					+ "(Name) VALUES (?)",
					Statement.RETURN_GENERATED_KEYS);
			
			statement.setString(1, department.getName());
			int rowsAffected = statement.executeUpdate();
			
			if(rowsAffected > 0) {
				ResultSet result = statement.getGeneratedKeys();
				
				if(result.next()) {
					int id = result.getInt(1);
					department.setId(id);
				}
				
				DB.closeResultSet(result);
			}
		}
		catch(SQLException e){
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(statement);
		}
	}

	@Override
	public void update(Department department) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteById(Integer id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Department findById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Department> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

}
