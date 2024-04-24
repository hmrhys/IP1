/**
 * 
 */
package edu.ncsu.csc216.wolf_hire.model.application;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.Before;
import org.junit.jupiter.api.Test;

import edu.ncsu.csc216.wolf_hire.model.command.Command;
import edu.ncsu.csc216.wolf_hire.model.command.Command.CommandValue;

/**
 * Tests that Application is being constructed correctly and that WolfHire's FSM is running accordingly.
 * 
 * @author hmreese2
 *
 */
public class ApplicationTest {
	
	/**
	 * Resets counter to start at the beginning of each test for testing purposes
	 * @throws Exception if there is an issue setting the counter
	 */
	@Before
	public void setUp() throws Exception {
		//Reset the counter at the beginning of every test.
		Application.setCounter(0);
	}
	
	/**
	 * Tests the shorter, more general Application constructor with invalid values
	 */
	@Test
	public void testApplicationShortInvalid() {
		// test null values for firstName, surname, unityId
		Exception e1 = assertThrows(IllegalArgumentException.class,
				() -> new Application(null, "surname", "unityId"));
		assertEquals("Application cannot be created.", e1.getMessage());
		
		Exception e2 = assertThrows(IllegalArgumentException.class,
				() -> new Application("firstName", null, "unityId"));
		assertEquals("Application cannot be created.", e2.getMessage());
		
		Exception e3 = assertThrows(IllegalArgumentException.class,
				() -> new Application("firstName", "surname", null));
		assertEquals("Application cannot be created.", e3.getMessage());
		
		// test empty string values for firstName, surname, unityId
		Exception e4 = assertThrows(IllegalArgumentException.class,
				() -> new Application("", "surname", "unityId"));
		assertEquals("Application cannot be created.", e4.getMessage());
		
		Exception e5 = assertThrows(IllegalArgumentException.class,
				() -> new Application("firstName", "", "unityId"));
		assertEquals("Application cannot be created.", e5.getMessage());
		
		Exception e6 = assertThrows(IllegalArgumentException.class,
				() -> new Application("firstName", "surname", ""));
		assertEquals("Application cannot be created.", e6.getMessage());
	}
	
	/**
	 * Tests the shorter, more general Application constructor with valid values. 
	 */
	@Test
	public void testApplicationShortValid() {
		assertDoesNotThrow(() -> new Application("firstName", "surname", "unityId"));
	}
	
	/**
	 * Tests the longer, more specified Application constructor with invalid values
	 */
	@Test public void testApplicationLongInvalid() {
		// test null state, firstName, surname, unityId
		Exception e1 = assertThrows(IllegalArgumentException.class,
				() -> new Application(1, null, "firstName", "surname", "unityId", "reviewer", null));
		assertEquals("Application cannot be created.", e1.getMessage());
		Exception e2 = assertThrows(IllegalArgumentException.class,
				() -> new Application(1, Application.REVIEWING_NAME, null, "surname", "unityId", "reviewer", null));
		assertEquals("Application cannot be created.", e2.getMessage());
		Exception e3 = assertThrows(IllegalArgumentException.class,
				() -> new Application(1, Application.REVIEWING_NAME, "firstName", null, "unityId", "reviewer", null));
		assertEquals("Application cannot be created.", e3.getMessage());
		Exception e4 = assertThrows(IllegalArgumentException.class,
				() -> new Application(1, Application.REVIEWING_NAME, "firstName", "surname", null, "reviewer", null));
		assertEquals("Application cannot be created.", e4.getMessage());
		
		// test empty string state, firstName, surname, unityId, reviewer, note
		Exception e5 = assertThrows(IllegalArgumentException.class,
				() -> new Application(1, "", "firstName", "surname", "unityId", "reviewer", null));
		assertEquals("Application cannot be created.", e5.getMessage());
		Exception e6 = assertThrows(IllegalArgumentException.class,
				() -> new Application(1, Application.REVIEWING_NAME, "", "surname", "unityId", "reviewer", null));
		assertEquals("Application cannot be created.", e6.getMessage());
		Exception e7 = assertThrows(IllegalArgumentException.class,
				() -> new Application(1, Application.REVIEWING_NAME, "firstName", "", "unityId", "reviewer", null));
		assertEquals("Application cannot be created.", e7.getMessage());
		Exception e8 = assertThrows(IllegalArgumentException.class,
				() -> new Application(1, Application.REVIEWING_NAME, "firstName", "surname", "", "reviewer", null));
		assertEquals("Application cannot be created.", e8.getMessage());
		Exception e9 = assertThrows(IllegalArgumentException.class,
				() -> new Application(1, Application.REVIEWING_NAME, "firstName", "surname", "unityId", "", null));
		assertEquals("Application cannot be created.", e9.getMessage());
		Exception e10 = assertThrows(IllegalArgumentException.class,
				() -> new Application(1, Application.REJECTED_NAME, "firstName", "surname", "unityId", null, ""));
		assertEquals("Application cannot be created.", e10.getMessage());
		
		// test invalid note options - valid reason with invalid state
		Exception e11 = assertThrows(IllegalArgumentException.class,
				() -> new Application(1, Application.SUBMITTED_NAME, "firstName", "surname", "unityId", null, Application.FIRED_TERMINATION));
		assertEquals("Application cannot be created.", e11.getMessage());
		Exception e12 = assertThrows(IllegalArgumentException.class,
				() -> new Application(1, Application.REJECTED_NAME, "firstName", "surname", "unityId", null, Application.FIRED_TERMINATION));
		assertEquals("Application cannot be created.", e12.getMessage());
		Exception e13 = assertThrows(IllegalArgumentException.class,
				() -> new Application(1, Application.SUBMITTED_NAME, "firstName", "surname", "unityId", null, Application.QUALIFICATIONS_REJECTION));
		assertEquals("Application cannot be created.", e13.getMessage());
		Exception e14 = assertThrows(IllegalArgumentException.class,
				() -> new Application(1, Application.INACTIVE_NAME, "firstName", "surname", "unityId", "reviewer", Application.QUALIFICATIONS_REJECTION));
		assertEquals("Application cannot be created.", e14.getMessage());
		
		// test invalid note options - invalid rejection reason or invalid termination reason (inappropriate non-empty string in correct state)
		Exception e15 = assertThrows(IllegalArgumentException.class,
				() -> new Application(1, Application.REVIEWING_NAME, "firstName", "surname", "unityId", null, "note"));
		assertEquals("Application cannot be created.", e15.getMessage());
		Exception e16 = assertThrows(IllegalArgumentException.class,
				() -> new Application(1, Application.INACTIVE_NAME, "firstName", "surname", "unityId", "reviewer", "note"));
		assertEquals("Application cannot be created.", e16.getMessage());
		
		// test reviewer value in invalid states
		Exception e17 = assertThrows(IllegalArgumentException.class,
				() -> new Application(1, Application.SUBMITTED_NAME, "firstName", "surname", "unityId", "reviewer", null));
		assertEquals("Application cannot be created.", e17.getMessage());
		Exception e18 = assertThrows(IllegalArgumentException.class,
				() -> new Application(1, Application.REJECTED_NAME, "firstName", "surname", "unityId", "reviewer", Application.QUALIFICATIONS_REJECTION));
		assertEquals("Application cannot be created.", e18.getMessage());
		
		// test invalid state name
		Exception e19 = assertThrows(IllegalArgumentException.class,
				() -> new Application(1, "State", "firstName", "surname", "unityId", null, null));
		assertEquals("Application cannot be created.", e19.getMessage());
		
		// test with 0 for id
		Exception e20 = assertThrows(IllegalArgumentException.class,
				() -> new Application(0, Application.SUBMITTED_NAME, "firstName", "surname", "unityId", null, null));
		assertEquals("Application cannot be created.", e20.getMessage());
	}
	
	/**
	 * Tests the longer, more specified Application constructor with valid values
	 */
	@Test public void testApplicationLongValid() {
		assertDoesNotThrow(() -> new Application(1, Application.SUBMITTED_NAME, "firstName", "surname", "unityId", null, null));
		assertDoesNotThrow(() -> new Application(1, Application.REJECTED_NAME, "firstName", "surname", "unityId", null, Application.QUALIFICATIONS_REJECTION));
		assertDoesNotThrow(() -> new Application(1, Application.REVIEWING_NAME, "firstName", "surname", "unityId", "reviewer", null));
		assertDoesNotThrow(() -> new Application(1, Application.INTERVIEWING_NAME, "firstName", "surname", "unityId", "reviewer", null));
		assertDoesNotThrow(() -> new Application(1, Application.PROCESSING_NAME, "firstName", "surname", "unityId", "reviewer", null));
		assertDoesNotThrow(() -> new Application(1, Application.HIRED_NAME, "firstName", "surname", "unityId", "reviewer", null));
		assertDoesNotThrow(() -> new Application(1, Application.INACTIVE_NAME, "firstName", "surname", "unityId", "reviewer", Application.FIRED_TERMINATION));
	}
	
	/**
	 * Tests that all getter methods (including long constructor) are working appropriately
	 */
	@Test
	public void testGettersAndConstructorsValid() {
		//Reset the counter 
		Application.setCounter(0);
		
		// test getters through application created with short constructor
		Application a = new Application("firstName", "surname", "unityId");
		assertEquals(1, a.getId());
		assertEquals("firstName", a.getFirstName());
		assertEquals("surname", a.getSurname());
		assertEquals("unityId", a.getUnityId());
		assertEquals("", a.getReviewer());
		assertEquals("", a.getNote());
		
		// test getters through applications created with long constructor
		Application a2  = new Application(1, Application.SUBMITTED_NAME, "firstName", "surname", "unityId", null, null);
		assertEquals(1, a2.getId());
		assertEquals("Submitted", a2.getState());
		assertEquals("firstName", a2.getFirstName());
		assertEquals("surname", a2.getSurname());
		assertEquals("unityId", a2.getUnityId());
		assertEquals("", a2.getReviewer());
		assertEquals("", a2.getNote());
		
		Application a3 = new Application(1, Application.REJECTED_NAME, "firstName", "surname", "unityId", null, Application.QUALIFICATIONS_REJECTION);
		assertEquals(1, a3.getId());
		assertEquals("Rejected", a3.getState());
		assertEquals("firstName", a3.getFirstName());
		assertEquals("surname", a3.getSurname());
		assertEquals("unityId", a3.getUnityId());
		assertEquals("", a3.getReviewer());
		assertEquals("Qualifications", a3.getNote());
		
		Application a4 = new Application(1, Application.REVIEWING_NAME, "firstName", "surname", "unityId", "reviewer", null);
		assertEquals(1, a4.getId());
		assertEquals("Reviewing", a4.getState());
		assertEquals("firstName", a4.getFirstName());
		assertEquals("surname", a4.getSurname());
		assertEquals("unityId", a4.getUnityId());
		assertEquals("reviewer", a4.getReviewer());
		assertEquals("", a4.getNote());
		
		Application a5 = new Application(1, Application.INTERVIEWING_NAME, "firstName", "surname", "unityId", "reviewer", null);
		assertEquals(1, a5.getId());
		assertEquals("Interviewing", a5.getState());
		assertEquals("firstName", a5.getFirstName());
		assertEquals("surname", a5.getSurname());
		assertEquals("unityId", a5.getUnityId());
		assertEquals("reviewer", a5.getReviewer());
		assertEquals("", a5.getNote());
		
		Application a6 = new Application(1, Application.PROCESSING_NAME, "firstName", "surname", "unityId", "reviewer", null);
		assertEquals(1, a6.getId());
		assertEquals("Processing", a6.getState());
		assertEquals("firstName", a6.getFirstName());
		assertEquals("surname", a6.getSurname());
		assertEquals("unityId", a6.getUnityId());
		assertEquals("reviewer", a6.getReviewer());
		assertEquals("", a6.getNote());
		
		Application a7 = new Application(1, Application.HIRED_NAME, "firstName", "surname", "unityId", "reviewer", null);
		assertEquals(1, a7.getId());
		assertEquals("Hired", a7.getState());
		assertEquals("firstName", a7.getFirstName());
		assertEquals("surname", a7.getSurname());
		assertEquals("unityId", a7.getUnityId());
		assertEquals("reviewer", a7.getReviewer());
		assertEquals("", a7.getNote());
		
		Application a8 = new Application(1, Application.INACTIVE_NAME, "firstName", "surname", "unityId", "reviewer", Application.FIRED_TERMINATION);
		assertEquals(1, a8.getId());
		assertEquals("Inactive", a8.getState());
		assertEquals("firstName", a8.getFirstName());
		assertEquals("surname", a8.getSurname());
		assertEquals("unityId", a8.getUnityId());
		assertEquals("reviewer", a8.getReviewer());
		assertEquals("Fired", a8.getNote());
	}
	
	/**
	 * Tests counter and id incrementation after multiple applications have been created.
	 * Ensures that id and counter are being correctly added up and maintained.
	 */
	@Test
	public void testCounter() {
		// During testing, you can use the setCounter() method to set the Application to a known counter 
		// and ensure that the first Application is created with that known id and the second one is created with an id of counter + 1
		
		// reset counter for testing purposes
		Application.setCounter(0);
		Application a1 = new Application("firstName", "surname", "unityId");
		assertEquals(1, a1.getId());
		
		Application a2 = new Application("firstName", "surname", "unityId");
		assertEquals(1, a1.getId());
		assertEquals(2, a2.getId());
		
		Application a3 = new Application(3, Application.REVIEWING_NAME, "firstName", "surname", "unityId", "reviewer", null);
		assertEquals(1, a1.getId());
		assertEquals(2, a2.getId());
		assertEquals(3, a3.getId());
		
		Application a4 = new Application(5, Application.REJECTED_NAME, "firstName", "surname", "unityId", null, Application.DUPLICATE_REJECTION);
		assertEquals(1, a1.getId());
		assertEquals(2, a2.getId());
		assertEquals(3, a3.getId());
		assertEquals(5, a4.getId());
		
		Application a5 = new Application("firstName", "surname", "unityId");
		assertEquals(1, a1.getId());
		assertEquals(2, a2.getId());
		assertEquals(3, a3.getId());
		assertEquals(5, a4.getId());
		assertEquals(6, a5.getId());
	}
	
	/**
	 * Tests that toString() method is working and formatting correctly
	 */
	@Test
	public void testToString() {
		// reset counter for testing purposes
		Application.setCounter(0);
		
		// SUBMITTED state 
		Application a1 = new Application("firstName", "surname", "unityId");
		assertEquals(1, a1.getId());
		assertEquals("Submitted", a1.getState());
		assertEquals("firstName", a1.getFirstName());
		assertEquals("surname", a1.getSurname());
		assertEquals("unityId", a1.getUnityId());
		assertEquals("", a1.getReviewer());
		assertEquals("", a1.getNote());
		assertEquals("* 1,Submitted,firstName,surname,unityId,,", a1.toString());
		// REJECTED state
		Application a2 = new Application(2, Application.REJECTED_NAME, "firstName", "surname", "unityId", null, Application.DUPLICATE_REJECTION);
		assertEquals(2, a2.getId());
		assertEquals("Rejected", a2.getState());
		assertEquals("firstName", a2.getFirstName());
		assertEquals("surname", a2.getSurname());
		assertEquals("unityId", a2.getUnityId());
		assertEquals("", a2.getReviewer());
		assertEquals("Duplicate", a2.getNote());
		assertEquals("* 2,Rejected,firstName,surname,unityId,,Duplicate", a2.toString());
		// REVIEWING state
		Application a3 = new Application(3, Application.REVIEWING_NAME, "firstName", "surname", "unityId", "reviewer", null);
		assertEquals(3, a3.getId());
		assertEquals("Reviewing", a3.getState());
		assertEquals("firstName", a3.getFirstName());
		assertEquals("surname", a3.getSurname());
		assertEquals("unityId", a3.getUnityId());
		assertEquals("reviewer", a3.getReviewer());
		assertEquals("", a3.getNote());
		assertEquals("* 3,Reviewing,firstName,surname,unityId,reviewer,", a3.toString());
		// INTERVIEWING state
		Application a4 = new Application(4, Application.INTERVIEWING_NAME, "firstName", "surname", "unityId", "reviewer", null);
		assertEquals(4, a4.getId());
		assertEquals("Interviewing", a4.getState());
		assertEquals("firstName", a4.getFirstName());
		assertEquals("surname", a4.getSurname());
		assertEquals("unityId", a4.getUnityId());
		assertEquals("reviewer", a4.getReviewer());
		assertEquals("", a4.getNote());
		assertEquals("* 4,Interviewing,firstName,surname,unityId,reviewer,", a4.toString());
		// PROCESSING state
		Application a5 = new Application(5, Application.PROCESSING_NAME, "firstName", "surname", "unityId", "reviewer", null);
		assertEquals(5, a5.getId());
		assertEquals("Processing", a5.getState());
		assertEquals("firstName", a5.getFirstName());
		assertEquals("surname", a5.getSurname());
		assertEquals("unityId", a5.getUnityId());
		assertEquals("reviewer", a5.getReviewer());
		assertEquals("", a5.getNote());
		assertEquals("* 5,Processing,firstName,surname,unityId,reviewer,", a5.toString());
		// HIRED state
		Application a6 = new Application(6, Application.HIRED_NAME, "firstName", "surname", "unityId", "reviewer", null);
		assertEquals(6, a6.getId());
		assertEquals("Hired", a6.getState());
		assertEquals("firstName", a6.getFirstName());
		assertEquals("surname", a6.getSurname());
		assertEquals("unityId", a6.getUnityId());
		assertEquals("reviewer", a6.getReviewer());
		assertEquals("", a6.getNote());
		assertEquals("* 6,Hired,firstName,surname,unityId,reviewer,", a6.toString());
		// INACTIVE state
		Application a7 = new Application(7, Application.INACTIVE_NAME, "firstName", "surname", "unityId", "reviewer", Application.COMPLETED_TERMINATION);
		assertEquals(7, a7.getId());
		assertEquals("Inactive", a7.getState());
		assertEquals("firstName", a7.getFirstName());
		assertEquals("surname", a7.getSurname());
		assertEquals("unityId", a7.getUnityId());
		assertEquals("reviewer", a7.getReviewer());
		assertEquals("Completed", a7.getNote());
		assertEquals("* 7,Inactive,firstName,surname,unityId,reviewer,Completed", a7.toString());
	}
	
	/**
	 * Originally used as a debugging test for the counter functionality, this provides additional tests that supplement ApplicationTest.testCounter()
	 */
	@Test
	public void testAdditionalCounterTests() {
		// reset counter for testing purposes
		Application.setCounter(0);
		
		Application a1 = new Application("firstName", "surname", "unityId");
		assertEquals(1, a1.getId());
		Application a2 = new Application(2, Application.SUBMITTED_NAME, "firstName", "surname", "unityId", null, null);
		assertEquals(1, a1.getId());
		assertEquals(2, a2.getId());
		Application a3 = new Application(3, Application.INACTIVE_NAME, "firstName", "surname", "unityId", "reviewer", Application.FIRED_TERMINATION);
		assertEquals(1, a1.getId());
		assertEquals(2, a2.getId());
		assertEquals(3, a3.getId());
		Application a4 = new Application("firstName", "surname", "unityId");
		assertEquals(1, a1.getId());
		assertEquals(2, a2.getId());
		assertEquals(3, a3.getId());
		assertEquals(4, a4.getId());
		Application a5 = new Application("firstName", "surname", "unityId");
		assertEquals(1, a1.getId());
		assertEquals(2, a2.getId());
		assertEquals(3, a3.getId());
		assertEquals(4, a4.getId());
		assertEquals(5, a5.getId());
	}
	
	// the following tests the State pattern
	
	/**
	 * Tests the state pattern with all possible transitions (valid and invalid)
	 */
	@Test
	public void testApplicationStateValid() {
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
				
		// reset counter for testing purposes
		Application.setCounter(0);
				
		// create testable application starting in submitted state (start state of FSM)
		Application a = new Application("firstName", "surname", "unityId");
		assertEquals(1, a.getId());
		assertEquals(Application.SUBMITTED_NAME, a.getState());
		assertEquals("firstName", a.getFirstName());
		assertEquals("surname", a.getSurname());
		assertEquals("unityId", a.getUnityId());
		assertEquals("", a.getReviewer());
		assertEquals("", a.getNote());
		
		Exception e = assertThrows(UnsupportedOperationException.class,
				() -> a.update(new Command(CommandValue.REJECT, "Other")));
		assertEquals("Invalid command!!", e.getMessage());
		assertEquals("Submitted", a.getState());
		// test invalid transitions from SUBMITTED state
		Exception e1 = assertThrows(UnsupportedOperationException.class,
				() -> a.update(resubmit));
		assertEquals("Invalid command.", e1.getMessage());
		Exception e2 = assertThrows(UnsupportedOperationException.class,
				() -> a.update(schedule));
		assertEquals("Invalid command.", e2.getMessage());
		Exception e3 = assertThrows(UnsupportedOperationException.class,
				() -> a.update(cmdReturn));
		assertEquals("Invalid command.", e3.getMessage());
		Exception e4 = assertThrows(UnsupportedOperationException.class,
				() -> a.update(process));
		assertEquals("Invalid command.", e4.getMessage());
		Exception e5 = assertThrows(UnsupportedOperationException.class,
				() -> a.update(hire));
		assertEquals("Invalid command.", e5.getMessage());
		Exception e6 = assertThrows(UnsupportedOperationException.class,
				() -> a.update(terminate));
		assertEquals("Invalid command.", e6.getMessage());
		
		// test SUBMITTED to REJECTED
		assertDoesNotThrow(() -> a.update(reject));
		assertEquals(Application.REJECTED_NAME, a.getState());
		assertEquals("", a.getReviewer());
		assertEquals(Application.DUPLICATE_REJECTION, a.getNote());
		
		// test invalid transitions from REJECTED state
		Exception e7 = assertThrows(UnsupportedOperationException.class,
				() -> a.update(assign));
		assertEquals("Invalid command.", e7.getMessage());
		Exception e8 = assertThrows(UnsupportedOperationException.class,
				() -> a.update(reject));
		assertEquals("Invalid command.", e8.getMessage());
		Exception e9 = assertThrows(UnsupportedOperationException.class,
				() -> a.update(cmdReturn));
		assertEquals("Invalid command.", e9.getMessage());
		Exception e10 = assertThrows(UnsupportedOperationException.class,
				() -> a.update(schedule));
		assertEquals("Invalid command.", e10.getMessage());
		Exception e11 = assertThrows(UnsupportedOperationException.class,
				() -> a.update(process));
		assertEquals("Invalid command.", e11.getMessage());
		Exception e12 = assertThrows(UnsupportedOperationException.class,
				() -> a.update(hire));
		assertEquals("Invalid command.", e12.getMessage());
		Exception e13 = assertThrows(UnsupportedOperationException.class,
				() -> a.update(terminate));
		assertEquals("Invalid command.", e13.getMessage());
		
		// test REJECTED to SUBMITTED
		assertDoesNotThrow(() -> a.update(resubmit));
		assertEquals(Application.SUBMITTED_NAME, a.getState());
		assertEquals("", a.getReviewer());
		assertEquals("", a.getNote());
		
		// test SUBMITTED to REVIEWING
		assertDoesNotThrow(() -> a.update(assign));
		assertEquals(Application.REVIEWING_NAME, a.getState());
		assertEquals("reviewer", a.getReviewer());
		assertEquals("", a.getNote());
		
		// test invalid transitions from REVIEWING state
		Exception e14 = assertThrows(UnsupportedOperationException.class,
				() -> a.update(assign));
		assertEquals("Invalid command.", e14.getMessage());
		Exception e15 = assertThrows(UnsupportedOperationException.class,
				() -> a.update(resubmit));
		assertEquals("Invalid command.", e15.getMessage());
		Exception e16 = assertThrows(UnsupportedOperationException.class,
				() -> a.update(process));
		assertEquals("Invalid command.", e16.getMessage());
		Exception e17 = assertThrows(UnsupportedOperationException.class,
				() -> a.update(hire));
		assertEquals("Invalid command.", e17.getMessage());
		Exception e18 = assertThrows(UnsupportedOperationException.class,
				() -> a.update(terminate));
		assertEquals("Invalid command.", e18.getMessage());
		
		// test REVIEWING to SUBMITTED
		assertDoesNotThrow(() -> a.update(cmdReturn));
		assertEquals(Application.SUBMITTED_NAME, a.getState());
		assertEquals("", a.getReviewer());
		assertEquals("", a.getNote());
		// go back to REVIEWING
		assertDoesNotThrow(() -> a.update(assign));
		assertEquals(Application.REVIEWING_NAME, a.getState());
		assertEquals("reviewer", a.getReviewer());
		assertEquals("", a.getNote());
		
		// test REVIEWING to REJECTED
		assertDoesNotThrow(() -> a.update(reject));
		assertEquals(Application.REJECTED_NAME, a.getState());
		assertEquals("", a.getReviewer());
		assertEquals(Application.DUPLICATE_REJECTION, a.getNote());
		// go back to REVIEWING (rejected > submitted > reviewing)
		assertDoesNotThrow(() -> a.update(resubmit));
		assertDoesNotThrow(() -> a.update(assign));
		assertEquals(Application.REVIEWING_NAME, a.getState());
		assertEquals("reviewer", a.getReviewer());
		assertEquals("", a.getNote());
		
		// test REVIEWING to INTERVIEWING
		assertDoesNotThrow(() -> a.update(schedule));
		assertEquals(Application.INTERVIEWING_NAME, a.getState());
		assertEquals("reviewer", a.getReviewer());
		assertEquals("", a.getNote());
		
		// test invalid transitions from INTERVIEWING state
		Exception e19 = assertThrows(UnsupportedOperationException.class,
				() -> a.update(resubmit));
		assertEquals("Invalid command.", e19.getMessage());
		Exception e20 = assertThrows(UnsupportedOperationException.class,
				() -> a.update(cmdReturn));
		assertEquals("Invalid command.", e20.getMessage());
		Exception e21 = assertThrows(UnsupportedOperationException.class,
				() -> a.update(hire));
		assertEquals("Invalid command.", e21.getMessage());
		Exception e22 = assertThrows(UnsupportedOperationException.class,
				() -> a.update(terminate));
		assertEquals("Invalid command.", e22.getMessage());
		
		// test INTERVIEWING to REJECTED
		assertDoesNotThrow(() -> a.update(reject));
		assertEquals(Application.REJECTED_NAME, a.getState());
		assertEquals("", a.getReviewer());
		assertEquals(Application.DUPLICATE_REJECTION, a.getNote());
		// go back to INTERVIEWING
		assertDoesNotThrow(() -> a.update(resubmit));
		assertDoesNotThrow(() -> a.update(assign));
		assertDoesNotThrow(() -> a.update(schedule));
		assertEquals(Application.INTERVIEWING_NAME, a.getState());
		assertEquals("reviewer", a.getReviewer());
		assertEquals("", a.getNote());
		
		// test INTERVIEWING to REVIEWING with new reviewer (assign2 command)
		assertDoesNotThrow(() -> a.update(assign2));
		assertEquals(Application.REVIEWING_NAME, a.getState());
		assertEquals("reviewer2", a.getReviewer());
		assertEquals("", a.getNote());
		// go back to INTERVIEWING
		assertDoesNotThrow(() -> a.update(schedule));
		// test INTERVIEWING to INTERVIEWING
		assertDoesNotThrow(() -> a.update(schedule));
		assertEquals(Application.INTERVIEWING_NAME, a.getState());
		assertEquals("reviewer2", a.getReviewer());
		assertEquals("", a.getNote());
		
		// test INTERVIEWING to PROCESSING
		assertDoesNotThrow(() -> a.update(process));
		assertEquals(Application.PROCESSING_NAME, a.getState());
		assertEquals("reviewer2", a.getReviewer());
		assertEquals("", a.getNote());
		
		// test invalid transitions from PROCESSING state
		Exception e23 = assertThrows(UnsupportedOperationException.class,
				() -> a.update(assign));
		assertEquals("Invalid command.", e23.getMessage());
		Exception e24 = assertThrows(UnsupportedOperationException.class,
				() -> a.update(resubmit));
		assertEquals("Invalid command.", e24.getMessage());
		Exception e25 = assertThrows(UnsupportedOperationException.class,
				() -> a.update(cmdReturn));
		assertEquals("Invalid command.", e25.getMessage());
		Exception e26 = assertThrows(UnsupportedOperationException.class,
				() -> a.update(schedule));
		assertEquals("Invalid command.", e26.getMessage());
		Exception e27 = assertThrows(UnsupportedOperationException.class,
				() -> a.update(process));
		assertEquals("Invalid command.", e27.getMessage());
		Exception e28 = assertThrows(UnsupportedOperationException.class,
				() -> a.update(terminate));
		assertEquals("Invalid command.", e28.getMessage());
		
		// test PROCESSING to REJECTED
		assertDoesNotThrow(() -> a.update(reject));
		assertEquals(Application.REJECTED_NAME, a.getState());
		assertEquals("", a.getReviewer());
		assertEquals(Application.DUPLICATE_REJECTION, a.getNote());
		// go back to PROCESSING (rejected > submitted > reviewing > interviewing > processing)
		assertDoesNotThrow(() -> a.update(resubmit));
		assertDoesNotThrow(() -> a.update(assign));
		assertDoesNotThrow(() -> a.update(schedule));
		assertDoesNotThrow(() -> a.update(process));

		// test PROCESSING to HIRED
		assertDoesNotThrow(() -> a.update(hire));
		assertEquals(Application.HIRED_NAME, a.getState());
		assertEquals("reviewer", a.getReviewer());
		assertEquals("", a.getNote());
		
		// test invalid transitions from HIRED state
		Exception e29 = assertThrows(UnsupportedOperationException.class,
				() -> a.update(assign));
		assertEquals("Invalid command.", e29.getMessage());
		Exception e30 = assertThrows(UnsupportedOperationException.class,
				() -> a.update(reject));
		assertEquals("Invalid command.", e30.getMessage());
		Exception e31 = assertThrows(UnsupportedOperationException.class,
				() -> a.update(resubmit));
		assertEquals("Invalid command.", e31.getMessage());
		Exception e32 = assertThrows(UnsupportedOperationException.class,
				() -> a.update(cmdReturn));
		assertEquals("Invalid command.", e32.getMessage());
		Exception e33 = assertThrows(UnsupportedOperationException.class,
				() -> a.update(schedule));
		assertEquals("Invalid command.", e33.getMessage());
		Exception e34 = assertThrows(UnsupportedOperationException.class,
				() -> a.update(process));
		assertEquals("Invalid command.", e34.getMessage());
		Exception e35 = assertThrows(UnsupportedOperationException.class,
				() -> a.update(hire));
		assertEquals("Invalid command.", e35.getMessage());
		
		// test HIRED to INACTIVE
		assertDoesNotThrow(() -> a.update(terminate));
		assertEquals(Application.INACTIVE_NAME, a.getState());
		assertEquals("reviewer", a.getReviewer());
		assertEquals(Application.FIRED_TERMINATION, a.getNote());
		
		// test invalid transitions from INACTIVE state
		Exception e36 = assertThrows(UnsupportedOperationException.class,
				() -> a.update(assign));
		assertEquals("Invalid command.", e36.getMessage());
		Exception e37 = assertThrows(UnsupportedOperationException.class,
				() -> a.update(reject));
		assertEquals("Invalid command.", e37.getMessage());
		Exception e38 = assertThrows(UnsupportedOperationException.class,
				() -> a.update(resubmit));
		assertEquals("Invalid command.", e38.getMessage());
		Exception e39 = assertThrows(UnsupportedOperationException.class,
				() -> a.update(cmdReturn));
		assertEquals("Invalid command.", e39.getMessage());
		Exception e40 = assertThrows(UnsupportedOperationException.class,
				() -> a.update(schedule));
		assertEquals("Invalid command.", e40.getMessage());
		Exception e41 = assertThrows(UnsupportedOperationException.class,
				() -> a.update(process));
		assertEquals("Invalid command.", e41.getMessage());
		Exception e42 = assertThrows(UnsupportedOperationException.class,
				() -> a.update(hire));
		assertEquals("Invalid command.", e42.getMessage());
		Exception e43 = assertThrows(UnsupportedOperationException.class,
				() -> a.update(terminate));
		assertEquals("Invalid command.", e43.getMessage());
	}
	
	/**
	 * Tests that counter is keeping up even when IDs are given out of order (greater than, less than current counter value etc.)
	 */
	@Test
	public void testCounterOutOfOrder() {
		// reset counter for testing purposes
		Application.setCounter(0);
		
		Application a = new Application("firstName", "surname", "unityId");
		Application aa = new Application(2, Application.INACTIVE_NAME, "firstName", "surname", "unityId", "reviewer", Application.FIRED_TERMINATION);
		assertEquals(1, a.getId());
		assertEquals(2, aa.getId());
		Application a1 = new Application(7, "Inactive", "Audrey", "Kemp", "akemp", "tnmacnei", "Fired");
		assertEquals(7, a1.getId());
		Application a2 = new Application(5, Application.INACTIVE_NAME, "firstName", "surname", "unityId", "reviewer", Application.FIRED_TERMINATION);
		assertEquals(7, a1.getId());
		assertEquals(5, a2.getId());
		Application a3 = new Application("firstName", "surname", "unityId");
		assertEquals(7, a1.getId());
		assertEquals(5, a2.getId());
		assertEquals(8, a3.getId());
	}
	
	/**
	 * Tests setting counter to 1 and ensuring that the next id is still set to 1.
	 * Originally a TS test, but was created locally for debugging purposes.
	 */
	@Test
	public void testTSCounter1() {
		Application.setCounter(0);
		
		Application app1 = new Application(3, "Rejected", "Jeremy", "Short", "jshort", null, "Duplicate");
		assertEquals(3, app1.getId());
		Application app2 = new Application(5, "Submitted", "Colleen", "Price", "coprice", null, null);
		assertEquals(5, app2.getId());
		Application app3 = new Application("Jessica", "Smith", "jesmith");
		assertEquals(6, app3.getId());
		// test setting counter to 1, next id constructed from short constructor should be 1
		Application.setCounter(1);
		
		Application a1 = new Application("firstName", "surname", "unityId");
		assertEquals(1, a1.getId());
		
		Application a2 = new Application("Charlie", "Brown", "cbrown");
		assertEquals(2, a2.getId());
		
	}
	
	/**
	 * Additional counter testing, used to supplement testing for TS debugging purposes.
	 */
	@Test
	public void testTSCounter2() {
		Application.setCounter(0);
		
		Application a1 = new Application("firstName", "surname", "unityId");
		assertEquals(1, a1.getId());
		
		Application a2 = new Application("Charlie", "Brown", "cbrown");
		assertEquals(2, a2.getId());
	}
	
	/**
	 * Additional counter tests used to ensure functionality and debug
	 */
	@Test
	public void testDebuggingCounter() {
		Application.setCounter(0);
		
		Application a1 = new Application("Betsy", "Bop", "bbop");
		assertEquals(1, a1.getId());
		Application a2 = new Application("Charlie", "Brown", "chbrown");
		assertEquals(2, a2.getId());
		Application a3 = new Application(3, "Submitted", "Snoop", "Dogg", "sdogg", null, null);
		assertEquals(3, a3.getId());
		Application a4 = new Application("Randall", "Steves", "rasteve");
		assertEquals(4, a4.getId());
		Application a5 = new Application(6, "Rejected", "Rose", "Stone", "rstone", null, "Duplicate");
		assertEquals(6, a5.getId());
		Application a6 = new Application("Maddie", "Ziegler", "mziegler");
		assertEquals(7, a6.getId());
		
		Application.setCounter(0);
		
		Application a = new Application(1, "Processing", "Joan", "Hewitt", "jhewitt", "reviewer", null);
		assertEquals(1, a.getId());
		Application aa = new Application("Jack", "Blanco", "jblanco");
		assertEquals(2, aa.getId());
		Application aaa = new Application(4, "Interviewing", "Harry", "Jackson", "hjacks", "reviewer", null);
		assertEquals(4, aaa.getId());
		Application aaaa = new Application("Rebecca", "Black", "rblack");
		assertEquals(5, aaaa.getId());
	}
}
