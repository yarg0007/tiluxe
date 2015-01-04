/*****************************************************************************
 *                        Copyright ASB Technologies (c) 2011
 *                               Java Source
 *
 * This source is the property of ASB Technologies. Any duplication or reuse
 * without the consent of ASB Technologies is prohibited.
 *
 ****************************************************************************/

package com.asbtechnologies.android.tiluxe.test;

// External Imports
import com.asbtechnologies.android.tiluxe.MainActivity;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.TextView;

// Internal Imports

/**
 * Test class for MainActivity to test application launching, closing, pausing
 * etc.
 * 
 * @author Ben Yarger
 * @version $Revision: 1.1 $
 */
public class TiluxeStartupTest extends 
	ActivityInstrumentationTestCase2<MainActivity> {
	
	private MainActivity mActivity;
	private TextView mView;
	private String resourceString;
	

	public TiluxeStartupTest() {
		super("com.asbtechnologies.android.tiluxe", MainActivity.class);
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		mActivity = this.getActivity();
		mView = (TextView) mActivity.findViewById(com.asbtechnologies.android.tiluxe.R.id.textview);
		resourceString = mActivity.getString(com.asbtechnologies.android.tiluxe.R.string.hello);
	}
	
	public void testPreconditions() {
		assertNotNull(mView);
	}
	
	public void testText() {
		assertEquals(resourceString, (String)mView.getText());
	}
}
