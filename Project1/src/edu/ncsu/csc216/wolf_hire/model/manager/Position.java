package edu.ncsu.csc216.wolf_hire.model.manager;

import java.util.ArrayList;
import java.util.List;

import edu.ncsu.csc216.wolf_hire.model.application.Application;
import edu.ncsu.csc216.wolf_hire.model.command.Command;

/**
 * Concrete class that stores and manages the information for a Position object and the list
 * of Application object(s) in the Position.
 * 
 * A Position object knows its name, hours per work, and pay rate, as well as its list of applications.
 * 
 * @author hmreese2
 *
 */
public class Position {

	/** List of applications that correspond to a Position */
	private ArrayList<Application> applications;
	/** Name of Position */
	private String positionName;
	/** Hours per week that a position will require */
	private int hoursPerWeek;
	/** Pay rate that a position has */
	private int payRate;
	
	/**
	 * Constructs a Position object using position name, hours per week worked, and pay rate
	 * @param positionName name of position
	 * @param hoursPerWeek hours a position works per week
	 * @param payRate rate of pay for position
	 * @throws IllegalArgumentException with message "Position cannot be created." 
	 * if positionName is null or empty,
	 * if hoursPerWeek is less than 5 or more than 20, or
	 * if payRate is less than 7 or more than 35
	 */
	public Position(String positionName, int hoursPerWeek, int payRate) {
		// set positionName, hoursPerWeek, payRate
		setPositionName(positionName);
		setHoursPerWeek(hoursPerWeek);
		setPayRate(payRate);
		
		// create new empty List (can use either ArrayList or LinkedList)
		applications = new ArrayList<Application>();
	}
	
	/**
	 * Sets the counter for the Application instances to the value of 
	 * the maximum id in the list of Applications for the position + 1 using Application.setCounter().
	 */
	public void setApplicationId() {
		
		int maxId = 0;
		// find maxId in list of applications
		for (int i = 0; i < applications.size(); i++) {
			if (maxId < applications.get(i).getId()) {
				maxId = applications.get(i).getId();
			}
		}
		
		Application.setCounter(maxId);
	}
	
	/**
	 * Error checks and sets name of position
	 * @param positionName name of position
	 * @throws IllegalArgumentException with message "Position cannot be created." if positionName is null or empty string
	 */
	private void setPositionName(String positionName) {
		// error check for null or empty string
		if (positionName == null || positionName.length() == 0) {
			throw new IllegalArgumentException("Position cannot be created.");
		}
		
		// set field
		this.positionName = positionName;
	}
	
	/**
	 * Gets the name of a position
	 * @return position name
	 */
	public String getPositionName() {
		return positionName;
	}
	
	/**
	 * Error checks and sets the hours worked per week for a position
	 * @param hoursPerWeek position's hours per week
	 * @throws IllegalArgumentException with message "Position cannot be created." if hoursPerWeek are less than 5 or more than 20
	 */
	private void setHoursPerWeek(int hoursPerWeek) {
		// error check for valid boundaries
		if (hoursPerWeek < 5 || hoursPerWeek > 20) {
			throw new IllegalArgumentException("Position cannot be created.");
		}
		
		// set field
		this.hoursPerWeek = hoursPerWeek;
	}
	
	/** 
	 * Gets hours worked per week
	 * @return position's hours per week
	 */
	public int getHoursPerWeek() {
		return hoursPerWeek;
	}
	
	/**
	 * Error checks and sets pay rate of a Position
	 * @param payRate Position's pay rate
	 * @throws IllegalArgumentException with message "Position cannot be created." if payRate is less than 7 or more than 35
	 */
	private void setPayRate(int payRate) {
		// check that payRate is within valid boundaries
		if (payRate < 7 || payRate > 35) {
			throw new IllegalArgumentException("Position cannot be created.");
		}
		
		// set field
		this.payRate = payRate;
	}
	
	/**
	 * Gets pay rate of a position
	 * @return position pay rate
	 */
	public int getPayRate() {
		return payRate;
	}
	
	/**
	 * Creates a new application using given fields in the submitted state, 
	 * adds new application to the list in sorted order, returns new application's id.
	 * @param firstName first name of applicant
	 * @param surname surname of applicant
	 * @param unityId unity id of applicant
	 * @return application id of new application added to list
	 */
	public int addApplication(String firstName, String surname, String unityId) {
		// create new application
		Application a = new Application(firstName, surname, unityId);
		// add application
		applications.add(a);
		
		return a.getId();
	}
	
	/**
	 * Adds application object to list in sorted order by id.
	 * @param application application being added to list
	 * @return id of newly added Application
	 * @throws IllegalArgumentException with message "Application cannot be created." if an application
	 * already exists with given id.
	 */
	public int addApplication(Application application) {
		// check for duplicates - if return value is not null then application already exists in list
		if (getApplicationById(application.getId()) != null) {
			throw new IllegalArgumentException("Application cannot be created.");
		} 
		
		// add applications
		applications.add(application);
		
		// sort added applications by id
		for (int i = 0; i < applications.size(); i++) {
			
			for (int j = applications.size() - 1; j > i; j--) {
				if (applications.get(i).getId() > applications.get(j).getId()) {
					Application temp = applications.get(j);
					applications.remove(j);
					applications.add(i, temp);
				}
			}
		}
		
		return application.getId();
	}
	
	/**
	 * Gets the list of applications corresponding to the position 
	 * @return list of applications
	 */
	public List<Application> getApplications() {
		return applications;
	}
	
	/**
	 * Gets application from Position list using Application id
	 * @param id application id used to find specified application in Position list
	 * @return application from position list using given id. If there is no application with given id, return null.
	 */
	public Application getApplicationById(int id) {
		// find application matching given id in list
		for (int i = 0; i < applications.size(); i++) {
			if (applications.get(i).getId() == id) {
				// match found!
				return applications.get(i);
			}
		}
		
		// if no application can be found, return null
		return null;
	}
	
	/**
	 * Issues a specified command to an id-specified application to initiate a state transition in the FSM.
	 * If there is no Application with given id, the list doesn't change.
	 * @param id if of application that is being issued command
	 * @param c command that is being executed
	 * @throws UnsupportedOperationException with message "Invalid command.", THROWN ORIGINALLY in Application FSM, if command initiates invalid transition.
	 */
	public void executeCommand(int id, Command c) {
		getApplicationById(id).update(c);
	}
	
	/**
	 * Deletes application by id. If there is no application with given id, the list doesn't change.
	 * @param id id of application that is being deleted
	 */
	public void deleteApplicationById(int id) {
		// check if id exists in list -> id exists if return value is not null
		if (getApplicationById(id) == null) {
			return; // application DNE so no changes -> exit method
		} else {
			applications.remove(getApplicationById(id));
		}
	}
	
	/**
	 * Creates a string representation of the information of a Position object.
	 * @return formatted string representation of Position
	 */
	public String toString() {
		// Format:
		// # PositionName,hoursPerWeek,payRate
		// * id,state,fistName,surname,unityId,reviewerId,note
		// * ...
		
		String position = "# " + positionName + "," + hoursPerWeek + "," + payRate;
		String application = "";
		for (int i = 0; i < applications.size(); i++) {
			application = application + applications.get(i).toString() + "\n";
		}
		
		if ("".equals(application)) {
			return "";
		} else {
			return position + "\n" + application;
		}
	}
}
