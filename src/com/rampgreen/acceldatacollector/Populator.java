package com.rampgreen.acceldatacollector;

import org.json.JSONObject;

public interface Populator
{
	public void populateBean(JSONObject jsonObject);
	public String toMap();
}
