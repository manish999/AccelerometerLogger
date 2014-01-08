package com.rampgreen.acceldatacollector;

import com.rampgreen.acceldatacollector.csv.Points;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * This class helps to make a http request query
 * 
 *  @author Manish Pathak
 */
public class QueryHelper {

//	?task=authenticate&username=paramgir@gmail.com&passwod=param
	public static Map<String, String> createLoginQuery(String userName, String passWord) {
		HashMap<String, String> params = new HashMap<String, String>();
//		params.put("action", "user");
		params.put("task", "authenticate");
		params.put("username", userName);
		params.put("password", passWord);
		return params;
	}

	public static Map<String, String> createRegistrationQuery(String userName, String salutation, String firstName, String middleName, String lastName, String password,String dateOfBirth, String height, String weight, String gender) {
//		task=register&username=paramgir@gmail.com&firstname=param&lastname=gir&gender=F&password=param
		HashMap<String, String> params = new HashMap<String, String>();
//		params.put("action", "user");
		params.put("task", "register");
		params.put("username", userName);
//		params.put("salutation", salutation);
//		params.put("firstname", firstName);
//		params.put("middlename", middleName);
		params.put("firstname", lastName);
//		params.put("lastname", "");
		params.put("password", password);
//		params.put("dob", dateOfBirth);
//		params.put("height", height);
//		params.put("weight", weight);
		params.put("gender", gender);
		return params;
	}
	
	public static Map<String, String> createSensorDataQuery(String jsonData) {
		
		HashMap<String, String> params = new HashMap<String, String>();
//		params.put("action", "user");
		params.put("task", "push_acclerometer_data");
		params.put("data_json", jsonData);
		
		return params;
	}
}