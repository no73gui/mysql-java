import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import projects.dao.DbConnection;


public class main {

	public static void main(String[] args) {
		
		Connection db1 = null;
		
		try {
			// try with possible failure; db1 Connection is set to DbConnection.getConnection
			// this will attempt the getConnection method within the DbConnection class.
			db1 = DbConnection.getConnection();
			// output a status update
			System.out.println("Connection to Database 1 is ACTIVE");
			
			// create an empty statement object to hold the SQL statement we plan to send
			// to the db.
			Statement stmnt = db1.createStatement();
			// form the statement
			String sqlQuery = "SELECT * FROM project";
			// execute the sqlQuery string as resultdb1. This will provide the result
			ResultSet resultdb1 = stmnt.executeQuery(sqlQuery);
			
			while (resultdb1.next()) {
				int id = resultdb1.getInt("id");
				String name = resultdb1.getString("name");
				System.out.println("ID: " + id + "Name : " + name);
				
			}
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			try {
				if (db1 != null) {
					db1.close();
					System.out.println("Connection to Database 1 is TERMINATED");
				}
			}
			catch (SQLException e){
				e.printStackTrace();
			}
		}
		
		
	}

}
