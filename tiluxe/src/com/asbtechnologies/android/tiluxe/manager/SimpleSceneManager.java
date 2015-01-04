/*****************************************************************************
 *                        Copyright ASB Technologies (c) 2011
 *                               Java Source
 *
 * This source is the property of ASB Technologies. Any duplication or reuse
 * without the consent of ASB Technologies is prohibited.
 *
 ****************************************************************************/
package com.asbtechnologies.android.tiluxe.manager;

// External Imports
import java.util.HashMap;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.content.res.Resources;

//Internal Imports
import com.asbtechnologies.android.tiluxe.board.BoardPiece;
import com.asbtechnologies.android.tiluxe.board.DefaultBoard;
import com.asbtechnologies.android.tiluxe.board.DefaultBoardPiece;
import com.asbtechnologies.android.tiluxe.rendering.nodes.GameObject;
import com.asbtechnologies.android.tiluxe.rendering.nodes.LegendGameObject;
import com.asbtechnologies.android.tiluxe.rendering.nodes.PickableGameObject;
import com.asbtechnologies.android.tiluxe.rendering.nodes.TileGameObject;
import com.asbtechnologies.android.tiluxe.util.TextureLoader;

/**
 * Simple scene graph that holds of the scene assets to render, performs scene
 * operations like picking, loading textures and drawing the scene each frame.
 *
 * @author Ben Yarger
 * @version $Revision: 1.3 $
 */
public class SimpleSceneManager {
	
	/** Vertical adjustment of playing board when rendered. */
	private static final float FIXED_VERTICAL_ADJUSTMENT = -3.0f;
	
	/** Texture loader to load and retrieve textures with. */
	private TextureLoader textureLoader;
	
	/** Reference to Android context. */
	private Context context;
	
	/** True when a scene has been created and is live, false otherwise. */
	private boolean isSceneLive;
	
	/** Reference to the playing board. */
	private DefaultBoard board;
	
	/** Game objects to draw each frame. */
	private GameObject[] gameObjects;
	
	/** Map of BoardPiece reference ID's to GameObject map. */
	private HashMap<Integer, GameObject> referenceIDToGameObjMap;
	
	/** The last picked reference ID */
	private int lastPickReferenceID;
	
	/** Texture resources to load up. */
	private String[] textureResources = new String[] {
			"free_space",
			"tile_neutral_option_a",
			"tile_neutral_option_b",
			"tile_neutral_option_c",
			"tile_neutral_option_d",
			"tile_turned_off",
			"tile_turned_on",
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
	 * Default constructor.
	 * @param gl OpenGL reference
	 * @param context Android context
	 */
	public SimpleSceneManager(GL10 gl, Context context) {
		
		// Load up the textures we will need to use.
		this.textureLoader = new TextureLoader(gl, context);
		this.context = context;
		loadTextures();
	}
	
	//--------------------------------------------------------------------------
	// Public methods.
	//--------------------------------------------------------------------------
	
	/**
	 * Initializes all of the scene graphics. Sets up the scene objects.
	 * 
	 * @param board Board to represent graphically.
	 * @param displayWidth Width of the display.
	 * @param displayHeight Height of the display.
	 */
	public void initializeGraphics(
			DefaultBoard board, 
			int displayWidth, 
			int displayHeight) {
		
		setupScene(board, displayWidth, displayHeight);
		isSceneLive = true;
	}
	
	/**
	 * Clean up and shut down the scene.
	 */
	public void disposeOfScene() {
		
		textureLoader.dispose();
		gameObjects = null;
		referenceIDToGameObjMap = null;
		context = null;
		isSceneLive = false;
		board = null;
	}
	
	/**
	 * Check if the scene is live. A live scene is one that is setup and 
	 * rendering each frame.
	 * 
	 * @return True if the scene is live, false otherwise.
	 */
	public boolean isSceneLive() {
		return isSceneLive;
	}
	
	/**
	 * Draw the full scene.
	 * 
	 * @param gl OpenGL context to render with.
	 */
	public void drawScene(GL10 gl) {
		
		if (gameObjects != null) {
			
			for (int i = 0; i < gameObjects.length; i++) {
				
				gameObjects[i].drawObject(gl);
			}
		}
	}
	
	/**
	 * Perform a pick test on the scene and return the reference ID of the 
	 * scene object that was picked.
	 * 
	 * @param motionType Type of input motion (Android MotionEvent value).
	 * @param xPos Horizontal screen position.
	 * @param yPos Vertical screen position.
	 * @return Reference ID value or -1 if nothing picked.
	 */
	public int testPick(float motionType, float xPos, float yPos) {

		for (int i = 0; i < gameObjects.length; i++) {
			
			if (gameObjects[i] instanceof PickableGameObject) {
				
				if (((PickableGameObject)gameObjects[i]).isPicked(
						motionType, xPos, yPos)) {
	
					if (lastPickReferenceID != 
						gameObjects[i].getReferenceID()) {

						// Move the last pick back
						GameObject oldPick = 
							referenceIDToGameObjMap.get(lastPickReferenceID);
						
						if (oldPick != null) {
							oldPick.translateObject(0.0f, 0.0f, 0.0f);
							oldPick.scaleObject(1.0f, 1.0f, 1.0f);
						}
						
						lastPickReferenceID = gameObjects[i].getReferenceID();
					}
					
					return gameObjects[i].getReferenceID();
				}
			}
		}
		
		return -1;
	}
	
	/**
	 * Update the legend hints displayed around the game board.
	 */
	public void updateLegends() {
		
		int[] horizontalHint = 
			board.getHorizontalLegendHint();
		int[] verticalHint = 
			board.getVerticalLegendHint();
		
		for (int j = 0; j < gameObjects.length; j++) {
			
			// If the last pick was the hint button, make sure we display 
			// the hint version of the legend. Do this until another pick 
			// is made.
			if (gameObjects[j] instanceof LegendGameObject) {

				((LegendGameObject)gameObjects[j]).
					renderHint(true, horizontalHint, verticalHint);

			}
		}
	}
	
	public void resetScene() {
		
		for (int i = 0; i < gameObjects.length; i++) {
			
			gameObjects[i].reset();
		}
	}
	
	//--------------------------------------------------------------------------
	// Private methods
	//--------------------------------------------------------------------------
	
	/**
	 * Setup all of the scene objects before rendering the first frame.
	 * 
	 * @param board Playing board to display.
	 * @param displayWidth Width of the display.
	 * @param displayHeight Height of the display.
	 */
	private void setupScene(
			DefaultBoard board, 
			int displayWidth, 
			int displayHeight) {
		
		DefaultBoardPiece[][] boardPieces = 
			(DefaultBoardPiece[][]) board.getBoardPieces();
		
		if (boardPieces == null) {
			return;
		}
		
		this.board = board;
		
		referenceIDToGameObjMap = new HashMap<Integer, GameObject>();
		
		// Get the dimensions of the board.
		int boardWidth = board.getWidth();
		int boardHeight = board.getHeight();
		
		// The fill width is the most critical piece because it is the least 
		// amount of space to fill. We will then use the same value to make
		// each tile square in the height dimension. Use boardWidth + 1 
		// to account for the legend.
		float tileWidth = displayWidth/(boardWidth+1.0f);
		float tileHeight = tileWidth;
		float initialXPosition = tileWidth/2.0f;
		float initialYPosition = tileHeight/2.0f;
		
		// We expect to have an orthographic projection with the center of the
		// screen at 0,0,0. Since we want to start to fill from the top of the 
		// screen we have to begin placing from -0.5*screenWidth, 
		// 0.5*screenHeight.
		
		float startTopLeftXPosition = -displayWidth/2.0f;
		float startTopLeftYPosition = displayHeight/2.0f;
		
		// Padding to apply to tiles to prevent edges from touching. Applied
		// to each side of every tile.
		float tilePadding = 1.0f;
		
		// Total number of objects is the board width * board height + 
		// the vertical legend + horizontal legend. 
		int totalNumberOfObjects = 
			boardWidth * boardHeight + boardWidth + boardHeight;
		
		gameObjects = new GameObject[totalNumberOfObjects];
		
		// Start by filling the board with the tiles.
		int indexTracker = 0;
		boolean isEmpty;
		
		for (int w = 0; w < boardWidth; w++) {
			for (int h = 0; h < boardHeight; h++) {
				
				isEmpty = false;
				
				if (boardPieces[w][h].isEmpty()) {
					
					isEmpty = true;
				}
				
				gameObjects[indexTracker] = new TileGameObject(
						textureLoader, 
						startTopLeftXPosition + initialXPosition + tileWidth * w, 
						startTopLeftYPosition - initialYPosition - tileHeight * h + FIXED_VERTICAL_ADJUSTMENT, 
						tileWidth - 2.0f * tilePadding, 
						tileHeight - 2.0f * tilePadding,
						isEmpty,
						boardPieces[w][h].getPieceID(),
						boardPieces[w][h].getCurrentState());
				
				referenceIDToGameObjMap.put(
						boardPieces[w][h].getPieceID(), 
						gameObjects[indexTracker]);
				
				indexTracker++;
			}
		}
		
		// Now create the legend pieces. Start with the vertical legend and then
		// create the horizontal legend.
		int[] verticalLegend = board.getVerticalLegend();
		
		for (int i = 0; i < verticalLegend.length; i++) {
			
			gameObjects[indexTracker] = new LegendGameObject(
					textureLoader, 
					startTopLeftXPosition + initialXPosition + tileWidth * boardWidth, 
					startTopLeftYPosition - initialYPosition - tileHeight * i + FIXED_VERTICAL_ADJUSTMENT,
					tileWidth - 2.0f * tilePadding, 
					tileHeight - 2.0f * tilePadding,
					verticalLegend[i],
					BoardPiece.VERTICAL_ORIENTATION,
					i);
			
			indexTracker++;
		}
		
		int[] horizontalLegend = board.getHorizontalLegend();
		
		for (int i = 0; i < horizontalLegend.length; i++) {
			
			gameObjects[indexTracker] = new LegendGameObject(
					textureLoader, 
					startTopLeftXPosition + initialXPosition + tileWidth * i, 
					startTopLeftYPosition - initialYPosition - tileHeight * boardWidth + FIXED_VERTICAL_ADJUSTMENT, 
					tileWidth - 2.0f * tilePadding, 
					tileHeight - 2.0f * tilePadding,
					horizontalLegend[i],
					BoardPiece.HORIZONTAL_ORIENTATION,
					i);
			
			indexTracker++;
		}

		// Now initialize the hint state of all legend tiles
		updateLegends();
	}
	
	/**
	 * Load the textures to be used. Loads all drawable resources defined in 
	 * textureResources.
	 */
	private void loadTextures() {
		
		int resourceID;
		Resources resources = context.getResources();
		
		for (int i = 0; i < textureResources.length; i++) {
			
			resourceID = resources.getIdentifier(
					textureResources[i], 
					"drawable", 
					"com.asbtechnologies.android.tiluxe");
			
			// Add the texture to the list of resources to be loaded by the
			// texture loader utility.
			textureLoader.addTexture(textureResources[i], resourceID);
		}
		
		// Now load the textures for use.
		textureLoader.loadTextures();
	}
}
