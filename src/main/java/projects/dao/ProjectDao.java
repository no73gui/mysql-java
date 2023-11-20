package projects.dao;
// It's a good idea to add constants for values that are used over and over again in a class. 

// The table names are used by all the methods that write to or read from the tables.

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.mysql.cj.protocol.Resultset;

import projects.entity.Category;
import projects.entity.Material;
import projects.entity.Project;
import projects.entity.Step;
import projects.exception.DbException;
import provided.util.DaoBase;

@SuppressWarnings("unused")
public class ProjectDao extends DaoBase {

	// create local constants.
	private static final String PROJECT_TABLE = "projects";
	private static final String PROJECT_CATEGORY_TABLE = "project_category";
	private static final String CATEGORY_TABLE = "category";
	private static final String MATERIAL_TABLE = "material";
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
			try (PreparedStatement stmnt = connect.prepareStatement(sql)){ // parameterIndex:? ... each paraIndex indicates a int
				// value. The int represents what position it is in in the String literal created outside the try block. 
				setParameter(stmnt, 1,project.getProjectName(), String.class);
				setParameter(stmnt, 2,project.getEstimatedHours(), BigDecimal.class);
				setParameter(stmnt, 3,project.getActualHours(), BigDecimal.class);
				setParameter(stmnt, 4,project.getDifficulty(), Integer.class);
				setParameter(stmnt, 5,project.getNotes(), String.class);
				// executeUpdate() executes the query. Can be used to create drop insert update delete etc.

				// stmnt.executeQuery(); will execute select statements for pulling data.
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
	
	
	
	// selects all projects from Projects
	public List<Project> fetchAllProjects() {

		// create SQL prepared statement to insert the values from the Project project
		// passed in.
		// @formatter:off
		String sql = "" + "SELECT project_id , project_name FROM " + PROJECT_TABLE +
				" " + "ORDER BY (project_id);";
		// ? is a place holder
		// formatter:on
		
		// get new connection from DbConnection.getConnection() as dbProjectDao
		try (Connection connect = DbConnection.getConnection()){
			// if successful, start a transaction. startTransaction() is in the DaoBase class.
			startTransaction(connect);
			// PreparedStatement from java.sql
			try (PreparedStatement stmnt = connect.prepareStatement(sql)){
				// run the stmnt , store value in result set.
				try (ResultSet result = stmnt.executeQuery()) {
					// if success, create the array List of Project called lsOfProj that each will be added to. .
					List<Project> lsOfProj = new ArrayList<Project>();
					// while result has another value, instantiate a new Project.
					while (result.next()) {
						// extract the calue from result set and add it to ArrayList lsOfProj.
						lsOfProj.add(extract(result, Project.class));
						
					}
					return lsOfProj;
				}
				
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


	
	public Optional<Project> getProjectBy_Id(Integer projectId) {
		String sql = "SELECT * FROM " + PROJECT_TABLE + " WHERE project_id = ?;";
		// attempt connection to db via try with resources
		try(Connection connect = DbConnection.getConnection()){
			// on success, start sql transaction
			startTransaction(connect);

			try{
				// on transaction, instantiate project of type Project with null value.
				// this will insure that on each transaction, there is no active project
				// selection.
				Project project = null;

				// prepare sql statement
				try(PreparedStatement stmnt = connect.prepareStatement(sql)){
					// getters and setters
					setParameter(stmnt, 1, projectId , Integer.class);
					// obtain resultset from the executed sql query.
					try(ResultSet rslt = stmnt.executeQuery()){
						// process the query. .next() checks if there is at least one row in the resultset.
						if (rslt.next()){
							// extract rslt and assign to project value.
							project = extract(rslt, Project.class);
						}
					}
				}
						if (Objects.nonNull(project)){
							project.getMaterials().addAll(getProject_Materials(connect , projectId));

						}
						// commit transaction
						commitTransaction(connect);
						return Optional.ofNullable(project);
						
			}

			// on fail, rollback connecton
			catch (Exception e){
				rollbackTransaction(connect);
				// throw DbException message from Exception.
				throw new DbException(e);
			}
		}
			// catch sql exception should the sql database fail to connect
			catch (SQLException e){
				throw new DbException(e);
		}
		
	}
	
	
	
	private List<Material> getProject_Materials(Connection connect , Integer projectId) throws SQLException {
		String sql = "SELECT m.material_name FROM " + MATERIAL_TABLE + " m JOIN "
				+ PROJECT_TABLE + " p WHERE p.project_id = ?;";
		// connectoin
		try(PreparedStatement stmnt = connect.prepareStatement(sql)){
			setParameter(stmnt, 1, projectId, Integer.class);

			try(ResultSet rslt = stmnt.executeQuery()){

				List<Material> matLst= new LinkedList<>();
				while (rslt.next()){
					matLst.add(extract(rslt, Material.class));

				}
				return matLst;

			}
		}
	}
	
	
	
	
	private List<Step> getProject_Steps(Connection connect , Integer projectId) throws SQLException {
		String sql = "SELECT s.steps FROM " + STEP_TABLE 
				+ " s JOIN " + PROJECT_TABLE
				+ " p WHERE p.project_id = ?;";
		
		try (PreparedStatement stmnt = connect.prepareStatement(sql)){
			setParameter(stmnt, 1, projectId, Step.class);
			try(ResultSet rslt = stmnt.executeQuery()){
				
				List<Step> stepLst = new LinkedList<>();
				
				while (rslt.next()) {
					stepLst.add(extract(rslt, Step.class));
				}
				return stepLst;
			}
		}
	}
	
	
	
	private List<Category> getProject_Cat(Connection connect , Integer projectId) throws SQLException {
		// join 3 tables to get project category.
		// get category from category table, category column.
		String sql = "SELECT c.category " +
	             "FROM " + CATEGORY_TABLE + " c " +
				// join project_category on rows where category_id's in category and Project category are same.
	             "JOIN " + PROJECT_CATEGORY_TABLE + "pc ON c.category_id = pc.category_id " +
				// also join project table where project_category and project project_id is the same.
	             "JOIN " + PROJECT_TABLE + " p ON pc.project_id = p.project_id " +
	             "WHERE p.project_id = ? ";
		
		try (PreparedStatement stmnt = connect.prepareStatement(sql)){
			setParameter(stmnt, 1, projectId, Category.class);
			try(ResultSet rslt = stmnt.executeQuery()){
				
				List<Category> catLst = new LinkedList<>();
				
				while (rslt.next()) {
					catLst.add(extract(rslt, Category.class));
				}
				return catLst;
			}
		}
	}
	
	// self explanitory name
	public void modifyProjectDetails(Project project) {
		// TODO modify project details using string literals as sql
		
		// @formatter: off
		String sql = "UPDATE " + PROJECT_TABLE + " SET project_name = ?, estimated_hours = ?, actual_hours = ?, difficulty = ?, notes = ?, "
				+ "WHERE project_id = ?;";
		// @formatter: on
		try (Connection connect = DbConnection.getConnection()){
			// if successful, start a transaction. startTransaction() is in the DaoBase class.
			startTransaction(connect);
			// PreparedStatement from java.sql
			try (PreparedStatement stmnt = connect.prepareStatement(sql)){ // parameterIndex:? ... each paraIndex indicates a int
				// value. The int represents what position it is in in the String literal created outside the try block. 
				setParameter(stmnt, 1,project.getProjectName(), String.class);
				setParameter(stmnt, 2,project.getEstimatedHours(), BigDecimal.class);
				setParameter(stmnt, 3,project.getActualHours(), BigDecimal.class);
				setParameter(stmnt, 4,project.getDifficulty(), Integer.class);
				setParameter(stmnt, 5,project.getNotes(), String.class);
				// executeUpdate() executes the query. Can be used to create drop insert update delete etc.

				// stmnt.executeQuery(); will execute select statements for pulling data.
				stmnt.executeUpdate();
				
				commitTransaction(connect);
								
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



	public boolean deleteProject(Integer project_id) {
	    String sql = "DELETE FROM " + PROJECT_TABLE + " WHERE project_id = ?";
	    
	    try (Connection connect = DbConnection.getConnection()) {
	        startTransaction(connect);

	        try (PreparedStatement stmt = connect.prepareStatement(sql)) {
	            // Set the parameter for the project_id
	            stmt.setInt(1, project_id);

	            // Execute the DELETE statement
	            int rowsAffected = stmt.executeUpdate();

	            // Check for changes
	            if (rowsAffected > 0) {
	                // Proceed to transaction commit
	                commitTransaction(connect);
	                // Return true for success
	                return true;
	            } else {
	                // Rollback if no changes occur; return false for success status
	                rollbackTransaction(connect);
	                return false;
	            }
	        } catch (SQLException e) {
	            rollbackTransaction(connect);
	            throw new DbException(e);
	        }
	    } catch (Exception e) {
	        throw new DbException(e);
	    }
	}

}




