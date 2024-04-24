package edu.ncsu.csc216.wolf_hire.model.command;

/**
 * Contains the object representation of the command that a user would use to perform a state transition
 * in the WolfHire FSM.
 * 
 * Command class encapsulates the information about a user's command that leads to a transition in WolfHire's FSM.
 * 
 * Contains one inner enumeration, CommandValue. 
 * CommandValue represents one of the eight possible commands a user can make to perform a state change.
 * These eight commands are: ASSIGN, REJECT, RESUBMIT, RETURN, SCHEDULE, PROCESS, HIRE, TERMINATE, in no particular order.
 * 
 * A command knows its ComandValue and its commandInformation, which either reflects the reviewer ID, the rejection reason, 
 * or the termination reason.
 * 
 * @author hmreese2
 *
 */
public class Command {

	/** 
	 * Enumeration of Command that holds all possible Command values
	 * @author hmreese2
	 *
	 */
	public enum CommandValue { ASSIGN, REJECT, RESUBMIT, RETURN, SCHEDULE, PROCESS, HIRE, TERMINATE }
	
	/** Represents the value of the command used to initiate a state transition */
	private CommandValue command;
	/** String representation of command information - reflects associated application reviewer ID, rejection reason, or termination reason */
	private String commandInformation;

	/**
	 * Constructs a Command object using value of command and information of command
	 * @param command value of command that is being issued
	 * @param commandInformation information that represents command's associated application's assigned reviewer id or rejection/termination reason. 
	 * @throws IllegalArgumentException with message "Invalid information." if command is null,
	 * or if command is ASSIGN,REJECT,TERMINATE and has null/empty string commandInformation value,
	 * or if command is RESUBMIT,RETURN,SCHEDULE,PROCESS,HIRE and has non-null commandInformation.
	 */
	public Command(CommandValue command, String commandInformation) {
		// error check for null command
		if (command == null) {
			throw new IllegalArgumentException("Invalid information.");
		}
		
		// error check for valid commandInformation values given the CommandValue value
		if (commandInformation != null && (command == CommandValue.RETURN || command == CommandValue.RESUBMIT || command == CommandValue.SCHEDULE
				|| command == CommandValue.PROCESS || command == CommandValue.HIRE)) {
			throw new IllegalArgumentException("Invalid information.");
		} else if ((commandInformation == null || commandInformation.length() == 0) && (command == CommandValue.ASSIGN || command == CommandValue.REJECT 
				|| command == CommandValue.TERMINATE)) {
			throw new IllegalArgumentException("Invalid information.");
		}
		
		// set fields
		this.command = command;
		this.commandInformation = commandInformation;
	}
	
	/**
	 * Gets the command type that is being given
	 * @return value of command
	 */
	public CommandValue getCommand() {
		return command;
	}
	
	/**
	 * Gets command information
	 * @return command information as a String
	 */
	public String getCommandInformation() {
		return commandInformation;
	}
}
