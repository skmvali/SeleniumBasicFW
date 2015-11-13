package com.page.data;


import com.datamanager.ConfigManager;

	public class SendMailData

	{
	private String sToAddress;
	private String sSubject;
	private String sMessageBody;
	  
	
	//Constructor to define/call methods
	public SendMailData() throws Exception
	{		
	
	ConfigManager app = new ConfigManager("App");	
	setsToAddress(app.getProperty("App.Username"));
	setsSubject(app.getProperty("App.Subject"));
	setsMessageBody(app.getProperty("App.Body"));
	
	} 
	
	/**
	 * Purpose - To set ToAddress data
	 * @param sToAddress
	 */
	public void setsToAddress(String sToAddress) 
	{
		this.sToAddress = sToAddress; 
	}
	
	/**
	 * Purpose - To get ToAddress data
	 * @return
	 */
	public String getsToAddress() 
	{
		return sToAddress;
	}

	
	/**
	 * Purpose - To set mail subject
	 * @param sSubject
	 */
	public void setsSubject(String sSubject) 
	{
		this.sSubject = sSubject; 
	}
	
	/**
	 * Purpose - To get mail subject data
	 * @return
	 */
	public String getsSubject() 
	{
		return sSubject;
	}

	/**
	 * Purpose - To set message body
	 * @param sMessageBody
	 */
	public void setsMessageBody(String sMessageBody) 
	{
		this.sMessageBody = sMessageBody; 
	}
	
	/**
	 * Purpose - To get message body data
	 * @return
	 */
	public String getsMessageBody() 
	{
		return sMessageBody;
	}
}
