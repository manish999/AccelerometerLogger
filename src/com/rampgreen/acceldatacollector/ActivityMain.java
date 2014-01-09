package com.rampgreen.acceldatacollector;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.rampgreen.acceldatacollector.csv.Points;
import com.rampgreen.acceldatacollector.util.AppLog;
import com.rampgreen.acceldatacollector.util.AppSettings;

import java.io.File;
import java.io.FileWriter;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ActivityMain extends Activity  implements SensorEventListener, Listener, ErrorListener
{
	private static final String COMMA = "," ;
	private SensorManager mSensorManager;
	private Sensor mAccelerometer;
	private StringBuilder pointBuffer = new StringBuilder();// seperated by comma

	private Button cmdrecord;
	private Button cmdresettime;
	private Button cmdstop;
	private TextView txtlogtime;
	private TextView txtsensordata;
	private String activityType = Constants.ACCEL_ACTIVITY_RUNNING;
	Date enddate;
	FileWriter writer;
	File dir;
	File f;
	int filesave_choice;
	Date startdate;
	Double starttime = Double.valueOf(0.0D);
	Vector<Double> times = new Vector();
	DecimalFormat df3 = new DecimalFormat("#0.0000");
	DecimalFormat df6 = new DecimalFormat("#0.000000");
	DecimalFormat df0 = new DecimalFormat("#0");

	boolean isFirstRecording = true;
	boolean isRecording = false;
	boolean isStartFirstTimeStamp = false;
	String duration;

	Vector<Float> valuesX = new Vector();
	Vector<Float> valuesY = new Vector();
	Vector<Float> valuesZ = new Vector();
	
//	final ScheduledExecutorService exec = Executors.newScheduledThreadPool(1);

	Vector<Points> csvModelList = new Vector<Points>();
	private boolean isActive;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.logging);

		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

//		mSensorManager.registerListener(this, mAccelerometer,
//				SensorManager.SENSOR_DELAY_FASTEST);
		// setting listener
		((RadioGroup) findViewById(R.id.toggleGroup)).setOnCheckedChangeListener(ToggleListener);

		this.txtsensordata = ((TextView)findViewById(R.id.txtlogdata));
		this.txtlogtime = ((TextView)findViewById(R.id.txtlogtime));
		this.cmdrecord = ((Button)findViewById(R.id.cmdrecord));
		this.cmdstop = ((Button)findViewById(R.id.cmdstopsave));
		this.cmdresettime = ((Button)findViewById(R.id.cmdresettime));

		this.startdate = Calendar.getInstance().getTime();
		//		    this.imggraph = ((ImageView)findViewById(2131230749));
		reset_variables();
		setListener();
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


		//		this.sensorManager = ((SensorManager)getSystemService("sensor"));
		//		this.sensor = this.sensorManager.getDefaultSensor(this.sensortype);
		//		this.sensorManager.registerListener(this, this.sensor, 0);
		//		if (this.sensor == null)
		//		{
		//			Toast.makeText(getApplicationContext(), "Invalid sensor.", 0).show();
		//			finish();
		//		}

	}
	int selectedSpeed = 0;
	@Override
	protected void onResume()
	{
		super.onResume();
		String sensorSpeedSetting = (String)AppSettings.getPrefernce(this, null, AppSettings.SPEED_SENSOR, "0");
		selectedSpeed = Integer.valueOf(sensorSpeedSetting).intValue();
		switch (selectedSpeed) {
		case 0:
			mSensorManager.registerListener(this, mAccelerometer,
					SensorManager.SENSOR_DELAY_NORMAL);
			
//			timer.schedule(new TimerTask() {
//				@Override
//				public void run() {
//					activateSensor();
//				    }
//				},0,1000);//Update text every second
			
//			exec.schedule(new Runnable(){
//			    @Override
//			    public void run(){
//			        activateSensor();
//			    }
//			}, 1, TimeUnit.SECONDS);
			
			break;
		case 1:
			mSensorManager.registerListener(this, mAccelerometer,
					SensorManager.SENSOR_DELAY_NORMAL);
//			timer.cancel();
//			isActive = true;
//			exec.shutdownNow();
			break;
		case 2:
			mSensorManager.registerListener(this, mAccelerometer,
					SensorManager.SENSOR_DELAY_UI);
//			timer.cancel();
//			isActive = true;
			break;
		case 3:
			mSensorManager.registerListener(this, mAccelerometer,
					SensorManager.SENSOR_DELAY_GAME);
//			timer.cancel();
//			isActive = true;
			break;
		case 4:
			mSensorManager.registerListener(this, mAccelerometer,
					SensorManager.SENSOR_DELAY_FASTEST);
//			timer.cancel();
//			isActive = true;
			break;
		case 5:

			break;

		default:
			break;
		}
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		mSensorManager.unregisterListener(this, mAccelerometer);
//		timer.cancel();
//		isActive = true;
//		exec.shutdownNow();
	}

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

		case R.id.action_settings:
			intent = new Intent(this, PreferencesActivity.class);
			startActivity(intent);
			break;
		case R.id.action_logout:
			AppSettings.setPreference(this, null, AppSettings.ACCESS_TOKEN, "");
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

	static final RadioGroup.OnCheckedChangeListener ToggleListener = new RadioGroup.OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(final RadioGroup radioGroup, final int i) {
			LinearLayout toggleButtonContainer;
			for (int index = 0; index < radioGroup.getChildCount(); index++) {
				toggleButtonContainer =  (LinearLayout) radioGroup.getChildAt(index);
				for (int j = 0; j < toggleButtonContainer.getChildCount(); j++) {
					final ToggleButton view = (ToggleButton) toggleButtonContainer.getChildAt(j);
					if(view.getId() == i) {
						view.setChecked(true);	
					} else {
						view.setChecked(false);
					}
					boolean shouldcheck ;
					//					if(view.isChecked()) {
					//						shouldcheck = true;
					//					} else {
					shouldcheck = view.getId() == i;
					//					}

					//					view.setChecked(true);
				}
			}
		}
	};

	public void onToggle(View view) {
		LinearLayout linearLayout = (LinearLayout)view.getParent();
		((ToggleButton)view).setChecked(true);
		((RadioGroup)linearLayout.getParent()).check(view.getId());
		// app specific stuff ..
		String toggleBtnText = ((ToggleButton)view).getText().toString();
		AppLog.e(toggleBtnText);
		activityType = getActivityType(toggleBtnText);
	}

	private void reset_variables()
	{
		csvModelList.clear();
		pointBuffer.setLength(0);// more faster than insert and allocate new one

		//	    this.valuesX.clear();
		//	    this.valuesY.clear();
		//	    this.valuesZ.clear();
		//	    this.valuesmag.clear();
		//	    this.times.clear();
		//	    this.valuesX.add(Float.valueOf(0.0F));
		//	    this.valuesY.add(Float.valueOf(0.0F));
		//	    this.valuesZ.add(Float.valueOf(0.0F));
		//	    this.valuesmag.add(Float.valueOf(0.0F));
		//	    this.times.add(Double.valueOf(0.0D));

	}

	private long startSystemTime;
	private void setListener()
	{
		this.cmdrecord.setOnClickListener(new View.OnClickListener()
		{
			

			@Override
			public void onClick(View v)
			{
				ActivityMain localLoggingActivity;
				boolean bool2;
				
				if (!ActivityMain.this.isRecording)
				{
					//					ActivityMain.this.cmdrecord.setBackgroundResource(2130837507);
					//					ActivityMain.this.cmdrecord.setText("Pause");
					reset_variables();
					startSystemTime = System.currentTimeMillis();
					ActivityMain.this.cmdstop.setEnabled(true);
					ActivityMain.this.cmdresettime.setEnabled(true);
					ActivityMain.this.cmdrecord.setEnabled(false);
					ActivityMain.this.cmdrecord.setBackgroundResource(R.drawable.button_disabled);
					isStartFirstTimeStamp = true;
					if (ActivityMain.this.isFirstRecording)
					{
						ActivityMain.this.isFirstRecording = false;
						//		              ActivityMain.this.writeheaders();
					}
					localLoggingActivity = ActivityMain.this;
					//					boolean bool1 = ActivityMain.this.isRecording;
					//					bool2 = false;
					//					if (!bool1)
					//						break label148;
				}
				ActivityMain.this.isRecording = true;
				//				while (true)
				//				{
				//					localLoggingActivity.isRecording = bool2;
				//					return;
				ActivityMain.this.enddate = Calendar.getInstance().getTime();
				//				ActivityMain.this.cmdrecord.setBackgroundResource(R.drawable.button_record);
				//				ActivityMain.this.cmdrecord.setText("Resume");
				//					break;
				//					label148: bool2 = true;
				//				}
			}
		});

		this.cmdstop.setOnClickListener(new View.OnClickListener()
		{

			public void onClick(View paramAnonymousView)
			{

				//write json and send it to server

				if (ActivityMain.this.isRecording)
					ActivityMain.this.enddate = Calendar.getInstance().getTime();
				ActivityMain.this.isRecording = false;
				ActivityMain.this.cmdrecord.setEnabled(true);
				ActivityMain.this.cmdrecord.setBackgroundResource(R.drawable.button_up);
				ActivityMain.this.cmdrecord.setText("Start");
				ActivityMain.this.cmdstop.setEnabled(false);
				ActivityMain.this.cmdresettime.setEnabled(false);
				ActivityMain.this.isFirstRecording = true;
				if (ActivityMain.this.isStartFirstTimeStamp)
				{
					ActivityMain.this.isStartFirstTimeStamp = false;
					//		              ActivityMain.this.writeheaders();
				}
				try
				{ 
					Vector<Points> list = (Vector<Points>) ActivityMain.this.csvModelList.clone();
					String id = BeanController.getLoginBean().getId();

					String logData = sendLoggerData(id, activityType+"", ActivityMain.this.startdate.getTime()+"", ActivityMain.this.enddate.getTime()+"", list);



					//					ActivityMain.this.writer.append(id+ "," + ActivityMain.this.startdate+ ","+ActivityMain.this.enddate+"," +1+","+3000+",");
					//					for (Points points : list)
					//					{
					//						ActivityMain.this.writer.append(points.toString()+ ",");
					//					}
					//write csv file 
					//					Vector<Points> list = (Vector<Points>) ActivityMain.this.csvModelList.clone();
					//					String id = BeanController.getLoginBean().getId();
					//					ActivityMain.this.writer.append(id+ "," + ActivityMain.this.startdate+ ","+ActivityMain.this.enddate+"," +1+","+3000+",");
					//					for (Points points : list)
					//					{
					//						ActivityMain.this.writer.append(points.toString()+ ",");
					//					}
					//					ActivityMain.this.writer.append("\n###end");
					//
					//					ActivityMain.this.writer.append("\nData logging started on: " + ActivityMain.this.startdate.toString() + "\nData logging stopped on: " + ActivityMain.this.enddate.toString() + "\n");
					//					ActivityMain.this.writer.flush();
					//					ActivityMain.this.writer.close();
					//					CharSequence[] arrayOfCharSequence = { "Save to text file", "Save to CSV file" };
					//					ActivityMain.this.filesave_choice = 0;
					//					new AlertDialog.Builder(ActivityMain.this).setIcon(2130837510).setTitle("Save data?").setPositiveButton("OK", new DialogInterface.OnClickListener()
					//					{
					//						public void onClick(DialogInterface paramAnonymous2DialogInterface, int paramAnonymous2Int)
					//						{
					//							SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
					//							File localFile = new File(ActivityMain.this.dir, "SLog-" + localSimpleDateFormat.format(ActivityMain.this.enddate) + ".txt");
					//							if (ActivityMain.this.filesave_choice == 0)
					//								localFile = new File(ActivityMain.this.dir, "SLog-" + localSimpleDateFormat.format(ActivityMain.this.enddate) + ".txt");
					//							ActivityMain.this.f.renameTo(localFile);
					//							ActivityMain.this.f = new File(ActivityMain.this.dir, "temp.txt");
					//							try
					//							{
					//								ActivityMain.this.f.createNewFile();
					//								ActivityMain.this.writer = new FileWriter(ActivityMain.this.f);
					//								Toast.makeText(ActivityMain.this.getApplicationContext(), "Sensor data successfully saved to " + ActivityMain.this.dir.getPath() + "/", 1).show();
					//								return;
					//								//									if (ActivityMain.this.filesave_choice != 1)
					//								//									localFile = new File(ActivityMain.this.dir, "SLog-" + localSimpleDateFormat.format(ActivityMain.this.enddate) + ".csv");
					//							}
					//							catch (IOException localIOException)
					//							{
					//								localIOException.printStackTrace();
					//							}
					//						}
					//					}).setNegativeButton("Cancel", new DialogInterface.OnClickListener()
					//					{
					//						public void onClick(DialogInterface paramAnonymous2DialogInterface, int paramAnonymous2Int)
					//						{
					//							ActivityMain.this.f.delete();
					//							ActivityMain.this.f = new File(ActivityMain.this.dir, "temp.txt");
					//							try
					//							{
					//								ActivityMain.this.f.createNewFile();
					//								ActivityMain.this.writer = new FileWriter(ActivityMain.this.f);
					//								Toast.makeText(ActivityMain.this.getApplicationContext(), "Sensor data was not saved.", 0).show();
					//								return;
					//							}
					//							catch (IOException localIOException)
					//							{
					//								while (true)
					//									localIOException.printStackTrace();
					//							}
					//						}
					//					}).setSingleChoiceItems(arrayOfCharSequence, 0, new DialogInterface.OnClickListener()
					//					{
					//						public void onClick(DialogInterface paramAnonymous2DialogInterface, int paramAnonymous2Int)
					//						{
					//							ActivityMain.this.filesave_choice = paramAnonymous2Int;
					//						}
					//					}).show();
					//					return;
				}
				catch (JSONException localIOException)
				{
					localIOException.printStackTrace();
				}
			}
		});

		this.cmdresettime.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View paramAnonymousView)
			{
				ActivityMain.this.reset_variables();
				ActivityMain.this.startdate = Calendar.getInstance().getTime();
				ActivityMain.this.enddate = Calendar.getInstance().getTime();
				ActivityMain.this.isStartFirstTimeStamp = true;
			}
		});
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1)
	{

	}

	@Override
	public void onSensorChanged(SensorEvent sensorEvent)
	{
//		if (isActive) {
//            numSamples++;
		if(selectedSpeed == 0) {
            long now = System.currentTimeMillis();
            if (now >= startSystemTime + 1000) {
//                samplingRate = numSamples / ((now - startSystemTime) / 1000.0);
            	startSystemTime = now;
//                isActive = false;
//                accelerometerTest.displayRates();
            	

    			float x =  sensorEvent.values[0];
    			float y =  sensorEvent.values[1];
    			float z =  sensorEvent.values[2];
    			long timestamp = sensorEvent.timestamp;
    			// adding the data to the list
    			csvModelList.add(new Points(x,y,z));

    			pointBuffer.append(df3.format(x) + COMMA + df3.format(y) + COMMA + df3.format(z) + COMMA);
    			if(isStartFirstTimeStamp) {
    				this.starttime = Double.valueOf(sensorEvent.timestamp);
    				isStartFirstTimeStamp = false;
    			}
    			if(isRecording) {
    				double d = (sensorEvent.timestamp - this.starttime.doubleValue()) / 1000000000.0D;
    				duration = df0.format(d)+"";
    				this.txtlogtime.setText("Time : " + this.df3.format(d) + "sec");
    			}
    			this.txtsensordata.setText(Html.fromHtml("<b><font color=\"red\">X axis</font></b> : " + this.df3.format(x) + " m/s<sup>2</sup>" + "<br><b><font color=\"green\">Y axis</font></b> : " + this.df3.format(y) + " m/s<sup>2</sup>" + "<br><b><font color=\"blue\">Z axis</font></b> : " + this.df3.format(z) + " m/s<sup>2</sup>"));
//    			if(timer.isTerminated()) {
//    				isActive = true;
    			
            }
        } else {
        	float x =  sensorEvent.values[0];
			float y =  sensorEvent.values[1];
			float z =  sensorEvent.values[2];
			long timestamp = sensorEvent.timestamp;
			// adding the data to the list
			csvModelList.add(new Points(x,y,z));

			pointBuffer.append(df3.format(x) + COMMA + df3.format(y) + COMMA + df3.format(z) + COMMA);
			if(isStartFirstTimeStamp) {
				this.starttime = Double.valueOf(sensorEvent.timestamp);
				isStartFirstTimeStamp = false;
			}
			if(isRecording) {
				double d = (sensorEvent.timestamp - this.starttime.doubleValue()) / 1000000000.0D;
				duration =d+"";
				this.txtlogtime.setText("Time : " + this.df3.format(d) + "sec");
			}
			this.txtsensordata.setText(Html.fromHtml("<b><font color=\"red\">X axis</font></b> : " + this.df3.format(x) + " m/s<sup>2</sup>" + "<br><b><font color=\"green\">Y axis</font></b> : " + this.df3.format(y) + " m/s<sup>2</sup>" + "<br><b><font color=\"blue\">Z axis</font></b> : " + this.df3.format(z) + " m/s<sup>2</sup>"));
        }
		
		
//		if (isActive) {
//			float x =  sensorEvent.values[0];
//			float y =  sensorEvent.values[1];
//			float z =  sensorEvent.values[2];
//			long timestamp = sensorEvent.timestamp;
//			// adding the data to the list
//			csvModelList.add(new Points(x,y,z));
//
//			pointBuffer.append(df3.format(x) + COMMA + df3.format(y) + COMMA + df3.format(z) + COMMA);
//			if(isStartFirstTimeStamp) {
//				this.starttime = Double.valueOf(sensorEvent.timestamp);
//				isStartFirstTimeStamp = false;
//			}
//			if(isRecording) {
//				double d = (sensorEvent.timestamp - this.starttime.doubleValue()) / 1000000000.0D;
//				duration =d+"";
//				this.txtlogtime.setText("Time : " + this.df3.format(d) + "sec");
//			}
//			this.txtsensordata.setText(Html.fromHtml("<b><font color=\"red\">X axis</font></b> : " + this.df3.format(x) + " m/s<sup>2</sup>" + "<br><b><font color=\"green\">Y axis</font></b> : " + this.df3.format(y) + " m/s<sup>2</sup>" + "<br><b><font color=\"blue\">Z axis</font></b> : " + this.df3.format(z) + " m/s<sup>2</sup>"));
////			if(timer.isTerminated()) {
//				isActive = true;
//			} else {
//				isActive = false;
//			}
//		}
	}

	private String sendLoggerData(String userID, String activityType, String startTime, String endTime, Vector<Points> pointsList) throws JSONException 
	{
		// Here we convert Java Object to JSON 
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("user_id", userID); // Set the first name/pair 
		jsonObj.put("activity_type", activityType);

		//			        JSONObject jsonAdd = new JSONObject(); // we need another object to store the address
		jsonObj.put("start_time_stamp", startTime);
		jsonObj.put("end_time_stamp", endTime);
		jsonObj.put("duration", duration);
		StringBuilder stringBuilder = new StringBuilder(pointBuffer);
		//		for (Points points : pointsList)
		//		{
		//			stringBuilder.append(points.getX() + "," + points.getY() + "," +points.getZ());
		//			stringBuilder.append(",");
		//		} 
		//		
		//		stringBuilder.deleteCharAt(pointsList.size()-1);
		jsonObj.put("points", stringBuilder.toString());

		// get network queue and add request to the queue
		MyRequestQueue queue = MyVolley.getRequestQueue();
		Map<String, String> loginParam = QueryHelper.createSensorDataQuery(jsonObj.toString());
		CustomRequest customRequest = new CustomRequest(Method.POST,
				Constants.URL_WEB_SERVICE, loginParam,
				ActivityMain.this, ActivityMain.this);
		//		showLoadingBar();
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
		//			        for (PhoneNumber pn : person.getPhoneList() ) {
		//			            JSONObject pnObj = new JSONObject();
		//			            pnObj.put("num", pn.getNumber());
		//			            pnObj.put("type", pn.getType());
		//			            jsonArr.put(pnObj);
		//			        }

		//		jsonObj.put("points", jsonArr);

		// get network queue and add request to the queue
		//		RequestQueue queue = MyVolley.getVollyRequestQueue();
		//		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Method.POST, Constants.URL_WEB_SERVICE, jsonObj, this, this);
		//		Map<String, String> loginParam = QueryHelper.createSensorDataQuery(userID,activityType, startTime, endTime, pointsList);
		//		StringRequest stringRequest = new StringRequest(Method.POST, Constants.URL_WEB_SERVICE, ActivityMain.this, (ErrorListener) ActivityMain.this);
		//		CustomRequest customRequest = new CustomRequest(Method.POST,
		//				Constants.URL_WEB_SERVICE, loginParam,
		//				LoginActivity.this, LoginActivity.this);

		//		queue.add(jsonObjectRequest);

		Log.e("JSON REQUEST String********* ", jsonObj.toString()+"**********");
		return jsonObj.toString();

	}
	//	catch(JSONException ex) {
	//		ex.printStackTrace();
	//	}

	@Override
	public void onResponse(Object response)
	{
		reset_variables();
		AppLog.e("Volly success"+response.toString());
		// TODO Auto-generated method stub

	}

	@Override
	public void onErrorResponse(VolleyError error)
	{
		reset_variables();
		AppLog.e("Volly error"+error.getMessage());
	}

	private String getActivityType(String activityType) {
		AppLog.e(activityType);
		String[] arrayOfString = getResources().getStringArray(R.array.date_array);
		if(activityType.equalsIgnoreCase("Running")) {
			return Constants.ACCEL_ACTIVITY_RUNNING;
		} else if(activityType.equalsIgnoreCase("Walking")) {
			return Constants.ACCEL_ACTIVITY_WALKING;
		} else if(activityType.equalsIgnoreCase("Sitting")) {
			return Constants.ACCEL_ACTIVITY_SITTING;
		} else if(activityType.equalsIgnoreCase("Sleeping")) {
			return Constants.ACCEL_ACTIVITY_SLEEPING;
		} else if(activityType.equalsIgnoreCase("Climb Up")) {
			return Constants.ACCEL_ACTIVITY_CLIMBING_UP;
		} else if(activityType.equalsIgnoreCase("Climb Down")) {
			return Constants.ACCEL_ACTIVITY_CLIMBING_DOWN;
		}
		return Constants.ACCEL_ACTIVITY_RUNNING;
	}
	
	private void activateSensor() {
		isActive = true;
	}
	Timer timer = new Timer();
	private void updateDisplay() {
	    
	    timer.schedule(new TimerTask() {

	@Override
	public void run() {
//	         Calendar c = Calendar.getInstance();
//	         mYear = c.get(Calendar.YEAR);
//	         mMonth = c.get(Calendar.MONTH);
//	         mDay = c.get(Calendar.DAY_OF_MONTH);
//	         mHour = c.get(Calendar.HOUR_OF_DAY);
//	         mMinute = c.get(Calendar.MINUTE);
//	         mSecond = c.get(Calendar.SECOND);
//	        cDateDisplay.setText(new StringBuilder()
//	            // Month is 0 based so add 1
//	            .append(mDay).append("/")
//	            .append(mMonth + 1).append("/")
//	            .append(mYear).append(" "));
//	       cTimeDisplay.setText(
//	             new StringBuilder()
//	                .append(pad(mHour)).append(":")
//	                .append(pad(mMinute)).append(":").append(pad(mSecond)));
		activateSensor();
	    }
	},0,1000);//Update text every second

	}

}
