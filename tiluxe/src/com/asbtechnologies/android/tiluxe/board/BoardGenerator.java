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
 * Describes the functionality required of the board generation class. 
 * BoardGenerator implementations are responsible for creating default and 
 * custom playing boards.
 * 
 * @author Ben Yarger
 * @version $Revision: 1.4 $
 */
public interface BoardGenerator {

	/**
	 * Generate the default board.
	 * 
	 * @return Generated Board.
	 */
	public Board generateDefaultBoard();
	
	/**
	 * Generate a custom board based on the parameters. Board width and height
	 * must be appropriate for the boardPieces passed in.
	 * 
	 * @param boardWidth Width of the board
	 * @param boardHeight Height of the board
	 * @param activePieceLengths Piece lengths to be generated on the board.
	 * @param numberOfFreeEmpty Number of empty tiles to give away.
	 * @param numberOfFreeTargets Number of target tiles to give away.
	 * @return Generated Board.
	 */
	public Board generateCustomBoard(
			int boardWidth, 
			int boardHeight, 
			int[] activePieceLengths, 
			int numberOfFreeEmpty,
			int numberOfFreeTargets);
	
	/**
	 * Get the target BoardPieces. These are the BoardPieces that must be 
	 * identified on the board.
	 * 
	 * @return Target BoardPieces.
	 */
	public BoardPiece[] getTargetPieces();
}
