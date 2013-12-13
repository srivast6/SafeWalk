package edu.purdue.SafeWalk.bitmaps;

import java.lang.ref.WeakReference;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

public class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap>
{
	private final WeakReference<ImageView> imageViewReference; 
	public String filename = null; 
	private final float height, width;
	
	public BitmapWorkerTask(ImageView imageView, float height, float width)
	{
		imageViewReference = new WeakReference<ImageView>(imageView);
		this.height = height; 
		this.width = width; 
	}
	
	@Override
	protected Bitmap doInBackground(String... filenames)
	{
		filename = filenames[0];
		Bitmap bitmap = BitmapHelper.decodeSampledBitmapFromFile(filename, width, height);
		BitmapHelper.addBitmapToMemoryCache(String.valueOf(filename), bitmap);
		return bitmap;
	}
	
	@Override
    protected void onPostExecute(Bitmap bitmap) {
		if (isCancelled()) {
            bitmap = null;
        }

        if (imageViewReference != null) {
        	ImageView imageView = imageViewReference.get();
        	BitmapWorkerTask bitmapDownloaderTask = BitmapHelper.getBitmapWorkerTask(imageView);
            // Change bitmap only if this process is still associated with it
            if (this == bitmapDownloaderTask) {
                imageView.setImageBitmap(bitmap);
            }
        }
		
		
    }
}
