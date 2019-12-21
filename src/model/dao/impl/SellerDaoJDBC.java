package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao{
	
	private Connection connection;
	
	public SellerDaoJDBC(Connection connection) {
		this.connection = connection;
	}

	@Override
	public void insert(Seller seller) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Seller seller) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteById(Integer id) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * This method search for a seller in the database seller
	 * @param id is the Id of a seller
	 */
	@Override
	public Seller findById(Integer id) {
		
		PreparedStatement statement = null;
		ResultSet result = null;
		
		try {
			/**
			 * This query joins the tables seller and department,
			 * then shows only the seller with the specific Id
			 */
			statement =  connection.prepareStatement(
					"SELECT seller.*,department.Name as DepartmentName "
					+ "FROM seller INNER JOIN department "
					+ "ON seller.DepartmentId = department.Id "
					+ "WHERE seller.Id = ?");
			
			statement.setInt(1, id);
			result = statement.executeQuery();
			
			/**
			 * Return true if the pointer is not in the first line
			 */
			if(result.next()) {
				
				Department department = new Department();
				department.setId(result.getInt("DepartmentId"));
				department.setName(result.getString("DepartmentName"));
				
				Seller seller = new Seller();
				seller.setId(result.getInt("Id"));
				seller.setEmail(result.getString("Email"));
				seller.setName(result.getString("Name"));
				seller.setBirthDate(result.getDate("BirthDate"));
				seller.setBaseSalary(result.getDouble("BaseSalary"));
				seller.setDepartment(department);
				
				return seller;
			}
			
			/**
			 * Returns null if the pointer is pointing to a row out of range
			 */
			return null;
			
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(statement);
			DB.closeResultSet(result);
		}
	}

	@Override
	public List<Seller> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

}
