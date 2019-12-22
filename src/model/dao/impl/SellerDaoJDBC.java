package model.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

	/**
	 * This method does a insertion into table seller
	 */
	@Override
	public void insert(Seller seller) {

		PreparedStatement statement = null;
		
		try {
			/**
			 * This query inserts into the table seller the specified attributes
			 */
			statement = connection.prepareStatement(
					"INSERT INTO seller "
					+ "(Name, Email, BirthDate, BaseSalary, DepartmentId) "
					+ "VALUES "
					+ "(?, ?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);
			
			statement.setString(1, seller.getName());
			statement.setString(2, seller.getEmail());
			statement.setDate(3, new Date(seller.getBirthDate().getTime()));
			statement.setDouble(4, seller.getBaseSalary());
			statement.setInt(5, seller.getDepartment().getId());
			
			int rowsAffected = statement.executeUpdate();
			
			/**
			 * If the rows affected is more then zero,
			 * the generated keys is get and then
			 * the id of object seller is set with this value
			 */
			if(rowsAffected > 0) {
				ResultSet result = statement.getGeneratedKeys();
				
				if(result.next()) {
					int id = result.getInt(1);
					seller.setId(id);
				}
				
				DB.closeResultSet(result);
			}
			else {
				throw new DbException("Error! no rows affected");
			}
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(statement);
		}
	}

	/**
	 * This method updates a seller data
	 * @param seller receives a seller object
	 */
	@Override
	public void update(Seller seller) {

		PreparedStatement statement = null;
		
		try {
			/**
			 * This query inserts into the table seller the specified attributes
			 */
			statement = connection.prepareStatement(
					"UPDATE seller "
					+ "SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? "
					+ "WHERE Id = ?");
			
			statement.setString(1, seller.getName());
			statement.setString(2, seller.getEmail());
			statement.setDate(3, new Date(seller.getBirthDate().getTime()));
			statement.setDouble(4, seller.getBaseSalary());
			statement.setInt(5, seller.getDepartment().getId());
			statement.setInt(6, seller.getId());
			
			statement.executeUpdate();
			
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(statement);
		}
		
	}

	@Override
	public void deleteById(Integer id) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * This method search for a seller in the table seller
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

	/**
	 * This method return a list of all sellers
	 * in the table seller
	 */
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

	/**
	 * This method returns a list of sellers
	 * with the department specified, doing a join
	 * in the table seller and the table department
	 */
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
