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
import java.util.ArrayList;

//Internal Imports
import com.asbtechnologies.android.tiluxe.util.IdGenerator;

/**
 * Default implementation of the board. The board has a set size and some number
 * of board pieces placed on the board. The 0,0 index of the board is presumed 
 * to be in the top left corner of the board.
 * 
 * @author Ben Yarger
 * @version $Revision: 1.17 $
 */
public class DefaultBoard implements Board, BoardPicker, BoardState, 
	BoardPerimeterLegend {
	
	/** Width of the board (number of columns). */
	private int boardWidth;
	
	/** Height of the board (number of rows). */
	private int boardHeight;
	
	/** Playing board */
	private DefaultBoardPiece[][] playingBoard;
	
	/** The last board piece picked. */
	private BoardPiece pickedPiece;
	
	/** The target parent pieces (group) that need to be found. */
	private ArrayList<DefaultBoardPiece> targetParentPieces;
	
	/**
	 * Default constructor, establishes board size and fills it with board 
	 * pieces that must be matched to dead.
	 * 
	 * @param boardWidth Width of the board (number of columns)
	 * @param boardHeight Height of the board (number of rows)
	 */
	public DefaultBoard(int boardWidth, int boardHeight) {
		
		this.boardWidth = boardWidth;
		this.boardHeight = boardHeight;
		
		this.playingBoard = null;
		this.playingBoard = new DefaultBoardPiece[this.boardWidth][this.boardHeight];
		this.pickedPiece = null;
		this.targetParentPieces = new ArrayList<DefaultBoardPiece>();
		
		for (int w = 0; w < boardWidth; w++) {
			for (int h = 0; h <boardHeight; h++) {
				
				playingBoard[w][h] = 
					new DefaultBoardPiece(
							w, 
							h, 
							BoardPiece.HORIZONTAL_ORIENTATION, 
							1, 
							BoardPieceState.STATE_DEAD,
							IdGenerator.getInstance().generateID());
			}
		}
	}
	
	//--------------------------------------------------------------------------
	// Methods required by Board
	//--------------------------------------------------------------------------

	/**
	 * Get the width of the board (number of columns).
	 * 
	 * @return Width of the board
	 */
	@Override
	public int getWidth() {
		
		return boardWidth;
	}

	/**
	 * Get the height of the board (number of rows)
	 * 
	 * @return Height of the board
	 */
	@Override
	public int getHeight() {
		
		return boardHeight;
	}

	/**
	 * Get the pieces on the board. Returns an array containing the pieces on 
	 * the board.
	 * 
	 * @return Multi-dimensional copy of playing board
	 */
	@Override
	public BoardPiece[][] getBoardPieces() {

		DefaultBoardPiece[][] boardPieceSet = 
			new DefaultBoardPiece[boardWidth][boardHeight];
		
		for (int w = 0; w < boardWidth; w++) {
			for (int h = 0; h < boardHeight; h++) {
				
				boardPieceSet[w][h] = playingBoard[w][h];
			}
		}
		
		return boardPieceSet;
	}
	
	/* (non-Javadoc)
	 * @see com.asbtechnologies.android.tiluxe.board.Board#getParentTargetPieces()
	 */
	@Override
	public BoardPiece[] getParentTargetPieces() {
		
		DefaultBoardPiece[] tmpPieces = 
			new DefaultBoardPiece[targetParentPieces.size()];
		
		targetParentPieces.toArray(tmpPieces);
		
		return tmpPieces;
	}

	/**
	 * Add a piece to the board. The board is populated with the board sub 
	 * pieces. If there are no board sub pieces, then the piece itself will be 
	 * added to the board. The add operation will overwrite existing board 
	 * pieces.
	 * 
	 * @param Board piece to add
	 */
	@Override
	public void addBoardPiece(BoardPiece boardPiece) {
		
		if (((DefaultBoardPiece)boardPiece).getMatchState() == BoardPieceState.STATE_ALIVE) {
			targetParentPieces.add((DefaultBoardPiece)boardPiece);
		}
		BoardPiece[] subPieces = boardPiece.getSubPieces();
		
		// The pos array we get from the boardPiece has two indices.
		// 0 - The x position
		// 1 - The y position
		int[] pos;

		if (subPieces == null || subPieces.length == 1) {
			
			pos = boardPiece.getLocation();
			playingBoard[pos[0]][pos[1]] = (DefaultBoardPiece) boardPiece;
			
		} else {
			
			for (int i = 0; i < subPieces.length; i++) {
				
				pos = subPieces[i].getLocation();
				playingBoard[pos[0]][pos[1]] = (DefaultBoardPiece) subPieces[i];
			}
			
		}
		
	}

	/**
	 * Remove a piece from the board. All pieces with the matching group id 
	 * of the piece to be removed will also be removed. Removing a board piece
	 * sets that board location to an EMPTY board piece with a group id of -1.
	 * 
	 * @param Board piece to remove
	 */
	@Override
	public void removeBoardPiece(BoardPiece boardPiece) {
		
		targetParentPieces.remove((DefaultBoardPiece) boardPiece);
		DefaultBoardPiece tempPiece;
		
		for (int w = 0; w < boardWidth; w++) {
			for (int h = 0; h < boardHeight; h++) {
				if (playingBoard[w][h].getGroupID() == boardPiece.getGroupID()) {
					
					tempPiece = (DefaultBoardPiece) playingBoard[w][h];
					
					playingBoard[w][h] = new DefaultBoardPiece(
							w, 
							h, 
							tempPiece.getOrientation(), 
							tempPiece.getLength(), 
							BoardPieceState.EMPTY, 
							-1);
					
				}
			}
		}
	}
	
	/**
	 * Print out the board. Show pieces that must match the alive state. Include
	 * legends in output.
	 */
	@Override
	public void printBoard() {
		
		String rowData;
		int[] verticalLegend = getVerticalLegend();
		int[] horizontalLegend = getHorizontalLegend();
		
		if (verticalLegend == null || verticalLegend.length != boardHeight) {
			
			throw new ArrayIndexOutOfBoundsException(
					"verticalLegend does not match  board height for "+
					"the purpose of printing the key.");
			
		} else if (horizontalLegend == null || 
				horizontalLegend.length != boardWidth) {
			
			throw new ArrayIndexOutOfBoundsException(
					"horizontalLegend does not match  board height for "+
					"the purpose of printing the key.");
		}
		
		for (int h = 0; h < boardHeight; h++) {
			
			rowData = "";
			
			for (int w = 0; w < boardWidth; w++) {
				
				if (((BoardPieceState)playingBoard[w][h]).getMatchState() == 
					BoardPieceState.STATE_ALIVE) {
					rowData = rowData + "X ";
				} else if (((BoardPieceState)playingBoard[w][h]).getMatchState()
						== BoardPieceState.EMPTY) {
					rowData = rowData + "E ";
				} else {
					rowData = rowData + "0 ";
				}
			}
			
			rowData = rowData + "|" +  verticalLegend[h]+"\n";
//			Log.i("PrintBoard", rowData);
		}
		
		rowData = "";
		for (int w = 0; w < horizontalLegend.length; w++) {
			rowData += "--";
		}
		
//		Log.i("PrintBoard", rowData);
		
		rowData = "";
		for (int w = 0; w < horizontalLegend.length; w++) {
			rowData += horizontalLegend[w] + " ";
		}
		
//		Log.i("PrintBoard", rowData);
	}
	
	/* (non-Javadoc)
	 * @see com.asbtechnologies.android.tiluxe.board.Board#resetBoard()
	 */
	@Override
	public void resetBoard() {
		
		int startingState;
		
		for (int w = 0; w < boardWidth; w++) {
			for (int h = 0; h < boardHeight; h++) {
				startingState = playingBoard[w][h].getStartingState();
				
				if (startingState == BoardPieceState.STATE_ALIVE) {
					playingBoard[w][h].setState(BoardPieceState.STATE_ALIVE);
				} else if (startingState == BoardPieceState.STATE_LIMBO) {
					playingBoard[w][h].setState(BoardPieceState.STATE_LIMBO);
				}
			}
		}
	}

	//--------------------------------------------------------------------------
	// Methods required by BoardPicker
	//--------------------------------------------------------------------------
	
	/**
	 * Pick a location on the board, and return true if there was a piece there.
	 * Board locations start at index [0][0] in the top left corner of the 
	 * board.
	 * 
	 * @param xBoardLocation Horizontal column number
	 * @param yBoardLocation Vertical row number
	 * @return True if a BoardPiece was picked, false otherwise
	 */
	@Override
	public boolean pickBoardLocation(int xBoardLocation, int yBoardLocation) {
		
		pickedPiece = null;
		
		// Must be an appropriately bounded check
		if (xBoardLocation > boardWidth || xBoardLocation < 0) {
			return false;
		} else if (yBoardLocation > boardHeight || xBoardLocation < 0) {
			return false;
		}
		
		pickedPiece = playingBoard[xBoardLocation][yBoardLocation];
		
		// If the piece picked was not set, return null
		if (pickedPiece == null) {
			return false;
		}
		
		return true;
	}

	/**
	 * Get the last BoardPiece picked.
	 * 
	 * @return BoardPiece that was picked, otherwise null
	 */
	@Override
	public BoardPiece getLastPick() {

		return pickedPiece;
	}
	
	/* (non-Javadoc)
	 * @see com.asbtechnologies.android.tiluxe.board.BoardPicker#pickByID(int)
	 */
	public BoardPiece pickByID(int pieceID) {
		
		pickedPiece = null;
		
		for (int w = 0; w < boardWidth; w++) {
			for (int h = 0; h < boardHeight; h++) {
				if (playingBoard[w][h].getPieceID() == pieceID) {
					pickedPiece = playingBoard[w][h];
					return pickedPiece;
				}
			}
		}
		
		return pickedPiece;
	}
	
	//--------------------------------------------------------------------------
	// Methods required by BoardState
	//--------------------------------------------------------------------------
	
	/**
	 * See if the game is over.
	 * 
	 * @return True if the game is over, false otherwise
	 */
	@Override
	public boolean isGameOver() {

		// The only way a game is considered over is if either...
		// A) Only the pieces that should be marked alive are set to alive.
		// B) Only the pieces that should be in limbo or dead are marked dead.
		// Eliminating option B for the time being.
		
		// Check for case A - alive check
		boolean caseACorrect = true;
		int currentAState = -1;
		int matchAState = -1;
		
		for (int h = 0; h < boardHeight; h++) {
			for (int w = 0; w < boardWidth; w++) {
				
				// Expectation is that a piece that should be in limbo or dead
				// is not marked as alive, and that a piece that should be 
				// marked alive is marked alive.
				currentAState = 
					((BoardPieceState)playingBoard[w][h]).getCurrentState();
				
				matchAState =
					((BoardPieceState)playingBoard[w][h]).getMatchState();

				if ((matchAState == BoardPieceState.STATE_LIMBO || 
						matchAState == BoardPieceState.STATE_DEAD) && 
						currentAState == BoardPieceState.STATE_ALIVE) {
					
					caseACorrect = false;
					break;
					
				} else if (matchAState == BoardPieceState.STATE_ALIVE && 
						currentAState != BoardPieceState.STATE_ALIVE) {
					
					caseACorrect = false;
					break;
					
				}
			}
			
			if (!caseACorrect) {
				break;
			}
		}
/*		
		// Check for case B - dead check
		boolean caseBCorrect = true;
		int currentBState = -1;
		int matchBState = -1;
		
		for (int h = 0; h < boardHeight; h++) {
			for (int w = 0; w < boardWidth; w++) {
				
				// Expectation is that a piece that should be in limbo or alive
				// is not marked as dead and that a piece that should be marked
				// dead is marked dead.
				currentBState = 
					((BoardPieceState)playingBoard[w][h]).getCurrentState();
				
				matchBState =
					((BoardPieceState)playingBoard[w][h]).getMatchState();

				if ((matchBState == BoardPieceState.STATE_LIMBO || 
						matchBState == BoardPieceState.STATE_ALIVE) && 
						currentBState == BoardPieceState.STATE_DEAD) {
					
					caseBCorrect = false;
					break;
					
				} else if (matchBState == BoardPieceState.STATE_DEAD && 
						currentBState != BoardPieceState.STATE_DEAD) {
					
					caseBCorrect = false;
					break;
					
				}
			}
			
			if (!caseBCorrect) {
				break;
			}
		}
*/		
//		if (caseACorrect || caseBCorrect) {
		if (caseACorrect) {
			return true;
		}
		
		return false;
	}

	//--------------------------------------------------------------------------
	// Methods required by BoardPerimeterLegend
	//--------------------------------------------------------------------------
	
	/**
	 * Get the vertical legend for the Board. This shows how many pieces are to
	 * be discovered in each row of the Board.
	 * 
	 * @return Array of int values tied to each row of the board
	 */
	@Override
	public int[] getVerticalLegend() {
		
		int[] verticalLegend = new int[boardHeight];
		
		for (int h = 0; h < boardHeight; h++) {
			
			verticalLegend[h] = 0;
			
			for (int w = 0; w < boardWidth; w++) {
				
				if (((BoardPieceState)playingBoard[w][h]).getMatchState() == 
					BoardPieceState.STATE_ALIVE) {
					
					verticalLegend[h]++;
				}
			}
		}
		
		return verticalLegend;
	}

	/**
	 * Get the horizontal legend for the Board. This shows how many pieces are 
	 * to be discovered in each column of the Board.
	 * 
	 * @return Array of int values tied to each column of the board
	 */
	@Override
	public int[] getHorizontalLegend() {
		
		int[] horizontalLegend = new int[boardWidth];
		
		for (int w = 0; w < boardWidth; w++) {
			
			horizontalLegend[w] = 0;
			
			for (int h = 0; h < boardHeight; h++) {
				
				if (((BoardPieceState)playingBoard[w][h]).getMatchState() == 
					BoardPieceState.STATE_ALIVE) {
					
					horizontalLegend[w]++;
				}
			}
		}
		
		return horizontalLegend;
	}

	/**
	 * Get the vertical legend hint values for the Board. These correlate with 
	 * the rows and the values can be: HINT_ALL_CORRECT, HINT_ALMOST_CORRECT, 
	 * HINT_TOO_FEW, HINT_TOO_MANY.
	 * 
	 * HINT_ALL_CORRECT -> correct number and correct locations
	 * HINT_ALMOST_CORRECT -> correct number but incorrect locations
	 * HINT_TOO_FEW -> more pieces that must match alive than have been set
	 * HINT_TOO_MANY -> fewer pieces that must match alive than have been set
	 * 
	 * @return Hint value
	 */
	@Override
	public int[] getVerticalLegendHint() {
		
		// Get the vertical legend so we know how many pieces we are trying to 
		// match against. Then determine how many in each row are set to alive
		// and compare that against the expectation to get our hint value.
		int[] verticalHintLegend = getVerticalLegend();
		int numberOfAliveSet = 0;
		boolean correctLocations = true;
		
		for (int h = 0; h < boardHeight; h++) {		
			
			numberOfAliveSet = 0;
			correctLocations = true;
			
			for (int w = 0; w < boardWidth; w++) {
				
				if (((BoardPieceState)playingBoard[w][h]).getCurrentState() == 
					BoardPieceState.STATE_ALIVE) {
					
					numberOfAliveSet++;
					
					if (!((BoardPieceState)
							playingBoard[w][h]).isStateCorrect()) {
						
						correctLocations = false;
					}
				}
			}
			
			// Replace the verticalHintLegend value with the correct hint val
			if (verticalHintLegend[h] == numberOfAliveSet && 
					correctLocations) {
				
				verticalHintLegend[h] = BoardPerimeterLegend.HINT_ALL_CORRECT;
				
			} else if (verticalHintLegend[h] == numberOfAliveSet && 
					!correctLocations) {
				
				verticalHintLegend[h] = 
					BoardPerimeterLegend.HINT_ALMOST_CORRECT;
				
			} else if (verticalHintLegend[h] > numberOfAliveSet) {
				
				verticalHintLegend[h] = BoardPerimeterLegend.HINT_TOO_FEW;
				
			} else if (verticalHintLegend[h] < numberOfAliveSet) {
				
				verticalHintLegend[h] = BoardPerimeterLegend.HINT_TOO_MANY;
				
			}
			
//			Log.i("VERTICAL LEGEND HINT CALCULATION ROW: "+h,""+verticalHintLegend[h]);
		}
		
		return verticalHintLegend;
	}

	/**
	 * Get the horizontal legend hint values for the Board. These correlate with 
	 * the columns and the values can be: HINT_ALL_CORRECT, HINT_ALMOST_CORRECT, 
	 * HINT_TOO_FEW, HINT_TOO_MANY.
	 * 
	 * HINT_ALL_CORRECT -> correct number and correct locations
	 * HINT_ALMOST_CORRECT -> correct number but incorrect locations
	 * HINT_TOO_FEW -> more pieces that must match alive than have been set
	 * HINT_TOO_MANY -> fewer pieces that must match alive than have been set
	 * 
	 * @return Hint value
	 */
	@Override
	public int[] getHorizontalLegendHint() {
		
		// Get the horizontal legend so we know how many pieces we are trying to 
		// match against. Then determine how many in each column are set to 
		// alive and compare that against the expectation to get our hint value.
		int[] horizontalHintLegend = getHorizontalLegend();
		int numberOfAliveSet = 0;
		boolean correctLocations = true;
		
		for (int w = 0; w < boardWidth; w++) {		
			
			numberOfAliveSet = 0;
			correctLocations = true;
			
			for (int h = 0; h < boardHeight; h++) {
				
				if (((BoardPieceState)playingBoard[w][h]).getCurrentState() == 
					BoardPieceState.STATE_ALIVE) {
					
					numberOfAliveSet++;
					
					if (!((BoardPieceState)
							playingBoard[w][h]).isStateCorrect()) {
						
						correctLocations = false;
					}
				}
			}

			// Replace the horizontalHintLegend value with the correct hint val
			if (horizontalHintLegend[w] == numberOfAliveSet && 
					correctLocations) {
				
				horizontalHintLegend[w] = BoardPerimeterLegend.HINT_ALL_CORRECT;
				
			} else if (horizontalHintLegend[w] == numberOfAliveSet && 
					!correctLocations) {
				
				horizontalHintLegend[w] = 
					BoardPerimeterLegend.HINT_ALMOST_CORRECT;
				
			} else if (horizontalHintLegend[w] > numberOfAliveSet) {
				
				horizontalHintLegend[w] = BoardPerimeterLegend.HINT_TOO_FEW;
				
			} else if (horizontalHintLegend[w] < numberOfAliveSet) {
				
				horizontalHintLegend[w] = BoardPerimeterLegend.HINT_TOO_MANY;
				
			}
		}
		
		return horizontalHintLegend;
	}

}
