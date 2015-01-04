/*****************************************************************************
 *                        Copyright ASB Technologies (c) 2011
 *                               Java Source
 *
 * This source is the property of ASB Technologies. Any duplication or reuse
 * without the consent of ASB Technologies is prohibited.
 *
 ****************************************************************************/
package com.asbtechnologies.android.tiluxe.rendering;

// External Imports
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

// Internal Imports
import com.asbtechnologies.android.tiluxe.board.DefaultBoard;
import com.asbtechnologies.android.tiluxe.board.DefaultBoardPiece;

/**
 * Game view for Android Activities that want a visual game board 
 * representation. Creates an OpenGL view and draws the game board using the 
 * OpenGL renderer.
 *
 * @author Ben Yarger
 * @version $Revision: 1.6 $
 */
public class GameGLView extends GLSurfaceView {
	
	/** Reference to Android context (parent Activity). */
	private Context context;
	
	/** Reference to renderer. */
	private OpenGLRenderer glRenderer;
	
	/** The active playing board. */
	private DefaultBoard board;
	
	/** The board piece that was picked. */
	private DefaultBoardPiece pickPiece;

	/**
	 * Default constructor.
	 * 
	 * @param context Android context (parent Activity)
	 */
	public GameGLView(Context context, DefaultBoard board) {
		
		super(context);
		
		this.context = context;
		this.board = board;
		
		// Set the renderer for the view.
		glRenderer = new OpenGLRenderer(this.context, board);
		this.setRenderer(glRenderer);
	}
	
	/* (non-Javadoc)
	 * @see android.view.View#onTouchEvent(android.view.MotionEvent)
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		// On a mouse up, take the picked tile and change its state and visual
		// representation. If this is a mouse down or mouse move, just test 
		// the pick. Testing the pick automatically performs graphical update
		// changes. For the mouse move case, it makes the tile larger. For
		// mouse up it cycles the texture of the tile to the next appropriate
		// texture.
		if (event.getAction() == MotionEvent.ACTION_UP) {

			int pieceID = glRenderer.testPick(
					MotionEvent.ACTION_UP, 
					event.getX(), 
					event.getY());
			
			pickPiece = (DefaultBoardPiece) board.pickByID(pieceID);
			
			if (pickPiece != null) {
				pickPiece.setNextState();
			}
			
			glRenderer.updateLegends();
			
		} else if (event.getAction() == MotionEvent.ACTION_MOVE ||
				event.getAction() == MotionEvent.ACTION_DOWN) {
			
			glRenderer.testPick(
					MotionEvent.ACTION_MOVE, 
					event.getX(), 
					event.getY());
		}
		
		return true;
	}
	
	//--------------------------------------------------------------------------
	// Public methods
	//--------------------------------------------------------------------------
	
	/**
	 * Clean before quit.
	 */
	public void dispose() {
		
		board = null;
		pickPiece = null;
		
		glRenderer.disposeOfRenderer();
		glRenderer = null;
		
		context = null;
	}
	
	/**
	 * Register a listener with the OpenGL renderer used by the view.
	 * 
	 * @param listener Listener to register.
	 */
	public void passThroughOpenGLRendererListener(
			OpenGLRendererListener listener) {
		
		glRenderer.registerListener(listener);
	}

	/**
	 * Reset the scene graphics.
	 */
	public void resetGraphics() {
		glRenderer.resetGraphics();
	}
}
