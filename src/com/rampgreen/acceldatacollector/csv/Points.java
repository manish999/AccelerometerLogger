package com.rampgreen.acceldatacollector.csv;

import java.text.DecimalFormat;


public class Points
{
	DecimalFormat df3 = new DecimalFormat("#0.0000");
	DecimalFormat df6 = new DecimalFormat("#0.000000");
	
	private static final String COMMA = "," ;
	//User ID       Activity type    Duration      3000 x 3 data points (x,y,z) with 4 decimal places each

	private float x;
	private float y;
	private float z;
	private long timeStampEvent;

//	public static String[] getColumnForCSV() {
//		return new String[]{"userID", "startStamp", "endStamp", "activityType", "duration", "x", "y", "z"};
//	}
	public Points(float x, float y, float z)
	{
		this.x= x;
		this.y = y;
		this.z = z;
	}
	
	public String getX()
	{
		return df3.format(x);
//		return CSVUtil.round(x, 4);
	}
	public void setX(float x)
	{
		this.x = x;
	}
	public String getY()
	{
//		return CSVUtil.round(y, 4);
		return df3.format(y);
	}
	public void setY(float y)
	{
		this.y = y;
	}
	public String getZ()
	{
//		return CSVUtil.round(z, 4);
		return df3.format(z);
	}
	public void setZ(float z)
	{
		this.z = z;
	}
	
	@Override
	public String toString()
	{
		return getX() + COMMA + getY() + COMMA + getZ();
	}

	public String toCSV() {
		String csvCommaString = getX() + COMMA + getY() + COMMA + getZ();
		return csvCommaString;
	}
}
