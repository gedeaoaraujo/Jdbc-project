package application;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

public class Program2 {

	public static void main(String[] args) {

		Department department = new Department(null, "Music");
		DepartmentDao departmentDao = DaoFactory.createDepartmentDao();
		departmentDao.insert(department);
		System.out.println("Department added: " + department.getName());
	}
}
