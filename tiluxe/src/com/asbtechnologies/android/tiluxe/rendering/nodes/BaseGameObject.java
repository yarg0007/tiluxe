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
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import com.asbtechnologies.android.tiluxe.util.TextureLoader;

// Internal Imports

/**
 * Baseline implementation of GameObject. All game object implementations 
 * should extend this class. Controls translations, and calling the draw 
 * routine. 
 *
 * @author Ben Yarger
 * @version $Revision: 1.2 $
 */
public abstract class BaseGameObject implements GameObject {
	
	/** Reference ID to outside data object. */
	private int referenceID;
	
	/** Initial X axis position of the tile in the scene. */
	protected float defaultXPos;
	
	/** Initial Y axis position of the tile in the scene. */
	protected float defaultYPos;
	
	/** Initial z axis position of the tile in the scene. */
	private float defaultZPos;
	
	/** Width of the tile. */
	protected float width;
	
	/** Height of the tile. */
	protected float height;
	
	/** Raw geometry coordinates. */
	protected float[] coords;
	
	/** Raw texture coordinates. */
	protected float[] textureCoords;
	
	/** Coordinate buffer contains the coordinates for rendering. */
	protected FloatBuffer coordBuffer;
	
	/** Texture buffer contains the texture coordinates for rendering. */
	protected FloatBuffer textureBuffer;
	
	/** X axis translation amount, default should be 0. */
	protected float translateXAxis;
	
	/** Y axis translation amount, default should be 0. */
	protected float translateYAxis;
	
	/** Z axis translation amount, default should be 0. */
	protected float translateZAxis;
	
	/** Axis angle rotation, angle amount, default should be 0. */
	protected float rotateAngle;
	
	/** Axis angle rotation, z axis component, default should be 0. */
	protected float rotateXAxis;
	
	/** Axis angle rotation, y axis component, default should be 0. */
	protected float rotateYAxis;
	
	/** Axis angle rotation, z axis component, default should be 0. */
	protected float rotateZAxis;
	
	/** X axis scale amount, default should be 1. */
	protected float scaleXAxis;
	
	/** Y axis scale amount, default should be 1. */
	protected float scaleYAxis;
	
	/** Z axis scale amount, default should be 1. */
	protected float scaleZAxis;
	
	/** The default texture to always come back to. */
	protected String defaultTexture;
	
	/** The texture to display. */
	protected String useTexture;
	
	/**
	 * Reference to the texture loader with the drawable resources loaded up
	 * and ready to be used as textures.
	 */
	private TextureLoader textureLoader;
	
	/**
	 * Default constructor.
	 * 
	 * @param textureLoader Reference to TextureLoader with textures to use.
	 */
	public BaseGameObject(TextureLoader textureLoader, int referenceID) {
		
		this.textureLoader = textureLoader;
		
		defaultXPos = 0.0f;
		defaultYPos = 0.0f;
		defaultZPos = -10.0f;
		
		translateXAxis = 0.0f;
		translateYAxis = 0.0f;
		translateZAxis = 0.0f;
		
		scaleXAxis = 1.0f;
		scaleYAxis = 1.0f;
		scaleZAxis = 1.0f;
		
		rotateAngle = 0.0f;
		rotateXAxis = 1.0f;
		rotateYAxis = 0.0f;
		rotateZAxis = 0.0f;
		
		this.referenceID = referenceID;
	}

	//--------------------------------------------------------------------------
	// Methods required by GameObject
	//--------------------------------------------------------------------------
	
	/* (non-Javadoc)
	 * @see com.asbtechnologies.android.tiluxe.rendering.GameObject#dispose()
	 */
	@Override
	public void dispose() {
		textureLoader = null;
	}
	
	/* (non-Javadoc)
	 * @see com.asbtechnologies.android.tiluxe.rendering.GameObject#getReferenceID()
	 */
	@Override
	public int getReferenceID() {
		return referenceID;
	}
	
	/* (non-Javadoc)
	 * @see com.asbtechnologies.android.tiluxe.rendering.GameObject#getDefaultPosition(float[])
	 */
	@Override
	public void getDefaultPosition(float[] defaultPosition) 
		throws ArrayIndexOutOfBoundsException {
		
		if (defaultPosition == null || defaultPosition.length < 3) {
			
			throw new ArrayIndexOutOfBoundsException();
		}
		
		defaultPosition[0] = defaultXPos;
		defaultPosition[1] = defaultYPos;
		defaultPosition[2] = defaultZPos;
	}
	
	/* (non-Javadoc)
	 * @see com.asbtechnologies.android.tiluxe.rendering.GameObject#translateObject(float, float, float)
	 */
	@Override
	public void translateObject(float x, float y, float z) {
		
		translateXAxis = x;
		translateYAxis = y;
		translateZAxis = z;
	}

	/* (non-Javadoc)
	 * @see com.asbtechnologies.android.tiluxe.rendering.GameObject#rotateObject(float, float, float, float)
	 */
	@Override
	public void rotateObject(float angle, float x, float y, float z) {
		
		rotateAngle = angle;
		rotateXAxis = x;
		rotateYAxis = y;
		rotateZAxis = z;
	}

	/* (non-Javadoc)
	 * @see com.asbtechnologies.android.tiluxe.rendering.GameObject#scaleObject(float, float, float)
	 */
	@Override
	public void scaleObject(float x, float y, float z) {
		
		scaleXAxis = x;
		scaleYAxis = y;
		scaleZAxis = z;
	}

	/* (non-Javadoc)
	 * @see com.asbtechnologies.android.tiluxe.rendering.GameObject#drawObject()
	 */
	@Override
	public void drawObject(GL10 gl) {
		
		textureLoader.setTexture(useTexture);
		
		gl.glLoadIdentity();                 // Reset the model-view matrix
		gl.glTranslatef(defaultXPos+translateXAxis, defaultYPos+translateYAxis, defaultZPos+translateZAxis); // Translate left and into the screen
//		gl.glRotatef(anglePyramid, 0.1f, 1.0f, -0.1f); // Rotate (NEW)
		gl.glScalef(scaleXAxis, scaleYAxis, scaleZAxis);
		
	    // Enable vertex-array and define its buffer
	    gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
	    gl.glVertexPointer(3, GL10.GL_FLOAT, 0, coordBuffer);
	    
	    gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);  // Enable texture-coords-array (NEW)
	    gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer); // Define texture-coords buffer (NEW)
	    
	    // Draw the primitives from the vertex-array directly
	    gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, coords.length / 3);
	    
	    gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);  // Disable texture-coords-array (NEW)
	    gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);

	}
	
	/* (non-Javadoc)
	 * @see com.asbtechnologies.android.tiluxe.rendering.nodes.GameObject#reset()
	 */
	@Override
	public void reset() {
		return;
	}
}
