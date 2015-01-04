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
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

// Internal Imports

/**
 * Displays the tutorials which are playable games starting at a very basic 
 * level and working up to a more complicated game. Each game introduces new
 * concepts to the user and allows them to build up game knowledge and 
 * confidence while being introduced to the game.
 * 
 * @author Ben Yarger
 * @version $Revision: 1.5 $
 */
public class TutorialActivity extends Activity {
	
	/** Bundle return request code to identify result from tutorial view. */
	public static final int TUTORIAL_VIEW_DONE = 98;
	
	/** Bundle key to identify the bundle with. */
	public static final String TUTORIAL_BUNDLE = "tutorialBundle";
	
	/** Bundle flag for tutorial instructions. */
	public static final String TUTORIAL_INSTRUCTIONS = "tutorialInstructions";
	
	/** Bundle flag for tutorial board size. */
	public static final String TUTORIAL_BOARD_SIZE = "tutorialBoardSize";
	
	/** Bundle flag for tutorial target lengths. */
	public static final String TUTORIAL_TARGET_LENGTHS = "tutorialTarLengths";
	
	/** Bundle flag for tutorial free targets. */
	public static final String TUTORIAL_FREE_TARGETS = "tutorialFreeTargets";
	
	/** Bundle flag for tutorial free empty tiles. */
	public static final String TUTORIAL_FREE_EMPTY = "tutorialFreeEmpty";
	
	/** Total number of tutorials to show. */
	private static final int TOTAL_TUTORIALS = 6;
	
	/** Index ordered instructions for each tutorial played. */
	private static String[] tutorialLevelInstructions;
	
	/** Index ordered board sizes for each tutorial played. */
	private static int[] tutorialBoardSize;
	
	/** Index keyed target lengths for each tutorial played. */
	private static Map<Integer, int[]> tutorialLevelTargetLengths;
	
	/** Index ordered free targets for each tutorial played. */
	private static int[] tutorialLevelFreeTargets;
	
	/** Index ordered free empty tiles for each tutorial playes. */
	private static int[] tutorialLevelFreeEmpty;
	
	/** Tutorial index tracking which tutorial to execute. */
	private int tutorialIndex;
	
	/**
	 * Create the view.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		tutorialIndex = 0;
		
		// Initialize all of our tutorial fields.
		tutorialLevelInstructions = new String[] {
			(String) getText(R.string.tutorial_one),
			(String) getText(R.string.tutorial_two),
			(String) getText(R.string.tutorial_three),
			(String) getText(R.string.tutorial_four),
			(String) getText(R.string.tutorial_five),
			(String) getText(R.string.tutorial_six),
		};
		
		tutorialBoardSize = new int[] {
			1, 2, 3, 3, 4, 5
		};
		
		tutorialLevelTargetLengths = new HashMap<Integer, int[]>();
		tutorialLevelTargetLengths.put(Integer.valueOf(0), new int[] {1});
		tutorialLevelTargetLengths.put(Integer.valueOf(1), new int[] {1});
		tutorialLevelTargetLengths.put(Integer.valueOf(2), new int[] {1, 1});
		tutorialLevelTargetLengths.put(Integer.valueOf(3), new int[] {2, 1});
		tutorialLevelTargetLengths.put(Integer.valueOf(4), new int[] {2, 1, 1});
		tutorialLevelTargetLengths.put(Integer.valueOf(5), new int[] {3, 2, 1});
		
		tutorialLevelFreeTargets = new int[] {
			0, 0, 0, 0, 1, 2
		};
		
		tutorialLevelFreeEmpty = new int[] {
			0, 0, 0, 0, 2, 3	
		};
		
		// Kick off the tutorial setup and then execution.
		nextTutorial();
		
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onPause()
	 */
	@Override
	protected void onPause() {
		super.onPause();
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		
		if (data == null) {
			
			finish();
			
		} else if (requestCode == TUTORIAL_VIEW_DONE) {
			
	        nextTutorial();
		}
		

	}
	
	//--------------------------------------------------------------------------
	// Private methods
	//--------------------------------------------------------------------------
	
	/**
	 * Put up the next tutorial game.
	 */
	private void nextTutorial() {
		
		// If we have more tutorials to display, then build another game
		// view and display the new tutorial. Otherwise, finish.
		if (tutorialIndex < TOTAL_TUTORIALS) {
			
			// Display the tutorial
			Intent i = new Intent(
	        		getBaseContext(), 
	        		TutorialDisplayActivity.class);
			
			Bundle bundle = new Bundle();
			bundle.putString(
					TutorialActivity.TUTORIAL_INSTRUCTIONS, 
					tutorialLevelInstructions[tutorialIndex]);	
			
			bundle.putInt(
					TutorialActivity.TUTORIAL_BOARD_SIZE, 
					tutorialBoardSize[tutorialIndex]);
			
			bundle.putIntArray(
					TutorialActivity.TUTORIAL_TARGET_LENGTHS, 
					tutorialLevelTargetLengths.get(
							Integer.valueOf(tutorialIndex)));
			
			bundle.putInt(
					TutorialActivity.TUTORIAL_FREE_TARGETS, 
					tutorialLevelFreeTargets[tutorialIndex]);
			
			bundle.putInt(
					TutorialActivity.TUTORIAL_FREE_EMPTY, 
					tutorialLevelFreeEmpty[tutorialIndex]);

			i.putExtra(TutorialActivity.TUTORIAL_BUNDLE, bundle);
			
			tutorialIndex++;

			startActivityForResult(i, TUTORIAL_VIEW_DONE);
			
		} else {
			finish();
		}
	}

}
