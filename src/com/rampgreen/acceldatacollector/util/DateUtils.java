package com.rampgreen.acceldatacollector.util;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * @author Manish Pathak
 *
 */
public class DateUtils {
	public static final String FORMAT_YYYYMMDD_DASHESH = "yyyy-MM-dd";
	public static final String FORMAT_YYYYMMDD = "yyyyMMdd";
	public static final String FORMAT_DDMMYYYY_DASHESH = "dd-MM-yyyy";
	public static final String FORMAT_YYYYMMDD_SLASHES = "yyyy/MM/dd";
	public static final String GENERIC_DISPLAY_FORMAT = "E, dd MMM yyyy";
	public static final String TIME_DISPLAY_FORMAT = "HH mm ss";
	public static final int TODAY = 0;
	public static final int YESTERDAY = 1;
	public static final int LAST_WEEK = 2;
	public static final int LAST_TWO_WEEK = 3;
	public static final int LAST_MONTH = 4;
	public static final int LAST_TWO_MONTH = 5;
	public static final int LAST_SIX_MONTH = 6;
	public static final int LAST_YEAR= 7;

	public static final String formatDate(Date dt, String format) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(dt);

		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(format);
		sdf.setTimeZone(TimeZone.getDefault());     
		return (sdf.format(cal.getTime()));   
	}

	public static final String getCurrentDate(String format) {
		Calendar cal = Calendar.getInstance(TimeZone.getDefault());
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(format);
		sdf.setTimeZone(TimeZone.getDefault());     
		return (sdf.format(cal.getTime()));
	}

	public static final String dateToString(Date dt, String dateformat) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(dt);

		StringBuffer ret = new StringBuffer();
		String separator = new String();
		if(dateformat.equals(DateUtils.FORMAT_YYYYMMDD_DASHESH) ) {
			separator = "-";
		}
		if(dateformat.equals(DateUtils.FORMAT_YYYYMMDD_SLASHES) ) {
			separator = "/";
		}
		ret.append(cal.get(Calendar.YEAR));
		ret.append(separator);
		ret.append(cal.get(Calendar.MONTH) + 1);
		ret.append(separator);
		ret.append(cal.get(Calendar.DATE));

		return ret.toString();
	}

	public static final String dateToString(Date dt, String tzString, String dateformat) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(dt);
		cal.setTimeZone(TimeZone.getTimeZone(tzString));

		StringBuffer ret = new StringBuffer();
		String separator = new String();
		if(dateformat.equals(DateUtils.FORMAT_YYYYMMDD_DASHESH) ) {
			separator = "-";
		}
		if(dateformat.equals(DateUtils.FORMAT_YYYYMMDD_SLASHES) ) {
			separator = "/";
		}
		ret.append(cal.get(Calendar.YEAR));
		ret.append(separator);
		ret.append(cal.get(Calendar.MONTH) + 1);
		ret.append(separator);
		ret.append(cal.get(Calendar.DATE));

		return ret.toString();
	}

	public static final String getTimeFromDate(Date dt) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(dt);

		StringBuffer ret = new StringBuffer();
		ret.append(cal.get(Calendar.HOUR));
		ret.append(":");
		ret.append(cal.get(Calendar.MINUTE));

		return ret.toString();
	}

	public static final String getTimeFromDate(Date dt, String tzString) {
		try {
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(dt);
			gc.setTimeZone(TimeZone.getTimeZone(tzString));
			StringBuffer ret = new StringBuffer();
			ret.append(gc.get(Calendar.HOUR));
			ret.append(":");
			ret.append(gc.get(Calendar.MINUTE));
			ret.append(" ");
			if(gc.get(Calendar.AM_PM) == 0) {
				ret.append("AM");
			}
			else { 
				ret.append("PM");
			}
			return ret.toString();
		}
		catch(Exception e) {
			return "";
		}
	}

	public static final String getDateTimeFromDate(Date dt, String tzString) {
		try {
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(dt);
			gc.setTimeZone(TimeZone.getTimeZone(tzString));
			StringBuffer ret = new StringBuffer();
			ret.append(gc.get(Calendar.YEAR));
			ret.append("-");
			ret.append(gc.get(Calendar.MONTH) - 1);
			ret.append("-");
			ret.append(gc.get(Calendar.DATE));
			ret.append(" ");
			ret.append(gc.get(Calendar.HOUR));
			ret.append(":");
			ret.append(gc.get(Calendar.MINUTE));
			ret.append(" ");
			if(gc.get(Calendar.AM_PM) == 0) {
				ret.append("AM");
			}
			else { 
				ret.append("PM");
			}
			return ret.toString();
		}
		catch(Exception e) {
			return "";
		}
	}

	public static final String calendarToString(Calendar cal, String dateformat) {
		StringBuffer ret = new StringBuffer();
		if(dateformat.equals(FORMAT_YYYYMMDD_DASHESH) ) {
			ret.append(cal.get(Calendar.YEAR));
			ret.append("-");

			String month = null;
			int mo = cal.get(Calendar.MONTH) + 1; /* Calendar month is zero indexed, string months are not */
			if(mo < 10) {
				month = "0" + mo;
			}
			else {
				month = "" + mo;
			}
			ret.append(month);      

			ret.append("-");

			String date = null;
			int dt = cal.get(Calendar.DATE);
			if(dt < 10) {
				date = "0" + dt;
			}
			else {
				date = "" + dt;
			}
			ret.append(date);
		} else 	if(dateformat.equals(FORMAT_DDMMYYYY_DASHESH) ) {
			
			String date = null;
			int dt = cal.get(Calendar.DATE);
			if(dt < 10) {
				date = "0" + dt;
			}
			else {
				date = "" + dt;
			}
			ret.append(date);
			ret.append("-");
			
			String month = null;
			int mo = cal.get(Calendar.MONTH) + 1; /* Calendar month is zero indexed, string months are not */
			if(mo < 10) {
				month = "0" + mo;
			}
			else {
				month = "" + mo;
			}
			ret.append(month);      

			ret.append("-");
			ret.append(cal.get(Calendar.YEAR));
		}

		return ret.toString();
	}



	public static final GregorianCalendar getCurrentCalendar(String utimezonestring) {
		try {
			GregorianCalendar gc = new GregorianCalendar();

			gc.setTimeZone(TimeZone.getTimeZone(utimezonestring));
			return gc;
		}
		catch(Exception e) {
			//If exception, return server TimeStamp
			return new GregorianCalendar();
		}
	}
	
	public static String[] getDateRangeFirstLast(int cmd) {
		GregorianCalendar gc = new GregorianCalendar();
		GregorianCalendar gc2 = new GregorianCalendar();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); //$NON-NLS-1$
		int lastDateOfMonth;
		String ret[] = new String[2];
		switch (cmd) {
		case TODAY:

			break;
		case YESTERDAY:
			gc2.add(Calendar.DATE, -1);
			break;
		case LAST_WEEK:
			gc2.add(Calendar.WEEK_OF_YEAR, -1);// last week
			gc2.set(Calendar.DAY_OF_WEEK, gc2.getFirstDayOfWeek());// first day
			gc.set(Calendar.DAY_OF_WEEK, gc.getFirstDayOfWeek());// first day of this week
			gc.add(Calendar.DAY_OF_MONTH, -1);// last day of previous week
			break;
		case LAST_TWO_WEEK:
			gc2.add(Calendar.WEEK_OF_YEAR, -2);// last week
			gc2.set(Calendar.DAY_OF_WEEK, gc2.getFirstDayOfWeek());// first day
			gc.set(Calendar.DAY_OF_WEEK, gc.getFirstDayOfWeek());// first day of this week
			gc.add(Calendar.DAY_OF_MONTH, -1);// last day of previous week
			break;
		case LAST_MONTH:
			// add -1 month to current month
			gc2.add(Calendar.MONTH, -1);
			gc.add(Calendar.MONTH, -1); 
			lastDateOfMonth = gc.getActualMaximum(Calendar.DAY_OF_MONTH);
			gc2.set(Calendar.DATE, 1);// set DATE to 1, so first date of previous month
			gc.set(Calendar.DATE, lastDateOfMonth);
			break;
		case LAST_TWO_MONTH:
			gc2.add(Calendar.MONTH, -2);
			gc.add(Calendar.MONTH, -1); 
			lastDateOfMonth = gc.getActualMaximum(Calendar.DAY_OF_MONTH);
			gc2.set(Calendar.DATE, 1);// set DATE to 1, so first date of previous month
			gc.set(Calendar.DATE, lastDateOfMonth);
			break;
		case LAST_SIX_MONTH:
			gc.add(Calendar.MONTH, -1); 
			for(int i = 0; i < 6; i++) {
				gc2.add(Calendar.MONTH, -1);
			}
			lastDateOfMonth = gc.getActualMaximum(Calendar.DAY_OF_MONTH);
			gc2.set(Calendar.DATE, 1);// set DATE to 1, so first date of previous month
			gc.set(Calendar.DATE, lastDateOfMonth);
			break;
		case LAST_YEAR:
			gc.add(Calendar.YEAR, -1); 
			gc2.add(Calendar.YEAR, -1);
			lastDateOfMonth = gc.getActualMaximum(Calendar.DAY_OF_MONTH);
			gc2.set(Calendar.DATE, 1);// set DATE to 1, so first date of previous month
			gc2.set(Calendar.MONTH, 0);
			gc.set(Calendar.DATE, 31);
			gc.set(Calendar.MONTH, 11);

			break;

		default:
			break;
		}
		ret[1] = sdf.format(gc.getTime());
		ret[0] = sdf.format(gc2.getTime());
		return ret;
	}

	/**
	 * 
	 * It gives date range from the today date.  Example: today is 5 sep 2013 so last month gives the 2013-08-05 to 2013-09-05   
	 * 
	 * @param cmd it is the constants of Dateutils class. 
	 * @return string array which holds two dates in  "yyyy-MM-dd" format.
	 */
	public static String[] getDateRange(int cmd) {
		GregorianCalendar gc = new GregorianCalendar();
		GregorianCalendar gc2 = new GregorianCalendar();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); //$NON-NLS-1$
//		try {
//			Date time = (Date) sdf.parseObject("2013-03-31");
//			gc.setTime(time);
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		String ret[] = new String[2];
		ret[1] = sdf.format(gc.getTime());
		switch (cmd) {
		case TODAY:
			
			break;
		case YESTERDAY:
			gc2.add(Calendar.DATE, -1);
			break;
		case LAST_WEEK:
			for(int i = 0; i < 7; i++) {
				gc2.add(Calendar.DATE, -1);
			}
			break;
		case LAST_TWO_WEEK:
			for(int i = 0; i < 14; i++) {
				gc2.add(Calendar.DATE, -1);
			}
			break;
		case LAST_MONTH:
			gc2.add(Calendar.MONTH, -1);    
			break;
		case LAST_TWO_MONTH:
			gc2.add(Calendar.MONTH, -1);
			gc2.add(Calendar.MONTH, -1);
			break;
		case LAST_SIX_MONTH:
			for(int i = 0; i < 6; i++) {
				gc2.add(Calendar.MONTH, -1);
			}
			break;
		case LAST_YEAR:
			gc2.add(Calendar.YEAR, -1);
			break;

		default:
			break;
		}
		ret[0] = sdf.format(gc2.getTime());
		return ret;
	}


	public static final String getDayString(int day) {
		switch (day) {
		case Calendar.SUNDAY:
			return "SUNDAY";      
		case Calendar.MONDAY:
			return "MONDAY";
		case Calendar.TUESDAY:
			return "TUESDAY";
		case Calendar.WEDNESDAY:
			return "WEDNESDAY";
		case Calendar.THURSDAY:
			return "THURSDAY";
		case Calendar.FRIDAY:
			return "FRIDAY";
		case Calendar.SATURDAY:
			return "SATURDAY";
		}
		return "";
	}

	public static String convertToString(int date, int month, int year, String dateFormat) {
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.set(year, month, date);
		return DateUtils.calendarToString(calendar, FORMAT_YYYYMMDD_DASHESH);
	}

	public static Calendar convertToCalender(String date, String dateFormat){
		Calendar calendar = GregorianCalendar.getInstance();
		try {
			Format formatter = new SimpleDateFormat(dateFormat);
			Date time = (Date) formatter.parseObject(date);
			calendar.setTime(time);
		} catch (ParseException e) {
			AppLog.e("Parsing exception to convert date in dateutils class");
		}
		return calendar;
	}
}