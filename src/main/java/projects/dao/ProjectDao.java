package projects.dao;
// It's a good idea to add constants for values that are used over and over again in a class. 

// The table names are used by all the methods that write to or read from the tables.

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import projects.entity.Project;
import projects.exception.DbException;
import provided.util.DaoBase;

@SuppressWarnings("unused")
public class ProjectDao extends DaoBase {

	// create local constants.
	private static final String CATEGORY_TABLE = "category";
	private static final String MATERIAL_NAME = "material";
	private static final String PROJECT_TABLE = "projects";
	private static final String PROJECT_CATEGORY_TABLE = "project_category";
	private static final String STEP_TABLE = "step";

	public Project insertProject(Project project) {
		// create SQL prepared statement to insert the values from the Project project
		// passed in.
		// @formatter:off
		String sql = "" + "INSERT INTO " + PROJECT_TABLE + " " + "(project_name, estimated_hours, "
				+ "actual_hours, difficulty, notes) "
				+ "VALUES " + "(?, ?, ?, ?, ?)"; 
		// ? is a place holder
		// formatter:on
		
		// get new connection from DbConnection.getConnection() as dbProjectDao
		try (Connection connect = DbConnection.getConnection()){
			// if successful, start a transaction. startTransaction() is in the DaoBase class.
			startTransaction(connect);
			// PreparedStatement from java.sql
			try (PreparedStatement stmnt = connect.prepareStatement(sql)){
				setParameter(stmnt, 1,project.getProjectName(), String.class);
				setParameter(stmnt, 2,project.getEstimatedHours(), BigDecimal.class);
				setParameter(stmnt, 3,project.getActualHours(), BigDecimal.class);
				setParameter(stmnt, 4,project.getDifficulty(), Integer.class);
				setParameter(stmnt, 5,project.getNotes(), String.class);
				// update, save project details.
				stmnt.executeUpdate();
				
				Integer projectId = getLastInsertId(connect, PROJECT_TABLE);
				commitTransaction(connect);
				
				project.setProjectId(projectId);
				return project;
				
			}
			catch (Exception e) {
				rollbackTransaction(connect);
				throw new DbException(e);
			}
		}
		catch(SQLException e) {
			throw new DbException(e);
		}
		
	}

}
