package com.rampgreen.acceldatacollector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import com.rampgreen.acceldatacollector.csv.Points;
import com.rampgreen.acceldatacollector.db.MyAppDbAdapter;
import com.rampgreen.acceldatacollector.db.MyAppDbSQL;
import com.rampgreen.acceldatacollector.util.AppLog;
import com.rampgreen.acceldatacollector.util.AppSettings;
import com.rampgreen.acceldatacollector.util.StringUtils;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

public class ActivityConfigure extends Activity implements SeekBar.OnSeekBarChangeListener, OnItemSelectedListener{

	private SeekBar mSeekBarRunning = null;
	private SeekBar mSeekBarWalking = null;
	private SeekBar mSeekBarSitting = null;
	private SeekBar mSeekBarClimbingUp = null;
	private SeekBar mSeekBarClimbingDown = null;

	private Spinner mSpinnerRunning = null;
	private Spinner mSpinnerWalking = null;
	private Spinner mSpinnerSitting = null;
	private Spinner mSpinnerClimbingUp = null;
	private Spinner mSpinnerClimbingDown = null;

	private TextView mTextViewSeekBarRunning = null;
	private TextView mTextViewSeekBarWalking = null;
	private TextView mTextViewSeekBarSitting = null;
	private TextView mTextViewSeekBarClimbingUp = null;
	private TextView mTextViewSeekBarClimbingDown = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.configure_activity);
		initUI();
		initData();
	}

	private void initData() {
		mSpinnerRunning.setSelection(0);
		mSpinnerWalking.setSelection(0);
		mSpinnerSitting.setSelection(0);
		mSpinnerClimbingUp.setSelection(0);
		mSpinnerClimbingDown.setSelection(0);

		mSeekBarRunning.setProgress(1);
		mSeekBarWalking.setProgress(1);
		mSeekBarSitting.setProgress(1);
		mSeekBarClimbingUp.setProgress(1);
		mSeekBarClimbingDown.setProgress(1);
	}

	private void initUI() {
		LinearLayout layoutRunning = (LinearLayout)findViewById(R.id.seekbar_running);
		LinearLayout layoutWalking = (LinearLayout)findViewById(R.id.seekbar_walking);
		LinearLayout layoutSitting = (LinearLayout)findViewById(R.id.seekbar_sitting);
		LinearLayout layoutClimbingUp = (LinearLayout)findViewById(R.id.seekbar_climbing_up);
		LinearLayout layoutClimbingDown = (LinearLayout)findViewById(R.id.seekbar_climbing_down);

		mSeekBarRunning = (SeekBar)layoutRunning.findViewById(R.id.seekBar);
		mSeekBarWalking = (SeekBar)layoutWalking.findViewById(R.id.seekBar);
		mSeekBarSitting = (SeekBar)layoutSitting.findViewById(R.id.seekBar);
		mSeekBarClimbingUp = (SeekBar)layoutClimbingUp.findViewById(R.id.seekBar);
		mSeekBarClimbingDown = (SeekBar)layoutClimbingDown.findViewById(R.id.seekBar);
		mSeekBarRunning.setTag(1);
		mSeekBarWalking.setTag(2);
		mSeekBarSitting.setTag(3);
		mSeekBarClimbingUp.setTag(4);
		mSeekBarClimbingDown.setTag(5);
		mSeekBarRunning.setOnSeekBarChangeListener(this);
		mSeekBarWalking.setOnSeekBarChangeListener(this);
		mSeekBarSitting.setOnSeekBarChangeListener(this);
		mSeekBarClimbingUp.setOnSeekBarChangeListener(this);
		mSeekBarClimbingDown.setOnSeekBarChangeListener(this);

		mSpinnerRunning = (Spinner)layoutRunning.findViewById(R.id.spinner);
		mSpinnerWalking = (Spinner)layoutWalking.findViewById(R.id.spinner);
		mSpinnerSitting = (Spinner)layoutSitting.findViewById(R.id.spinner);
		mSpinnerClimbingUp = (Spinner)layoutClimbingUp.findViewById(R.id.spinner);
		mSpinnerClimbingDown = (Spinner)layoutClimbingDown.findViewById(R.id.spinner);

		mTextViewSeekBarRunning = (TextView)layoutRunning.findViewById(R.id.textView_seekbar);
		mTextViewSeekBarWalking = (TextView)layoutWalking.findViewById(R.id.textView_seekbar);
		mTextViewSeekBarSitting = (TextView)layoutSitting.findViewById(R.id.textView_seekbar);
		mTextViewSeekBarClimbingUp = (TextView)layoutClimbingUp.findViewById(R.id.textView_seekbar);
		mTextViewSeekBarClimbingDown = (TextView)layoutClimbingDown.findViewById(R.id.textView_seekbar);

		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.sensor_speed_array_values,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpinnerRunning.setAdapter(adapter);
		mSpinnerWalking.setAdapter(adapter);
		mSpinnerSitting.setAdapter(adapter);
		mSpinnerClimbingUp.setAdapter(adapter);
		mSpinnerClimbingDown.setAdapter(adapter);

		mSpinnerRunning.setOnItemSelectedListener(
				new OnItemSelectedListener() {
					public void onItemSelected(
							AdapterView<?> parent, View view, int position, long id) {
						//	                        showToast("Spinner2: position=" + position + " id=" + id);
					}

					public void onNothingSelected(AdapterView<?> parent) {
					}
				});

		mSpinnerWalking.setOnItemSelectedListener(
				new OnItemSelectedListener() {
					public void onItemSelected(
							AdapterView<?> parent, View view, int position, long id) {
						//	                        showToast("Spinner2: position=" + position + " id=" + id);
					}

					public void onNothingSelected(AdapterView<?> parent) {
					}
				});

		mSpinnerSitting.setOnItemSelectedListener(
				new OnItemSelectedListener() {
					public void onItemSelected(
							AdapterView<?> parent, View view, int position, long id) {
						//	                        showToast("Spinner2: position=" + position + " id=" + id);
					}

					public void onNothingSelected(AdapterView<?> parent) {
					}
				});

		mSpinnerClimbingUp.setOnItemSelectedListener(
				new OnItemSelectedListener() {
					public void onItemSelected(
							AdapterView<?> parent, View view, int position, long id) {
						//	                        showToast("Spinner2: position=" + position + " id=" + id);
					}

					public void onNothingSelected(AdapterView<?> parent) {
					}
				});

		mSpinnerClimbingDown.setOnItemSelectedListener(
				new OnItemSelectedListener() {
					public void onItemSelected(
							AdapterView<?> parent, View view, int position, long id) {
						//	                        showToast("Spinner2: position=" + position + " id=" + id);
					}

					public void onNothingSelected(AdapterView<?> parent) {
					}
				});
	}

	@SuppressWarnings("unchecked")
	private void initialiseSpinnerData(HashMap<String, String> frequncyMap) {
		ArrayAdapter<CharSequence> arrayAdapter = (ArrayAdapter<CharSequence>) mSpinnerRunning.getAdapter(); //cast to an ArrayAdapter
		String storedFrequncy = frequncyMap.get(Constants.ACCEL_ACTIVITY_RUNNING);
		mSpinnerRunning.setSelection(arrayAdapter.getPosition(storedFrequncy));

		arrayAdapter = (ArrayAdapter<CharSequence>) mSpinnerWalking.getAdapter(); 
		storedFrequncy = frequncyMap.get(Constants.ACCEL_ACTIVITY_WALKING);
		mSpinnerWalking.setSelection(arrayAdapter.getPosition(storedFrequncy));

		arrayAdapter = (ArrayAdapter<CharSequence>) mSpinnerSitting.getAdapter(); 
		storedFrequncy = frequncyMap.get(Constants.ACCEL_ACTIVITY_SITTING);
		mSpinnerSitting.setSelection(arrayAdapter.getPosition(storedFrequncy));

		arrayAdapter = (ArrayAdapter<CharSequence>) mSpinnerClimbingUp.getAdapter(); 
		storedFrequncy = frequncyMap.get(Constants.ACCEL_ACTIVITY_CLIMBING_UP);
		mSpinnerClimbingUp.setSelection(arrayAdapter.getPosition(storedFrequncy));

		arrayAdapter = (ArrayAdapter<CharSequence>) mSpinnerClimbingDown.getAdapter(); 
		storedFrequncy = frequncyMap.get(Constants.ACCEL_ACTIVITY_CLIMBING_DOWN);
		mSpinnerClimbingDown.setSelection(arrayAdapter.getPosition(storedFrequncy));
	}

	private void initialiseSeekBarData(HashMap<String, String> totalTimeMap) {
		String storedTotalTime = totalTimeMap.get(Constants.ACCEL_ACTIVITY_RUNNING);
		mSeekBarRunning.setProgress(Integer.parseInt(storedTotalTime)-1);
		
		storedTotalTime = totalTimeMap.get(Constants.ACCEL_ACTIVITY_WALKING);
		mSeekBarWalking.setProgress(Integer.parseInt(storedTotalTime)-1);
		
		storedTotalTime = totalTimeMap.get(Constants.ACCEL_ACTIVITY_SITTING);
		mSeekBarSitting.setProgress(Integer.parseInt(storedTotalTime)-1);
		
		storedTotalTime = totalTimeMap.get(Constants.ACCEL_ACTIVITY_CLIMBING_UP);
		mSeekBarClimbingUp.setProgress(Integer.parseInt(storedTotalTime)-1);
		
		storedTotalTime = totalTimeMap.get(Constants.ACCEL_ACTIVITY_CLIMBING_DOWN);
		mSeekBarClimbingDown.setProgress(Integer.parseInt(storedTotalTime)-1);

	}

	@Override
	protected void onStart() {
		super.onStart();
		String id = BeanController.getLoginBean().getId();
		if(StringUtils.isEmpty(id) || id.equalsIgnoreCase("0")) {
			id = (String)AppSettings.getPrefernce(this, null, AppSettings.USER_ID, "");
		}
		// will return the all 5 activities setting for a particular user.
		ArrayList<HashMap<String, String>> settingList = fetchSettingData(id, null);
		if(settingList.size() != 0) {
			HashMap< String, String> frequcnyMap = settingList.get(0);
			HashMap< String, String> totalTimeMap = settingList.get(1);
			initialiseSpinnerData(frequcnyMap);
			initialiseSeekBarData(totalTimeMap);
		}
		AppLog.e(settingList.toString());
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onStop() {
		super.onStop();

		String id = BeanController.getLoginBean().getId();
		if(StringUtils.isEmpty(id) || id.equalsIgnoreCase("0")) {
			id = (String)AppSettings.getPrefernce(this, null, AppSettings.USER_ID, "");
		}
		String hertzRunning = mSpinnerRunning.getSelectedItem().toString();
		String hertzWalking = mSpinnerWalking.getSelectedItem().toString();
		String hertzSitting = mSpinnerSitting.getSelectedItem().toString();
		String hertzClimbingUp = mSpinnerClimbingUp.getSelectedItem().toString();
		String hertzClimbingDown = mSpinnerClimbingDown.getSelectedItem().toString();

		AppLog.e(hertzRunning+""+hertzWalking+""+hertzSitting+""+hertzClimbingUp+""+hertzClimbingDown+"");

		storeAccelSetting(id, Constants.ACCEL_ACTIVITY_RUNNING, hertzRunning, mTextViewSeekBarRunning.getText().toString());
		storeAccelSetting(id, Constants.ACCEL_ACTIVITY_WALKING, hertzWalking, mTextViewSeekBarWalking.getText().toString());
		storeAccelSetting(id, Constants.ACCEL_ACTIVITY_SITTING, hertzSitting, mTextViewSeekBarSitting.getText().toString());
		storeAccelSetting(id, Constants.ACCEL_ACTIVITY_CLIMBING_UP, hertzClimbingUp, mTextViewSeekBarClimbingUp.getText().toString());
		storeAccelSetting(id, Constants.ACCEL_ACTIVITY_CLIMBING_DOWN, hertzClimbingDown, mTextViewSeekBarClimbingDown.getText().toString());

		ArrayList<HashMap<String, String>> settingList = fetchSettingData(id, Constants.ACCEL_ACTIVITY_WALKING);
		AppLog.e(settingList.toString());
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		if(fromUser) {
			AppLog.e("onprogreesschanged : "+fromUser+progress);
		}
		progress = progress + 1;		
		Integer seekInt = (Integer)seekBar.getTag();
		switch (seekInt) {
		case 1:
			mTextViewSeekBarRunning.setText(progress+"");
			break;
		case 2:
			mTextViewSeekBarWalking.setText(progress+"");
			break;
		case 3:
			mTextViewSeekBarSitting.setText(progress+"");
			break;
		case 4:
			mTextViewSeekBarClimbingUp.setText(progress+"");
			break;
		case 5:
			mTextViewSeekBarClimbingDown.setText(progress+"");
			break;

		default:
			break;
		}
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
		Object item = parent.getItemAtPosition(pos);
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {

	}

	private MyAppDbSQL dbAppDbObj;
	private void storeAccelSetting(String userID, String activityType, String frequency, String totalTime) {
		try{
			if (this.dbAppDbObj == null) {
				// set database query class object
				this.dbAppDbObj = new MyAppDbSQL(this);
			}
			// save the data to the database table
			boolean dbOpenResult = this.dbAppDbObj.openDbAdapter();

			if (dbOpenResult) {
				boolean blIsSuccessful = this.dbAppDbObj.updateSettingEntry(userID, activityType, frequency, totalTime);
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

	Cursor mEntryCursor;
	private ArrayList<HashMap<String, String>> fetchSettingData(String userID, String activityType) {
		ArrayList<HashMap<String, String>> arrayList = new ArrayList<HashMap<String,String>>();
		HashMap<String, String> hashMapFrequency = new HashMap<String, String>();
		HashMap<String, String> hashMapTotalTime = new HashMap<String, String>();
		try{

			if (this.dbAppDbObj == null) {
				// set database query class object
				this.dbAppDbObj = new MyAppDbSQL(this);
			}

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
					if(mEntryCursor != null)
					{
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
}
