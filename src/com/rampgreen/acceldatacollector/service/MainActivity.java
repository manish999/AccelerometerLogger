package com.rampgreen.acceldatacollector.service;

import java.text.DecimalFormat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.text.InputType;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import com.rampgreen.acceldatacollector.MainActivityNew;
import com.rampgreen.acceldatacollector.R;
import com.rampgreen.acceldatacollector.util.AppLog;
import com.rampgreen.acceldatacollector.util.AppSettings;
import com.rampgreen.acceldatacollector.util.StringUtils;

public class MainActivity extends Activity implements View.OnClickListener, ServiceConnection{
	private Button btnStart, btnStop, btnBind, btnUnbind, btnUpby1, btnUpby10;
	private TextView textStatus, textIntValue, textStrValue;
	private Messenger mServiceMessenger = null;
	boolean mIsBound;
	private DecimalFormat df3 = new DecimalFormat("#0.0000");
	private DecimalFormat df6 = new DecimalFormat("#0.000000");
	private DecimalFormat df0 = new DecimalFormat("#0");

	private static final String LOGTAG = "MainActivity";
	public static final int EXPLICTLY_STOP = 1000;
	private final Messenger mMessenger = new Messenger(new IncomingMessageHandler());

	private ServiceConnection mConnection = this;
	private View buttonWalkingOnly;
	private TextView txtsensordata;
	private TextView txtlogtime;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		btnStart = (Button)findViewById(R.id.btnStart);
		btnStop = (Button)findViewById(R.id.btnStop);
		btnBind = (Button)findViewById(R.id.btnBind);
		btnUnbind = (Button)findViewById(R.id.btnUnbind);
		textStatus = (TextView)findViewById(R.id.textStatus);
		textIntValue = (TextView)findViewById(R.id.textIntValue);
		textStrValue = (TextView)findViewById(R.id.textStrValue);
		btnUpby1 = (Button)findViewById(R.id.btnUpby1);
		btnUpby10 = (Button)findViewById(R.id.btnUpby10);
		
		buttonWalkingOnly = (Button)findViewById(R.id.btn_start_stop);
		txtsensordata = (TextView)findViewById(R.id.txtlogdata);
		txtlogtime = (TextView)findViewById(R.id.txtlogtime);

		btnStart.setOnClickListener(this);
		btnStop.setOnClickListener(this);
		btnBind.setOnClickListener(this);
		btnUnbind.setOnClickListener(this);
		btnUpby1.setOnClickListener(this);
		btnUpby10.setOnClickListener(this);

		buttonWalkingOnly.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if(v.isSelected()) {
					//whichButtonSelected = 0;
					AppLog.e("buttonWalking buton is not selected");
					buttonWalkingOnly.setEnabled(true);
					buttonWalkingOnly.setBackgroundResource(R.drawable.start);
					v.setSelected(false);
					v.setBackgroundResource(R.drawable.start);
				} else{
					//whichButtonSelected = PERFORM_CLICK_BUTTON_WALKING;
					AppLog.e("buttonWalking buton is selected");
					buttonWalkingOnly.setEnabled(false);
					buttonWalkingOnly.setBackgroundResource(R.drawable.start_disable);
					v.setEnabled(true);
					v.setSelected(true);
					v.setBackgroundResource(R.drawable.stop);
				}
			}
		});
		automaticBind();
	}

	/**
	 * Check if the service is running. If the service is running 
	 * when the activity starts, we want to automatically bind to it.
	 */
	private void automaticBind() {
		if (AccelerometerService.isRunning()) {
			doBindService();
			btnBind.performClick();
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("textStatus", textStatus.getText().toString());
		outState.putString("textIntValue", textIntValue.getText().toString());
		outState.putString("textStrValue", textStrValue.getText().toString());
		outState.putBoolean("startButtonState", btnBind.isEnabled());
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		if (savedInstanceState != null) {
			textStatus.setText(savedInstanceState.getString("textStatus"));
			textIntValue.setText(savedInstanceState.getString("textIntValue"));
			textStrValue.setText(savedInstanceState.getString("textStrValue"));
		}
		super.onRestoreInstanceState(savedInstanceState);
	}

	/**
	 * Send data to the service
	 * @param intvaluetosend The data to send
	 */
	private void sendMessageToService(int intvaluetosend) {
		if (mIsBound) {
			if (mServiceMessenger != null) {
				try {
					Message msg = Message.obtain(null, AccelerometerService.MSG_SET_INT_VALUE, intvaluetosend, 0);
					msg.replyTo = mMessenger;
					mServiceMessenger.send(msg);
				} catch (RemoteException e) {
				}
			}
		}
	}

	/**
	 * Bind this Activity to AccelerometerService
	 */
	private void doBindService() {
		// if we will not call below line, service destroyed on click back button because of BIND_AUTO_CREATE
		startService(new Intent(MainActivity.this, AccelerometerService.class));
		bindService(new Intent(this, AccelerometerService.class), mConnection, Context.BIND_AUTO_CREATE);
		mIsBound = true;
		textStatus.setText("Binding.");
	}

	/**
	 * Un-bind this Activity to AccelerometerService
	 */	
	private void doUnbindService(boolean shouldRunInBackground) {
		if (mIsBound) {
			// If we have received the service, and hence registered with it, then now is the time to unregister.
			if (mServiceMessenger != null) {
				try {
					Message msg;
					if(shouldRunInBackground)
						msg = Message.obtain(null, AccelerometerService.MSG_UNREGISTER_CLIENT);
					else
						msg = Message.obtain(null, AccelerometerService.MSG_STOP_RECORDING);
					msg.replyTo = mMessenger;
					mServiceMessenger.send(msg);
				} catch (RemoteException e) {
					// There is nothing special we need to do if the service has crashed.
				}
			}
			// Detach our existing connection.
			unbindService(mConnection);
			mIsBound = false;
			textStatus.setText("Unbinding.");
		}
	}

	/**
	 * Handle button clicks
	 */
	@Override
	public void onClick(View v) {
		if(v.equals(btnStart)) {
			//			startService(new Intent(MainActivity.this, AccelerometerService.class));
		}
		else if(v.equals(btnStop)) {
//			sendMessageToService(EXPLICTLY_STOP);
//			doUnbindService(false);
//			stopService(new Intent(MainActivity.this, AccelerometerService.class));
		}
		else if(v.equals(btnBind)) {
			if(v.isSelected()) {
				//whichButtonSelected = 0;
				AppLog.e("buttonWalking buton is not selected");
				btnBind.setEnabled(true);
				btnBind.setBackgroundResource(R.drawable.start);
				v.setSelected(false);
				v.setBackgroundResource(R.drawable.start);
				// unbind the service and stop
				doUnbindService(false);
//				stopService(new Intent(MainActivity.this, AccelerometerService.class));
			} else{
				//whichButtonSelected = PERFORM_CLICK_BUTTON_WALKING;
				AppLog.e("buttonWalking buton is selected");
				btnBind.setEnabled(false);
				btnBind.setBackgroundResource(R.drawable.start_disable);
//				v.setEnabled(true);
//				v.setSelected(true);
//				v.setBackgroundResource(R.drawable.stop);
				doBindService();
			}
		}
		else if(v.equals(btnUnbind)) {
//			doUnbindService(false);
		}
		else if(v.equals(btnUpby1)) {
			//			sendMessageToService(1);
		}
		else if(v.equals(btnUpby10)) {
			//			sendMessageToService(10);
		}
	}

	@Override
	public void onServiceConnected(ComponentName name, IBinder service) {
		mServiceMessenger = new Messenger(service);
		textStatus.setText("Attached.");
		try {
			Message msg = Message.obtain(null, AccelerometerService.MSG_REGISTER_CLIENT);
			msg.replyTo = mMessenger;
			mServiceMessenger.send(msg);
		} 
		catch (RemoteException e) {
			// In this case the service has crashed before we could even do anything with it
		} 
	}

	@Override
	public void onServiceDisconnected(ComponentName name) {
		// This is called when the connection with the service has been unexpectedly disconnected - process crashed.
		mServiceMessenger = null;
		textStatus.setText("Disconnected.");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		try {
			doUnbindService(true);
		} catch (Throwable t) {
			Log.e(LOGTAG, "Failed to unbind from the service", t);
		}
	}

	/**
	 * Handle incoming messages from AccelerometerService
	 */
	private class IncomingMessageHandler extends Handler {		

		@Override
		public void handleMessage(Message msg) {
			// Log.d(LOGTAG,"IncomingHandler:handleMessage");
			switch (msg.what) {
			case AccelerometerService.MSG_SET_INT_VALUE:
//				textIntValue.setText("Elapsed duration: " + msg.arg1+ " seconds");
				break;
			case AccelerometerService.MSG_SET_STRING_VALUE:
				String str1 = msg.getData().getString("str1");
				Float x = msg.getData().getFloat("x");
				Float y = msg.getData().getFloat("y");
				Float z = msg.getData().getFloat("z");
				Float duration = msg.getData().getFloat("duration");
				boolean enableStartButton = msg.getData().getBoolean("startButtonState");
				if(enableStartButton) {
//					btnBind.setEnabled(true);
					btnBind.setSelected(true);
					btnBind.performClick();
//					btnBind.setBackgroundResource(R.drawable.start);
				}
				textStrValue.setText(str1);
//				txtsensordata.setText(Html.fromHtml("<b><font color=\"red\">X axis</font></b> : " + df3.format(x) + " m/s<sup>2</sup>" + "<br><b><font color=\"green\">Y axis</font></b> : " + df3.format(y) + " m/s<sup>2</sup>" + "<br><b><font color=\"blue\">Z axis</font></b> : " + df3.format(z) + " m/s<sup>2</sup>"));
//				txtlogtime.setText("" + df0.format(duration) + " seconds");
				break;
			default:
				super.handleMessage(msg);
			}
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
//		getMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem p_item) {
		switch (p_item.getItemId()) {
		case 0:

		case R.id.action_setting_accelerometer:
			showEditDialog();
			return true;
//		case R.id.action_logout:
//			AppSettings.setPreference(this, null, AppSettings.ACCESS_TOKEN, "");
//			AppSettings.setPreference(this, null, AppSettings.USER_ID, "");
////			AppSettings.setPreference(this, null, AppSettings.USER_SELECTED_MAIL_ID, "");
//			AppSettings.setPreference(this, null, AppSettings.USER_SELECTED_PASSWORD, "");
//			BeanController.getLoginBean().setId("");
//
//			intent = new Intent(getApplicationContext(), LoginActivity.class);
//			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			startActivity(intent);
//			finish();
//			return true;
		}
		return super.onOptionsItemSelected(p_item);
	}
	
	private void showEditDialog() {
		//	      FragmentManager fm = getSupportFragmentManager();
		//	      EditNameDialog editNameDialog = EditNameDialog.newInstance("Some Title");
		//	      editNameDialog.show(fm, "fragment_edit_name");
		AlertDialog.Builder alert =new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AwesomeDialogTheme));

		alert.setCancelable(false);
		alert.setTitle("Enter duration in minutes");
		alert.setMessage(null);
		LayoutParams para = new LayoutParams(LayoutParams.MATCH_PARENT, 45);
		para.gravity = Gravity.CENTER;
//		String sensorSpeedSetting = (String)AppSettings.getPrefernce(this, null, AppSettings.DURATION, "1");
//		int selectedTimeInMinutes = Integer.valueOf(sensorSpeedSetting).intValue();
		// Set an EditText view to get user input 
		final EditText input = new EditText(this);
		input.setBackgroundColor(Color.WHITE);
		input.setTextColor(Color.BLACK);
		input.setGravity(Gravity.CENTER);
		input.setRawInputType(InputType.TYPE_CLASS_NUMBER);
		input.setLayoutParams(para);
		alert.setView(input);
//		getWindow().setSoftInputMode(
//				WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		
		alert.setPositiveButton("Finish", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				String stepsCount = input.getText().toString();
				if(StringUtils.isNumeric(stepsCount)) {
					AppSettings.setPreference(MainActivity.this, null, AppSettings.DURATION, stepsCount);
					AppLog.showToast(MainActivity.this, "Duration : " + stepsCount);
				} else {
					AppLog.showToast(MainActivity.this, "Only numeric values are allowed.");
					showEditDialog();
				}
			}
		});
		alert.show();
		input.requestFocus();
	}
}