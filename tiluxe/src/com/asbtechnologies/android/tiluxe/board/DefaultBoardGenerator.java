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
import java.util.Arrays;
import java.util.Random;

//Internal Imports
import com.asbtechnologies.android.tiluxe.util.IdGenerator;

/**
 * Default implementation of the board generator. The board generator creates a 
 * board and populates it with board pieces.
 * 
 * @author Ben Yarger
 * @version $Revision: 1.10 $
 */
public class DefaultBoardGenerator implements BoardGenerator {
	
	/** Number of times we can attempt to perform an action before it fails. */
	private static final int ATTEMPT_LIMIT = 10;
	
	/**
	 * Default board width.
	 */
	private static final int DEFAULT_BOARD_WIDTH = 10;
	
	/**
	 * Default board height.
	 */
	private static final int DEFAULT_BOARD_HEIGHT = 10;
	
	/**
	 * Default board piece lengths to generate.
	 */
	private static final int[] DEFAULT_PIECE_LENGTHS = 
		new int[] {4, 3, 3, 2, 2, 2, 1, 1, 1, 1};
	
	/**
	 * The board pieces making up the current board.
	 */
	private BoardPiece[] boardPieces;
	
	/**
	 * The playing board.
	 */
	private DefaultBoard playingBoard;
	
	/**
	 * Default constructor.
	 */
	public DefaultBoardGenerator() {
		
	}
	
	//--------------------------------------------------------------------------
	// Methods required by BoardGenerator
	//--------------------------------------------------------------------------

	/**
	 * Generate the default board. This is a 10x10 board with the following 
	 * board pieces: 1 x length 4, 2 x length 3, 3 x length 2, 4 x length 1.
	 * Does not place any empty pieces.
	 * 
	 * @return Generated Board.
	 */
	@Override
	public Board generateDefaultBoard() {
		
		this.playingBoard = null;
		this.boardPieces = null;
		this.playingBoard = 
			new DefaultBoard(DEFAULT_BOARD_WIDTH, DEFAULT_BOARD_HEIGHT);
		
		this.boardPieces = new BoardPiece[DEFAULT_PIECE_LENGTHS.length];

		randomlyPlaceActivePieces(DEFAULT_PIECE_LENGTHS);
		
		return playingBoard;
	}

	/* (non-Javadoc)
	 * @see com.asbtechnologies.android.tiluxe.board.BoardGenerator#generateCustomBoard(int, int, int[], int)
	 */
	@Override
	public Board generateCustomBoard(
			int boardWidth, 
			int boardHeight,
			int[] pieceLengths,
			int numberOfFreeEmpty,
			int numberOfFreeTargets ) {
		
		this.playingBoard = null;
		this.boardPieces = null;
		this.playingBoard = new DefaultBoard(boardWidth, boardHeight);
		this.boardPieces = new BoardPiece[pieceLengths.length];
		
		if (!randomlyPlaceActivePieces(pieceLengths)) {
			return null;
		}
		
		setupTargetTileHints(numberOfFreeTargets);
		randomlyPlaceEmptyPieces(numberOfFreeEmpty);

		return playingBoard;
	}

	/**
	 * Get the target BoardPieces. These are the BoardPieces that must be 
	 * identified on the board.
	 * 
	 * @return Target BoardPieces.
	 */
	@Override
	public BoardPiece[] getTargetPieces() {
		return boardPieces;
	}

	//--------------------------------------------------------------------------
	// Public methods
	//--------------------------------------------------------------------------
	
	/**
	 * Confirm if the placement of the proposed piece is legal on the current
	 * board.
	 * 
	 * @param xPos Horizontal position on the board.
	 * @param yPos Vertical position on the board.
	 * @param orientation Orientation of the piece on the board.
	 * @param length Lenght of the piece on the board.
	 * @param boardHeight Overall height of the board.
	 * @param 
	 */
	public boolean isValidPlacement(
			int xPos,
			int yPos,
			int orientation,
			int length) {
		
		return validatePiecePlacement(xPos, yPos, orientation, length);
	}
	
	//--------------------------------------------------------------------------
	// Private methods
	//--------------------------------------------------------------------------
	
	/**
	 * Randomly populate the playing board with the pseudo random placement.
	 * 
	 * @param pieceLengths The set of piece lengths to be created on the board.
	 * @return True if successful, false otherwise.
	 */
	private boolean randomlyPlaceActivePieces(
			int[] pieceLengths) {
		
		// Catch null cases
		if (pieceLengths == null) {
			return false;
		}
		
		Random randomNumbers = new Random();
		
		// Sort the pieceLengths so we deal with the longest pieces first.
		// Unfortunately, this sort orders them in ascending order, so we have
		// to run our for loop backwards through the array.
		Arrays.sort(pieceLengths);

		for (int i = (pieceLengths.length - 1); i >= 0; i--) {
			
			// Attempt to place the piece at a location on the board abiding by
			// placement rules. Placement rules are that no piece can have 
			// another piece touching it. This is a brute force method.
			boolean done = false;
			int xPos = -1;
			int yPos = -1;
			int length = pieceLengths[i];
			int orientation = -1;
			
			// Calculate the width/height limits to choose the random 
			// number generation range to use.
			int widthLimit = playingBoard.getWidth();
			int heightLimit = playingBoard.getHeight();
		
			if (orientation == 0) {
				widthLimit -= (length-1);
			} else {
				heightLimit -= (length-1);
			}
			
			int counter = 0;
			
			while(!done) {
				
				// Randomly determine orientation (0 = horizontal, 1 = vertical)
				orientation = randomNumbers.nextInt(2);

				xPos = randomNumbers.nextInt(widthLimit);
				yPos = randomNumbers.nextInt(heightLimit);
				
				// If this is not an illegal placement, add the piece to the 
				// board.
				if (validatePiecePlacement(
						xPos, 
						yPos, 
						orientation, 
						length)) {
					
					DefaultBoardPiece newBoardPiece = new DefaultBoardPiece(
							xPos, 
							yPos, 
							orientation, 
							length, 
							BoardPieceState.STATE_ALIVE, 
							IdGenerator.getInstance().generateID());
					
					boardPieces[i] = newBoardPiece;
					playingBoard.addBoardPiece(newBoardPiece);
					
					done = true;
				}
				
				counter++;
				
				if (counter > ATTEMPT_LIMIT) {
					return false;
				}
			}
		}
		
		return true;
	}
	
	/**
	 * Randomly populate the playing board with the specified number of empty
	 * pieces. Empty pieces can be touching alive pieces, but cannot be placed
	 * on top of live pieces. They can only be placed on dead pieces.
	 * 
	 * @param numberToShow Number of empty tiles to show.
	 */
	private void randomlyPlaceEmptyPieces(int numberToShow) {
		
		// All empty pieces should be of length one and the orientation
		// doesn't matter.
		int length = 1;
		int orientation = BoardPiece.HORIZONTAL_ORIENTATION;
		ArrayList<DefaultBoardPiece> emptySpots = 
			new ArrayList<DefaultBoardPiece>();
		
		DefaultBoardPiece tmpPiece;

		// Get the legends and legend hints to determine where free spaces
		// should be placed. We will not place a free space in a row or column
		// that has 0 targets in it or is already solved by the free tiles 
		// given away.
		int[] horizontalLegend = playingBoard.getHorizontalLegend();
		int[] horizontalLegendHint = playingBoard.getHorizontalLegendHint();
		int[] verticalLegend = playingBoard.getVerticalLegend();
		int[] verticalLegendHint = playingBoard.getVerticalLegendHint();
		
		// Figure out how many spaces can accept empty pieces. Brute force.
		for (int w = 0; w < playingBoard.getWidth(); w++) {
			
			if (horizontalLegend[w] == 0 || 
					horizontalLegendHint[w] == 
					BoardPerimeterLegend.HINT_ALL_CORRECT) {
				continue;
			}
			
			for (int h = 0; h < playingBoard.getHeight(); h++) {
				
				if (verticalLegend[h] == 0 || 
						verticalLegendHint[h] ==
						BoardPerimeterLegend.HINT_ALL_CORRECT) {
					continue;
				}
				
				tmpPiece = (DefaultBoardPiece) 
					playingBoard.getBoardPieces()[w][h];
				
				if (tmpPiece.getMatchState() == BoardPieceState.STATE_DEAD) {
					emptySpots.add(tmpPiece);
				}
			}
		}
		
		Random randomNumbers = new Random();
		
		if (emptySpots.size() < numberToShow) {
			numberToShow = emptySpots.size();
		}
		
		int index;
		int[] location = new int[2];

		for (int i = 0; i < numberToShow; i++) {
			
			index = randomNumbers.nextInt(emptySpots.size());
			location = emptySpots.get(index).getLocation();
			
			DefaultBoardPiece newBoardPiece = new DefaultBoardPiece(
				location[0], 
				location[1], 
				orientation, 
				length, 
				BoardPieceState.EMPTY, 
				IdGenerator.getInstance().generateID());
			
			newBoardPiece.setDefaultState(BoardPieceState.EMPTY);
					
			playingBoard.addBoardPiece(newBoardPiece);

			emptySpots.remove(index);
		}
	}
	
	/**
	 * Give away some number of hidden target tiles to the user.
	 * 
	 * @param numberToShow Number of hidden target tiles to identify for the 
	 * user.
	 */
	private void setupTargetTileHints(int numberToShow) {
		
		Random random = new Random();
		int selection = 0;
		int subSelection = 0;
		boolean hintMade = false;
		DefaultBoardPiece subPiece;
		
		for (int i = 0; i < numberToShow; i++) {
			
			hintMade = false;
			
			while (!hintMade) {
			
				selection = random.nextInt(boardPieces.length);
			
				if (boardPieces[selection].getSubPieces() != null) {
					subSelection = random.nextInt(
							boardPieces[selection].getSubPieces().length);
					
					subPiece = (DefaultBoardPiece) 
						boardPieces[selection].getSubPieces()[subSelection];
				} else {
					subPiece = (DefaultBoardPiece) boardPieces[selection];
				}
				
				if (subPiece.getCurrentState() != BoardPieceState.STATE_ALIVE) {
					subPiece.setDefaultState(BoardPieceState.STATE_ALIVE);
					hintMade = true;
				}
			}
		}
		
		subPiece = null;
		random = null;
	}
	
	/**
	 * Check the placement of a piece against the current board. Placement is
	 * only legal if the piece does not touch any other board pieces.
	 * 
	 * @param xPos Horizontal board position.
	 * @param yPos Vertical board position.
	 * @param orientation Horizontal or vertical BoardPiece constant value.
	 * @param length Length of the board piece.
	 * @return True if the piece can be placed, false otherwise.
	 */
	private boolean validatePiecePlacement(
			int xPos,
			int yPos,
			int orientation,
			int length) {
		
		int testXPos = xPos;
		int testYPos = yPos;
		int widthLimit = playingBoard.getWidth()-1;
		int heightLimit = playingBoard.getHeight()-1;
		
		// Check board limits against the position.
		if (xPos < 0 || xPos > widthLimit) {
			return false;
		} else if (yPos < 0 || yPos > heightLimit) {
			return false;
		}
		
		// Depending on orientation, make sure the position + length does not
		// extend beyond the bounds of the board.
		if (orientation == BoardPiece.HORIZONTAL_ORIENTATION &&
				(xPos+length-1) > widthLimit) {
			
			return false;
			
		} else if (orientation == BoardPiece.VERTICAL_ORIENTATION && 
				(yPos+length-1) > heightLimit) {
			
			return false;
		}
		
		// Confirm it doesn't touch any neighbors. Do this by checking 
		// each of the 9 squares surrounding and including the space
		// specified. We are expecting a legal placement to have all 
		// spaces checked be set to dead.
		for (int l = 0; l < length; l++) {
			
			for (int w = -1; w < 2; w++) {				
				for (int h = -1; h < 2; h++) {
					
					testXPos = xPos + w;
					testYPos = yPos + h;
					
					// Increment the test position along the length.
					if (orientation == 0) {
						testXPos += l;
					} else {
						testYPos += l;
					}
					
					// if the width or height position is < 0 or > the
					// respective dimension board limit (width/height)
					// then skip the check.
					if (testXPos < 0 || testXPos > widthLimit) {
						continue;
					} else if (testYPos < 0 || testYPos > heightLimit) {
						continue;
					}
					
					playingBoard.pickBoardLocation(testXPos, testYPos);
					
					// This is an illegal placement if the checked 
					// board piece match state is not equal to dead.
					if (((DefaultBoardPiece)playingBoard.getLastPick()
							).getMatchState() != BoardPieceState.STATE_DEAD) {
						
						return false;
					}
				}
			}
		}
		
		return true;
	}
}
