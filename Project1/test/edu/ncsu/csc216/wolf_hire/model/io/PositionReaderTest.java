/**
 * 
 */
package edu.ncsu.csc216.wolf_hire.model.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.fail;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import edu.ncsu.csc216.wolf_hire.model.manager.Position;

/**
 * Tests that PositionReader class is correctly taking, reading, processing, and constructing 
 * Position and Application objects using data from given files.
 * 
 * Checks that...
 * Invalid files that do not exist throw an IAE with the message "Unable to load file [fileName]".
 * Invalid files with missing or invalid fields are ignored and skipped.
 * Valid files are successfully loaded in with the correct contents.
 * 
 * @author hmreese2
 *
 */
public class PositionReaderTest {

	/** Invalid file - does not exist */
	private final String invalidFile1 = "test-files/positions0.txt";
	/** Valid positions file with one position */
	private final String validFile1 = "test-files/positions1.txt";
	/** Valid positions file with four positions */
	private final String validFile2 = "test-files/positions2.txt";
	/** Invalid file that is missing position name */
	private final String invalidFile2 = "test-files/positions3.txt";
	/** Invalid file - missing position hoursPerWeek */
	private final String invalidFile3 = "test-files/positions4.txt";
	/** Invalid file - missing position payRate */
	private final String invalidFile4 = "test-files/positions5.txt";
	/** Invalid file - missing application id */
	private final String invalidFile5 = "test-files/positions6.txt";
	/** Invalid file - missing application state */
	private final String invalidFile6 = "test-files/positions7.txt";
	/** Invalid file - missing application firstName */
	private final String invalidFile7 = "test-files/positions8.txt";
	/** Invalid file - missing application surname */
	private final String invalidFile8 = "test-files/positions9.txt";
	/** Invalid file - missing application unityId */
	private final String invalidFile9 = "test-files/positions10.txt";
	/** Invalid file - extra item */
	private final String invalidFile10 = "test-files/positions11.txt";
	/** Valid file - position with no applications */
	private final String validFile3 = "test-files/positions12.txt";
	/** Invalid file - Submitted state with reviewer */
	private final String invalidFile11 = "test-files/positions13.txt";
	/** Invalid file - Submitted state with note */
	private final String invalidFile12 = "test-files/positions14.txt";
	/** Invalid file - Rejected state with reviewer */
	private final String invalidFile13 = "test-files/positions15.txt";
	/** Invalid file - Rejected state with missing note */
	private final String invalidFile14 = "test-files/positions16.txt";
	/** Invalid file - Rejected state with incorrect note */
	private final String invalidFile15 = "test-files/positions17.txt";
	/** Invalid file - Reviewing state with missing reviewer */
	private final String invalidFile16 = "test-files/positions18.txt";
	/** Invalid file - Reviewing state with note */
	private final String invalidFile17 = "test-files/positions19.txt";
	/** Invalid file - Interviewing state with missing reviewer */
	private final String invalidFile18 = "test-files/positions20.txt";
	/** Invalid file - Interviewing state with note */
	private final String invalidFile19 = "test-files/positions21.txt";
	/** Invalid file - Processing state with missing reviewer */
	private final String invalidFile20 = "test-files/positions22.txt";
	/** Invalid file - Processing state with note */
	private final String invalidFile21 = "test-files/positions23.txt";
	/** Invalid file - Hired state with missing reviewer */
	private final String invalidFile22 = "test-files/positions24.txt";
	/** Invalid file - Hired state with note */
	private final String invalidFile23 = "test-files/positions25.txt";
	/** Invalid file - Inactive state with missing reviewer */
	private final String invalidFile24 = "test-files/positions26.txt";
	/** Invalid file - Inactive state with missing note */
	private final String invalidFile25 = "test-files/positions27.txt";
	/** Invalid file - Inactive state with wrong note */
	private final String invalidFile26 = "test-files/positions28.txt";
	
	/** Represents expected values for validFile1 */
	private final String[] validList1 = { "# CSC 216 PTF,12,15\n* 2,Submitted,Carol,Schmidt,cschmid,,\n"
			+ "* 3,Rejected,Kathleen,Gillespie,kgilles,,Incomplete\n* 4,Hired,Fiona,Rosario,frosari,sesmith5,\n"
			+ "* 7,Inactive,Deanna,Sanders,dsander,sesmith5,Completed\n"
			+ "* 8,Interviewing,Benjamin,Nieves,bmnieves,sesmith5,\n"
			+ "* 11,Processing,Quemby,Mullen,qmullen,sesmith5,\n" };
	
	/** Represents expected valued for validFile2 */
	private final String[] validList2 = { "# CSC 116 Grader,10,11\n"
			+ "* 1,Reviewing,Cailin,Roach,cvroach,tnmacnei,\n"
			+ "* 2,Rejected,Clinton,Armstrong,carmstr,,Qualifications\n"
			+ "* 3,Processing,Eagan,Cardenas,ecarden,tnmacnei,\n"
			+ "* 4,Processing,Ferdinand,Acevedo,faceved,tnmacnei,\n"
			+ "* 5,Hired,Craig,Armstrong,carmstr,tnmacnei,\n"
			+ "* 7,Inactive,Audrey,Kemp,akemp,tnmacnei,Fired\n" 
			+ "* 25,Interviewing,Harper,Holden,hholden,tnmacnei,\n",
			 "# CSC 216 PTF,12,15\n"
			+ "* 2,Submitted,Carol,Schmidt,cschmid,,\n"
			+ "* 3,Rejected,Kathleen,Gillespie,kgilles,,Incomplete\n"
			+ "* 4,Hired,Fiona,Rosario,frosari,sesmith5,\n"
			+ "* 7,Inactive,Deanna,Sanders,dsander,sesmith5,Completed\n"
			+ "* 8,Interviewing,Benjamin,Nieves,bmnieves,sesmith5,\n"
			+ "* 11,Processing,Quemby,Mullen,qmullen,sesmith5,\n",
			 "# CSC 226 Grader,8,15\n"
			+ "* 5,Hired,Karen,Mcguire,kamcguir,jdyoung2,\n"
			+ "* 7,Interviewing,Dean,Wilder,dwilder,jdyoung2,\n"
			+ "* 8,Inactive,Nehru,Contreras,ncontre,jdyoung2,Completed\n",
			 "# Research Assistant,15,16\n" 
			+ "* 3,Submitted,Inez,Koch,ikoch,,\n" 
			+ "* 7,Reviewing,Kerry,Tucker,kbtucker,tmbarnes,\n"};
	
	/**
	 * Tests that constructor is empty and that all return values are not null.
	 * Constructor is not used for this class PositionReader.
	 */
	@Test
	public void testConstructor() {
		assertNotNull(new PositionReader());
	}
	/**
	 * Tests reading in a file that doesn't exist file
	 */
	@Test
	public void testInvalidFileDoesNotExist() {
		// test loading a file that DNE
		Exception e = assertThrows(IllegalArgumentException.class,
				() -> PositionReader.readPositionFile(invalidFile1));
		
		// test that exception message is correct
		assertEquals("Unable to load file " + invalidFile1, e.getMessage());
	}
	
	/**
	 * Test loading in valid files with one position
	 */
	@Test
	public void testValidFileOnePosition() {
		// test loading a file with one position
		try {
			ArrayList<Position> positions = PositionReader.readPositionFile(validFile1);
			// check that contents loaded in correctly
			assertEquals(1, positions.size());
			assertEquals(6, positions.get(0).getApplications().size());
			for (int i = 0; i < validList1.length; i++) {
				assertEquals(validList1[i], positions.get(i).toString());
			}
		} catch (IllegalArgumentException e) {
			fail(e.getMessage());
		}		

	}
	
	/**
	 * Tests loading in a valid file with multiple positions and a position with applications out of order
	 */
	@Test
	public void testValidFileMultiplePositions() {
		// test loading in a file with multiple positions
		try {
			ArrayList<Position> positions = PositionReader.readPositionFile(validFile2);
			// check that contents loaded in correctly
			assertEquals(4, positions.size());
			assertEquals(7, positions.get(0).getApplications().size());
			for (int i = 0; i < validList2.length; i++) {
				assertEquals(validList2[i], positions.get(i).toString());
			}
		} catch (IllegalArgumentException e) {
			//e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	/**
	 * Tests loading in files with invalid contents (i.e. missing position/application fields, extra items)
	 */
	@Test
	public void testInvalidFileMissingContents() {
		// test loading in file with missing positionName
		try {
			ArrayList<Position> positions = PositionReader.readPositionFile(invalidFile2);
			// check that invalid position was skipped
			assertEquals(0, positions.size());	
		} catch (IllegalArgumentException e) {
			fail(e.getMessage());
		}
		
		// test loading in file with missing position hoursPerWeek
		try {
			ArrayList<Position> positions = PositionReader.readPositionFile(invalidFile3);
			// check that invalid position was skipped
			assertEquals(0, positions.size());
		} catch (IllegalArgumentException e) {
			fail(e.getMessage());
		}
		
		// test loading in file with missing position payRate
		try {
			ArrayList<Position> positions = PositionReader.readPositionFile(invalidFile4);
			// check that invalid position was skipped
			assertEquals(0, positions.size());
		} catch (IllegalArgumentException e) {
			fail(e.getMessage());
		}
		
		// test loading in file with missing application id
		try {
			ArrayList<Position> positions = PositionReader.readPositionFile(invalidFile5);
			// check that invalid application was skipped - list should be 0, position must have at least one valid application
			assertEquals(0, positions.size());
		} catch (IllegalArgumentException e) {
			fail(e.getMessage());
		}
		
		// test loading in file with missing application state
		try {
			ArrayList<Position> positions = PositionReader.readPositionFile(invalidFile6);
			// check that invalid application was skipped
			assertEquals(0, positions.size());
		} catch (IllegalArgumentException e) {
			fail(e.getMessage());
		}
		
		// test loading in file with missing application firstName
		try {
			ArrayList<Position> positions = PositionReader.readPositionFile(invalidFile7);
			// check that invalid application was skipped
			assertEquals(0, positions.size());
		} catch (IllegalArgumentException e) {
			fail(e.getMessage());
		}
		
		// test loading in file with missing application surname
		try {
			ArrayList<Position> positions = PositionReader.readPositionFile(invalidFile8);
			// check that invalid application was skipped
			assertEquals(0, positions.size());
		} catch (IllegalArgumentException e) {
			fail(e.getMessage());
		}
		
		// test loading in file with missing unityId
		try {
			ArrayList<Position> positions = PositionReader.readPositionFile(invalidFile9);
			// check that invalid application was skipped
			assertEquals(0, positions.size());
		} catch (IllegalArgumentException e) {
			fail(e.getMessage());
		}
		
		// test loading in file with extra item
		try {
			ArrayList<Position> positions = PositionReader.readPositionFile(invalidFile10);
			// check that invalid application was skipped
			assertEquals(0, positions.size());
		} catch (IllegalArgumentException e) {
			fail(e.getMessage());
		}
	}
	
	/**
	 * Test loading file that has valid position with no applications -> should return empty list
	 */
	@Test
	public void testInvalidFileNoApplications() {
		// test loading in a file with no applications
		try {
			ArrayList<Position> positions = PositionReader.readPositionFile(validFile3);
			// check that contents loaded in correctly - positions should be empty, position must have at least one application to be added
			assertEquals(0, positions.size());
		} catch (IllegalArgumentException e) {
			fail(e.getMessage());
		}
	}
	
	/**
	 * Test files that invalid fields given their state (e.g. is in SubmittedState but has a reviewer).
	 * Makes sure that invalid objects are skipped over.
	 */
	@Test
	public void testInvalidFile() {
		// test file with SubmittedState and reviewer
		try {
			ArrayList<Position> positions = PositionReader.readPositionFile(invalidFile11);
			// check that contents loaded in correctly - positions should be empty
			assertEquals(0, positions.size());
		} catch (IllegalArgumentException e) {
			fail(e.getMessage());
		}
		
		// test file with SubmittedState and note
		try {
			ArrayList<Position> positions = PositionReader.readPositionFile(invalidFile12);
			// check that contents loaded in correctly - positions should be empty
			assertEquals(0, positions.size());
		} catch (IllegalArgumentException e) {
			fail(e.getMessage());
		}
		
		// test file with RejectedState and reviewer
		try {
			ArrayList<Position> positions = PositionReader.readPositionFile(invalidFile13);
			// check that contents loaded in correctly - positions should be empty
			assertEquals(0, positions.size());
		} catch (IllegalArgumentException e) {
			fail(e.getMessage());
		}
		
		// test file with RejectedState and missing note
		try {
			ArrayList<Position> positions = PositionReader.readPositionFile(invalidFile14);
			// check that contents loaded in correctly - positions should be empty
			assertEquals(0, positions.size());
		} catch (IllegalArgumentException e) {
			fail(e.getMessage());
		}
		
		// test file with RejectedState and incorrect note
		try {
			ArrayList<Position> positions = PositionReader.readPositionFile(invalidFile15);
			// check that contents loaded in correctly - positions should be empty
			assertEquals(0, positions.size());
		} catch (IllegalArgumentException e) {
			fail(e.getMessage());
		}
		
		// test file with ReviewingState and missing reviewer
		try {
			ArrayList<Position> positions = PositionReader.readPositionFile(invalidFile16);
			// check that contents loaded in correctly - positions should be empty
			assertEquals(0, positions.size());
		} catch (IllegalArgumentException e) {
			fail(e.getMessage());
		}
		
		// test file with ReviewingState and note
		try {
			ArrayList<Position> positions = PositionReader.readPositionFile(invalidFile17);
			// check that contents loaded in correctly - positions should be empty
			assertEquals(0, positions.size());
		} catch (IllegalArgumentException e) {
			fail(e.getMessage());
		}
		
		// test file with InterviewingState and missing reviewer
		try {
			ArrayList<Position> positions = PositionReader.readPositionFile(invalidFile18);
			// check that contents loaded in correctly - positions should be empty
			assertEquals(0, positions.size());
		} catch (IllegalArgumentException e) {
			fail(e.getMessage());
		}
		
		// test file with InterviewingState and note
		try {
			ArrayList<Position> positions = PositionReader.readPositionFile(invalidFile19);
			// check that contents loaded in correctly - positions should be empty
			assertEquals(0, positions.size());
		} catch (IllegalArgumentException e) {
			fail(e.getMessage());
		}
		
		// test file with ProcessingState and missing reviewer
		try {
			ArrayList<Position> positions = PositionReader.readPositionFile(invalidFile20);
			// check that contents loaded in correctly - positions should be empty
			assertEquals(0, positions.size());
		} catch (IllegalArgumentException e) {
			fail(e.getMessage());
		}
		
		// test file with ProcessingState and note
		try {
			ArrayList<Position> positions = PositionReader.readPositionFile(invalidFile21);
			// check that contents loaded in correctly - positions should be empty
			assertEquals(0, positions.size());
		} catch (IllegalArgumentException e) {
			fail(e.getMessage());
		}
		
		// test file with HiredState and missing reviewer
		try {
			ArrayList<Position> positions = PositionReader.readPositionFile(invalidFile22);
			// check that contents loaded in correctly - positions should be empty
			assertEquals(0, positions.size());
		} catch (IllegalArgumentException e) {
			fail(e.getMessage());
		}
		
		// test file with HiredState and note
		try {
			ArrayList<Position> positions = PositionReader.readPositionFile(invalidFile23);
			// check that contents loaded in correctly - positions should be empty
			assertEquals(0, positions.size());
		} catch (IllegalArgumentException e) {
			fail(e.getMessage());
		}
		
		// test file with InactiveState and missing reviewer
		try {
			ArrayList<Position> positions = PositionReader.readPositionFile(invalidFile24);
			// check that contents loaded in correctly - positions should be empty
			assertEquals(0, positions.size());
		} catch (IllegalArgumentException e) {
			fail(e.getMessage());
		}
		
		// test file with InactiveState and missing note
		try {
			ArrayList<Position> positions = PositionReader.readPositionFile(invalidFile25);
			// check that contents loaded in correctly - positions should be empty
			assertEquals(0, positions.size());
		} catch (IllegalArgumentException e) {
			fail(e.getMessage());
		}
		
		// test file with InactiveState and incorrect note
		try {
			ArrayList<Position> positions = PositionReader.readPositionFile(invalidFile26);
			// check that contents loaded in correctly - positions should be empty
			assertEquals(0, positions.size());
		} catch (IllegalArgumentException e) {
			fail(e.getMessage());
		}
	}

}
