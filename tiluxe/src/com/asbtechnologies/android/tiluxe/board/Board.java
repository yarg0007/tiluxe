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
 * Interface that describes the board. The board has a set size and some number
 * of board pieces placed on the board. The 0,0 index of the board is presumed 
 * to be in the top left corner of the board.
 * 
 * @author Ben Yarger
 * @version $Revision: 1.5 $
 */
public interface Board {

	/**
	 * Get the width of the board.
	 * 
	 * @return Board width
	 */
	public int getWidth();
	
	/**
	 * Get the height of the board.
	 * 
	 * @return Board height
	 */
	public int getHeight();
	
	/**
	 * Get the pieces on the board.
	 * 
	 * @return Multi-dimensional copy of playing board
	 */
	public BoardPiece[][] getBoardPieces();
	
	/**
	 * Get the parent target pieces that contain the individual sub pieces to 
	 * find.
	 * 
	 * @return Parent board pieces.
	 */
	public BoardPiece[] getParentTargetPieces();
	
	/**
	 * Add a new piece to the board.
	 * 
	 * @param boardPiece Piece to add to the board
	 */
	public void addBoardPiece(BoardPiece boardPiece);
	
	/**
	 * Remove a piece from the board.
	 * 
	 * @param boardPiece Piece to remove from the board
	 */
	public void removeBoardPiece(BoardPiece boardPiece);
	
	/**
	 * Print out the board.
	 */
	public void printBoard();
	
	/**
	 * Reset the board to the starting state.
	 */
	public void resetBoard();
}
