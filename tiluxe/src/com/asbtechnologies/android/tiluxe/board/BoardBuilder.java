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
 * Interface that describes the board builder. The board builder sets up the 
 * board, including the board size, number and location of pieces, etc.
 * 
 * @author Ben Yarger
 * @version $Revision: 1.1 $
 */
public interface BoardBuilder {

	/**
	 * Generate a board with a default layout.
	 * 
	 * @return Default board
	 */
	public Board fillDefaultBoard();
	
	/**
	 * Generate a board with the pieces specified. Creates the board according
	 * to the board width and height specified.
	 * 
	 * @param boardWidth Width of the board (number of columns)
	 * @param boardHeight Height of the board (number of rows)
	 * @param pieces The pieces to place on the board via the fill operation
	 * @return Custom board
	 */
	public Board fillBoard(
			int boardWidth, 
			int boardHeight, 
			BoardPiece[] boardPieces);
}
