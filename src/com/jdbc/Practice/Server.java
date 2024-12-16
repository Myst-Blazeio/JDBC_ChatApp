package com.jdbc.Practice;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class Server {
	private static Connection connection;

	private static void insertManager(Scanner scanner) {
		try { 
			System.out.print("Enter Manager Name: ");
			String managerName = scanner.nextLine();

			String sql = "INSERT INTO Managers (ManagerName) VALUES (?)";
			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setString(1, managerName);
			int rows = stmt.executeUpdate();
			if (rows > 0) {
				System.out.println("Manager inserted successfully.");
			}
		} catch (Exception e) {
			System.out.println("Error in Inserting Manager....");
			e.printStackTrace();
		}
	}

	private static void deleteManager(Scanner scanner) {
		try {
			System.out.print("Enter Manager ID to delete: ");
			int managerId = scanner.nextInt();
			System.out.print("Enter new Manager ID for reassignment: ");
			int newManagerId = scanner.nextInt();

			// Reassign employees to a new manager
			String reassignSql = "UPDATE Employees SET ManagerID = ? WHERE ManagerID = ?";
			PreparedStatement reassignStmt = connection.prepareStatement(reassignSql);
			reassignStmt.setInt(1, newManagerId);
			reassignStmt.setInt(2, managerId);
			reassignStmt.executeUpdate();

			// Delete the manager
			String deleteSql = "DELETE FROM Managers WHERE ManagerID = ?";
			PreparedStatement deleteStmt = connection.prepareStatement(deleteSql);
			deleteStmt.setInt(1, managerId);
			int rows = deleteStmt.executeUpdate();

			if (rows > 0) {
				System.out.println("Manager deleted and employees reassigned successfully.");
			}
		} catch (Exception e) {
			System.out.println("Error in Deleting Manager....");
			e.printStackTrace();
		}
	}

	private static void insertEmployee(Scanner scanner) {
		try {
			System.out.print("Enter Employee Name: ");
			String employeeName = scanner.nextLine();
			System.out.print("Enter Manager ID: ");
			int managerId = scanner.nextInt();

			String sql = "INSERT INTO Employees (EmployeeName, ManagerID) VALUES (?, ?)";
			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setString(1, employeeName);
			stmt.setInt(2, managerId);
			int rows = stmt.executeUpdate();
			if (rows > 0) {
				System.out.println("Employee inserted successfully.");
			}
		} catch (Exception e) {
			System.out.println("Error in Inserting Employee....");
			e.printStackTrace();
		}
	}

	private static void deleteEmployee(Scanner scanner) {
		try {
			System.out.print("Enter Employee ID to delete: ");
			int employeeId = scanner.nextInt();

			String sql = "DELETE FROM Employees WHERE EmployeeID = ?";
			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setInt(1, employeeId);
			int rows = stmt.executeUpdate();
			if (rows > 0) {
				System.out.println("Employee deleted successfully.");
			}
		} catch (Exception e) {
			System.out.println("Error in Deleting Employee....");
			e.printStackTrace();
		}
	}

	private static void displayManagersAndEmployees() {
		try {
			System.out.println("\nManagers:");
			String managerSql = "SELECT * FROM Managers";
			Statement managerStmt = connection.createStatement();
			ResultSet managerRs = managerStmt.executeQuery(managerSql);
			while (managerRs.next()) {
				System.out.printf("Manager ID: %d, Manager Name: %s\n", managerRs.getInt("ManagerID"),
						managerRs.getString("ManagerName"));
			}

			System.out.println("\nEmployees:");
			String employeeSql = "SELECT e.EmployeeID, e.EmployeeName, m.ManagerName " + "FROM Employees e "
					+ "LEFT JOIN Managers m ON e.ManagerID = m.ManagerID";
			Statement employeeStmt = connection.createStatement();
			ResultSet employeeRs = employeeStmt.executeQuery(employeeSql);
			while (employeeRs.next()) {
				System.out.printf("Employee ID: %d, Employee Name: %s, Manager Assigned: %s\n",
						employeeRs.getInt("EmployeeID"), employeeRs.getString("EmployeeName"),
						employeeRs.getString("ManagerName"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		try {
			connection = ConnectionProvider.getConnection();

			Scanner scanner = new Scanner(System.in);
			int choice;

			do {
				System.out.println("\nMenu:");
				System.out.println("1. Insert New Manager");
				System.out.println("2. Delete Manager");
				System.out.println("3. Insert New Employee");
				System.out.println("4. Delete Employee");
				System.out.println("5. Display Managers and Employees");
				System.out.println("6. Exit");
				System.out.print("Enter your choice: ");
				choice = scanner.nextInt();
				scanner.nextLine(); // Consume newline

				switch (choice) {
				case 1:
					insertManager(scanner);
					break;
				case 2:
					deleteManager(scanner);
					break;
				case 3:
					insertEmployee(scanner);
					break;
				case 4:
					deleteEmployee(scanner);
					break;
				case 5:
					displayManagersAndEmployees();
					break;
				case 6:
					System.out.println("Exiting program.");
					break;
				default:
					System.out.println("Invalid choice. Please try again.");
				}
			} while (choice != 6);

		} catch (Exception e) {
			System.out.println("Error.....");
			e.printStackTrace();
		} finally {
			// Close the connection
			try {
				if (connection != null) {
					connection.close();
					System.out.println("Connection closed.");
				}
			} catch (Exception e) {
				System.out.println("Connection is not closed....");
				e.printStackTrace();
			}
		}
	}
}
