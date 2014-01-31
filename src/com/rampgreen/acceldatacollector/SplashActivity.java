package com.rampgreen.acceldatacollector;

import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.rampgreen.acceldatacollector.util.AppLog;
import com.rampgreen.acceldatacollector.util.AppSettings;
import com.rampgreen.acceldatacollector.util.DeviceUtil;
import com.rampgreen.acceldatacollector.util.StringUtils;

public class SplashActivity extends Activity {

	//stopping splash screen starting home activity.
	private static final int STOPSPLASH = 0;
	//time duration in millisecond for which your splash screen should visible to
	//user. here i have taken half second
	private static final long SPLASHTIME = 2000;
	String selectedMailID = "";
	String accountToken = "";
	String password = "";
	private boolean isAccountInMobile;
	private Handler splashHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case STOPSPLASH:
				Intent intent = new Intent();
				selectedMailID = (String) AppSettings.getPrefernce(SplashActivity.this, null, AppSettings.USER_SELECTED_MAIL_ID, "");
//				accountToken = (String) AppSettings.getPrefernce(SplashActivity.this, null, AppSettings., "");
				password = (String) AppSettings.getPrefernce(SplashActivity.this, null, AppSettings.USER_SELECTED_PASSWORD, "");
				//Generating and Starting new intent on splash time out	
				if(StringUtils.notEmpty (selectedMailID) && StringUtils.notEmpty(password)){
					intent.setClass(getApplicationContext(), MainActivity.class);
				} else {
					intent.setClass(getApplicationContext(), LoginActivity.class);
				}
				intent.putExtra(Constants.CALLING_ACTIVITY_TYPE, Constants.CALLING_ACTIVITY_SPLASH);
				startActivity(intent);
				SplashActivity.this.finish(); 
				break;
			}
			super.handleMessage(msg);
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Prepare the system account manager. On registering the listener below, we also ask for
		// an initial callback to pre-populate the account list.
		View layout = ((LayoutInflater)getSystemService("layout_inflater")).inflate(R.layout.splash_screen, null);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(layout);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		// first time preinitialization
		long lastVersionInstalled = DeviceUtil.getVersionInstalled(this);
		long current_updated_v = DeviceUtil.getVersion(this);
		if( lastVersionInstalled  != current_updated_v ){
			AppLog.d("MOV", "version is updated so some pre initialization can be done here");
			// whenever user update app , there should be a fresh start.
//			AppSettings.setPreference(this, null, AppSettings.USER_SELECTED_MAIL_ID, "");
//			AppSettings.setPreference(this, null, AppSettings.USER_SELECTED_PASSWORD, "");
//			AppSettings.setPreference(this, null, AppSettings.ACCOUNT_TOKEN, "");
//			AppSettings.setPreference(this, null, AppSettings.PROFILE_ID, "");
//			//			String curDate = DateUtils.getCurrentDate(DateUtils.FORMAT_YYYYMMDD_DASHESH);
//			AppSettings.setPreference(this, null, AppSettings.START_DATE, daterange[0]);
//			AppSettings.setPreference(this, null, AppSettings.END_DATE, daterange[1]);
		}

		Message msg = new Message();
		msg.what = STOPSPLASH;
		splashHandler.sendMessageDelayed(msg, SPLASHTIME);	
	}

	public void onBackPressed() {
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
