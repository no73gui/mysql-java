// this class file is mainly used as a service pass-through between ProjectApp.java and the DAO 
// file in the data layer.
package projects.service;

import java.util.List;
import java.util.NoSuchElementException;
import projects.dao.ProjectDao;
import projects.entity.Project;
import projects.exception.DbException;

public class ProjectService {
	public List<Project> fetchAllProjects(){
		return projectDao.fetchAllProjects();
	}
	
	private ProjectDao projectDao = new ProjectDao();
	
	public Project addProject(Project project) {
		// call insert project on projectDao. pass the project parameter into this
		// method. project is what we take as
		// argument in addProject.
		return projectDao.insertProject(project);

	}
	public Project getProjectBy_Id(Integer projectId){
		return projectDao.getProjectBy_Id(projectId).orElseThrow(
			() -> new NoSuchElementException("Project with project ID : " + projectId + " does not exist."));

	}
	public void modifyProjectDetails(Project project) {
		// dao will return a boolean true if the UPDATE operation is successful
		if (project == null) {
	        throw new IllegalArgumentException("Project cannot be null.");
	    }

	    try {
	        // Attempt to modify project details
	        projectDao.modifyProjectDetails(project);
	    } catch (DbException e) {
	        // Handle the exception or rethrow with a more specific message if needed
	        throw new DbException("Failed to modify project details: " + e.getMessage());
	    }
	}
	public boolean deleteProject(Integer project_id) {
		boolean deleted = projectDao.deleteProject(project_id);
		if (!deleted) {
			throw new DbException("Project with project_id : " + project_id + " does not exist.");
		}
		return deleted;
	}
}