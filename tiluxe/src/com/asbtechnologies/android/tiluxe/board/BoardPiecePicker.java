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
* Interface that describes the handling of board piece picking based on board
* locations. The 0,0 index of the board is presumed to be in the top left 
* corner of the board.
* 
* @author Ben Yarger
* @version $Revision: 1.2 $
*/
public interface BoardPiecePicker {

	/**
	 * See if the piece was picked based on the x and y board positions. X 
	 * relates to column number, y relates to row number.
	 * 
	 * @param xPickPos Column position picked
	 * @param yPickPos Row position picked
	 */
	public boolean pickPiece(int xPickPos, int yPickPos);
	
	/**
	 * Get the piece that was picked.
	 * 
	 * @param xPickPos Column position picked
	 * @param yPickPos Row position picked
	 * @return BoardPiece picked at that location, null if none picked
	 */
	public BoardPiece getPickPiece(int xPickPos, int yPickPos);
}
