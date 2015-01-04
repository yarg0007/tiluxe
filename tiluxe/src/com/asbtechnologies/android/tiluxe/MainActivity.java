/*****************************************************************************
 *                        Copyright ASB Technologies (c) 2011
 *                               Java Source
 *
 * This source is the property of ASB Technologies. Any duplication or reuse
 * without the consent of ASB Technologies is prohibited.
 *
 ****************************************************************************/

package com.asbtechnologies.android.tiluxe;

// External Import
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.View;

// Internal Import

/**
 * Entry point for game. Displays the initial view for selection options or 
 * starting a game. Performs the initial resource loading operations to make 
 * game time s
 *
 * @author Ben Yarger
 * @version $Revision: 1.12 $
 */
public class MainActivity extends Activity {
	
	/** Shared preferences key to look up last installed version. */
	private static final String PREVIOUS_VERSION_KEY = "prevVer";
	
	/** The start button on the main menu. */
	private View startButton;
	
	/** The options button on the main menu. */
	private View optionsButton;
	
    /** 
     * Called when the activity is first created. Sets up the main menu buttons
     * and all related data objects. 
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        
        // Get the main screen layout.
        setContentView(R.layout.main);
        
        // Tie the main UI elements to the listeners.
        startButton = findViewById(R.id.startButton);
        optionsButton = findViewById(R.id.optionButton);
        
        if (startButton != null) {
        	startButton.setOnClickListener(startButtonListener);
        }
        
        if (optionsButton != null) {
        	optionsButton.setOnClickListener(optionButtonListener);
        }
        
        // Check the version info and see if we need to display the tutorial
		// for a new install or update.
		PackageInfo pInfo;
		String versionInfo = "";
		
		try {
			pInfo = getPackageManager().getPackageInfo(
					getPackageName(), 
					PackageManager.GET_META_DATA);
			
			versionInfo = pInfo.versionName;
			
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Compare version info with stored preferences data, and show the 
		// tutorials if needed.
		// If it is just an updated version, display the update message
		SharedPreferences preferences = getSharedPreferences(
				SetPreferencesActivity.PREFERENCE_NAME, MODE_PRIVATE);
		
		String prevVersionInfo = 
			preferences.getString(PREVIOUS_VERSION_KEY, null);
		
		if (prevVersionInfo == null) {		
			
			// Display the tutorial
			Intent i = new Intent(
	        		getBaseContext(), 
	        		TutorialActivity.class);
	        
	        startActivity(i);
	        
		} else if (!prevVersionInfo.equals(versionInfo)) {
			
			// Display the update message
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
				
			builder.setMessage(R.string.update_message);
			builder.setCancelable(false);
			builder.setNeutralButton(R.string.ok_button, 
					new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					;
				}
			});
				
			AlertDialog alert = builder.create();
			
			
			if (alert != null) {
				alert.show();
			}
		}
		
		// Update the property
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(PREVIOUS_VERSION_KEY, versionInfo);
		editor.commit();
        
    }
    
    //--------------------------------------------------------------------------
    // Click listeners for start and option buttons
    //--------------------------------------------------------------------------
    
    /**
     * Start button listener for clicks.
     */
    private View.OnClickListener startButtonListener = new View.OnClickListener() {
        
    	public void onClick(View v) {
    		
        	Intent i = new Intent(
            		getBaseContext(), 
            		GameActivity.class);
            
            startActivity(i);
        }
    };
    
    private View.OnClickListener optionButtonListener = 
    	new View.OnClickListener() {
    	
	        public void onClick(View v) {
	        	
	            Intent i = new Intent(
	            		getBaseContext(), 
	            		OptionsActivity.class);
	            
	            startActivity(i);
	        }
    };
}