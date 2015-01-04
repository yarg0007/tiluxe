/*****************************************************************************
 *                        Copyright ASB Technologies (c) 2011
 *                               Java Source
 *
 * This source is the property of ASB Technologies. Any duplication or reuse
 * without the consent of ASB Technologies is prohibited.
 *
 ****************************************************************************/

package com.asbtechnologies.android.tiluxe.manager;

/**
 * Listener for GameManager changes. GameManagerListeners can be registered with
 * the GameManager and will get notified when various game activities occur. 
 *
 * @author Ben Yarger
 * @version $Revision: 1.2 $
 */
public interface GameManagerListener {
	
	//--------------------------------------------------------------------------
	// Game listener messages
	//--------------------------------------------------------------------------
	
	/** 
	 * Signal that the GameManager is fully set up and ready to play a game. 
	 * When this message is sent, playNewGame() can be called to start the game. 
	 */
	public static final int GM_READY = 0;
	
	/** Game over message, not won. */
	public static final int GM_GAME_OVER = 1;
	
	/** Game over, game was won. */
	public static final int GM_GAME_WON = 2;
	
	/** The game started. */
	public static final int GM_STARTED = 3;
	
	/** The game is paused. */
	public static final int GM_PAUSED = 4;
	
	/** The game restarted. After a pause */
	public static final int GM_RESTARTED = 5;
	
	/** A game is already in progress. */
	public static final int GM_IN_PROGRESS = 7;
	
	//--------------------------------------------------------------------------
	// Method definitions
	//--------------------------------------------------------------------------

	/**
	 * Receives notification from GameManager with a message value. Message 
	 * value can be one of the message constants defined by GameManagerListener.
	 * 
	 * @param message Message value from GameManagerListener.
	 */
	public void gameManagerNotification(int message);
}
