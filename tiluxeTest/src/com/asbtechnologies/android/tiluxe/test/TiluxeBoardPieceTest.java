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
import junit.framework.TestCase;

// Internal Imports
import com.asbtechnologies.android.tiluxe.board.BoardPiece;
import com.asbtechnologies.android.tiluxe.board.BoardPieceState;
import com.asbtechnologies.android.tiluxe.board.DefaultBoardPiece;

/**
 * Unit tests for DefaultBoardPiece implementation.
 * 
 * @author Ben Yarger
 * @version $Revision: 1.4 $
 */
public class TiluxeBoardPieceTest extends TestCase {
	
	/**
	 * Default constructor.
	 * 
	 * @param name
	 */
	public TiluxeBoardPieceTest(String name) {
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
	// Orientation tests
	//--------------------------------------------------------------------------
	
	/**
	 * Confirm the orientation value comes back as HORIZONTAL if the 
	 * orientation value is illegal.
	 */
	public void testIllegalOrientationValue() {
		
		int orientationValue = 2;
		
		DefaultBoardPiece defaultBoardPiece = new DefaultBoardPiece(
				0, 
				0, 
				orientationValue, 
				5, 
				BoardPieceState.STATE_ALIVE, 
				0);
		
		assertEquals(
				BoardPiece.HORIZONTAL_ORIENTATION, 
				defaultBoardPiece.getOrientation());
		
		defaultBoardPiece = null;
	}
	
	/**
	 * Confirm that a request of HORIZONTAL orientation is honored in the 
	 * resulting board piece.
	 */
	public void testOrientationHorizontal() {
		
		int orientationValue = BoardPiece.HORIZONTAL_ORIENTATION;
		
		DefaultBoardPiece defaultBoardPiece = new DefaultBoardPiece(
				0, 
				0, 
				orientationValue, 
				5, 
				BoardPieceState.STATE_ALIVE, 
				0);
		
		assertEquals(
				BoardPiece.HORIZONTAL_ORIENTATION, 
				defaultBoardPiece.getOrientation());
		
		defaultBoardPiece = null;
	}
	
	/**
	 * Confirm that a request of VERTICAL orientation is honored in the 
	 * resulting board piece.
	 */
	public void testOrientationVertical() {
		
		int orientationValue = BoardPiece.VERTICAL_ORIENTATION;
		
		DefaultBoardPiece defaultBoardPiece = new DefaultBoardPiece(
				0, 
				0, 
				orientationValue, 
				5, 
				BoardPieceState.STATE_ALIVE, 
				0);
		
		assertEquals(
				BoardPiece.VERTICAL_ORIENTATION, 
				defaultBoardPiece.getOrientation());
		
		defaultBoardPiece = null;
	}
	
	//--------------------------------------------------------------------------
	// Group ID tests
	//--------------------------------------------------------------------------
	
	/**
	 * Confirm the group ID comes back as expected from the defaultBoardPiece.
	 */
	public void testGroupID() {
		
		DefaultBoardPiece defaultBoardPiece = new DefaultBoardPiece(
				0, 
				0, 
				BoardPiece.HORIZONTAL_ORIENTATION, 
				5, 
				BoardPieceState.STATE_ALIVE, 
				0);
		
		assertEquals(0, defaultBoardPiece.getGroupID());
		
		defaultBoardPiece = null;
	}
	
	/**
	 * Confirm the group ID comes back as expected with a group ID of 
	 * Integer.MAX_VALUE.
	 */
	public void testGroupIDMax() {
		
		DefaultBoardPiece altBoardPiece = new DefaultBoardPiece(
				1, 
				1, 
				BoardPiece.HORIZONTAL_ORIENTATION, 
				3, 
				BoardPieceState.STATE_DEAD, 
				Integer.MAX_VALUE);
		
		assertEquals(Integer.MAX_VALUE, altBoardPiece.getGroupID());
		
		altBoardPiece = null;
	}
	
	/**
	 * Confirm the group ID comes back as expected with a group ID of 
	 * Integer.MIN_VALUE.
	 */
	public void testGroupIDMin() {
		
		DefaultBoardPiece altBoardPiece = new DefaultBoardPiece(
				1, 
				1, 
				BoardPiece.HORIZONTAL_ORIENTATION, 
				3, 
				BoardPieceState.STATE_DEAD, 
				Integer.MIN_VALUE);
		
		assertEquals(Integer.MIN_VALUE, altBoardPiece.getGroupID());
		
		altBoardPiece = null;
	}
	
	//--------------------------------------------------------------------------
	// Piece length tests
	//--------------------------------------------------------------------------
	
	/**
	 * Confirm that the piece length 2 comes back as expected. Make sure the 
	 * number of sub pieces matches length.
	 */
	public void testLength() {
		
		int pieceLength = 2;
		
		DefaultBoardPiece altBoardPiece = new DefaultBoardPiece(
				1, 
				1, 
				BoardPiece.HORIZONTAL_ORIENTATION, 
				pieceLength, 
				BoardPieceState.STATE_DEAD, 
				0);
		
		assertEquals(pieceLength, altBoardPiece.getLength());
		assertNotNull(altBoardPiece.getSubPieces());
		assertEquals(pieceLength, altBoardPiece.getSubPieces().length);
		
		altBoardPiece = null;
	}
	
	/**
	 * Confirm that the piece length 1 comes back as expected. Make sure sub
	 * pieces comes back null.
	 */
	public void testLengthOne() {
		
		int pieceLength = 1;
		
		DefaultBoardPiece altBoardPiece = new DefaultBoardPiece(
				1, 
				1, 
				BoardPiece.HORIZONTAL_ORIENTATION, 
				pieceLength, 
				BoardPieceState.STATE_DEAD, 
				0);
		
		assertEquals(pieceLength, altBoardPiece.getLength());
		assertNull(altBoardPiece.getSubPieces());
		
		altBoardPiece = null;
	}
 	
	/**
	 * Confirm that the piece length comes back as 100. Make sure
	 * the number of sub pieces matches 100.
	 */
	public void testLengthMax() {
		
		int pieceLength = 100;
		
		DefaultBoardPiece altBoardPiece = new DefaultBoardPiece(
				1, 
				1, 
				BoardPiece.HORIZONTAL_ORIENTATION, 
				pieceLength, 
				BoardPieceState.STATE_DEAD, 
				0);
		
		assertEquals(pieceLength, altBoardPiece.getLength());
		assertNotNull(altBoardPiece.getSubPieces());
		assertEquals(pieceLength, altBoardPiece.getSubPieces().length);
		
		altBoardPiece = null;
	}
	
	/**
	 * Confirm that the piece length comes back as 0, and sub pieces come
	 * back as null.
	 */
	public void testLengthNegativeOne() {
		
		int pieceLength = -1;
		
		DefaultBoardPiece altBoardPiece = new DefaultBoardPiece(
				1, 
				1, 
				BoardPiece.HORIZONTAL_ORIENTATION, 
				pieceLength, 
				BoardPieceState.STATE_DEAD, 
				0);
		
		assertEquals(0, altBoardPiece.getLength());
		assertNull(altBoardPiece.getSubPieces());
		
		altBoardPiece = null;
	}
	
	/**
	 * Confirm that the piece length comes back as 0 and sub pieces come back 
	 * as null.
	 */
	public void testLengthMin() {
		
		int pieceLength = Integer.MIN_VALUE;
		
		DefaultBoardPiece altBoardPiece = new DefaultBoardPiece(
				1, 
				1, 
				BoardPiece.HORIZONTAL_ORIENTATION, 
				pieceLength, 
				BoardPieceState.STATE_DEAD, 
				0);
		
		assertEquals(0, altBoardPiece.getLength());
		assertNull(altBoardPiece.getSubPieces());
		
		altBoardPiece = null;
	}
	
	/**
	 * Confirm that the piece length comes back as 0 and sub pieces come back
	 * as null.
	 */
	public void testLengthZero() {
		
		int pieceLength = 0;
		
		DefaultBoardPiece altBoardPiece = new DefaultBoardPiece(
				1, 
				1, 
				BoardPiece.HORIZONTAL_ORIENTATION, 
				pieceLength, 
				BoardPieceState.STATE_DEAD, 
				0);
		
		assertEquals(pieceLength, altBoardPiece.getLength());
		assertNull(altBoardPiece.getSubPieces());
		
		altBoardPiece = null;
	}

	//--------------------------------------------------------------------------
	// Position tests
	//--------------------------------------------------------------------------
	
	/**
	 * Confirm that the x position comes back as 1.
	 */
	public void testXPos() {
		
		int xPos = 1;
		
		DefaultBoardPiece altBoardPiece = new DefaultBoardPiece(
				xPos, 
				1, 
				BoardPiece.HORIZONTAL_ORIENTATION, 
				1, 
				BoardPieceState.STATE_DEAD, 
				0);
		
		assertNotNull(altBoardPiece.getLocation());
		assertEquals(xPos, altBoardPiece.getLocation()[0]);
		
		altBoardPiece = null;
	}
	
	/**
	 * Confirm that the x position comes back as Integer.MAX_VALUE.
	 */
	public void testXPosMax() {

		int xPos = Integer.MAX_VALUE;
		
		DefaultBoardPiece altBoardPiece = new DefaultBoardPiece(
				xPos, 
				1, 
				BoardPiece.HORIZONTAL_ORIENTATION, 
				1, 
				BoardPieceState.STATE_DEAD, 
				0);
		
		assertNotNull(altBoardPiece.getLocation());
		assertEquals(xPos, altBoardPiece.getLocation()[0]);
		
		altBoardPiece = null;
	}
	
	/**
	 * Confirm that the x position comes back as 0.
	 */
	public void testXPosNegativeOne() {
		
		int xPos = -1;
		
		DefaultBoardPiece altBoardPiece = new DefaultBoardPiece(
				xPos, 
				1, 
				BoardPiece.HORIZONTAL_ORIENTATION, 
				1, 
				BoardPieceState.STATE_DEAD, 
				0);
		
		assertNotNull(altBoardPiece.getLocation());
		assertEquals(0, altBoardPiece.getLocation()[0]);
		
		altBoardPiece = null;
	}
	
	/**
	 * Confirm that the x position comes back as 0.
	 */
	public void testXPosMin() {
		
		int xPos = Integer.MIN_VALUE;
		
		DefaultBoardPiece altBoardPiece = new DefaultBoardPiece(
				xPos, 
				1, 
				BoardPiece.HORIZONTAL_ORIENTATION, 
				1, 
				BoardPieceState.STATE_DEAD, 
				0);
		
		assertNotNull(altBoardPiece.getLocation());
		assertEquals(0, altBoardPiece.getLocation()[0]);
		
		altBoardPiece = null;
	}
	
	/**
	 * Confirm that the x position comes back as 0.
	 */
	public void testXPosZero() {
		
		int xPos = 0;
		
		DefaultBoardPiece altBoardPiece = new DefaultBoardPiece(
				xPos, 
				1, 
				BoardPiece.HORIZONTAL_ORIENTATION, 
				1, 
				BoardPieceState.STATE_DEAD, 
				0);

		assertNotNull(altBoardPiece.getLocation());
		assertEquals(xPos, altBoardPiece.getLocation()[0]);
		
		altBoardPiece = null;
	}
	
	/**
	 * Confirm that the y position comes back as 1.
	 */
	public void testYPos() {
		
		int yPos = 1;
		
		DefaultBoardPiece altBoardPiece = new DefaultBoardPiece(
				1, 
				yPos, 
				BoardPiece.HORIZONTAL_ORIENTATION, 
				1, 
				BoardPieceState.STATE_DEAD, 
				0);

		assertNotNull(altBoardPiece.getLocation());
		assertEquals(yPos, altBoardPiece.getLocation()[1]);
		
		altBoardPiece = null;
	}
	
	/**
	 * Confirm that the y position comes back as Interger.MAX_VALUE.
	 */
	public void testYPosMax() {
		
		int yPos = Integer.MAX_VALUE;
		
		DefaultBoardPiece altBoardPiece = new DefaultBoardPiece(
				1, 
				yPos, 
				BoardPiece.HORIZONTAL_ORIENTATION, 
				1, 
				BoardPieceState.STATE_DEAD, 
				0);

		assertNotNull(altBoardPiece.getLocation());
		assertEquals(yPos, altBoardPiece.getLocation()[1]);
		
		altBoardPiece = null;
	}
	
	/**
	 * Confirm that the y position comes back as 0.
	 */
	public void testYPosNegativeOne() {
		
		int yPos = -1;
		
		DefaultBoardPiece altBoardPiece = new DefaultBoardPiece(
				1, 
				yPos, 
				BoardPiece.HORIZONTAL_ORIENTATION, 
				1, 
				BoardPieceState.STATE_DEAD, 
				0);

		assertNotNull(altBoardPiece.getLocation());
		assertEquals(0, altBoardPiece.getLocation()[1]);
		
		altBoardPiece = null;
	}
	
	/**
	 * Confirm that the y position comes back as 0.
	 */
	public void testYPosMin() {
		
		int yPos = Integer.MIN_VALUE;
		
		DefaultBoardPiece altBoardPiece = new DefaultBoardPiece(
				1, 
				yPos, 
				BoardPiece.HORIZONTAL_ORIENTATION, 
				1, 
				BoardPieceState.STATE_DEAD, 
				0);

		assertNotNull(altBoardPiece.getLocation());
		assertEquals(0, altBoardPiece.getLocation()[1]);
		
		altBoardPiece = null;
	}
	
	/**
	 * Confirm that the y position comes back as 0.
	 */
	public void testYPosZero() {
		
		int yPos = 0;
		
		DefaultBoardPiece altBoardPiece = new DefaultBoardPiece(
				1, 
				yPos, 
				BoardPiece.HORIZONTAL_ORIENTATION, 
				1, 
				BoardPieceState.STATE_DEAD, 
				0);

		assertNotNull(altBoardPiece.getLocation());
		assertEquals(0, altBoardPiece.getLocation()[1]);
		
		altBoardPiece = null;
	}

	//--------------------------------------------------------------------------
	// State condition tests
	//--------------------------------------------------------------------------
	
	/**
	 * Confirm that the state we have to match is correct or alive state.
	 */
	public void testAliveMatchState() {
		
		DefaultBoardPiece defaultBoardPiece = new DefaultBoardPiece(
				0, 
				0, 
				BoardPiece.HORIZONTAL_ORIENTATION, 
				5, 
				BoardPieceState.STATE_ALIVE, 
				0);
		
		assertEquals(
				BoardPieceState.STATE_ALIVE, defaultBoardPiece.getMatchState());
		
		defaultBoardPiece = null;
	}
	
	/**
	 * Confirm that the state we have to match is correct for dead state.
	 */
	public void testDeadMatchState() {
		
		DefaultBoardPiece altBoardPiece = new DefaultBoardPiece(
				1, 
				1, 
				BoardPiece.HORIZONTAL_ORIENTATION, 
				3, 
				BoardPieceState.STATE_DEAD, 
				22);
		
		assertEquals(BoardPieceState.STATE_DEAD, altBoardPiece.getMatchState());
		
		altBoardPiece = null;
	}
	
	/**
	 * Confirm that the state we have to match is correct when the 
	 * expectedState value passed in is not a legal value. In this case the
	 * resulting match state should be STATE_LIMBO.
	 */
	public void testIllegalExpectedStateValue() {
		
		int illegalExpectedStateValue = 23;
		
		DefaultBoardPiece altBoardPiece = new DefaultBoardPiece(
				1, 
				1, 
				BoardPiece.HORIZONTAL_ORIENTATION, 
				3, 
				illegalExpectedStateValue, 
				22);
		
		assertEquals(
				BoardPieceState.STATE_DEAD, altBoardPiece.getMatchState());
		
		altBoardPiece = null;
		
	}
	
	//--------------------------------------------------------------------------
	// Sub pieces tests
	//--------------------------------------------------------------------------
	
	/**
	 * Confirm the correct number of sub pieces come back along a horizontal
	 * orientation.
	 */
	public void testCorrectNumberHorizontalSubPieces() {
		
		int xPos = 1;
		int yPos = 2;
		int length = 4;
		
		DefaultBoardPiece altBoardPiece = new DefaultBoardPiece(
				xPos, 
				yPos, 
				BoardPiece.HORIZONTAL_ORIENTATION, 
				length, 
				BoardPieceState.STATE_ALIVE, 
				22);
		
		DefaultBoardPiece[] subPieces = 
			(DefaultBoardPiece[]) altBoardPiece.getSubPieces();
		
		assertNotNull(subPieces);
		assertEquals(length, subPieces.length);
		
		altBoardPiece = null;
	}
	
	/**
	 * Confirm the correct number of sub pieces come back along a vertical
	 * orientation.
	 */
	public void testCorrectNumberVerticalSubPieces() {
		
		int xPos = 1;
		int yPos = 2;
		int length = 3;
		
		DefaultBoardPiece altBoardPiece = new DefaultBoardPiece(
				xPos, 
				yPos, 
				BoardPiece.VERTICAL_ORIENTATION, 
				length, 
				BoardPieceState.STATE_ALIVE, 
				22);
		
		DefaultBoardPiece[] subPieces = 
			(DefaultBoardPiece[]) altBoardPiece.getSubPieces();
		
		assertNotNull(subPieces);
		assertEquals(length, subPieces.length);
		
		altBoardPiece = null;		
	}
	
	/**
	 * Confirm that each sub piece comes back in order by position starting
	 * from the position of the parent piece specified by the constructor.
	 * Testing horizontal sub pieces.
	 */
	public void testHorizontalSubPiecesPositionOrder() {
		
		int xPos = 1;
		int yPos = 2;
		int length = 5;
		
		DefaultBoardPiece altBoardPiece = new DefaultBoardPiece(
				xPos, 
				yPos, 
				BoardPiece.HORIZONTAL_ORIENTATION, 
				length, 
				BoardPieceState.STATE_ALIVE, 
				22);
		
		DefaultBoardPiece[] subPieces = 
			(DefaultBoardPiece[]) altBoardPiece.getSubPieces();
		
		assertNotNull(subPieces);
		int[] testPos;
		
		for (int i = 0; i < subPieces.length; i++) {
			testPos = subPieces[i].getLocation();
			assertNotNull(testPos);
			assertEquals(xPos+i, testPos[0]);
			assertEquals(yPos, testPos[1]);
		}
		
		altBoardPiece = null;
	}
	
	/**
	 * Confirm that each sub piece comes back in order by position starting
	 * from the position of the parent piece specified by the constructor.
	 * Testing vertical sub pieces.
	 */
	public void testVerticalSubPiecesPositionOrder() {
		
		int xPos = 1;
		int yPos = 2;
		int length = 5;
		
		DefaultBoardPiece altBoardPiece = new DefaultBoardPiece(
				xPos, 
				yPos, 
				BoardPiece.VERTICAL_ORIENTATION, 
				length, 
				BoardPieceState.STATE_ALIVE, 
				22);
		
		DefaultBoardPiece[] subPieces = 
			(DefaultBoardPiece[]) altBoardPiece.getSubPieces();
		
		assertNotNull(subPieces);
		int[] testPos;
		
		for (int i = 0; i < subPieces.length; i++) {
			testPos = subPieces[i].getLocation();
			assertNotNull(testPos);
			assertEquals(xPos, testPos[0]);
			assertEquals(yPos+i, testPos[1]);
		}
		
		altBoardPiece = null;
	}
	
	/**
	 * Confirm each sub pieces matches the expected state of the parent.
	 */
	public void testSubPieceExpectedStateMatch() {
		
		int xPos = 1;
		int yPos = 2;
		int length = 5;
		int expectedState = BoardPieceState.STATE_ALIVE;
		
		DefaultBoardPiece altBoardPiece = new DefaultBoardPiece(
				xPos, 
				yPos, 
				BoardPiece.VERTICAL_ORIENTATION, 
				length, 
				expectedState, 
				22);
		
		DefaultBoardPiece[] subPieces = 
			(DefaultBoardPiece[]) altBoardPiece.getSubPieces();
		
		assertNotNull(subPieces);
		
		for (int i = 0; i < subPieces.length; i++) {
			assertEquals(expectedState, subPieces[i].getMatchState());
		}
		
		altBoardPiece = null;		
	}
	
	/**
	 * Confirm each sub piece has the same HORIZONTAL orientation of the parent.
	 */
	public void testSubPieceHoriziontalOrientation() {
		
		int xPos = 1;
		int yPos = 2;
		int length = 5;
		int expectedState = BoardPieceState.STATE_ALIVE;
		int orientation = BoardPiece.HORIZONTAL_ORIENTATION;
		
		DefaultBoardPiece altBoardPiece = new DefaultBoardPiece(
				xPos, 
				yPos, 
				orientation, 
				length, 
				expectedState, 
				22);
		
		DefaultBoardPiece[] subPieces = 
			(DefaultBoardPiece[]) altBoardPiece.getSubPieces();
		
		assertNotNull(subPieces);
		
		for (int i = 0; i < subPieces.length; i++) {
			assertEquals(orientation, subPieces[i].getOrientation());
		}
		
		altBoardPiece = null;	
	}
	
	/**
	 * Confirm each sub piece has the same VERTICAL orientation of the parent.
	 */
	public void testSubPieceVerticalOrientation() {
		
		int xPos = 1;
		int yPos = 2;
		int length = 5;
		int expectedState = BoardPieceState.STATE_ALIVE;
		int orientation = BoardPiece.VERTICAL_ORIENTATION;
		
		DefaultBoardPiece altBoardPiece = new DefaultBoardPiece(
				xPos, 
				yPos, 
				orientation, 
				length, 
				expectedState, 
				22);
		
		DefaultBoardPiece[] subPieces = 
			(DefaultBoardPiece[]) altBoardPiece.getSubPieces();
		
		assertNotNull(subPieces);
		
		for (int i = 0; i < subPieces.length; i++) {
			assertEquals(orientation, subPieces[i].getOrientation());
		}
		
		altBoardPiece = null;	
	}
	
	/**
	 * Confirm each sub piece has the same group id as the parent.
	 */
	public void testSubPieceMatchingGroupID() {
		
		int xPos = 1;
		int yPos = 2;
		int length = 5;
		int expectedState = BoardPieceState.STATE_ALIVE;
		int orientation = BoardPiece.VERTICAL_ORIENTATION;
		int groupID = 22;
		
		DefaultBoardPiece altBoardPiece = new DefaultBoardPiece(
				xPos, 
				yPos, 
				orientation, 
				length, 
				expectedState, 
				groupID);
		
		DefaultBoardPiece[] subPieces = 
			(DefaultBoardPiece[]) altBoardPiece.getSubPieces();
		
		assertNotNull(subPieces);
		
		for (int i = 0; i < subPieces.length; i++) {
			assertEquals(groupID, subPieces[i].getGroupID());
		}
		
		altBoardPiece = null;
	}
	
	/**
	 * Confirm each sub piece does not have its own sub pieces.
	 */
	public void testSubPieceNoSubs() {
		
		int xPos = 1;
		int yPos = 2;
		int length = 5;
		int expectedState = BoardPieceState.STATE_ALIVE;
		int orientation = BoardPiece.VERTICAL_ORIENTATION;
		int groupID = 22;
		
		DefaultBoardPiece altBoardPiece = new DefaultBoardPiece(
				xPos, 
				yPos, 
				orientation, 
				length, 
				expectedState, 
				groupID);
		
		DefaultBoardPiece[] subPieces = 
			(DefaultBoardPiece[]) altBoardPiece.getSubPieces();
		
		assertNotNull(subPieces);
		
		for (int i = 0; i < subPieces.length; i++) {
			assertNull(subPieces[i].getSubPieces());
		}
		
		altBoardPiece = null;
	}
	
	//--------------------------------------------------------------------------
	// Confirm board piece state changes
	//--------------------------------------------------------------------------
	
	/**
	 * Confirm that an empty state case is processed correctly on the sub pieces
	 * especially if one of them change state.
	 */
	public void testEmptyState() {
		
		int xPos = 1;
		int yPos = 2;
		int length = 5;
		int expectedState = BoardPieceState.EMPTY;
		int orientation = BoardPiece.VERTICAL_ORIENTATION;
		int groupID = 22;
		
		DefaultBoardPiece altBoardPiece = new DefaultBoardPiece(
				xPos, 
				yPos, 
				orientation, 
				length, 
				expectedState, 
				groupID);
		
		DefaultBoardPiece[] subPieces = 
			(DefaultBoardPiece[]) altBoardPiece.getSubPieces();
		
		assertNotNull(subPieces);
		assertEquals(expectedState, subPieces[0].getCurrentState());
		
		// Attempt to change the current state of the first sub piece
		int changedState = BoardPieceState.STATE_ALIVE;
		
		subPieces[0].setState(changedState);
		assertEquals(changedState, subPieces[0].getCurrentState());
		
		// Confirm the remaining sub pieces current state are still set to 
		// empty.
		for (int i = 1; i < subPieces.length; i++) {
			assertEquals(expectedState, subPieces[i].getCurrentState());
		}
		
		altBoardPiece = null;
	}
	
	/**
	 * Confirm that an alive state case is processed correctly on the sub
	 * pieces especially if one of them change state.
	 */
	public void testAliveState() {
		
		int xPos = 1;
		int yPos = 2;
		int length = 5;
		int expectedState = BoardPieceState.STATE_ALIVE;
		int orientation = BoardPiece.VERTICAL_ORIENTATION;
		int groupID = 22;
		int testSubPieceIndex = 4;
		
		DefaultBoardPiece altBoardPiece = new DefaultBoardPiece(
				xPos, 
				yPos, 
				orientation, 
				length, 
				expectedState, 
				groupID);
		
		DefaultBoardPiece[] subPieces = 
			(DefaultBoardPiece[]) altBoardPiece.getSubPieces();
		
		assertNotNull(subPieces);
		assertEquals(
				BoardPieceState.STATE_LIMBO, 
				subPieces[testSubPieceIndex].getCurrentState());
		
		// Attempt to change the current state of the test sub piece
		int changedState = BoardPieceState.STATE_DEAD;
		
		subPieces[testSubPieceIndex].setState(changedState);
		assertEquals(
				changedState, subPieces[testSubPieceIndex].getCurrentState());
		
		// Confirm the remaining sub pieces current state are still set to 
		// limbo.
		for (int i = 0; i < subPieces.length-1; i++) {
			assertEquals(
					BoardPieceState.STATE_LIMBO, 
					subPieces[i].getCurrentState());
		}
		
		// Set it back to the expected outcome state and confirm the match is
		// correct.
		subPieces[testSubPieceIndex].setState(expectedState);
		assertTrue(subPieces[testSubPieceIndex].isStateCorrect());
		
		altBoardPiece = null;
	}
	
	/**
	 * Confirm that a dead state case is processed correctly on the sub
	 * pieces especially if one of them change state.
	 */
	public void testDeadState() {
		
		int xPos = 1;
		int yPos = 2;
		int length = 5;
		int expectedState = BoardPieceState.STATE_DEAD;
		int orientation = BoardPiece.VERTICAL_ORIENTATION;
		int groupID = 22;
		int testSubPieceIndex = 0;
		
		DefaultBoardPiece altBoardPiece = new DefaultBoardPiece(
				xPos, 
				yPos, 
				orientation, 
				length, 
				expectedState, 
				groupID);
		
		DefaultBoardPiece[] subPieces = 
			(DefaultBoardPiece[]) altBoardPiece.getSubPieces();
		
		assertNotNull(subPieces);
		assertEquals(
				BoardPieceState.STATE_LIMBO, 
				subPieces[testSubPieceIndex].getCurrentState());
		
		// Attempt to change the current state of the test sub piece
		int changedState = BoardPieceState.STATE_ALIVE;
		
		subPieces[testSubPieceIndex].setState(changedState);
		assertEquals(
				changedState, subPieces[testSubPieceIndex].getCurrentState());
		
		// Confirm the remaining sub pieces current state are still set to 
		// limbo.
		for (int i = 1; i < subPieces.length; i++) {
			assertEquals(
					BoardPieceState.STATE_LIMBO, 
					subPieces[i].getCurrentState());
		}
		
		// Set it back to the expected outcome state and confirm the match is
		// correct.
		subPieces[testSubPieceIndex].setState(expectedState);
		assertTrue(subPieces[testSubPieceIndex].isStateCorrect());
		
		altBoardPiece = null;
	}
}
