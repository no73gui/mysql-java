package projects.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import projects.exception.DbException;

public class DbConnection {
	private static String HOST = "localhost";
	private static String PASSWORD = "projects";
	private static int PORT = 3306;
	private static String SCHEMA = "projects";
	private static String USER = "projects";

	public static Connection getConnection() {
		// this uri string is created using the String format method. the %s stands for
		// string, while %d represents a integer value
		// the .format method allows us to use the % in place of defined variables so
		// long as they are defined within the methods
		// declaration. The HOST, PORT, SCHEMA, etc. correspond to the values we have
		// set in the String format.
		String uri = String.format("jdbc:mysql://%s:%d/%s?user=%s&password=%s", HOST, PORT, SCHEMA, USER, PASSWORD);
		// attempt this, with expectation of possible errors.
		try {
			// getConnection lets us pass in the uri previously created, getConnection will
			// attempt to form a connection and print
			// success message if successful and has a return value connection of type
			// Connection.
			Connection connection = DriverManager.getConnection(uri);
			System.out.println("Connection to " + SCHEMA + " successful");
			return connection;
		} catch (SQLException e) {
			// catch errors from SQL.
			System.err.println("Connection failed: " + e.getMessage());
			// creates a new DbE with a defaulted message "Failed to establish......", and a
			// cause of whatever the SQLException
			// cause is, stored as e.
			throw new DbException("Failed to establish a database connection at " + uri);
		}
	}
}