/**
 * 
 */
package edu.ncsu.csc216.wolf_hire.model.manager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;

import edu.ncsu.csc216.wolf_hire.model.application.Application;
import edu.ncsu.csc216.wolf_hire.model.command.Command;
import edu.ncsu.csc216.wolf_hire.model.command.Command.CommandValue;

/**
 * Tests that Position is being constructed correctly and that the list of application(s) within
 * a position are being managed appropriately.
 * 
 * @author hmreese2
 *
 */
public class PositionTest {
	
	/** Testable Position object used for multiple testing purposes */
	private Position p = new Position("positionName", 10, 20);

	/**
	 * Tests Position constructor with invalid values
	 */
	@Test
	public void testPositionInvalid() {
		// test empty string and null positionName values
		assertThrows(IllegalArgumentException.class,
				() -> new Position("", 6, 8));
		assertThrows(IllegalArgumentException.class,
				() -> new Position(null, 6, 8));
		
		// test invalid hoursPerWeek boundaries
		assertThrows(IllegalArgumentException.class,
				() -> new Position("positionName", 4, 8));
		assertThrows(IllegalArgumentException.class,
				() -> new Position("positionName", 21, 8));
		
		// test invalid payRate boundaries
		assertThrows(IllegalArgumentException.class,
				() -> new Position("positionName", 6, 6));
		assertThrows(IllegalArgumentException.class,
				() -> new Position("positionName", 6, 36));
	}
	
	/**
	 * Tests Position constructor with valid values (on and in-between boundaries).
	 * Tests that getter methods are returning accurate values.
	 */
	@Test
	public void testPositionValid() {
		// test boundary values with hoursPerWeek
		assertDoesNotThrow(() -> new Position("positionName", 5, 8));
		assertDoesNotThrow(() -> new Position("positionName", 20, 8));
		// test in-between boundary values with hoursPerWeek
		assertDoesNotThrow(() -> new Position("positionName", 10, 8));
		// test boundary values with payRate
		assertDoesNotThrow(() -> new Position("positionName", 6, 7));
		assertDoesNotThrow(() -> new Position("positionName", 6, 35));
		// test in-between boundary values with payRate
		assertDoesNotThrow(() -> new Position("positionName", 6, 20));
		
		// check that application list was constructed correctly
		p = new Position("positionName", 10, 20);
		assertEquals("positionName", p.getPositionName());
		assertEquals(10, p.getHoursPerWeek());
		assertEquals(20, p.getPayRate());
		assertEquals(0, p.getApplications().size());

	}
	
	/**
	 * Tests adding an application (fields as parameters) to list
	 */
	@Test
	public void testAddApplicationFields() {
		// reset counter for testing purposes
		Application.setCounter(0);
		
		assertEquals(1, p.addApplication("firstName", "surname", "unityId"));
		assertEquals(1, p.getApplications().size());
		assertEquals(Application.SUBMITTED_NAME, p.getApplicationById(1).getState());
		assertEquals("firstName", p.getApplicationById(1).getFirstName());
		assertEquals("surname", p.getApplicationById(1).getSurname());
		assertEquals("unityId", p.getApplicationById(1).getUnityId());
		assertEquals("", p.getApplicationById(1).getReviewer());
		assertEquals("", p.getApplicationById(1).getNote());
		
	}
	
	/**
	 * Tests adding an application object to list through Position.addApplication(Application) method.
	 * Tests both valid and invalid pathways (invalid being a duplicate id existing).
	 */
	@Test
	public void testAddApplicationObject() {
		Application.setCounter(0);
		// test valid pathway
		Application a = new Application(1, Application.REVIEWING_NAME, "firstName", "surname", "unityId", "reviewer", null);

		try {
			// test adding one application
			assertEquals(1, p.addApplication(a));
			// test that application list has been updated correctly
			assertEquals(1, p.getApplications().size());
			assertEquals(1, p.getApplicationById(1).getId());
			assertEquals(Application.REVIEWING_NAME, p.getApplicationById(1).getState());
			assertEquals("firstName", p.getApplicationById(1).getFirstName());
			assertEquals("surname", p.getApplicationById(1).getSurname());
			assertEquals("unityId", p.getApplicationById(1).getUnityId());
			assertEquals("reviewer", p.getApplicationById(1).getReviewer());
			assertEquals("", p.getApplicationById(1).getNote());
		
			// test adding more applications
			Application a2 = new Application("Deja", "Pence", "dpence");
			Application a3 = new Application(4, Application.HIRED_NAME, "John", "Fargo", "jfargo", "reviewer", null);
			Application a4 = new Application(3, Application.INTERVIEWING_NAME, "Margot", "Rigby", "mrigby3", "reviewer", null);
			assertEquals(2, p.addApplication(a2));
			assertEquals(4, p.addApplication(a3));
			assertEquals(3, p.addApplication(a4));
			assertEquals(4, p.getApplications().size());
			// check contents of new applications
			assertEquals(2, p.getApplicationById(2).getId());
			assertEquals(Application.SUBMITTED_NAME, p.getApplicationById(2).getState());
			assertEquals("Deja", p.getApplicationById(2).getFirstName());
			assertEquals("Pence", p.getApplicationById(2).getSurname());
			assertEquals("dpence", p.getApplicationById(2).getUnityId());
			assertEquals("", p.getApplicationById(2).getReviewer());
			assertEquals("", p.getApplicationById(2).getNote());
			
			assertEquals(4, p.getApplicationById(4).getId());
			assertEquals(Application.HIRED_NAME, p.getApplicationById(4).getState());
			assertEquals("John", p.getApplicationById(4).getFirstName());
			assertEquals("Fargo", p.getApplicationById(4).getSurname());
			assertEquals("jfargo", p.getApplicationById(4).getUnityId());
			assertEquals("reviewer", p.getApplicationById(4).getReviewer());
			assertEquals("", p.getApplicationById(4).getNote());
			
			assertEquals(3, p.getApplicationById(3).getId());
			assertEquals(Application.INTERVIEWING_NAME, p.getApplicationById(3).getState());
			assertEquals("Margot", p.getApplicationById(3).getFirstName());
			assertEquals("Rigby", p.getApplicationById(3).getSurname());
			assertEquals("mrigby3", p.getApplicationById(3).getUnityId());
			assertEquals("reviewer", p.getApplicationById(3).getReviewer());
			assertEquals("", p.getApplicationById(3).getNote());
		
			// test getting an application by an id that doesn't exist in list
			assertNull(p.getApplicationById(7));
		
			// test invalid pathway - adding duplicate application id
			Application other = new Application(2, Application.REJECTED_NAME, "first", "last", "flast2", null, Application.DUPLICATE_REJECTION);
			Exception e = assertThrows(IllegalArgumentException.class,
				() -> p.addApplication(other));
			assertEquals("Application cannot be created.", e.getMessage());
		
			// test that list size hasn't changed
			assertEquals(4, p.getApplications().size());
		
		} catch (IllegalArgumentException e) {
			fail(e.getMessage());
		}
		
	}
	
	/**
	 * Tests adding applications out of order, makes sure they are sorted when added to list
	 */
	@Test
	public void testAddingSortedOrder() {
		// reset counter for testing purposes
		Application.setCounter(0);
		
		// create testable applications
		Application a3 = new Application(3, Application.REJECTED_NAME, "May", "Walter", "mcwalt2", null, Application.QUALIFICATIONS_REJECTION);
		Application a2 = new Application(2, Application.INACTIVE_NAME, "Summer", "Fitzgerald", "smfitz2", "reviewer", Application.FIRED_TERMINATION);
		Application a4 = new Application(4, Application.PROCESSING_NAME, "Perry", "Platypus", "pplatypus", "reviewer", null);
		Application a1 = new Application(1, Application.INTERVIEWING_NAME, "Howard", "Smith", "hasmith", "reviewer", null);
		Application a5 = new Application(5, Application.SUBMITTED_NAME, "James", "Stevenson", "jsteve", null, null);

		// add applications
		assertEquals(2, p.addApplication(a2));
		assertEquals(1, p.addApplication(a1));
		assertEquals(3, p.addApplication(a3));
		assertEquals(5, p.addApplication(a5));
		assertEquals(4, p.addApplication(a4));
		assertEquals(5, p.getApplications().size());
		
		// check that applications have been with their appropriate id
		assertEquals(a1, p.getApplicationById(1));
		assertEquals(a2, p.getApplicationById(2));
		assertEquals(a3, p.getApplicationById(3));
		assertEquals(a4, p.getApplicationById(4));
		assertEquals(a5, p.getApplicationById(5));
	
		// check that applications were sorted after adding
		assertEquals(a1, p.getApplications().get(0));
		assertEquals(a2, p.getApplications().get(1));
		assertEquals(a3, p.getApplications().get(2));
		assertEquals(a4, p.getApplications().get(3));
		assertEquals(a5, p.getApplications().get(4));
	}
	
	/**
	 * Additional tests on sort - focuses on sorting when both addApplication() methods are used together
	 */
	@Test
	public void testSortExtended() {
		// reset counter for testing purposes
		Application.setCounter(0);
				
		// make and add applications
		Application a4 = new Application(4, Application.INACTIVE_NAME, "Susan", "Brown", "sbrown3", "rvlad2", Application.RESIGNED_TERMINATION);
		assertEquals(4, p.addApplication(a4));
		Application a1 = new Application(1, Application.REJECTED_NAME, "Andrew", "Cat", "acat", null, Application.DUPLICATE_REJECTION);
		assertEquals(1, p.addApplication(a1));
		assertEquals(5, p.addApplication("Jay", "Fox", "jfox"));	
		Application a2 = new Application(2, Application.INTERVIEWING_NAME, "Henry", "Dog", "hdog", "hmrese", null);
		assertEquals(2, p.addApplication(a2));
		assertEquals(6, p.addApplication("Harry", "Smith", "hsmith"));
		Application a7 = new Application(7, Application.SUBMITTED_NAME, "Henry", "Dog", "hdog", null, null);
		assertEquals(7, p.addApplication(a7));
		assertEquals(8, p.addApplication("Bo", "Peep", "bopeep"));
		assertEquals(7, p.getApplications().size());
		
		
		// check that they are sorted:
		
		// index 0
		assertEquals(a1, p.getApplications().get(0));
		assertEquals(1, p.getApplications().get(0).getId());
		// index 1
		assertEquals(a2, p.getApplications().get(1));
		assertEquals(2, p.getApplications().get(1).getId());
		// index 2
		assertEquals(a4, p.getApplications().get(2));
		assertEquals(4, p.getApplications().get(2).getId());
		// index 3
		assertEquals(5, p.getApplications().get(3).getId());
		assertEquals("Jay", p.getApplications().get(3).getFirstName());
		assertEquals("Fox", p.getApplications().get(3).getSurname());
		assertEquals("jfox", p.getApplications().get(3).getUnityId());
		// index 4
		assertEquals(6, p.getApplications().get(4).getId());
		assertEquals("Harry", p.getApplications().get(4).getFirstName());
		assertEquals("Smith", p.getApplications().get(4).getSurname());
		assertEquals("hsmith", p.getApplications().get(4).getUnityId());
		// index 5
		assertEquals(a7, p.getApplications().get(5));
		// index 6
		assertEquals(8, p.getApplications().get(6).getId());
		assertEquals("Bo", p.getApplications().get(6).getFirstName());
		assertEquals("Peep", p.getApplications().get(6).getSurname());
		assertEquals("bopeep", p.getApplications().get(6).getUnityId());
	}
	
	/**
	 * Tests executing a command on an application.
	 */
	@Test
	public void testExecuteCommand() {
		// reset counter for testing purposes
		Application.setCounter(0);
		
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
		
		// create testable application in SUBMITTED state and add to list
		Application a = new Application("Fred", "Michaels", "fmichae2");
		p.addApplication(a);
		// ensure you are working with correct id
		assertEquals(1, p.getApplicationById(1).getId());
		
		// test a SUBMITTED to REJECTED
		p.executeCommand(1, reject);
		assertEquals(Application.REJECTED_NAME, p.getApplicationById(1).getState());
		// test REJECTED to SUBMITTED
		p.executeCommand(1, resubmit);
		assertEquals(Application.SUBMITTED_NAME, p.getApplicationById(1).getState());
		// test SUBMITTED to REVIEWING
		p.executeCommand(1, assign);
		assertEquals(Application.REVIEWING_NAME, p.getApplicationById(1).getState());
		// test REVIEWING to SUBMITTED
		p.executeCommand(1, cmdReturn);
		assertEquals(Application.SUBMITTED_NAME, p.getApplicationById(1).getState());
		// test SUBMITTED back to REVIEWING with new reviewer
		p.executeCommand(1, assign2);
		assertEquals("reviewer2", p.getApplicationById(1).getReviewer());
		assertEquals(Application.REVIEWING_NAME, p.getApplicationById(1).getState());
		// test REVIEWING to REJECTED 
		p.executeCommand(1, reject);
		assertEquals(Application.REJECTED_NAME, p.getApplicationById(1).getState());
		// go back to REVIEWING
		p.executeCommand(1, resubmit);
		p.executeCommand(1, assign);
		// test REVIEWING to INTERVIEWING
		p.executeCommand(1, schedule);
		assertEquals(Application.INTERVIEWING_NAME, p.getApplicationById(1).getState());
		// test INTERVIEWING to REJECTED
		p.executeCommand(1, reject);
		assertEquals(Application.REJECTED_NAME, p.getApplicationById(1).getState());
		// go back to INTERVIEWING
		p.executeCommand(1, resubmit);
		p.executeCommand(1, assign);
		p.executeCommand(1, schedule);
		// test INTERVIEWING to REVIEWING
		p.executeCommand(1, assign2);
		assertEquals("reviewer2", p.getApplicationById(1).getReviewer());
		assertEquals(Application.REVIEWING_NAME, p.getApplicationById(1).getState());
		// to back to INTERVIEWING
		p.executeCommand(1, schedule);
		// test INTERVIEWING to INTERVIEWING
		p.executeCommand(1, schedule);
		assertEquals(Application.INTERVIEWING_NAME, p.getApplicationById(1).getState());
		// test INTERVIEWING to PROCESSING
		p.executeCommand(1, process);
		assertEquals(Application.PROCESSING_NAME, p.getApplicationById(1).getState());
		// test PROCESSING to REJECTED
		p.executeCommand(1, reject);
		assertEquals(Application.REJECTED_NAME, p.getApplicationById(1).getState());
		// go back to PROCESSING
		p.executeCommand(1, resubmit);
		p.executeCommand(1, assign);
		p.executeCommand(1, schedule);
		p.executeCommand(1, process);
		// test PROCESSING to HIRED
		p.executeCommand(1, hire);
		assertEquals(Application.HIRED_NAME, p.getApplicationById(1).getState());
		// test HIRED to INACTIVE
		p.executeCommand(1, terminate);
		assertEquals(Application.INACTIVE_NAME, p.getApplicationById(1).getState());
	}
	
	/**
	 * Tests deleting multiple applications from list through deleteApplicationsById() method, as well as attempting to delete applications with IDs that DNE.
	 */
	@Test
	public void testDeleteApplicationById() {
		// reset counter for testing purposes
		Application.setCounter(0);
		
		// add testable applications to position
		Application a1 = new Application(1, Application.PROCESSING_NAME, "Javon", "Hader", "jhader2", "reviewer", null);
		Application a2 = new Application(2, Application.INTERVIEWING_NAME, "Delaney", "Brooks", "dbrooks1", "reviewer", null);
		Application a3 = new Application(3, Application.REVIEWING_NAME, "Donna", "Sigma", "dsigma2", "reviewer", null);
		Application a4 = new Application("Jimmy", "Constantine", "jconstant");
		assertEquals(4, a4.getId());
		// add applications to list	
		p.addApplication(a1);
		p.addApplication(a2);
		p.addApplication(a3);
		p.addApplication(a4);	
		// check list
		assertEquals(4, p.getApplications().size());
		assertEquals(a1, p.getApplications().get(0));
		assertEquals(a2, p.getApplications().get(1));
		assertEquals(a3, p.getApplications().get(2));
		assertEquals(a4, p.getApplications().get(3));
		
		// test deleting application that doesn't exist in list -> list should not change
		p.deleteApplicationById(7);
		assertEquals(4, p.getApplications().size());
		
		// test deleting item from middle
		p.deleteApplicationById(2);
		assertEquals(3, p.getApplications().size());
		assertEquals(a1, p.getApplications().get(0));
		assertEquals(a3, p.getApplications().get(1));
		assertEquals(a4, p.getApplications().get(2));
		
		// test deleting from end
		p.deleteApplicationById(3);
		assertEquals(2, p.getApplications().size());
		assertEquals(a1, p.getApplications().get(0));
		assertEquals(a4, p.getApplications().get(1));
		
		// test deleting item from front
		p.deleteApplicationById(1);
		assertEquals(1, p.getApplications().size());
		assertEquals(a4, p.getApplications().get(0));
		
		// test deleting all items from list
		p.deleteApplicationById(4);
		assertEquals(0, p.getApplications().size());
	}
	
	/**
	 * Tests that toString() is formatting the strings correctly
	 */
	@Test
	public void testToString() {
		// reset counter for testing purposes
		Application.setCounter(0);
		
		
		// Recall format:
		// # PositionName,hoursPerWeek,payRate
		// * id,state,fistName,surname,unityId,reviewerId,note
		// * ...
		
		// make testable applications and add to list
		Application a1 = new Application("Jimmy", "Constantine", "jconstant");
		Application a2 = new Application(2, Application.PROCESSING_NAME, "Javon", "Hader", "jhader2", "reviewer", null);
		Application a3 = new Application(3, Application.INTERVIEWING_NAME, "Delaney", "Brooks", "dbrooks1", "reviewer", null);
		Application a4 = new Application(4, Application.REVIEWING_NAME, "Donna", "Sigma", "dsigma2", "reviewer", null);
		Application a5 = new Application(5, Application.INACTIVE_NAME, "Howard", "Scarborough", "hscarbor", "reviewer", Application.RESIGNED_TERMINATION);
		
		p.addApplication(a1);
		p.addApplication(a2);
		p.addApplication(a3);
		p.addApplication(a4);
		p.addApplication(a5);
		assertEquals(5, p.getApplications().size());
		
		// create expected toString() return String
		String expected = "# positionName,10,20\n* 1,Submitted,Jimmy,Constantine,jconstant,,\n"
				+ "* 2,Processing,Javon,Hader,jhader2,reviewer,\n* 3,Interviewing,Delaney,Brooks,dbrooks1,reviewer,\n"
				+ "* 4,Reviewing,Donna,Sigma,dsigma2,reviewer,\n* 5,Inactive,Howard,Scarborough,hscarbor,reviewer,Resigned\n";
		
		// test
		assertEquals(expected, p.toString());
	}
	
	/**
	 * TS test created locally for debugging purposes.
	 * Tests that counter is being updated correctly when adding applications to positions.
	 */
	@Test
	public void testTS() {
		// reset counter for testing purposes
		Application.setCounter(0);
				
		// 1. Check a Position starts with no applications
		assertEquals(0, p.getApplications().size());
		
		// 2. addApplication("Carol", "Schmidt", "cschmid").
		p.addApplication("Carol", "Schmidt", "cschmid");
		// Returned id should be 1 bc counter is set to 1.
		assertEquals(1, p.getApplications().get(0).getId());
		// List of Application has a length of 1
		assertEquals(1, p.getApplications().size());
		// 3. Check that new application with id 1 has correct information.
		assertEquals("Carol", p.getApplicationById(1).getFirstName());
		assertEquals("Schmidt", p.getApplicationById(1).getSurname());
		assertEquals("cschmid", p.getApplicationById(1).getUnityId());
		// 4. addApplication("Fiona", "Rosario", "frosari').
		p.addApplication("Fiona", "Rosario", "frosari");
		// (my own checks)
		assertEquals("Fiona", p.getApplications().get(1).getFirstName());
		assertEquals("Rosario", p.getApplications().get(1).getSurname());
		assertEquals("frosari", p.getApplications().get(1).getUnityId());
		// Returned id should be 2 bc previous application had an id of 1.
		assertEquals(2, p.getApplications().get(1).getId());
		// List of Application has a length of 2
		assertEquals(2, p.getApplications().size());
	}
}
