package com.rampgreen.acceldatacollector;

import java.util.HashMap;
import java.util.Map;

/**
 * This class helps to make a http request query
 * 
 *  @author Manish Pathak
 */
public class QueryHelper {

	public static Map<String, String> createLoginQuery(String userName, String passWord) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("task", "authenticate");
		params.put("username", userName);
		params.put("password", passWord);
		return params;
	}

	public static Map<String, String> createRegistrationQuery(String userName, String salutation, String firstName, String middleName, String lastName, String password,String dateOfBirth, String height, String weight, String gender) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("task", "register");
		params.put("username", userName);
		params.put("firstname", lastName);
		params.put("password", password);
		params.put("gender", gender);
		return params;
	}
	
	public static Map<String, String> createSensorDataQuery(String jsonData) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("task", "push_acclerometer_data");
		params.put("data_json", jsonData);
		return params;
	}
}