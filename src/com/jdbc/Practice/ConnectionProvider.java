package com.jdbc.Practice;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionProvider {
	
	public static Connection con;
	
	public static Connection getConnection() {
		
		try {
			final String URL = "jdbc:mysql://localhost:3306/your_database_name";
		    final String USER = "your_username";
		    final String PASSWORD = "your_password";
		    
			if(con==null) {
				
				Class.forName("com.mysql.cj.jdbc.Driver");
				con = DriverManager.getConnection(URL,USER,PASSWORD);
				System.out.println("Connected to Database Successfully....");
				
			}else {
				
				System.out.println("Failed to Connect to Database");
			}
			
		}
		catch(Exception e){
			System.out.println("Error......");
			e.printStackTrace();
		}
		
		return con;
		
	}

}
