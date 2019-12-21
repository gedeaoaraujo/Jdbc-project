package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
			 * Return the seller object if the pointer is not pointing
			 * to a row out of range
			 */
			if(result.next()) {
				Department department = instantiateDepartment(result);
				Seller seller = instantiateSeller(department, result);
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

	/**
	 * This method instantiates a seller object
	 * @param department is the department associated with the seller
	 * @param result is the return of a ResultSet from the connection 
	 * @return returns a seller object
	 * @throws SQLException propagates a exception
	 */
	private Seller instantiateSeller(Department department, ResultSet result) throws SQLException {
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
	 * This method instantiates a department object 
	 * @param result is a ResultSet type used to get the parameters
	 * @return returns a department object
	 * @throws SQLException propagates a exception
	 */
	private Department instantiateDepartment(ResultSet result) throws SQLException {
		Department department = new Department();
		department.setId(result.getInt("DepartmentId"));
		department.setName(result.getString("DepartmentName"));
		return department;
	}

	@Override
	public List<Seller> findAll() {
		
		PreparedStatement statement = null;
		ResultSet result = null;
		
		try {
			/**
			 * This query joins the tables seller and department,
			 * then shows a list of sellers ordered by Name attribute
			 */
			statement =  connection.prepareStatement(
					"SELECT seller.*,department.Name as DepartmentName "
					+ "FROM seller INNER JOIN department "
					+ "ON seller.DepartmentId = department.Id "
					+ "ORDER BY Name");
			
			result = statement.executeQuery();
			
			List<Seller> list = new ArrayList<>();
			Map<Integer, Department> map = new HashMap<>();
			
			/**
			 * Return a list of seller objects while the pointer 
			 * is not pointing to a row out of range
			 */
			while(result.next()) {
				
				Department dep = map.get(result.getInt("DepartmentId"));
				if(dep == null) {
					dep = instantiateDepartment(result);
					map.put(result.getInt("DepartmentId"), dep);
				}
				
				Seller sellerObj = instantiateSeller(dep, result);
				list.add(sellerObj);
			}
			
			/**
			 * Returns a empty list if the pointer is pointing
			 * to a row out of range
			 */
			return list;
			
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(statement);
			DB.closeResultSet(result);
		}
	}

	@Override
	public List<Seller> findByDepartment(Department department) {
		
		PreparedStatement statement = null;
		ResultSet result = null;
		
		try {
			/**
			 * This query joins the tables seller and department,
			 * then shows a list of sellers with the specific Id
			 * ordered by Name attribute
			 */
			statement =  connection.prepareStatement(
					"SELECT seller.*,department.Name as DepartmentName "
					+ "FROM seller INNER JOIN department "
					+ "ON seller.DepartmentId = department.Id "
					+ "WHERE DepartmentId = ? "
					+ "ORDER BY Name");
			
			statement.setInt(1, department.getId());
			result = statement.executeQuery();
			
			List<Seller> list = new ArrayList<>();
			Map<Integer, Department> map = new HashMap<>();
			
			/**
			 * Return a list of seller objects while the pointer 
			 * is not pointing to a row out of range
			 */
			while(result.next()) {
				
				Department dep = map.get(result.getInt("DepartmentId"));
				if(dep == null) {
					dep = instantiateDepartment(result);
					map.put(result.getInt("DepartmentId"), dep);
				}
				
				Seller sellerObj = instantiateSeller(dep, result);
				list.add(sellerObj);
			}
			
			/**
			 * Returns a empty list if the pointer is pointing
			 * to a row out of range
			 */
			return list;
			
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(statement);
			DB.closeResultSet(result);
		}
	}
}
