package com.page.data;

import com.datamanager.ConfigManager;

public class EmailData 
{
	ConfigManager appData = new ConfigManager("App");
	public final String gmailURL = appData.getProperty("App.URL");
	public final String gmailUsername = appData.getProperty("App.Username");
	public final String gmailPassword = appData.getProperty("App.Password");
	public final String subject = appData.getProperty("App.Subject");
	public final String body = appData.getProperty("App.Body");
}
