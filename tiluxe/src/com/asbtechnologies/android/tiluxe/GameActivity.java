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
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

// Internal Imports
import com.asbtechnologies.android.tiluxe.manager.GameManager;
import com.asbtechnologies.android.tiluxe.manager.GameManagerListener;

/**
 * Tiluxe Game Activity that adds the OpenGLSurfaceView and then starts the 
 * game manager that gets the game underway.
 *
 * @author Ben Yarger
 * @version $Revision: 1.8 $
 */
public class GameActivity extends Activity implements GameManagerListener{
	
	/** Key for passing the play again flag in the bundle. */
	public static final String PLAY_AGAIN_KEY = "playAgainKey";
	
	/** Bundle key for passing the bundle between activities. */
	public static final String BUNDLE_KEY = "bundleKey";
	
	/** Game manager that keeps the game moving along. */
	private GameManager gameManager;
	
	/** 
	 * Internal reference code for identifying input results from win activity. 
	 */
	private static final int WIN_MENU_CODE = 1;
	
	/** ID for new game menu option prompt to confirm action. */
	private static final int NEW_GAME_PROMPT = 2;
	
	/** ID for reset game menu option prompt to confirm action. */
	private static final int RESET_GAME_PROMPT = 3;
	
	/** ID for option menu prompt to confirm action. */
	private static final int OPTION_MENU_PROMPT = 4;
	
	/** Confirms if a game has been terminated. */
	private boolean gameTerminated = true;
	
	/** Flags when a new game has been requested. */
	private boolean newGameRequested = false;
	
	/** 
	 * Side pockets the previous difficulty setting to see if any changes
	 * have been made in the middle of a game.
	 */
	private String previousDifficulty;
	
	/**
	 * Side pockets the previous difficulty setting to see if any changes
	 * have been made in the middle of a game.
	 */
	private String previousBoardSize ;
	
	/** Map of board size to text data. */
	private Map<String, String> boardSizeTextTable;
	
	/**
	 * Default constructor.
	 */
	public GameActivity() {
		
	}
	
	//--------------------------------------------------------------------------
	// Activity methods we wanted control over.
	//--------------------------------------------------------------------------

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		
		if (boardSizeTextTable == null) {
			
			// Set the board size dimension table.
			boardSizeTextTable = new HashMap<String, String>();
			
			boardSizeTextTable.put(
					(String) getResources().getText(R.string.four_by_four), 
					(String) getResources().getText(
							R.string.four_by_four_notes));
			
			boardSizeTextTable.put(
					(String) getResources().getText(R.string.five_by_five), 
					(String) getResources().getText(
							R.string.five_by_five_notes));
			
			boardSizeTextTable.put(
					(String) getResources().getText(R.string.six_by_six), 
					(String) getResources().getText(
							R.string.six_by_six_notes));
			
			boardSizeTextTable.put(
					(String) getResources().getText(R.string.seven_by_seven), 
					(String) getResources().getText(
							R.string.seven_by_seven_notes));
			
			boardSizeTextTable.put(
					(String) getResources().getText(R.string.eight_by_eight), 
					(String) getResources().getText(
							R.string.eight_by_eight_notes));
			
			boardSizeTextTable.put(
					(String) getResources().getText(R.string.nine_by_nine), 
					(String) getResources().getText(
							R.string.nine_by_nine_notes));
			
			boardSizeTextTable.put(
					(String) getResources().getText(R.string.ten_by_ten), 
					(String) getResources().getText(
							R.string.ten_by_ten_notes));
		}
		
		playNewGame();
	
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
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		
		// OPTION_MENU_PROMPT
		//	- If the option menu values have changed, generate a new board.
		// DATA == NULL
		//	- If data comes back null, then finish the game.
		//	- Result of win menu's menu button being pressed.
		// WIN_MENU_CODE
		// 	- If the choice is to play again, generate a new board.
		
		if (requestCode == OPTION_MENU_PROMPT) {
				
			String[] options = sampleCurrentGameOptions();
			
			if (previousDifficulty != options[0] ||
					previousBoardSize != options[1]) {
				
				playNewGame();
			}
			
		} else if (data == null) {
			
			finish();
			
		} else if (requestCode == WIN_MENU_CODE) {
			
	        boolean playAgain = 
	        	data.getBundleExtra(BUNDLE_KEY).getBoolean(PLAY_AGAIN_KEY);
	        
	        if (playAgain) {
	        	playNewGame();
	        } else {
	        	finish();
	        }
		}
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreateOptionsMenu(android.content.menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.quick_menu, menu);
	    return true;
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		AlertDialog alert = null;
		
		switch(item.getItemId()) {
		
		case R.id.qm_reset_game:
			
			builder.setMessage(R.string.qm_reset_game_message);
			builder.setCancelable(false);
			builder.setPositiveButton(R.string.yes_button, 
					new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					GameActivity.this.resetGame();
				}
			});
			builder.setNegativeButton(R.string.no_button, 
					new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			});
			
			alert = builder.create();
			break;
			
		case R.id.qm_new_game:
			
			builder.setMessage(R.string.qm_new_game_message);
			builder.setCancelable(false);
			builder.setPositiveButton(R.string.yes_button, 
					new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					GameActivity.this.playNewGame();
				}
			});
			builder.setNegativeButton(R.string.no_button, 
					new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			});
			
			alert = builder.create();
			break;
			
		case R.id.qm_options:
			
			builder.setMessage(R.string.qm_options_message);
			builder.setCancelable(false);
			builder.setPositiveButton(R.string.yes_button, 
					new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					GameActivity.this.changeOptions();
				}
			});
			builder.setNegativeButton(R.string.no_button, 
					new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			});
			
			alert = builder.create();
			break;
		}
		
		if (alert != null) {
			alert.show();
			return true;
		}
		
		return false;
		
	}

	//--------------------------------------------------------------------------
	// Methods required by GameManagerListener - register with GameManager.
	//--------------------------------------------------------------------------
	
	@Override
	public void gameManagerNotification(int message) {
		
		switch(message) {
		
		case GameManagerListener.GM_READY:
			gameTerminated = false;
			gameManager.playNewGame();
			break;
		
		case GameManagerListener.GM_GAME_OVER:
			gameTerminated = true;
			
			if (newGameRequested) {
				gameManager.playNewGame();
				newGameRequested = false;
			}
			break;
			
		case GameManagerListener.GM_GAME_WON:
			
			gameTerminated = true;
			
			onDestroy();
			
			Intent i = new Intent(
            		getBaseContext(), 
            		WinActivity.class);
            
			Bundle bundle = new Bundle();
			bundle.putBoolean(PLAY_AGAIN_KEY, true);
			i.putExtra(BUNDLE_KEY, bundle);
			
			startActivityForResult(i, WIN_MENU_CODE);
            
			break;
			
		case GameManagerListener.GM_PAUSED:
			break;
			
		case GameManagerListener.GM_RESTARTED:
			break;
			
		case GameManagerListener.GM_STARTED:
			break;
		case GameManagerListener.GM_IN_PROGRESS:
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
	private void playNewGame() {
		
		if (gameManager != null) {
			gameManager.quitGame();
			newGameRequested = true;
		}
		
		// Create a new GameManager and pass in this activity as the context.
		gameManager = new GameManager(this);
		
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
		
		// Set the text.
		SharedPreferences preferences = getSharedPreferences(
				SetPreferencesActivity.PREFERENCE_NAME, 
				Context.MODE_PRIVATE);
		
		String boardSize = preferences.getString(
				(String) getResources().getText(R.string.board_size_key), 
				(String) getResources().getText(R.string.six_by_six));
		
		TextView glText = (TextView) findViewById(R.id.glText);
		glText.setText(boardSizeTextTable.get(boardSize));
		
		
		// Register the activity with the game manager and wait for notification
		// that it is ready to play the game.
		gameManager.registerListener(this);
	}

	/**
	 * Reset the board to its starting state.
	 */
	private void resetGame() {
		
		gameManager.resetGame();
	}
	
	/**
	 * Change the menu options mid-game. Starts the option menu activity and
	 * expects a result. At that time, the game will be restarted with new
	 * settings if the settings have changed. If they have not changed, then
	 * the current game will be resumed.
	 */
	private void changeOptions() {
		
		String[] currentOptions = sampleCurrentGameOptions();
		
		previousDifficulty = currentOptions[0];
		previousBoardSize = currentOptions[1];
		
		Intent i = new Intent(
        		getBaseContext(), 
        		SetPreferencesActivity.class);
        
        startActivityForResult(i, OPTION_MENU_PROMPT);

	}
	
	/**
	 * Sample the current game options to look for changes against when 
	 * the user changes options in the middle of a game.
	 * 
	 * @return String array index 0 = previousDifficulty, 1 = previousBoardSize
	 */
	private String[] sampleCurrentGameOptions() {
	
		SharedPreferences preferences = getBaseContext().getSharedPreferences(
				SetPreferencesActivity.PREFERENCE_NAME, 
				Context.MODE_PRIVATE);
		
		Resources resources = getBaseContext().getResources();
		
		String[] results = new String[2];
	
		// Get the level of difficulty to build into the board.
		results[0] = preferences.getString(
			(String) resources.getText(R.string.difficulty_key), 
			(String) resources.getText(R.string.easy_setting));
	
		// Get the board size.
		results[1] = preferences.getString(
			(String) resources.getText(R.string.board_size_key), 
			(String) resources.getText(R.string.six_by_six));
		
		return results;
	}

}
