/*****************************************************************************
 *                        Copyright ASB Technologies (c) 2011
 *                               Java Source
 *
 * This source is the property of ASB Technologies. Any duplication or reuse
 * without the consent of ASB Technologies is prohibited.
 *
 ****************************************************************************/

package com.asbtechnologies.android.tiluxe.test;

// External Imports
import android.util.Log;

import com.asbtechnologies.android.tiluxe.board.BoardPerimeterLegend;
import com.asbtechnologies.android.tiluxe.board.BoardPiece;
import com.asbtechnologies.android.tiluxe.board.BoardPieceState;
import com.asbtechnologies.android.tiluxe.board.DefaultBoard;
import com.asbtechnologies.android.tiluxe.board.DefaultBoardPiece;
import com.asbtechnologies.android.tiluxe.util.IdGenerator;

import junit.framework.TestCase;

// Internal Imports

/**
 * Unit tests for DefaultBoard implementation.
 * 
 * @author Ben Yarger
 * @version $Revision: 1.3 $
 */
public class TiluxeBoardTest extends TestCase {

	/**
	 * Default constructor.
	 * 
	 * @param name
	 */
	public TiluxeBoardTest(String name) {
		super(name);
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	//--------------------------------------------------------------------------
	//--------------------------------------------------------------------------
	// Tests
	//--------------------------------------------------------------------------
	//--------------------------------------------------------------------------
	
	//--------------------------------------------------------------------------
	// Test a default board for initialization correctness
	//--------------------------------------------------------------------------
	
	/**
	 * Test the initialization of a board to make sure the basic parameters
	 * that the board is established with are correct.
	 */
	public void testDefaultBoardInitialization() {
		
		int boardWidth = 5;
		int boardHeight = 6;
		
		DefaultBoard defaultBoard = new DefaultBoard(boardWidth, boardHeight);
		
		// Validate that we got a board back and that the resulting dimensions
		// matched what we specified.
		assertNotNull(defaultBoard);
		
		assertEquals(boardWidth, defaultBoard.getWidth());
		assertEquals(boardHeight, defaultBoard.getHeight());
	
		// Validate that the board is full of sub pieces. Each sub piece should
		// have its current state set to limbo, but expects to match dead.
		DefaultBoardPiece[][] boardPieces = 
			(DefaultBoardPiece[][]) defaultBoard.getBoardPieces();
		
		assertNotNull(boardPieces);
		
		for (int w = 0; w < boardWidth; w++) {
			for (int h = 0; h < boardHeight; h++) {
				
				assertNotNull(boardPieces[w][h]);
				assertEquals(
						BoardPieceState.STATE_LIMBO, 
						boardPieces[w][h].getCurrentState());
				assertEquals(
						BoardPieceState.STATE_DEAD, 
						boardPieces[w][h].getMatchState());
			}
		}
		
		boardPieces = null;
		defaultBoard = null;
	}
	
	/**
	 * Add a piece to the board and confirm we can get it back correctly.
	 */
	public void testAddPieceToBoard() {
		
		int boardWidth = 6;
		int boardHeight = 6;
		
		DefaultBoard defaultBoard = new DefaultBoard(boardWidth, boardHeight);
		
		// Validate that we got a board back.
		assertNotNull(defaultBoard);
		
		// Add a piece to the board and confirm we can get the correct piece
		// back. Do this by doing a pick and also by getting the full list of
		// of board pieces and locate it in the results.
		int xPos = 3;
		int yPos = 4;
		int orientation = BoardPiece.HORIZONTAL_ORIENTATION;
		int length = 1;
		int expectedState = BoardPieceState.STATE_ALIVE;
		int groupID = IdGenerator.getInstance().generateID();
		
		DefaultBoardPiece boardPiece =  new DefaultBoardPiece(
			xPos, 
			yPos, 
			orientation, 
			length, 
			expectedState, 
			groupID);
		
		defaultBoard.addBoardPiece(boardPiece);
		
		// Test by picking first.
		defaultBoard.pickBoardLocation(xPos, yPos);
		DefaultBoardPiece pickedPiece = 
			(DefaultBoardPiece) defaultBoard.getLastPick();
		
		assertNotNull(pickedPiece);
		assertEquals(boardPiece, pickedPiece);
		assertEquals(boardPiece.getGroupID(), pickedPiece.getGroupID());
		assertEquals(boardPiece.getLength(), pickedPiece.getLength());
		assertEquals(boardPiece.getMatchState(), pickedPiece.getMatchState());
		assertEquals(boardPiece.getOrientation(), pickedPiece.getOrientation());
		
		// Now test by getting the full list of board pieces.
		DefaultBoardPiece[][] boardPieces = 
			(DefaultBoardPiece[][]) defaultBoard.getBoardPieces();
		
		assertNotNull(boardPieces);
		assertEquals(
			boardPiece, boardPieces[xPos][yPos]);
		assertEquals(
			boardPiece.getGroupID(), boardPieces[xPos][yPos].getGroupID());
		assertEquals(
			boardPiece.getLength(), boardPieces[xPos][yPos].getLength());
		assertEquals(
			boardPiece.getMatchState(), 
			boardPieces[xPos][yPos].getMatchState());
		assertEquals(
			boardPiece.getOrientation(), 
			boardPieces[xPos][yPos].getOrientation());
	
		boardPiece = null;
		pickedPiece = null;
		boardPieces = null;
		defaultBoard = null;
	}
	
	/**
	 * Remove a piece from the board and confirm that it is correctly removed.
	 * Also confirm that subsequent removals of the empty pieces that replace
	 * the removed piece result in just removed pieces.
	 */
	public void testRemovePieceFromBoard() {

		int boardWidth = 6;
		int boardHeight = 6;
		
		DefaultBoard defaultBoard = new DefaultBoard(boardWidth, boardHeight);
		
		// Validate that we got a board back.
		assertNotNull(defaultBoard);
		
		// Add a piece to the board in order to confirm the remove operations
		// work correctly. This piece should be of length > 1 in order to 
		// confirm all matching sub pieces, of the same group id, are correctly
		// removed.
		int xPos = 2;
		int yPos = 4;
		int orientation = BoardPiece.HORIZONTAL_ORIENTATION;
		int length = 3;
		int expectedState = BoardPieceState.STATE_ALIVE;
		int groupID = IdGenerator.getInstance().generateID();
		
		DefaultBoardPiece boardPiece =  new DefaultBoardPiece(
			xPos, 
			yPos, 
			orientation, 
			length, 
			expectedState, 
			groupID);
		
		defaultBoard.addBoardPiece(boardPiece);
		
		// Now remove the piece.
		defaultBoard.removeBoardPiece(boardPiece);
		
		// Now confirm it was removed correctly by getting the full list of 
		// board pieces and making sure it doesn't exist anywhere.
		DefaultBoardPiece[][] boardPieces = 
			(DefaultBoardPiece[][]) defaultBoard.getBoardPieces();
		
		assertNotNull(boardPieces);
		
		for (int w = 0; w < boardWidth; w++) {
			for (int h = 0; h < boardHeight; h++) {
				assertNotSame(boardPiece.getGroupID(), boardPieces[w][h]);
			}
		}
		
		// Confirm that the sub pieces, where the piece formerly existed, are 
		// now setup as empty pieces with group id == -1.
		for (int w = 0; w < length; w++) {
			assertEquals(
					BoardPieceState.EMPTY, 
					boardPieces[xPos+w][yPos].getMatchState());
			assertEquals(-1, boardPieces[xPos+w][yPos].getGroupID());
		}
		
		// Now attempt to remove the now empty piece. Net result is that it 
		// should stay an empty piece.
		defaultBoard.pickBoardLocation(xPos, yPos);
		DefaultBoardPiece pickPiece = 
			(DefaultBoardPiece) defaultBoard.getLastPick();
		
		defaultBoard.removeBoardPiece(pickPiece);
		
		// Confirm that the sub pieces are still empty pieces with group 
		// id == -1.
		for (int w = 0; w < length; w++) {
			assertEquals(
					BoardPieceState.EMPTY, 
					boardPieces[xPos+w][yPos].getMatchState());
			assertEquals(-1, boardPieces[xPos+w][yPos].getGroupID());
		}
	
		pickPiece = null;
		boardPiece = null;
		boardPieces = null;
		defaultBoard = null;
	}
	
	/**
	 * Remove a piece from the board and confirm that it is correctly removed.
	 * Then add it back to make sure it updates the board correctly.
	 */
	public void testRemovePieceAndAddBack() {

		int boardWidth = 6;
		int boardHeight = 6;
		
		DefaultBoard defaultBoard = new DefaultBoard(boardWidth, boardHeight);
		
		// Validate that we got a board back.
		assertNotNull(defaultBoard);
		
		// Add a piece to the board in order to confirm the remove operations
		// work correctly. This piece should be of length > 1 in order to 
		// confirm all matching sub pieces, of the same group id, are correctly
		// removed.
		int xPos = 2;
		int yPos = 4;
		int orientation = BoardPiece.HORIZONTAL_ORIENTATION;
		int length = 3;
		int expectedState = BoardPieceState.STATE_ALIVE;
		int groupID = IdGenerator.getInstance().generateID();
		
		DefaultBoardPiece boardPiece =  new DefaultBoardPiece(
			xPos, 
			yPos, 
			orientation, 
			length, 
			expectedState, 
			groupID);
		
		defaultBoard.addBoardPiece(boardPiece);
		
		// Now remove the piece.
		defaultBoard.removeBoardPiece(boardPiece);
		
		// Now add it back and confirm it exists correctly.
		defaultBoard.addBoardPiece(boardPiece);
		
		DefaultBoardPiece[][] boardPieces = 
			(DefaultBoardPiece[][]) defaultBoard.getBoardPieces();
		
		assertNotNull(boardPieces);
		
		for (int w = xPos; w < length; w++) {
				assertEquals(
						groupID, 
						boardPieces[w][yPos].getGroupID());
		}
	
		boardPiece = null;
		boardPieces = null;
		defaultBoard = null;
	}
	
	//--------------------------------------------------------------------------
	// Test game over
	//--------------------------------------------------------------------------
	
	/**
	 * Test game over check for three pieces placed on the board all expecting
	 * to be set to alive. Set them to alive and confirm the game is over.
	 */
	public void testAlivePiecesGameOver() {
		
		int boardWidth = 6;
		int boardHeight = 6;
		
		DefaultBoard defaultBoard = new DefaultBoard(boardWidth, boardHeight);
		
		// Validate that we got a board back.
		assertNotNull(defaultBoard);
		
		// Add three pieces to the board, one of length 1, one of length 2, and
		// one of length three.
		//
		// d d d d d d
		// d a d d d d
		// d d d d a d
		// d d d d a d
		// d d d d d d
		// d a a a d d
		
		// Create length 1 piece
		int xPos1 = 1;
		int yPos1 = 1;
		int orientation1 = BoardPiece.HORIZONTAL_ORIENTATION;
		int length1 = 1;
		int expectedState1 = BoardPieceState.STATE_ALIVE;
		int groupID1 = IdGenerator.getInstance().generateID();
		
		DefaultBoardPiece boardPiece1 =  new DefaultBoardPiece(
			xPos1, 
			yPos1, 
			orientation1, 
			length1, 
			expectedState1, 
			groupID1);
		
		defaultBoard.addBoardPiece(boardPiece1);
		
		// Create length 2 piece
		int xPos2 = 4;
		int yPos2 = 2;
		int orientation2 = BoardPiece.VERTICAL_ORIENTATION;
		int length2 = 2;
		int expectedState2 = BoardPieceState.STATE_ALIVE;
		int groupID2 = IdGenerator.getInstance().generateID();
		
		DefaultBoardPiece boardPiece2 =  new DefaultBoardPiece(
			xPos2, 
			yPos2, 
			orientation2, 
			length2, 
			expectedState2, 
			groupID2);
		
		defaultBoard.addBoardPiece(boardPiece2);
		
		// Create length 3 piece
		int xPos3 = 1;
		int yPos3 = 5;
		int orientation3 = BoardPiece.HORIZONTAL_ORIENTATION;
		int length3 = 3;
		int expectedState3 = BoardPieceState.STATE_ALIVE;
		int groupID3 = IdGenerator.getInstance().generateID();
		
		DefaultBoardPiece boardPiece3 =  new DefaultBoardPiece(
			xPos3, 
			yPos3, 
			orientation3, 
			length3, 
			expectedState3, 
			groupID3);
		
		defaultBoard.addBoardPiece(boardPiece3);
		
		// Now set the state of the board pieces to alive for each of the 3 
		// board pieces added.
		defaultBoard.pickBoardLocation(xPos1, yPos1);
		((DefaultBoardPiece)defaultBoard.getLastPick()).setState(
				BoardPieceState.STATE_ALIVE);
		
		defaultBoard.pickBoardLocation(xPos2, yPos2);
		((DefaultBoardPiece)defaultBoard.getLastPick()).setState(
				BoardPieceState.STATE_ALIVE);
		
		defaultBoard.pickBoardLocation(xPos2, yPos2+1);
		((DefaultBoardPiece)defaultBoard.getLastPick()).setState(
				BoardPieceState.STATE_ALIVE);
		
		defaultBoard.pickBoardLocation(xPos3, yPos3);
		((DefaultBoardPiece)defaultBoard.getLastPick()).setState(
				BoardPieceState.STATE_ALIVE);
		
		defaultBoard.pickBoardLocation(xPos3+1, yPos3);
		((DefaultBoardPiece)defaultBoard.getLastPick()).setState(
				BoardPieceState.STATE_ALIVE);
		
		defaultBoard.pickBoardLocation(xPos3+2, yPos3);
		((DefaultBoardPiece)defaultBoard.getLastPick()).setState(
				BoardPieceState.STATE_ALIVE);
		
		// Confirm that the game is now over
		assertTrue(defaultBoard.isGameOver());
	
		boardPiece1 = null;
		boardPiece2 = null;
		boardPiece3 = null;
		defaultBoard = null;
	}
	
	/**
	 * Test game over check by setting state of dead pieces to dead.
	 */
	public void testDeadPiecesGameOver() {
		
		int boardWidth = 3;
		int boardHeight = 3;
		
		DefaultBoard defaultBoard = new DefaultBoard(boardWidth, boardHeight);
		
		// Validate that we got a board back.
		assertNotNull(defaultBoard);
		
		// Add alive pieces to the board to minimize how many pieces need to be
		// set to dead to complete this test.
		
		// Create piece 1
		int xPos1 = 0;
		int yPos1 = 0;
		int orientation1 = BoardPiece.HORIZONTAL_ORIENTATION;
		int length1 = 3;
		int expectedState1 = BoardPieceState.STATE_ALIVE;
		int groupID1 = IdGenerator.getInstance().generateID();
		
		DefaultBoardPiece boardPiece1 =  new DefaultBoardPiece(
			xPos1, 
			yPos1, 
			orientation1, 
			length1, 
			expectedState1, 
			groupID1);
		
		defaultBoard.addBoardPiece(boardPiece1);
		
		// Create piece 2
		int xPos2 = 0;
		int yPos2 = 1;
		int orientation2 = BoardPiece.VERTICAL_ORIENTATION;
		int length2 = 2;
		int expectedState2 = BoardPieceState.STATE_ALIVE;
		int groupID2 = IdGenerator.getInstance().generateID();
		
		DefaultBoardPiece boardPiece2 =  new DefaultBoardPiece(
			xPos2, 
			yPos2, 
			orientation2, 
			length2, 
			expectedState2, 
			groupID2);
		
		defaultBoard.addBoardPiece(boardPiece2);
		
		// Create piece 3
		int xPos3 = 1;
		int yPos3 = 2;
		int orientation3 = BoardPiece.HORIZONTAL_ORIENTATION;
		int length3 = 2;
		int expectedState3 = BoardPieceState.STATE_ALIVE;
		int groupID3 = IdGenerator.getInstance().generateID();
		
		DefaultBoardPiece boardPiece3 =  new DefaultBoardPiece(
			xPos3, 
			yPos3, 
			orientation3, 
			length3, 
			expectedState3, 
			groupID3);
		
		defaultBoard.addBoardPiece(boardPiece3);
		
		// Now set the state of all non alive board pieces to dead.
		defaultBoard.pickBoardLocation(1, 1);
		((DefaultBoardPiece)defaultBoard.getLastPick()).setState(
				BoardPieceState.STATE_DEAD);
		
		defaultBoard.pickBoardLocation(2, 1);
		((DefaultBoardPiece)defaultBoard.getLastPick()).setState(
				BoardPieceState.STATE_DEAD);
		
		// Confirm that the game is now over
		assertTrue(defaultBoard.isGameOver());
	
		boardPiece1 = null;
		boardPiece2 = null;
		boardPiece3 = null;
		defaultBoard = null;
	}
	
	/**
	 * Test game over with all board pieces being alive pieces. Set each to
	 * alive and confirm.
	 */
	public void testAllAlivePiecesGameOver() {
		
		int boardWidth = 3;
		int boardHeight = 3;
		
		DefaultBoard defaultBoard = new DefaultBoard(boardWidth, boardHeight);
		
		// Validate that we got a board back.
		assertNotNull(defaultBoard);
		
		// Add three pieces to the board, one of length 1, one of length 2, and
		// one of length three.
		
		// Create piece 1
		int xPos1 = 0;
		int yPos1 = 0;
		int orientation1 = BoardPiece.HORIZONTAL_ORIENTATION;
		int length1 = 3;
		int expectedState1 = BoardPieceState.STATE_ALIVE;
		int groupID1 = IdGenerator.getInstance().generateID();
		
		DefaultBoardPiece boardPiece1 =  new DefaultBoardPiece(
			xPos1, 
			yPos1, 
			orientation1, 
			length1, 
			expectedState1, 
			groupID1);
		
		defaultBoard.addBoardPiece(boardPiece1);
		
		// Create piece 2
		int xPos2 = 0;
		int yPos2 = 1;
		int orientation2 = BoardPiece.HORIZONTAL_ORIENTATION;
		int length2 = 3;
		int expectedState2 = BoardPieceState.STATE_ALIVE;
		int groupID2 = IdGenerator.getInstance().generateID();
		
		DefaultBoardPiece boardPiece2 =  new DefaultBoardPiece(
			xPos2, 
			yPos2, 
			orientation2, 
			length2, 
			expectedState2, 
			groupID2);
		
		defaultBoard.addBoardPiece(boardPiece2);
		
		// Create piece 3
		int xPos3 = 0;
		int yPos3 = 2;
		int orientation3 = BoardPiece.HORIZONTAL_ORIENTATION;
		int length3 = 3;
		int expectedState3 = BoardPieceState.STATE_ALIVE;
		int groupID3 = IdGenerator.getInstance().generateID();
		
		DefaultBoardPiece boardPiece3 =  new DefaultBoardPiece(
			xPos3, 
			yPos3, 
			orientation3, 
			length3, 
			expectedState3, 
			groupID3);
		
		defaultBoard.addBoardPiece(boardPiece3);
		
		// Now set the state of the board pieces to alive.
		defaultBoard.pickBoardLocation(0, 0);
		((DefaultBoardPiece)defaultBoard.getLastPick()).setState(
				BoardPieceState.STATE_ALIVE);
		
		defaultBoard.pickBoardLocation(0, 1);
		((DefaultBoardPiece)defaultBoard.getLastPick()).setState(
				BoardPieceState.STATE_ALIVE);
		
		defaultBoard.pickBoardLocation(0, 2);
		((DefaultBoardPiece)defaultBoard.getLastPick()).setState(
				BoardPieceState.STATE_ALIVE);
		
		defaultBoard.pickBoardLocation(1, 0);
		((DefaultBoardPiece)defaultBoard.getLastPick()).setState(
				BoardPieceState.STATE_ALIVE);
		
		defaultBoard.pickBoardLocation(1, 1);
		((DefaultBoardPiece)defaultBoard.getLastPick()).setState(
				BoardPieceState.STATE_ALIVE);
		
		defaultBoard.pickBoardLocation(1, 2);
		((DefaultBoardPiece)defaultBoard.getLastPick()).setState(
				BoardPieceState.STATE_ALIVE);
		
		defaultBoard.pickBoardLocation(2, 0);
		((DefaultBoardPiece)defaultBoard.getLastPick()).setState(
				BoardPieceState.STATE_ALIVE);
		
		defaultBoard.pickBoardLocation(2, 1);
		((DefaultBoardPiece)defaultBoard.getLastPick()).setState(
				BoardPieceState.STATE_ALIVE);
		
		defaultBoard.pickBoardLocation(2, 2);
		((DefaultBoardPiece)defaultBoard.getLastPick()).setState(
				BoardPieceState.STATE_ALIVE);
		
		// Confirm that the game is now over
		assertTrue(defaultBoard.isGameOver());
	
		boardPiece1 = null;
		boardPiece2 = null;
		boardPiece3 = null;
		defaultBoard = null;
	}
	
	/**
	 * Test game over check by setting all pieces to dead. Entire board is
	 * dead pieces.
	 */
	public void testAllDeadPiecesGameOver() {
		
		int boardWidth = 3;
		int boardHeight = 3;
		
		DefaultBoard defaultBoard = new DefaultBoard(boardWidth, boardHeight);
		
		// Validate that we got a board back.
		assertNotNull(defaultBoard);
		
		// Now set the state of all board pieces to dead.
		defaultBoard.pickBoardLocation(0, 0);
		((DefaultBoardPiece)defaultBoard.getLastPick()).setState(
				BoardPieceState.STATE_DEAD);
		
		defaultBoard.pickBoardLocation(0, 1);
		((DefaultBoardPiece)defaultBoard.getLastPick()).setState(
				BoardPieceState.STATE_DEAD);
		
		defaultBoard.pickBoardLocation(0, 2);
		((DefaultBoardPiece)defaultBoard.getLastPick()).setState(
				BoardPieceState.STATE_DEAD);
		
		defaultBoard.pickBoardLocation(1, 0);
		((DefaultBoardPiece)defaultBoard.getLastPick()).setState(
				BoardPieceState.STATE_DEAD);
		
		defaultBoard.pickBoardLocation(1, 1);
		((DefaultBoardPiece)defaultBoard.getLastPick()).setState(
				BoardPieceState.STATE_DEAD);
		
		defaultBoard.pickBoardLocation(1, 2);
		((DefaultBoardPiece)defaultBoard.getLastPick()).setState(
				BoardPieceState.STATE_DEAD);
		
		defaultBoard.pickBoardLocation(2, 0);
		((DefaultBoardPiece)defaultBoard.getLastPick()).setState(
				BoardPieceState.STATE_DEAD);
		
		defaultBoard.pickBoardLocation(2, 1);
		((DefaultBoardPiece)defaultBoard.getLastPick()).setState(
				BoardPieceState.STATE_DEAD);
		
		defaultBoard.pickBoardLocation(2, 2);
		((DefaultBoardPiece)defaultBoard.getLastPick()).setState(
				BoardPieceState.STATE_DEAD);
		defaultBoard.printBoard();
		// Confirm that the game is now over
		assertTrue(defaultBoard.isGameOver());
	
		defaultBoard = null;
	}
	
	/**
	 * Test game over check for three pieces placed on the board all expecting
	 * to be set to alive. Set them to alive and confirm the game is over. This
	 * test includes empty piece cases.
	 */
	public void testAlivePiecesWithEmptiesGameOver() {
		
		int boardWidth = 6;
		int boardHeight = 6;
		
		DefaultBoard defaultBoard = new DefaultBoard(boardWidth, boardHeight);
		
		// Validate that we got a board back.
		assertNotNull(defaultBoard);
		
		// Add three pieces to the board, one of length 1, one of length 2, and
		// one of length three.
		//
		// d d d d d d
		// d a d d d d
		// d d d d a d
		// d d d d a d
		// d d d d d d
		// d a a a d d
		
		// Create length 1 piece
		int xPos1 = 1;
		int yPos1 = 1;
		int orientation1 = BoardPiece.HORIZONTAL_ORIENTATION;
		int length1 = 1;
		int expectedState1 = BoardPieceState.STATE_ALIVE;
		int groupID1 = IdGenerator.getInstance().generateID();
		
		DefaultBoardPiece boardPiece1 =  new DefaultBoardPiece(
			xPos1, 
			yPos1, 
			orientation1, 
			length1, 
			expectedState1, 
			groupID1);
		
		defaultBoard.addBoardPiece(boardPiece1);
		
		// Create length 2 piece
		int xPos2 = 5;
		int yPos2 = 2;
		int orientation2 = BoardPiece.VERTICAL_ORIENTATION;
		int length2 = 2;
		int expectedState2 = BoardPieceState.STATE_ALIVE;
		int groupID2 = IdGenerator.getInstance().generateID();
		
		DefaultBoardPiece boardPiece2 =  new DefaultBoardPiece(
			xPos2, 
			yPos2, 
			orientation2, 
			length2, 
			expectedState2, 
			groupID2);
		
		defaultBoard.addBoardPiece(boardPiece2);
		
		// Create length 3 piece
		int xPos3 = 1;
		int yPos3 = 5;
		int orientation3 = BoardPiece.HORIZONTAL_ORIENTATION;
		int length3 = 3;
		int expectedState3 = BoardPieceState.STATE_ALIVE;
		int groupID3 = IdGenerator.getInstance().generateID();
		
		DefaultBoardPiece boardPiece3 =  new DefaultBoardPiece(
			xPos3, 
			yPos3, 
			orientation3, 
			length3, 
			expectedState3, 
			groupID3);
		
		defaultBoard.addBoardPiece(boardPiece3);
		
		// Create two empty pieces of each of length one.
		int xPosE1 = 0;
		int yPosE1 = 0;
		int orientationE1 = BoardPiece.HORIZONTAL_ORIENTATION;
		int lengthE1 = 1;
		int expectedStateE1 = BoardPieceState.EMPTY;
		int groupIDE1 = IdGenerator.getInstance().generateID();
		
		DefaultBoardPiece emptyPiece1 =  new DefaultBoardPiece(
			xPosE1, 
			yPosE1, 
			orientationE1, 
			lengthE1, 
			expectedStateE1, 
			groupIDE1);
		
		defaultBoard.addBoardPiece(emptyPiece1);
		
		// Create second empty piece.
		int xPosE2 = 3;
		int yPosE2 = 3;
		int orientationE2 = BoardPiece.HORIZONTAL_ORIENTATION;
		int lengthE2 = 1;
		int expectedStateE2 = BoardPieceState.EMPTY;
		int groupIDE2 = IdGenerator.getInstance().generateID();
		
		DefaultBoardPiece emptyPiece2 =  new DefaultBoardPiece(
			xPosE2, 
			yPosE2, 
			orientationE2, 
			lengthE2,
			expectedStateE2, 
			groupIDE2);
		
		defaultBoard.addBoardPiece(emptyPiece2);
		
		// Now set the state of the board pieces to alive for each of the 3 
		// board pieces added.
		defaultBoard.pickBoardLocation(xPos1, yPos1);
		((DefaultBoardPiece)defaultBoard.getLastPick()).setState(
				BoardPieceState.STATE_ALIVE);
		
		defaultBoard.pickBoardLocation(xPos2, yPos2);
		((DefaultBoardPiece)defaultBoard.getLastPick()).setState(
				BoardPieceState.STATE_ALIVE);
		
		defaultBoard.pickBoardLocation(xPos2, yPos2+1);
		((DefaultBoardPiece)defaultBoard.getLastPick()).setState(
				BoardPieceState.STATE_ALIVE);
		
		defaultBoard.pickBoardLocation(xPos3, yPos3);
		((DefaultBoardPiece)defaultBoard.getLastPick()).setState(
				BoardPieceState.STATE_ALIVE);
		
		defaultBoard.pickBoardLocation(xPos3+1, yPos3);
		((DefaultBoardPiece)defaultBoard.getLastPick()).setState(
				BoardPieceState.STATE_ALIVE);
		
		defaultBoard.pickBoardLocation(xPos3+2, yPos3);
		((DefaultBoardPiece)defaultBoard.getLastPick()).setState(
				BoardPieceState.STATE_ALIVE);
		
		// Confirm that the game is now over
		assertTrue(defaultBoard.isGameOver());
	
		boardPiece1 = null;
		boardPiece2 = null;
		boardPiece3 = null;
		emptyPiece1 = null;
		emptyPiece2 = null;
		defaultBoard = null;
	}
	
	/**
	 * Test game over check by setting state of dead pieces to dead.
	 */
	public void testDeadPiecesWithEmptyGameOver() {
		
		int boardWidth = 3;
		int boardHeight = 3;
		
		DefaultBoard defaultBoard = new DefaultBoard(boardWidth, boardHeight);
		
		// Validate that we got a board back.
		assertNotNull(defaultBoard);
		
		// Add alive pieces to the board to minimize how many pieces need to be
		// set to dead to complete this test.
		
		// Create piece 1
		int xPos1 = 0;
		int yPos1 = 0;
		int orientation1 = BoardPiece.HORIZONTAL_ORIENTATION;
		int length1 = 2;
		int expectedState1 = BoardPieceState.STATE_ALIVE;
		int groupID1 = IdGenerator.getInstance().generateID();
		
		DefaultBoardPiece boardPiece1 =  new DefaultBoardPiece(
			xPos1, 
			yPos1, 
			orientation1, 
			length1, 
			expectedState1, 
			groupID1);
		
		defaultBoard.addBoardPiece(boardPiece1);
		
		// Create piece 2
		int xPos2 = 0;
		int yPos2 = 1;
		int orientation2 = BoardPiece.VERTICAL_ORIENTATION;
		int length2 = 1;
		int expectedState2 = BoardPieceState.STATE_ALIVE;
		int groupID2 = IdGenerator.getInstance().generateID();
		
		DefaultBoardPiece boardPiece2 =  new DefaultBoardPiece(
			xPos2, 
			yPos2, 
			orientation2, 
			length2, 
			expectedState2, 
			groupID2);
		
		defaultBoard.addBoardPiece(boardPiece2);
		
		// Create piece 3
		int xPos3 = 1;
		int yPos3 = 2;
		int orientation3 = BoardPiece.HORIZONTAL_ORIENTATION;
		int length3 = 2;
		int expectedState3 = BoardPieceState.STATE_ALIVE;
		int groupID3 = IdGenerator.getInstance().generateID();
		
		DefaultBoardPiece boardPiece3 =  new DefaultBoardPiece(
			xPos3, 
			yPos3, 
			orientation3, 
			length3, 
			expectedState3, 
			groupID3);
		
		defaultBoard.addBoardPiece(boardPiece3);
		
		// Create two empty pieces
		int xPosE1 = 2;
		int yPosE1 = 0;
		int orientationE1 = BoardPiece.HORIZONTAL_ORIENTATION;
		int lengthE1 = 1;
		int expectedStateE1 = BoardPieceState.EMPTY;
		int groupIDE1 = IdGenerator.getInstance().generateID();
		
		DefaultBoardPiece emptyPiece1 =  new DefaultBoardPiece(
			xPosE1, 
			yPosE1, 
			orientationE1, 
			lengthE1, 
			expectedStateE1, 
			groupIDE1);
		
		defaultBoard.addBoardPiece(emptyPiece1);
		
		// Create second empty piece.
		int xPosE2 = 0;
		int yPosE2 = 2;
		int orientationE2 = BoardPiece.HORIZONTAL_ORIENTATION;
		int lengthE2 = 1;
		int expectedStateE2 = BoardPieceState.EMPTY;
		int groupIDE2 = IdGenerator.getInstance().generateID();
		
		DefaultBoardPiece emptyPiece2 =  new DefaultBoardPiece(
			xPosE2, 
			yPosE2, 
			orientationE2, 
			lengthE2,
			expectedStateE2, 
			groupIDE2);
		
		defaultBoard.addBoardPiece(emptyPiece2);
		
		// Now set the state of all non alive board pieces to dead.
		defaultBoard.pickBoardLocation(1, 1);
		((DefaultBoardPiece)defaultBoard.getLastPick()).setState(
				BoardPieceState.STATE_DEAD);
		
		defaultBoard.pickBoardLocation(2, 1);
		((DefaultBoardPiece)defaultBoard.getLastPick()).setState(
				BoardPieceState.STATE_DEAD);
		
		// Confirm that the game is now over
		assertTrue(defaultBoard.isGameOver());
	
		boardPiece1 = null;
		boardPiece2 = null;
		boardPiece3 = null;
		emptyPiece1 = null;
		emptyPiece2 = null;
		defaultBoard = null;
	}
	
	/**
	 * Test game over check for three pieces placed on the board all expecting
	 * to be set to alive. Set two to alive and confirm the game is not over.
	 */
	public void testAlivePiecesNotGameOver() {
		
		int boardWidth = 6;
		int boardHeight = 6;
		
		DefaultBoard defaultBoard = new DefaultBoard(boardWidth, boardHeight);
		
		// Validate that we got a board back.
		assertNotNull(defaultBoard);
		
		// Add three pieces to the board, one of length 1, one of length 2, and
		// one of length three.
		//
		// d d d d d d
		// d a d d d d
		// d d d d a d
		// d d d d a d
		// d d d d d d
		// d a a a d d
		
		// Create length 1 piece
		int xPos1 = 1;
		int yPos1 = 1;
		int orientation1 = BoardPiece.HORIZONTAL_ORIENTATION;
		int length1 = 1;
		int expectedState1 = BoardPieceState.STATE_ALIVE;
		int groupID1 = IdGenerator.getInstance().generateID();
		
		DefaultBoardPiece boardPiece1 =  new DefaultBoardPiece(
			xPos1, 
			yPos1, 
			orientation1, 
			length1, 
			expectedState1, 
			groupID1);
		
		defaultBoard.addBoardPiece(boardPiece1);
		
		// Create length 2 piece
		int xPos2 = 4;
		int yPos2 = 2;
		int orientation2 = BoardPiece.VERTICAL_ORIENTATION;
		int length2 = 2;
		int expectedState2 = BoardPieceState.STATE_ALIVE;
		int groupID2 = IdGenerator.getInstance().generateID();
		
		DefaultBoardPiece boardPiece2 =  new DefaultBoardPiece(
			xPos2, 
			yPos2, 
			orientation2, 
			length2, 
			expectedState2, 
			groupID2);
		
		defaultBoard.addBoardPiece(boardPiece2);
		
		// Create length 3 piece
		int xPos3 = 1;
		int yPos3 = 5;
		int orientation3 = BoardPiece.HORIZONTAL_ORIENTATION;
		int length3 = 3;
		int expectedState3 = BoardPieceState.STATE_ALIVE;
		int groupID3 = IdGenerator.getInstance().generateID();
		
		DefaultBoardPiece boardPiece3 =  new DefaultBoardPiece(
			xPos3, 
			yPos3, 
			orientation3, 
			length3, 
			expectedState3, 
			groupID3);
		
		defaultBoard.addBoardPiece(boardPiece3);
		
		// Now set the state of the board pieces to alive for the last two 
		// board pieces added.
		
		defaultBoard.pickBoardLocation(xPos2, yPos2);
		((DefaultBoardPiece)defaultBoard.getLastPick()).setState(
				BoardPieceState.STATE_ALIVE);
		
		defaultBoard.pickBoardLocation(xPos2, yPos2+1);
		((DefaultBoardPiece)defaultBoard.getLastPick()).setState(
				BoardPieceState.STATE_ALIVE);
		
		defaultBoard.pickBoardLocation(xPos3, yPos3);
		((DefaultBoardPiece)defaultBoard.getLastPick()).setState(
				BoardPieceState.STATE_ALIVE);
		
		defaultBoard.pickBoardLocation(xPos3+1, yPos3);
		((DefaultBoardPiece)defaultBoard.getLastPick()).setState(
				BoardPieceState.STATE_ALIVE);
		
		defaultBoard.pickBoardLocation(xPos3+2, yPos3);
		((DefaultBoardPiece)defaultBoard.getLastPick()).setState(
				BoardPieceState.STATE_ALIVE);
		
		// Confirm that the game is not over
		assertFalse(defaultBoard.isGameOver());
	
		boardPiece1 = null;
		boardPiece2 = null;
		boardPiece3 = null;
		defaultBoard = null;
	}
	
	/**
	 * Test game over check by setting state of dead pieces to dead, except for
	 * one which will check that the game is not over.
	 */
	public void testDeadPiecesNotGameOver() {
		
		int boardWidth = 3;
		int boardHeight = 3;
		
		DefaultBoard defaultBoard = new DefaultBoard(boardWidth, boardHeight);
		
		// Validate that we got a board back.
		assertNotNull(defaultBoard);
		
		// Add alive pieces to the board to minimize how many pieces need to be
		// set to dead to complete this test.
		
		// Create piece 1
		int xPos1 = 0;
		int yPos1 = 0;
		int orientation1 = BoardPiece.HORIZONTAL_ORIENTATION;
		int length1 = 3;
		int expectedState1 = BoardPieceState.STATE_ALIVE;
		int groupID1 = IdGenerator.getInstance().generateID();
		
		DefaultBoardPiece boardPiece1 =  new DefaultBoardPiece(
			xPos1, 
			yPos1, 
			orientation1, 
			length1, 
			expectedState1, 
			groupID1);
		
		defaultBoard.addBoardPiece(boardPiece1);
		
		// Create piece 2
		int xPos2 = 0;
		int yPos2 = 1;
		int orientation2 = BoardPiece.VERTICAL_ORIENTATION;
		int length2 = 2;
		int expectedState2 = BoardPieceState.STATE_ALIVE;
		int groupID2 = IdGenerator.getInstance().generateID();
		
		DefaultBoardPiece boardPiece2 =  new DefaultBoardPiece(
			xPos2, 
			yPos2, 
			orientation2, 
			length2, 
			expectedState2, 
			groupID2);
		
		defaultBoard.addBoardPiece(boardPiece2);
		
		// Create piece 3
		int xPos3 = 1;
		int yPos3 = 2;
		int orientation3 = BoardPiece.HORIZONTAL_ORIENTATION;
		int length3 = 2;
		int expectedState3 = BoardPieceState.STATE_ALIVE;
		int groupID3 = IdGenerator.getInstance().generateID();
		
		DefaultBoardPiece boardPiece3 =  new DefaultBoardPiece(
			xPos3, 
			yPos3, 
			orientation3, 
			length3, 
			expectedState3, 
			groupID3);
		
		defaultBoard.addBoardPiece(boardPiece3);
		
		// Now set the state of all non alive board pieces to dead.
		defaultBoard.pickBoardLocation(1, 1);
		((DefaultBoardPiece)defaultBoard.getLastPick()).setState(
				BoardPieceState.STATE_DEAD);
		
		// Confirm that the game is now over
		assertFalse(defaultBoard.isGameOver());
	
		boardPiece1 = null;
		boardPiece2 = null;
		boardPiece3 = null;
		defaultBoard = null;
	}
	
	//--------------------------------------------------------------------------
	// Test board picking, legends and hints
	//--------------------------------------------------------------------------
	
	/**
	 * Test the picking method. Confirm the piece we add is then pickable.
	 */
	public void testPick() {
		
		int boardWidth = 6;
		int boardHeight = 6;
		
		DefaultBoard defaultBoard = new DefaultBoard(boardWidth, boardHeight);
		
		// Validate that we got a board back.
		assertNotNull(defaultBoard);
		
		// Add a piece to the board and then attempt to pick it
		int xPos1 = 3;
		int yPos1 = 3;
		int orientation1 = BoardPiece.HORIZONTAL_ORIENTATION;
		int length1 = 1;
		int expectedState1 = BoardPieceState.STATE_ALIVE;
		int groupID1 = IdGenerator.getInstance().generateID();
		
		DefaultBoardPiece boardPiece1 =  new DefaultBoardPiece(
			xPos1, 
			yPos1, 
			orientation1, 
			length1, 
			expectedState1, 
			groupID1);
		
		defaultBoard.addBoardPiece(boardPiece1);
		
		// Pick the piece
		defaultBoard.pickBoardLocation(xPos1, yPos1);
		DefaultBoardPiece pickPiece = 
			(DefaultBoardPiece) defaultBoard.getLastPick();
		
		assertEquals(boardPiece1, pickPiece);
		assertEquals(groupID1, pickPiece.getGroupID());
		
		pickPiece = null;
		boardPiece1 = null;
		defaultBoard = null;
	}
	
	/**
	 * Test the vertical legend to make sure it is calculating correctly.
	 */
	public void testVerticalLegend() {
	
		int boardWidth = 6;
		int boardHeight = 6;
		
		DefaultBoard defaultBoard = new DefaultBoard(boardWidth, boardHeight);
		
		// Validate that we got a board back.
		assertNotNull(defaultBoard);
		
		// Add a series of pieces to the board and then confirm the vertical 
		// legend is correct.
		//
		// d d a a d d
		// a d d d a d
		// a d d d d d
		// d d d d d d
		// d a a a a d
		// a d d d d a
		
		int[] verticalLegendKey = new int[] {2, 2, 1, 0, 4, 2};
		
		// Piece 1
		int xPos1 = 2;
		int yPos1 = 0;
		int orientation1 = BoardPiece.HORIZONTAL_ORIENTATION;
		int length1 = 2;
		int expectedState1 = BoardPieceState.STATE_ALIVE;
		int groupID1 = IdGenerator.getInstance().generateID();
		
		DefaultBoardPiece boardPiece1 =  new DefaultBoardPiece(
			xPos1, 
			yPos1, 
			orientation1, 
			length1, 
			expectedState1, 
			groupID1);
		
		defaultBoard.addBoardPiece(boardPiece1);
		
		// Piece 2
		int xPos2 = 0;
		int yPos2 = 1;
		int orientation2 = BoardPiece.VERTICAL_ORIENTATION;
		int length2 = 2;
		int expectedState2 = BoardPieceState.STATE_ALIVE;
		int groupID2 = IdGenerator.getInstance().generateID();
		
		DefaultBoardPiece boardPiece2 =  new DefaultBoardPiece(
			xPos2, 
			yPos2, 
			orientation2, 
			length2, 
			expectedState2, 
			groupID2);
		
		defaultBoard.addBoardPiece(boardPiece2);
		
		// Piece 3
		int xPos3 = 4;
		int yPos3 = 1;
		int orientation3 = BoardPiece.VERTICAL_ORIENTATION;
		int length3 = 1;
		int expectedState3 = BoardPieceState.STATE_ALIVE;
		int groupID3 = IdGenerator.getInstance().generateID();
		
		DefaultBoardPiece boardPiece3 =  new DefaultBoardPiece(
			xPos3, 
			yPos3, 
			orientation3, 
			length3, 
			expectedState3, 
			groupID3);
		
		defaultBoard.addBoardPiece(boardPiece3);
		
		// Piece 4
		int xPos4 = 1;
		int yPos4 = 4;
		int orientation4 = BoardPiece.HORIZONTAL_ORIENTATION;
		int length4 = 4;
		int expectedState4 = BoardPieceState.STATE_ALIVE;
		int groupID4 = IdGenerator.getInstance().generateID();
		
		DefaultBoardPiece boardPiece4 =  new DefaultBoardPiece(
			xPos4, 
			yPos4, 
			orientation4, 
			length4, 
			expectedState4, 
			groupID4);
		
		defaultBoard.addBoardPiece(boardPiece4);
		
		// Piece 5
		int xPos5 = 0;
		int yPos5 = 5;
		int orientation5 = BoardPiece.HORIZONTAL_ORIENTATION;
		int length5 = 1;
		int expectedState5 = BoardPieceState.STATE_ALIVE;
		int groupID5 = IdGenerator.getInstance().generateID();
		
		DefaultBoardPiece boardPiece5 =  new DefaultBoardPiece(
			xPos5, 
			yPos5, 
			orientation5, 
			length5, 
			expectedState5, 
			groupID5);
		
		defaultBoard.addBoardPiece(boardPiece5);
		
		// Piece 6
		int xPos6 = 5;
		int yPos6 = 5;
		int orientation6 = BoardPiece.HORIZONTAL_ORIENTATION;
		int length6 = 1;
		int expectedState6 = BoardPieceState.STATE_ALIVE;
		int groupID6 = IdGenerator.getInstance().generateID();
		
		DefaultBoardPiece boardPiece6 =  new DefaultBoardPiece(
			xPos6, 
			yPos6, 
			orientation6, 
			length6, 
			expectedState6, 
			groupID6);
		
		defaultBoard.addBoardPiece(boardPiece6);
		
		// Get the vertical legend
		int[] verticalLegend = defaultBoard.getVerticalLegend();
		
		assertNotNull(verticalLegend);
		assertEquals(boardHeight, verticalLegend.length);
		
		for (int i = 0; i < boardHeight; i++) {
			assertEquals(verticalLegendKey[i], verticalLegend[i]);
		}

		boardPiece1 = null;
		boardPiece2 = null;
		boardPiece3 = null;
		boardPiece4 = null;
		boardPiece5 = null;
		boardPiece6 = null;
		verticalLegendKey = null;
		verticalLegend = null;
		defaultBoard = null;
	}
	
	/**
	 * Test the horizontal legend and make sure it is correct for the layout
	 * of the board.
	 */
	public void testHorizontalLegend() {
		
		int boardWidth = 6;
		int boardHeight = 6;
		
		DefaultBoard defaultBoard = new DefaultBoard(boardWidth, boardHeight);
		
		// Validate that we got a board back.
		assertNotNull(defaultBoard);
		
		// Add a series of pieces to the board and then confirm the vertical 
		// legend is correct.
		//
		// d d a a d d
		// a d d d a d
		// a d d d d d
		// d d d d d d
		// d a a a a d
		// a d d d d a
		
		int[] horizontalLegendKey = new int[] {3, 1, 2, 2, 2, 1};
		
		// Piece 1
		int xPos1 = 2;
		int yPos1 = 0;
		int orientation1 = BoardPiece.HORIZONTAL_ORIENTATION;
		int length1 = 2;
		int expectedState1 = BoardPieceState.STATE_ALIVE;
		int groupID1 = IdGenerator.getInstance().generateID();
		
		DefaultBoardPiece boardPiece1 =  new DefaultBoardPiece(
			xPos1, 
			yPos1, 
			orientation1, 
			length1, 
			expectedState1, 
			groupID1);
		
		defaultBoard.addBoardPiece(boardPiece1);
		
		// Piece 2
		int xPos2 = 0;
		int yPos2 = 1;
		int orientation2 = BoardPiece.VERTICAL_ORIENTATION;
		int length2 = 2;
		int expectedState2 = BoardPieceState.STATE_ALIVE;
		int groupID2 = IdGenerator.getInstance().generateID();
		
		DefaultBoardPiece boardPiece2 =  new DefaultBoardPiece(
			xPos2, 
			yPos2, 
			orientation2, 
			length2, 
			expectedState2, 
			groupID2);
		
		defaultBoard.addBoardPiece(boardPiece2);
		
		// Piece 3
		int xPos3 = 4;
		int yPos3 = 1;
		int orientation3 = BoardPiece.VERTICAL_ORIENTATION;
		int length3 = 1;
		int expectedState3 = BoardPieceState.STATE_ALIVE;
		int groupID3 = IdGenerator.getInstance().generateID();
		
		DefaultBoardPiece boardPiece3 =  new DefaultBoardPiece(
			xPos3, 
			yPos3, 
			orientation3, 
			length3, 
			expectedState3, 
			groupID3);
		
		defaultBoard.addBoardPiece(boardPiece3);
		
		// Piece 4
		int xPos4 = 1;
		int yPos4 = 4;
		int orientation4 = BoardPiece.HORIZONTAL_ORIENTATION;
		int length4 = 4;
		int expectedState4 = BoardPieceState.STATE_ALIVE;
		int groupID4 = IdGenerator.getInstance().generateID();
		
		DefaultBoardPiece boardPiece4 =  new DefaultBoardPiece(
			xPos4, 
			yPos4, 
			orientation4, 
			length4, 
			expectedState4, 
			groupID4);
		
		defaultBoard.addBoardPiece(boardPiece4);
		
		// Piece 5
		int xPos5 = 0;
		int yPos5 = 5;
		int orientation5 = BoardPiece.HORIZONTAL_ORIENTATION;
		int length5 = 1;
		int expectedState5 = BoardPieceState.STATE_ALIVE;
		int groupID5 = IdGenerator.getInstance().generateID();
		
		DefaultBoardPiece boardPiece5 =  new DefaultBoardPiece(
			xPos5, 
			yPos5, 
			orientation5, 
			length5, 
			expectedState5, 
			groupID5);
		
		defaultBoard.addBoardPiece(boardPiece5);
		
		// Piece 6
		int xPos6 = 5;
		int yPos6 = 5;
		int orientation6 = BoardPiece.HORIZONTAL_ORIENTATION;
		int length6 = 1;
		int expectedState6 = BoardPieceState.STATE_ALIVE;
		int groupID6 = IdGenerator.getInstance().generateID();
		
		DefaultBoardPiece boardPiece6 =  new DefaultBoardPiece(
			xPos6, 
			yPos6, 
			orientation6, 
			length6, 
			expectedState6, 
			groupID6);
		
		defaultBoard.addBoardPiece(boardPiece6);
		
		// Get the vertical legend
		int[] horizontalLegend = defaultBoard.getHorizontalLegend();
		
		assertNotNull(horizontalLegend);
		assertEquals(boardWidth, horizontalLegend.length);
		
		for (int i = 0; i < boardWidth; i++) {
			assertEquals(horizontalLegendKey[i], horizontalLegend[i]);
		}

		boardPiece1 = null;
		boardPiece2 = null;
		boardPiece3 = null;
		boardPiece4 = null;
		boardPiece5 = null;
		boardPiece6 = null;
		horizontalLegendKey = null;
		horizontalLegend = null;
		defaultBoard = null;
	}
	
	/**
	 * Test that the vertical hints are correct.
	 */
	public void testVerticalHints() {
		
		int boardWidth = 6;
		int boardHeight = 6;
		
		DefaultBoard defaultBoard = new DefaultBoard(boardWidth, boardHeight);
		
		// Validate that we got a board back.
		assertNotNull(defaultBoard);
		
		// Add a series of pieces to the board and then confirm the vertical 
		// legend hint is correct. The diagram below shows the board layout
		// and the resulting test configuration.
		//
		// d d a a d d --> d d A a d d
		// a d d d a d --> a A A d a d
		// a d d d d d --> A A d d d d
		// d d d d d d --> d d d d d d
		// d a a a a d --> d A A a a d
		// a d d d d a --> A d d d d A
		
		int[] verticalLegendHintKey = new int[] {
				BoardPerimeterLegend.HINT_TOO_FEW, 
				BoardPerimeterLegend.HINT_ALMOST_CORRECT, 
				BoardPerimeterLegend.HINT_TOO_MANY, 
				BoardPerimeterLegend.HINT_ALL_CORRECT, 
				BoardPerimeterLegend.HINT_TOO_FEW,
				BoardPerimeterLegend.HINT_ALL_CORRECT};
		
		// Piece 1
		int xPos1 = 2;
		int yPos1 = 0;
		int orientation1 = BoardPiece.HORIZONTAL_ORIENTATION;
		int length1 = 2;
		int expectedState1 = BoardPieceState.STATE_ALIVE;
		int groupID1 = IdGenerator.getInstance().generateID();
		
		DefaultBoardPiece boardPiece1 =  new DefaultBoardPiece(
			xPos1, 
			yPos1, 
			orientation1, 
			length1, 
			expectedState1, 
			groupID1);
		
		defaultBoard.addBoardPiece(boardPiece1);
		
		// Piece 2
		int xPos2 = 0;
		int yPos2 = 1;
		int orientation2 = BoardPiece.VERTICAL_ORIENTATION;
		int length2 = 2;
		int expectedState2 = BoardPieceState.STATE_ALIVE;
		int groupID2 = IdGenerator.getInstance().generateID();
		
		DefaultBoardPiece boardPiece2 =  new DefaultBoardPiece(
			xPos2, 
			yPos2, 
			orientation2, 
			length2, 
			expectedState2, 
			groupID2);
		
		defaultBoard.addBoardPiece(boardPiece2);
		
		// Piece 3
		int xPos3 = 4;
		int yPos3 = 1;
		int orientation3 = BoardPiece.VERTICAL_ORIENTATION;
		int length3 = 1;
		int expectedState3 = BoardPieceState.STATE_ALIVE;
		int groupID3 = IdGenerator.getInstance().generateID();
		
		DefaultBoardPiece boardPiece3 =  new DefaultBoardPiece(
			xPos3, 
			yPos3, 
			orientation3, 
			length3, 
			expectedState3, 
			groupID3);
		
		defaultBoard.addBoardPiece(boardPiece3);
		
		// Piece 4
		int xPos4 = 1;
		int yPos4 = 4;
		int orientation4 = BoardPiece.HORIZONTAL_ORIENTATION;
		int length4 = 4;
		int expectedState4 = BoardPieceState.STATE_ALIVE;
		int groupID4 = IdGenerator.getInstance().generateID();
		
		DefaultBoardPiece boardPiece4 =  new DefaultBoardPiece(
			xPos4, 
			yPos4, 
			orientation4, 
			length4, 
			expectedState4, 
			groupID4);
		
		defaultBoard.addBoardPiece(boardPiece4);
		
		// Piece 5
		int xPos5 = 0;
		int yPos5 = 5;
		int orientation5 = BoardPiece.HORIZONTAL_ORIENTATION;
		int length5 = 1;
		int expectedState5 = BoardPieceState.STATE_ALIVE;
		int groupID5 = IdGenerator.getInstance().generateID();
		
		DefaultBoardPiece boardPiece5 =  new DefaultBoardPiece(
			xPos5, 
			yPos5, 
			orientation5, 
			length5, 
			expectedState5, 
			groupID5);
		
		defaultBoard.addBoardPiece(boardPiece5);
		
		// Piece 6
		int xPos6 = 5;
		int yPos6 = 5;
		int orientation6 = BoardPiece.HORIZONTAL_ORIENTATION;
		int length6 = 1;
		int expectedState6 = BoardPieceState.STATE_ALIVE;
		int groupID6 = IdGenerator.getInstance().generateID();
		
		DefaultBoardPiece boardPiece6 =  new DefaultBoardPiece(
			xPos6, 
			yPos6, 
			orientation6, 
			length6, 
			expectedState6, 
			groupID6);
		
		defaultBoard.addBoardPiece(boardPiece6);
		
		// Setup the board states according to the test specified above in the
		// diagram.
		defaultBoard.pickBoardLocation(2, 0);
		((DefaultBoardPiece)defaultBoard.getLastPick()).setState(
				BoardPieceState.STATE_ALIVE);
		
		defaultBoard.pickBoardLocation(1, 1);
		((DefaultBoardPiece)defaultBoard.getLastPick()).setState(
				BoardPieceState.STATE_ALIVE);
		
		defaultBoard.pickBoardLocation(2, 1);
		((DefaultBoardPiece)defaultBoard.getLastPick()).setState(
				BoardPieceState.STATE_ALIVE);
		
		defaultBoard.pickBoardLocation(0, 2);
		((DefaultBoardPiece)defaultBoard.getLastPick()).setState(
				BoardPieceState.STATE_ALIVE);
		
		defaultBoard.pickBoardLocation(1, 2);
		((DefaultBoardPiece)defaultBoard.getLastPick()).setState(
				BoardPieceState.STATE_ALIVE);
		
		defaultBoard.pickBoardLocation(1, 4);
		((DefaultBoardPiece)defaultBoard.getLastPick()).setState(
				BoardPieceState.STATE_ALIVE);
		
		defaultBoard.pickBoardLocation(2, 4);
		((DefaultBoardPiece)defaultBoard.getLastPick()).setState(
				BoardPieceState.STATE_ALIVE);
		
		defaultBoard.pickBoardLocation(0, 5);
		((DefaultBoardPiece)defaultBoard.getLastPick()).setState(
				BoardPieceState.STATE_ALIVE);
		
		defaultBoard.pickBoardLocation(5, 5);
		((DefaultBoardPiece)defaultBoard.getLastPick()).setState(
				BoardPieceState.STATE_ALIVE);
		Log.i("TiluxeBoardTest - vertical hint test", "vertical hint test");
		// Get the vertical legend hint
		int[] verticalLegendHint = defaultBoard.getVerticalLegendHint();
		
		assertNotNull(verticalLegendHint);
		assertEquals(boardHeight, verticalLegendHint.length);
		
		for (int i = 0; i < boardHeight; i++) {
			assertEquals(verticalLegendHintKey[i], verticalLegendHint[i]);
		}

		boardPiece1 = null;
		boardPiece2 = null;
		boardPiece3 = null;
		boardPiece4 = null;
		boardPiece5 = null;
		boardPiece6 = null;
		verticalLegendHintKey = null;
		verticalLegendHint = null;
		defaultBoard = null;
	}
	
	/**
	 * Test that the horizontal legend hints are correct.
	 */
	public void testHorizontalHints() {
		
		int boardWidth = 6;
		int boardHeight = 6;
		
		DefaultBoard defaultBoard = new DefaultBoard(boardWidth, boardHeight);
		
		// Validate that we got a board back.
		assertNotNull(defaultBoard);
		
		// Add a series of pieces to the board and then confirm the vertical 
		// legend hint is correct. The diagram below shows the board layout
		// and the resulting test configuration.
		//
		// d d a a d d --> d d A a d d
		// a d d d a d --> a A A d a d
		// a d d d d d --> A A d d d d
		// d d d d d d --> d d d d d d
		// d a a a a d --> d A A a a d
		// a d d d d a --> A d d d d A
		
		int[] horizontalLegendHintKey = new int[] {
				BoardPerimeterLegend.HINT_TOO_FEW, 
				BoardPerimeterLegend.HINT_TOO_MANY, 
				BoardPerimeterLegend.HINT_TOO_MANY, 
				BoardPerimeterLegend.HINT_TOO_FEW, 
				BoardPerimeterLegend.HINT_TOO_FEW,
				BoardPerimeterLegend.HINT_ALL_CORRECT};
		
		// Piece 1
		int xPos1 = 2;
		int yPos1 = 0;
		int orientation1 = BoardPiece.HORIZONTAL_ORIENTATION;
		int length1 = 2;
		int expectedState1 = BoardPieceState.STATE_ALIVE;
		int groupID1 = IdGenerator.getInstance().generateID();
		
		DefaultBoardPiece boardPiece1 =  new DefaultBoardPiece(
			xPos1, 
			yPos1, 
			orientation1, 
			length1, 
			expectedState1, 
			groupID1);
		
		defaultBoard.addBoardPiece(boardPiece1);
		
		// Piece 2
		int xPos2 = 0;
		int yPos2 = 1;
		int orientation2 = BoardPiece.VERTICAL_ORIENTATION;
		int length2 = 2;
		int expectedState2 = BoardPieceState.STATE_ALIVE;
		int groupID2 = IdGenerator.getInstance().generateID();
		
		DefaultBoardPiece boardPiece2 =  new DefaultBoardPiece(
			xPos2, 
			yPos2, 
			orientation2, 
			length2, 
			expectedState2, 
			groupID2);
		
		defaultBoard.addBoardPiece(boardPiece2);
		
		// Piece 3
		int xPos3 = 4;
		int yPos3 = 1;
		int orientation3 = BoardPiece.VERTICAL_ORIENTATION;
		int length3 = 1;
		int expectedState3 = BoardPieceState.STATE_ALIVE;
		int groupID3 = IdGenerator.getInstance().generateID();
		
		DefaultBoardPiece boardPiece3 =  new DefaultBoardPiece(
			xPos3, 
			yPos3, 
			orientation3, 
			length3, 
			expectedState3, 
			groupID3);
		
		defaultBoard.addBoardPiece(boardPiece3);
		
		// Piece 4
		int xPos4 = 1;
		int yPos4 = 4;
		int orientation4 = BoardPiece.HORIZONTAL_ORIENTATION;
		int length4 = 4;
		int expectedState4 = BoardPieceState.STATE_ALIVE;
		int groupID4 = IdGenerator.getInstance().generateID();
		
		DefaultBoardPiece boardPiece4 =  new DefaultBoardPiece(
			xPos4, 
			yPos4, 
			orientation4, 
			length4, 
			expectedState4, 
			groupID4);
		
		defaultBoard.addBoardPiece(boardPiece4);
		
		// Piece 5
		int xPos5 = 0;
		int yPos5 = 5;
		int orientation5 = BoardPiece.HORIZONTAL_ORIENTATION;
		int length5 = 1;
		int expectedState5 = BoardPieceState.STATE_ALIVE;
		int groupID5 = IdGenerator.getInstance().generateID();
		
		DefaultBoardPiece boardPiece5 =  new DefaultBoardPiece(
			xPos5, 
			yPos5, 
			orientation5, 
			length5, 
			expectedState5, 
			groupID5);
		
		defaultBoard.addBoardPiece(boardPiece5);
		
		// Piece 6
		int xPos6 = 5;
		int yPos6 = 5;
		int orientation6 = BoardPiece.HORIZONTAL_ORIENTATION;
		int length6 = 1;
		int expectedState6 = BoardPieceState.STATE_ALIVE;
		int groupID6 = IdGenerator.getInstance().generateID();
		
		DefaultBoardPiece boardPiece6 =  new DefaultBoardPiece(
			xPos6, 
			yPos6, 
			orientation6, 
			length6, 
			expectedState6, 
			groupID6);
		
		defaultBoard.addBoardPiece(boardPiece6);
		
		// Setup the board states according to the test specified above in the
		// diagram.
		defaultBoard.pickBoardLocation(2, 0);
		((DefaultBoardPiece)defaultBoard.getLastPick()).setState(
				BoardPieceState.STATE_ALIVE);
		
		defaultBoard.pickBoardLocation(1, 1);
		((DefaultBoardPiece)defaultBoard.getLastPick()).setState(
				BoardPieceState.STATE_ALIVE);
		
		defaultBoard.pickBoardLocation(2, 1);
		((DefaultBoardPiece)defaultBoard.getLastPick()).setState(
				BoardPieceState.STATE_ALIVE);
		
		defaultBoard.pickBoardLocation(0, 2);
		((DefaultBoardPiece)defaultBoard.getLastPick()).setState(
				BoardPieceState.STATE_ALIVE);
		
		defaultBoard.pickBoardLocation(1, 2);
		((DefaultBoardPiece)defaultBoard.getLastPick()).setState(
				BoardPieceState.STATE_ALIVE);
		
		defaultBoard.pickBoardLocation(1, 4);
		((DefaultBoardPiece)defaultBoard.getLastPick()).setState(
				BoardPieceState.STATE_ALIVE);
		
		defaultBoard.pickBoardLocation(2, 4);
		((DefaultBoardPiece)defaultBoard.getLastPick()).setState(
				BoardPieceState.STATE_ALIVE);
		
		defaultBoard.pickBoardLocation(0, 5);
		((DefaultBoardPiece)defaultBoard.getLastPick()).setState(
				BoardPieceState.STATE_ALIVE);
		
		defaultBoard.pickBoardLocation(5, 5);
		((DefaultBoardPiece)defaultBoard.getLastPick()).setState(
				BoardPieceState.STATE_ALIVE);
		
		// Get the vertical legend hint
		int[] horizontalLegendHint = defaultBoard.getHorizontalLegendHint();
		
		assertNotNull(horizontalLegendHint);
		assertEquals(boardWidth, horizontalLegendHint.length);
		
		for (int i = 0; i < boardWidth; i++) {
			assertEquals(horizontalLegendHintKey[i], horizontalLegendHint[i]);
		}

		boardPiece1 = null;
		boardPiece2 = null;
		boardPiece3 = null;
		boardPiece4 = null;
		boardPiece5 = null;
		boardPiece6 = null;
		horizontalLegendHintKey = null;
		horizontalLegendHint = null;
		defaultBoard = null;
	}
}
