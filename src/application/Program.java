package application;

import java.sql.Connection;

import db.DB;

public class Program {

	public static void main(String[] args) {
		
		Connection connection = DB.createConnection();
		DB.closeConnection();
	}

}
