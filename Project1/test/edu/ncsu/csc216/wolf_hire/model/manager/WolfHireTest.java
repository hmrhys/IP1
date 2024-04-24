/**
 * 
 */
package edu.ncsu.csc216.wolf_hire.model.manager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import org.junit.Before;
import org.junit.jupiter.api.Test;

import edu.ncsu.csc216.wolf_hire.model.application.Application;
import edu.ncsu.csc216.wolf_hire.model.command.Command;
import edu.ncsu.csc216.wolf_hire.model.command.Command.CommandValue;

/**
 * Tests that WolfHire is correctly maintaining Position lists and handling GUI commands.
 * 
 * @author hmreese2
 *
 */
public class WolfHireTest {
	
	/** Represents single instance of WolfHire used for testing purposes */
	private WolfHire instance = WolfHire.getInstance();
	/** Valid file with one position in it to be read */
	private String positions1 = "test-files/positions1.txt";
	/** Valid file with multiple (4) positions in it to be read */
	private String positions2 = "test-files/positions2.txt";
	/** Expected file results for file that is written with PositionWriter */
	private String expPositions2 = "test-files/expected_positions2.txt";

	/**
	 * Helper method that resets the ID counter each time so that there are no issues when checking UserStory ID
	 * @throws Exception if there is an issue setting the counter for UserStory
	 */
	@Before
	public void setUp() throws Exception {
		//Reset the counter at the beginning of every test.
		Application.setCounter(0);
	}
	
	/**
	 * Test resetManager() to make sure it is working properly for testing purposes
	 */
	@Test
	public void testResetManager() {
		assertEquals(0, instance.getPositionList().length);
		assertNull(instance.getApplicationsAsArray("All"));
		
		instance.addNewPosition("P1", 12, 15);
		instance.addNewPosition("P2", 10, 10);
		instance.addNewPosition("P3", 12, 12);
		assertEquals(3, instance.getPositionList().length);
		
		instance.resetManager();
		assertEquals(0, instance.getPositionList().length);
		assertNull(instance.getApplicationsAsArray("All"));
	}
	
	/**
	 * Tests adding a new position with invalid parameter values (null and empty string positionNames).
	 */
	@Test
	public void testAddNewPositionInvalid() {
		// test adding with null positionName
		Exception e1 = assertThrows(IllegalArgumentException.class,
				() -> instance.addNewPosition(null, 12, 15));
		assertEquals("Position cannot be created.", e1.getMessage());
		
		// test adding with empty string positionName
		Exception e2 = assertThrows(IllegalArgumentException.class,
				() -> instance.addNewPosition("", 12, 15));
		assertEquals("Position cannot be created.", e2.getMessage());
		
		// reset manager for testing purposes
		instance.resetManager();
	}
	
	/**
	 * Tests addNewPosition(String positionName) with valid parameter values.
	 * Tests addNewPosition with invalid duplicate positionName value.
	 */
	@Test
	public void testAddNewPosition() {
		// test adding one position
		assertDoesNotThrow(() -> instance.addNewPosition("Position1", 12, 15));
		assertEquals(1, instance.getPositionList().length);
		assertEquals("Position1", instance.getActivePositionName());
		assertEquals(12, instance.getActivePosition().getHoursPerWeek());
		assertEquals(15, instance.getActivePosition().getPayRate());
		
		// test adding two positions 
		assertDoesNotThrow(() -> instance.addNewPosition("Position2", 12, 15));
		assertEquals(2, instance.getPositionList().length);
		assertEquals("Position2", instance.getActivePositionName());
		assertEquals(12, instance.getActivePosition().getHoursPerWeek());
		assertEquals(15, instance.getActivePosition().getPayRate());
		
		// test adding duplicate position
		Exception e = assertThrows(IllegalArgumentException.class,
				() -> instance.addNewPosition("Position1", 11, 12));
		assertEquals("Position cannot be created.", e.getMessage());
		
		// check that list hasn't changed
		assertEquals(2, instance.getPositionList().length);
		assertEquals("Position2", instance.getActivePositionName());
		assertEquals(12, instance.getActivePosition().getHoursPerWeek());
		assertEquals(15, instance.getActivePosition().getPayRate());
		
		// reset manager for testing purposes
		instance.resetManager();
	}
	
	/**
	 * Tests loading a position that doesn't exist (should throw IAE) and loading a position that does exist (primary route)
	 */
	@Test
	public void testLoadPosition() {
		// reset manager for testing purposes
		instance.resetManager();
		
		// test loading position when list is empty
		Exception e1 = assertThrows(IllegalArgumentException.class,
				() -> instance.loadPosition("Position"));
		assertEquals("Position not available.", e1.getMessage());
		
		// test loading position with empty string for parameter
		Exception e2 = assertThrows(IllegalArgumentException.class,
				() -> instance.loadPosition(""));
		assertEquals("Position not available.", e2.getMessage());
		
		// test loading position with null for parameter
		Exception e3 = assertThrows(IllegalArgumentException.class,
				() -> instance.loadPosition(null));
		assertEquals("Position not available.", e3.getMessage());
		
		// test loading position when positions is null
		assertNull(instance.getActivePosition());
		assertNull(instance.getApplicationsAsArray("All"));
		instance.loadPositionsFromFile(positions2);
		assertEquals("CSC 116 Grader", instance.getActivePositionName());
		assertEquals(7, instance.getApplicationsAsArray("All").length);
		
		// reset manager for further testing
		instance.resetManager();
		
		// add some testable positions to instance list
		instance.addNewPosition("Position", 12, 15);
		instance.addNewPosition("Position2", 10, 12);
		instance.addNewPosition("Position3", 10, 15);
		// check they've been added
		assertEquals(3, instance.getPositionList().length);
		assertEquals("Position3", instance.getActivePositionName());
		assertEquals("Position3", instance.getActivePosition().getPositionName());
		assertEquals(10, instance.getActivePosition().getHoursPerWeek());
		assertEquals(15, instance.getActivePosition().getPayRate());
		
		// test loading position that doesn't exist by name
		Exception e4 = assertThrows(IllegalArgumentException.class,
				() -> instance.loadPosition("Position1"));
		assertEquals("Position not available.", e4.getMessage());
		assertEquals(3, instance.getPositionList().length);
		assertEquals("Position3", instance.getActivePositionName());
		assertEquals("Position3", instance.getActivePosition().getPositionName());
		assertEquals(10, instance.getActivePosition().getHoursPerWeek());
		assertEquals(15, instance.getActivePosition().getPayRate());
		
		// test loading valid position
		assertDoesNotThrow(() -> instance.loadPosition("Position"));
		// check that position was made active
		assertEquals("Position", instance.getActivePositionName());
		assertEquals("Position", instance.getActivePosition().getPositionName());
		assertEquals(12, instance.getActivePosition().getHoursPerWeek());
		assertEquals(15, instance.getActivePosition().getPayRate());
		// check that application id was set properly
		assertEquals(1, instance.getActivePosition().addApplication("James", "Wallabie", "jwalla"));
		assertEquals(2, instance.getActivePosition().addApplication("Eleanor", "Rigby", "elrigby"));
		assertEquals(3, instance.getActivePosition().addApplication("Abby Lee", "Miller", "aldc"));
		
		// test loading another valid position
		assertDoesNotThrow(() -> instance.loadPosition("Position2"));
		// check that position was made active
		assertEquals("Position2", instance.getActivePositionName());
		assertEquals("Position2", instance.getActivePosition().getPositionName());
		assertEquals(10, instance.getActivePosition().getHoursPerWeek());
		assertEquals(12, instance.getActivePosition().getPayRate());
		// check that application id was set properly
		assertEquals(1, instance.getActivePosition().addApplication("Spongebob", "Squarepants", "sbsqrp"));
		assertEquals(2, instance.getActivePosition().addApplication("Michelle", "MyBell", "mmybell"));
		assertEquals(3, instance.getActivePosition().addApplication("Rocky", "Raccoon", "roracco"));
		
		// reset manager for testing purposes
		instance.resetManager();
	}
	
	/**
	 * Tests getting active position when there is none (==null) and when there is (==activePosition).
	 */
	@Test
	public void testGetActivePosition() {		
		// test when there is no active position
		assertNull(instance.getActivePosition());
		
		// add position to make active - test when active position does exist
		instance.addNewPosition("Position", 12, 14);
		assertEquals("Position", instance.getActivePosition().getPositionName());
		assertEquals(12, instance.getActivePosition().getHoursPerWeek());
		assertEquals(14, instance.getActivePosition().getPayRate());		
		// test adding another position
		instance.addNewPosition("P2", 10, 10);
		assertEquals("P2", instance.getActivePosition().getPositionName());
		assertEquals(10, instance.getActivePosition().getHoursPerWeek());
		assertEquals(10, instance.getActivePosition().getPayRate());
	
		// reset manager for testing purposes
		instance.resetManager();
	}
	
	/**
	 * Tests getting the correct name back of the current active position.
	 * Tests also using this method when there is no active position.
	 */
	@Test
	public void testGetActivePositionName() {
		// test when there is no active position
		assertNull(instance.getActivePositionName());
		
		// add position to make active - test when active position does exist
		instance.addNewPosition("P1", 12, 12);
		assertEquals("P1", instance.getActivePositionName());
		// test adding another position
		instance.addNewPosition("P2", 12, 14);
		assertEquals("P2", instance.getActivePositionName());
		
		// reset manager for testing purposes
		instance.resetManager();
	}
	
	/**
	 * Tests getPositionList() when no positions are added (list is empty) and when list is not empty.
	 */
	@Test
	public void testGetPositionList() {
		// reset manager for testing purposes
		instance.resetManager();
		
		// test when list empty - no positions have been added
		assertEquals(0, instance.getPositionList().length);
		
		// test list after adding some positions
		instance.addNewPosition("p1", 12, 12);
		instance.addNewPosition("p2", 12, 12);
		instance.addNewPosition("p3", 12, 12);
		assertEquals(3, instance.getPositionList().length);
		assertEquals("p1", instance.getPositionList()[0]);
		assertEquals("p2", instance.getPositionList()[1]);
		assertEquals("p3", instance.getPositionList()[2]);
		
		// reset manager for testing purposes
		instance.resetManager();
	}
	
	/**
	 * Tests loadPositionFromFile() method with file that has one position
	 */
	@Test
	public void testLoadOnePositionFromFile() {
		// reset manager for testing purposes
		instance.resetManager();
		
		try {
			
			instance.loadPositionsFromFile(positions1);
			assertEquals(1, instance.getPositionList().length);
			assertEquals("CSC 216 PTF", instance.getActivePositionName());
			assertEquals(6, instance.getApplicationsAsArray("All").length);
			
		} catch (Exception e) {
			fail(e.getMessage());
		}
		
		// reset manager for testing purposes
		instance.resetManager();
	}
	
	/**
	 * Tests loadPositionsFromFile() method with file that has multiple (four) positions in it
	 */
	@Test
	public void testLoadMultiplePositionsFromFile() {
		// reset manager for testing purposes
		instance.resetManager();
		
		try {
			
			instance.loadPositionsFromFile(positions2);
			assertEquals(4, instance.getPositionList().length);
			assertEquals("CSC 116 Grader", instance.getActivePositionName());
			assertEquals("CSC 116 Grader", instance.getActivePosition().getPositionName());
			assertEquals(10, instance.getActivePosition().getHoursPerWeek());
			assertEquals(11, instance.getActivePosition().getPayRate());
			
		} catch (Exception e) {
			fail(e.getMessage());
		}
		
		// reset manager for testing purposes
		instance.resetManager();
	}
	
	/**
	 * Tests saving positions to a file, works with PositionWriter class.
	 */
	@Test
	public void testSavePositionsToFile() {
		// reset manager for testing purposes
		instance.resetManager();
		
		// test saving positions when there is no activePosition
		assertNull(instance.getActivePosition());
		Exception e = assertThrows(IllegalArgumentException.class,
				() -> instance.savePositionsToFile("test-files/actual_positions2.txt"));
		assertEquals("Unable to save file.", e.getMessage());
		
		// test with valid parameters - 2 positions and their respective applications
		//instance..setCounter(1);
		instance.addNewPosition("Position 1", 12, 12);
		instance.addApplicationToPosition("Cailin", "Roach", "cvroach");
		instance.addApplicationToPosition("Alika", "Reilly", "areilly");
		instance.addApplicationToPosition("Eagan", "Cardenas", "ecarden");
		assertEquals(3, instance.getApplicationsAsArray("All").length);
		instance.addNewPosition("Position 3", 15, 15);
		instance.addApplicationToPosition("Carol", "Schmidt", "cschmid");
		instance.addApplicationToPosition("Fiona", "Rosario", "frosari");
		assertEquals(2, instance.getApplicationsAsArray("All").length);
		assertDoesNotThrow(() -> instance.savePositionsToFile("test-files/actual_positions2.txt"));
		checkFiles(expPositions2, "test-files/actual_positions2.txt");
		
		// reset manager for testing purposes
		instance.resetManager();
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
	 * Tests getApplicationsAsArray() first with all *State options, then with All option.
	 */
	@Test
	public void testGetApplicationsAsArray() {
		// check if activePosition is null! Returned list should be null!
		assertNull(instance.getApplicationsAsArray("All"));
		
		// load file so that you have positions and applications to work with
		instance.loadPositionsFromFile(positions1);
		
		// make expected list for Submitted filter
		String[][] expSubmitted = new String[1][4];
		expSubmitted[0][0] = 2 + "";
		expSubmitted[0][1] = "Submitted";
		expSubmitted[0][2] = "cschmid";
		expSubmitted[0][3] = "";
		// make expected list for Rejected filter
		String[][] expRejected = new String[1][4];
		expRejected[0][0] = 3 + "";
		expRejected[0][1] = "Rejected";
		expRejected[0][2] = "kgilles";
		expRejected[0][3] = "";
		// make expected list for Interviewing filter
		String[][] expInterviewing = new String[1][4];
		expInterviewing[0][0] = 8 + "";
		expInterviewing[0][1] = "Interviewing";
		expInterviewing[0][2] = "bmnieves";
		expInterviewing[0][3] = "sesmith5";
		// make expected list for Processing filter
		String[][] expProcessing = new String[1][4];
		expProcessing[0][0] = 11 + "";
		expProcessing[0][1] = "Processing";
		expProcessing[0][2] = "qmullen";
		expProcessing[0][3] = "sesmith5";
		// make expected list for Hired filter
		String[][] expHired = new String[1][4];
		expHired[0][0] = 4 + "";
		expHired[0][1] = "Hired";
		expHired[0][2] = "frosari";
		expHired[0][3] = "sesmith5";
		// make expected list for Inactive filter
		String[][] expInactive = new String[1][4];
		expInactive[0][0] = 7 + "";
		expInactive[0][1] = "Inactive";
		expInactive[0][2] = "dsander";
		expInactive[0][3] = "sesmith5";
		// make expected list for All filter
		String[][] expAll = new String[6][4];
		expAll[0][0] = 2 + "";
		expAll[0][1] = "Submitted";
		expAll[0][2] = "cschmid";
		expAll[0][3] = "";
		expAll[1][0] = 3 + "";
		expAll[1][1] = "Rejected";
		expAll[1][2] = "kgilles";
		expAll[1][3] = "";
		expAll[2][0] = 4 + "";
		expAll[2][1] = "Hired";
		expAll[2][2] = "frosari";
		expAll[2][3] = "sesmith5";
		expAll[3][0] = 7 + "";
		expAll[3][1] = "Inactive";
		expAll[3][2] = "dsander";
		expAll[3][3] = "sesmith5";
		expAll[4][0] = 8 + "";
		expAll[4][1] = "Interviewing";
		expAll[4][2] = "bmnieves";
		expAll[4][3] = "sesmith5";
		expAll[5][0] = 11 + "";
		expAll[5][1] = "Processing";
		expAll[5][2] = "qmullen";
		expAll[5][3] = "sesmith5";
		
		assertEquals("CSC 216 PTF", instance.getActivePositionName());
		assertEquals(6, instance.getActivePosition().getApplications().size());
		
		// test when filter is All
		String[][] actAll = instance.getApplicationsAsArray("All");
		assertEquals(6, instance.getApplicationsAsArray("All").length);
		assertEquals(expAll[0][0], actAll[0][0]);
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 4; j++) {
				assertEquals(expAll[i][j], actAll[i][j]);
			}
		}
		assertEquals(expAll.length, actAll.length);
		
		// test when filter is Submitted state
		String[][] actSubmitted = instance.getApplicationsAsArray("Submitted");
		assertEquals(expSubmitted.length, actSubmitted.length);
		for (int i = 0; i < 1; i++) {
			for (int j = 0; j < 4; j++) {
				assertEquals(expSubmitted[i][j], actSubmitted[i][j]);
			}
		}
		
		// test when filter is Rejected state
		String[][] actRejected = instance.getApplicationsAsArray("Rejected");
		
		assertEquals(expRejected.length, actRejected.length);
		for (int i = 0; i < 1; i++) {
			for (int j = 0; j < 4; j++) {
				assertEquals(expRejected[i][j], actRejected[i][j]);
			}
		}
		
		// test when filter is Reviewing state
		assertEquals(0, instance.getApplicationsAsArray("Reviewing").length);
		
		// test when filter is Interviewing state
		String[][] actInterviewing = instance.getApplicationsAsArray("Interviewing");
		assertEquals(expInterviewing.length, actInterviewing.length);
		for (int i = 0; i < 1; i++) {
			for (int j = 0; j < 4; j++) {
				assertEquals(expInterviewing[i][j], actInterviewing[i][j]);
			}
		}
		
		// test when filter is Processing state
		String[][] actProcessing = instance.getApplicationsAsArray("Processing");
		assertEquals(expProcessing.length, actProcessing.length);
		for (int i = 0; i < 1; i++) {
			for (int j = 0; j < 4; j++) {
				assertEquals(expProcessing[i][j], actProcessing[i][j]);
			}
		}
		
		// test when filter is Hired state
		String[][] actHired = instance.getApplicationsAsArray("Hired");
		assertEquals(expHired.length, actHired.length);
		for (int i = 0; i < 1; i++) {
			for (int j = 0; j < 4; j++) {
				assertEquals(expHired[i][j], actHired[i][j]);
			}
		}
		
		// test when filter is Inactive state
		String[][] actInactive = instance.getApplicationsAsArray("Inactive");
		assertEquals(expInactive.length, actInactive.length);
		for (int i = 0; i < 1; i++) {
			for (int j = 0; j < 4; j++) {
				assertEquals(expInactive[i][j], actInactive[i][j]);
			}
		}
		
		// reset manager for testing purposes
		instance.resetManager();
	}
	
	/**
	 * Tests getting application by id from activePosition, should be null if there is no activePosition
	 */
	@Test
	public void testGetApplicationById() {
		// test with no activePosition
		assertNull(instance.getActivePosition());
		assertNull(instance.getApplicationById(1));
		
		String[][] expected = new String[6][4];
		expected[0][0] = 2 + "";
		expected[0][1] = "Submitted";
		expected[0][2] = "cschmid";
		expected[0][3] = "";
		expected[1][0] = 3 + "";
		expected[1][1] = "Rejected";
		expected[1][2] = "kgilles";
		expected[1][3] = "";
		expected[2][0] = 4 + "";
		expected[2][1] = "Hired";
		expected[2][2] = "frosari";
		expected[2][3] = "sesmith5";
		expected[3][0] = 7 + "";
		expected[3][1] = "Inactive";
		expected[3][2] = "dsander";
		expected[3][3] = "sesmith5";
		expected[4][0] = 8 + "";
		expected[4][1] = "Interviewing";
		expected[4][2] = "bmnieves";
		expected[4][3] = "sesmith5";
		expected[5][0] = 11 + "";
		expected[5][1] = "Processing";
		expected[5][2] = "qmullen";
		expected[5][3] = "sesmith5";
		// test after loading in some applications
		instance.loadPositionsFromFile(positions1);
		assertEquals("CSC 216 PTF", instance.getActivePositionName());
		
		assertEquals(6, instance.getActivePosition().getApplications().size());
		assertEquals(6, instance.getApplicationsAsArray("All").length);
		assertEquals("cschmid", instance.getApplicationById(2).getUnityId());
		assertEquals(expected[0][0], instance.getApplicationsAsArray("All")[0][0]);
		assertEquals(expected[0][1], instance.getApplicationsAsArray("All")[0][1]);
		assertEquals(expected[0][2], instance.getApplicationsAsArray("All")[0][2]);
		assertEquals(expected[0][3], instance.getApplicationsAsArray("All")[0][3]);
		assertEquals("kgilles", instance.getApplicationById(3).getUnityId());
		assertEquals(expected[1][0], instance.getApplicationsAsArray("All")[1][0]);
		assertEquals(expected[1][1], instance.getApplicationsAsArray("All")[1][1]);
		assertEquals(expected[1][2], instance.getApplicationsAsArray("All")[1][2]);
		assertEquals(expected[1][3], instance.getApplicationsAsArray("All")[1][3]);
		assertEquals("frosari", instance.getApplicationById(4).getUnityId());
		assertEquals(expected[2][0], instance.getApplicationsAsArray("All")[2][0]);
		assertEquals(expected[2][1], instance.getApplicationsAsArray("All")[2][1]);
		assertEquals(expected[2][2], instance.getApplicationsAsArray("All")[2][2]);
		assertEquals(expected[2][3], instance.getApplicationsAsArray("All")[2][3]);
		assertEquals("dsander", instance.getApplicationById(7).getUnityId());		
		assertEquals(expected[3][0], instance.getApplicationsAsArray("All")[3][0]);
		assertEquals(expected[3][1], instance.getApplicationsAsArray("All")[3][1]);
		assertEquals(expected[3][2], instance.getApplicationsAsArray("All")[3][2]);
		assertEquals(expected[3][3], instance.getApplicationsAsArray("All")[3][3]);
		assertEquals("bmnieves", instance.getApplicationById(8).getUnityId());
		assertEquals(expected[4][0], instance.getApplicationsAsArray("All")[4][0]);
		assertEquals(expected[4][1], instance.getApplicationsAsArray("All")[4][1]);
		assertEquals(expected[4][2], instance.getApplicationsAsArray("All")[4][2]);
		assertEquals(expected[4][3], instance.getApplicationsAsArray("All")[4][3]);
		assertEquals("qmullen", instance.getApplicationById(11).getUnityId());
		assertEquals(expected[5][0], instance.getApplicationsAsArray("All")[5][0]);
		assertEquals(expected[5][1], instance.getApplicationsAsArray("All")[5][1]);
		assertEquals(expected[5][2], instance.getApplicationsAsArray("All")[5][2]);
		assertEquals(expected[5][3], instance.getApplicationsAsArray("All")[5][3]);
		
		// test getting application with id that doesn't exist in activePosition list
		assertNull(instance.getApplicationById(5));
		
		// reset manager for testing purposes
		instance.resetManager();
	}
	
	/**
	 * Test executeCommand(), first testing for no activePosition
	 */
	@Test
	public void testExecuteCommand() {
		// provide list of easily-accessible commands
		Command assign = new Command(CommandValue.ASSIGN, "reviewer");
		Command assign2 = new Command(CommandValue.ASSIGN, "reviewer2");
		Command reject = new Command(CommandValue.REJECT, Application.DUPLICATE_REJECTION);
		Command resubmit = new Command(CommandValue.RESUBMIT, null);
		Command cmdReturn = new Command(CommandValue.RETURN, null);
		Command schedule = new Command(CommandValue.SCHEDULE, null);
		Command process = new Command(CommandValue.PROCESS, null);
		Command hire = new Command(CommandValue.HIRE, null);
		Command terminate = new Command(CommandValue.TERMINATE, Application.FIRED_TERMINATION);
		
		// test executing command when there is no activePosition
		assertNull(instance.getActivePosition());
		assertNull(instance.getApplicationsAsArray("All"));
		instance.executeCommand(1, reject);
		assertNull(instance.getApplicationsAsArray("All"));
		assertNull(instance.getApplicationsAsArray("Reject"));
		
		// add application to test with
		instance.addNewPosition("Position", 12, 15);
		assertEquals("Position", instance.getActivePositionName());
		instance.addApplicationToPosition("Paul", "McCartney", "pmccart");
		assertEquals(1, instance.getActivePosition().getApplications().size());
		assertEquals(1, instance.getApplicationsAsArray("All").length);
		assertEquals(1, instance.getActivePosition().getApplications().get(0).getId());
		assertEquals("Submitted", instance.getApplicationById(1).getState()); // make sure you are starting in Submitted
		
		// execute SUBMITTED to REJECTED
		assertDoesNotThrow(() -> instance.executeCommand(1, reject));
		assertEquals("Rejected", instance.getApplicationById(1).getState());

		// execute REJECTED to SUBMITTED
		assertDoesNotThrow(() -> instance.executeCommand(1, resubmit));
		assertEquals("Submitted", instance.getApplicationById(1).getState());
		
		// execute SUBMITTED to REVIEWING
		assertDoesNotThrow(() -> instance.executeCommand(1, assign));
		assertEquals("Reviewing", instance.getApplicationById(1).getState());
		
		// execute REVIEWING to SUBMITTED
		assertDoesNotThrow(() -> instance.executeCommand(1, cmdReturn));
		assertEquals("Submitted", instance.getApplicationById(1).getState());
		// go back to REVIEWING
		assertDoesNotThrow(() -> instance.executeCommand(1, assign));
		assertEquals("Reviewing", instance.getApplicationById(1).getState());
		
		// execute REVIEWING to REJECTED
		assertDoesNotThrow(() -> instance.executeCommand(1, reject));
		assertEquals("Rejected", instance.getApplicationById(1).getState());
		// go back to REVIEWING
		assertDoesNotThrow(() -> instance.executeCommand(1, resubmit));
		assertDoesNotThrow(() -> instance.executeCommand(1, assign));
		assertEquals("Reviewing", instance.getApplicationById(1).getState());
		
		// execute REVIEWING TO INTERVIEWING
		assertDoesNotThrow(() -> instance.executeCommand(1, schedule));
		assertEquals("Interviewing", instance.getApplicationById(1).getState());
		
		// test INTERVIEWING to REJECTED
		assertDoesNotThrow(() -> instance.executeCommand(1, reject));
		assertEquals("Rejected", instance.getApplicationById(1).getState());
		// go back to INTERVIEWING
		assertDoesNotThrow(() -> instance.executeCommand(1, resubmit));
		assertDoesNotThrow(() -> instance.executeCommand(1, assign));
		assertDoesNotThrow(() -> instance.executeCommand(1, schedule));
		assertEquals("Interviewing", instance.getApplicationById(1).getState());
		
		// test INTERVIEWING to REVIEWING
		assertDoesNotThrow(() -> instance.executeCommand(1, assign2));
		assertEquals("Reviewing", instance.getApplicationById(1).getState());
		// go back to INTERVIEWING
		assertDoesNotThrow(() -> instance.executeCommand(1, schedule));
		assertEquals("Interviewing", instance.getApplicationById(1).getState());
		
		// execute INTERVIEWING to INTERVIEWING
		assertDoesNotThrow(() -> instance.executeCommand(1, schedule));
		assertEquals("Interviewing", instance.getApplicationById(1).getState());
		
		// execute INTERVIEWING to PROCESSING
		assertDoesNotThrow(() -> instance.executeCommand(1, process));
		assertEquals("Processing", instance.getApplicationById(1).getState());

		// execute PROCESSING to REJECTED
		assertDoesNotThrow(() -> instance.executeCommand(1, reject));
		assertEquals("Rejected", instance.getApplicationById(1).getState());
		// go back to PROCESSING
		assertDoesNotThrow(() -> instance.executeCommand(1, resubmit));
		assertDoesNotThrow(() -> instance.executeCommand(1, assign));
		assertDoesNotThrow(() -> instance.executeCommand(1, schedule));
		assertDoesNotThrow(() -> instance.executeCommand(1, process));
		assertEquals("Processing", instance.getApplicationById(1).getState());
		
		// execute PROCESSING to HIRED
		assertDoesNotThrow(() -> instance.executeCommand(1, hire));
		assertEquals("Hired", instance.getApplicationById(1).getState());
	
		// execute HIRED to INACTIVE
		assertDoesNotThrow(() -> instance.executeCommand(1, terminate));
		assertEquals("Inactive", instance.getApplicationById(1).getState());
	
		// test when active position does not have an application whose id matches the one given
		Exception e44 = assertThrows(IllegalArgumentException.class,
				() -> instance.executeCommand(2, reject));
		assertEquals("Invalid information.", e44.getMessage());
		
		// reset manager for testing purposes
		instance.resetManager();
	}
	
	/**
	 * Tests deleting application by id, first tests if there is no active position
	 */
	@Test
	public void testDeleteApplicationById() {
		// applications used for testing
		String[][] expected = new String[6][4];
		expected[0][0] = 2 + "";
		expected[0][1] = "Submitted";
		expected[0][2] = "cschmid";
		expected[0][3] = "";
		expected[1][0] = 3 + "";
		expected[1][1] = "Rejected";
		expected[1][2] = "kgilles";
		expected[1][3] = "";
		expected[2][0] = 4 + "";
		expected[2][1] = "Hired";
		expected[2][2] = "frosari";
		expected[2][3] = "sesmith5";
		expected[3][0] = 7 + "";
		expected[3][1] = "Inactive";
		expected[3][2] = "dsander";
		expected[3][3] = "sesmith5";
		expected[4][0] = 8 + "";
		expected[4][1] = "Interviewing";
		expected[4][2] = "bmnieves";
		expected[4][3] = "sesmith5";
		expected[5][0] = 11 + "";
		expected[5][1] = "Processing";
		expected[5][2] = "qmullen";
		expected[5][3] = "sesmith5";
	
		// test when there is no activePosition
		assertNull(instance.getActivePosition());
		assertNull(instance.getApplicationsAsArray("All"));
		instance.deleteApplicationById(1);
		assertNull(instance.getApplicationsAsArray("All"));
		
		// add position and applications to test
		instance.loadPositionsFromFile("test-files/positions2.txt");
		assertEquals(4, instance.getPositionList().length);
		assertEquals("CSC 116 Grader", instance.getActivePositionName());
		instance.loadPosition("CSC 216 PTF");
		assertEquals("CSC 216 PTF", instance.getActivePositionName());
		assertEquals(6, instance.getActivePosition().getApplications().size());
		assertEquals(6, instance.getApplicationsAsArray("All").length);
		// check added applications contents
		assertEquals(6, instance.getActivePosition().getApplications().size());
		assertEquals(6, instance.getApplicationsAsArray("All").length);
		assertEquals("cschmid", instance.getApplicationById(2).getUnityId());
		assertEquals(expected[0][0], instance.getApplicationsAsArray("All")[0][0]);
		assertEquals(expected[0][1], instance.getApplicationsAsArray("All")[0][1]);
		assertEquals(expected[0][2], instance.getApplicationsAsArray("All")[0][2]);
		assertEquals(expected[0][3], instance.getApplicationsAsArray("All")[0][3]);
		assertEquals("kgilles", instance.getApplicationById(3).getUnityId());
		assertEquals(expected[1][0], instance.getApplicationsAsArray("All")[1][0]);
		assertEquals(expected[1][1], instance.getApplicationsAsArray("All")[1][1]);
		assertEquals(expected[1][2], instance.getApplicationsAsArray("All")[1][2]);
		assertEquals(expected[1][3], instance.getApplicationsAsArray("All")[1][3]);
		assertEquals("frosari", instance.getApplicationById(4).getUnityId());
		assertEquals(expected[2][0], instance.getApplicationsAsArray("All")[2][0]);
		assertEquals(expected[2][1], instance.getApplicationsAsArray("All")[2][1]);
		assertEquals(expected[2][2], instance.getApplicationsAsArray("All")[2][2]);
		assertEquals(expected[2][3], instance.getApplicationsAsArray("All")[2][3]);
		assertEquals("dsander", instance.getApplicationById(7).getUnityId());		
		assertEquals(expected[3][0], instance.getApplicationsAsArray("All")[3][0]);
		assertEquals(expected[3][1], instance.getApplicationsAsArray("All")[3][1]);
		assertEquals(expected[3][2], instance.getApplicationsAsArray("All")[3][2]);
		assertEquals(expected[3][3], instance.getApplicationsAsArray("All")[3][3]);
		assertEquals("bmnieves", instance.getApplicationById(8).getUnityId());
		assertEquals(expected[4][0], instance.getApplicationsAsArray("All")[4][0]);
		assertEquals(expected[4][1], instance.getApplicationsAsArray("All")[4][1]);
		assertEquals(expected[4][2], instance.getApplicationsAsArray("All")[4][2]);
		assertEquals(expected[4][3], instance.getApplicationsAsArray("All")[4][3]);
		assertEquals("qmullen", instance.getApplicationById(11).getUnityId());
		assertEquals(expected[5][0], instance.getApplicationsAsArray("All")[5][0]);
		assertEquals(expected[5][1], instance.getApplicationsAsArray("All")[5][1]);
		assertEquals(expected[5][2], instance.getApplicationsAsArray("All")[5][2]);
		assertEquals(expected[5][3], instance.getApplicationsAsArray("All")[5][3]);
		
		// test deleting one application
		instance.deleteApplicationById(2);
		assertEquals(5, instance.getActivePosition().getApplications().size());
		assertEquals(5, instance.getApplicationsAsArray("All").length);
		assertNull(instance.getApplicationById(2));
		assertEquals("kgilles", instance.getApplicationById(3).getUnityId());
		assertEquals(expected[1][0], instance.getApplicationsAsArray("All")[0][0]);
		assertEquals(expected[1][1], instance.getApplicationsAsArray("All")[0][1]);
		assertEquals(expected[1][2], instance.getApplicationsAsArray("All")[0][2]);
		assertEquals(expected[1][3], instance.getApplicationsAsArray("All")[0][3]);
		assertEquals("frosari", instance.getApplicationById(4).getUnityId());
		assertEquals(expected[2][0], instance.getApplicationsAsArray("All")[1][0]);
		assertEquals(expected[2][1], instance.getApplicationsAsArray("All")[1][1]);
		assertEquals(expected[2][2], instance.getApplicationsAsArray("All")[1][2]);
		assertEquals(expected[2][3], instance.getApplicationsAsArray("All")[1][3]);
		assertEquals("dsander", instance.getApplicationById(7).getUnityId());		
		assertEquals(expected[3][0], instance.getApplicationsAsArray("All")[2][0]);
		assertEquals(expected[3][1], instance.getApplicationsAsArray("All")[2][1]);
		assertEquals(expected[3][2], instance.getApplicationsAsArray("All")[2][2]);
		assertEquals(expected[3][3], instance.getApplicationsAsArray("All")[2][3]);
		assertEquals("bmnieves", instance.getApplicationById(8).getUnityId());
		assertEquals(expected[4][0], instance.getApplicationsAsArray("All")[3][0]);
		assertEquals(expected[4][1], instance.getApplicationsAsArray("All")[3][1]);
		assertEquals(expected[4][2], instance.getApplicationsAsArray("All")[3][2]);
		assertEquals(expected[4][3], instance.getApplicationsAsArray("All")[3][3]);
		assertEquals("qmullen", instance.getApplicationById(11).getUnityId());
		assertEquals(expected[5][0], instance.getApplicationsAsArray("All")[4][0]);
		assertEquals(expected[5][1], instance.getApplicationsAsArray("All")[4][1]);
		assertEquals(expected[5][2], instance.getApplicationsAsArray("All")[4][2]);
		assertEquals(expected[5][3], instance.getApplicationsAsArray("All")[4][3]);
		
		// test deleting multiple applications
		instance.deleteApplicationById(7); // from middle
		assertEquals(4, instance.getActivePosition().getApplications().size());
		assertEquals(4, instance.getApplicationsAsArray("All").length);
		assertNull(instance.getApplicationById(2));
		assertEquals("kgilles", instance.getApplicationById(3).getUnityId());
		assertEquals(expected[1][0], instance.getApplicationsAsArray("All")[0][0]);
		assertEquals(expected[1][1], instance.getApplicationsAsArray("All")[0][1]);
		assertEquals(expected[1][2], instance.getApplicationsAsArray("All")[0][2]);
		assertEquals(expected[1][3], instance.getApplicationsAsArray("All")[0][3]);
		assertEquals("frosari", instance.getApplicationById(4).getUnityId());
		assertEquals(expected[2][0], instance.getApplicationsAsArray("All")[1][0]);
		assertEquals(expected[2][1], instance.getApplicationsAsArray("All")[1][1]);
		assertEquals(expected[2][2], instance.getApplicationsAsArray("All")[1][2]);
		assertEquals(expected[2][3], instance.getApplicationsAsArray("All")[1][3]);
		assertNull(instance.getApplicationById(7));
		assertEquals("bmnieves", instance.getApplicationById(8).getUnityId());
		assertEquals(expected[4][0], instance.getApplicationsAsArray("All")[2][0]);
		assertEquals(expected[4][1], instance.getApplicationsAsArray("All")[2][1]);
		assertEquals(expected[4][2], instance.getApplicationsAsArray("All")[2][2]);
		assertEquals(expected[4][3], instance.getApplicationsAsArray("All")[2][3]);
		assertEquals("qmullen", instance.getApplicationById(11).getUnityId());
		assertEquals(expected[5][0], instance.getApplicationsAsArray("All")[3][0]);
		assertEquals(expected[5][1], instance.getApplicationsAsArray("All")[3][1]);
		assertEquals(expected[5][2], instance.getApplicationsAsArray("All")[3][2]);
		assertEquals(expected[5][3], instance.getApplicationsAsArray("All")[3][3]);
		
		instance.deleteApplicationById(11); // from back
		assertEquals(3, instance.getActivePosition().getApplications().size());
		assertEquals(3, instance.getApplicationsAsArray("All").length);
		assertNull(instance.getApplicationById(2));
		assertEquals("kgilles", instance.getApplicationById(3).getUnityId());
		assertEquals(expected[1][0], instance.getApplicationsAsArray("All")[0][0]);
		assertEquals(expected[1][1], instance.getApplicationsAsArray("All")[0][1]);
		assertEquals(expected[1][2], instance.getApplicationsAsArray("All")[0][2]);
		assertEquals(expected[1][3], instance.getApplicationsAsArray("All")[0][3]);
		assertEquals("frosari", instance.getApplicationById(4).getUnityId());
		assertEquals(expected[2][0], instance.getApplicationsAsArray("All")[1][0]);
		assertEquals(expected[2][1], instance.getApplicationsAsArray("All")[1][1]);
		assertEquals(expected[2][2], instance.getApplicationsAsArray("All")[1][2]);
		assertEquals(expected[2][3], instance.getApplicationsAsArray("All")[1][3]);
		assertNull(instance.getApplicationById(7));
		assertEquals("bmnieves", instance.getApplicationById(8).getUnityId());
		assertEquals(expected[4][0], instance.getApplicationsAsArray("All")[2][0]);
		assertEquals(expected[4][1], instance.getApplicationsAsArray("All")[2][1]);
		assertEquals(expected[4][2], instance.getApplicationsAsArray("All")[2][2]);
		assertEquals(expected[4][3], instance.getApplicationsAsArray("All")[2][3]);
		assertNull(instance.getApplicationById(11));
		
		instance.deleteApplicationById(8); 
		assertEquals(2, instance.getActivePosition().getApplications().size());
		assertEquals(2, instance.getApplicationsAsArray("All").length);
		assertNull(instance.getApplicationById(2));
		assertEquals("kgilles", instance.getApplicationById(3).getUnityId());
		assertEquals(expected[1][0], instance.getApplicationsAsArray("All")[0][0]);
		assertEquals(expected[1][1], instance.getApplicationsAsArray("All")[0][1]);
		assertEquals(expected[1][2], instance.getApplicationsAsArray("All")[0][2]);
		assertEquals(expected[1][3], instance.getApplicationsAsArray("All")[0][3]);
		assertEquals("frosari", instance.getApplicationById(4).getUnityId());
		assertEquals(expected[2][0], instance.getApplicationsAsArray("All")[1][0]);
		assertEquals(expected[2][1], instance.getApplicationsAsArray("All")[1][1]);
		assertEquals(expected[2][2], instance.getApplicationsAsArray("All")[1][2]);
		assertEquals(expected[2][3], instance.getApplicationsAsArray("All")[1][3]);
		assertNull(instance.getApplicationById(7));
		assertNull(instance.getApplicationById(8));
		assertNull(instance.getApplicationById(11));
		
		// reset manager for testing purposes
		instance.resetManager();
	}
	
	/**
	 * Additional testing for WolfHire.getApplicationsAsArray() method.
	 * Tests that application array is working correctly when some applications are updated.
	 * Tests that changes are reflected and addressed in method.
	 */
	@Test
	public void testArrayFilter() {
		assertNull(instance.getActivePosition());
		
		// add applications and check contents
		//System.out.println("ADDING APPLCIATIONS");
		instance.addNewPosition("P", 12, 12);
		instance.addApplicationToPosition("Hanna", "Reese", "hmreese");
		instance.addApplicationToPosition("Brooke", "Ballard", "bkballar");
		instance.addApplicationToPosition("Maggie", "Gardner", "megardn");
		assertEquals("P", instance.getActivePositionName());
		assertEquals(3, instance.getActivePosition().getApplications().size());
		assertEquals(3, instance.getApplicationsAsArray("All").length);
		
		// test when filter is All
		assertEquals("hmreese", instance.getActivePosition().getApplications().get(0).getUnityId()); // check first contents compared with application list
		assertEquals("Submitted", instance.getActivePosition().getApplications().get(0).getState());
		assertEquals("1", instance.getApplicationsAsArray("All")[0][0]);
		assertEquals("Submitted", instance.getApplicationsAsArray("All")[0][1]);
		assertEquals("hmreese", instance.getApplicationsAsArray("All")[0][2]);
		assertEquals("", instance.getApplicationsAsArray("All")[0][3]);
		assertEquals("2", instance.getApplicationsAsArray("All")[1][0]);
		assertEquals("Submitted", instance.getApplicationsAsArray("All")[1][1]);
		assertEquals("bkballar", instance.getApplicationsAsArray("All")[1][2]);
		assertEquals("", instance.getApplicationsAsArray("All")[1][3]);
		assertEquals("3", instance.getApplicationsAsArray("All")[2][0]);
		assertEquals("Submitted", instance.getApplicationsAsArray("All")[2][1]);
		assertEquals("megardn", instance.getApplicationsAsArray("All")[2][2]);
		assertEquals("", instance.getApplicationsAsArray("All")[2][3]);
		
		// test other *State filters
		assertEquals(3, instance.getApplicationsAsArray("Submitted").length);
		assertEquals(0, instance.getApplicationsAsArray("Rejected").length);
		assertEquals(0, instance.getApplicationsAsArray("Reviewing").length);
		assertEquals(0, instance.getApplicationsAsArray("Interviewing").length);
		assertEquals(0, instance.getApplicationsAsArray("Processing").length);
		assertEquals(0, instance.getApplicationsAsArray("Hired").length);
		assertEquals(0, instance.getApplicationsAsArray("Inactive").length);
		
		// test ALL filter after updating some of the applications to different states
		instance.executeCommand(1, new Command(CommandValue.REJECT, Application.DUPLICATE_REJECTION));
		assertEquals("Rejected", instance.getActivePosition().getApplications().get(0).getState());
		
		instance.executeCommand(3, new Command(CommandValue.ASSIGN, "reviewer"));
		assertEquals("Reviewing", instance.getActivePosition().getApplications().get(2).getState());
		assertEquals(1, instance.getApplicationsAsArray("Reviewing").length);
		instance.executeCommand(3, new Command(CommandValue.SCHEDULE, null));
		assertEquals("Interviewing", instance.getActivePosition().getApplications().get(2).getState());
		assertEquals(0, instance.getApplicationsAsArray("Reviewing").length);
		assertEquals(1, instance.getApplicationsAsArray("Interviewing").length);
		
		assertEquals("1", instance.getApplicationsAsArray("All")[0][0]);
		assertEquals("Rejected", instance.getApplicationsAsArray("All")[0][1]);
		assertEquals("hmreese", instance.getApplicationsAsArray("All")[0][2]);
		assertEquals("", instance.getApplicationsAsArray("All")[0][3]);
		assertEquals("2", instance.getApplicationsAsArray("All")[1][0]);
		assertEquals("Submitted", instance.getApplicationsAsArray("All")[1][1]);
		assertEquals("bkballar", instance.getApplicationsAsArray("All")[1][2]);
		assertEquals("", instance.getApplicationsAsArray("All")[1][3]);
		assertEquals("3", instance.getApplicationsAsArray("All")[2][0]);
		assertEquals("Interviewing", instance.getApplicationsAsArray("All")[2][1]);
		assertEquals("megardn", instance.getApplicationsAsArray("All")[2][2]);
		assertEquals("reviewer", instance.getApplicationsAsArray("All")[2][3]);
		
		// test SUBMITTED filter (when there is only one application)
		assertEquals(1, instance.getApplicationsAsArray("Submitted").length);
		assertEquals("2", instance.getApplicationsAsArray("Submitted")[0][0]);
		assertEquals("Submitted", instance.getApplicationsAsArray("Submitted")[0][1]);
		assertEquals("hmreese", instance.getApplicationsAsArray("All")[0][2]);
		assertEquals("", instance.getApplicationsAsArray("All")[0][3]);
		
		// move application 1 back to SUBMITTED state and then test submitted state
		instance.executeCommand(1, new Command(CommandValue.RESUBMIT, null));
		assertEquals("Submitted", instance.getActivePosition().getApplications().get(0).getState());
		// test SUBMITTED filter (when there is more than one application)
		assertEquals("2", instance.getApplicationsAsArray("Submitted")[1][0]);
		assertEquals("1", instance.getApplicationsAsArray("Submitted")[0][0]);
		assertEquals("Submitted", instance.getApplicationsAsArray("Submitted")[0][1]);
		assertEquals("hmreese", instance.getApplicationsAsArray("All")[0][2]);
		assertEquals("", instance.getApplicationsAsArray("All")[0][3]);
		assertEquals("2", instance.getApplicationsAsArray("Submitted")[1][0]);
		assertEquals("Submitted", instance.getApplicationsAsArray("Submitted")[1][1]);
		assertEquals("bkballar", instance.getApplicationsAsArray("All")[1][2]);
		assertEquals("", instance.getApplicationsAsArray("All")[1][3]);
		
		// move applications out of SUBMITTED state to test for filtered list of no SUBMITTED applications (as well as other *State filters)
		instance.executeCommand(1, new Command(CommandValue.REJECT, "Duplicate"));
		instance.executeCommand(2, new Command(CommandValue.REJECT, "Duplicate"));
		instance.executeCommand(3, new Command(CommandValue.REJECT, "Duplicate"));
		assertEquals(3, instance.getApplicationsAsArray("All").length);
		assertEquals(3, instance.getApplicationsAsArray("Rejected").length);
		assertEquals(0, instance.getApplicationsAsArray("Submitted").length);
		assertEquals(0, instance.getApplicationsAsArray("Reviewing").length);
		assertEquals(0, instance.getApplicationsAsArray("Processing").length);
		assertEquals(0, instance.getApplicationsAsArray("Interviewing").length);
		assertEquals(0, instance.getApplicationsAsArray("Hired").length);
		assertEquals(0, instance.getApplicationsAsArray("Inactive").length);
		// test contents from ALL filter
		assertEquals("hmreese", instance.getActivePosition().getApplications().get(0).getUnityId()); // check first contents compared with application list
		assertEquals("Rejected", instance.getActivePosition().getApplications().get(0).getState());
		assertEquals("1", instance.getApplicationsAsArray("All")[0][0]);
		assertEquals("Rejected", instance.getApplicationsAsArray("All")[0][1]);
		assertEquals("hmreese", instance.getApplicationsAsArray("All")[0][2]);
		assertEquals("", instance.getApplicationsAsArray("All")[0][3]);
		assertEquals("2", instance.getApplicationsAsArray("All")[1][0]);
		assertEquals("Rejected", instance.getApplicationsAsArray("All")[1][1]);
		assertEquals("bkballar", instance.getApplicationsAsArray("All")[1][2]);
		assertEquals("", instance.getApplicationsAsArray("All")[1][3]);
		assertEquals("3", instance.getApplicationsAsArray("All")[2][0]);
		assertEquals("Rejected", instance.getApplicationsAsArray("All")[2][1]);
		assertEquals("megardn", instance.getApplicationsAsArray("All")[2][2]);
		assertEquals("", instance.getApplicationsAsArray("All")[2][3]);
		// test contents from REJECTED filter
		assertEquals("hmreese", instance.getActivePosition().getApplications().get(0).getUnityId()); // check first contents compared with application list
		assertEquals("Rejected", instance.getActivePosition().getApplications().get(0).getState());
		assertEquals("1", instance.getApplicationsAsArray("Rejected")[0][0]);
		assertEquals("Rejected", instance.getApplicationsAsArray("Rejected")[0][1]);
		assertEquals("hmreese", instance.getApplicationsAsArray("Rejected")[0][2]);
		assertEquals("", instance.getApplicationsAsArray("Rejected")[0][3]);
		assertEquals("2", instance.getApplicationsAsArray("Rejected")[1][0]);
		assertEquals("Rejected", instance.getApplicationsAsArray("Rejected")[1][1]);
		assertEquals("bkballar", instance.getApplicationsAsArray("Rejected")[1][2]);
		assertEquals("", instance.getApplicationsAsArray("Rejected")[1][3]);
		assertEquals("3", instance.getApplicationsAsArray("Rejected")[2][0]);
		assertEquals("Rejected", instance.getApplicationsAsArray("Rejected")[2][1]);
		assertEquals("megardn", instance.getApplicationsAsArray("Rejected")[2][2]);
		assertEquals("", instance.getApplicationsAsArray("Rejected")[2][3]);
		
		// test contents with INACTIVE filter:
		// move to SUBMITTED
		instance.executeCommand(1, new Command(CommandValue.RESUBMIT, null));
		assertEquals(3, instance.getApplicationsAsArray("All").length);
		assertEquals(2, instance.getApplicationsAsArray("Rejected").length);
		assertEquals(1, instance.getApplicationsAsArray("Submitted").length);
		assertEquals(0, instance.getApplicationsAsArray("Reviewing").length);
		assertEquals(0, instance.getApplicationsAsArray("Processing").length);
		assertEquals(0, instance.getApplicationsAsArray("Interviewing").length);
		assertEquals(0, instance.getApplicationsAsArray("Hired").length);
		assertEquals(0, instance.getApplicationsAsArray("Inactive").length);
		// move to REVIEWING 
		instance.executeCommand(1, new Command(CommandValue.ASSIGN, "Reviewer"));
		assertEquals(3, instance.getApplicationsAsArray("All").length);
		assertEquals(2, instance.getApplicationsAsArray("Rejected").length);
		assertEquals(0, instance.getApplicationsAsArray("Submitted").length);
		assertEquals(1, instance.getApplicationsAsArray("Reviewing").length);
		assertEquals(0, instance.getApplicationsAsArray("Processing").length);
		assertEquals(0, instance.getApplicationsAsArray("Interviewing").length);
		assertEquals(0, instance.getApplicationsAsArray("Hired").length);
		assertEquals(0, instance.getApplicationsAsArray("Inactive").length);
		// move to INTERVIEWING
		instance.executeCommand(1, new Command(CommandValue.SCHEDULE, null));
		assertEquals(3, instance.getApplicationsAsArray("All").length);
		assertEquals(2, instance.getApplicationsAsArray("Rejected").length);
		assertEquals(0, instance.getApplicationsAsArray("Submitted").length);
		assertEquals(0, instance.getApplicationsAsArray("Reviewing").length);
		assertEquals(1, instance.getApplicationsAsArray("Interviewing").length);
		assertEquals(0, instance.getApplicationsAsArray("Processing").length);
		assertEquals(0, instance.getApplicationsAsArray("Hired").length);
		assertEquals(0, instance.getApplicationsAsArray("Inactive").length);
		// move to PROCESSING
		instance.executeCommand(1, new Command(CommandValue.PROCESS, null));
		assertEquals(3, instance.getApplicationsAsArray("All").length);
		assertEquals(2, instance.getApplicationsAsArray("Rejected").length);
		assertEquals(0, instance.getApplicationsAsArray("Submitted").length);
		assertEquals(0, instance.getApplicationsAsArray("Reviewing").length);
		assertEquals(0, instance.getApplicationsAsArray("Interviewing").length);
		assertEquals(1, instance.getApplicationsAsArray("Processing").length);
		assertEquals(0, instance.getApplicationsAsArray("Hired").length);
		assertEquals(0, instance.getApplicationsAsArray("Inactive").length);
		// move to HIRED
		instance.executeCommand(1, new Command(CommandValue.HIRE, null));
		assertEquals(3, instance.getApplicationsAsArray("All").length);
		assertEquals(2, instance.getApplicationsAsArray("Rejected").length);
		assertEquals(0, instance.getApplicationsAsArray("Submitted").length);
		assertEquals(0, instance.getApplicationsAsArray("Reviewing").length);
		assertEquals(0, instance.getApplicationsAsArray("Interviewing").length);
		assertEquals(0, instance.getApplicationsAsArray("Processing").length);
		assertEquals(1, instance.getApplicationsAsArray("Hired").length);
		assertEquals(0, instance.getApplicationsAsArray("Inactive").length);
		// move to INACTIVE
		instance.executeCommand(1, new Command(CommandValue.TERMINATE, "Resigned"));
		assertEquals(3, instance.getApplicationsAsArray("All").length);
		assertEquals(2, instance.getApplicationsAsArray("Rejected").length);
		assertEquals(0, instance.getApplicationsAsArray("Submitted").length);
		assertEquals(0, instance.getApplicationsAsArray("Reviewing").length);
		assertEquals(0, instance.getApplicationsAsArray("Interviewing").length);
		assertEquals(0, instance.getApplicationsAsArray("Processing").length);
		assertEquals(0, instance.getApplicationsAsArray("Hired").length);
		assertEquals(1, instance.getApplicationsAsArray("Inactive").length);
	}
	
}
