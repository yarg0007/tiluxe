/*****************************************************************************
 *                        Copyright ASB Technologies (c) 2011
 *                               Java Source
 *
 * This source is the property of ASB Technologies. Any duplication or reuse
 * without the consent of ASB Technologies is prohibited.
 *
 ****************************************************************************/

package com.asbtechnologies.android.tiluxe.board;

//External Imports

//Internal Imports

/**
* Interface that describes the state of a board. Determines if the game is over.
* 
* @author Ben Yarger
* @version $Revision: 1.2 $
*/
public interface BoardState {

	/**
	 * See if the game is over.
	 * 
	 * @return True if the game is over, false otherwise
	 */
	public boolean isGameOver();

}
