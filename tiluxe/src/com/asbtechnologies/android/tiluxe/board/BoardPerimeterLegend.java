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
* Interface that describes the legend around the board. This is the legend along
* the right and bottom sides of the board that indicate the number of pieces 
* that must be discovered in the respective row or column.
* 
* @author Ben Yarger
* @version $Revision: 1.2 $
*/
public interface BoardPerimeterLegend {
	
	/** Row or column is completely correct. Correct number and position. */
	public static final int HINT_ALL_CORRECT = 0;
	
	/**
	 * Row or column is almost completely correct, correct number but incorrect
	 * positions.
	 */
	public static final int HINT_ALMOST_CORRECT = 1;
	
	/** There are still hidden tiles to be discovered in the row or column. */
	public static final int HINT_TOO_FEW = 2;
	
	/** Too many tiles have been marked in the row or column. */
	public static final int HINT_TOO_MANY = 3;

	/**
	 * Get the vertical legend for the Board. This shows how many pieces are to
	 * be discovered in each row of the Board.
	 * 
	 * @return Array of int values tied to each row of the board
	 */
	public int[] getVerticalLegend();
	
	/**
	 * Get the horizontal legend for the Board. This shows how many pieces are 
	 * to be discovered in each column of the Board.
	 * 
	 * @return Array of int values tied to each column of the board
	 */
	public int[] getHorizontalLegend();
	
	/**
	 * Get the vertical legend hint values for the Board. These correlate with 
	 * the rows and the values can be: HINT_ALL_CORRECT, HINT_ALMOST_CORRECT, 
	 * HINT_TOO_FEW, HINT_TOO_MANY.
	 * 
	 * @return Hint value
	 */
	public int[] getVerticalLegendHint();
	
	/**
	 * Get the horizontal legend hint values for the Board. These correlate with 
	 * the columns and the values can be: HINT_ALL_CORRECT, HINT_ALMOST_CORRECT, 
	 * HINT_TOO_FEW, HINT_TOO_MANY.
	 * 
	 * @return Hint value
	 */
	public int[] getHorizontalLegendHint();
}
