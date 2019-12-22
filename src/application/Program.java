package application;

import java.util.Date;
import java.util.List;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {
		
		SellerDao sellerDao = DaoFactory.createSellerDao();
		
		System.out.println("=== Seller findById() ===");
		Seller seller = sellerDao.findById(5);
		System.out.println(seller);
		
		System.out.println("\n=== Seller findByDepartment() ===");
		Department department = new Department(2 , null);
		List<Seller> list = sellerDao.findByDepartment(department);
		
		for (Seller obj : list) {
			System.out.println(obj);	
		}
		
		System.out.println("\n=== Seller findAll() ===");
		list = sellerDao.findAll();
		
		for (Seller obj : list) {
			System.out.println(obj);	
		}
		
		System.out.println("\n=== Seller insert() ===");
		Seller newSeller = new Seller(null, "Greg", "gregory@gmail.com", new Date(), 5000.0, department);
		sellerDao.insert(newSeller);
		System.out.println("New seller id: " + newSeller.getId());
		

		System.out.println("\n=== Seller update() ===");
		seller = sellerDao.findById(8);
		seller.setName("Mutant");
		sellerDao.update(seller);
		System.out.println("Update Complete");
		
		System.out.println("\n=== Seller delete() ===");
		sellerDao.deleteById(1);
		System.out.println("Seller Deleted");
	}
}
