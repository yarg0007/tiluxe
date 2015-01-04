/*****************************************************************************
 *                        Copyright ASB Technologies (c) 2011
 *                               Java Source
 *
 * This source is the property of ASB Technologies. Any duplication or reuse
 * without the consent of ASB Technologies is prohibited.
 *
 ****************************************************************************/

package com.asbtechnologies.android.tiluxe.board;

import com.asbtechnologies.android.tiluxe.util.IdGenerator;

//External Imports

//Internal Imports

/**
 * Default BoardPiece implementation. Board pieces occupy space on the 
 * board, can have length and orientation.
 * 
 * @author Ben Yarger
 * @version $Revision: 1.10 $
 */
public class DefaultBoardPiece implements BoardPiece, BoardPieceState {
	
	/** Parent board piece (group parent). */
	private DefaultBoardPiece parent;
	
	/** X axis board position. */
	private int xPos;
	
	/** Y axis board position. */
	private int yPos;
	
	/** Horizontal or vertical orientation of the piece. */
	private int orientation;
	
	/** Length of the piece on the board. */
	private int length;
	
	/** Unique group id identifying the piece, and any sub pieces. */
	private int groupID;
	
	/** Unique id to identify the individual piece. */
	private int pieceID;
	
	/** Hold the desired state of the piece the user must match. */
	private int expectedState;
	
	/** Track the current state of the piece. */
	private int currentState;
	
	/** Sub pieces making up the original BoardPiece. */
	private DefaultBoardPiece[] subPieces;
	
	/** The default state of the piece for resetting to. */
	private int defaultPieceState = BoardPieceState.STATE_LIMBO;
	
	/**
	 * Default constructor. Position refers to head of piece when length is 
	 * greater than 1. The head is the tile closest to 0,0 board position.
	 * If the length is < 0, then the length will be set to 0. If xPos or yPos 
	 * are < 0, then the respective value will be set to 0. If the expectedState
	 * is not a legal state value, it will default to STATE_DEAD. If the 
	 * orientation is not a legal value, it will default to HORIZONTAL.
	 * 
	 * @param xPos X axis board position
	 * @param yPos Y axis board position
	 * @param orientation Horizontal or vertical orientation on the board
	 * @param length The length of the piece
	 * @param expectedState The state of the board piece that must be matched by
	 * the end user. Will set current state to either EMPTY if expectedState is
	 * EMPTY or STATE_LIMBO for all other values.
	 * @param groupID Unique groupID to assign to piece and sub pieces
	 */
	public DefaultBoardPiece(
			int xPos, 
			int yPos, 
			int orientation, 
			int length,
			int expectedState,
			int groupID) {
		
		this.xPos = xPos;
		this.yPos = yPos;
		this.orientation = orientation;
		this.length = length;
		this.expectedState = expectedState;
		this.groupID = groupID;
		this.parent = null;
		
		// Minimum allowed length is 0.
		if (this.length < 0) {
			this.length = 0;
		}
		
		// Minimum allowed xPos is 0.
		if (this.xPos < 0) {
			this.xPos = 0;
		}
		
		// Minimum allowed yPos is 0.
		if (this.yPos < 0) {
			this.yPos = 0;
		}
		
		// Expected orientation must be a known orientation type. Otherwise,
		// default to HORIZONTAL.
		if (this.orientation != BoardPiece.HORIZONTAL_ORIENTATION && 
				this.orientation != BoardPiece.VERTICAL_ORIENTATION) {
			
			this.orientation = BoardPiece.HORIZONTAL_ORIENTATION;
		}
		
		// Expected state must be a known state type, prevent bad values.
		// Default to STATE_DEAD if not a legal value. 
		if (this.expectedState != BoardPieceState.EMPTY && 
				this.expectedState != BoardPieceState.STATE_ALIVE &&
				this.expectedState != BoardPieceState.STATE_DEAD &&
				this.expectedState != BoardPieceState.STATE_LIMBO) {
			
			this.expectedState = BoardPieceState.STATE_DEAD;
		}
		
		// The current state will always be in limbo if not set to empty.
		if (this.expectedState == BoardPieceState.EMPTY) {
			currentState = BoardPieceState.EMPTY;
		} else {
			currentState = BoardPieceState.STATE_LIMBO;
		}
		
		pieceID = IdGenerator.getInstance().generateID();
		subDividePieces();
	}
	
	/**
	 * Internal constructor used for creating sub pieces.
	 * 
	 * @param xPos X axis board position
	 * @param yPos Y axis board position
	 * @param orientation Horizontal or vertical orientation on the board
	 * @param length The length of the piece
	 * @param expectedState The state of the board piece that must be matched by
	 * the end user. Will set current state to either EMPTY if expectedState is
	 * EMPTY or STATE_LIMBO for all other values.
	 * @param groupID Unique groupID to assign to piece and sub pieces
	 * @param parent Parent board piece.
	 */
	private DefaultBoardPiece(
			int xPos, 
			int yPos, 
			int orientation, 
			int length,
			int expectedState,
			int groupID,
			DefaultBoardPiece parent) {
		
		this(xPos, yPos, orientation, length, expectedState, groupID);
//		parent = parent;
	}

	//--------------------------------------------------------------------------
	// Required by BoardPiece
	//--------------------------------------------------------------------------
	
	/* (non-Javadoc)
	 * @see com.asbtechnologies.android.tiluxe.board.BoardPiece#getPieceID()
	 */
	@Override
	public int getPieceID() {
		return pieceID;
	}
	
	/* (non-Javadoc)
	 * @see com.asbtechnologies.android.tiluxe.board.BoardPiece#getGroupID()
	 */
	@Override
	public int getGroupID() {
		return groupID;
	}
	
	/* (non-Javadoc)
	 * @see com.asbtechnologies.android.tiluxe.board.BoardPiece#getLocation()
	 */
	@Override
	public int[] getLocation() {
		
		int[] location = new int[] {xPos, yPos};
		return location;
	}

	/* (non-Javadoc)
	 * @see com.asbtechnologies.android.tiluxe.board.BoardPiece#getOrientation()
	 */
	@Override
	public int getOrientation() {
		
		return orientation;
	}

	/* (non-Javadoc)
	 * @see com.asbtechnologies.android.tiluxe.board.BoardPiece#getLength()
	 */
	@Override
	public int getLength() {

		return length;
	}

	/* (non-Javadoc)
	 * @see com.asbtechnologies.android.tiluxe.board.BoardPiece#getSubPieces()
	 */
	@Override
	public DefaultBoardPiece[] getSubPieces() {
	
		if (subPieces == null) {
			return null;
		}
		
		DefaultBoardPiece[] subPiecesCopy = 
			new DefaultBoardPiece[subPieces.length];
		
		for (int i = 0; i < subPieces.length; i++) {
			subPiecesCopy[i] = subPieces[i];
		}
		
		return subPiecesCopy;
	}
	
	//--------------------------------------------------------------------------
	// Methods required by BoardPieceState
	//--------------------------------------------------------------------------

	/* (non-Javadoc)
	 * @see com.asbtechnologies.android.tiluxe.board.BoardPieceState#isEmpty()
	 */
	@Override
	public boolean isEmpty() {
		
		if (currentState == BoardPieceState.EMPTY) {
			return true;
		}
		
		return false;
	}

	/* (non-Javadoc)
	 * @see com.asbtechnologies.android.tiluxe.board.BoardPieceState#isAlive()
	 */
	@Override
	public boolean isAlive() {
		
		if (currentState == BoardPieceState.STATE_ALIVE) {
			return true;
		}
		
		return false;
	}

	/* (non-Javadoc)
	 * @see com.asbtechnologies.android.tiluxe.board.BoardPieceState#isDead()
	 */
	@Override
	public boolean isDead() {
		
		if (currentState == BoardPieceState.STATE_DEAD) {
			return true;
		}
		
		return false;
	}

	/* (non-Javadoc)
	 * @see com.asbtechnologies.android.tiluxe.board.BoardPieceState#isInLimbo()
	 */
	@Override
	public boolean isInLimbo() {
		
		if (currentState == BoardPieceState.STATE_LIMBO) {
			return true;
		}
		
		return false;
	}

	/* (non-Javadoc)
	 * @see com.asbtechnologies.android.tiluxe.board.BoardPieceState#setState(int)
	 */
	@Override
	public void setState(int boardState) {
		
		currentState = boardState;
	}
	
	/* (non-Javadoc)
	 * @see com.asbtechnologies.android.tiluxe.board.BoardPieceState#setDefaultState(int)
	 */
	@Override
	public void setDefaultState(int boardState) {
	
		defaultPieceState = boardState;
		currentState = boardState;
	}

	/* (non-Javadoc)
	 * @see com.asbtechnologies.android.tiluxe.board.BoardPieceState#getCurrentState()
	 */
	@Override
	public int getCurrentState() {
		
		return currentState;
	}

	/* (non-Javadoc)
	 * @see com.asbtechnologies.android.tiluxe.board.BoardPieceState#isGroupStateCorrect()
	 */
	@Override
	public boolean isGroupStateCorrect() {
		
		DefaultBoardPiece[] subPieces = getSubPieces();
		
		if (subPieces == null && parent == null) {
			
			return isStateCorrect();
			//return false;
		} else if (subPieces == null){
			return false;
		}
		
		for (int i = 0; i < subPieces.length; i++) {
			
			if (!subPieces[i].isStateCorrect()) {
				return false;
			}
		}
		
		return true;
	}
	
	/* (non-Javadoc)
	 * @see com.asbtechnologies.android.tiluxe.board.BoardPieceState#isStateCorrect()
	 */
	@Override
	public boolean isStateCorrect() {
		
		if (currentState == expectedState) {
			return true;
		}
		
		return false;
	}

	/* (non-Javadoc)
	 * @see com.asbtechnologies.android.tiluxe.board.BoardPieceState#getMatchState()
	 */
	@Override
	public int getMatchState() {
		return expectedState;
	}
	
	/* (non-Javadoc)
	 * @see com.asbtechnologies.android.tiluxe.board.BoardPieceState#setNextState()
	 */
	@Override
	public void setNextState() {
		
		if (!isEmpty()) {
			
			if (getCurrentState() == BoardPieceState.STATE_LIMBO) {
				setState(BoardPieceState.STATE_ALIVE);
			} else if (getCurrentState() == BoardPieceState.STATE_ALIVE) {
				setState(BoardPieceState.STATE_DEAD);
			} else {
				setState(BoardPieceState.STATE_LIMBO);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see com.asbtechnologies.android.tiluxe.board.BoardPieceState#getStartingState()
	 */
	@Override
	public int getStartingState() {
		
		return defaultPieceState;
	}
	
	//--------------------------------------------------------------------------
	// Private class methods
	//--------------------------------------------------------------------------
	
	/**
	 * Generate the sub pieces of the piece. The sub pieces are created based on
	 * length. 1 piece per length size (length = 5, 5 sub pieces are created).
	 */
	private void subDividePieces() {
		
		// If the length is one or less, no sub pieces exist
		if (length <= 1) {
			subPieces = null;
			return;
		}
		
		// Create n board pieces where n = length
		subPieces = new DefaultBoardPiece[length];
		
		// Create each sub piece along the horizontal or vertical orientation
		// associated with the parent piece.
		for (int i = 0; i < length; i++) {
			
			if (orientation == BoardPiece.HORIZONTAL_ORIENTATION) {
				subPieces[i] = 
					new DefaultBoardPiece(
						xPos+i, 
						yPos, 
						orientation, 
						1, 
						expectedState,
						groupID,
						this);
			} else {
				subPieces[i] = 
					new DefaultBoardPiece(
						xPos, 
						yPos+i, 
						orientation, 
						1, 
						expectedState,
						groupID,
						this);
			}
		}
	}

}
