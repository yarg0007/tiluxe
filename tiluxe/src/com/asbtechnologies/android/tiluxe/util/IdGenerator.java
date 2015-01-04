/*****************************************************************************
 *                        Copyright ASB Technologies (c) 2011
 *                               Java Source
 *
 * This source is the property of ASB Technologies. Any duplication or reuse
 * without the consent of ASB Technologies is prohibited.
 *
 ****************************************************************************/

package com.asbtechnologies.android.tiluxe.util;

//External Imports

//Internal Imports

/**
* Singleton pseudo unique id generator. Each request for a new ID will return 
* the next consecutive positive int value, up to Integer.MAX_VALUE. The next 
* request past Integer.MAX_VALUE will recycle back to 1. All id's start from 1.
* 
* @author Ben Yarger
* @version $Revision: 1.2 $
*/
public class IdGenerator {

	/** Last id value generated. */
	private static int idValue;
	
	/** Internal static reference. */
	private static IdGenerator idgInstance;
	
	/**
	 * Default constructor.
	 */
	private IdGenerator() {
		
		idValue = 0;
	}
	
	/**
	 * Get the instance of this class.
	 * 
	 * @return Class instance
	 */
	public static synchronized IdGenerator getInstance() {
		
		if (idgInstance == null) {
			 idgInstance = new IdGenerator();
		}
		
		return idgInstance;
	}
	
	/**
	 * Generate a new id value. ID values are recycled to 1 after 
	 * Integer.MAX_VALUE.
	 * 
	 * @return ID value
	 */
	public synchronized int generateID() {
		
		if (idValue == Integer.MAX_VALUE) {
			idValue = 0;
		}
		
		idValue++;
		
		return idValue;
	}
	
	/**
	 * For testing purposes, set the id value to Integer.MAX_VALUE
	 */
	public synchronized void setToMaxValue() {
		idValue = Integer.MAX_VALUE;
	}
}
