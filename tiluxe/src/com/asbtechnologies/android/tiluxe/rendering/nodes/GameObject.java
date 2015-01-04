/*****************************************************************************
 *                        Copyright ASB Technologies (c) 2011
 *                               Java Source
 *
 * This source is the property of ASB Technologies. Any duplication or reuse
 * without the consent of ASB Technologies is prohibited.
 *
 ****************************************************************************/

package com.asbtechnologies.android.tiluxe.rendering.nodes;

// External Imports
import javax.microedition.khronos.opengles.GL10;

// Internal Imports

/**
 * Defines the requirements for game objects. Game objects are renderable 
 * entities that are passed through the SimpleSceneManager for rendering.
 * 
 * @author Ben Yarger
 * @version $Revision: 1.2 $
 */
public interface GameObject {
	
	/**
	 * Get the reference ID that ties this game object to another piece of data.
	 * 
	 * @return Reference ID. 
	 */
	public int getReferenceID();
	
	/**
	 * Clean up the game object when done.
	 */
	public void dispose();
	
	/** 
	 * Get the default position of the object. This is the position it is 
	 * initially created with.
	 * 
	 * @param defaultPosition float[3] array of x,y,z ordered initial position
	 * values.
	 */
	public void getDefaultPosition(float[] defaultPosition) 
		throws ArrayIndexOutOfBoundsException;
	
	/**
	 * Translate the object along the specified x, y and z axis. Pass in 0,0,0
	 * to position at default location.
	 * 
	 * @param x X-axis translation.
	 * @param y Y-axis translation.
	 * @param z Z-axis translation.
	 */
	public void translateObject(float x, float y, float z);
	
	/**
	 * Rotate the object via an axis-angle rotation.
	 * 
	 * @param angle Angle to rotate through.
	 * @param x X-axis of rotation.
	 * @param y Y-axis of rotation.
	 * @param z Z-axis of rotation.
	 */
	public void rotateObject(float angle, float x, float y, float z);
	
	/**
	 * Scale the object by the amounts specified. Set to 1,1,1 for default size.
	 * 
	 * @param x X-axis scale factor.
	 * @param y Y-axis scale factor.
	 * @param z Z-axis scale factor.
	 */
	public void scaleObject(float x, float y, float z);
	
	/**
	 * Issue the OpenGL calls to render the object.
	 * 
	 * @param OpenGL context to draw in.
	 */
	public void drawObject(GL10 gl);
	
	/**
	 * Reset the object to its initial state for rendering.
	 */
	public void reset();
}
