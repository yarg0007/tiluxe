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
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Random;

import android.view.MotionEvent;

// Internal Imports
import com.asbtechnologies.android.tiluxe.board.BoardPieceState;
import com.asbtechnologies.android.tiluxe.util.TextureLoader;

/**
 * Default implementation of a game tile. Creates the geometric representation 
 * of the tile. These are pickable and are what make up the playing board.
 *
 * @author Ben Yarger
 * @version $Revision: 1.3 $
 */
public class TileGameObject extends BaseGameObject 
	implements PickableGameObject{
	
	/** Flag if the tile is an empty tile or not. */
	private boolean isEmpty;
	
	/** The current selection state of the tile at creation time. */
	private int currentState;
	
	/** The texture initially set to the tile. */
	private int initialTextureIndex = 0;
	
	/**
	 * Texture options that can be used with TileGameObjects.
	 */
	private static String[] textureOptions = new String[]{
		"free_space",
		"tile_neutral_option_a",
		"tile_neutral_option_b",
		"tile_neutral_option_c",
		"tile_neutral_option_d",
		"tile_turned_off",
		"tile_turned_on"
	};
	
	/**
	 * Textures to cycle through.
	 */
	private String[] textureCycle;
	
	/**
	 * Index of the texture cycle.
	 */
	private int textureCycleIndex;
	
	/**
	 * Default constructor. Takes in the parameters for the 2D tile and then
	 * creates the geometric representation of the tile with one of the 
	 * standard tile texture graphics.
	 * 
	 * @param textureLoader Reference to TextureLoader with textures to use.
	 * @param xPos X axis position of tile in 3 space.
	 * @param yPos Y axis position of tile in 3 space.
	 * @param width Width of tile.
	 * @param height Height of tile.
	 * @param isEmtpy True to create an empty tile piece, false otherwise.
	 * @param pieceID Unique ID reference back to board piece.
	 * @param currentState The current selection state of the tile.
	 */
	public TileGameObject(
			TextureLoader textureLoader, 
			float xPos, 
			float yPos, 
			float width, 
			float height,
			boolean isEmpty,
			int pieceID,
			int currentState) {
		
		super(textureLoader, pieceID);

		this.defaultXPos = xPos;
		this.defaultYPos = yPos;
		this.width = width;
		this.height = height;
		this.isEmpty = isEmpty;
		this.currentState = currentState;
		
		initializeTile();
	}

	/* (non-Javadoc)
	 * @see com.asbtechnologies.android.tiluxe.rendering.nodes.BaseGameObject#reset()
	 */
	@Override
	public void reset() {
		textureCycleIndex = initialTextureIndex;
		useTexture = textureCycle[textureCycleIndex];
	}
	
	//--------------------------------------------------------------------------
	// Methods required by PickableGameObject
	//--------------------------------------------------------------------------
	
	/* (non-Javadoc)
	 * @see com.asbtechnologies.android.tiluxe.rendering.PickableGameObject#isPicked(float, float)
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
		
		translateZAxis = 0.0f;
		scaleXAxis = 1.0f;
		scaleYAxis = 1.0f;
		
		if (minObjX < xPos && xPos < maxObjX) {
			
			if (minObjY < yPos && yPos < maxObjY) {
				
				if ((int)motionType == MotionEvent.ACTION_UP) {
					textureCycleIndex++;
					
					if (textureCycleIndex >= textureCycle.length) {
						textureCycleIndex = 0;
					}
					
					useTexture = textureCycle[textureCycleIndex];
				
				} else if ((int)motionType == MotionEvent.ACTION_MOVE ||
						(int)motionType == MotionEvent.ACTION_DOWN) {
					
					translateZAxis = 1.0f;
					scaleXAxis = 3.0f;
					scaleYAxis = 3.0f;
				}
				
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

		// Randomly choose the default texture to apply, or set to empty piece
		if (isEmpty) {

			defaultTexture = textureOptions[0];
			useTexture = defaultTexture;
			
			textureCycle = new String[] {
				"free_space"
			};
			
		} else {
			Random randomGenerator = new Random();
			int randomIndex = randomGenerator.nextInt(4)+1;
			defaultTexture = textureOptions[randomIndex];
			
			textureCycle = new String[] {
				defaultTexture,
				"tile_turned_on",
				"tile_turned_off"
			};
			
			if (currentState == BoardPieceState.STATE_LIMBO) {
				textureCycleIndex = 0;
				useTexture = textureCycle[textureCycleIndex];
			} else if (currentState == BoardPieceState.STATE_ALIVE) {
				textureCycleIndex = 1;
				useTexture = textureCycle[textureCycleIndex];
			} else if (currentState == BoardPieceState.STATE_DEAD) {
				textureCycleIndex = 2;
				useTexture = textureCycle[textureCycleIndex];
			}
			
			initialTextureIndex = textureCycleIndex;
		}
	}
}
