/*****************************************************************************
 *                        Copyright ASB Technologies (c) 2011
 *                               Java Source
 *
 * This source is the property of ASB Technologies. Any duplication or reuse
 * without the consent of ASB Technologies is prohibited.
 *
 ****************************************************************************/

package com.asbtechnologies.android.tiluxe.test;

// External Imports
import com.asbtechnologies.android.tiluxe.util.IdGenerator;

import junit.framework.TestCase;

// Internal Imports

/**
 * Unit tests for IdGenerator.
 * 
 * @author Ben Yarger
 * @version $Revision: 1.2 $
 */
public class TiluxeIdGeneratorTest extends TestCase {

	/**
	 * Default constructor.
	 * 
	 * @param name
	 */
	public TiluxeIdGeneratorTest(String name) {
		super(name);
	}
	
	/**
	 * Confirm that an instance of IdGenerator can be obtained.
	 */
	public void testAGetInstance() {
		
		IdGenerator generatorInstance = IdGenerator.getInstance();
		assertNotNull(generatorInstance);
	}
	
/*	public void testBGetInitialValue() {
		
		assertEquals(1, IdGenerator.getInstance().generateID());
	}
*/	
	/**
	 * Confirm that the full range of values from 2 to Integer.MAX_VALUE and 
	 * back to 1 can be achieved.
	 */
	public void testCFullRangeOfIds() {
		
		IdGenerator.getInstance().setToMaxValue();
		assertEquals(1, IdGenerator.getInstance().generateID());
	}
	
	/**
	 * Confirm sequence of one through ten id values are returned.
	 */
	public void testDOneThroughTenValues() {
		
		for (int i = 2; i < 10; i++) {
			assertEquals(i, IdGenerator.getInstance().generateID());
		}
	}
}
