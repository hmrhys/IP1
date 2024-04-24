package edu.ncsu.csc216.wolf_hire.model.io;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import edu.ncsu.csc216.wolf_hire.model.application.Application;
import edu.ncsu.csc216.wolf_hire.model.manager.Position;

/**
 * Processes a file containing position and application information.
 * Creates a List of Position objects and the objects' associated Application object(s).
 * A processed position must have at least ONE application in order to be considered "valid" and added to the positions ArrayList variable.
 * 
 * Works closely with manager.WolfHire loadPositionsFromFile() method to load pre-existing lists of positions and their corresponding lists of applications.
 * Invalid positions or applications are skipped. Positions must have at least one application to be considered valid.
 * 
 * @author hmreese2
 *
 */
public class PositionReader {

	/**
	 * Unused constructor for PositionReader; remains empty, used by default.
	 */
	public PositionReader() {
		// empty constructor
	}
	
	/**
	 * Reads the file containing Position information, processes data, and uses that data to 
	 * construct Position, Application, and Position list objects.
	 * Any invalid applications or positions (they cannot be constructed) are to be ignored.
	 * @param fileName name of file being read and processed
	 * @return ArrayList of Positions and their respective Application objects, derived from given file.
	 * @throws IllegalArgumentException with message "Unable to load file [fileName]." if file cannot be loaded because it doesn't exist.
	 */
	public static ArrayList<Position> readPositionFile(String fileName) {
		
		ArrayList<Position> positions = new ArrayList<Position>();
		// create String object to read the whole file into the String object, line by line
		String fileContents = "";		
		try {
			Scanner fileReader = new Scanner(new FileInputStream(fileName));
			// read in file contents
			// make sure to add in \n characters at the end of each line since those are used as delimiters 
			while (fileReader.hasNextLine()) {
				if (!fileReader.hasNextLine()) {
					fileContents += fileReader.nextLine();
					break;
				}
				
				fileContents += fileReader.nextLine() + "\n";
			}
									
			fileReader.close();
			
			// check that the first character is #, if not the file is invalid and an empty list should be returned
			if (fileContents.charAt(0) != '#') {
				return positions;
			}
			
			Scanner contentReader = new Scanner(fileContents);
			contentReader.useDelimiter("\\r?\\n?[#]");
			
			// make position objects
			while(contentReader.hasNext()) {
				
				try {
				// make position objects with applications (if applicable), and add to list
				positions.add(processPosition(contentReader.next()));
				
				} catch (Exception e) {
					// skip application/position if there is an issue making or adding them
				}
			}
							
			contentReader.close();
			
			// check if position has at least one application - position must have at least one application to be in the list!
			for (int i = 0; i < positions.size(); i++) {
				if (positions.get(i).getApplications().size() == 0) {
					// if position has no applications, remove it from list
					positions.remove(i);
				}
			}
						
		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException("Unable to load file " + fileName);
		}
		
		return positions;
	}
	
	/**
	 * Processes Position text, breaks apart and uses processPositionLine() to create a Position object.
	 * Then, uses processApplication() to create Application objects and adds those to Position object (if applicable).
	 * Returns the newly created Position with Applications (if applicable). Otherwise, an empty Position is returned.
	 * @param positionText text that contains Position and possible application data
	 * @return Position object constructed from data of passed in text, with or without applications.
	 */
	private static Position processPosition(String positionText) {
		Scanner line = new Scanner(positionText);
		
		// make position from first line - using nextLine().trim() gets ride of the Position line for us in the scanner and excess whitespace, so that the application line(s) are all that's left.
		Position p = processPositionLine(line.nextLine().trim());
		
		// make applications and add them to position
		line.useDelimiter("\\r?\\n?[*]");
		if (!line.hasNext()) {
			line.close();
			throw new IllegalArgumentException();
		} else {
			while (line.hasNext()) {
				try {
				p.addApplication(processApplication(line.next().trim()));
				} catch (Exception e) {
					// skip application if there is an issue making or adding it
				}
			}
		}	
		
		line.close();
				
		return p;
	}
	
	/**
	 * Processes a line of Position information and creates a Position object
	 * @param positionLine line that contains Position information
	 * @return Position object constructed from processed information
	 */
	private static Position processPositionLine(String positionLine) {
		Scanner line = new Scanner(positionLine);
		
		String positionName = "";
		int hoursPerWeek = 0;
		int payRate = 0;
		
		// break apart first line using comma delimiter to create new Position (using processPositionLine)
		line.useDelimiter(",");
		
		while (line.hasNext()) {
			positionName = line.next();
			hoursPerWeek = line.nextInt();
			payRate = line.nextInt();
		}
		
		line.close();
		
		return new Position(positionName, hoursPerWeek, payRate);
	}
	
	/**
	 * Processes line of Application information and creates a subsequent Application object.
	 * @param applicationLine line of Application information
	 * @return application object constructed from processed application information
	 */
	private static Application processApplication(String applicationLine) {
		Scanner line = new Scanner(applicationLine);
		line.useDelimiter(",");
			
		// set fields
		int id = line.nextInt();
		String state = line.next();
		String firstName = line.next();
		String surname = line.next();
		String unityId = line.next();
		String reviewer = null;
		String note = null;
		// check for reviewer and note values
		if (line.hasNext() ) {
			reviewer = line.next();
			if (line.hasNext()) {
				note = line.next();
			}
		}
		
		// make sure that reviewer and note aren't returned as empty strings
		if ("".equals(reviewer)) {
			reviewer = null;
		}
		
		line.close();
						
		return new Application(id, state, firstName, surname, unityId, reviewer, note);		
				
	}
}
