package com.utilities;

import org.testng.Assert;

public class CloudNotFoundException extends Exception 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public CloudNotFoundException()
	{
		super();
	}
	public CloudNotFoundException(String message)
	{
		super(message);
		Assert.fail(message+"The given cloud configuration doesn't exist. Please check Sys.properties file");
	}

}
