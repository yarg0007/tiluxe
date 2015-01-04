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
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

// Internal Imports

/**
 * Runs only the basic set of tests. For quick validation, but final validation
 * must pass the comprehensive test.
 * 
 * @author Ben Yarger
 * @version $Revision: 1.2 $
 */
public class TiluxeSuiteQuickTest extends TestCase {

	public static Test suite() {
		
		TestSuite suite = new TestSuite();
		
		// Android specific tests startup test
		suite.addTestSuite(TiluxeStartupTest.class);
		
		// Board specific tests
		suite.addTestSuite(TiluxeBoardPieceTest.class);
		
		// Game specific tests
		
		return suite;
	}
	
}
