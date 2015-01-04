/*****************************************************************************
 *                        Copyright ASB Technologies (c) 2011
 *                               Java Source
 *
 * This source is the property of ASB Technologies. Any duplication or reuse
 * without the consent of ASB Technologies is prohibited.
 *
 ****************************************************************************/

package com.asbtechnologies.android.tiluxe.board;

public interface BoardPieceState {

	/** Empty BoardPiece. Could be a place holder or an empty square. */
	public static final int EMPTY = -1;
	
	/** State flag for alive BoardPiece. */
	public static final int STATE_ALIVE = 0;
	
	/** State flag for dead BoardPiece. */
	public static final int STATE_DEAD = 1;
	
	/** State flag for in limbo BoardPiece. */
	public static final int STATE_LIMBO = 2;
	
	/** Check if the BoardPiece is empty. */
	public boolean isEmpty();
	
	/**
	 * Check if the BoardPiece is alive. If it isn't alive, that does not 
	 * guarantee dead.
	 * 
	 * @return True if alive, false otherwise
	 */
	public boolean isAlive();
	
	/**
	 * Check if the BoardPiece is dead. If it isn't dead, that does not 
	 * guarantee alive.
	 * 
	 * @return True if dead, false otherwise
	 */
	public boolean isDead();
	
	/**
	 * Check if the BoardPiece is in limbo. If it isn't in limbo, that does not
	 * guarantee alive or dead.
	 * 
	 * @return True if in limbo, false otherwise
	 */
	public boolean isInLimbo();
	
	/**
	 * Set the state of the BoardPiece.
	 * 
	 * @param boardState BoardPieceState value
	 */
	public void setState(int boardState);
	
	/**
	 * Set the state of the BoardPiece and lock in the default state for 
	 * resetting at a later time.
	 * 
	 * @param boardState
	 */
	public void setDefaultState(int boardState);
	
	/**
	 * Get the current state of the BoardPiece.
	 * 
	 * @return Board state
	 */
	public int getCurrentState();
	
	/**
	 * Is the current state of all the group's sub pieces correct.
	 * 
	 * @return True if all the group's sub pieces have the correct state.
	 */
	public boolean isGroupStateCorrect();
	
	/**
	 * Is the current state of the BoardPiece the correct state given some 
	 * set of expected parameters for correctness. In other words, does the 
	 * current state of the BoardPiece meet the internally expected state of
	 * the BoardPiece set?
	 * 
	 * @return True if BoardPiece is correct, false otherwise
	 */
	public boolean isStateCorrect();
	
	/**
	 * Get the state that needs to be matched in order to have the board piece
	 * set correctly.
	 * 
	 * @return Board piece state that needs to be matched
	 */
	public int getMatchState();
	
	/**
	 * Cycle to the next state for the piece.
	 */
	public void setNextState();
	
	/**
	 * Get the game starting state for the tile. STATE_ALIVE = started on, 
	 * STATE_DEAD = free turned off tile, STATE_LIMBO = default colored tile.
	 * 
	 * @return STATE_ALIVE, STATE_DEAD or STATE_LIMBO
	 */
	public int getStartingState();
}
