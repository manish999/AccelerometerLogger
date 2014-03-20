package com.rampgreen.acceldatacollector.util;

import java.util.Random;

public class StringUtils {

	private static int space = 1; 
	public static boolean isEmpty(String str){
		if("".equals(str) || "null".equals(str) || str == null){
			return true;
		}
			return false;
	}

	public static boolean notEmpty(String str){
		if("".equals(str) || "null".equals(str) || str == null){
			return false;
		}
			return true;
	}

	public static boolean isDigit(String str){
		int j,i;
		int flag = 0;
		if(isEmpty(str))
			return false;
		if(notEmpty(str)){
			for(i=0;i<str.length();i++)
			{
				j=str.charAt(i);
				if((j>=65 && j<=90)||(j>=97 && j<=122))
				{
					flag=1;
					break;
				}

			}
		}
		return flag==1 ? false : true;
	}
	
	public static boolean isNumeric(String str){
		if(isEmpty(str))
			return false;
		try{
			int a = Integer.parseInt(str.trim());
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public static String cutString(String str,int length){
		String result = str;
		if(str.length() > length){
			result = str.substring(0,length) + "...";
		}
		return result;
	}

	public static String appendRandomSpace(String string){
		Random random = new Random();
		int randomNum =random.nextInt(50);
		for(int index =0;index< randomNum ; index++)
			string = string +index;
		return string;
	}

	public static String inBetweenSpace(String string){
		if (string.contains(" ")) {
			if (space == 1) {
				int t = string.indexOf(" ");
				string = string.substring(0,t+1)+" "+string.substring(t+1);
				space =0;
			}
			else {
				space =1;
			} 
		}
		return string;
	}

	public static String appendInBetweenRandomSpace(String string){
		String spaces = " "; 
		Random random = new Random();
		int randomNum =random.nextInt(10);
		for(int index =0; index< randomNum ; index++)
			spaces  = spaces +" ";
		if(string.contains(" ")) {
			int t = string.indexOf(" ");
			string = string.substring(0,t)+spaces+string.substring(t+1);
		}
		return string;
	}

	/**
	 * this method search the mobile number from msg body.
	 * @param body Message body
	 * @return mobile number, if not present return null
	 */
	public static String searchMobileNum (String body){
		if(body.contains(":")){
			if(body.indexOf(":") == 10){
				body = body.substring(0, 10);
			}
		}
		if(isDigit(body))
			return body;
		else return null;
	}

	/**
	 * @param booleanString
	 * @return if 0,null, "" or NULL return false otherwise true.
	 */
	public static Boolean getBooleanServer (String booleanString) {
		if("0".equals(booleanString) || isEmpty(booleanString)){
			return false;
		}
			return true;
	}
	
	public static String removeCommaFromFirstAndLast(String string) {
		//last index checked
		string = string.charAt(string.length() - 1) == ',' ? string
				.substring(0, string.length() - 1) : string;
		//first index is checked
		string = string.charAt(0) == ',' ? string
						.substring(1) : string;
		return string;
	}
}
