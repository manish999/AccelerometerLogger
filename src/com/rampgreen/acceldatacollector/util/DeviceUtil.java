package com.rampgreen.acceldatacollector.util;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.net.MailTo;
import android.os.Build;
import android.util.DisplayMetrics;
import android.widget.Toast;

/**
 * @author Manish Pathak
 *
 */
public class DeviceUtil {

	public static final String PREFERENCE_APP_KEY = "in.rampgreen.simpliti.APP_PREFERENCE_PRIVATE";
	public static final String PREFERENCE_VERSION_KEY = "in.rampgreen.simpliti.APP_VERSION";
	public static boolean setVersion(Context ctx){
		long v = getVersion(ctx);
		boolean out = false;
		if(v>0){
			SharedPreferences prf = getPreferences(ctx);
			out = prf.edit().putLong(PREFERENCE_VERSION_KEY, v).commit();
		}
		return out;
	};

	public static long getVersion(Context ctx){
		long v = 0L;
		PackageInfo pInfo;
		try {
			pInfo 	= getPackageInfo(ctx);
			v		= pInfo.versionCode;
		} catch (NameNotFoundException e) {
			AppLog.printStackTrace(e);
		}
		return v;
	}

	public static long getVersionInstalled(Context ctx){
		SharedPreferences prf = getPreferences(ctx);
		return prf.getLong(PREFERENCE_VERSION_KEY, 0L);
	}

	public static SharedPreferences getPreferences(Context ctx){
		return ctx.getApplicationContext().getSharedPreferences(PREFERENCE_APP_KEY,
				Context.MODE_PRIVATE);
	}

	public static PackageInfo getPackageInfo(Context ctx) throws NameNotFoundException{
		return ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), PackageManager.GET_META_DATA);
	}

	public static void deviceDensity(Context context) {
		int density= context.getResources().getDisplayMetrics().densityDpi;

		switch(density)
		{
		case DisplayMetrics.DENSITY_LOW:
			Toast.makeText(context, "LDPI", Toast.LENGTH_SHORT).show();
			break;
		case DisplayMetrics.DENSITY_MEDIUM:
			Toast.makeText(context, "MDPI", Toast.LENGTH_SHORT).show();
			break;
		case DisplayMetrics.DENSITY_HIGH:
			Toast.makeText(context, "HDPI", Toast.LENGTH_SHORT).show();
			break;
		case DisplayMetrics.DENSITY_XHIGH:
			Toast.makeText(context, "XHDPI", Toast.LENGTH_SHORT).show();
			break;
		}
	}

	public static void deviceScreenResolution(Context context) {
		int screenSize = context.getResources().getConfiguration().screenLayout &
				Configuration.SCREENLAYOUT_SIZE_MASK;

		switch(screenSize) {
		case Configuration.SCREENLAYOUT_SIZE_LARGE:
			Toast.makeText(context, "Large screen",Toast.LENGTH_LONG).show();
			break;
		case Configuration.SCREENLAYOUT_SIZE_NORMAL:
			Toast.makeText(context, "Normal screen",Toast.LENGTH_LONG).show();
			break;
		case Configuration.SCREENLAYOUT_SIZE_SMALL:
			Toast.makeText(context, "Small screen",Toast.LENGTH_LONG).show();
			break;
		default:
			Toast.makeText(context, "Screen size is neither large, normal or small" , Toast.LENGTH_LONG).show();
		}
	}

	public static void openFeedbackChooser (Context context, final String url) {
		if (url.startsWith("mailto:")) {
			MailTo mt = MailTo.parse(url);
			Intent emailIntent = new Intent(
					android.content.Intent.ACTION_SEND);
			emailIntent.setType("message/rfc822");// it will open only
			emailIntent.putExtra(Intent.EXTRA_EMAIL,
					new String[] { mt.getTo() });
			emailIntent.putExtra(Intent.EXTRA_SUBJECT,
					"Simpliti Analytics Feedback/Support");
			context.startActivity(Intent.createChooser(emailIntent,
					"Simpliti Analytics Support"));
		}
	}

	public static class CompatibilityManager {
		public static final String KINDLE_FIRE_MODEL = "Kindle Fire";

		/**
		 * Get the current Android API level.
		 */
		public static int getSdkVersion() {
			return android.os.Build.VERSION.SDK_INT;
		}

		/**
		 * Determine if the device is running API level 8 or higher.
		 */
		public static boolean isFroyo() {
			return getSdkVersion() >= Build.VERSION_CODES.FROYO;
		}

		/**
		 * Determine if the device is running API level 11 or higher.
		 */
		public static boolean isHoneycomb() {
			return getSdkVersion() >= Build.VERSION_CODES.HONEYCOMB;
		}

		/**
		 * Determine if the device is running API level 14 or higher.
		 */
		public static boolean isIceCreamSandwich() {
			return getSdkVersion() >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
		}

		/**
		 * Determine if the current device is a first generation Kindle Fire.
		 * @return true if the device model is equal to "Kindle Fire", false if otherwise.
		 */
		public static boolean isKindleFire() {
			return Build.MODEL.equals(KINDLE_FIRE_MODEL);
		}
	}
}
