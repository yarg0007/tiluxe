/*****************************************************************************
 *                        Copyright ASB Technologies (c) 2011
 *                               Java Source
 *
 * This source is the property of ASB Technologies. Any duplication or reuse
 * without the consent of ASB Technologies is prohibited.
 *
 ****************************************************************************/
package com.asbtechnologies.android.tiluxe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Win screen that is displayed when a game is completed. Gives the option to
 * play again or go back to the main menu.
 *
 * @author Ben Yarger
 * @version $Revision: 1.1 $
 */
public class WinActivity extends Activity {
	
	/** The play again button on the win screen. */
	private View playAgainButton;
	
	/** The main menu button on the win screen. */
	private View mainMenuButton;

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		// Set the win screen layout
		setContentView(R.layout.win);
		
        // Tie the main UI elements to the listeners.
        playAgainButton = findViewById(R.id.playAgainButton);
        mainMenuButton = findViewById(R.id.mainMenuButton);
        
        if (playAgainButton != null) {
        	playAgainButton.setOnClickListener(playAgainButtonListener);
        }
        
        if (mainMenuButton != null) {
        	mainMenuButton.setOnClickListener(mainMenuButtonListener);
        }
	}

	//--------------------------------------------------------------------------
    // Click listeners for start and option buttons
    //--------------------------------------------------------------------------
    
    /**
     * Play again button listener for clicks.
     */
    private View.OnClickListener playAgainButtonListener = 
    	new View.OnClickListener() {
        
    	public void onClick(View v) {

    		Bundle bundle = new Bundle();
    		bundle.putBoolean(GameActivity.PLAY_AGAIN_KEY, true);
            
        	Intent intent = new Intent();
        	intent.putExtra(GameActivity.BUNDLE_KEY, bundle);
        	setResult(0, intent);
        	
            finish();
        }
    };
    
    /**
     * Main menu button listener for clicks.
     */
    private View.OnClickListener mainMenuButtonListener = 
    	new View.OnClickListener() {
    	
	        public void onClick(View v) {
	        	
	        	Bundle bundle = new Bundle();
	    		bundle.putBoolean(GameActivity.PLAY_AGAIN_KEY, false);
	            
	        	Intent intent = new Intent();
	        	intent.putExtra(GameActivity.BUNDLE_KEY, bundle);
	        	setResult(0, intent);
	        	
	            finish();
	        }
    };
}
