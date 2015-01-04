/*****************************************************************************
 *                        Copyright ASB Technologies (c) 2011
 *                               Java Source
 *
 * This source is the property of ASB Technologies. Any duplication or reuse
 * without the consent of ASB Technologies is prohibited.
 *
 ****************************************************************************/

package com.asbtechnologies.android.tiluxe.board;

// External Imports

// Internal Imports

/**
 * Interface that describes board pieces. Board pieces occupy space on the 
 * board, can have length and orientation.
 * 
 * @author Ben Yarger
 * @version $Revision: 1.5 $
 */
public interface BoardPiece {
	
	/** Horizontal piece orientation constant. */
	public static final int HORIZONTAL_ORIENTATION = 0;
	
	/** Vertical piece orientation constant. */
	public static final int VERTICAL_ORIENTATION = 1;
	
	/**
	 * Get the unique identification number for the piece.
	 * 
	 * @return Unique idenfication number.
	 */
	public int getPieceID();
	
	/**
	 * Get the unique identification number for the group of BoardPieces this
	 * BoardPiece belongs to.
	 * 
	 * @return Unique identification number
	 */
	public int getGroupID();
	
	/**
	 * Get the location of the piece on the board.
	 * 
	 * @return Position on the board [0] = x, [1] = y
	 */
	public int[] getLocation();
	
	/**
	 * Get the orientation of the piece on the board, either horizontal or
	 * vertical.
	 * 
	 * @return HORIZONTAL_ORIENTATION or VERTICAL_ORIENTATION
	 */
	public int getOrientation();

	/**
	 * Get the length of the piece on the board.
	 * 
	 * @return The length of the piece on the board
	 */
	public int getLength();
	
	/**
	 * Get the sub pieces of the BoardPiece.
	 * 
	 * @return Array of sub BoardPieces
	 */
	public BoardPiece[] getSubPieces();
}
