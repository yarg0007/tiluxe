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

// Internal Imports
import com.asbtechnologies.android.tiluxe.board.BoardPerimeterLegend;
import com.asbtechnologies.android.tiluxe.board.BoardPiece;
import com.asbtechnologies.android.tiluxe.util.TextureLoader;

/**
 * Default implementation of legend tile. Creates the geometric representation
 * of the tile that informs the user of the status of the row or column and 
 * how many hidden targets are in that row or column.
 *
 * @author Ben Yarger
 * @version $Revision: 1.1 $
 */
public class LegendGameObject extends BaseGameObject {
	
	/** Value held by legend tile. */
	private int legendAmount;
	
	/** Vertical or Horizontal legend orientation (Use BoardPiece constants). */
	private int orientation;
	
	/** The index of the legend row or column. */
	private int index;
	
	/**
	 * Texture options that can be used with TileGameObjects.
	 * 
	 * number_*_a are the correct hint icons in green.
	 * number_*_c are the incorrect hint icons in red.
	 * 
	 * There are 10 icons (0 - 9) in each set.
	 */
	private static String[] textureOptions = new String[]{
		"number_0_a",
		"number_1_a",
		"number_2_a",
		"number_3_a",
		"number_4_a",
		"number_5_a",
		"number_6_a",
		"number_7_a",
		"number_8_a",
		"number_9_a",
		"number_0_c",
		"number_1_c",
		"number_2_c",
		"number_3_c",
		"number_4_c",
		"number_5_c",
		"number_6_c",
		"number_7_c",
		"number_8_c",
		"number_9_c"
	};
	
	/**
	 * Default constructor. Takes in the parameters for the 2D tile and then
	 * creates the geometric representation of the tile with one of the 
	 * legend tile texture graphics.
	 * 
	 * @param textureLoader Reference to TextureLoader with textures to use.
	 * @param xPos X axis position of tile in 3 space.
	 * @param yPos Y axis position of tile in 3 space.
	 * @param width Width of tile.
	 * @param height Height of tile.
	 * @param legendAmount The value held by the legend tile.
	 * @param orientation The orientation of the legend.
	 * @param index The row or column index of the legend tile.
	 */
	public LegendGameObject(
			TextureLoader textureLoader, 
			float xPos, 
			float yPos, 
			float width, 
			float height,
			int legendAmount,
			int orientation,
			int index) {
		
		super(textureLoader, -1);

		this.defaultXPos = xPos;
		this.defaultYPos = yPos;
		this.width = width;
		this.height = height;
		this.legendAmount = legendAmount;
		this.orientation = orientation;
		this.index = index;
		
		initializeTile();
	}

	/**
	 * Render the legend hint to show if the associated row or column is correct
	 * or incorrect.
	 * 
	 * @param enable True to enable hints, false otherwise.
	 * @param horizontalHint Horizontal hints to apply.
	 * @param verticalHint Vertical hints to apply.
	 */
	public void renderHint(
			boolean enable, 
			int[] horizontalHint, 
			int[] verticalHint) {
		
		if (enable == false) {
			// Set the default texture based on the legend amount.
			useTexture = defaultTexture;
			return;
		}

		if (orientation == BoardPiece.HORIZONTAL_ORIENTATION) {

			switch (horizontalHint[index]) {
			
			case BoardPerimeterLegend.HINT_ALL_CORRECT:
				useTexture = textureOptions[legendAmount];
				break;
				
			case BoardPerimeterLegend.HINT_ALMOST_CORRECT:
			case BoardPerimeterLegend.HINT_TOO_MANY:
			case BoardPerimeterLegend.HINT_TOO_FEW:
				useTexture = textureOptions[legendAmount + 10];
				break;
			}
			
		} else {

			switch (verticalHint[index]) {
			
			case BoardPerimeterLegend.HINT_ALL_CORRECT:
				useTexture = textureOptions[legendAmount];
				break;
				
			case BoardPerimeterLegend.HINT_ALMOST_CORRECT:
			case BoardPerimeterLegend.HINT_TOO_MANY:
			case BoardPerimeterLegend.HINT_TOO_FEW:
				useTexture = textureOptions[legendAmount + 10];
				break;
			}
		}
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

		// Set the default texture based on the legend amount.
		defaultTexture = textureOptions[legendAmount];
		useTexture = defaultTexture;
	}
}
