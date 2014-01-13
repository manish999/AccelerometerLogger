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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settingslayout);
		mPrefPeriod=(ListPreference) findPreference ("period");
		setPreferenceChangeListener("period",this);
		mPrefPeriod.setOnPreferenceClickListener(this);
		String getPreSelectedDate = (String)AppSettings.getPrefernce(PreferencesActivity.this, null, AppSettings.SPEED_SENSOR,"0");
		int preSelectedDate = Integer.valueOf(getPreSelectedDate).intValue();
		mPrefPeriod.setValueIndex(preSelectedDate);
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
			case 5:
				openEditInDialog("0");
				break;

			default:
				break;
			}
		}
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
		return false;
	}

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

