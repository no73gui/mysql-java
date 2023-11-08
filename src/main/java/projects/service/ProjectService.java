// this class file is mainly used as a service pass-through between ProjectApp.java and the DAO 
// file in the data layer.
package projects.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import projects.dao.DbConnection;
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
		return projectDao.fetchProjectBy_Id(projectId).orElseThrow(
			() -> new NoSuchElementException("Project with project ID : " + projectId + " does not exist."));

	}
}