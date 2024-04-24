package edu.ncsu.csc216.wolf_hire.model.manager;

import java.util.ArrayList;

import edu.ncsu.csc216.wolf_hire.model.application.Application;
import edu.ncsu.csc216.wolf_hire.model.command.Command;
import edu.ncsu.csc216.wolf_hire.model.io.PositionReader;
import edu.ncsu.csc216.wolf_hire.model.io.PositionWriter;

/**
 * Concrete class that maintains a list of Positions and their associated application lists, the active position, and handles
 * commands from the GUI.
 * 
 * WolfHire class implements the Singleton Design Pattern, where it constructs a single instance of itself to implement functionality.
 * Singleton Design Pattern means that only one instance of, in this case WolfHire, can ever be created. It ensures that all parts of WolgHireGUI are interacting
 * with the same WolfHire instance at all times.
 * 
 * @author hmreese2
 *
 */
public class WolfHire {
	
	/** ArrayList of Position type that holds the list of positions */
	private ArrayList<Position> positions;
	/** Represents the active position in the WolfHire system */
	private Position activePosition;
	/** Single instance of WolfHire that represents Singleton design pattern */
	private static WolfHire singleton;

	/**
	 * Private constructor for WolfHire, utilizes Singleton Design Pattern for construction.
	 * Is called when singleton is null, return singleton instance
	 */
	private WolfHire() {
		this.positions = new ArrayList<Position>();
		this.activePosition = getActivePosition();
	}
	
	/**
	 * Implements Singleton design pattern. 
	 * Creates the one single instance of WolfHire that is ever used.
	 * getInstance() checks if the singleton is null. If it is, getInstance() calls private WolfHire() constructor to create single instance.
	 * @return a single instance of WolfHire using the singleton design pattern
	 */
	public static WolfHire getInstance() {
		if (singleton == null) {
			singleton = new WolfHire();
		}
		return singleton;
	}
	
	/**
	 * Loads Position information(s) from file and reads/processes contents using PositionReader.
	 * The returned list of Positions are added to the positions field.
	 * The first position in the list returned from PositionReader is made the activePosition.
	 * 
	 * Works closely with PositionReader io class to correctly load in file and file contents.
	 * 
	 * @param fileName name of file that is being loaded
	 */
	public void loadPositionsFromFile(String fileName) {
		try {
			// if list is currently empty, add loaded list read from file
			if (positions == null) {
				this.positions = PositionReader.readPositionFile(fileName);
				this.activePosition = positions.get(0);
			} else {
				// if list is not currently empty, add loaded list to rest of positions list
				ArrayList<Position> list = PositionReader.readPositionFile(fileName);
				for (int i = 0; i < list.size(); i++) {
					this.positions.add(list.get(i));
				}
				
				this.activePosition = list.get(0);
			}
		} catch (Exception e) {
			// exception throw in PositionReader class
		}
	}
	
	/**
	 * Saves positions to a file.
	 * Works closely with PositionWriter io class to correctly write list of positions contents to file
	 * @param fileName name of file being written to
	 * @throws IllegalArgumentException with message "Unable to save file." if activePosition is null, or issue in saving file (thrown in PositionWriter).
	 */
	public void savePositionsToFile(String fileName) {
		// check for null activePosition
		if (activePosition == null) {
			throw new IllegalArgumentException("Unable to save file.");
		}
		
		// save positions to file
		try {
			PositionWriter.writePositionsToFile(fileName, positions);
		} catch (Exception e) {
			// thrown in PositionWriter
		}
	}
	
	/**
	 * Creates a new position with the given info and adds it to the end of the WolfHire Positions list.
	 * Position is then loaded as the "activePosition" by calling the loadPosition(String positionName) method.
	 * @param positionName name of position
	 * @param hoursPerWeek hours per week worked for a position
	 * @param payRate rate of pay for a position
	 * @throws IllegalArgumentException with message "Position cannot be created." if positionName parameter is null, empty string, or duplicate of an existing position-name (CASE-SENSITIVE).
	 */
	public void addNewPosition(String positionName, int hoursPerWeek, int payRate) {
		// error check for null or empty string values
		if (positionName == null || "".equals(positionName)) {
			throw new IllegalArgumentException("Position cannot be created.");
		}
		
		// error check for duplicate values
		for (int i = 0; i < getPositionList().length; i++) {
			if (positionName.equals(getPositionList()[i])) {
				throw new IllegalArgumentException("Position cannot be created.");
			}
		}
		
		// create new position with parameter values
		Position p = new Position(positionName, hoursPerWeek, payRate);
		
		// add to end of position list
		positions.add(p);
		
		// load new position as activePosition
		loadPosition(positionName);
	}
	
	/**
	 * Finds Position with the given name in the list, makes it active or activePosition,
	 * and sets the application id for that position so that any new Applications added to position are 
	 * created with the next correct id.
	 * @param positionName name of position being loaded
	 * @throws IllegalArgumentException with message "Position not available." if there is not position with the given name.
	 */
	public void loadPosition(String positionName) {
		// check if there is position name that matches positionName parameter
		boolean match = false;
		for (int i = 0; i < getPositionList().length; i++) {
			if (positionName.equals(getPositionList()[i])) {
				match = true;
				// make position the activePosition
				this.activePosition = positions.get(i);
				
				// set application id for position so that future applications are correctly numbered
				activePosition.setApplicationId();
			}
		}
		
		// if no position name matches, throw IAE
		if (!match) {
			throw new IllegalArgumentException("Position not available.");
		} 
	}
	
	/**
	 * Gets the name of the position that is currently active in system
	 * @return activePosition, or null if there is no activePosition.
	 */
	public Position getActivePosition() {
		if (activePosition == null) {
			return null;
		}
		return activePosition;
	}
	
	/**
	 * Gets the name of the current activePosition
	 * @return name of activePosition. If activePosition is null, then null is returned.
	 */
	public String getActivePositionName() {
		// do null check - if activePosition is null, return null for name
		if (activePosition == null) {
			return null;
		}
		
		return activePosition.getPositionName();
	}
	
	/**
	 * Gets the list of Positions by name in order they appear as a 1D String array list.
	 * @return 1D String array list of position names in the order they are listed in positions list. If no positions, return empty list.
	 */
	public String[] getPositionList() {
		// check if list is empty - if so, return empty String[] list
		if (positions.size() == 0) {
			return new String[0];
		}
		
		// populate list
		String[] list = new String[positions.size()];
		for (int i = 0; i < positions.size(); i++) {
			list[i] = positions.get(i).getPositionName();
		}
		
		return list;
	}
	
	/**
	 * Add application to a position using all application fields
	 * @param firstName first name of applicant that is being added to position
	 * @param surname last name of applicant
	 * @param unityId applicant's unityId
	 * @throws IllegalArgumentException with message "Application cannot be created." in Application class if there is issue constructing application
	 */
	public void addApplicationToPosition(String firstName, String surname, String unityId) {
		// check if activePosition is null
		if (activePosition == null) {
			return;
		}
		
		try {
			activePosition.addApplication(firstName, surname, unityId);
		} catch (IllegalArgumentException e) {
			// thrown in Application class
		}
	}
	
	/**
	 * Executes a command to a given application by application id
	 * @param id if of application that is being issued the command
	 * @param c command that is being issued to application
	 * @throws IllegalArgumentException with message "Invalid information." if id is invalid.
	 */
	public void executeCommand(int id, Command c) {
		// check if activePosition is null
		if (activePosition == null) {
			return;
		}
		// check if there exists application with given id in activePosition
		if (activePosition.getApplicationById(id) == null) {
			throw new IllegalArgumentException("Invalid information.");
		}
		
		// execute command
		activePosition.getApplicationById(id).update(c);
	}
	
	/**
	 * Locates an application by its id and deletes it from Position.
	 * GUI handles selected application, developer does not need to worry whether the id exists in activePosition or not
	 * @param id id of application being deleted.
	 */
	public void deleteApplicationById(int id) {
		// check if activePosition is null - if so, nothing should happen
		if (activePosition == null) {
			return;
		}
		
		activePosition.getApplications().remove(getApplicationById(id));
	}
	
	/**
	 * Displays application list as 2D String array list.
	 * Format: a row for every application in activePosition, 4 columns (id, state, unityId, reviewer)
	 * @param filter string that represents what item the arrays need to be filtered by (either *State option, or All which lists all information).
	 * @return 2D String array list of applications filtered by either their state, of filter value is *State, or by listing all information if filter value is "All"
	 * Or null if there is no activePosition
	 */
	public String[][] getApplicationsAsArray(String filter) {
		// null check for activePosition
		if (activePosition == null) {
			return null;
		}
		
		// keep count of how many times a state is listed
		int submittedCount = 0;
		int rejectedCount = 0;
		int interviewingCount = 0;
		int processingCount = 0;
		int reviewingCount = 0;
		int hiredCount = 0;
		int inactiveCount = 0;
		
		// update counters and populate potential list for ALL filter
		String[][] all = new String[activePosition.getApplications().size()][4];
		for (int i = 0; i < all.length; i++) {
			Application a = activePosition.getApplications().get(i);
			all[i][0] = a.getId() + "";
			all[i][1] = a.getState();
			all[i][2] = a.getUnityId();
			all[i][3] = a.getReviewer();
			
			// update state counters
			if ("Submitted".equals(a.getState())) {
				submittedCount++;
			} else if ("Rejected".equals(a.getState())) {
				rejectedCount++;
			} else if ("Reviewing".equals(a.getState())) {
				reviewingCount++;
			} else if ("Interviewing".equals(a.getState())) {
				interviewingCount++;
			} else if ("Processing".equals(a.getState())) {
				processingCount++;
			} else if ("Hired".equals(a.getState())) {
				hiredCount++;
			} else if ("Inactive".equals(a.getState())) {
				inactiveCount++;
			}
		}
		
		// create lists using counters for correct filter, or return previous ALL list if filter is ALL
		int k = 0;
		switch(filter) {
		
			case "Submitted":
				String[][] submitted = new String[submittedCount][4];
				for (int i = 0; i < all.length; i++) {
					Application a = activePosition.getApplications().get(i);
					if ("Submitted".equals(a.getState())) {
						submitted[k][0] = a.getId() + "";
						submitted[k][1] = a.getState();
						submitted[k][2] = a.getUnityId();
						submitted[k][3] = a.getReviewer();
						k++;
					}
				}	
				
				if (k == 0) {
					return new String[0][0];
				} else {
					return submitted;
				}
			
			case "Rejected":
				String[][] rejected = new String[rejectedCount][4];
				for (int i = 0; i < activePosition.getApplications().size(); i++) {
					Application a = activePosition.getApplications().get(i);
					if ("Rejected".equals(a.getState())) {
						rejected[k][0] = a.getId() + "";
						rejected[k][1] = a.getState();
						rejected[k][2] = a.getUnityId();
						rejected[k][3] = a.getReviewer();
						k++;
					}
				}	
				
				if (k == 0) {
					return new String[0][0];
				} else {
					return rejected;
				}
			
			case "Reviewing":
				String[][] reviewing = new String[reviewingCount][4];
				for (int i = 0; i < activePosition.getApplications().size(); i++) {
					Application a = activePosition.getApplications().get(i);
					if ("Reviewing".equals(a.getState())) {
						reviewing[k][0] = a.getId() + "";
						reviewing[k][1] = a.getState();
						reviewing[k][2] = a.getUnityId();
						reviewing[k][3] = a.getReviewer();
						k++;
					}
				}	
				
				if (k == 0) {
					return new String[0][0];
				} else {
					return reviewing;
				}
			
			case "Interviewing":
				String[][] interviewing = new String[interviewingCount][4];
				for (int i = 0; i < activePosition.getApplications().size(); i++) {
					Application a = activePosition.getApplications().get(i);
					if ("Interviewing".equals(a.getState())) {
						interviewing[k][0] = a.getId() + "";
						interviewing[k][1] = a.getState();
						interviewing[k][2] = a.getUnityId();
						interviewing[k][3] = a.getReviewer();
						k++;
					}
				}	
				
				if (k == 0) {
					return new String[0][0];
				} else {
					return interviewing;
				}
				
			case "Processing":
				String[][] processing = new String[processingCount][4];
				for (int i = 0; i < activePosition.getApplications().size(); i++) {
					Application a = activePosition.getApplications().get(i);
					if ("Processing".equals(a.getState())) {
						processing[k][0] = a.getId() + "";
						processing[k][1] = a.getState();
						processing[k][2] = a.getUnityId();
						processing[k][3] = a.getReviewer();
						k++;
					}
				}	
				
				if (k == 0) {
					return new String[0][0];
				} else {
					return processing;
				}
				
			case "Hired":
				String[][] hired = new String[hiredCount][4];
				for (int i = 0; i < activePosition.getApplications().size(); i++) {
					Application a = activePosition.getApplications().get(i);
					if ("Hired".equals(a.getState())) {
						hired[k][0] = a.getId() + "";
						hired[k][1] = a.getState();
						hired[k][2] = a.getUnityId();
						hired[k][3] = a.getReviewer();
						k++;
					}
				}	
				
				if (k == 0) {
					return new String[0][0];
				} else {
					return hired;
				}
				
			case "Inactive":
				String[][] inactive = new String[inactiveCount][4];
				for (int i = 0; i < activePosition.getApplications().size(); i++) {
					Application a = activePosition.getApplications().get(i);
					if ("Inactive".equals(a.getState())) {
						inactive[k][0] = a.getId() + "";
						inactive[k][1] = a.getState();
						inactive[k][2] = a.getUnityId();
						inactive[k][3] = a.getReviewer();
						k++;
					}
				}	
				
				if (k == 0) {
					return new String[0][0];
				} else {
					return inactive;
				}
				
			default:
				return all;
		}
		
	}
	
	/**
	 * Locates an application from activePosition using given id and returns it
	 * @param id id of potential application in activePosition
	 * @return application that matches given id, or null if there is no application with the given id in the activePosition
	 */
	public Application getApplicationById(int id) {
		// check if activePosition is null
		if (activePosition == null) {
			return null;
		}
		
		// look through activePosition's application list for application with matching id
		for (int i = 0; i < activePosition.getApplications().size(); i++) {
			if (activePosition.getApplications().get(i).getId() == id) {
				return activePosition.getApplications().get(i);
			}
		}
		
		// if no match is found return null
		return null;
	}
	
	/**
	 * Resets WolfHire manager.
	 * Intended for testing purposes only.
	 */
	protected void resetManager() {
		this.positions = new ArrayList<Position>();
		this.activePosition = null;
		singleton = null;
//		getInstance();
	}
}
