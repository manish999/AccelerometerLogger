package com.rampgreen.acceldatacollector;

import android.app.ActivityManager;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;


/**
 * Helper class that is used to provide references to initialized RequestQueue(s) and ImageLoader(s)
 * 
 * @author Manish Pathak
 * 
 */
public class MyVolley {
	private static RequestQueue mRequestQueue;
	private static MyRequestQueue mMyRequestQueue;
	private static ImageLoader mImageLoader;
	static MyRequestQueue instance;


//	private MyVolley() {
//		// no instances
//	}


	static void init(Context context) {
		mRequestQueue = Volley.newRequestQueue(context);

		if(mMyRequestQueue == null) {
			mMyRequestQueue = new MyRequestQueue();
		}

		int memClass = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE))
				.getMemoryClass();
		// Use 1/8th of the available memory for this memory cache.
		int cacheSize = 1024 * 1024 * memClass / 8;
		//        mImageLoader = new ImageLoader(mRequestQueue, new BitmapLruCache(cacheSize));
	}

	public static RequestQueue getVollyRequestQueue() {
		if (mRequestQueue != null) {
			return mRequestQueue;
		} else {
			throw new IllegalStateException("RequestQueue not initialized");
		}
	}


	public static MyRequestQueue getRequestQueue() {
		if (mMyRequestQueue != null) {
			return mMyRequestQueue;
		} else {
			throw new IllegalStateException("RequestQueue not initialized");
		}
	}


	/**
	 * Returns instance of ImageLoader initialized with {@see FakeImageCache} which effectively means
	 * that no memory caching is used. This is useful for images that you know that will be show
	 * only once.
	 * 
	 * @return
	 */
	public static ImageLoader getImageLoader() {
		if (mImageLoader != null) {
			return mImageLoader;
		} else {
			throw new IllegalStateException("ImageLoader not initialized");
		}
	}

	
}
