/*****************************************************************************
 *                        Copyright ASB Technologies (c) 2011
 *                               Java Source
 *
 * This source is the property of ASB Technologies. Any duplication or reuse
 * without the consent of ASB Technologies is prohibited.
 *
 ****************************************************************************/
package com.asbtechnologies.android.tiluxe;

// External Imports
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

// Internal Imports
import com.asbtechnologies.android.tiluxe.manager.GameManager;
import com.asbtechnologies.android.tiluxe.manager.GameManagerListener;

/**
 * Displays the tutorial which is a playable game. Each game introduces new
 * concepts to the user and allows them to build up game knowledge and 
 * confidence while being introduced to the game.
 * 
 * @author Ben Yarger
 * @version $Revision: 1.1 $
 */
public class TutorialDisplayActivity extends Activity implements 
	GameManagerListener {
	
	/** Bundle flag that can be set to true to show the tutorial. */
	public static final String SHOW_TUTORIAL = "welcomeScreen";
	
	/** Bundle key to identify the bundle with. */
	public static final String TUTORIAL_BUNDLE = "tutorialBundle";
	
	/** Game manager that keeps the game moving along. */
	private GameManager gameManager;
	
	/** Text field for displaying tutorial instructions. */
	private TextView glText;
	
	/** Instructions for each tutorial played. */
	private static String tutorialLevelInstructions;
	
	/** Board sizes for each tutorial played. */
	private static int tutorialBoardSize;
	
	/** Target lengths for each tutorial played. */
	private static int[] tutorialLevelTargetLengths;
	
	/** Number of free targets for each tutorial played. */
	private static int tutorialLevelFreeTargets;
	
	/** Number of free empty tiles for each tutorial played. */
	private static int tutorialLevelFreeEmpty;
	
	/** Tutorial index tracking which tutorial to execute. */
	private int tutorialIndex;
	
	/**
	 * Create the view.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);

		// Get any bundled info.
		Bundle tmpBundle = null;
		
		if (savedInstanceState != null) {
			tmpBundle = savedInstanceState; 
		} else if (getIntent().getBundleExtra(TUTORIAL_BUNDLE) != null) {
			tmpBundle = getIntent().getBundleExtra(TUTORIAL_BUNDLE);
		} else {
			finish();
		}
		
		// Special case catch in case the ship goes down.
		if (tmpBundle == null) {
			finish();
		}
		
		tutorialLevelInstructions = 
			tmpBundle.getString(TutorialActivity.TUTORIAL_INSTRUCTIONS);	
		
		tutorialBoardSize =
			tmpBundle.getInt(TutorialActivity.TUTORIAL_BOARD_SIZE);
		
		tutorialLevelTargetLengths = 
			tmpBundle.getIntArray(TutorialActivity.TUTORIAL_TARGET_LENGTHS);
		
		tutorialLevelFreeTargets = 
			tmpBundle.getInt(TutorialActivity.TUTORIAL_FREE_TARGETS);
		
		tutorialLevelFreeEmpty = 
			tmpBundle.getInt(TutorialActivity.TUTORIAL_FREE_EMPTY);
		
		tmpBundle = null;

		// Kick off the tutorial setup and then execution.
		beginTutorial();
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		gameManager.quitGame();
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onPause()
	 */
	@Override
	protected void onPause() {
		super.onPause();
		gameManager.pauseGame();
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();
		gameManager.resumeGame();
	}
	
	//--------------------------------------------------------------------------
	// Methods required by GameManagerListener - register with GameManager.
	//--------------------------------------------------------------------------
	
	@Override
	public void gameManagerNotification(int message) {
		
		switch(message) {
		
		case GameManagerListener.GM_READY:
			
			gameManager.playNewGame();
			break;
		
		case GameManagerListener.GM_GAME_OVER:
			break;
			
		case GameManagerListener.GM_GAME_WON:
            
			onDestroy();
			
        	Intent intent = new Intent();
        	setResult(0, intent);
        	
            finish();

			break;
			
		case GameManagerListener.GM_PAUSED:
			break;
			
		case GameManagerListener.GM_RESTARTED:
			break;
			
		case GameManagerListener.GM_STARTED:
			break;
		}
	}
	
	//--------------------------------------------------------------------------
	// Private methods
	//--------------------------------------------------------------------------
	
	/**
	 * If the game is still going, quit it and build a new board. Then wait for
	 * the ready message to play the new game.
	 */
	private void beginTutorial() {
		
		if (gameManager != null) {
			gameManager.quitGame();
//			((GameGLView)gameManager.getView()).dispose();
		}
		
		// Create a new GameManager and pass in this activity as the context.
		gameManager = new GameManager(
				this, 
				tutorialBoardSize, 
				tutorialBoardSize, 
				tutorialLevelTargetLengths, 
				tutorialLevelFreeEmpty, 
				tutorialLevelFreeTargets);

		// Get the view created by the GameManager and use that as our view.
		setContentView(gameManager.getView());
		
		// Add the text view at the bottom.
		LayoutInflater inflater = getLayoutInflater();
		View tmpView;
		tmpView = inflater.inflate(R.layout.game, null);
		addContentView(
				tmpView, 
				new ViewGroup.LayoutParams(
						ViewGroup.LayoutParams.FILL_PARENT, 
						ViewGroup.LayoutParams.FILL_PARENT));
		
		glText = (TextView) findViewById(R.id.glText);
		glText.setText(tutorialLevelInstructions);
		
		tutorialIndex++;

		// Register the activity with the game manager and wait for notification
		// that it is ready to play the game.
		gameManager.registerListener(this);
	}

}
