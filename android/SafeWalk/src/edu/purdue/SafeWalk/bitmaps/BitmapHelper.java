package edu.purdue.SafeWalk.bitmaps;

import java.lang.Math;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.widget.ImageView;

/**
 * This class was created to help fix the crashes caused by bitmaps being loaded
 * and overflowing the memory. It contains helper methods for dealing with
 * bitmaps.
 * 
 * @author David Tschida (dtschida)
 * @date Sep 8, 2013 7:29:10 PM
 * 
 */
public final class BitmapHelper
{
	
	private static String TAG = "BitmapHelper";
	private static LruCache<String, Bitmap> mMemoryCache;
	
	/**
	 * This method is used to create a sample size for a bitmap given the
	 * required size and the Options class for the bitmap.
	 * 
	 * Run this method after first running
	 * 
	 * <pre>
	 * <code>
	 * final BitmapFactory.Options foo = new BitmapFactory.Options();
	 * foo.inJustDecodeBounds = true; 
	 * BitmapFactory.decodeResource(Resources, int, foo);
	 * </code>
	 * </pre>
	 * 
	 * Then set the output to <code>foo.inSampleSize</code> and then decode the
	 * image.
	 * 
	 * (If using the same BitmapFactory, remember to change
	 * <code>inJustDecodeBounds</code> back to false.)
	 * 
	 * This method was taken from the Developer tutorial on the android website
	 * (licensed under Creative Commons) The original source can be found here:
	 * {@link http
	 * ://developer.android.com/training/displaying-bitmaps/load-bitmap.html}
	 * 
	 * @param options
	 *            A bitmap options class created with
	 * @param reqWidth
	 *            The preferred width of the image.
	 * @param reqHeight
	 *            The preferred height of the image.
	 * @return The sample size (to be used set to options.inSampleSize)
	 */
	public static int calculateInSampleSize(BitmapFactory.Options options,
			float reqWidth, float reqHeight)
	{
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		
		if (height > reqHeight || width > reqWidth)
		{
			// Calculate ratios of height and width to requested height and
			// width
			final int heightRatio = Math.round(height
					/ reqHeight);
			final int widthRatio = Math.round(width / reqWidth);
			
			// Choose the smallest ratio as inSampleSize value, this will
			// guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		Log.v(TAG, "inSampleSize = " + inSampleSize);
		return inSampleSize;
	}
	
	/**
	 * Allows decoding of a bitmap with a specified height and width.
	 * 
	 * Modified from the Android developer tutorial. Original source can be
	 * found at {@link http
	 * ://developer.android.com/training/displaying-bitmaps/load-bitmap.html}
	 * 
	 * @param filename
	 *            The filename of the file to be decoded.
	 * @param reqWidth
	 *            The preferred width of the output bitmap.
	 * @param reqHeight
	 *            The preferred height of the output bitmap.
	 * @return the decoded bitmap, or null
	 */
	public static Bitmap decodeSampledBitmapFromFile(String filename,
			float reqWidth, float reqHeight)
	{
		Log.v(TAG, "Recieved " + filename + " with (w,h): (" + reqWidth + ", "
				+ reqHeight + ").");
		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filename, options);
		
		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);
		
		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		Bitmap decodedBitmap = BitmapFactory.decodeFile(filename, options);
		Log.v(TAG, "The Bitmap is " + decodedBitmap.toString());
		return decodedBitmap;
	}
	
	public static void loadBitmapAsAsyncTask(String filepath,
			ImageView imageView, float width, float height)
	{
		final Bitmap bitmap = getBitmapFromMemCache(filepath);
		
		if(bitmap != null)
		{
			imageView.setImageBitmap(bitmap);
		}
		else if (cancelPotentialWork(filepath, imageView))
		{
			BitmapWorkerTask task = new BitmapWorkerTask(imageView, height,
					width);
			AsyncDrawable downloadedDrawable = new AsyncDrawable(task);
			imageView.setImageDrawable(downloadedDrawable);
			task.execute(filepath/* , cookie */);
		}
		
	}
	
	private static boolean cancelPotentialWork(String filepath,
			ImageView imageView)
	{
		BitmapWorkerTask bitmapLoaderTask = getBitmapWorkerTask(imageView);
		
		if (bitmapLoaderTask != null)
		{
			String bitmapPath = bitmapLoaderTask.filename;
			if ((bitmapPath == null) || (!bitmapPath.equals(filepath)))
			{
				bitmapLoaderTask.cancel(true);
			} else
			{
				// The same URL is already being downloaded.
				return false;
			}
		}
		return true;
	}
	
	public static BitmapWorkerTask getBitmapWorkerTask(ImageView imageView)
	{
		if (imageView != null)
		{
			Drawable drawable = imageView.getDrawable();
			if (drawable instanceof AsyncDrawable)
			{
				AsyncDrawable downloadedDrawable = (AsyncDrawable) drawable;
				return downloadedDrawable.getBitmapWorkerTask();
			}
		}
		return null;
	}
	
	public void initMemoryCache()
	{
		final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

	    // Use 1/8th of the available memory for this memory cache.
	    final int cacheSize = maxMemory / 15;

		mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
	        @Override
	        protected int sizeOf(String key, Bitmap bitmap) {
	            // The cache size will be measured in kilobytes rather than
	            // number of items.
	            return (bitmap.getRowBytes() * bitmap.getHeight()) / 1024;
	        }
	    };
	}
	
	public static void clearCache()
	{
		mMemoryCache.evictAll();
	}
	
	public static void addBitmapToMemoryCache(String key, Bitmap bitmap) {
	    assert(mMemoryCache != null);
		if (getBitmapFromMemCache(key) == null) {
	        mMemoryCache.put(key, bitmap);
	    }
	}

	public static Bitmap getBitmapFromMemCache(String key) {
		assert(mMemoryCache != null);
		return mMemoryCache.get(key);
	}
	
}
