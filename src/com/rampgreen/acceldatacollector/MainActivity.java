package com.rampgreen.acceldatacollector;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.achartengine.GraphicalView;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.rampgreen.acceldatacollector.csv.Points;
import com.rampgreen.acceldatacollector.db.MyAppDbAdapter;
import com.rampgreen.acceldatacollector.db.MyAppDbSQL;
import com.rampgreen.acceldatacollector.util.AppLog;
import com.rampgreen.acceldatacollector.util.AppSettings;
import com.rampgreen.acceldatacollector.util.StringUtils;
import com.rampgreen.acceldatacollector.util.WidgetUtil;

public class MainActivity extends Activity  implements SensorEventListener, Listener, ErrorListener, Runnable, CustomTimer.CustomTimerCallBack
{
	private int whichButtonSelected = 0;

	private static final String COMMA = "," ;
	private SensorManager mSensorManager;
	private Sensor mAccelerometer;
	private StringBuilder pointBufferFiller = new StringBuilder();// seperated by comma

	private TextView txtlogtime;
	private String activityTypeForSensor = Constants.ACCEL_ACTIVITY_RUNNING;
	private Date enddate;
	private Date startdate;
	private DecimalFormat df3 = new DecimalFormat("#0.0000");
	private DecimalFormat df6 = new DecimalFormat("#0.000000");
	private DecimalFormat df0 = new DecimalFormat("#0");

	boolean isRecording = false;
	boolean isStartFirstTimeStamp = false;
	private String durationToBeStoredOnServer = "0";
	private int selectedSpeed = 0;
	private boolean init;
	int timeCounter = 0;

	private Vector<Points> csvModelList = new Vector<Points>();

	private int calledActivity;
	private GraphicalView view;

	private LinearLayout mChartContainer;
	private ArrayList<Float> xList = new ArrayList<Float>();
	private ArrayList<Float> yList = new ArrayList<Float>();
	private ArrayList<Float> zList = new ArrayList<Float>();;

	private Graph mGraph;
	private float sensorX, sensorY, sensorZ;

	private int totalReverseDuration = 60;
	private int totalReadingXYZ = 60;
	private boolean isLastCall;
	/*if frequency 2 and time duration 3 minute(180) so countmethodcall will be 360*/
	private int countMethodCall = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.logging_new);
		// initialising the ui element 
		initUI();
		String userName = (String) AppSettings.getPrefernce(this, null, AppSettings.USER_SELECTED_MAIL_ID, ""); 
		String password = (String) AppSettings.getPrefernce(this, null, AppSettings.USER_SELECTED_PASSWORD, "");
		mChartContainer = (LinearLayout) findViewById(R.id.chart_containerAcc);
		mGraph = new Graph(this);

		Bundle bundle = getIntent().getExtras();
		if(bundle != null) {
			calledActivity = bundle.getInt(Constants.CALLING_ACTIVITY_TYPE);
		}
		switch (calledActivity) {
		case Constants.CALLING_ACTIVITY_SPLASH:
			if(StringUtils.notEmpty(userName) && StringUtils.notEmpty(password)) {
				AutoLogin autoLogin = new AutoLogin(this);
				autoLogin.doLogin();
			} else {
				AppSettings.setPreference(this, null, AppSettings.ACCESS_TOKEN, "");
				AppSettings.setPreference(this, null, AppSettings.USER_SELECTED_PASSWORD, "");
				AppSettings.setPreference(this, null, AppSettings.USER_ID, "");
				BeanController.getLoginBean().setId("");

				Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				finish();				
			}
			break;
		case Constants.CALLING_ACTIVITY_LOGIN:

			break;
		case Constants.CALLING_ACTIVITY_REGISTRATION:

			break;

		default:
			break;
		}
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

		this.txtlogtime = ((TextView)findViewById(R.id.txtlogtime));

		this.startdate = Calendar.getInstance().getTime();
		resetSensorDataFiller();

		/*******************************************************************
		this.dir = new File(Environment.getExternalStorageDirectory() + "/AccelLogger");
		this.dir.mkdirs();
		this.f = new File(this.dir, "temp.txt");
		try
		{
			this.f.createNewFile();
			this.writer = new FileWriter(this.f);
		} catch (Exception e) {
			e.printStackTrace();
		}
		 **/
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		String sensorSpeedSetting = (String)AppSettings.getPrefernce(this, null, AppSettings.SPEED_SENSOR, "0");
		selectedSpeed = Integer.valueOf(sensorSpeedSetting).intValue();
		switch (selectedSpeed) {
		case 0:
			mSensorManager.registerListener(this, mAccelerometer,
					SensorManager.SENSOR_DELAY_FASTEST);
			//			mSensorManager.registerListener(this, mAccelerometer,
			//					SensorManager.SENSOR_DELAY_NORMAL);
			break;
		case 1:
			mSensorManager.registerListener(this, mAccelerometer,
					SensorManager.SENSOR_DELAY_NORMAL);
			break;
		case 2:
			mSensorManager.registerListener(this, mAccelerometer,
					SensorManager.SENSOR_DELAY_UI);
			break;
		case 3:
			mSensorManager.registerListener(this, mAccelerometer,
					SensorManager.SENSOR_DELAY_GAME);
			break;
		case 4:
			mSensorManager.registerListener(this, mAccelerometer,
					SensorManager.SENSOR_DELAY_FASTEST);
			break;

		default:
			break;
		}

		String userID = BeanController.getLoginBean().getId();
		if(StringUtils.isEmpty(userID) || userID.equalsIgnoreCase("0")) {
			userID = (String)AppSettings.getPrefernce(MainActivity.this, null, AppSettings.USER_ID, "");
		}
		fetchAccelData(userID, activityTypeForSensor);

		if(! WidgetUtil.checkInternetConnection(this)) {
			WidgetUtil.showSettingDialog(this);
			return;
		}
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		mSensorManager.unregisterListener(this, mAccelerometer);
		enableAllToggleButton();

		if(isRecording && isStartFirstTimeStamp) {
			peroformButtonClick();
		}
		MainActivity.this.isRecording = false;
	}

	protected void onStop() {
		super.onStop();
		if(mEntryCursorAccel != null)
		{
			stopManagingCursor(mEntryCursorAccel);
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem p_item) {
		Intent intent;
		switch (p_item.getItemId()) {
		case 0:
			break;
		case R.id.action_setting_accelerometer:
			intent = new Intent(this, ActivityConfigure.class);
			startActivity(intent);
			break;

		case R.id.action_logout:
			AppSettings.setPreference(this, null, AppSettings.ACCESS_TOKEN, "");
			AppSettings.setPreference(this, null, AppSettings.USER_SELECTED_PASSWORD, "");
			AppSettings.setPreference(this, null, AppSettings.USER_ID, "");
			BeanController.getLoginBean().setId("");

			intent = new Intent(getApplicationContext(), LoginActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			finish();
			return true;
		}
		return super.onOptionsItemSelected(p_item);
	}

	private void resetSensorDataFiller()
	{
		csvModelList.clear();
		pointBufferFiller.setLength(0);// more faster than insert and allocate new one
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1)
	{

	}

	private void peroformButtonClick () {
		switch (whichButtonSelected) {
		case 1001:
			buttonRunning.performClick();
			break;
		case 1002:
			buttonWalking.performClick();
			break;
		case 1003:
			buttonSitting.performClick();
			break;
		case 1004:
			buttonclimbingUp.performClick();
			break;
		case 1005:
			buttonClimbingDown.performClick();
			break;

		default:
			break;
		}

		AppLog.e("performclick  ActivityType:   "+ activityTypeForSensor);
	}

	@Override
	public void onSensorChanged(SensorEvent sensorEvent)
	{
		sensorX =  sensorEvent.values[0];
		sensorY =  sensorEvent.values[1];
		sensorZ =  sensorEvent.values[2];
	}

	private String sendLoggerData(String userID, String activityType, String startTime, String endTime, Vector<Points> pointsList) throws JSONException 
	{
		if(durationToBeStoredOnServer.equalsIgnoreCase("0")) {
			return "Duration is 0, so no need to send logged data.";
		}
		// Here we convert Java Object to JSON 
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("user_id", userID); // Set the first name/pair 
		jsonObj.put("activity_type", activityType);
		jsonObj.put("start_time_stamp", startTime);
		jsonObj.put("end_time_stamp", endTime);
		jsonObj.put("duration", durationToBeStoredOnServer);
		StringBuilder stringBuilder = new StringBuilder(pointBufferFiller);
		jsonObj.put("points", stringBuilder.toString());

		// get network queue and add request to the queue
		if(! WidgetUtil.checkInternetConnection(this)) {
			WidgetUtil.showSettingDialog(this);
			return "No internet connection";
		}
		MyRequestQueue queue = MyVolley.getRequestQueue();
		Map<String, String> loginParam = QueryHelper.createSensorDataQuery(jsonObj.toString());
		CustomRequest customRequest = new CustomRequest(Method.POST,
				Constants.URL_WEB_SERVICE, loginParam,
				MainActivity.this, MainActivity.this);
		queue.add(customRequest);
		// In this case we need a json array to hold the java list
		/*******************************************************************/
		//		JSONArray jsonArr = new JSONArray();
		//		
		//		for (Points points : pointsList)
		//		{
		//			JSONObject pnObj = new JSONObject();
		//			pnObj.put("x", points.getX());
		//			pnObj.put("y", points.getY());
		//			pnObj.put("z", points.getZ());
		//			jsonArr.put(pnObj);
		//		}

		/*******************************************************************/

		//		jsonObj.put("points", jsonArr);

		// get network queue and add request to the queue
		//		RequestQueue queue = MyVolley.getVollyRequestQueue();
		//		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Method.POST, Constants.URL_WEB_SERVICE, jsonObj, this, this);
		//		Map<String, String> loginParam = QueryHelper.createSensorDataQuery(userID,activityType, startTime, endTime, pointsList);
		//		StringRequest stringRequest = new StringRequest(Method.POST, Constants.URL_WEB_SERVICE, MainActivity.this, (ErrorListener) MainActivity.this);
		//		CustomRequest customRequest = new CustomRequest(Method.POST,
		//				Constants.URL_WEB_SERVICE, loginParam,
		//				LoginActivity.this, LoginActivity.this);

		//		queue.add(jsonObjectRequest);

		Log.e("JSON REQUEST String********* ", jsonObj.toString()+"**********");
		return jsonObj.toString();

	}

	@Override
	public void onResponse(Object response)
	{
		resetSensorDataFiller();
		AppLog.e("Volly success"+response.toString());
		//play sound after completion. 
		playSound();
	}

	@Override
	public void onErrorResponse(VolleyError error)
	{
		resetSensorDataFiller();
		AppLog.e("Volly error"+error.getMessage());
	}

	private String getActivityType(String activityType) {
		AppLog.e("ingetActivtyTypeMethod : start"+activityType);
		String[] arrayOfString = getResources().getStringArray(R.array.date_array);
		if(activityType.equalsIgnoreCase("Running")) {
			AppLog.e("Run"+activityType);
			return Constants.ACCEL_ACTIVITY_RUNNING;
		} else if(activityType.equalsIgnoreCase("Walking")) {
			AppLog.e("walk"+activityType);
			return Constants.ACCEL_ACTIVITY_WALKING;
		} else if(activityType.equalsIgnoreCase("Sitting")) {
			AppLog.e("sit"+activityType);
			return Constants.ACCEL_ACTIVITY_SITTING;
			//		} else if(activityType.equalsIgnoreCase("Sleeping")) {
			//			return Constants.ACCEL_ACTIVITY_SLEEPING;
		} else if(activityType.equalsIgnoreCase("Climb Up")) {
			AppLog.e("climb up"+activityType);
			return Constants.ACCEL_ACTIVITY_CLIMBING_UP;
		} else if(activityType.equalsIgnoreCase("Climb Down")) {
			AppLog.e("climb down"+activityType);
			return Constants.ACCEL_ACTIVITY_CLIMBING_DOWN;
		}
		AppLog.e("ingetActivtyTypeMethod : end execption case"+activityType);
		return Constants.ACCEL_ACTIVITY_RUNNING;
	}

	private void disableAllToggleButton() {
		buttonRunning.setEnabled(false);
		buttonWalking.setEnabled(false);
		buttonSitting.setEnabled(false);
		buttonclimbingUp.setEnabled(false);
		buttonClimbingDown.setEnabled(false);

		buttonRunning.setBackgroundResource(R.drawable.start_disable);
		buttonWalking.setBackgroundResource(R.drawable.start_disable);
		buttonSitting.setBackgroundResource(R.drawable.start_disable);
		buttonclimbingUp.setBackgroundResource(R.drawable.start_disable);
		buttonClimbingDown.setBackgroundResource(R.drawable.start_disable);
	}

	private void enableAllToggleButton() {
		buttonRunning.setEnabled(true);
		buttonWalking.setEnabled(true);
		buttonSitting.setEnabled(true);
		buttonclimbingUp.setEnabled(true);
		buttonClimbingDown.setEnabled(true);

		buttonRunning.setBackgroundResource(R.drawable.start);
		buttonWalking.setBackgroundResource(R.drawable.start);
		buttonSitting.setBackgroundResource(R.drawable.start);
		buttonclimbingUp.setBackgroundResource(R.drawable.start);
		buttonClimbingDown.setBackgroundResource(R.drawable.start);
	}

	private ProgressBar runningBar1; 
	private ProgressBar runningBar2;
	private ProgressBar runningBar3;
	private Button buttonRunning;
	private TextView tvRunning;

	private ProgressBar walkingBar1;
	private ProgressBar walkingBar2;
	private ProgressBar walkingBar3;
	private Button buttonWalking;
	private TextView tvWalking;

	private ProgressBar sittingBar1;
	private ProgressBar sittingBar2;
	private ProgressBar sittingBar3;
	private Button buttonSitting;
	private TextView tvSitting;

	private ProgressBar climbingUpBar1;
	private ProgressBar climbingUpBar2;
	private ProgressBar climbingUpBar3;
	private Button buttonclimbingUp;
	private TextView tvClimbingUp;

	private ProgressBar climbingDownBar1;
	private ProgressBar climbingDownBar2;
	private ProgressBar climbingDownBar3;
	private Button buttonClimbingDown;
	private TextView tvClimbingDown;

	private void clickActivityButton(View v, String activityType, int whichButtonSelected) {
		//		if(v.isSelected()) {
		//			this.whichButtonSelected = whichButtonSelected;
		//			activityTypeForSensor = getActivityType(activityType);
		//			AppLog.e("running buton is not selected");
		//			enableAllToggleButton();
		//			v.setSelected(false);
		//			v.setBackgroundResource(R.drawable.start);
		//			stopRecordingData();
		//		} else{
		//			whichButtonSelected = 1001;
		//			AppLog.e("running buton is selected");
		//			disableAllToggleButton();
		//			v.setEnabled(true);
		//			v.setSelected(true);
		//			v.setBackgroundResource(R.drawable.stop);
		//			startRecordingData();
		//		}
	}

	private void initUI() {
		LinearLayout layoutRunning = (LinearLayout)findViewById(R.id.barbutton1);
		LinearLayout layoutWalking = (LinearLayout)findViewById(R.id.barbutton2);
		LinearLayout layoutSitting = (LinearLayout)findViewById(R.id.barbutton3);
		LinearLayout layoutClimbingUp = (LinearLayout)findViewById(R.id.barbutton4);
		LinearLayout layoutClimbingDown = (LinearLayout)findViewById(R.id.barbutton5);

		runningBar1 = (ProgressBar)layoutRunning.findViewById(R.id.indicator1);
		runningBar2 = (ProgressBar)layoutRunning.findViewById(R.id.indicator2);
		runningBar3 = (ProgressBar)layoutRunning.findViewById(R.id.indicator3);
		buttonRunning = (Button)layoutRunning.findViewById(R.id.btn_start_stop);
		tvRunning = (TextView)layoutRunning.findViewById(R.id.txtPartCompleted);

		walkingBar1 = (ProgressBar)layoutWalking.findViewById(R.id.indicator1);
		walkingBar2 = (ProgressBar)layoutWalking.findViewById(R.id.indicator2);
		walkingBar3 = (ProgressBar)layoutWalking.findViewById(R.id.indicator3);
		buttonWalking = (Button)layoutWalking.findViewById(R.id.btn_start_stop);
		tvWalking = (TextView)layoutWalking.findViewById(R.id.txtPartCompleted);

		sittingBar1 = (ProgressBar)layoutSitting.findViewById(R.id.indicator1);
		sittingBar2 = (ProgressBar)layoutSitting.findViewById(R.id.indicator2);
		sittingBar3 = (ProgressBar)layoutSitting.findViewById(R.id.indicator3);
		buttonSitting = (Button)layoutSitting.findViewById(R.id.btn_start_stop);
		tvSitting = (TextView)layoutSitting.findViewById(R.id.txtPartCompleted);

		climbingUpBar1 = (ProgressBar)layoutClimbingUp.findViewById(R.id.indicator1);
		climbingUpBar2 = (ProgressBar)layoutClimbingUp.findViewById(R.id.indicator2);
		climbingUpBar3 = (ProgressBar)layoutClimbingUp.findViewById(R.id.indicator3);
		buttonclimbingUp = (Button)layoutClimbingUp.findViewById(R.id.btn_start_stop);
		tvClimbingUp = (TextView)layoutClimbingUp.findViewById(R.id.txtPartCompleted);

		climbingDownBar1 = (ProgressBar)layoutClimbingDown.findViewById(R.id.indicator1);
		climbingDownBar2 = (ProgressBar)layoutClimbingDown.findViewById(R.id.indicator2);
		climbingDownBar3 = (ProgressBar)layoutClimbingDown.findViewById(R.id.indicator3);
		buttonClimbingDown = (Button)layoutClimbingDown.findViewById(R.id.btn_start_stop);
		tvClimbingDown = (TextView)layoutClimbingDown.findViewById(R.id.txtPartCompleted);

		buttonRunning.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{

				if(v.isSelected()) {
					whichButtonSelected = 0;
					activityTypeForSensor = getActivityType("Running");
					AppLog.e("running buton is not selected");
					enableAllToggleButton();
					v.setSelected(false);
					v.setBackgroundResource(R.drawable.start);
					stopRecordingData();
				} else{
					whichButtonSelected = 1001;
					AppLog.e("running buton is selected");
					disableAllToggleButton();
					v.setEnabled(true);
					v.setSelected(true);
					v.setBackgroundResource(R.drawable.stop);
					startRecordingData();
				}
			}
		});

		buttonWalking.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if(v.isSelected()) {
					whichButtonSelected = 0;
					activityTypeForSensor = getActivityType("Walking");
					AppLog.e("buttonWalking buton is not selected");
					enableAllToggleButton();
					v.setSelected(false);
					v.setBackgroundResource(R.drawable.start);
					stopRecordingData();
				} else{
					whichButtonSelected = 1002;
					AppLog.e("buttonWalking buton is selected");
					disableAllToggleButton();
					v.setEnabled(true);
					v.setSelected(true);
					v.setBackgroundResource(R.drawable.stop);
					startRecordingData();
				}
			}
		});

		buttonSitting.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if(v.isSelected()) {
					whichButtonSelected = 0;
					activityTypeForSensor = getActivityType("Sitting");
					AppLog.e("buttonSitting buton is not selected");
					enableAllToggleButton();
					v.setSelected(false);
					v.setBackgroundResource(R.drawable.start);
					stopRecordingData();
				} else{
					whichButtonSelected = 1003;
					AppLog.e("buttonSitting buton is selected");
					disableAllToggleButton();
					v.setEnabled(true);
					v.setSelected(true);
					v.setBackgroundResource(R.drawable.stop);
					startRecordingData();
				}
			}
		});


		buttonclimbingUp.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if(v.isSelected()) {
					whichButtonSelected = 0;
					activityTypeForSensor = getActivityType("Climb Up");
					AppLog.e("buttonclimbingUp buton is not selected");
					enableAllToggleButton();
					v.setSelected(false);
					v.setBackgroundResource(R.drawable.start);
					stopRecordingData();
				} else{
					whichButtonSelected = 1004;
					AppLog.e("buttonclimbingUp buton is selected");
					disableAllToggleButton();
					v.setEnabled(true);
					v.setSelected(true);
					v.setBackgroundResource(R.drawable.stop);
					startRecordingData();
				}
			}
		});


		buttonClimbingDown.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				if(v.isSelected()) {
					whichButtonSelected = 0;
					activityTypeForSensor = getActivityType("Climb Down");
					AppLog.e("buttonClimbingDown buton is not selected");
					enableAllToggleButton();
					v.setSelected(false);
					v.setBackgroundResource(R.drawable.start);
					stopRecordingData();
				} else{
					whichButtonSelected = 1005;
					AppLog.e("buttonClimbingDown buton is selected");
					disableAllToggleButton();
					v.setEnabled(true);
					v.setSelected(true);
					v.setBackgroundResource(R.drawable.stop);
					startRecordingData();
				}				
			}
		});
	}

	CustomTimer customTimer;
	int totalFrequency;
	private void startRecordingData() {
		if (! MainActivity.this.isRecording)
		{
			resetSensorDataFiller();
			isStartFirstTimeStamp = true;
		}
		HashMap<String, Integer> durFrequncyMap = getDurationAndFrequencyByActivity();
		totalReverseDuration = durFrequncyMap.get("time");// in seconds
		durationToBeStoredOnServer = totalReverseDuration +"";
		totalFrequency = durFrequncyMap.get("frequency");
		totalReadingXYZ = totalReverseDuration * totalFrequency * 60;
		customTimer = new CustomTimer(this, 0, totalFrequency);
		customTimer.setAutomaticCancel(totalReverseDuration * 1000);
		customTimer.start();

		MainActivity.this.isRecording = true;
		MainActivity.this.startdate = Calendar.getInstance().getTime();
		MainActivity.this.enddate = Calendar.getInstance().getTime();
//		duration = "0";
	}

	private void stopRecordingData() {
		//		if (MainActivity.this.isRecording)
		MainActivity.this.enddate = Calendar.getInstance().getTime();
		MainActivity.this.isRecording = false;
		MainActivity.this.isStartFirstTimeStamp = false;
		durationToBeStoredOnServer = countMethodCall / totalFrequency+"";
		countMethodCall = timeCounter = 0;
		customTimer.stop();
		try
		{ 
			Vector<Points> list = (Vector<Points>) MainActivity.this.csvModelList.clone();
			String id = BeanController.getLoginBean().getId();
			if(StringUtils.isEmpty(id) || id.equalsIgnoreCase("0")) {
				id = (String)AppSettings.getPrefernce(MainActivity.this, null, AppSettings.USER_ID, "");
			}

			storeAccelData(id, activityTypeForSensor+"", MainActivity.this.startdate.getTime()+"", MainActivity.this.enddate.getTime()+"", list);
			String logData = sendLoggerData(id, activityTypeForSensor+"", MainActivity.this.startdate.getTime()+"", MainActivity.this.enddate.getTime()+"", list);
			fetchAccelData(id, activityTypeForSensor);
		}
		catch (JSONException localIOException)
		{
			localIOException.printStackTrace();
		}
	}

	private MyAppDbSQL dbAppDbObj;
	private void storeAccelData(String userID, String activityType, String startTime, String endTime, Vector<Points> pointsList) {
		try{
			//			if (this.dbAppDbObj == null) {
			// set database query class object
			this.dbAppDbObj = new MyAppDbSQL(this);
			//			}
			if(durationToBeStoredOnServer.equalsIgnoreCase("0")) {
				AppLog.e("Duration is 0, so no need to send logged data.");
				return;
			}
			// save the data to the database table
			boolean dbOpenResult = this.dbAppDbObj.openDbAdapter();

			if (dbOpenResult) {
				StringBuilder stringBuilder = new StringBuilder(pointBufferFiller);
				//				boolean blIsSuccessful = this.dbAppDbObj.createLoginEntry("1", "manish", "manis@gmail.com", "pass1", "token1", "1");
				//				Cursor mEntryCursor = this.dbAppDbObj.fetchAccelDataListEntry(userID, activityType);
				//				startManagingCursor(mEntryCursor);
				//				String activity_type = mEntryCursor.getString(mEntryCursor.getColumnIndexOrThrow(MyAppDbAdapter.KEY_ACTIVITY_TYPE));
				//				String fetchedPartcompleted = mEntryCursor.getString(mEntryCursor.getColumnIndexOrThrow(MyAppDbAdapter.KEY_PART_COMPLETED));
				//				int fetchedPartcompletedInt = Integer.parseInt(fetchedPartcompleted);
				//				partCompleted = fetchedPartcompletedInt +1+"";// increment by 1 
				boolean blIsSuccessful = this.dbAppDbObj.createAccelDataEntryAutoIncrementPartCompletedColumn(userID, stringBuilder.toString(), startTime, endTime, activityType, durationToBeStoredOnServer, "20","3","3", "0");
				boolean dbCloseResult = this.dbAppDbObj.closeDbAdapter();
				if (!dbCloseResult)
					throw new Exception(
							"The database was not successfully closed.");
				if (blIsSuccessful == false) {
					//                "There was an issue, and the register entry data was not created.");
				} else {
					// bgh 08/26/2010 v1.03 - get the position of the list entry
					// that was just created
				}
			}
		}catch (Exception e) {
			AppLog.e(e.getMessage());
		}
	}
	Cursor mEntryCursorAccel;
	private void fetchAccelData(String userID, String activityType) {
		HashMap<String, String> hashMap = new HashMap<String, String>();
		try{

			//			if (this.dbAppDbObj == null) {
			// set database query class object
			this.dbAppDbObj = new MyAppDbSQL(this);
			//			}

			// save the data to the database table
			boolean dbOpenResult = this.dbAppDbObj.openDbAdapter();

			if (dbOpenResult) {
				mEntryCursorAccel = this.dbAppDbObj.fetchAccelDataListEntry(userID, null);
				boolean dbCloseResult = this.dbAppDbObj.closeDbAdapter();

				this.startManagingCursor(mEntryCursorAccel);
				if (!dbCloseResult)
					throw new Exception("The database was not successfully closed.");

				if(mEntryCursorAccel.moveToFirst()) {
					do {
						String id = mEntryCursorAccel.getString(mEntryCursorAccel.getColumnIndexOrThrow(MyAppDbAdapter.KEY_ROWID));
						String uID = mEntryCursorAccel.getString(mEntryCursorAccel.getColumnIndexOrThrow(MyAppDbAdapter.KEY_USER_ID));
						//						String name = mEntryCursor.getString(mEntryCursor.getColumnIndexOrThrow(MyAppDbAdapter.KEY_NAME));
						String activity_type = mEntryCursorAccel.getString(mEntryCursorAccel.getColumnIndexOrThrow(MyAppDbAdapter.KEY_ACTIVITY_TYPE));
						String duration_stored = mEntryCursorAccel.getString(mEntryCursorAccel.getColumnIndexOrThrow(MyAppDbAdapter.KEY_DURATION));
						String part_completed = mEntryCursorAccel.getString(mEntryCursorAccel.getColumnIndexOrThrow(MyAppDbAdapter.KEY_PART_COMPLETED));
						//							String name = mEntryCursor.getString(mEntryCursor.getColumnIndexOrThrow(MyAppDbAdapter.KEY_ROWID));
						//							String name = mEntryCursor.getString(mEntryCursor.getColumnIndexOrThrow(MyAppDbAdapter.KEY_ROWID));
						hashMap.put(activity_type, part_completed);
						AppLog.e("AccelTABLE DATA: "+duration_stored + "  ActivityType : "+activity_type);
						progreesFiller(activity_type, part_completed);
					} while (mEntryCursorAccel.moveToNext());
					if(mEntryCursorAccel != null)
					{
						stopManagingCursor(mEntryCursorAccel);
					}
				}
			}// end if (blIsSuccessful == false)
			AppLog.e("part Completed : "+hashMap.toString());
		}catch (Exception e) {
			AppLog.e(e.getMessage());
		}
	}

	private void progreesFiller(String activityType, String partCompletedString) {
		int partCompleted = Integer.parseInt(partCompletedString);
		if(activityType.equalsIgnoreCase("1")) {
			progressFillerRunning(partCompleted);
		} else if(activityType.equalsIgnoreCase("2")) {
			progressFillerWalking(partCompleted);
		} else if(activityType.equalsIgnoreCase("3")) {
			progressFillerSitting(partCompleted);
		} else if(activityType.equalsIgnoreCase("4")) {
			// because sitting and standin are same 
		} else if(activityType.equalsIgnoreCase("5")) {
			progressFillerClimbingUp(partCompleted);
		} else if(activityType.equalsIgnoreCase("6")) {
			progressFillerClimbingDown(partCompleted);
		}
	}

	private void progressFillerRunning(int partCompleted) {
		switch (partCompleted) {
		case 0:
			runningBar1.setProgress(1);
			runningBar2.setProgress(1);
			runningBar3.setProgress(1);
			tvRunning.setText("0/3");
		case 1:
			runningBar1.setProgress(100);
			tvRunning.setText(partCompleted+"/3");
			break;
		case 2:
			runningBar1.setProgress(100);
			runningBar2.setProgress(100);
			tvRunning.setText(partCompleted+"/3");
			break;
		case 3:
			runningBar1.setProgress(100);
			runningBar2.setProgress(100);
			runningBar3.setProgress(100);
			tvRunning.setText(partCompleted+"/3");

			break;

		default:
			runningBar1.setProgress(100);
			runningBar2.setProgress(100);
			runningBar3.setProgress(100);
			tvRunning.setText("3/3");
			break;
		}
	}

	private void progressFillerWalking(int partCompleted) {
		switch (partCompleted) {
		case 0:
			walkingBar1.setProgress(1);
			walkingBar2.setProgress(1);
			walkingBar3.setProgress(1);
			tvWalking.setText("0/3");
			break;
		case 1:
			walkingBar1.setProgress(100);
			tvWalking.setText(partCompleted+"/3");
			break;
		case 2:
			walkingBar1.setProgress(100);
			walkingBar2.setProgress(100);
			tvWalking.setText(partCompleted+"/3");
			break;
		case 3:
			walkingBar1.setProgress(100);
			walkingBar2.setProgress(100);
			walkingBar3.setProgress(100);
			tvWalking.setText(partCompleted+"/3");
			break;

		default:
			walkingBar1.setProgress(100);
			walkingBar2.setProgress(100);
			walkingBar3.setProgress(100);
			tvWalking.setText("3/3");
			break;
		}
	}

	private void progressFillerSitting(int partCompleted) {
		switch (partCompleted) {
		case 0:
			sittingBar1.setProgress(1);
			sittingBar2.setProgress(1);
			sittingBar3.setProgress(1);
			tvSitting.setText("0/3");
			break;
		case 1:
			sittingBar1.setProgress(100);
			tvSitting.setText(partCompleted+"/3");
			break;
		case 2:
			sittingBar1.setProgress(100);
			sittingBar2.setProgress(100);
			tvSitting.setText(partCompleted+"/3");
			break;
		case 3:
			sittingBar1.setProgress(100);
			sittingBar2.setProgress(100);
			sittingBar3.setProgress(100);
			tvSitting.setText(partCompleted+"/3");
			break;

		default:
			sittingBar1.setProgress(100);
			sittingBar2.setProgress(100);
			sittingBar3.setProgress(100);
			tvSitting.setText("3/3");
			break;
		}
	}

	private void progressFillerClimbingUp(int partCompleted) {
		switch (partCompleted) {
		case 0:
			climbingUpBar1.setProgress(1);
			climbingUpBar2.setProgress(1);
			climbingUpBar3.setProgress(1);
			tvClimbingUp.setText("0/3");
			break;
		case 1:
			climbingUpBar1.setProgress(100);
			tvClimbingUp.setText(partCompleted+"/3");
			break;
		case 2:
			climbingUpBar1.setProgress(100);
			climbingUpBar2.setProgress(100);
			tvClimbingUp.setText(partCompleted+"/3");
			break;
		case 3:
			climbingUpBar1.setProgress(100);
			climbingUpBar2.setProgress(100);
			climbingUpBar3.setProgress(100);
			tvClimbingUp.setText(partCompleted+"/3");
			break;

		default:
			climbingUpBar1.setProgress(100);
			climbingUpBar2.setProgress(100);
			climbingUpBar3.setProgress(100);
			tvClimbingUp.setText("3/3");
			break;
		}
	}

	private void progressFillerClimbingDown(int partCompleted) {
		switch (partCompleted) {
		case 0:
			climbingDownBar1.setProgress(1);
			climbingDownBar1.setProgress(1);
			climbingDownBar1.setProgress(1);
			tvClimbingDown.setText("0/3");
			break;
		case 1:
			climbingDownBar1.setProgress(100);
			tvClimbingDown.setText(partCompleted+"/3");
			break;
		case 2:
			climbingDownBar1.setProgress(100);
			climbingDownBar2.setProgress(100);
			tvClimbingDown.setText(partCompleted+"/3");
			break;
		case 3:
			climbingDownBar1.setProgress(100);
			climbingDownBar2.setProgress(100);
			climbingDownBar3.setProgress(100);
			tvClimbingDown.setText(partCompleted+"/3");
			break;

		default:
			climbingDownBar1.setProgress(100);
			climbingDownBar2.setProgress(100);
			climbingDownBar3.setProgress(100);
			tvClimbingDown.setText("3/3");
			break;
		}
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

	@Override
	public void exceuteOnUIThread(boolean isLastCall) {
		this.isLastCall = isLastCall;
		runOnUiThread(this);
	}

	@Override
	public void run() {
		AppLog.e("totalCall Of RUN method"+ ++countMethodCall);
		if(isLastCall) {
			AppLog.e("Last call : " + durationToBeStoredOnServer);
			if(timeCounter <= 0) {
				timeCounter = totalFrequency;
				this.txtlogtime.setText("" + --totalReverseDuration + " seconds");
				plotTheGraph();
			}
			timeCounter--;
			isStartFirstTimeStamp = false;
			isRecording = false;
			peroformButtonClick();
			return;
		}
		if(selectedSpeed == 0) {
			AppLog.e(df3.format(sensorX) + COMMA + df3.format(sensorY) + COMMA + df3.format(sensorZ) + COMMA);
			csvModelList.add(new Points(sensorX,sensorY,sensorZ));
			pointBufferFiller.append(df3.format(sensorX) + COMMA + df3.format(sensorY) + COMMA + df3.format(sensorZ) + COMMA);

			if(isStartFirstTimeStamp) {
				MainActivity.this.startdate = Calendar.getInstance().getTime();
				AppLog.e("isStartFirstTimeStamp");
				// start reading from 1st second so leave the 0th reading. 
				resetSensorDataFiller();
				isStartFirstTimeStamp = false;
			}
			if(isRecording) {
				//				double d = (sensorTimeStamp - this.starttime.doubleValue()) / 1000000000.0D;
				//				AppLog.e(d+"");
				//				duration = df0.format(d)+"";
				//				int reverseTimer = 60 - Integer.parseInt(duration);// to show reverse time from 60.
				if(timeCounter <= 0) {
					timeCounter = totalFrequency;
					this.txtlogtime.setText("" + --totalReverseDuration + " seconds");	
					plotTheGraph();
				}
				timeCounter--;
			}
		} 
	}

	private HashMap<String, Integer> getDurationAndFrequencyByActivity() {
		HashMap<String, Integer> durationFrequncyMap = new HashMap<String, Integer>();
		String id = BeanController.getLoginBean().getId();
		if(StringUtils.isEmpty(id) || id.equalsIgnoreCase("0")) {
			id = (String)AppSettings.getPrefernce(this, null, AppSettings.USER_ID, "");
		}

		ArrayList<HashMap<String, String>> mapData = fetchSettingData(id, activityTypeForSensor);
		HashMap<String, String> frequencyMap = mapData.get(0);
		HashMap<String, String> timeMap = mapData.get(1);
		String[] frequencyString = frequencyMap.get(activityTypeForSensor).split("\\ ");
		String timeString = timeMap.get(activityTypeForSensor);

		int time = Integer.parseInt(timeString)*60;
		int frequency = Integer.parseInt(frequencyString[0]);
		if(time == 0) time = 60;// default 1 minute
		if(frequency == 0) frequency = 1; //default 1 heartz

		AppLog.e("Fetched total time: "+timeString +"frequency : "+frequencyString);
		durationFrequncyMap.put("frequency", frequency);
		durationFrequncyMap.put("time", time);
		return durationFrequncyMap;
	}

	private Cursor mEntryCursor;
	private ArrayList<HashMap<String, String>> fetchSettingData(String userID, String activityType) {
		ArrayList<HashMap<String, String>> arrayList = new ArrayList<HashMap<String,String>>();
		HashMap<String, String> hashMapFrequency = new HashMap<String, String>();
		HashMap<String, String> hashMapTotalTime = new HashMap<String, String>();
		try{

			//			if (this.dbAppDbObj == null) {
			// set database query class object
			this.dbAppDbObj = new MyAppDbSQL(this);
			//			}

			// save the data to the database table
			boolean dbOpenResult = this.dbAppDbObj.openDbAdapter();

			if (dbOpenResult) {
				mEntryCursor = this.dbAppDbObj.fetchAccelSettingEntry(userID, activityType);
				boolean dbCloseResult = this.dbAppDbObj.closeDbAdapter();

				this.startManagingCursor(mEntryCursor);
				if (!dbCloseResult)
					throw new Exception("The database was not successfully closed.");
				if(mEntryCursor.moveToFirst()) {
					do {
						String id = mEntryCursor.getString(mEntryCursor.getColumnIndexOrThrow(MyAppDbAdapter.KEY_ROWID));
						String uID = mEntryCursor.getString(mEntryCursor.getColumnIndexOrThrow(MyAppDbAdapter.KEY_USER_ID));
						String activity_type = mEntryCursor.getString(mEntryCursor.getColumnIndexOrThrow(MyAppDbAdapter.KEY_ACTIVITY_TYPE));
						String storedFrequency = mEntryCursor.getString(mEntryCursor.getColumnIndexOrThrow(MyAppDbAdapter.KEY_SETTING_ACTIVITY_FREQUENCY));
						String storedTotalTime = mEntryCursor.getString(mEntryCursor.getColumnIndexOrThrow(MyAppDbAdapter.KEY_SETTING_TOTAL_TIME_PER_ACTIVITY));
						hashMapFrequency.put(activity_type, storedFrequency);
						hashMapTotalTime.put(activity_type, storedTotalTime);
						AppLog.e("storedFrequency : "+storedFrequency + "  ActivityType : "+activity_type + "  TotalTime : "+storedTotalTime);
						//						progreesFiller(activity_type, activity_type_value);
					} while (mEntryCursor.moveToNext());

					arrayList.add(hashMapFrequency);
					arrayList.add(hashMapTotalTime);
					if(mEntryCursor != null) {
						stopManagingCursor(mEntryCursor);
					}
				}
			}// end if (blIsSuccessful == false)
			AppLog.e("part Completed : "+hashMapFrequency.toString());
		}catch (Exception e) {
			AppLog.e(e.getMessage());
		}
		return arrayList;
	}

	private void plotTheGraph() {
		xList.add(sensorX);
		yList.add(sensorY);
		zList.add(sensorZ);
		mGraph.initData(xList, yList, zList);
		mGraph.setProperties();
		if (!init) {
			view = mGraph.getGraph();
			mChartContainer.addView(view);
			init = true;
		} else {
			mChartContainer.removeView(view);
			view = mGraph.getGraph();
			mChartContainer.addView(view);
		}

	}

	@Override
	public void startManagingCursor(Cursor c) {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
			super.startManagingCursor(c); 
		}
	}
}
