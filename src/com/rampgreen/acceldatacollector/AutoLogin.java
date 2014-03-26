package com.rampgreen.acceldatacollector;

import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.rampgreen.acceldatacollector.util.AppLog;
import com.rampgreen.acceldatacollector.util.AppSettings;
import com.rampgreen.acceldatacollector.util.ParserError;
import com.rampgreen.acceldatacollector.util.StringUtils;
import com.rampgreen.acceldatacollector.util.WidgetUtil;

import java.util.Map;

public class AutoLogin implements Response.Listener<JSONObject>, Response.ErrorListener
{
	Context mContext;
	private ProgressDialog progressDialog;
	private static int numAsyncTasks;


	public AutoLogin(Context context)
	{
		this.mContext = context; 
	}

	public AutoLogin(String userName, String Password)
	{
	}

	public void doLogin() {
		String userName = (String) AppSettings.getPrefernce(mContext, null, AppSettings.USER_SELECTED_MAIL_ID, ""); 
		String password = (String) AppSettings.getPrefernce(mContext, null, AppSettings.USER_SELECTED_PASSWORD, "");

		if(StringUtils.isEmpty(userName) && StringUtils.isEmpty(password)) {
			throw new IllegalArgumentException("either username or password is null/empty");
		}

		if(! WidgetUtil.checkInternetConnection(mContext)) {
			WidgetUtil.showSettingDialog(mContext);
			return;
		}
		// get network queue and add request to the queue
		MyRequestQueue queue = MyVolley.getRequestQueue();
		Map<String, String> loginParam = QueryHelper.createLoginQuery(
				userName, password);
		CustomRequest customRequest = new CustomRequest(Method.POST,
				Constants.URL_WEB_SERVICE, loginParam,
				this, this);
//		showLoadingBar();
		queue.add(customRequest);
	}

	@Override
	public void onErrorResponse(VolleyError error)
	{
		closeLoadingBar();
		AppLog.logToast(mContext, error.toString());
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
			AppLog.showToast(mContext, "wrong password");
			closeLoadingBar();
			break;
		case ParserError.CODE_USER_NOT_REGISTERED:
			intent = new Intent(mContext,
					RegistrationActivity.class);
//			intent.putExtra(Constants.LOGIN_EMAIL, mEtUserName.getText().toString());
			mContext.startActivity(intent);
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
			AppSettings.setPreference(mContext, null, AppSettings.USER_ID, login.getId());
			AppSettings.setPreference(mContext, null, AppSettings.USER_NAME, login.getUserName());
			// on success , call Main screen
			intent = new Intent(mContext, MainActivityOld.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//			intent.putExtra(Constants.BUNDLE_KEY_USERS, userBean);
			mContext.startActivity(intent);
			// to close the activity
//			finish();
			closeLoadingBar();
			break;

		default:
			closeLoadingBar();
			break;
		}

		if (code != ParserError.CODE_SUCCESS)
		{
			AppLog.logToast(mContext, "error web service response code - " + code);
		}

	}
	
	protected void showLoadingBar(){
		numAsyncTasks++;
		progressDialog = ProgressDialog.show(mContext, null, "Loading...", true);
	}

	protected void closeLoadingBar(){
		if (0 == --numAsyncTasks) {
			if(progressDialog != null && progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
		}
	}
}
