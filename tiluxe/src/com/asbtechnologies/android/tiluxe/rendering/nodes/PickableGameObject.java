/*****************************************************************************
 *                        Copyright ASB Technologies (c) 2011
 *                               Java Source
 *
 * This source is the property of ASB Technologies. Any duplication or reuse
 * without the consent of ASB Technologies is prohibited.
 *
 ****************************************************************************/

package com.asbtechnologies.android.tiluxe.rendering.nodes;

// External Imports

// Internal Imports

/**
 * Definition for a game object being pickable. In order to perform a pick check
 * game objects must implement this interface.
 *
 * @author Ben Yarger
 * @version $Revision: 1.1 $
 */
public interface PickableGameObject {

	/**
	 * See if the piece was picked based on the board position xPos and yPos.
	 * 
	 * @param motionType The type of motion occurring.
	 * @param xPos X screen position.
	 * @param yPos Y screen position.
	 * @return True if picked, false otherwise.
	 */
	public boolean isPicked(float motionType, float xPos, float yPos);
}
