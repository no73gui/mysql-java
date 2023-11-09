import java.math.BigDecimal;
import java.util.Objects;
import java.util.Scanner;
import java.util.List;
import projects.entity.Project;
import projects.exception.DbException;
import projects.service.ProjectService;

public class ProjectsApp {
	//@formatter:off
	// by using @formatter:off it ensures Eclipse formatter won't try to reformat
	// the list.
	
	// init new private instance of scanner that takes system input.
	private Scanner userInput = new Scanner(System.in);
	// this list will allow the list to be displayed to show the user what options
	// are available.
	private List<String> operationsList = List.of(
					"1) Add a project",
					"2) List projects",
					"3) Select a project"
					);
			//@formatter:on

	// create pvt inst of ProjectService with 0 construct named projectService.
	private ProjectService projectService = new ProjectService();

	// inst Project object
	private Project currentProject;
	
	public static void main(String[] args) {
		new ProjectsApp();
		ProjectsApp projectsApp = new ProjectsApp();
		// call the processUserSelection method from this class. ProjectsApp class.
		projectsApp.processUserSelections();

	}

	// create the processUserSelection method with no return type to be called in
	// main().
	private void processUserSelections() {
		// add local variable 'done' that will determine a boolean data type to reflect
		// status.
		boolean done = false;
		// while status 'done' is false (default based on local variable), try... ;
		// catch... an exception if occurs;

		while (!done) {
			try {
				int selection = getUserSelection();
				switch (selection) {
				case -1:
					done = true;
					System.out.println("Input Recieved ; Exiting Menu...");
					break;
				case 0:
					done = true;
					break;
				case 1:
					createProject();
					break;
				case 2:
					listProjects();
					break;
				case 3:
					selectProject();
					break;
				default:
					System.out.println("\n" + selection + " is not a valid selection. Try again...");
					break;
				}
			} catch (Exception e) {
				System.out.println("\nError: " + e + " Try again...");
				e.printStackTrace();
			}
		}
	}

	private void listProjects() {
		List<Project> projects = projectService.fetchAllProjects();
		System.out.println("\nProjects: ");
		// for each Projet item, print ID and name separated with a : and indent each 3 spaces
		projects.forEach(
				project -> System.out.println("   " + project.getProjectId() + ": " + project.getProjectName()));

	}

	// create a method to print and receive int input from a menu.
	private Integer getUserSelection() {
		// print menu
		printOperations();
		// take in user input. distinct name for method call. This method will return
		// int value.
		Integer input = getIntInput("Enter a menu selection");
		// return that checks value of local variable input is null.
		// Objects.isNull checks the Integer object is null. the Integer value 'input'
		// is passed into the isNull method.
		// ? is input null? return -1, if its not, return the input value.
		return Objects.isNull(input) ? -1 : input;
	}

	private void printOperations() {
		System.out.println("\nThese are the available selections. Press the Enter key to quit: ");
		// run forEach method on operationsList to iterate through the List
		// operationsList.
		// forEach thing -> print " " then the thing. thing can be anything. when
		// forEach is called,
		// each iteration assigns its value to 'thing'.
		operationsList.forEach(thing -> System.out.println(" " + thing));

		if (Objects.isNull(currentProject)){
			System.out.println("\nYou are not working with a project.");

		}else {
			System.out.println("You are working with project : " + currentProject);
		}
	}

	// get user input of type Integer.
	private Integer getIntInput(String prompt) {
		// convert String to Integer.
		// assign String input to value of getStringInput(prompt)
		String input = getStringInput(prompt);
		if (Objects.isNull(input)) {
			return null;
		}
		// attempt to convert String to Integer.
		try {
			// convert the String to Integer.
			// Integer.valueOf takes the String input, converts to Integer if possible.
			// https://docs.oracle.com/javase/7/docs/api/java/lang/Integer.html#valueOf(java.lang.String)
			return Integer.valueOf(input);
		} catch (NumberFormatException e) {
			// throw new DbException with message.
			throw new DbException(input + "is not a valid number. Try again...");
		}
	}

	private String getStringInput(String prompt) {
		// keep the cursor in-line; NOTE print not println. println terminates line, do
		// not use.
		System.out.print(prompt + ": ");
		// take user input.
		String input = userInput.nextLine();
		// input String is blank? return null, otherwise run trim on input to trim
		// whitespace, outer whitespace not removed.
		// https://www.tutorialspoint.com/java/lang/string_trim.htm
		return input.isBlank() ? null : input.trim();
	}

	// gather project details ; put details into Project object.
	private void createProject() {
		String projectName = getStringInput("Enter project name: ");
		BigDecimal estimatedHours = getDecimalInput("Enter estimated completion time (hours): ");
		BigDecimal actualHours = getDecimalInput("Enter actual completion time (hours): ");
		// no current difficulty validation.
		Integer difficulty = getIntInput("Enter project difficulty (1-5): ");
		String notes = getStringInput("Enter the project notes: ");
		// create new instance of Project named project with no construct.
		Project project = new Project();

		project.setProjectName(projectName);
		project.setEstimatedHours(estimatedHours);
		project.setActualHours(actualHours);
		project.setDifficulty(difficulty);
		project.setNotes(notes);

		Project dbProject = projectService.addProject(project);
		System.out.println("Task Complete : " + dbProject + "created");
	}
	
	private void selectProject(){ 
		// set to null so there is no selection when called
		currentProject = null;
		// list all projects
		listProjects();
		Integer selection = getIntInput("Enter a project ID to select a project : ");
		// assign output of selection passed into method to currentProject.
		currentProject = projectService.getProjectBy_Id(selection);
		if (Objects.isNull(currentProject)){
			System.out.println(" You are not currently working with a project.");
		}
		else {
			System.out.println("\nYou are working with project: " + currentProject);
		}


	}

	// create getDecimalInput()
	private BigDecimal getDecimalInput(String prompt) {
		// convert String to Integer.
		// assign String input to value of getStringInput(prompt)
		String input = getStringInput(prompt);
		if (Objects.isNull(input)) {
			return null;
		}
		// attempt to convert String to Integer.
		try {
			// convert the String to Integer.
			// Integer.valueOf takes the String input, converts to Integer if possible.
			// https://docs.oracle.com/javase/7/docs/api/java/lang/Integer.html#valueOf(java.lang.String)
			return new BigDecimal(input).setScale(2);
		} catch (NumberFormatException e) {
			// throw new DbException with message.
			throw new DbException(input + "is not a valid decimal. Try again...");
		}
	}
}
