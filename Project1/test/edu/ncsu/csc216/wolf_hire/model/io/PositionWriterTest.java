/**
 * 
 */
package edu.ncsu.csc216.wolf_hire.model.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import org.junit.jupiter.api.Test;

import edu.ncsu.csc216.wolf_hire.model.application.Application;
import edu.ncsu.csc216.wolf_hire.model.manager.Position;

/**
 * Tests that PositionWriter class is correctly writing data to the file given by the user.
 * 
 * @author hmreese2
 *
 */
public class PositionWriterTest {
	
	/** Valid file with expected file-printing results for a position with an application in Submitted state **/
	private String expSubmitted = "test-files/expected_submitted.txt";
	/** Valid file with expected file-printing results for a position with an application in Reviewing state **/
	private String expReviewing = "test-files/expected_reviewing.txt";
	/** Valid file with expected file-printing results for a position with an application in Rejected state **/
	private String expRejected = "test-files/expected_rejected.txt";
	/** Valid file with expected file-printing results for a position with an application in Processing state **/
	private String expProcessing = "test-files/expected_processing.txt";
	/** Valid file with expected file-printing results for a position with an application in Interviewing state **/
	private String expInterviewing = "test-files/expected_interviewing.txt";
	/** Valid file with expected file-printing results for a position with an application in Hired state **/
	private String expHired = "test-files/expected_hired.txt";
	/** Valid file with expected file-printing results for a position with an application in Inactive state **/
	private String expInactive = "test-files/expected_inactive.txt";
	/** Valid file with expected file-printing results for a list of 3 positions **/
	private String expPositions = "test-files/expected_positions.txt";
	/** Valid file with expected file-printing results for a list of 2 positions **/
	private String expPositions2 = "test-files/expected_positions2.txt";

	/**
	 * Tests unused constructor, makes sure that return value is non-null.
	 */
	@Test
	public void testConstructor() {
		assertNotNull(new PositionWriter());
	}
	
	/**
	 * Tests writing a file with invalid parameters
	 */
	@Test
	public void testInvalidParameters() {
		// create testable list
		ArrayList<Position> positions = new ArrayList<Position>();
		
		// add testable positions to list
		positions.add(new Position("Position1", 10, 20));
		positions.add(new Position("Position2", 12, 15));
		assertEquals(2, positions.size());
		
		// test with null fileName
		Exception e1 = assertThrows(IllegalArgumentException.class,
				() -> PositionWriter.writePositionsToFile(null, positions));
		assertEquals("Unable to save file.", e1.getMessage());
		
		// test with empty string fileName
		Exception e2 = assertThrows(IllegalArgumentException.class,
				() -> PositionWriter.writePositionsToFile("", positions));
		assertEquals("Unable to save file.", e2.getMessage());
		
		// test with null positions
		Exception e3 = assertThrows(IllegalArgumentException.class,
				() -> PositionWriter.writePositionsToFile("test-files/actual_nullPos.txt", null));
		assertEquals("Unable to save file.", e3.getMessage());
	}
	
	/**
	 * Tests writing a file with an invalid application
	 */
	@Test
	public void testValidParameters() {
		// test adding 1 position with applications in states:
		
		// Submitted
		ArrayList<Position> pos1 = new ArrayList<Position>();
		Position p1 = new Position("Position", 18, 20);
		p1.addApplication(new Application(2, Application.SUBMITTED_NAME, "Carol", "Schmidt", "cschmid", null, null));
		pos1.add(p1);
		// check contents of list
		assertEquals(1, pos1.size());
		assertEquals(1, pos1.get(0).getApplications().size());
		// test writePositionsToFile()
		assertDoesNotThrow(() -> PositionWriter.writePositionsToFile("test-files/actual_submitted", pos1));
		checkFiles(expSubmitted, "test-files/actual_submitted");
		
		// Reviewing
		ArrayList<Position> pos2 = new ArrayList<Position>();
		Position p2 = new Position("Position", 18, 20);
		p2.addApplication(new Application(7, Application.REVIEWING_NAME, "Kerry", "Tucker", "kbtucker", "tmbarnes", null));
		pos2.add(p2);
		assertEquals(1, pos2.size());
		assertDoesNotThrow(() -> PositionWriter.writePositionsToFile("test-files/actual_reviewing", pos2));
		checkFiles(expReviewing, "test-files/actual_reviewing");
		
		// Rejected
		ArrayList<Position> pos3 = new ArrayList<Position>();
		Position p3 = new Position("Position", 18, 20);
		p3.addApplication(new Application(3, Application.REJECTED_NAME, "Kathleen", "Gillespie", "kgilles", null, Application.INCOMPLETE_REJECTION));
		pos3.add(p3);
		assertEquals(1, pos3.size());
		assertDoesNotThrow(() -> PositionWriter.writePositionsToFile("test-files/actual_rejected", pos3));
		checkFiles(expRejected, "test-files/actual_rejected");
		
		// Processing
		ArrayList<Position> pos4 = new ArrayList<Position>();
		Position p4 = new Position("Position", 18, 20);
		p4.addApplication(new Application(11, Application.PROCESSING_NAME, "Quemby", "Mullen", "qmullen", "sesmith5", null));
		pos4.add(p4);
		assertEquals(1, pos4.size());
		assertDoesNotThrow(() -> PositionWriter.writePositionsToFile("test-files/actual_processing", pos4));
		checkFiles(expProcessing, "test-files/actual_processing");
		
		// Interviewing
		ArrayList<Position> pos5 = new ArrayList<Position>();
		Position p5 = new Position("Position", 18, 20);
		p5.addApplication(new Application(8, Application.INTERVIEWING_NAME, "Benjamin", "Nieves", "bmnieves", "sesmith5", null));
		pos5.add(p5);
		assertEquals(1, pos5.size());
		assertDoesNotThrow(() -> PositionWriter.writePositionsToFile("test-files/actual_interviewing", pos5));
		checkFiles(expInterviewing, "test-files/actual_interviewing");
		
		// Hired
		ArrayList<Position> pos6 = new ArrayList<Position>();
		Position p6 = new Position("Position", 18, 20);
		p6.addApplication(new Application(4, Application.HIRED_NAME, "Fiona", "Rosario", "frosari", "sesmith5", null));
		pos6.add(p6);
		assertEquals(1, pos6.size());
		assertDoesNotThrow(() -> PositionWriter.writePositionsToFile("test-files/actual_hired", pos6));
		checkFiles(expHired, "test-files/actual_hired");
		
		// Inactive
		ArrayList<Position> pos7 = new ArrayList<Position>();
		Position p7 = new Position("Position", 18, 20);
		p7.addApplication(new Application(7, Application.INACTIVE_NAME, "Deanna", "Sanders", "dsander", "tmbarnes", Application.COMPLETED_TERMINATION));
		pos7.add(p7);
		assertEquals(1, pos7.size());
		assertDoesNotThrow(() -> PositionWriter.writePositionsToFile("test-files/actual_inactive", pos7));
		checkFiles(expInactive, "test-files/actual_inactive");
	}
	
	/**
	 * Tests writing a file for multiple positions
	 */
	@Test
	public void testValidPositionsMultiple() {
		// test with 3 positions
		ArrayList<Position> pos1 = new ArrayList<Position>();
		Position p1 = new Position("Position A", 18, 20);
		p1.addApplication(new Application(2, Application.SUBMITTED_NAME, "Carol", "Schmidt", "cschmid", null, null));
		p1.addApplication(new Application(4, Application.HIRED_NAME, "Fiona", "Rosario", "frosari", "sesmith5", null));
		Position p2 = new Position("Position B", 10, 12);
		p2.addApplication(new Application(7, Application.INACTIVE_NAME, "Deanna", "Sanders", "dsander", "tmbarnes", Application.COMPLETED_TERMINATION));
		p2.addApplication(new Application(8, Application.INTERVIEWING_NAME, "Benjamin", "Nieves", "bmnieves", "sesmith5", null));
		p2.addApplication(new Application(11, Application.PROCESSING_NAME, "Quemby", "Mullen", "qmullen", "sesmith5", null));
		Position p3 = new Position("Position D", 11, 11);
		p3.addApplication(new Application(3, Application.REJECTED_NAME, "Kathleen", "Gillespie", "kgilles", null, Application.INCOMPLETE_REJECTION));
		pos1.add(p1);
		pos1.add(p2);
		pos1.add(p3);
		assertEquals(3, pos1.size());
		
		assertDoesNotThrow(() -> PositionWriter.writePositionsToFile("test-files/actual_positions", pos1));
		checkFiles(expPositions, "test-files/actual_positions");
		
		// test with 2 positions
		ArrayList<Position> pos2 = new ArrayList<Position>();
		Position p4 = new Position("Position 1", 12, 12);
		p4.addApplication(new Application(1, Application.SUBMITTED_NAME, "Cailin", "Roach", "cvroach", null, null));
		p4.addApplication(new Application(2, Application.SUBMITTED_NAME, "Alika", "Reilly", "areilly", null, null));
		p4.addApplication(new Application(3, Application.SUBMITTED_NAME, "Eagan", "Cardenas", "ecarden", null, null));
		Position p5 = new Position("Position 3", 15, 15);
		p5.addApplication(new Application(1, Application.SUBMITTED_NAME, "Carol", "Schmidt", "cschmid", null, null));
		p5.addApplication(new Application(2, Application.SUBMITTED_NAME, "Fiona", "Rosario", "frosari", null, null));
		pos2.add(p4);
		pos2.add(p5);
		assertEquals(2, pos2.size());
		
		assertDoesNotThrow(() -> PositionWriter.writePositionsToFile("test-files/actual_positions2", pos2));
		checkFiles(expPositions2, "test-files/actual_positions2");
	}
	
	/**
	 * Helper method to compare two files for the same contents
	 * @param expFile expected output
	 * @param actFile actual output
	 * @throws IOException if there is an error reading the passed in files
	 */
	@Test
	private void checkFiles(String expFile, String actFile) {
		try (Scanner expScanner = new Scanner(new File(expFile));
			Scanner actScanner = new Scanner(new File(actFile));) {
				
			while (expScanner.hasNextLine()) {
				assertEquals(expScanner.nextLine(), actScanner.nextLine());
			}
				
			expScanner.close();
			actScanner.close();
		} catch (IOException e) {
			fail("Error reading files.");
		}
	}
	
	/**
	 * TS test created locally for debugging purposes.
	 * Tests adding 4 positions where only 3 of them have at least one application. Method should write only the 
	 * three positions that have applications to given file.
	 */
	@Test
	public void testTS() {
		// Testing where expected results are the file: expPositions - expected_positions.txt
		
		// 1. Create 4 Positions - A, B, C, D
		Position p1 = new Position("Position A", 18, 20);
		Position p2 = new Position("Position B", 10, 12);
		Position p3 = new Position("Position C", 10, 10);
		Position p4 = new Position("Position D", 11, 11);
		// 2. Add applications to the Positions like in the expected output.
		p1.addApplication(new Application(2, "Submitted", "Carol", "Schmidt", "cschmid", null, null));
		p1.addApplication(new Application(4, "Hired", "Fiona", "Rosario", "frosari", "sesmith5", null));
		assertEquals(2, p1.getApplications().size());
		p2.addApplication(new Application(7, "Inactive", "Deanna", "Sanders", "dsander", "tmbarnes", "Completed"));
		p2.addApplication(new Application(8, "Interviewing", "Benjamin", "Nieves", "bmnieves", "sesmith5", null));
		p2.addApplication(new Application(11, "Processing", "Quemby", "Mullen", "qmullen", "sesmith5", null));
		assertEquals(3, p2.getApplications().size());
		// Position C should have no applications.
		assertEquals(0, p3.getApplications().size());
		p4.addApplication(new Application(3, "Rejected", "Kathleen", "Gillespie", "kgilles", null, "Incomplete"));
		assertEquals(1, p4.getApplications().size());
		
		ArrayList<Position> pList = new ArrayList<Position>();
		pList.add(p1);
		pList.add(p2);
		pList.add(p3);
		pList.add(p4);
		assertEquals(4, pList.size());
		// 3. Print to file actual_positions.txt
		assertDoesNotThrow(() -> PositionWriter.writePositionsToFile("test-files/actual.txt", pList));
		// Note that Position C shouldn't be printed bc there are no applications.
		checkFiles(expPositions, "test-files/actual.txt");
		// If this test fails, create a local copy so you can compare the actual output generated
	}
}
