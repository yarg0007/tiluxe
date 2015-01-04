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
import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;

// Internal Imports
import com.asbtechnologies.android.tiluxe.board.DefaultBoard;
import com.asbtechnologies.android.tiluxe.manager.SimpleSceneManager;

/**
 * OpenGL ES Renderer for game. Sets up OpenGL on surface created event and 
 * updates it on surface changed. Draws continuously calling the 
 * SimpleSceneManager to draw the entire scene.
 *
 * @author Ben Yarger
 * @version $Revision: 1.7 $
 */
public class OpenGLRenderer implements Renderer {
	
	/** Android context reference. */
	private Context context;
	
	/** Simple scene manager to control the elements to render. */
	private SimpleSceneManager sceneManager;
	
	/** Current surface width */
	private int surfaceWidth;
	
	/** Current surface height */
	private int surfaceHeight;
	
	/** The board data to represent graphically. */
	private DefaultBoard board;
	
	/** Registered listeners. */
	private ArrayList<OpenGLRendererListener> listeners;
	
	/**
	 * Default constructor.
	 * 
	 * @param context Android context.
	 */
	public OpenGLRenderer(Context context, DefaultBoard board) {
		
		this.context = context;
		this.board = board;
		listeners = new ArrayList<OpenGLRendererListener>();
	}
	
	//--------------------------------------------------------------------------
	// Public methods
	//--------------------------------------------------------------------------
	
	/**
	 * Stop and dispose of the renderer.
	 */
	public void disposeOfRenderer() {
		sceneManager.disposeOfScene();
		sceneManager = null;
	}
	
	/**
	 * Register a listener with the renderer to receive notifications from the
	 * renderer such as rendering started, rendering quit etc.
	 * 
	 * @param listener Listener to register.
	 */
	public void registerListener(OpenGLRendererListener listener) {
		listeners.add(listener);
	}
	
	/**
	 * Perform a pick test against the scene.
	 * 
	 * @param motionType Type of mouse motion (use Android's MotionEvent values)
	 * @param xPos Horizontal screen position of pick.
	 * @param yPos Vertical screen position of pick.
	 * @return ReferenceID of game object that was picked or -1 if nothing was
	 * picked.
	 */
	public int testPick(float motionType, float xPos, float yPos) {
		
		float testXPos = xPos - surfaceWidth/2.0f;
		float testYPos = surfaceHeight/2.0f - yPos;
		
		// Fix for NullPointerException reported by user (July 7, 2011)
		if (sceneManager == null) {
			return -1;
		}
		
		return sceneManager.testPick(
				motionType, 
				testXPos, 
				testYPos);
	}
	
	/**
	 * Update the game legends to reflect any changes made on the game board.
	 */
	public void updateLegends() {
		sceneManager.updateLegends();
	}
	
	/**
	 * Reset the scene graphics.
	 */
	public void resetGraphics() {
		
		sceneManager.resetScene();
		sceneManager.updateLegends();
	}
	
	//--------------------------------------------------------------------------
	// Private methods
	//--------------------------------------------------------------------------
	
	/**
	 * Issue a game message to each of the listeners.
	 * 
	 * @param message Message value to send to each listener.
	 */
	private void notifyListeners(int message) {
		
		for (int i = 0; i < listeners.size(); i++) {
			listeners.get(i).openGLRendererNotification(message);
		}
	}
	
	//--------------------------------------------------------------------------
	// Methods required by Renderer
	//--------------------------------------------------------------------------
	
	/* (non-Javadoc)
	 * @see android.opengl.GLSurfaceView.Renderer#onDrawFrame(javax.microedition.khronos.opengles.GL10)
	 */
	@Override
	public void onDrawFrame(GL10 gl) {
		
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
			
		// Draw the board
	    sceneManager.drawScene(gl);

	}

	/* (non-Javadoc)
	 * @see android.opengl.GLSurfaceView.Renderer#onSurfaceChanged(javax.microedition.khronos.opengles.GL10, int, int)
	 */
	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		
		surfaceWidth = width;
		surfaceHeight = height;
		
		gl.glViewport(0, 0, (int) surfaceWidth, (int) surfaceHeight);

		// Setup the orthographic projection.
		gl.glMatrixMode(GL10.GL_PROJECTION);
	    gl.glLoadIdentity();
	    
		gl.glOrthof(
	    		-surfaceWidth/2.0f, 
	    		surfaceWidth/2.0f, 
	    		-surfaceHeight/2.0f, 
	    		surfaceHeight/2.0f, 
	    		0.0f, 
	    		20.0f);

	    
	    gl.glMatrixMode(GL10.GL_MODELVIEW);
	    gl.glLoadIdentity();
	    
	    // We wait until we have the surface dimensions and then create the
	    // board. We don't expect the screen size to change so we only set this
	    // up once.
	    if (!sceneManager.isSceneLive()) {
	    	sceneManager.initializeGraphics(board, surfaceWidth, surfaceHeight);
	    	notifyListeners(OpenGLRendererListener.RENDERER_READY);
	    }
		
	}

	/* (non-Javadoc)
	 * @see android.opengl.GLSurfaceView.Renderer#onSurfaceCreated(javax.microedition.khronos.opengles.GL10, javax.microedition.khronos.egl.EGLConfig)
	 */
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {

		// Setup configuration.
		
		// Set color's clear-value.
		gl.glClearColor(0.2f, 0.2f, 0.2f, 1.0f);
		
		// Enable textures.
		gl.glEnable(GL10.GL_TEXTURE_2D);
		
		// Select blending function, then enable blending.
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		gl.glEnable(GL10.GL_BLEND);
		
		// Set the depth buffer and then enable depth tests.
		gl.glClearDepthf(1.0f);
		gl.glDepthFunc(GL10.GL_LEQUAL);
		gl.glEnable(GL10.GL_DEPTH_TEST);

//	    gl.glShadeModel(GL10.GL_SMOOTH);   // Enable smooth shading of color
//	    gl.glDisable(GL10.GL_DITHER);      // Disable dithering for better performance
	    
		// Create Nearest Filtered Texture
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,
				GL10.GL_LINEAR);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER,
				GL10.GL_LINEAR);

		// Different possible texture parameters, e.g. GL10.GL_CLAMP_TO_EDGE
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S,
				GL10.GL_REPEAT);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T,
				GL10.GL_REPEAT);
	    
	    
		sceneManager = new SimpleSceneManager(gl, context);

	}
}
