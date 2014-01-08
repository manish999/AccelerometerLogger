package com.rampgreen.acceldatacollector;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RequestQueue.RequestFilter;

/**
 * Wrapper of the Requestqueue {@link RequestQueue} to add the loading bar functionality on fetching the network data.   
 * 
 * @author Manish Pathak
 *
 */
public class MyRequestQueue {
	private RequestQueue requestQueue;



	public MyRequestQueue() {
		requestQueue = MyVolley.getVollyRequestQueue();
	}

	/**
	 * Adds a Request to the dispatch queue.
	 * @param request The request to service
	 * @return The passed-in request
	 */
	public void add(CustomRequest customRequest){
		requestQueue.add(customRequest);
	}

	/**
	 * Cancels all requests in this queue with the given tag. Tag must be non-null
	 * and equality is by identity.
	 */
	public void cancelAll(Object tag) {
		requestQueue.cancelAll(tag);
	}

	/**
	 * Cancels all requests in this queue for which the given filter applies.
	 * @param filter The filtering function to use
	 */
	public void cancelAll(RequestFilter filter) {
		requestQueue.cancelAll(filter);
	}

	/**
	 * Cancels all requests in this queue for all activities.
	 * 
	 */
	public void cancelAll() {
		requestQueue.cancelAll(new RequestQueue.RequestFilter() {
			@Override
			public boolean apply(Request<?> request) {
				return true;
			}
		});	
	}

	/**
	 * Starts the dispatchers in this queue.
	 */
	public void start() {
		requestQueue.start();
	}

	/**
	 * Stops the cache and network dispatchers.
	 */
	public void stop() {
		requestQueue.stop();
	}

	/**
	 * Gets a sequence number.
	 */
	public int getSequenceNumber() {
		return requestQueue.getSequenceNumber();
	}

	/**
	 * Gets the {@link Cache} instance being used.
	 */
	public Cache getCache() {
		return requestQueue.getCache();
	}
}