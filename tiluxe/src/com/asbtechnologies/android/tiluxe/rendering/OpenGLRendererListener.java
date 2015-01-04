/*****************************************************************************
 *                        Copyright ASB Technologies (c) 2011
 *                               Java Source
 *
 * This source is the property of ASB Technologies. Any duplication or reuse
 * without the consent of ASB Technologies is prohibited.
 *
 ****************************************************************************/

package com.asbtechnologies.android.tiluxe.rendering;

/**
 * Listener for changes in the state of the OpenGLRenderer. Notifies listeners
 * with renderer state messages such as ready and closed.
 *
 * @author Ben Yarger
 * @version $Revision: 1.3 $
 */
public interface OpenGLRendererListener {
	
	//--------------------------------------------------------------------------
	// Renderer listener messages
	//--------------------------------------------------------------------------
	
	/** Notification that the renderer is ready to render the scene. */
	public static final int RENDERER_READY = 100;
	
	/** Notification that the renderer has shut down correctly. */
	public static final int RENDERER_CLOSED = 101;
	
	//--------------------------------------------------------------------------
	// Method definitions
	//--------------------------------------------------------------------------

	/**
	 * Gets the messages issued by the renderer.
	 * 
	 * @param message Message value defined in OpenGLRendererListener.
	 */
	public void openGLRendererNotification(int message);
}
