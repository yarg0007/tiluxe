/*****************************************************************************
 *                        Copyright ASB Technologies (c) 2011
 *                               Java Source
 *
 * This source is the property of ASB Technologies. Any duplication or reuse
 * without the consent of ASB Technologies is prohibited.
 *
 ****************************************************************************/
package com.asbtechnologies.android.tiluxe.rendering.nodes;

// External Import
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

// Internal Import
import com.asbtechnologies.android.tiluxe.util.TextureLoader;

/**
 * Implementation of a button object in the game. Button game objects are 
 * pickable and provide functionality like a button widget.
 *
 * @author Ben Yarger
 * @version $Revision: 1.3 $
 */
public class ButtonGameObject extends BaseGameObject 
	implements PickableGameObject{

	/**
	 * Default constructor.
	 * 
	 * @param textureLoader Reference to TextureLoader with textures to use.
	 * @param xPos X axis position of tile in 3 space.
	 * @param yPos Y axis position of tile in 3 space.
	 * @param width Width of tile.
	 * @param height Height of tile.
	 * @param referenceID Unique ID to correctly identify this button with.
	 */
	public ButtonGameObject(
			TextureLoader textureLoader, 
			float xPos, 
			float yPos, 
			float width, 
			float height,
			int referenceID) {
		
		super(textureLoader, referenceID);
		
		this.defaultXPos = xPos;
		this.defaultYPos = yPos;
		this.width = width;
		this.height = height;
		
		initializeTile();
	}
	
	//--------------------------------------------------------------------------
	// Methods required by pickable game object
	//--------------------------------------------------------------------------
	
	/* (non-Javadoc)
	 * @see com.asbtechnologies.android.tiluxe.rendering.PickableGameObject#isPicked(float, float, float)
	 */
	@Override
	public boolean isPicked(float motionType, float xPos, float yPos) {
		
		float objXPos = defaultXPos + translateXAxis;
		float objYPos = defaultYPos + translateYAxis;
		
		float objWidth = width * scaleXAxis / 2.0f;
		float objHeight = height * scaleYAxis / 2.0f;
		
		float maxObjX = objXPos + objWidth;
		float minObjX = objXPos - objWidth;
		
		float maxObjY = objYPos + objHeight;
		float minObjY = objYPos - objHeight;
		
		translateZAxis = -10.0f;
		scaleXAxis = 1.0f;
		scaleYAxis = 1.0f;
		
		if (minObjX < xPos && xPos < maxObjX) {
			
			if (minObjY < yPos && yPos < maxObjY) {
				return true;
			}
		}
		
		return false;
	}

	//--------------------------------------------------------------------------
	// Private Methods
	//--------------------------------------------------------------------------
	
	/**
	 * Initialize the tile based on the position, width and height supplied to
	 * the constructor.
	 */
	private void initializeTile() {
	
		// Coordinates ordered bottom left, bottom right, top left, top right.
		coords = new float[] {
				-width/2.0f, -height/2.0f, 0.0f,
				width/2.0f, -height/2.0f, 0.0f,
				-width/2.0f, height/2.0f, 0.0f,
				width/2.0f, height/2.0f, 0.0f
		};
		
		// Texture coordinates bottom left, bottom right, top left, top right
		textureCoords = new float[] {
				0.0f, 1.0f,
				1.0f, 1.0f,
				0.0f, 0.0f,
				1.0f, 0.0f
		};
				   
        // float has 4 bytes, coordinate * 4 bytes
        ByteBuffer vbb = ByteBuffer.allocateDirect(coords.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        coordBuffer = vbb.asFloatBuffer();
        coordBuffer.put(coords);
        coordBuffer.position(0);
        
        // float is 4 bytes, therefore we multiply the number if
		// vertices with 4.
        ByteBuffer byteBuf = 
        	ByteBuffer.allocateDirect(textureCoords.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		textureBuffer = byteBuf.asFloatBuffer();
		textureBuffer.put(textureCoords);
		textureBuffer.position(0);

		// Set the texture to ui_hint.
		defaultTexture = "ui_hint";
		useTexture = defaultTexture;

	}

}
