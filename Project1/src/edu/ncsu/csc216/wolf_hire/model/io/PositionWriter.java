/**
 * 
 */
package edu.ncsu.csc216.wolf_hire.model.io;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;

import edu.ncsu.csc216.wolf_hire.model.manager.Position;

/**
 * Writes Position object(s) and their Application(s) to the file name provided by the user.
 * In order to be considered "valid" to write, the positions must each contain at least ONE application. Otherwise they should be skipped.
 * 
 * Works closely with the manager.WolfHire class to "save" positions and applications in WolfHire's savePositionstoFile() method.
 * 
 * @author hmreese2
 *
 */
public class PositionWriter {

	/**
	 * Unused constructor for PositionWriter()
	 * Remains empty; is defaulted by system.
	 */
	public PositionWriter() {
		// remains empty
	}
	/**
	 * Writes Position information to a file provided by user
	 * @param fileName name of file provided by user to write information to
	 * @param positions positions that are being written to a file
	 * @throws IllegalArgumentException with message "Unable to save file." if there are any errors trying to save file
	 */
	public static void writePositionsToFile(String fileName, ArrayList<Position> positions) {
		
		try {
			PrintStream p = new PrintStream(new File(fileName));
			
			for (int i = 0; i < positions.size(); i++) {
				p.print(positions.get(i).toString());
			}
			
			p.close();
		} catch (Exception e) {
			// catch any errors that occur and throw IAE
			throw new IllegalArgumentException("Unable to save file.");
		}
	}
}
