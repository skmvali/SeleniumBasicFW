package com.testng;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

import com.datamanager.ConfigManager;

public class Retry implements IRetryAnalyzer
{
	public int retryCount = 0;
	public int maxRetryCount = Integer.parseInt(new ConfigManager().getProperty("RetryCount"));
	
	/**
	 * This method is used to retry the failed test case again
	 */
	public boolean retry(ITestResult result) 
	{
		if(retryCount < maxRetryCount) 
		{ 
			retryCount++; 
			return true; 
		} 
		return false; 
	}

} 