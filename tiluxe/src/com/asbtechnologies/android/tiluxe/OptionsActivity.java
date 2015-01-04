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
import android.view.View;

// Internal Imports

/**
 * Handles the options menu functionality and display.
 *
 * @author Ben Yarger
 * @version $Revision: 1.1 $
 */
public class OptionsActivity extends Activity {

	/** Settings button view. */
	private View settingsButton;
	
	/** Tutorial button view. */
	private View tutorialButton;
	
	/** About button view. */
	private View aboutButton;

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.options);
		
		settingsButton = findViewById(R.id.settingsButton);
		tutorialButton = findViewById(R.id.tutorialButton);
		aboutButton = findViewById(R.id.aboutButton);
		
		if (settingsButton != null) {
			settingsButton.setOnClickListener(settingsButtonListener);
		}
		
		if (tutorialButton != null) {
			tutorialButton.setOnClickListener(tutorialButtonListener);
		}
		
		if (aboutButton != null) {
			aboutButton.setOnClickListener(aboutButtonListener);
		}
	}
	
	//--------------------------------------------------------------------------
    // Click listeners for settings, tutorial and about
    //--------------------------------------------------------------------------
    
    /**
     * Start button listener, displays preferences view.
     */
    private View.OnClickListener settingsButtonListener = 
    	new View.OnClickListener() {
        
    	@Override
    	public void onClick(View v) {
    		
    		Intent i = new Intent(
            		getBaseContext(), 
            		SetPreferencesActivity.class);
            
            startActivity(i);
        }
    };
    
    /**
     * Tutorial button listener, displays tutorial Activity.
     */
    private View.OnClickListener tutorialButtonListener = 
    	new View.OnClickListener() {
    	
    		@Override
	        public void onClick(View v) {
	        	
	            Intent i = new Intent(
	            		getBaseContext(), 
	            		TutorialActivity.class);
	            
	            startActivity(i);
	        }
    };

    /**
     * About button listener, displays about Activity.
     */
    private View.OnClickListener aboutButtonListener = 
    	new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {

				Intent i = new Intent(
	            		getBaseContext(), 
	            		AboutActivity.class);
	            
	            startActivity(i);
			}
		};
}
