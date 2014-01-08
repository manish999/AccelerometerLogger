package com.rampgreen.acceldatacollector;

import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.widget.TabHost;

import com.android.volley.Response;

/**
 * Base activity class is used for future purposes. we can add functionality to all classes that will extend it. 
 * 
 * @author Manish Pathak
 *
 */
abstract public class BaseActivity extends Activity implements Response.Listener<JSONObject>, Response.ErrorListener{
	private static final String TAG_1 = "0";
	private static final String TAG_2 = "1";
	private static final String TAG_3 = "2";
	private static final String TAG_4 = "3";
	private static final String TAG_5 = "4";

	private ProgressDialog progressDialog;
	protected TabHost mTabHost;
	private static int numAsyncTasks;

	
	protected void showLoadingBar(){
		numAsyncTasks++;
		progressDialog = ProgressDialog.show(this, null, "Loading...", true);
	}

	protected void closeLoadingBar(){
		if (0 == --numAsyncTasks) {
			if(progressDialog != null && progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
		}
	}

	@Override
	public void onBackPressed()
	{
		// cancel all requests when user press back button while loading bar is shown. 
		if(progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
			MyRequestQueue queue = MyVolley.getRequestQueue();
			queue.cancelAll();
		}
		super.onBackPressed();

	}

	public void setActionBarTitle(String title) {
		setTitle(title);
	}

}
