/*****************************************************************************
 *                        Copyright ASB Technologies (c) 2011
 *                               Java Source
 *
 * This source is the property of ASB Technologies. Any duplication or reuse
 * without the consent of ASB Technologies is prohibited.
 *
 ****************************************************************************/
package com.asbtechnologies.android.tiluxe.util;

// External Imports
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.HashMap;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

// Internal Imports

/**
 * Loads Android drawable resources as textures. Based on example from :
 * 
 * http://tkcodesharing.blogspot.com/2008/05/working-with-textures-in-androids.html
 *
 * @author Ben Yarger
 * @version $Revision: 1.1 $
 */
public class TextureLoader {
	
	/** Loaded GL textures available for use. */
	private int[] textures;
	
	/** Map of resource ids to textures array index. */
	private HashMap<Integer, Integer> textureMap;
	
	/** OpenGl graphics reference */
	private GL10 gl;
	
	/** Android application context reference */
	private Context context;
	
	/** Map of resource names to the resource id. */
	private HashMap<String, Integer> resourceIDMap;
	
	/**
	 * Default constructor.
	 */
	public TextureLoader(GL10 gl,  Context context) {
		
		this.gl = gl;
		this.context = context;
		resourceIDMap = new HashMap<String, Integer>();
	}
	
	/**
	 * Dispose of the TextureLoader. Called to clean up TextureLoading objects.
	 */
	public void dispose() {
		
		textures = null;
		textureMap = null;
		gl = null;
		context = null;
		resourceIDMap = null;
	}
	
	//--------------------------------------------------------------------------
	// Public methods
	//--------------------------------------------------------------------------
	
	/** 
	 * Add drawable resource id's of the textures to be loaded. Add all of the
	 * resources to be loaded as textures and then call loadTextures().
	 * 
	 * @param drawableResourceName Name of drawable resource
	 * @param drawableResource ID of the drawable resource
	 */
	public void addTexture(
			String drawableResourceName, int drawableResourceID) {
		
		resourceIDMap.put(drawableResourceName, drawableResourceID);

	}
	
	/** 
	 * Set the texture for the resource specified.
	 * 
	 * @param drawableName Name of the drawable resource to use as texture. 
	 */
	public void setTexture(String drawableName) {
		
		if (drawableName == null) {
			return;
		}
		
		Integer resourceID = resourceIDMap.get(drawableName);
		
		if (resourceID == null) {
			return;
		}
		
		Integer textureID = 
			textureMap.get(resourceID.intValue());
		
		if (textureID == null) {
			return;
		}
		
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[textureID.intValue()]);
	}

	/**
	 * Call this after all resources have been added using addTexture(). This
	 * will load each resource as a usable texture map. Loaded texture maps
	 * can be called up using setTexture().
	 */
	public void loadTextures() 
	{ 
		textureMap = new HashMap<Integer, Integer>();
		
		Object[] keys = resourceIDMap.keySet().toArray();
		
		// Establish the textures array according to the size of the 
		// drawableResources.
		textures = new int[keys.length];
		gl.glGenTextures(keys.length, textures, 0);
		
		// Populate the textureMap with loaded textures.
		// Also load each of the textures and establish each textures array 
		// index with the loaded texture data.
		for (int i = 0; i < keys.length; i++) {
			
			// Decode the resource, by id, from the available resources.
			Bitmap bmp = BitmapFactory.decodeResource(
					context.getResources(), resourceIDMap.get(keys[i]));
			
			ByteBuffer byteBuffer = extract(bmp);
			
			int texture = textures[i];
			int width = bmp.getWidth();
			int height = bmp.getHeight();
			
			gl.glBindTexture(
					GL10.GL_TEXTURE_2D, 
					texture);
			
			gl.glTexImage2D(
					GL10.GL_TEXTURE_2D, 
					0, 
					GL10.GL_RGBA, 
					width, 
					height, 
					0, 
					GL10.GL_RGBA, 
					GL10.GL_UNSIGNED_BYTE, 
					byteBuffer);
			
			gl.glTexParameterx(
					GL10.GL_TEXTURE_2D, 
					GL10.GL_TEXTURE_MIN_FILTER, 
					GL10.GL_LINEAR);
			
			gl.glTexParameterx(
					GL10.GL_TEXTURE_2D, 
					GL10.GL_TEXTURE_MAG_FILTER, 
					GL10.GL_LINEAR);
			
			// Add the texture to the lookup map.
			textureMap.put(
					resourceIDMap.get(keys[i]), 
					new Integer(i));
		}
	} 
	
	//--------------------------------------------------------------------------
	// Private methods
	//--------------------------------------------------------------------------
	
	/**
	 * Extract the resource bitmap and return the ByteBuffer representation of 
	 * that data.
	 * 
	 * @param bmp Bitmap to extract.
	 * @return Bitmap converted to ByteBuffer.
	 */
	private static ByteBuffer extract(Bitmap bmp) 
	{ 
		ByteBuffer bb = 
			ByteBuffer.allocateDirect(bmp.getHeight() * bmp.getWidth() * 4);
		
		bb.order(ByteOrder.BIG_ENDIAN); 
		
		IntBuffer ib = bb.asIntBuffer(); 
		
		// Convert ARGB -> RGBA 
		for (int y = bmp.getHeight() - 1; y > -1; y--) 
		{ 
			for (int x = 0; x < bmp.getWidth(); x++) 
			{ 
				int pix = bmp.getPixel(x, bmp.getHeight() - y - 1); 
				int alpha = ((pix >> 24) & 0xFF); 
				int red = ((pix >> 16) & 0xFF); 
				int green = ((pix >> 8) & 0xFF); 
				int blue = ((pix) & 0xFF); 

				ib.put(red << 24 | green << 16 | blue << 8 | alpha); 
			} 
		} 
		bb.position(0); 
		return bb; 
	} 

}
