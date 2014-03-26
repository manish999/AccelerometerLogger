package com.rampgreen.acceldatacollector.service;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.PowerManager;
import android.os.RemoteException;
import android.provider.Settings.Secure;
import android.support.v4.app.NotificationCompat;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.rampgreen.acceldatacollector.R;
import com.rampgreen.acceldatacollector.util.AppLog;
import com.rampgreen.acceldatacollector.util.AppSettings;
import com.rampgreen.acceldatacollector.util.StringUtils;
import com.rampgreen.acceldatacollector.util.WidgetUtil;

public class AccelerometerService extends Service implements SensorEventListener, Runnable, CustomTimer.CustomTimerCallBack
{
	private static boolean isRunning = false;
	private static final String COMMA = "," ;
	private static final int NOTIFICATION_ID = 1;

	public static final int MSG_REGISTER_CLIENT = 1;
	public static final int MSG_UNREGISTER_CLIENT = 2;
	public static final int MSG_SET_INT_VALUE = 3;
	public static final int MSG_SET_STRING_VALUE = 4;
	public static final int MSG_START_RECORDING= 5;
	public static final int MSG_STOP_RECORDING= 6;

	private List<Messenger> mClients = new ArrayList<Messenger>(); // Keeps track of all current registered clients.
	private int mValue = 0; // Holds last value set by a client.

	private float sensorX;
	private float sensorY;
	private float sensorZ;

	private StringBuilder pointBufferFiller = new StringBuilder();// seperated by comma
	private Date enddate;
	private Date startdate;
	private DecimalFormat df3 = new DecimalFormat("#0.0000");
	private DecimalFormat df6 = new DecimalFormat("#0.000000");
	private DecimalFormat df0 = new DecimalFormat("#0");

	private boolean isStartFirstTimeStamp = false;
	private boolean isRecording;
	private boolean isLastCall;

	/*if frequency 2 and time duration 3 minute(180) so countmethodcall will be 360*/
	private int countMethodCall = 0;
	private int totalReverseDuration = 60;
	private int totalReadingXYZ = 60;
	private int timeCounter = 0;
	private int totalFrequency;
	private String durationToBeStoredOnServer = "0";
	private PendingIntent contentIntent;
	private CustomTimer customTimer;
	private NotificationManager mNotificationManager;
	private final Messenger mMessenger = new Messenger(new IncomingMessageHandler()); // Target we publish for clients to send messages to IncomingHandler.
	private SensorManager mSensorManager;
	private Sensor mAccelerometer;
	//	private File dir;
	private File fileLog;
	private FileWriter writer;
	public static final String upLoadServerUri = "http://121.240.116.173/accelerometer/file.php";

	public static boolean isRunning() {
		return isRunning;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		AppLog.d("Service Started.");
		mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		mSensorManager.registerListener(this, mAccelerometer,
				SensorManager.SENSOR_DELAY_FASTEST);

		acquireWakeLock();
		// Register our receiver for the ACTION_SCREEN_OFF action. This will make our receiver
		// code be called whenever the phone enters standby mode.
		IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
		registerReceiver(mReceiver, filter);

		startRecordingData();
		startForeground();

		isRunning = true;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		AppLog.d("Received start id " + startId + ": " + intent);
		return START_STICKY; // Run until explicitly stopped.
	}

	@Override
	public IBinder onBind(Intent intent) {
		AppLog.d("onBind");
		return mMessenger.getBinder();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		AppLog.e("service destroy method is called.");

		mNotificationManager.cancel(R.string.service_started); // Cancel the persistent notification.
		// Unregister our receiver.
		unregisterReceiver(mReceiver);
		wakeLock.release();

		stopForeground(true);
		Log.i("MyService", "Service Stopped.");
		isRunning = false;
	}

	/**
	 * Handle incoming messages from MainActivity
	 */
	private class IncomingMessageHandler extends Handler { // Handler of incoming messages from clients.
		@Override
		public void handleMessage(Message msg) {
			AppLog.d("handleMessage: " + msg.what);
			switch (msg.what) {
			case MSG_REGISTER_CLIENT:
				mClients.add(msg.replyTo);
				break;
			case MSG_UNREGISTER_CLIENT:
				mClients.remove(msg.replyTo);
				//				stopPreRecordingData();
				break;
			case MSG_SET_INT_VALUE:
				//				incrementBy = msg.arg1;
				break;
			case MainActivity.EXPLICTLY_STOP:
				//				stopPreRecordingData();
				customTimer.stop();
				break;
			case MSG_START_RECORDING:

				break;
			case MSG_STOP_RECORDING:
				stopPreRecordingData();
				break;

			default:
				super.handleMessage(msg);
			}
		}
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
	}

	@Override
	public void onSensorChanged(SensorEvent sensorEvent) {
		sensorX =  sensorEvent.values[0];
		sensorY =  sensorEvent.values[1];
		sensorZ =  sensorEvent.values[2];
	}

	@Override
	public void exceuteOnUIThread(boolean isLastCall) {
		this.isLastCall = isLastCall;
		Thread thread = new Thread(this);
		thread.start();
		//		runOnUiThread(this);
		//		doTask();
	}	

	@Override
	public void run() {
		doTask();
	}

	private void doTask() {
		sendMessageToUI(1, false);
		AppLog.e("totalCall Of RUN method"+ ++countMethodCall);
		AppLog.e(df3.format(sensorX) + COMMA + df3.format(sensorY) + COMMA + df3.format(sensorZ) + COMMA);
		pointBufferFiller.append(df3.format(sensorX) + COMMA + df3.format(sensorY) + COMMA + df3.format(sensorZ) + COMMA);
		try {
			writer.append(pointBufferFiller.toString());
			pointBufferFiller.setLength(0);
		} catch (IOException e) {
			AppLog.e("IO EXCEPTION IN DOTASK METHOD"+e.getMessage());
		}

		if(isLastCall) {
			AppLog.e("Last call : " + durationToBeStoredOnServer);
			if(timeCounter <= 0) {
				timeCounter = totalFrequency;
				--totalReverseDuration;
				String formatedTime = getTimeInFormat(totalReverseDuration);
				updateNotification("Remaining time: "+formatedTime);
				//				this.txtlogtime.setText("" + --totalReverseDuration + " seconds");
				//				plotTheGraph();
			}
			timeCounter--;
			stopPreRecordingData();
			//			peroformButtonClick();
			return;
		}
		//		AppLog.e(df3.format(sensorX) + COMMA + df3.format(sensorY) + COMMA + df3.format(sensorZ) + COMMA);
		//		pointBufferFiller.append(df3.format(sensorX) + COMMA + df3.format(sensorY) + COMMA + df3.format(sensorZ) + COMMA);

		if(isStartFirstTimeStamp) {
			startdate = Calendar.getInstance().getTime();
			AppLog.e("isStartFirstTimeStamp");
			// start reading from 1st second so leave the 0th reading. 
			resetSensorDataFiller();
			isStartFirstTimeStamp = false;
		}
		if(isRecording) {
			if(timeCounter <= 0) {
				timeCounter = totalFrequency;
				--totalReverseDuration;
				String formatedTime = getTimeInFormat(totalReverseDuration);
				updateNotification("Remaining time: "+formatedTime );
				//				this.txtlogtime.setText("" + --totalReverseDuration + " seconds");	
				//				plotTheGraph();
			}
			timeCounter--;
		}

		try {
			writer.append(pointBufferFiller.toString());
			pointBufferFiller.setLength(0);
		} catch (IOException e) {
			AppLog.e("FILE IOEXCEPTION IN DO TASK METHOD"+e.getMessage());
		}
	}

	private void startForeground() {
		startForeground(1, getMyActivityNotification(""));
	}

	private Notification getMyActivityNotification(String text){
		// The PendingIntent to launch our activity if the user selects
		// this notification
		CharSequence title = "Accelerometer Data";//getText(R.string.title_activity);
		if(contentIntent == null) {
			contentIntent = PendingIntent.getActivity(this,
					0, new Intent(this, MainActivity.class), 0);
		}
		Notification notification = new NotificationCompat.Builder(this)
		.setContentTitle(title)
		.setContentText(text)
		.setSmallIcon(R.drawable.ic_launcher)
		.setContentIntent(contentIntent).build();  
		return notification;    
	}
	/**
this is the method that can be called to update the Notification
	 */
	private void updateNotification(String text) {
		Notification notification = getMyActivityNotification(text);
		mNotificationManager.notify(NOTIFICATION_ID, notification);
	}

	private void resetSensorDataFiller()
	{
		pointBufferFiller.setLength(0);// more faster than insert and allocate new one
	}

	/**
	 * Send the data to all clients.
	 * @param intvaluetosend The value to send.
	 */
	private void sendMessageToUI(int intvaluetosend, boolean enableStartButton) {
		Iterator<Messenger> messengerIterator = mClients.iterator();		
		while(messengerIterator.hasNext()) {
			Messenger messenger = messengerIterator.next();
			try {
				// Send data as an Integer				
				messenger.send(Message.obtain(null, MSG_SET_INT_VALUE, intvaluetosend, 0));

				// Send data as a String
				Bundle bundle = new Bundle();
				//				bundle.putString("str1", "x: " + intvaluetosend + "cd");
				bundle.putString("str1", "x: " + sensorX + "\ny: " + sensorY +"\nz: " + sensorZ);
				bundle.putFloat("x", sensorX);
				bundle.putFloat("y", sensorY);
				bundle.putFloat("z", sensorZ);
				bundle.putBoolean("startButtonState", enableStartButton);
				bundle.putFloat("duration", intvaluetosend);
				Message msg = Message.obtain(null, MSG_SET_STRING_VALUE);
				msg.setData(bundle);
				messenger.send(msg);

			} catch (RemoteException e) {
				// The client is dead. Remove it from the list.
				mClients.remove(messenger);
			}
		}
	}

	private void startRecordingData() {
		if (! isRecording)
		{
			resetSensorDataFiller();
			isStartFirstTimeStamp = true;
		}
		// crate a new file every time user press start button.

		if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			AppLog.showToast(this, "Memory card is unavailable or it is mounted to computer.");
			//handle case of no SDCARD present
		} else {
			File file = new File(Environment.getExternalStorageDirectory()
					+File.separator
					+"Accelerometer");//folder name
			file.mkdirs();
			String fileName = getUniquId()+"_"+Calendar.getInstance().getTimeInMillis()+".txt";
			AppLog.e(fileName);
			fileLog = new File(file, fileName);
			try {
				fileLog.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		try
		{
			this.writer = new FileWriter(this.fileLog);
		} catch (Exception e) {
			e.printStackTrace();
		}

		String sensorSpeedSetting = (String)AppSettings.getPrefernce(this, null, AppSettings.DURATION, "1");
		int selectedTimeInMinutes = Integer.valueOf(sensorSpeedSetting).intValue();

		totalFrequency = 20;// set frequency to 20 to release version7
		totalReverseDuration = 60*60*4;//60*selectedTimeInMinutes;// set time to 4 hours to release version11
		durationToBeStoredOnServer = totalReverseDuration +"";
		totalReadingXYZ = totalReverseDuration * totalFrequency * 60;
		customTimer = new CustomTimer(this, 0, totalFrequency);
		customTimer.setAutomaticCancel(totalReverseDuration * 1000);
		customTimer.start();

		isRecording = true;
		startdate = Calendar.getInstance().getTime();
		enddate = Calendar.getInstance().getTime();
		//		duration = "0";
	}

	Object object;
	private void stopPreRecordingData() {
		if(isRecording ==false) {
			return;
		}
		enddate = Calendar.getInstance().getTime();
		playSound();
		//		showEditDialog();
		isRecording = false;
		isStartFirstTimeStamp = false;
		durationToBeStoredOnServer = countMethodCall / totalFrequency+"";
		countMethodCall = timeCounter = 0;
		customTimer.stop();
		//		try {
		//			object.wait();
		//		} catch (InterruptedException e1) {
		//			
		//		}
		/********************* file computation and uploading file to server*************/
		try {
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String filePath = fileLog.getPath();
		updateNotification("uploading file ....");
		sendMessageToUI(1, true);

		if(! WidgetUtil.checkInternetConnection(this)) {
			updateNotification("No Internet Connection.");
			return;
		}
		final String finalPath  = filePath;
		new Thread(new Runnable() {

			@Override
			public void run() {
				int serverResponse = uploadFile(finalPath);
				if(serverResponse == 200) {
					updateNotification("file  uploaded successfully. "+finalPath);
				} else if(serverResponse == 0){
					updateNotification("log file not exist. "+finalPath);
				} else if(serverResponse == -1){
					updateNotification("no internet connection. "+finalPath);
				} else {
					updateNotification("something gonna wrong. "+finalPath);
				}
				/***********************************************************************************/
				stopForeground(true);
				stopSelf();
			}
		}).start();

	}

	private void playSound() {
		try {
			Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
			if(alert == null){
				// alert is null, using backup
				alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);	
				if(alert == null){  // I can't see this ever being null (as always have a default notification) but just incase
					// alert backup is null, using 2nd backup
					alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);               
				}
			}
			Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), alert);
			r.play();
		} catch (Exception e) {
			AppLog.e(e.getMessage());
		}
	}

	private String imagepath=null;
	private int serverResponseCode;
	public int uploadFile(String sourceFileUri) {
		if(! WidgetUtil.checkInternetConnection(this)) {
			updateNotification("No Internet Connection.");
			return -1;
		}

		String fileName = sourceFileUri;

		HttpURLConnection conn = null;
		DataOutputStream dos = null;  
		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";
		int bytesRead, bytesAvailable, bufferSize;
		byte[] buffer;
		int maxBufferSize = 1 * 1024 * 1024; 
		File sourceFile = new File(sourceFileUri); 

		if (!sourceFile.isFile()) {
			AppLog.e("Source File not exist :"+imagepath);
			return 0;
		}
		else
		{
			try { 
				// open a URL connection to the Servlet
				FileInputStream fileInputStream = new FileInputStream(sourceFile);
				URL url = new URL(upLoadServerUri);

				// Open a HTTP  connection to  the URL
				conn = (HttpURLConnection) url.openConnection(); 
				conn.setDoInput(true); // Allow Inputs
				conn.setDoOutput(true); // Allow Outputs
				conn.setUseCaches(false); // Don't use a Cached Copy
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Connection", "Keep-Alive");
				conn.setRequestProperty("ENCTYPE", "multipart/form-data");
				conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
				conn.setRequestProperty("file", fileName); 

				dos = new DataOutputStream(conn.getOutputStream());

				dos.writeBytes(twoHyphens + boundary + lineEnd); 
				dos.writeBytes("Content-Disposition: form-data; name=\"file\";filename=\""
						+ fileName + "\"" + lineEnd);

				dos.writeBytes(lineEnd);

				// create a buffer of  maximum size
				bytesAvailable = fileInputStream.available(); 

				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				buffer = new byte[bufferSize];

				// read file and write it into form...
				bytesRead = fileInputStream.read(buffer, 0, bufferSize);  

				while (bytesRead > 0) {

					dos.write(buffer, 0, bufferSize);
					bytesAvailable = fileInputStream.available();
					bufferSize = Math.min(bytesAvailable, maxBufferSize);
					bytesRead = fileInputStream.read(buffer, 0, bufferSize);   

				}

				// send multipart form data necesssary after file data...
				dos.writeBytes(lineEnd);
				dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

				// Responses from the server (code and message)
				serverResponseCode = conn.getResponseCode();
				String serverResponseMessage = conn.getResponseMessage();

				Log.i("uploadFile", "HTTP Response is : " 
						+ serverResponseMessage + ": " + serverResponseCode);

				if(serverResponseCode == 200){
					AppLog.e("File Upload Completed.\n\n See uploaded file here : \n\n"
							+upLoadServerUri);
				}    
				//close the streams //
				fileInputStream.close();
				dos.flush();
				dos.close();

			} catch (MalformedURLException ex) {
				AppLog.e("MalformedURLException Exception : check script url.");
				Log.e("Upload file to server", "error: " + ex.getMessage(), ex);  
			} catch (Exception e) {
				AppLog.e("Got Exception : see logcat "+e.getMessage());
				Log.e("Upload file to server Exception", "Exception : "  + e.getMessage(), e);  
			}
			//	          dialog.dismiss();       
			return serverResponseCode; 

		} // End else block 
	}

	private String getUniquId() {
		String id = WidgetUtil.getMacAddress(this);
		if(StringUtils.isEmpty(id)) {
			TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
			if (tm != null)
				id = tm.getDeviceId();
			if (StringUtils.isEmpty(id))
				id = Secure.getString(getContentResolver(),Secure.ANDROID_ID);
		}
		id = id.replaceAll("\\:", "");
		return id;
	}

	private String getTimeInFormat(int totalInSeconds) {
		int hours = totalInSeconds / (60*60);
		int minutes = (totalInSeconds % (60*60))/ 60;
		int seconds = (totalInSeconds % (60*60)) %60;
		String str = String.format("%02d:%02d:%02d", hours, minutes, seconds);
		return str;
	}

	// BroadcastReceiver for handling ACTION_SCREEN_OFF.
	private BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			// Check action just to be on the safe side.
			if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
				// Unregisters the listener and registers it again.
				//                StepService.this.unregisterDetector();
				//                StepService.this.registerDetector();
				//                if (mPedometerSettings.wakeAggressively()) {
				wakeLock.release();
				acquireWakeLock();
				//                }
			}
		}
	};
	private PowerManager.WakeLock wakeLock;

	private void acquireWakeLock() {
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		int wakeFlags;
		//        if (mPedometerSettings.wakeAggressively()) {
		//            wakeFlags = PowerManager.SCREEN_DIM_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP;
		//        }
		//        else if (mPedometerSettings.keepScreenOn()) {
		//            wakeFlags = PowerManager.SCREEN_DIM_WAKE_LOCK;
		//        }
		//        else {
		wakeFlags = PowerManager.PARTIAL_WAKE_LOCK;
		//        }
		wakeLock = pm.newWakeLock(wakeFlags, "Accelerometer");
		wakeLock.acquire();
	}

}