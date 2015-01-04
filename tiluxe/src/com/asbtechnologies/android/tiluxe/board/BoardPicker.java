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
* Interface that describes board picking. The 0,0 index of the board is presumed 
* to be in the top left corner of the board.
* 
* @author Ben Yarger
* @version $Revision: 1.2 $
*/
public interface BoardPicker {

	/**
	 * Pick a location on the board, and return true if there was a piece there.
	 * 
	 * @param xBoardLocation Horizontal column number
	 * @param yBoardLocation Vertical row number
	 * @return True if a BoardPiece was picked, false otherwise
	 */
	public boolean pickBoardLocation(int xBoardLocation, int yBoardLocation);
	
	/**
	 * Get the last BoardPiece picked.
	 * 
	 * @return BoardPiece that was picked
	 */
	public BoardPiece getLastPick();
	
	/**
	 * Get the piece picked by it's own unique id.
	 * 
	 * @param pieceID Unique piece ID to look up.
	 * @return BoardPiece requested.
	 */
	public BoardPiece pickByID(int pieceID);
}
