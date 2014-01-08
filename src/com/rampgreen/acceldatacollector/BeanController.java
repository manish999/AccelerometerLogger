package com.rampgreen.acceldatacollector;

/**
 * It is used to control all the bean of the application, every bean's instance is controlled by bean controller
 * 
 * @author Manish Pathak
 *
 */
public class BeanController 
{
	static LoginBean loginBean=null;
	
	public static LoginBean getLoginBean()
	{	
		if(loginBean == null)
		{
			loginBean = new LoginBean();
		}
		return loginBean;
	}
}

