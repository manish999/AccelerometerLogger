package com.rampgreen.acceldatacollector;

import org.json.JSONObject;

import com.rampgreen.acceldatacollector.util.AppLog;



public class LoginBean extends GeneralData implements Populator{

	private String userName;
	private String id;

	public LoginBean() {
		userName = "";
	}
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Override
	public String toMap()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void populateBean(JSONObject jsonObject)
	{
		AppLog.e(jsonObject.toString());
		// {"code":"200","message":"Success","user":{"id":"2","username":"paramgir@gmail.com","firstname":"param","lastname":"gir","gender":"F"}}
		JSONObject userJsonObj = jsonObject.optJSONObject("user");
		id = userJsonObj.optString("id", "");
		userName = jsonObject.optString("firstname", "");
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}
}
