package com.rampgreen.acceldatacollector;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.widget.EditText;

import com.rampgreen.acceldatacollector.util.AppLog;
import com.rampgreen.acceldatacollector.util.AppSettings;

/**
 * @author Manish Pathak
 *
 */
public class PreferencesActivity extends PreferenceActivity implements  OnPreferenceChangeListener, Preference.OnPreferenceClickListener {

	private ListPreference  mPrefPeriod;
	private int selectedDate;
	//	private MyDatePickerDialog myDatePickerDialog;
	//	private EditTextPreference editTextPreference;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settingslayout);
		//		editTextPreference = (EditTextPreference) findPreference("maxresult");
		mPrefPeriod=(ListPreference) findPreference ("period");
		setPreferenceChangeListener("period",this);
		//		setPreferenceChangeListener("maxresult",this);
		//		setPreferenceClickListener("maxresult",this);
		mPrefPeriod.setOnPreferenceClickListener(this);
		String getPreSelectedDate = (String)AppSettings.getPrefernce(PreferencesActivity.this, null, AppSettings.SPEED_SENSOR,"0");
		int preSelectedDate = Integer.valueOf(getPreSelectedDate).intValue();
		mPrefPeriod.setValueIndex(preSelectedDate);
	}

	@Override
	protected void onStart() {
		super.onStart();
	}
	private void setPreferenceClickListener(String key, Preference.OnPreferenceChangeListener listener){
		Preference preference = findPreference(key);
		preference.setOnPreferenceClickListener(PreferencesActivity.this);
	}

	private void setPreferenceCheckedChangeListener(String key, Preference.OnPreferenceChangeListener listener){
		Preference preference = findPreference(key);
		preference.setOnPreferenceClickListener(PreferencesActivity.this);
	}

	private void setPreferenceChangeListener(String key, Preference.OnPreferenceChangeListener listener){
		Preference preference = findPreference(key);
		preference.setOnPreferenceChangeListener(PreferencesActivity.this);
	}

	public boolean onPreferenceChange(Preference preference, Object newValue) {
		String paramString = preference.getKey();
		if (paramString.equals("period")) {
			int indexOfSelectedGateways = Integer.valueOf(String.valueOf(newValue));
			AppSettings.setPreference(PreferencesActivity.this, null, AppSettings.SPEED_SENSOR, (String)newValue);
			String[] arrayOfString = getResources().getStringArray(R.array.date_array);
			AppLog.showToast(getApplicationContext(),"Accelerometer Speed is changed to: " + arrayOfString[indexOfSelectedGateways]);
			mPrefPeriod.setValueIndex(indexOfSelectedGateways);// default last week
			mPrefPeriod.shouldCommit();
						String[] daterange;
						switch (indexOfSelectedGateways) {
						case 0:
							
							break;
						case 1:
							
							break;
						case 2:
							
							break;
						case 3:
							
							break;
						case 4:
							
							break;
						case 5://custom date
							openEditInDialog("0");
						break;
			
						default:
							break;
						}
		}
		//		else if (paramString.equals("maxresult")) {
		//			String maxResult = String.valueOf(newValue);
		//			if(StringUtils.isDigit(maxResult)) {
		//				AppSettings.setPreference(PreferencesActivity.this,null, AppSettings.MAX_RESULT, maxResult);
		//			}else {
		//				AppLog.showToast(PreferencesActivity.this, "Please enter the numeric value.");
		//			}
		//		}
		return false;
	}

	public boolean onPreferenceClick(Preference preference) {
		String prefKey = preference.getKey();
		if(prefKey.equalsIgnoreCase("period")) {
			String getPreSelectedDate = (String)AppSettings.getPrefernce(PreferencesActivity.this, null, AppSettings.SPEED_SENSOR,"0");
			int preSelectedDate = Integer.valueOf(getPreSelectedDate).intValue();
			mPrefPeriod.setValueIndex(preSelectedDate);
			mPrefPeriod.shouldCommit();
			AppLog.logToast(this, ""+preSelectedDate);
		}
		//		else if(prefKey.equalsIgnoreCase("maxresult")) {
		//			String maxResults = (String) AppSettings.getPrefernce(PreferencesActivity.this, null, AppSettings.MAX_RESULT, "500");
		//			editTextPreference.getEditText().setText(maxResults);
		//		}
		return false;
	}

	//	@Override
	//	public void onDateSet(DatePicker view, int year, int monthOfYear,
	//			int dayOfMonth) {
	//		String dateString = DateUtils.convertToString(dayOfMonth, monthOfYear, year, DateUtils.FORMAT_YYYYMMDD_DASHESH);
	//		if(selectedDate == MyDatePickerDialog.START_DATE) {
	//			//save the start date in prefrence
	//			AppSettings.setPreference(this, null, AppSettings.START_DATE, dateString);
	//			callDatePicker(MyDatePickerDialog.END_DATE);
	//			//			tvStartDate.setText(dateString);
	//
	//		}else if(selectedDate == MyDatePickerDialog.END_DATE) {
	//			//save the end date in prefrence
	//			AppSettings.setPreference(this, null, AppSettings.END_DATE, dateString);
	//			//			tvEndDate.setText(dateString);
	//		}
	//	}
	//
	//	private void callDatePicker(int date) {
	//		int mYear , mMonth, mDay;
	//		selectedDate = date;
	//		Calendar c = Calendar.getInstance();
	//		String startdate = (String) AppSettings.getPrefernce(this, null, AppSettings.START_DATE, "");
	//		String enddate = (String) AppSettings.getPrefernce(this, null, AppSettings.END_DATE, "");
	//
	//		if(selectedDate == MyDatePickerDialog.START_DATE) {
	//			if(StringUtils.notEmpty(startdate))
	//				c= DateUtils.convertToCalender(startdate, DateUtils.FORMAT_YYYYMMDD_DASHESH);
	//			mYear = c.get(Calendar.YEAR);
	//			mMonth = c.get(Calendar.MONTH);
	//			mDay = c.get(Calendar.DAY_OF_MONTH);
	//			myDatePickerDialog =  new MyDatePickerDialog(this, this, mYear, mMonth, mDay);
	//			myDatePickerDialog.setTitle("Select Start Date");
	//		}else if(selectedDate == MyDatePickerDialog.END_DATE) {
	//			if(StringUtils.notEmpty(enddate))AppSettings.setPreference(PreferencesActivity.this, null, AppSettings.CUSTOM_SPEED, value);
	//				c= DateUtils.convertToCalender(enddate, DateUtils.FORMAT_YYYYMMDD_DASHESH);
	//			mYear = c.get(Calendar.YEAR);
	//			mMonth = c.get(Calendar.MONTH);
	//			mDay = c.get(Calendar.DAY_OF_MONTH);
	//			myDatePickerDialog =  new MyDatePickerDialog(this, this, mYear, mMonth, mDay);
	//			myDatePickerDialog.setTitle("Select End Date");
	//		}
	//		myDatePickerDialog.show();
	//	}

	//	private void setQueryDate(String startDate, String endDate){
	//		AppSettings.setPreference(PreferencesActivity.this, null, AppSettings.START_DATE, startDate);
	//		AppSettings.setPreference(PreferencesActivity.this, null, AppSettings.END_DATE, endDate);
	//	}

	private void openEditInDialog(final String preSelectedIndex) {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle("Set the Speed Rate(in Htz)");
		alert.setMessage("");

		// Set an EditText view to get user input 
		String getPreSelectedDate = (String)AppSettings.getPrefernce(PreferencesActivity.this, null, AppSettings.CUSTOM_SPEED,"");
		final EditText input = new EditText(this);
		input.setText(getPreSelectedDate);
		alert.setView(input);
		
		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				String value = input.getText().toString();
				AppSettings.setPreference(PreferencesActivity.this, null, AppSettings.CUSTOM_SPEED, value);
			}
		});

		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				// Canceled set default that is .
				AppSettings.setPreference(PreferencesActivity.this, null, AppSettings.SPEED_SENSOR, preSelectedIndex);
			}
		});

		alert.show();
	}
}

