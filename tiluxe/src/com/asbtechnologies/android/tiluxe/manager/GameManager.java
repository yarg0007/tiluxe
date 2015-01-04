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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.opengl.GLSurfaceView;

// Internal Imports
import com.asbtechnologies.android.tiluxe.R;
import com.asbtechnologies.android.tiluxe.SetPreferencesActivity;
import com.asbtechnologies.android.tiluxe.board.DefaultBoard;
import com.asbtechnologies.android.tiluxe.board.DefaultBoardGenerator;
import com.asbtechnologies.android.tiluxe.rendering.GameGLView;
import com.asbtechnologies.android.tiluxe.rendering.OpenGLRendererListener;

/**
 * Entry point for a new game.
 * 
 * Manages the game loop and creates the GLSurfaceView. Processes input, 
 * updates the board and updates the graphics pipeline with changes. Controls 
 * when the game is over, and will send messages of game state to listeners
 * registered with the GameManager.
 *
 * @author Ben Yarger
 * @version $Revision: 1.7 $
 */
public class GameManager extends Thread implements OpenGLRendererListener {
	
	/** Listeners registered with the GameManager. */
	private ArrayList<GameManagerListener> listeners;
	
	/** OpenGL ES View. */
	private GameGLView gameView;
	
	/** The board generator used to populate a new game board. */
	private DefaultBoardGenerator boardGenerator;
	
	/** Shared preferences to retrieve data like difficulty settings. */
	private SharedPreferences preferences;
	
	/** The Android context passed in from the parent Activity. */
	private Context context;
	
	/** Android references for accessing drawables, strings etc. */
	private Resources resources;
	
	/** The board created by the boardGenerator. */
	private DefaultBoard board;
	
	/** Flag to know if a game is currently in progress or not. */
	private boolean gameInProgress;
	
	/** Flag to know if the renderer is ready. */
	private boolean rendererReady;
	
	/** Flag paused game. */
	private boolean pause;
	
	/** Map of board size to dimensions value. */
	private Map<String, Integer> boardSizeDimensionsTable;
	
	/** Map of board size to piece lengths. */
	private Map<String, int[]> boardSizePieceLengthsTable;
	
	/** Map of board size and difficulty to the number of free tiles. */
	private Map<String, Map<String, Integer>> boardSizeFreeTilesTable;
	
	/** Map of board size and difficulty to the number of free targets. */
	private Map<String, Map<String, Integer>> boardSizeFreeTargetsTable;
	
	/**
	 * Default constructor.
	 * 
	 * @param context Android context of view parent.
	 */
	public GameManager(Context context) {
		
		this.context = context;
		
		this.resources = this.context.getResources();
		
		this.preferences = context.getSharedPreferences(
					SetPreferencesActivity.PREFERENCE_NAME, 
					Context.MODE_PRIVATE);
		
		// Get the level of difficulty to build into the board.
		String difficulty = preferences.getString(
				(String) this.resources.getText(R.string.difficulty_key), 
				(String) this.resources.getText(R.string.easy_setting));
		
		// Get the board size.
		String boardSize = preferences.getString(
				(String) this.resources.getText(R.string.board_size_key), 
				(String) this.resources.getText(R.string.six_by_six));
		
		initializeGameOptionTables();
		
		Integer boardDimension = boardSizeDimensionsTable.get(boardSize);
		int[] boardPieceLengths = boardSizePieceLengthsTable.get(boardSize);
		
		Integer numFreeEmpty = 
			boardSizeFreeTilesTable.get(boardSize).get(difficulty);

		Integer numFreeTargets =
			boardSizeFreeTargetsTable.get(boardSize).get(difficulty);
		
		// Generate the board.
		this.boardGenerator = new DefaultBoardGenerator();
		
		while (board == null) {
			
			board = (DefaultBoard) 
				boardGenerator.generateCustomBoard(
					boardDimension.intValue(), 
					boardDimension.intValue(), 
					boardPieceLengths, 
					numFreeEmpty.intValue(),
					numFreeTargets.intValue());
		}
		
		board.printBoard();
		
		this.gameView = new GameGLView(context, board);
		this.gameView.passThroughOpenGLRendererListener(this);
		
		this.listeners = new ArrayList<GameManagerListener>();
		
		this.gameInProgress = false;
		this.rendererReady = false;
		this.pause = false;
	}
	
	/**
	 * Creates a specific game board according to the parameters passed in. 
	 * Board pieces are created in the order specified by pieceLengths.
	 * 
	 * @param context Android context of view parent.
	 * @param boardWidth Width of the board to create.
	 * @param boardHeight Height of the board to create.
	 * @param pieceLengths Length values of the pieces to create.
	 * @param numberOfFreeEmpty Number of free tiles to give away.
	 * @param numberOfFreeTargets Number of target tiles to give away.
	 */
	public GameManager(
			Context context,
			int boardWidth,
			int boardHeight,
			int[] pieceLengths,
			int numberOfFreeEmpty,
			int numberOfFreeTargets) {
		
		this.context = context;
		
		// Generate the board.
		this.boardGenerator = new DefaultBoardGenerator();

		board = null;
		
		while (board == null) {

			board = (DefaultBoard) 
				boardGenerator.generateCustomBoard(
					boardWidth, 
					boardHeight, 
					pieceLengths, 
					numberOfFreeEmpty,
					numberOfFreeTargets);
		}

		board.printBoard();
		
		this.gameView = new GameGLView(context, board);
		this.gameView.passThroughOpenGLRendererListener(this);
		
		this.listeners = new ArrayList<GameManagerListener>();
		
		this.gameInProgress = false;
		this.rendererReady = false;
		this.pause = false;
	}
	
	/**
	 * Get the GLSurfaceView controlled by the GameManager.
	 * 
	 * @return GLSurfaceView.
	 */
	public GLSurfaceView getView() {
		return gameView;
	}
	
	/**
	 * Register a listener for callback on GameManager events.
	 * 
	 * @param listener Listener to call back.
	 */
	public void registerListener(GameManagerListener listener) {
		listeners.add(listener);
	}
	
	/**
	 * Start a new game. Creates the brand new board, and then generates the 
	 * visuals for the board. Then kicks off the game thread that will process
	 * user input and update the graphics. If the game is currently running,
	 * then it will not be interrupted. Will only generate new games if the 
	 * current game is over.
	 */
	public void playNewGame() {
		
		// Do not interrupt a game already in progress.
		if (gameInProgress || this.isAlive()) {
			return;
		}
		
		// Notify all listeners that the game is starting then start the game
		notifyListeners(GameManagerListener.GM_STARTED);
		this.start();
	}
	
	/**
	 * Pause the current game.
	 */
	public void pauseGame() {
		
		if (gameInProgress) {
			pause = true;
		}
		gameView.onPause();
	}
	
	/**
	 * Restart the current game.
	 */
	public void resumeGame() {
		pause = false;
		this.interrupt();
		gameView.onResume();
	}
	
	/**
	 * Quit the current game.
	 */
	public void quitGame() {
		gameInProgress = false;
	}
	
	/**
	 * Reset the board to its starting state. In effect - clear the board.
	 */
	public void resetGame() {
		
		board.resetBoard();
		gameView.resetGraphics();
	}
	
	//--------------------------------------------------------------------------
	// Methods required by OpenGLRendererListener
	//--------------------------------------------------------------------------
	
	/* (non-Javadoc)
	 * @see com.asbtechnologies.android.tiluxe.rendering.OpenGLRendererListener#openGLRendererNotification(int)
	 */
	@Override
	public void openGLRendererNotification(int message) {
		
		switch (message) {
		
		case OpenGLRendererListener.RENDERER_READY:
			rendererReady = true;
			notifyListeners(GameManagerListener.GM_READY);
			break;
			
		case OpenGLRendererListener.RENDERER_CLOSED:
			rendererReady = false;
			notifyListeners(GameManagerListener.GM_GAME_OVER);
			break;
		}
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
			listeners.get(i).gameManagerNotification(message);
		}
	}
	
	/**
	 * Setup the lookup tables for the different game configurations.
	 */
	private void initializeGameOptionTables() {
		
		// Set the board size dimension table.
		boardSizeDimensionsTable = new HashMap<String, Integer>();
		
		boardSizeDimensionsTable.put(
				(String) this.resources.getText(R.string.four_by_four), 4);
		boardSizeDimensionsTable.put(
				(String) this.resources.getText(R.string.five_by_five), 5);
		boardSizeDimensionsTable.put(
				(String) this.resources.getText(R.string.six_by_six), 6);
		boardSizeDimensionsTable.put(
				(String) this.resources.getText(R.string.seven_by_seven), 7);
		boardSizeDimensionsTable.put(
				(String) this.resources.getText(R.string.eight_by_eight), 8);
		boardSizeDimensionsTable.put(
				(String) this.resources.getText(R.string.nine_by_nine), 9);
		boardSizeDimensionsTable.put(
				(String) this.resources.getText(R.string.ten_by_ten), 10);
		
		// Set the board size piece lengths table.
		boardSizePieceLengthsTable = new HashMap<String, int[]>();
		
		boardSizePieceLengthsTable.put(
				(String) this.resources.getText(R.string.four_by_four),
				new int[] {2, 1, 1});
		boardSizePieceLengthsTable.put(
				(String) this.resources.getText(R.string.five_by_five),
				new int[] {2, 1, 1, 1});
		boardSizePieceLengthsTable.put(
				(String) this.resources.getText(R.string.six_by_six),
				new int[] {3, 2, 1, 1, 1});
		boardSizePieceLengthsTable.put(
				(String) this.resources.getText(R.string.seven_by_seven),
				new int[] {3, 3, 2, 2, 1, 1});
		boardSizePieceLengthsTable.put(
				(String) this.resources.getText(R.string.eight_by_eight),
				new int[] {4, 3, 2, 2, 1, 1, 1});
		boardSizePieceLengthsTable.put(
				(String) this.resources.getText(R.string.nine_by_nine),
				new int[] {4, 3, 3, 2, 2, 1, 1, 1});
		boardSizePieceLengthsTable.put(
				(String) this.resources.getText(R.string.ten_by_ten),
				new int[] {4, 3, 3, 2, 2, 2, 1, 1, 1, 1});
	
		// Set the number of free targets based on board size and difficulty.
		boardSizeFreeTargetsTable = new HashMap<String, Map<String,Integer>>();
		
		HashMap<String, Integer> tmpMap = new HashMap<String, Integer>();
		
		// 4x4
		tmpMap = new HashMap<String, Integer>();
		tmpMap.put(
				(String) this.resources.getText(R.string.easy_setting),
				Integer.valueOf(1));
		tmpMap.put(
				(String) this.resources.getText(R.string.medium_setting),
				Integer.valueOf(1));
		tmpMap.put(
				(String) this.resources.getText(R.string.hard_setting),
				Integer.valueOf(0));
		
		boardSizeFreeTargetsTable.put(
				(String) this.resources.getText(R.string.four_by_four),
				tmpMap);

		// 5x5
		tmpMap = new HashMap<String, Integer>();
		tmpMap.put(
				(String) this.resources.getText(R.string.easy_setting),
				Integer.valueOf(2));
		tmpMap.put(
				(String) this.resources.getText(R.string.medium_setting),
				Integer.valueOf(1));
		tmpMap.put(
				(String) this.resources.getText(R.string.hard_setting),
				Integer.valueOf(0));
		
		boardSizeFreeTargetsTable.put(
				(String) this.resources.getText(R.string.five_by_five),
				tmpMap);
		
		// 6x6
		tmpMap = new HashMap<String, Integer>();
		tmpMap.put(
				(String) this.resources.getText(R.string.easy_setting),
				Integer.valueOf(3));
		tmpMap.put(
				(String) this.resources.getText(R.string.medium_setting),
				Integer.valueOf(2));
		tmpMap.put(
				(String) this.resources.getText(R.string.hard_setting),
				Integer.valueOf(1));
		
		boardSizeFreeTargetsTable.put(
				(String) this.resources.getText(R.string.six_by_six),
				tmpMap);
		
		// 7x7
		tmpMap = new HashMap<String, Integer>();
		tmpMap.put(
				(String) this.resources.getText(R.string.easy_setting),
				Integer.valueOf(4));
		tmpMap.put(
				(String) this.resources.getText(R.string.medium_setting),
				Integer.valueOf(3));
		tmpMap.put(
				(String) this.resources.getText(R.string.hard_setting),
				Integer.valueOf(2));
		
		boardSizeFreeTargetsTable.put(
				(String) this.resources.getText(R.string.seven_by_seven),
				tmpMap);
		
		// 8x8
		tmpMap = new HashMap<String, Integer>();
		tmpMap.put(
				(String) this.resources.getText(R.string.easy_setting),
				Integer.valueOf(5));
		tmpMap.put(
				(String) this.resources.getText(R.string.medium_setting),
				Integer.valueOf(4));
		tmpMap.put(
				(String) this.resources.getText(R.string.hard_setting),
				Integer.valueOf(3));
		
		boardSizeFreeTargetsTable.put(
				(String) this.resources.getText(R.string.eight_by_eight),
				tmpMap);
		
		//9x9
		tmpMap = new HashMap<String, Integer>();
		tmpMap.put(
				(String) this.resources.getText(R.string.easy_setting),
				Integer.valueOf(6));
		tmpMap.put(
				(String) this.resources.getText(R.string.medium_setting),
				Integer.valueOf(5));
		tmpMap.put(
				(String) this.resources.getText(R.string.hard_setting),
				Integer.valueOf(4));
		
		boardSizeFreeTargetsTable.put(
				(String) this.resources.getText(R.string.nine_by_nine),
				tmpMap);
		
		// 10x10
		tmpMap = new HashMap<String, Integer>();
		tmpMap.put(
				(String) this.resources.getText(R.string.easy_setting),
				Integer.valueOf(9));
		tmpMap.put(
				(String) this.resources.getText(R.string.medium_setting),
				Integer.valueOf(8));
		tmpMap.put(
				(String) this.resources.getText(R.string.hard_setting),
				Integer.valueOf(7));
		
		boardSizeFreeTargetsTable.put(
				(String) this.resources.getText(R.string.ten_by_ten),
				tmpMap);
		
		//----------------------------------------------------------------------
		// Set the number of empty tiles based on board size and difficulty.
		boardSizeFreeTilesTable = new HashMap<String, Map<String,Integer>>();
		
		// 4x4
		tmpMap = new HashMap<String, Integer>();
		tmpMap.put(
				(String) this.resources.getText(R.string.easy_setting),
				Integer.valueOf(4));
		tmpMap.put(
				(String) this.resources.getText(R.string.medium_setting),
				Integer.valueOf(3));
		tmpMap.put(
				(String) this.resources.getText(R.string.hard_setting),
				Integer.valueOf(2));
		
		boardSizeFreeTilesTable.put(
				(String) this.resources.getText(R.string.four_by_four),
				tmpMap);
		
		// 5x5
		tmpMap = new HashMap<String, Integer>();
		tmpMap.put(
				(String) this.resources.getText(R.string.easy_setting),
				Integer.valueOf(6));
		tmpMap.put(
				(String) this.resources.getText(R.string.medium_setting),
				Integer.valueOf(5));
		tmpMap.put(
				(String) this.resources.getText(R.string.hard_setting),
				Integer.valueOf(4));
		
		boardSizeFreeTilesTable.put(
				(String) this.resources.getText(R.string.five_by_five),
				tmpMap);
		
		// 6x6
		tmpMap = new HashMap<String, Integer>();
		tmpMap.put(
				(String) this.resources.getText(R.string.easy_setting),
				Integer.valueOf(8));
		tmpMap.put(
				(String) this.resources.getText(R.string.medium_setting),
				Integer.valueOf(6));
		tmpMap.put(
				(String) this.resources.getText(R.string.hard_setting),
				Integer.valueOf(4));
		
		boardSizeFreeTilesTable.put(
				(String) this.resources.getText(R.string.six_by_six),
				tmpMap);
		
		// 7x7
		tmpMap = new HashMap<String, Integer>();
		tmpMap.put(
				(String) this.resources.getText(R.string.easy_setting),
				Integer.valueOf(11));
		tmpMap.put(
				(String) this.resources.getText(R.string.medium_setting),
				Integer.valueOf(8));
		tmpMap.put(
				(String) this.resources.getText(R.string.hard_setting),
				Integer.valueOf(6));
		
		boardSizeFreeTilesTable.put(
				(String) this.resources.getText(R.string.seven_by_seven),
				tmpMap);
		
		// 8x8
		tmpMap = new HashMap<String, Integer>();
		tmpMap.put(
				(String) this.resources.getText(R.string.easy_setting),
				Integer.valueOf(12));
		tmpMap.put(
				(String) this.resources.getText(R.string.medium_setting),
				Integer.valueOf(9));
		tmpMap.put(
				(String) this.resources.getText(R.string.hard_setting),
				Integer.valueOf(6));
		
		boardSizeFreeTilesTable.put(
				(String) this.resources.getText(R.string.eight_by_eight),
				tmpMap);
		
		//9x9
		tmpMap = new HashMap<String, Integer>();
		tmpMap.put(
				(String) this.resources.getText(R.string.easy_setting),
				Integer.valueOf(15));
		tmpMap.put(
				(String) this.resources.getText(R.string.medium_setting),
				Integer.valueOf(13));
		tmpMap.put(
				(String) this.resources.getText(R.string.hard_setting),
				Integer.valueOf(11));
		
		boardSizeFreeTilesTable.put(
				(String) this.resources.getText(R.string.nine_by_nine),
				tmpMap);
		
		// 10x10
		tmpMap = new HashMap<String, Integer>();
		tmpMap.put(
				(String) this.resources.getText(R.string.easy_setting),
				Integer.valueOf(21));
		tmpMap.put(
				(String) this.resources.getText(R.string.medium_setting),
				Integer.valueOf(18));
		tmpMap.put(
				(String) this.resources.getText(R.string.hard_setting),
				Integer.valueOf(15));
		
		boardSizeFreeTilesTable.put(
				(String) this.resources.getText(R.string.ten_by_ten),
				tmpMap);

	}
	
	//--------------------------------------------------------------------------
	// Thread loop
	//--------------------------------------------------------------------------
	
	/**
	 * Game loop.
	 */
	public void run() {
		
		// Don't start if there is no board.
		if (board == null) {
			notifyListeners(GameManagerListener.GM_GAME_OVER);
			return;
		}
		
		// Don't start if the game is already in progress.
		if (gameInProgress) {
			notifyListeners(GameManagerListener.GM_IN_PROGRESS);
			return;
		}
		
		gameInProgress = true;
		
		while (!board.isGameOver() && gameInProgress) {
		
			try {
				this.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			synchronized (this) {

				while(pause) {
					try {
						this.wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		
		if (board.isGameOver() && gameInProgress) {
			// Notify all listeners that the game was won.
			notifyListeners(GameManagerListener.GM_GAME_WON);
		} else {
			notifyListeners(GameManagerListener.GM_GAME_OVER);
		}
		
		gameInProgress = false;
	}
}
