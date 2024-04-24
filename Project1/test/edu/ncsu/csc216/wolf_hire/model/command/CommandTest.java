/**
 * 
 */
package edu.ncsu.csc216.wolf_hire.model.command;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;

import edu.ncsu.csc216.wolf_hire.model.command.Command.CommandValue;

/**
 * Tests that Command and CommandValue classes have been appropriately constructed and 
 * meet system requirements for functionality.
 * 
 * @author hmreese2
 *
 */
public class CommandTest {

	/**
	 * Tests constructing command with invalid fields
	 */
	@Test
	public void testCommandInvalid() {
		// test null command
		Exception e1 = assertThrows(IllegalArgumentException.class, 
				() -> new Command(null, "Submitted"));
		assertEquals("Invalid information.", e1.getMessage());
		
		// test null/empty commandInformaation with ASSIGN,REJECT,TERMINATE
		Exception e2 = assertThrows(IllegalArgumentException.class, 
				() -> new Command(CommandValue.ASSIGN, null));
		assertEquals("Invalid information.", e2.getMessage());
		Exception e3 = assertThrows(IllegalArgumentException.class, 
				() -> new Command(CommandValue.ASSIGN, ""));
		assertEquals("Invalid information.", e3.getMessage());
		
		Exception e4 = assertThrows(IllegalArgumentException.class, 
				() -> new Command(CommandValue.REJECT, null));
		assertEquals("Invalid information.", e4.getMessage());
		Exception e5 = assertThrows(IllegalArgumentException.class, 
				() -> new Command(CommandValue.REJECT, null));
		assertEquals("Invalid information.", e5.getMessage());
		
		Exception e6 = assertThrows(IllegalArgumentException.class, 
				() -> new Command(CommandValue.TERMINATE, null));
		assertEquals("Invalid information.", e6.getMessage());
		Exception e7 = assertThrows(IllegalArgumentException.class, 
				() -> new Command(CommandValue.TERMINATE, null));
		assertEquals("Invalid information.", e7.getMessage());
		
		// test non-null commandInformation with RESUBMIT,RETURN,SCHEDULE,PROCESS,HIRE
		Exception e8 = assertThrows(IllegalArgumentException.class, 
				() -> new Command(CommandValue.RESUBMIT, "Resubmit"));
		assertEquals("Invalid information.", e8.getMessage());
		Exception e9 = assertThrows(IllegalArgumentException.class, 
				() -> new Command(CommandValue.RESUBMIT, "Resubmit"));
		assertEquals("Invalid information.", e9.getMessage());
		
		Exception e10 = assertThrows(IllegalArgumentException.class, 
				() -> new Command(CommandValue.RETURN, "Return"));
		assertEquals("Invalid information.", e10.getMessage());
		Exception e11 = assertThrows(IllegalArgumentException.class, 
				() -> new Command(CommandValue.RETURN, "Return"));
		assertEquals("Invalid information.", e11.getMessage());
		
		Exception e12 = assertThrows(IllegalArgumentException.class, 
				() -> new Command(CommandValue.SCHEDULE, "Schedule"));
		assertEquals("Invalid information.", e12.getMessage());
		Exception e13 = assertThrows(IllegalArgumentException.class, 
				() -> new Command(CommandValue.SCHEDULE, "Schedule"));
		assertEquals("Invalid information.", e13.getMessage());
		
		Exception e14 = assertThrows(IllegalArgumentException.class, 
				() -> new Command(CommandValue.PROCESS, "Process"));
		assertEquals("Invalid information.", e14.getMessage());
		Exception e15 = assertThrows(IllegalArgumentException.class, 
				() -> new Command(CommandValue.PROCESS, "Process"));
		assertEquals("Invalid information.", e15.getMessage());
		
		Exception e16 = assertThrows(IllegalArgumentException.class, 
				() -> new Command(CommandValue.HIRE, "Hire"));
		assertEquals("Invalid information.", e16.getMessage());
		Exception e17 = assertThrows(IllegalArgumentException.class, 
				() -> new Command(CommandValue.HIRE, "Hire"));
		assertEquals("Invalid information.", e17.getMessage());
		
	}
	
	/**
	 * Tests constructing command with valid CommandValue and String parameter values
	 */
	@Test
	public void testCommandValid() {
		// test non-null/non-empty commandInformation with ASSIGN,REJECT,TERMINATE
		assertDoesNotThrow(() -> new Command(CommandValue.ASSIGN, "Assign"));
		assertDoesNotThrow(() -> new Command(CommandValue.REJECT, "Reject"));
		assertDoesNotThrow(() -> new Command(CommandValue.TERMINATE, "Terminate"));
		
		// test null commandInformation with RESUBMIT,RETURN,SCHEDULE,PROCESS,HIRE
		assertDoesNotThrow(() -> new Command(CommandValue.RESUBMIT, null));
		assertDoesNotThrow(() -> new Command(CommandValue.RETURN, null));
		assertDoesNotThrow(() -> new Command(CommandValue.SCHEDULE, null));
		assertDoesNotThrow(() -> new Command(CommandValue.PROCESS, null));
		assertDoesNotThrow(() -> new Command(CommandValue.HIRE, null));
	}
	
	/**
	 * Tests getting command with getCommand() method
	 */
	@Test
	public void testGetCommand() {
		Command c = new Command(CommandValue.ASSIGN, "Assign");
		assertEquals(CommandValue.ASSIGN, c.getCommand());
	}
	
	/**
	 * Tests getting command information with getCommandInformation() method
	 */
	@Test
	public void testGetCommandInformation() {
		Command c = new Command(CommandValue.ASSIGN, "Assign");
		assertEquals("Assign", c.getCommandInformation());
	}
	
	/**
	 * TS test created locally for debugging purposes.
	 * Tests invalid command information for command value that can only have null commandinformation
	 */
	@Test
	public void testTS() {
		assertThrows(IllegalArgumentException.class,
				() -> new Command(CommandValue.HIRE, "Duplicate"));
		assertDoesNotThrow(() -> new Command(CommandValue.HIRE, null));
		assertThrows(IllegalArgumentException.class,
				() -> new Command(CommandValue.HIRE, ""));
		
	}
}
