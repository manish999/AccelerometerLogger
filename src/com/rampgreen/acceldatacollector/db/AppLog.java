package com.rampgreen.acceldatacollector.db;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * Debug helper class , it is used for debug the application.
 * 
 * @author Manish Pathak
 *
 */
public class AppLog {
	public static final String APP_TAG = "caretaker";
	private static final boolean SHOWLOG = true;
	public static final boolean RELEASE = false;

	private AppLog() {	
	}

	public static int logString(String message){
		return SHOWLOG ? Log.d(APP_TAG,message): 0;
	}

	public static int d(String tag, String message){
		if(message != null){
			return SHOWLOG ? Log.d(tag,message): 0;
		}
		return 0;
	}

	public static int v(String message){
		if(message != null){
			return SHOWLOG ? Log.v("tag",message): 0;
		}
		return 0;
	}

	public static int e(String message){
		if(message != null){
			return SHOWLOG ? Log.e("ERROR",message): 0;
		}
		return 0;
	}

	public static void logToast(Context context, String text){
		if(SHOWLOG)
			Toast.makeText(context, text, Toast.LENGTH_LONG).show();
	}

	public static void showToast(Context context, String text){
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}

	public static void printStackTrace(Throwable e) {
		if(SHOWLOG)
			e.printStackTrace();
	}

	public static void d(String msg, Object... args) {
		if (args != null && args.length > 0) {
			msg = String.format(msg, args);
		}
		Log.d(APP_TAG, msg);
	}

	public static void e(Throwable t, String msg, Object... args) {
		if (args != null && args.length > 0) {
			msg = String.format(msg, args);
		}
		Log.e(APP_TAG, msg, t);
	}
}
