package com.rampgreen.acceldatacollector.csv;


public class CsvModel
{
	private static final String COMMA = "," ;
	//User ID       Activity type    Duration      3000 x 3 data points (x,y,z) with 4 decimal places each
	private String userID = "";
	private String activityType = "";
	private String duration = "";
	private String startStamp = "";
	private String endStamp = "";
	private float x;
	private float y;
	private float z;

	public static String[] getColumnForCSV() {
		return new String[]{"userID", "startStamp", "endStamp", "activityType", "duration", "x", "y", "z"};
	}
	
	public String getUserID()
	{
		return userID;
	}
	public void setUserID(String userID)
	{
		this.userID = userID;
	}
	public String getActivityType()
	{
		return activityType;
	}
	public void setActivityType(String activityType)
	{
		this.activityType = activityType;
	}
	public String getDuration()
	{
		return duration;
	}
	public void setDuration(String duration)
	{
		this.duration = duration;
	}
	public float getX()
	{
		return CSVUtil.round(x, 4);
	}
	public void setX(float x)
	{
		this.x = x;
	}
	public float getY()
	{
		return CSVUtil.round(y, 4);
	}
	public void setY(float y)
	{
		this.y = y;
	}
	public float getZ()
	{
		return CSVUtil.round(z, 4);
	}
	public void setZ(float z)
	{
		this.z = z;
	}
	public String getStartStamp()
	{
		return startStamp;
	}

	public void setStartStamp(String startStamp)
	{
		this.startStamp = startStamp;
	}

	public String getEndStamp()
	{
		return endStamp;
	}

	public void setEndStamp(String endStamp)
	{
		this.endStamp = endStamp;
	}

	public String toCSV() {
		String csvCommaString = userID + COMMA + startStamp + COMMA + endStamp + COMMA + activityType+ COMMA + duration+ COMMA + getX() + COMMA + getY() + COMMA + getZ();
		return csvCommaString;
	}
}
