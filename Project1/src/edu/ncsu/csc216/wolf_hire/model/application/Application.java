/**
 * 
 */
package edu.ncsu.csc216.wolf_hire.model.application;

import edu.ncsu.csc216.wolf_hire.model.command.Command;
import edu.ncsu.csc216.wolf_hire.model.command.Command.CommandValue;

/**
 * Concrete class that implements and represents the context of WolfHire's State Pattern.
 * 
 * This class creates, stores, and manages all of the information for an Application object, 
 * which includes its current state.
 * An Application object knows its id, first name, surname, unity id, (optional) reviewer, and (optional) note.
 * 
 * The Application's state is updated when a Command encapsulating the specified transition is issued by the user.
 * Application class encapsulates the ApplicationState interface and seven concrete *State classes that represent each of the
 * seven possible state transitions in WolfHire's FSM.
 * 
 * @author hmreese2
 *
 */
public class Application {

	/** Represents unique id of application. Works closely with counter methods to maintain value, is set to counter value unless passed in id value is greater than counter. */
	private int applicationId;
	/** Represents first name of applicant */
	private String firstName;
	/** Represents last name of applicant */
	private String surname;
	/** Represents unity id of applicant */
	private String unityId;
	/** Represents reviewer of application (if applicable) */
	private String reviewer;
	/** Represents additional note included with application review - Contains rejection or termination reason for application (if applicable) */
	private String note;
	/** Counter that is incremented through incrementCounter() and set through setCounter() if id is greater than current counter value; used to set and maintain number for applicationId */
	private static int counter = 1;
	/** Represents name of Submitted state */
	public static final String SUBMITTED_NAME = "Submitted";
	/** Represents name of Rejected state */
	public static final String REJECTED_NAME = "Rejected";
	/** Represents name of Reviewing State */
	public static final String REVIEWING_NAME = "Reviewing";
	/** Represents name of Interviewing State */
	public static final String INTERVIEWING_NAME = "Interviewing";
	/** Represents name of Processing State */
	public static final String PROCESSING_NAME = "Processing";
	/** Represents name of Hired state */
	public static final String HIRED_NAME = "Hired";
	/** Represents name of Inactive state */
	public static final String INACTIVE_NAME = "Inactive";
	/** Represents rejection reason based on Qualifications */
	public static final String QUALIFICATIONS_REJECTION = "Qualifications";
	/** Represents rejection reason based on Incomplete */
	public static final String INCOMPLETE_REJECTION = "Incomplete";
	/** Represents rejection reason based on Positions */
	public static final String POSITIONS_REJECTION = "Positions";
	/** Represents rejection reason based on Duplicate */
	public static final String DUPLICATE_REJECTION = "Duplicate";
	/** Represents termination reason based on Completed */
	public static final String COMPLETED_TERMINATION = "Completed";
	/** Represents termination reason based on Resigned */
	public static final String RESIGNED_TERMINATION = "Resigned";
	/** Represents termination reason based on Fired */
	public static final String FIRED_TERMINATION = "Fired";
	/** Represents the current instance of the application's state */
	private ApplicationState currentState;
	/** Instance of SubmittedState of FSM */
	private final ApplicationState submittedState = new SubmittedState();
	/** Instance of RejectedState of FSM */
	private final ApplicationState rejectedState = new RejectedState();
	/** Instance of ReviewingState of FSM */
	private final ApplicationState reviewingState = new ReviewingState();
	/** Instance of InterviewingState of FSM */
	private final ApplicationState interviewingState = new InterviewingState();
	/** Instance of ProcessingState of FSM */
	private final ApplicationState processingState = new ProcessingState();
	/** Instance of HiredState of FSM */
	private final ApplicationState hiredState = new HiredState();
	/** Instance of InactiveState of FSM */
	private final ApplicationState inactiveState = new InactiveState();
	
	
	/**
	 * Interface for states in the Application State Pattern.  All 
	 * concrete Application states must implement the ApplicationState interface.
	 * The ApplicationState interface should be a private interface of the 
	 * Application class.
	 * 
	 * @author Dr. Sarah Heckman (sarah_heckman@ncsu.edu) 
	 */
	private interface ApplicationState {
		
		/**
		 * Update the Application from the given Command.
		 * An UnsupportedOperationException is thrown if the Command
		 * is not a valid action for the given state.  
		 * @param command Command describing the action that will update the Application's
		 * state.
		 * @throws UnsupportedOperationException if the Command is not a valid action
		 * for the given state.
		 */
		void updateState(Command command);
		
		/**
		 * Returns the name of the current state as a String.
		 * @return the name of the current state as a String.
		 */
		String getStateName();

	}
	
	/**
	 * Concrete private inner class of Application that implements ApplicationState interface.
	 * Represents the SUBMITTED state of the WolfHire FSM.
	 * 
	 * SubmittedState has no reviewer and no note.
	 * 
	 * @author hmreese2
	 *
	 */
	private class SubmittedState implements ApplicationState {

		/**
		 * Implements State transition.
		 * Updates application state to next state from given command.
		 * SubmittedState has no reviewer, no note
		 * @param command Command describing the action that will update the Application's
		 * state.
		 * @throws UnsupportedOperationException with message "Invalid command." if transition is invalid (command is not a valid action for given state)
		 */
		@Override
		public void updateState(Command command) {
			
			if (command.getCommand() == CommandValue.REJECT) {
				// transition to REJECTED state
				currentState = new RejectedState();
				// set rejectionReason
				setNote(command.getCommandInformation());
			} else if (command.getCommand() == CommandValue.ASSIGN) {
				// set reviewer
				setReviewer(command.getCommandInformation());
				// transition to REVIEWING state
				currentState = new ReviewingState();
			} else {
				throw new UnsupportedOperationException("Invalid command.");
			}
			
		}

		/**
		 * Gets state name of SubmittedState
		 * @return name of state
		 */
		@Override
		public String getStateName() {
			return SUBMITTED_NAME;
		}
		
	}
	
	/**
	 * Concrete private inner class of Application that implements ApplicationState interface.
	 * Represents the REJECTED state of the WolfHire FSM.
	 * 
	 * RejectedState has no reviewer, but a note highlighting reason for rejection.
	 * 
	 * @author hmreese2
	 *
	 */
	private class RejectedState implements ApplicationState {

		/**
		 * Implements State transition.
		 * Updates application state to next state from given command.
		 * RejectedState has no reviewer, has note for rejection reason
		 * @param command Command describing the action that will update the Application's
		 * state.
		 * @throws UnsupportedOperationException with message "Invalid command." if command is an invalid action for given state (attempt to execute invalid transition)
		 */
		@Override
		public void updateState(Command command) {
			// set reviewer back to null
			setReviewer(null);
			
			if (command.getCommand() == CommandValue.RESUBMIT) {
				// transition to SUBMITTED state
				currentState = new SubmittedState();
				// update note to null
				setNote(null);
			} else {
				throw new UnsupportedOperationException("Invalid command.");
			}
		}

		/**
		 * Gets state name of RejectedState
		 * @return name of state
		 */
		@Override
		public String getStateName() {
			return REJECTED_NAME;
		}
		
	}
	
	/**
	 * Concrete private inner class of Application that implements ApplicationState interface.
	 * Represents the REVIEWING state of the WolfHire FSM.
	 * 
	 * ReviewingState has reviewer but no note.
	 * 
	 * @author hmreese2
	 *
	 */
	private class ReviewingState implements ApplicationState {

		/**
		 * Implements State transition.
		 * Updates application state to next state using given command.
		 * ReviewingState has reviewer, no note
		 * @param command Command describing the action that will update the Application's
		 * state.
		 * @throws UnsupportedOperationException with message "Invalid command." if command is an invalid action for given state (attempt to execute invalid transition)
		 */
		@Override
		public void updateState(Command command) {
			if (command.getCommand() == CommandValue.RETURN) {
				// transition back to SUBMITTED state
				currentState = new SubmittedState();
				// set reviewer back to null
				setReviewer(null);
			} else if (command.getCommand() == CommandValue.REJECT) {
				// transition to REJECTED state
				currentState = new RejectedState();
				// set rejection reason
				setNote(command.getCommandInformation());
				// update reviewer to null
				setReviewer(null);
			} else if (command.getCommand() == CommandValue.SCHEDULE) {
				// transition to INTERVIEWING state
				currentState = new InterviewingState();
			} else {
				throw new UnsupportedOperationException("Invalid command.");
			}
		}

		/**
		 * Gets state name of ReviewingState
		 * @return state name
		 */
		@Override
		public String getStateName() {
			return REVIEWING_NAME;
		}
		
	}
	
	/**
	 * Concrete private inner class of Application that implements ApplicationState interface.
	 * Represents the INTERVIEWING state of the WolfHire FSM.
	 * 
	 * InterviewingState retains reviewer but has no note.
	 * 
	 * @author hmreese2
	 *
	 */
	private class InterviewingState implements ApplicationState {

		/**
		 * Implements State transition.
		 * Updates application state to next state using given command.
		 * InterviewingState has reviewer, has no note
		 * @param command Command describing the action that will update the Application's
		 * state.
		 * @throws UnsupportedOperationException with message "Invalid command." if command is an invalid action for given state (attempt to execute invalid transition)
		 */
		@Override
		public void updateState(Command command) {
			if (command.getCommand() == CommandValue.REJECT) {
				// transition to REJECTED state
				currentState = new RejectedState();
				// update note to rejection reason
				setNote(command.getCommandInformation());
				// update reviewer to null 
				setReviewer(null);
			} else if (command.getCommand() == CommandValue.ASSIGN) {
				// transition back to REVIEWING state
				currentState = new ReviewingState();
				// update reviewer
				setReviewer(command.getCommandInformation());
			} else if (command.getCommand() == CommandValue.SCHEDULE) {
				// transition to INTERVIEWING state (self-loop)
				currentState = new InterviewingState();
			} else if (command.getCommand() == CommandValue.PROCESS) {
				// transition to PROCESSING state
				currentState = new ProcessingState();
			} else {
				throw new UnsupportedOperationException("Invalid command.");
			}
		}

		/**
		 * Gets name of InterviewingState
		 * @return name of state
		 */
		@Override
		public String getStateName() {
			return INTERVIEWING_NAME;
		}
		
	}
	
	/**
	 * Concrete private inner class of Application that implements ApplicationState interface.
	 * Represents the PROCESSING state of the WolfHire FSM.
	 * 
	 * ProcessingState retains reviewer but has no note.
	 * 
	 * @author hmreese2
	 *
	 */
	private class ProcessingState implements ApplicationState {

		/**
		 * Implements State transition.
		 * Updates application state to next state using given command.
		 * ProcessingState retains reviewer, no note
		 * @param command Command describing the action that will update the Application's
		 * state.
		 * @throws UnsupportedOperationException with message "Invalid command." if command is an invalid action for given state (attempt to execute invalid transition)
		 */
		@Override
		public void updateState(Command command) {
			if (command.getCommand() == CommandValue.REJECT) {
				// transition to REJECTED state
				currentState = new RejectedState();
				// update note to rejection reason
				setNote(command.getCommandInformation());
				// update reviewer to null
				setReviewer(null);
			} else if (command.getCommand() == CommandValue.HIRE) {
				// transition to HIRED state
				currentState = new HiredState();
			} else {
				throw new UnsupportedOperationException("Invalid command.");
			}
			
		}

		/**
		 * Gets name of ProcessingState
		 * @return name of state
		 */
		@Override
		public String getStateName() {
			return PROCESSING_NAME;
		}
		
	}
	
	/**
	 * Concrete private inner class of Application that implements ApplicationState interface.
	 * Represents the HIRED state of the WolfHire FSM.
	 * 
	 * HiredState retains reviewer but has no note.
	 * 
	 * @author hmreese2
	 *
	 */
	private class HiredState implements ApplicationState {

		/**
		 * Implements State transition.
		 * Updates application state to next state using given command.
		 * HiredState retains reviewer, no note
		 * @param command Command describing the action that will update the Application's
		 * state.
		 * @throws UnsupportedOperationException with message "Invalid command." if command is an invalid action for given state (attempt to execute invalid transition)
		 */
		@Override
		public void updateState(Command command) {
			if (command.getCommand() == CommandValue.TERMINATE) {
				// transition to INACTIVE state
				currentState = new InactiveState();
				// update note to termination reason
				setNote(command.getCommandInformation());
			} else {
				throw new UnsupportedOperationException("Invalid command.");
			}
			
		}

		/**
		 * Gets name of HiredState
		 * @return name of state
		 */
		@Override
		public String getStateName() {
			return HIRED_NAME;
		}
		
	}
	
	/**
	 * Concrete private inner class of Application that implements ApplicationState interface.
	 * Represents the INACTIVE state of the WolfHire FSM.
	 * 
	 * InactiveState retains reviewer and a note that highlights termination reason.
	 * 
	 * @author hmreese2
	 *
	 */
	private class InactiveState implements ApplicationState {

		/**
		 * Implements State transition.
		 * Updates application state to next state from given command.
		 * InactiveState retains reviewer, note for termination reason
		 * @param command Command describing the action that will update the Application's
		 * state.
		 * @throws UnsupportedOperationException with message "Invalid command." if command is an invalid action for given state (attempt to execute invalid transition)
		 */
		@Override
		public void updateState(Command command) {
			// there are no valid transitions beyond INACTIVE state, so throw exception
			throw new UnsupportedOperationException("Invalid command.");
		}

		/**
		 * Gets name of InactiveState
		 * @return name of state
		 */
		@Override
		public String getStateName() {
			return INACTIVE_NAME;
		}
		
	}
	
	/**
	 * Basic (starter) constructor for Application that constructs an application with only firstName, surname, and unityId fields.
	 * A new application starts in the SUBMITTED state, both reviewer and note values are null.
	 * Application's fields are set by calling to the full Application constructor, where id is passed as counter, state as SUBMITTED, and reviewer/note fields as null.
	 * @param firstName first name of applicant
	 * @param surname surname of applicant
	 * @param unityId unity id of applicant
	 * @throws IllegalArgumentException with message "Application cannot be created." if any parameters are invalid (i.e. if any are null or empty strings)
	 */
	public Application(String firstName, String surname, String unityId) {
		this(counter, SUBMITTED_NAME, firstName, surname, unityId, null, null);
	}
	
	/**
	 * Longer (full) constructor for Application that constructs with id, state, first/last name, unity id, reviewer, and note fields.
	 * If the incoming id is greater than the current value in counter, then counter should be updated to id+1 using setCounter(id) method.
	 * If incoming id is less than counter, applicationId is set to value of id parameter. If if is equal to counter, counter is incremented using incrementCounter().
	 * Shorter constructor calls to this constructor to complete application construction (if necessary).
	 * @param id if of application
	 * @param state state of application
	 * @param firstName first name of applicant
	 * @param surname surname of applicant
	 * @param unityId unity id of applicant
	 * @param reviewer reviewer for application
	 * @param note note for application
	 * @throws IllegalArgumentException with message "Application cannot be created." if any parameters are invalid
	 */
	public Application(int id, String state, String firstName, String surname, String unityId, String reviewer, String note) {		
		// set basic fields (firstName, surname, unityId)
		setFirstName(firstName);
		setSurname(surname);
		setUnityId(unityId);
		// set reviewer before state (that way can check if reviewer is correct for given state)
		setReviewer(reviewer);
		setState(state);
		// set note after setting state (since that would determine which notes are valid given the state)
		setNote(note);
		// set id last -> ensures there are no errors before updating counter
		setId(id);
		
	}
	
	/**
	 * Error checks and sets the id of an application. Updates counter as necessary:
	 * If id is greater than current counter, counter is set to id + 1 using setCounter().
	 * If id is equal to current counter, counter is incremented by 1 using incrementCounter().
	 * If id is less than current counter, counter does not change.
	 * @param id application id
	 * @throws IllegalArgumentException with message "Application cannot be created." if id is negative or less than counter.
	 */
	private void setId(int id) {
		
		if (id <= 0) { 
			throw new IllegalArgumentException("Application cannot be created.");
		} 
		
		this.applicationId = id;
		if (id > counter) { 	
			setCounter(id);
		} else if (id == counter) {
			incrementCounter();
		}
	
	}
	
	/**
	 * Error checks and sets the state that an application is in
	 * @param stateValue value of application state
	 * @throws IllegalArgumentException with message "Application cannot be created." if state is null/empty string
	 * or if state is not a valid state (SUBMMITED, REJECTED, REVIEWING, INTERVIEWING, PROCESSING, HIRED, or INACTIVE),
	 * or if state has a null or non-null reviewer value for an inappropriate state.
	 */
	private void setState(String stateValue) {
		if (stateValue == null || stateValue.length() == 0) {
			throw new IllegalArgumentException("Application cannot be created.");
		}
		
		// error check appropriate state-corresponding parameters, set state or throw exception if invalid value
		switch(stateValue) {
		
			case SUBMITTED_NAME:
				if (reviewer != null) {
					throw new IllegalArgumentException("Application cannot be created.");
				}
				
				this.currentState = submittedState;
				break;
			
			case REJECTED_NAME:
				if (reviewer != null) {
					throw new IllegalArgumentException("Application cannot be created.");
				}
				this.currentState = rejectedState;
				break;
			
			case REVIEWING_NAME:
				if (reviewer == null) {
					throw new IllegalArgumentException("Application cannot be created.");
				}
				
				this.currentState = reviewingState;
				break;
				
			case INTERVIEWING_NAME:
				if (reviewer == null) {
					throw new IllegalArgumentException("Application cannot be created.");
				}
				
				this.currentState = interviewingState;
				break;
			
			case PROCESSING_NAME:
				if (reviewer == null) {
					throw new IllegalArgumentException("Application cannot be created.");
				}
				
				this.currentState = processingState;
				break;
				
			case HIRED_NAME:
				if (reviewer == null) {
					throw new IllegalArgumentException("Application cannot be created.");
				}
				
				this.currentState = hiredState;
				break;
			
			case INACTIVE_NAME:
				if (reviewer == null) {
					throw new IllegalArgumentException("Application cannot be created.");
				}
				
				this.currentState = inactiveState;
				break;
			
			default:
				throw new IllegalArgumentException("Application cannot be created.");
		}
	}
	
	/**
	 * Gets id of application
	 * @return id of application
	 */
	public int getId() {
		return applicationId;
	}
	
	/**
	 * Gets state of application
	 * @return currentState of application
	 */
	public String getState() {
		return currentState.getStateName();
	}
	
	/**
	 * Gets first name of application's applicant
	 * @return applicant's first name
	 */
	public String getFirstName() {
		return firstName;
	}
	
	/**
	 * Error checks and sets first name of application's applicant
	 * @param firstName on application
	 * @throws IllegalArgumentException with message "Application cannot be created." if firstName is null or empty string
	 */
	private void setFirstName(String firstName) {
		if (firstName == null || firstName.length() == 0) {
			throw new IllegalArgumentException("Application cannot be created.");
		}
		
		this.firstName = firstName;
	}
	
	/**
	 * Gets surname of application's applicant
	 * @return applicant surname
	 */
	public String getSurname() {
		return surname;
	}
	
	/**
	 * Error checks and sets surname of application's applicant
	 * @param surname on application
	 * @throws IllegalArgumentException with message "Application cannot be created." if surname is null or empty string
	 */
	private void setSurname(String surname) {
		if (surname == null || surname.length() == 0) {
			throw new IllegalArgumentException("Application cannot be created.");
		}
		
		this.surname = surname;
	}
	
	/**
	 * Gets unity id on application
	 * @return applicant unity id
	 */
	public String getUnityId() {
		return unityId;
	}
	
	/**
	 * Error checks and sets unity id of applicant
	 * @param unityId of applicant
	 * @throws IllegalArgumentException with message "Application cannot be created." if unityId parameter is null or empty string.
	 */
	private void setUnityId(String unityId) {
		if (unityId == null || unityId.length() == 0) {
			throw new IllegalArgumentException("Application cannot be created.");
		}
		
		this.unityId = unityId;
	}
	
	/**
	 * Gets application's reviewer
	 * @return reviewer of application
	 */
	public String getReviewer() {
		if (reviewer == null) {
			return "";
		}
		
		return reviewer;
	}
	
	/**
	 * Error checks for empty string value (further error checking for null in valid state is done in setState()) and sets reviewer of application.
	 * Can only take on values of null (e.g. not assigned a reviewer) or a non-empty string.
	 * @param reviewer application's reviewer
	 * @throws IllegalArgumentException with message "Application cannot be created." if parameter value is an empty string.
	 */
	private void setReviewer(String reviewer) {	
		if ("".equals(reviewer)) {
			throw new IllegalArgumentException("Application cannot be created.");
		}
		this.reviewer = reviewer;
	}
	
	/**
	 * Gets application note
	 * @return application note
	 */
	public String getNote() {
		if (note == null) {
			return "";
		}
		
		return note;
	}
	
	/**
	 * Error checks and sets note of application.
	 * Can only take values of null (e.g. no current rejection or termination reasons) or one of the rejection or termination reason constants.
	 * Valid Rejection Reasons: QUALIFICATIONS_REJECTION, INCOMPLETE_REJECTION, POSITIONS_REJECTION, DUPLICATE_REJECTION
	 * Valid Termination Reasons: COMPLETED_TERMINATION, RESIGNED_TERMINATION, FIRED_TERMINATION
	 * @param note application note
	 * @throws IllegalArgumentException with message "Application cannot be created." if value is empty string or null for an inappropriate state.
	 */
	private void setNote(String note) {
		if (note == null) {
			// check that null note is valid for current state
			if (currentState.getStateName().equals(REJECTED_NAME) || currentState.getStateName().equals(INACTIVE_NAME)) {
				throw new IllegalArgumentException("Application cannot be created.");
			}
			this.note = note;
			
		} else if (note.length() == 0) {
			throw new IllegalArgumentException("Application cannot be created.");
		} else {
			// check that appropriate message is set for current state
			switch(currentState.getStateName()) {
			
			case REJECTED_NAME:
				if (note.equals(Application.QUALIFICATIONS_REJECTION) || note.equals(Application.INCOMPLETE_REJECTION)
						|| note.equals(Application.POSITIONS_REJECTION) || note.equals(Application.DUPLICATE_REJECTION)) {
					this.note = note;
				} else {
					throw new IllegalArgumentException("Application cannot be created.");
				}
				
				break;
				
			case INACTIVE_NAME:
				if (note.equals(Application.COMPLETED_TERMINATION) || note.equals(Application.RESIGNED_TERMINATION)
						|| note.equals(Application.FIRED_TERMINATION)) {
					this.note = note;
				} else {
					throw new IllegalArgumentException("Application cannot be created.");
				}
				
				break;
				
			default:
				throw new IllegalArgumentException("Application cannot be created.");
			} 
		}
	}
	
	/**
	 * Increments counter variable.
	 * Used for updating list placement.
	 */
	public static void incrementCounter() {
		counter++;
	}
	
	/**
	 * Sets value for counter variable to newCount.
	 * Updates counter to id + 1 if id is greater than counter.
	 * @param newCount value being set to counter
	 */
	public static void setCounter(int newCount) {
		
		if (newCount == 1) {
			counter = newCount;
		} else {
			counter = newCount + 1;
		}
	
	}
	
	/**
	 * Returns application information as a formatted string representation of the information.
	 * If reviewer or note are null they should be changed to empty string values in the return statement.
	 * @return formatted string representation of application object's information
	 */
	public String toString() {
		return "* " + getId() + "," + getState() + "," + getFirstName() + "," + getSurname() + "," + getUnityId() + "," + getReviewer() + "," + getNote();
	}
	
	/**
	 * Command issued to application to update its state placement in the FSM.
	 * @param c command given to application
	 * @throws UnsupportedOperationException with message "Invalid command." if transition is invalid with given command
	 */
	public void update(Command c) {	
		switch (c.getCommand()) {
		case REJECT:
			if (c.getCommandInformation().equals(DUPLICATE_REJECTION) || c.getCommandInformation().equals(QUALIFICATIONS_REJECTION) 
					|| c.getCommandInformation().equals(INCOMPLETE_REJECTION) || c.getCommandInformation().equals(POSITIONS_REJECTION)) {
				currentState.updateState(c);
			} else {
				throw new UnsupportedOperationException("Invalid command!!");
			}
			break;
		case TERMINATE:
			if (c.getCommandInformation().equals(FIRED_TERMINATION) || c.getCommandInformation().equals(RESIGNED_TERMINATION) 
					|| c.getCommandInformation().equals(COMPLETED_TERMINATION)) {
				currentState.updateState(c);
			} else {
				throw new UnsupportedOperationException("Invalid command!!");
			}
			break;
		default:
			currentState.updateState(c);
		}
		
		

	}
}
