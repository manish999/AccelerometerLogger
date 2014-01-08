package com.rampgreen.acceldatacollector;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.andreabaccega.widget.FormEditText;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.rampgreen.acceldatacollector.util.AppLog;
import com.rampgreen.acceldatacollector.util.AppSettings;
import com.rampgreen.acceldatacollector.util.ParserError;
import com.rampgreen.acceldatacollector.util.WidgetUtil;

import java.util.Map;

public class RegistrationActivity extends BaseActivity
{
	private Button mBtnRegistration;
	private FormEditText mEtUserName;
	private FormEditText mEtFirstName;
	private FormEditText mEtMiddleName;
	private FormEditText mEtLastName;
	private FormEditText mEtPassword;

	private String emailId;
	private String mSalutation;
	private Spinner mSpinner;
	private boolean mIsValid;
	private RadioGroup mRadioSexGroup;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.registration);

		// get all FormEditText field
		mEtUserName = (FormEditText) findViewById(R.id.et_email);
		mEtPassword = (FormEditText) findViewById(R.id.et_password);
		mEtFirstName = (FormEditText) findViewById(R.id.et_first_name);
		mEtMiddleName = (FormEditText) findViewById(R.id.et_middle_name);
		mEtLastName = (FormEditText) findViewById(R.id.et_last_name);
		mRadioSexGroup = (RadioGroup) findViewById(R.id.radioSex);
		mBtnRegistration = (Button) findViewById(R.id.btn_register);

		// get email id from preference store and set to registration page.
		emailId = (String)AppSettings.getPrefernce(this, null, AppSettings.USER_SELECTED_MAIL_ID, "");
		mEtUserName.setText(emailId);

		// configure the spinner for salutation
		mSpinner = ((Spinner) findViewById(R.id.spinner_salutation));
		ArrayAdapter<CharSequence> localArrayAdapter = ArrayAdapter.createFromResource(this, R.array.salutation_array,
				android.R.layout.simple_spinner_item);
		localArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		this.mSpinner.setAdapter(localArrayAdapter);

		// handle the spinner 
		this.mSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parentView,
					View selectedItemView, int position, long id) {

				AppSettings.setPreference(RegistrationActivity.this, null, AppSettings.SALUTATION, String.valueOf(position));
				mSalutation =  getResources().getStringArray(R.array.salutation_array)[position];
				mSpinner.setSelection(position);
				mSpinner.setSelected(true);
				AppLog.logToast(getApplicationContext(), "Spinner1: position="
						+ position + " id=" + id);
			}

			public void onNothingSelected(AdapterView<?> parent) {
				mSalutation =  getResources().getStringArray(R.array.salutation_array)[0];
				AppLog.logToast(getApplicationContext(), "Spinner1: unselected");
			}
		});


		// click on register button
		mBtnRegistration.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				onClickNext(arg0);
				if(mIsValid) {
					String userName = mEtUserName.getText().toString();
					String firstName = mEtFirstName.getText().toString();
					String middleName = mEtMiddleName.getText().toString();
					String lastName = mEtLastName.getText().toString();
					String password = mEtPassword.getText().toString();

					// get the selection of genderbutton
					int selectedId = mRadioSexGroup.getCheckedRadioButtonId();
					// find the radiobutton by returned id
					RadioButton radioSexButton = (RadioButton) findViewById(selectedId);
					String sexText = radioSexButton.getText().toString();
					sexText = sexText.equalsIgnoreCase("Male") ? "M" : "F"; 

					if(! WidgetUtil.checkInternetConnection(RegistrationActivity.this)) {
						WidgetUtil.showSettingDialog(RegistrationActivity.this);
						return;
					}
					// request registration web service 
					showLoadingBar();
					MyRequestQueue queue = MyVolley.getRequestQueue();
					Map<String, String> registerParam = QueryHelper.createRegistrationQuery(userName, "", 
							firstName, middleName , lastName, password, "","", "", sexText);
					CustomRequest customRequest = new CustomRequest(Method.POST,
							Constants.URL_WEB_SERVICE, registerParam, RegistrationActivity.this, RegistrationActivity.this);
					queue.add(customRequest);
				}
			}
		});
	}

	@Override
	public void onResponse(JSONObject response)
	{
		closeLoadingBar();
		int code = Integer.parseInt(response.optString("code"));
		String msg = response.optString("message");

		switch (code) {
		case ParserError.CODE_ACTION_NOT_FOUND:
			AppLog.logToast(this, "CODE_ACTION_NOT_FOUND" + code);
			break;
		case ParserError.CODE_MISSING_ACTION:
			AppLog.logToast(this, "CODE_MISSING_ACTION" + code);
			break;
		case ParserError.CODE_MISSING_TASK:
			AppLog.logToast(this, "CODE_MISSING_TASK" + code);
			break;
		case ParserError.CODE_CLIENT_AUTHORIZATION_FAILED:
			AppLog.logToast(this, "CODE_CLIENT_AUTHORIZATION_FAILED" + code);
			break;
		case ParserError.CODE_TOKEN_GENERATION_FAILED:
			AppLog.logToast(this, "CODE_TOKEN_GENERATION_FAILED" + code);
			break;
		case ParserError.CODE_USERNAME_REQUIRED:
			AppLog.logToast(this, "CODE_USERNAME_REQUIRED" + code);
			break;
		case ParserError.CODE_PASSWORD_REQUIRED:
			AppLog.logToast(this, "CODE_PASSWORD_REQUIRED" + code);
			break;
		case ParserError.CODE_PASSWORD_WRONG:
			AppLog.showToast(this, "wrong password");
			break;
		case ParserError.CODE_USER_NOT_REGISTERED:
			AppLog.logToast(this, "CODE_PASSWORD_WRONG" + code);
			break;
		case ParserError.CODE_INVALID_TOKEN:
			AppLog.logToast(this, "CODE_INVALID_TOKEN" + code);
			break;
		case ParserError.CODE_TOKEN_EXPIRED:
			AppLog.logToast(this, "CODE_TOKEN_EXPIRED" + code);
			break;
		case ParserError.CODE_INTERNAL_SERVER_ERROR:
			AppLog.showToast(this, "Internal server error, please contact to administrator.");
			break;
		case ParserError.CODE_USER_ALREADY_REGISTERED:
			AppLog.showToast(this, "User already registered");
			break;
		case ParserError.CODE_SUCCESS:
			LoginBean login = BeanController.getLoginBean();
			login.populateBean(response);
			AppLog.logToast(this,  response.toString());
			// store deviceID for 
			AppSettings.setPreference(this, null, AppSettings.USER_ID, login.getId());
			// open home activity 
			Intent intent = new Intent(getApplicationContext(), ActivityMain.class);
			//						intent.putExtra(Constants.BUNDLE_KEY_USERS, userBean);
			startActivity(intent);
			// to close the activity
			finish();
			closeLoadingBar();
			//			callUserListWebService();
			break;

		default:
			break;
		}

		if (code != ParserError.CODE_SUCCESS)
		{
			AppLog.logToast(this, "error web service response code - " + code);
		}
	}

	@Override
	public void onErrorResponse(VolleyError error)
	{
		Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show();
	}

	public void onClickNext(View v)
	{
		mIsValid = true;
		FormEditText[] allFields = { mEtUserName , mEtLastName, mEtPassword};
		for (FormEditText field : allFields)
		{
			mIsValid = field.testValidity() && mIsValid;
		}
		// for debug purpose
		if (mIsValid)
		{
			mIsValid = true;
			AppLog.logToast(this, "Valid");
		} else
		{
			mIsValid = false;
			AppLog.logToast(this, "inValid");
			// EditText are going to appear with an exclamation mark and an
			// explicative message.
		}
	}


	//	private void callUserListWebService () {
	//		String accessToken = BeanController.getLoginBean().getAccessToken();
	//		MyRequestQueue queue = MyVolley.getRequestQueue();
	//		Map<String, String> loginParam = QueryHelper.createAllUsersQuery(accessToken);
	//
	//		CustomRequest customRequest = new CustomRequest(Method.POST,
	//				Constants.URL_WEB_SERVICE, loginParam,
	//				new ResponseListener(), new ErrorListener());
	//		queue.add(customRequest);
	//	}

	//	private class ResponseListener implements Response.Listener<JSONObject> {
	//		@Override
	//		public void onResponse(JSONObject response){
	//			int code = Integer.parseInt(response.optString("code"));
	//			String msg = response.optString("message");
	//			switch (code) {
	//			case ParserError.CODE_NO_USER_FOUND:
	//				User userBean = BeanController.getUserBean();
	//				userBean.populateBean(response);
	//				AppLog.logString(response.toString());
	//
	//				TextDisplaySettings textSetting = BeanController.getTextDisplaySettings();
	//				textSetting.populateBean(response);
	//
	//				VisualDisplaySettings visualSetting = BeanController.getVisualDisplaySettings();
	//				visualSetting.populateBean(response);
	//
	//				// on success , call Home screen
	//				Intent intent = new Intent(getApplicationContext(), FragmentChangeActivity.class);
	//				intent.putExtra(Constants.BUNDLE_KEY_USERS, userBean);
	//				startActivity(intent);
	//				// to close the activity
	//				closeLoadingBar();
	//				finish();
	//
	//				break;
	//			case ParserError.CODE_SUCCESS:
	//				userBean = BeanController.getUserBean();
	//				userBean.populateBean(response);
	//				AppLog.logString(response.toString());
	//
	//				textSetting = BeanController.getTextDisplaySettings();
	//				textSetting.populateBean(response);
	//
	//				visualSetting = BeanController.getVisualDisplaySettings();
	//				visualSetting.populateBean(response);
	//
	//				// on success , call Home screen
	//				intent = new Intent(getApplicationContext(), FragmentChangeActivity.class);
	//				intent.putExtra(Constants.BUNDLE_KEY_USERS, userBean);
	//				startActivity(intent);
	//				// to close the activity
	//				closeLoadingBar();
	//				finish();
	//
	//				break;
	//
	//			default:
	//				closeLoadingBar();
	//				break;
	//			}
	//
	//			if (code != ParserError.CODE_SUCCESS)
	//			{
	//				AppLog.logToast(RegistrationActivity.this, "error web service response code - " + code);
	//			}
	//		}
	//	}


	private class ErrorListener implements Response.ErrorListener{
		@Override
		public void onErrorResponse(VolleyError error){
			closeLoadingBar();
			AppLog.logToast(RegistrationActivity.this, error.toString());
		}
	}
}
