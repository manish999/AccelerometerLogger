package com.rampgreen.acceldatacollector;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.andreabaccega.widget.FormEditText;
import com.android.volley.Request.Method;
import com.android.volley.VolleyError;
import com.rampgreen.acceldatacollector.util.AppLog;
import com.rampgreen.acceldatacollector.util.AppSettings;
import com.rampgreen.acceldatacollector.util.ParserError;
import com.rampgreen.acceldatacollector.util.WidgetUtil;

import java.util.Map;

public class LoginActivity extends BaseActivity
{

	private Button mLoginButton;
	private FormEditText mEtUserName;
	private FormEditText mEtPassword;

	private boolean mIsValid;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		// obtain form field from xml
		mEtUserName = (FormEditText) findViewById(R.id.et_email);
		mEtPassword = (FormEditText) findViewById(R.id.et_password);
		mLoginButton = (Button) findViewById(R.id.btn_login);

		// handle login button click 
		mLoginButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View arg0) {
				onClickNext(arg0);
				// if every field is valid, send request to RTNeuroserver
				if(mIsValid) {
					String username = mEtUserName.getText().toString();
					String password = mEtPassword.getText().toString();

					AppSettings.setPreference(LoginActivity.this, null, AppSettings.USER_SELECTED_MAIL_ID, username);
					AppSettings.setPreference(LoginActivity.this, null, AppSettings.USER_SELECTED_PASSWORD, password);

					if(! WidgetUtil.checkInternetConnection(LoginActivity.this)) {
						WidgetUtil.showSettingDialog(LoginActivity.this);
						return;
					}
					// get network queue and add request to the queue
					MyRequestQueue queue = MyVolley.getRequestQueue();
					Map<String, String> loginParam = QueryHelper.createLoginQuery(
							username, password);
					CustomRequest customRequest = new CustomRequest(Method.POST,
							Constants.URL_WEB_SERVICE, loginParam,
							LoginActivity.this, LoginActivity.this);
					showLoadingBar();
					queue.add(customRequest);
				}
			}
		});
	}

	@Override
	public void onResponse(JSONObject response)
	{

		int code = Integer.parseInt(response.optString("code"));
		String msg = response.optString("message");
		Intent intent;
		switch (code) {
		case ParserError.CODE_ACTION_NOT_FOUND:
			closeLoadingBar();
			break;
		case ParserError.CODE_MISSING_ACTION:
			closeLoadingBar();
			break;
		case ParserError.CODE_MISSING_TASK:
			closeLoadingBar();
			break;
		case ParserError.CODE_CLIENT_AUTHORIZATION_FAILED:
			closeLoadingBar();
			break;
		case ParserError.CODE_TOKEN_GENERATION_FAILED:
			closeLoadingBar();
			break;
		case ParserError.CODE_USERNAME_REQUIRED:

			break;
		case ParserError.CODE_PASSWORD_REQUIRED:
			closeLoadingBar();
			break;
		case ParserError.CODE_PASSWORD_WRONG:
			AppLog.showToast(this, "wrong password");
			closeLoadingBar();
			break;
		case ParserError.CODE_USER_NOT_REGISTERED:
			intent = new Intent(getApplicationContext(),
					RegistrationActivity.class);
			intent.putExtra(Constants.LOGIN_EMAIL, mEtUserName.getText().toString());
			startActivity(intent);
			closeLoadingBar();
			break;
		case ParserError.CODE_INVALID_TOKEN:
			closeLoadingBar();
			break;
		case ParserError.CODE_TOKEN_EXPIRED:
			closeLoadingBar();
			break;
		case ParserError.CODE_INTERNAL_SERVER_ERROR:
			closeLoadingBar();
			break;
		case ParserError.CODE_USER_ALREADY_REGISTERED:
			closeLoadingBar();
			break;
		case ParserError.CODE_SUCCESS:
			LoginBean login = BeanController.getLoginBean();
			login.populateBean(response);
			AppLog.logString(response.toString());
			// store userID  
			AppSettings.setPreference(this, null, AppSettings.USER_ID, login.getId());
			AppSettings.setPreference(this, null, AppSettings.USER_NAME, login.getUserName());
			// on success , call Main screen
			intent = new Intent(getApplicationContext(), MainActivityOld.class);
//			intent.putExtra(Constants.BUNDLE_KEY_USERS, userBean);
			intent.putExtra(Constants.CALLING_ACTIVITY_TYPE, Constants.CALLING_ACTIVITY_LOGIN);
			startActivity(intent);
			// to close the activity
			finish();
			closeLoadingBar();
			break;

		default:
			closeLoadingBar();
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
		closeLoadingBar();
		AppLog.logToast(this, error.toString());
	}

	public void onClickNext(View v)
	{
		mIsValid = true;
		FormEditText[] allFields = { mEtUserName , mEtPassword};

		for (FormEditText field : allFields)
		{
			mIsValid = field.testValidity() && mIsValid;
		}

		//for debug purpose
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
}
