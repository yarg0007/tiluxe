/*****************************************************************************
 *                        Copyright ASB Technologies (c) 2011
 *                               Java Source
 *
 * This source is the property of ASB Technologies. Any duplication or reuse
 * without the consent of ASB Technologies is prohibited.
 *
 ****************************************************************************/

package com.asbtechnologies.android.tiluxe.test;

//External Imports
import android.util.Log;

import com.asbtechnologies.android.tiluxe.board.BoardPiece;
import com.asbtechnologies.android.tiluxe.board.BoardPieceState;
import com.asbtechnologies.android.tiluxe.board.DefaultBoard;
import com.asbtechnologies.android.tiluxe.board.DefaultBoardGenerator;
import com.asbtechnologies.android.tiluxe.board.DefaultBoardPiece;
import com.asbtechnologies.android.tiluxe.util.IdGenerator;

import junit.framework.TestCase;

//Internal Imports

/**
 * Unit tests for DefaultBoardGenerator implementation.
 * 
 * @author Ben Yarger
 * @version $Revision: 1.4 $
 */
public class TiluxeBoardGeneratorTest extends TestCase {

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
	 * Default constructor.
	 * 
	 * @param name
	 */
	public TiluxeBoardGeneratorTest(String name) {
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
	
	/**
	 * Test that a board is returned.
	 */
	public void testGenerateDefaultBoard() {
		
		DefaultBoardGenerator boardGenerator = new DefaultBoardGenerator();
		
		DefaultBoard playingBoard = 
			(DefaultBoard) boardGenerator.generateDefaultBoard();
		
		assertNotNull(playingBoard);
		
		boardGenerator = null;
		playingBoard = null;
	}
	
	public void testGenerateCustomBoard() {
		
		DefaultBoardGenerator boardGenerator = new DefaultBoardGenerator();
		
		DefaultBoard playingBoard = 
			(DefaultBoard) boardGenerator.generateCustomBoard(
					DEFAULT_BOARD_WIDTH, 
					DEFAULT_BOARD_HEIGHT, 
					DEFAULT_PIECE_LENGTHS, 
					0);
		
		assertNotNull(playingBoard);
		
		playingBoard.printBoard();
		
		// Generate another board
		playingBoard = 
			(DefaultBoard) boardGenerator.generateCustomBoard(
					DEFAULT_BOARD_WIDTH, 
					DEFAULT_BOARD_HEIGHT, 
					DEFAULT_PIECE_LENGTHS, 
					4);
		
		assertNotNull(playingBoard);
		
		playingBoard.printBoard();
		
		// Generate another board
		playingBoard = 
			(DefaultBoard) boardGenerator.generateCustomBoard(
					DEFAULT_BOARD_WIDTH, 
					DEFAULT_BOARD_HEIGHT, 
					DEFAULT_PIECE_LENGTHS, 
					4);
		
		assertNotNull(playingBoard);
		
		playingBoard.printBoard();
		
		// Generate another board
		playingBoard = 
			(DefaultBoard) boardGenerator.generateCustomBoard(
					DEFAULT_BOARD_WIDTH, 
					DEFAULT_BOARD_HEIGHT, 
					DEFAULT_PIECE_LENGTHS, 
					8);
		
		assertNotNull(playingBoard);
		
		playingBoard.printBoard();
		Log.i("TEST_GENERATE_CUSTOM_BOARD", "problem spot checking");
		// Generate another board
		playingBoard = 
			(DefaultBoard) boardGenerator.generateCustomBoard(
					DEFAULT_BOARD_WIDTH, 
					DEFAULT_BOARD_HEIGHT, 
					DEFAULT_PIECE_LENGTHS, 
					8);
		
		assertNotNull(playingBoard);
		
		playingBoard.printBoard();
		
		boardGenerator = null;
		playingBoard = null;
	}
	
	/**
	 * Test the placement check around a piece of length one.
	 */
	public void testPlacementCheckAroundLengthOne() {
		
		DefaultBoardGenerator boardGenerator = new DefaultBoardGenerator();
		
		DefaultBoard board = (DefaultBoard) 
			boardGenerator.generateCustomBoard(
					DEFAULT_BOARD_WIDTH, DEFAULT_BOARD_HEIGHT, new int[0], 0);
		
		DefaultBoardPiece boardPiece = new DefaultBoardPiece(
				4,
				4, 
				BoardPiece.VERTICAL_ORIENTATION, 
				1, 
				BoardPieceState.STATE_ALIVE, 
				IdGenerator.getInstance().generateID());
		
		board.addBoardPiece(boardPiece);
		
		// These should all be illegal placements
		assertFalse(boardGenerator.isValidPlacement(3, 3, 0, 1));
		assertFalse(boardGenerator.isValidPlacement(3, 4, 0, 1));
		assertFalse(boardGenerator.isValidPlacement(3, 5, 0, 1));
		assertFalse(boardGenerator.isValidPlacement(4, 3, 0, 1));
		assertFalse(boardGenerator.isValidPlacement(4, 4, 0, 1));
		assertFalse(boardGenerator.isValidPlacement(4, 5, 0, 1));
		assertFalse(boardGenerator.isValidPlacement(5, 3, 0, 1));
		assertFalse(boardGenerator.isValidPlacement(5, 4, 0, 1));
		assertFalse(boardGenerator.isValidPlacement(5, 5, 0, 1));
		
		// These should also be failures
		assertFalse(boardGenerator.isValidPlacement(4, 2, 1, 2));
		assertFalse(boardGenerator.isValidPlacement(2, 4, 0, 2));
		assertFalse(boardGenerator.isValidPlacement(5, 2, 1, 4));
		
		// These should all be legal placements
		assertTrue(boardGenerator.isValidPlacement(2, 3, 0, 1));
		assertTrue(boardGenerator.isValidPlacement(3, 2, 0, 1));
		assertTrue(boardGenerator.isValidPlacement(5, 2, 0, 1));
		assertTrue(boardGenerator.isValidPlacement(5, 6, 0, 1));
		assertTrue(boardGenerator.isValidPlacement(6, 4, 0, 1));
		assertTrue(boardGenerator.isValidPlacement(2, 4, 0, 1));
		assertTrue(boardGenerator.isValidPlacement(4, 6, 0, 1));
		assertTrue(boardGenerator.isValidPlacement(6, 6, 0, 1));
		
		// These should also be legal placements
		assertTrue(boardGenerator.isValidPlacement(2, 2, 1, 4));
		assertTrue(boardGenerator.isValidPlacement(6, 2, 0, 3));
		assertTrue(boardGenerator.isValidPlacement(6, 3, 0, 3));
		
		boardPiece = null;
		board = null;
		boardGenerator = null;
	}
	
	/**
	 * Test the placement check around a piece of length two.
	 */
	public void testPlacementCheckAroundLengthTwo() {
	
		DefaultBoardGenerator boardGenerator = new DefaultBoardGenerator();
		
		DefaultBoard board = (DefaultBoard) 
			boardGenerator.generateCustomBoard(
					DEFAULT_BOARD_WIDTH, DEFAULT_BOARD_HEIGHT, new int[0], 0);
		
		DefaultBoardPiece boardPiece = new DefaultBoardPiece(
				4,
				4, 
				BoardPiece.VERTICAL_ORIENTATION, 
				2, 
				BoardPieceState.STATE_ALIVE, 
				IdGenerator.getInstance().generateID());
		
		board.addBoardPiece(boardPiece);
		
		// These should all be illegal placements
		assertFalse(boardGenerator.isValidPlacement(3, 3, 0, 1));
		assertFalse(boardGenerator.isValidPlacement(3, 6, 0, 1));
		assertFalse(boardGenerator.isValidPlacement(5, 6, 0, 1));
		assertFalse(boardGenerator.isValidPlacement(5, 3, 0, 1));
		
		// These should also be failures
		assertFalse(boardGenerator.isValidPlacement(4, 2, 1, 2));
		assertFalse(boardGenerator.isValidPlacement(2, 4, 0, 2));
		assertFalse(boardGenerator.isValidPlacement(5, 2, 1, 4));
		
		// These should all be legal placements
		assertTrue(boardGenerator.isValidPlacement(2, 3, 0, 1));
		assertTrue(boardGenerator.isValidPlacement(3, 2, 0, 1));
		assertTrue(boardGenerator.isValidPlacement(5, 2, 0, 1));
		assertTrue(boardGenerator.isValidPlacement(5, 7, 0, 1));
		assertTrue(boardGenerator.isValidPlacement(4, 7, 0, 1));
		
		// These should also be legal placements
		assertTrue(boardGenerator.isValidPlacement(2, 2, 1, 4));
		assertTrue(boardGenerator.isValidPlacement(6, 2, 0, 4));
		assertTrue(boardGenerator.isValidPlacement(4, 7, 0, 3));
		
		boardPiece = null;
		board = null;
		boardGenerator = null;
	}
	
	/**
	 * Test the placement check around a piece of length four.
	 */
	public void testPlacementCheckAroundLengthFour() {
		
		DefaultBoardGenerator boardGenerator = new DefaultBoardGenerator();
		
		DefaultBoard board = (DefaultBoard) 
			boardGenerator.generateCustomBoard(
					DEFAULT_BOARD_WIDTH, DEFAULT_BOARD_HEIGHT, new int[0], 0);
		
		DefaultBoardPiece boardPiece = new DefaultBoardPiece(
				4,
				4, 
				BoardPiece.VERTICAL_ORIENTATION, 
				4, 
				BoardPieceState.STATE_ALIVE, 
				IdGenerator.getInstance().generateID());
		
		board.addBoardPiece(boardPiece);
		
		// These should all be illegal placements
		assertFalse(boardGenerator.isValidPlacement(3, 3, 0, 1));
		assertFalse(boardGenerator.isValidPlacement(3, 8, 0, 1));
		assertFalse(boardGenerator.isValidPlacement(5, 8, 0, 1));
		assertFalse(boardGenerator.isValidPlacement(5, 3, 0, 1));
		assertFalse(boardGenerator.isValidPlacement(3, 6, 0, 1));
		assertFalse(boardGenerator.isValidPlacement(4, 6, 0, 1));
		assertFalse(boardGenerator.isValidPlacement(5, 6, 0, 1));
		
		// These should also be failures
		assertFalse(boardGenerator.isValidPlacement(3, 0, 1, 4));
		assertFalse(boardGenerator.isValidPlacement(3, 8, 0, 3));
		assertFalse(boardGenerator.isValidPlacement(5, 4, 0, 4));
		
		boardPiece = null;
		board = null;
		boardGenerator = null;
	}
	
	/**
	 * Test the placement check around a piece of length one.
	 */
	public void testDiagonalThreeSpecialCase() {
		
		// This is the test case that is an illegal board result
		// 04-05 09:12:11.759: INFO/PrintBoard(17815): 0 0 0 0 X 0 0 X 0 0 |2
		// 04-05 09:12:11.759: INFO/PrintBoard(17815): 0 0 0 0 0 0 0 0 0 0 |0
		// 04-05 09:12:11.769: INFO/PrintBoard(17815): 0 0 0 0 0 X X X 0 0 |3
		// 04-05 09:12:11.769: INFO/PrintBoard(17815): 0 0 X X 0 0 0 0 X 0 |3
		// 04-05 09:12:11.769: INFO/PrintBoard(17815): 0 0 0 0 0 0 X 0 X 0 |2
		// 04-05 09:12:11.769: INFO/PrintBoard(17815): 0 X 0 0 X 0 X 0 X 0 |4
		// 04-05 09:12:11.769: INFO/PrintBoard(17815): 0 0 0 0 X 0 0 0 X 0 |2
		// 04-05 09:12:11.769: INFO/PrintBoard(17815): 0 0 0 0 X 0 0 0 0 0 |1
		// 04-05 09:12:11.769: INFO/PrintBoard(17815): X X 0 0 0 0 X 0 0 0 |3
		// 04-05 09:12:11.769: INFO/PrintBoard(17815): 0 0 0 0 0 0 0 0 0 0 |0
		// 04-05 09:12:11.769: INFO/PrintBoard(17815): --------------------
		// 04-05 09:12:11.769: INFO/PrintBoard(17815): 1 2 1 1 4 1 4 2 4 0 
		
		DefaultBoardGenerator boardGenerator = new DefaultBoardGenerator();
		
		DefaultBoard board = (DefaultBoard) 
			boardGenerator.generateCustomBoard(
					DEFAULT_BOARD_WIDTH, DEFAULT_BOARD_HEIGHT, new int[0], 0);
		
		DefaultBoardPiece boardPiece = new DefaultBoardPiece(
				5,
				2, 
				BoardPiece.HORIZONTAL_ORIENTATION, 
				3, 
				BoardPieceState.STATE_ALIVE, 
				IdGenerator.getInstance().generateID());
		
		board.addBoardPiece(boardPiece);
		
		// Test the addition of a new piece 
		assertFalse(boardGenerator.isValidPlacement(
				8, 3, BoardPiece.VERTICAL_ORIENTATION, 3));
		
		// Now test again with the vertical piece being generated first
		board = (DefaultBoard) 
			boardGenerator.generateCustomBoard(DEFAULT_BOARD_WIDTH, DEFAULT_BOARD_HEIGHT, new int[0], 0);
		
		boardPiece = new DefaultBoardPiece(
				8,
				3, 
				BoardPiece.VERTICAL_ORIENTATION, 
				3, 
				BoardPieceState.STATE_ALIVE, 
				IdGenerator.getInstance().generateID());
		
		board.addBoardPiece(boardPiece);
		
		// Test the addition of a new piece 
		assertFalse(boardGenerator.isValidPlacement(
				5, 2, BoardPiece.HORIZONTAL_ORIENTATION, 3));
		
		boardPiece = null;
		board = null;
		boardGenerator = null;
	}
	
	/**
	 * Test the placement validation in and around the top left corner.
	 */
	public void testTopLeftCorner() {
		
		//-------------------------------------------------------
		// Test the case where the active piece is in the corner.
		//-------------------------------------------------------
		DefaultBoardGenerator boardGenerator = new DefaultBoardGenerator();
		
		DefaultBoard board = (DefaultBoard) 
			boardGenerator.generateCustomBoard(
					DEFAULT_BOARD_WIDTH, DEFAULT_BOARD_HEIGHT, new int[0], 0);
		
		DefaultBoardPiece boardPiece = new DefaultBoardPiece(
				0,
				0, 
				BoardPiece.VERTICAL_ORIENTATION, 
				1, 
				BoardPieceState.STATE_ALIVE, 
				IdGenerator.getInstance().generateID());
		
		board.addBoardPiece(boardPiece);
		
		// These should all be illegal placements
		assertFalse(boardGenerator.isValidPlacement(0, 0, 0, 1));
		assertFalse(boardGenerator.isValidPlacement(0, 1, 0, 1));
		assertFalse(boardGenerator.isValidPlacement(1, 1, 0, 1));
		assertFalse(boardGenerator.isValidPlacement(1, 0, 0, 1));

		// These should all be legal placements
		assertTrue(boardGenerator.isValidPlacement(2, 0, 0, 1));
		assertTrue(boardGenerator.isValidPlacement(2, 2, 0, 1));
		assertTrue(boardGenerator.isValidPlacement(0, 2, 0, 1));
		
		//-------------------------------------------------
		// Test the case where the active piece is at 1,1.
		//-------------------------------------------------
		board = (DefaultBoard)
			boardGenerator.generateCustomBoard(
					DEFAULT_BOARD_WIDTH, DEFAULT_BOARD_HEIGHT, new int[0], 0);
		
		boardPiece = new DefaultBoardPiece(
				1,
				1, 
				BoardPiece.VERTICAL_ORIENTATION, 
				1, 
				BoardPieceState.STATE_ALIVE, 
				IdGenerator.getInstance().generateID());
		
		board.addBoardPiece(boardPiece);
		
		// These should all be illegal placements
		assertFalse(boardGenerator.isValidPlacement(0, 0, 0, 1));
		assertFalse(boardGenerator.isValidPlacement(0, 1, 0, 1));
		assertFalse(boardGenerator.isValidPlacement(1, 1, 0, 1));
		assertFalse(boardGenerator.isValidPlacement(1, 0, 0, 1));
		assertFalse(boardGenerator.isValidPlacement(2, 0, 0, 1));
		assertFalse(boardGenerator.isValidPlacement(2, 1, 0, 1));
		assertFalse(boardGenerator.isValidPlacement(2, 2, 0, 1));
		assertFalse(boardGenerator.isValidPlacement(1, 2, 0, 1));
		assertFalse(boardGenerator.isValidPlacement(0, 2, 0, 1));		
		
		boardPiece = null;
		board = null;
		boardGenerator = null;
	}
	
	/**
	 * Test the placement validation in and around the top right corner.
	 */
	public void testTopRightCorner() {
		
		//-------------------------------------------------------
		// Test the case where the active piece is in the corner.
		//-------------------------------------------------------
		DefaultBoardGenerator boardGenerator = new DefaultBoardGenerator();
		
		DefaultBoard board = (DefaultBoard) 
			boardGenerator.generateCustomBoard(
					DEFAULT_BOARD_WIDTH, DEFAULT_BOARD_HEIGHT, new int[0], 0);
		
		DefaultBoardPiece boardPiece = new DefaultBoardPiece(
				9,
				0, 
				BoardPiece.VERTICAL_ORIENTATION, 
				1, 
				BoardPieceState.STATE_ALIVE, 
				IdGenerator.getInstance().generateID());
		
		board.addBoardPiece(boardPiece);
		
		// These should all be illegal placements
		assertFalse(boardGenerator.isValidPlacement(9, 0, 0, 1));
		assertFalse(boardGenerator.isValidPlacement(8, 0, 0, 1));
		assertFalse(boardGenerator.isValidPlacement(8, 1, 0, 1));
		assertFalse(boardGenerator.isValidPlacement(9, 1, 0, 1));

		// These should all be legal placements
		assertTrue(boardGenerator.isValidPlacement(7, 0, 0, 1));
		assertTrue(boardGenerator.isValidPlacement(7, 2, 0, 1));
		assertTrue(boardGenerator.isValidPlacement(0, 2, 0, 1));
		
		//-------------------------------------------------
		// Test the case where the active piece is at 8,1.
		//-------------------------------------------------
		board = (DefaultBoard)
			boardGenerator.generateCustomBoard(
					DEFAULT_BOARD_WIDTH, DEFAULT_BOARD_HEIGHT, new int[0], 0);
		
		boardPiece = new DefaultBoardPiece(
				8,
				1, 
				BoardPiece.VERTICAL_ORIENTATION, 
				1, 
				BoardPieceState.STATE_ALIVE, 
				IdGenerator.getInstance().generateID());
		
		board.addBoardPiece(boardPiece);
		
		// These should all be illegal placements
		assertFalse(boardGenerator.isValidPlacement(9, 0, 0, 1));
		assertFalse(boardGenerator.isValidPlacement(8, 0, 0, 1));
		assertFalse(boardGenerator.isValidPlacement(8, 1, 0, 1));
		assertFalse(boardGenerator.isValidPlacement(9, 1, 0, 1));
		assertFalse(boardGenerator.isValidPlacement(7, 0, 0, 1));
		assertFalse(boardGenerator.isValidPlacement(7, 1, 0, 1));
		assertFalse(boardGenerator.isValidPlacement(7, 2, 0, 1));
		assertFalse(boardGenerator.isValidPlacement(8, 2, 0, 1));
		assertFalse(boardGenerator.isValidPlacement(9, 2, 0, 1));		
		
		boardPiece = null;
		board = null;
		boardGenerator = null;
	}
	
	/**
	 * Test the placement validation in and around the bottom right corner.
	 */
	public void testBottomRightCorner() {
		
		//-------------------------------------------------------
		// Test the case where the active piece is in the corner.
		//-------------------------------------------------------
		DefaultBoardGenerator boardGenerator = new DefaultBoardGenerator();
		
		DefaultBoard board = (DefaultBoard) 
			boardGenerator.generateCustomBoard(
					DEFAULT_BOARD_WIDTH, DEFAULT_BOARD_HEIGHT, new int[0], 0);
		
		DefaultBoardPiece boardPiece = new DefaultBoardPiece(
				9,
				9, 
				BoardPiece.VERTICAL_ORIENTATION, 
				1, 
				BoardPieceState.STATE_ALIVE, 
				IdGenerator.getInstance().generateID());
		
		board.addBoardPiece(boardPiece);
		
		// These should all be illegal placements
		assertFalse(boardGenerator.isValidPlacement(9, 9, 0, 1));
		assertFalse(boardGenerator.isValidPlacement(8, 9, 0, 1));
		assertFalse(boardGenerator.isValidPlacement(8, 8, 0, 1));
		assertFalse(boardGenerator.isValidPlacement(9, 8, 0, 1));

		// These should all be legal placements
		assertTrue(boardGenerator.isValidPlacement(7, 9, 0, 1));
		assertTrue(boardGenerator.isValidPlacement(7, 7, 0, 1));
		assertTrue(boardGenerator.isValidPlacement(9, 7, 0, 1));
		
		//-------------------------------------------------
		// Test the case where the active piece is at 8,8.
		//-------------------------------------------------
		board = (DefaultBoard)
			boardGenerator.generateCustomBoard(
					DEFAULT_BOARD_WIDTH, DEFAULT_BOARD_HEIGHT, new int[0], 0);
		
		boardPiece = new DefaultBoardPiece(
				8,
				8, 
				BoardPiece.VERTICAL_ORIENTATION, 
				1, 
				BoardPieceState.STATE_ALIVE, 
				IdGenerator.getInstance().generateID());
		
		board.addBoardPiece(boardPiece);
		
		// These should all be illegal placements
		assertFalse(boardGenerator.isValidPlacement(9, 9, 0, 1));
		assertFalse(boardGenerator.isValidPlacement(8, 9, 0, 1));
		assertFalse(boardGenerator.isValidPlacement(8, 8, 0, 1));
		assertFalse(boardGenerator.isValidPlacement(9, 8, 0, 1));
		assertFalse(boardGenerator.isValidPlacement(7, 9, 0, 1));
		assertFalse(boardGenerator.isValidPlacement(7, 8, 0, 1));
		assertFalse(boardGenerator.isValidPlacement(7, 7, 0, 1));
		assertFalse(boardGenerator.isValidPlacement(8, 7, 0, 1));
		assertFalse(boardGenerator.isValidPlacement(9, 7, 0, 1));		
		
		boardPiece = null;
		board = null;
		boardGenerator = null;
	}
	
	/**
	 * Test the placement validation in and around the bottom left corner.
	 */
	public void testBottomLeftCorner() {
		
		//-------------------------------------------------------
		// Test the case where the active piece is in the corner.
		//-------------------------------------------------------
		DefaultBoardGenerator boardGenerator = new DefaultBoardGenerator();
		
		DefaultBoard board = (DefaultBoard) 
			boardGenerator.generateCustomBoard(
					DEFAULT_BOARD_WIDTH, DEFAULT_BOARD_HEIGHT, new int[0], 0);
		
		DefaultBoardPiece boardPiece = new DefaultBoardPiece(
				0,
				9, 
				BoardPiece.VERTICAL_ORIENTATION, 
				1, 
				BoardPieceState.STATE_ALIVE, 
				IdGenerator.getInstance().generateID());
		
		board.addBoardPiece(boardPiece);
		
		// These should all be illegal placements
		assertFalse(boardGenerator.isValidPlacement(0, 9, 0, 1));
		assertFalse(boardGenerator.isValidPlacement(0, 8, 0, 1));
		assertFalse(boardGenerator.isValidPlacement(1, 8, 0, 1));
		assertFalse(boardGenerator.isValidPlacement(1, 9, 0, 1));

		// These should all be legal placements
		assertTrue(boardGenerator.isValidPlacement(0, 7, 0, 1));
		assertTrue(boardGenerator.isValidPlacement(2, 7, 0, 1));
		assertTrue(boardGenerator.isValidPlacement(2, 9, 0, 1));
		
		//-------------------------------------------------
		// Test the case where the active piece is at 1,8.
		//-------------------------------------------------
		board = (DefaultBoard)
			boardGenerator.generateCustomBoard(
					DEFAULT_BOARD_WIDTH, DEFAULT_BOARD_HEIGHT, new int[0], 0);
		
		boardPiece = new DefaultBoardPiece(
				1,
				8, 
				BoardPiece.VERTICAL_ORIENTATION, 
				1, 
				BoardPieceState.STATE_ALIVE, 
				IdGenerator.getInstance().generateID());
		
		board.addBoardPiece(boardPiece);
		
		// These should all be illegal placements
		assertFalse(boardGenerator.isValidPlacement(0, 9, 0, 1));
		assertFalse(boardGenerator.isValidPlacement(0, 8, 0, 1));
		assertFalse(boardGenerator.isValidPlacement(1, 8, 0, 1));
		assertFalse(boardGenerator.isValidPlacement(1, 9, 0, 1));
		assertFalse(boardGenerator.isValidPlacement(0, 7, 0, 1));
		assertFalse(boardGenerator.isValidPlacement(1, 7, 0, 1));
		assertFalse(boardGenerator.isValidPlacement(2, 7, 0, 1));
		assertFalse(boardGenerator.isValidPlacement(2, 8, 0, 1));
		assertFalse(boardGenerator.isValidPlacement(2, 9, 0, 1));		
		
		boardPiece = null;
		board = null;
		boardGenerator = null;
	}
	
	/**
	 * Test the placement validation along the top edge.
	 */
	public void testTop() {
		
		//---------------------------------------------------------
		// Test the case where the active piece is on the top edge.
		//---------------------------------------------------------
		DefaultBoardGenerator boardGenerator = new DefaultBoardGenerator();
		
		DefaultBoard board = (DefaultBoard) 
			boardGenerator.generateCustomBoard(
					DEFAULT_BOARD_WIDTH, DEFAULT_BOARD_HEIGHT, new int[0], 0);
		
		DefaultBoardPiece boardPiece = new DefaultBoardPiece(
				5,
				0, 
				BoardPiece.VERTICAL_ORIENTATION, 
				1, 
				BoardPieceState.STATE_ALIVE, 
				IdGenerator.getInstance().generateID());
		
		board.addBoardPiece(boardPiece);
		
		// These should all be illegal placements
		assertFalse(boardGenerator.isValidPlacement(5, 0, 0, 1));
		assertFalse(boardGenerator.isValidPlacement(4, 0, 0, 1));
		assertFalse(boardGenerator.isValidPlacement(4, 1, 0, 1));
		assertFalse(boardGenerator.isValidPlacement(5, 1, 0, 1));
		assertFalse(boardGenerator.isValidPlacement(6, 1, 0, 1));
		assertFalse(boardGenerator.isValidPlacement(6, 0, 0, 1));
		
		//-------------------------------------------------
		// Test the case where the active piece is at 5,1.
		//-------------------------------------------------
		board = (DefaultBoard)
			boardGenerator.generateCustomBoard(
					DEFAULT_BOARD_WIDTH, DEFAULT_BOARD_HEIGHT, new int[0], 0);
		
		boardPiece = new DefaultBoardPiece(
				5,
				1, 
				BoardPiece.VERTICAL_ORIENTATION, 
				1, 
				BoardPieceState.STATE_ALIVE, 
				IdGenerator.getInstance().generateID());
		
		board.addBoardPiece(boardPiece);
		
		// These should all be illegal placements
		assertFalse(boardGenerator.isValidPlacement(4, 0, 0, 1));
		assertFalse(boardGenerator.isValidPlacement(4, 1, 0, 1));
		assertFalse(boardGenerator.isValidPlacement(4, 2, 0, 1));
		assertFalse(boardGenerator.isValidPlacement(5, 2, 0, 1));
		assertFalse(boardGenerator.isValidPlacement(6, 2, 0, 1));
		assertFalse(boardGenerator.isValidPlacement(6, 1, 0, 1));
		assertFalse(boardGenerator.isValidPlacement(6, 0, 0, 1));
		assertFalse(boardGenerator.isValidPlacement(5, 0, 0, 1));
		assertFalse(boardGenerator.isValidPlacement(5, 1, 0, 1));		
		
		boardPiece = null;
		board = null;
		boardGenerator = null;
	}
	
	/**
	 * Test the placement validation along the right edge.
	 */
	public void testRight() {
		
		//-----------------------------------------------------------
		// Test the case where the active piece is on the right edge.
		//-----------------------------------------------------------
		DefaultBoardGenerator boardGenerator = new DefaultBoardGenerator();
		
		DefaultBoard board = (DefaultBoard) 
			boardGenerator.generateCustomBoard(
					DEFAULT_BOARD_WIDTH, DEFAULT_BOARD_HEIGHT, new int[0], 0);
		
		DefaultBoardPiece boardPiece = new DefaultBoardPiece(
				9,
				5, 
				BoardPiece.VERTICAL_ORIENTATION, 
				1, 
				BoardPieceState.STATE_ALIVE, 
				IdGenerator.getInstance().generateID());
		
		board.addBoardPiece(boardPiece);
		
		// These should all be illegal placements
		assertFalse(boardGenerator.isValidPlacement(9, 4, 0, 1));
		assertFalse(boardGenerator.isValidPlacement(8, 4, 0, 1));
		assertFalse(boardGenerator.isValidPlacement(8, 5, 0, 1));
		assertFalse(boardGenerator.isValidPlacement(8, 6, 0, 1));
		assertFalse(boardGenerator.isValidPlacement(9, 6, 0, 1));
		assertFalse(boardGenerator.isValidPlacement(9, 5, 0, 1));
		
		//-------------------------------------------------
		// Test the case where the active piece is at 8,5.
		//-------------------------------------------------
		board = (DefaultBoard)
			boardGenerator.generateCustomBoard(
					DEFAULT_BOARD_WIDTH, DEFAULT_BOARD_HEIGHT, new int[0], 0);
		
		boardPiece = new DefaultBoardPiece(
				8,
				5, 
				BoardPiece.VERTICAL_ORIENTATION, 
				1, 
				BoardPieceState.STATE_ALIVE, 
				IdGenerator.getInstance().generateID());
		
		board.addBoardPiece(boardPiece);
		
		// These should all be illegal placements
		assertFalse(boardGenerator.isValidPlacement(7, 4, 0, 1));
		assertFalse(boardGenerator.isValidPlacement(7, 5, 0, 1));
		assertFalse(boardGenerator.isValidPlacement(7, 6, 0, 1));
		assertFalse(boardGenerator.isValidPlacement(8, 6, 0, 1));
		assertFalse(boardGenerator.isValidPlacement(9, 6, 0, 1));
		assertFalse(boardGenerator.isValidPlacement(9, 5, 0, 1));
		assertFalse(boardGenerator.isValidPlacement(9, 4, 0, 1));
		assertFalse(boardGenerator.isValidPlacement(8, 4, 0, 1));
		assertFalse(boardGenerator.isValidPlacement(8, 5, 0, 1));		
		
		boardPiece = null;
		board = null;
		boardGenerator = null;
	}
	
	/**
	 * Test the placement validation along the bottom edge.
	 */
	public void testBottom() {
		
		//------------------------------------------------------------
		// Test the case where the active piece is on the bottom edge.
		//------------------------------------------------------------
		DefaultBoardGenerator boardGenerator = new DefaultBoardGenerator();
		
		DefaultBoard board = (DefaultBoard) 
			boardGenerator.generateCustomBoard(
					DEFAULT_BOARD_WIDTH, DEFAULT_BOARD_HEIGHT, new int[0], 0);
		
		DefaultBoardPiece boardPiece = new DefaultBoardPiece(
				5,
				9, 
				BoardPiece.VERTICAL_ORIENTATION, 
				1, 
				BoardPieceState.STATE_ALIVE, 
				IdGenerator.getInstance().generateID());
		
		board.addBoardPiece(boardPiece);
		
		// These should all be illegal placements
		assertFalse(boardGenerator.isValidPlacement(4, 9, 0, 1));
		assertFalse(boardGenerator.isValidPlacement(4, 8, 0, 1));
		assertFalse(boardGenerator.isValidPlacement(5, 8, 0, 1));
		assertFalse(boardGenerator.isValidPlacement(6, 8, 0, 1));
		assertFalse(boardGenerator.isValidPlacement(6, 9, 0, 1));
		assertFalse(boardGenerator.isValidPlacement(5, 9, 0, 1));
		
		//-------------------------------------------------
		// Test the case where the active piece is at 5,8.
		//-------------------------------------------------
		board = (DefaultBoard)
			boardGenerator.generateCustomBoard(
					DEFAULT_BOARD_WIDTH, DEFAULT_BOARD_HEIGHT, new int[0], 0);
		
		boardPiece = new DefaultBoardPiece(
				5,
				8, 
				BoardPiece.VERTICAL_ORIENTATION, 
				1, 
				BoardPieceState.STATE_ALIVE, 
				IdGenerator.getInstance().generateID());
		
		board.addBoardPiece(boardPiece);
		
		// These should all be illegal placements
		assertFalse(boardGenerator.isValidPlacement(4, 9, 0, 1));
		assertFalse(boardGenerator.isValidPlacement(4, 8, 0, 1));
		assertFalse(boardGenerator.isValidPlacement(4, 7, 0, 1));
		assertFalse(boardGenerator.isValidPlacement(5, 7, 0, 1));
		assertFalse(boardGenerator.isValidPlacement(6, 7, 0, 1));
		assertFalse(boardGenerator.isValidPlacement(6, 8, 0, 1));
		assertFalse(boardGenerator.isValidPlacement(6, 9, 0, 1));
		assertFalse(boardGenerator.isValidPlacement(5, 9, 0, 1));
		assertFalse(boardGenerator.isValidPlacement(5, 8, 0, 1));		
		
		boardPiece = null;
		board = null;
		boardGenerator = null;
	}
	
	/**
	 * Test the placement validation along the left edge.
	 */
	public void testLeft() {
		
		//----------------------------------------------------------
		// Test the case where the active piece is on the left edge.
		//----------------------------------------------------------
		DefaultBoardGenerator boardGenerator = new DefaultBoardGenerator();
		
		DefaultBoard board = (DefaultBoard) 
			boardGenerator.generateCustomBoard(
					DEFAULT_BOARD_WIDTH, DEFAULT_BOARD_HEIGHT, new int[0], 0);
		
		DefaultBoardPiece boardPiece = new DefaultBoardPiece(
				0,
				5, 
				BoardPiece.VERTICAL_ORIENTATION, 
				1, 
				BoardPieceState.STATE_ALIVE, 
				IdGenerator.getInstance().generateID());
		
		board.addBoardPiece(boardPiece);
		
		// These should all be illegal placements
		assertFalse(boardGenerator.isValidPlacement(0, 4, 0, 1));
		assertFalse(boardGenerator.isValidPlacement(1, 4, 0, 1));
		assertFalse(boardGenerator.isValidPlacement(1, 5, 0, 1));
		assertFalse(boardGenerator.isValidPlacement(1, 6, 0, 1));
		assertFalse(boardGenerator.isValidPlacement(0, 6, 0, 1));
		assertFalse(boardGenerator.isValidPlacement(0, 5, 0, 1));
		
		//-------------------------------------------------
		// Test the case where the active piece is at 1,5.
		//-------------------------------------------------
		board = (DefaultBoard)
			boardGenerator.generateCustomBoard(
					DEFAULT_BOARD_WIDTH, DEFAULT_BOARD_HEIGHT, new int[0], 0);
		
		boardPiece = new DefaultBoardPiece(
				1,
				5, 
				BoardPiece.VERTICAL_ORIENTATION, 
				1, 
				BoardPieceState.STATE_ALIVE, 
				IdGenerator.getInstance().generateID());
		
		board.addBoardPiece(boardPiece);
		
		// These should all be illegal placements
		assertFalse(boardGenerator.isValidPlacement(0, 4, 0, 1));
		assertFalse(boardGenerator.isValidPlacement(0, 5, 0, 1));
		assertFalse(boardGenerator.isValidPlacement(0, 6, 0, 1));
		assertFalse(boardGenerator.isValidPlacement(1, 6, 0, 1));
		assertFalse(boardGenerator.isValidPlacement(2, 6, 0, 1));
		assertFalse(boardGenerator.isValidPlacement(2, 5, 0, 1));
		assertFalse(boardGenerator.isValidPlacement(2, 4, 0, 1));
		assertFalse(boardGenerator.isValidPlacement(1, 4, 0, 1));
		assertFalse(boardGenerator.isValidPlacement(1, 5, 0, 1));		
		
		boardPiece = null;
		board = null;
		boardGenerator = null;
	}
	
	public void testEmptyPiecePlacement() {
		// TODO: complete implementation
	}
}
