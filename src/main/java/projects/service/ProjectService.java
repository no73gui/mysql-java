// this class file is mainly used as a service pass-through between ProjectApp.java and the DAO 
// file in the data layer.
package projects.service;

import java.util.List;

import projects.dao.ProjectDao;
import projects.entity.Project;

public class ProjectService {
	private ProjectDao projectDao = new ProjectDao();

	public Project addProject(Project project) {
		// call insert project on projectDao. pass the project parameter into this
		// method. project is what we take as
		// argument in addProject.
		return projectDao.insertProject(project);

	}

	public List<Project> fetchAllProjects() {

		return null;
	}

}
