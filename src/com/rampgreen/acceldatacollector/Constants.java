package com.rampgreen.acceldatacollector;

public class Constants
{

	//	public static final String URL_WEB_SERVICE = "http://10.15.5.11/mwservice/index.php";
//	public static final String URL_WEB_SERVICE = "http://121.240.116.173/mwservice/index.php";
	//	public static final String URL_WEB_SERVICE = "http://121.240.116.173/mwservice_dev/index.php";
	public static final String LOGIN_ACTION = "";
	public static final String URL_WEB_SERVICE = "http://121.240.116.173/accelerometer/service.php";

	public static final String ACCEL_ACTIVITY_RUNNING = "1";
	public static final String ACCEL_ACTIVITY_WALKING = "2";
	public static final String ACCEL_ACTIVITY_SITTING = "3";// sitting and sleeping both are same
	public static final String ACCEL_ACTIVITY_SLEEPING = "4";
	public static final String ACCEL_ACTIVITY_CLIMBING_UP = "5";
	public static final String ACCEL_ACTIVITY_CLIMBING_DOWN = "6";

	public static final String LOGIN_EMAIL = "loginmail";
	
	public static final String CALLING_ACTIVITY_TYPE= "calling_activity";
	public static final int CALLING_ACTIVITY_BY_BACK_BUTTON = 100;
	public static final int CALLING_ACTIVITY_SPLASH = 101;
	public static final int CALLING_ACTIVITY_LOGIN= 102;
	public static final int CALLING_ACTIVITY_REGISTRATION= 103;
	public static final int CALLING_ACTIVITY_MAIN= 104;
}
