package com.utilities;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.testng.Assert;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * The following MailinatorAPI class has few methods that calls Mailinator API
 * to read the email Inbox of a specific user. This user must supply the
 * following information through Mailinator constructor
 * 
 * @String token - This is found under account settings page of Mailinator once
 *         logged in
 * @String emailId - This is your email account name (e.g. user1 in
 *         user1@mailinator.com)
 **/

public class MailinatorAPI  
{
	private  final String USER_AGENT = "Mozilla/5.0";
	private String token;
	private String emailId;
	private String mailBoxURL;
	private String emailURL;
	private Logger log = Logger.getLogger("MailinatorAPI");
	
	public MailinatorAPI(String token, String emailId)
	{
		this.token=token;
		this.emailId=emailId;
	}
	
	private MailinatorAPI(){}
	
	/**
	 * Purpose -- The following method constructs an URL that calls a Mailinator
	 * API to retrieve the entire inbox of that email account. 
	 * @return -- Json string which has list of all email messages that the email
	 * account has received thus far
	 **/
	public String getEmailBoxAPIResponse()
	{		
		mailBoxURL="https://api.mailinator.com/api/inbox?to="+emailId+"&token="+token;
		return getResponseFromAPI(mailBoxURL);
	}
	
	/**
	 * Purpose -- The following method initializes HttpURLConnection and does GET request for the given API location
	 * @param -- URL of the Mailinator inbox
	 * @return -- Json string which has list of all email messages that the email
	 * account has received thus far
	 */
	private String getResponseFromAPI(String URL) 
	{
		String inputLine;
		try
		{
			//Creating URL object 'obj' from String 'URL'
			URL obj = new URL(URL);			
			
			//Creating an object which stores the connection to the remote object referred to by the URL. 
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			
			//Assigning GET method as the requested method
			con.setRequestMethod("GET");
			con.setRequestProperty("User-Agent", USER_AGENT);
	 		
 			//Creating an object for Buffering character input stream
 			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
 			StringBuffer response = new StringBuffer();
	 		
 			while ((inputLine = in.readLine()) != null) 
	 		{
				response.append(inputLine);
			}
			in.close();
			
	 		return response.toString();
		}
		catch(MalformedURLException e)
		{
			log.error("Exception occurred while constructing URL object\n" + e.getMessage());
			Assert.fail("Exception occurred while constructing URL object" + e.getMessage());
			return null;
		}
		catch(IOException e)
		{
			log.error("Exception occurred while reading the buffered input\n" + e.getMessage());
			Assert.fail("Exception occurred while reading the buffered input\n" + e.getMessage());
			return null;
		}
		catch(Exception e)
		{
			log.error("Exception occurred while accessing URL\n" + e.getMessage());
			Assert.fail("Exception occurred while accessing URL\n" + e.getMessage());
			return null;
		}
	}
	
	/**
	 * Purpose -- The following method retrieves the latest message ID from the
	 * list of all emails received based on the response received from
	 * getEmailBoxAPIResponse() method This message Id is used further to
	 * retrieve the email body text
	 * @param -- Inbox API response
	 * @return -- Message ID of the latest email
	 * */
    public String getLatestMessageID(String response)
    {
    	String Id=null;
    	try
    	{
    		String JsonString = response.toString();
    		JSONParser parser = new JSONParser();
    		JSONObject object = (JSONObject) parser.parse(JsonString);
    		JSONArray messages = (JSONArray)object.get("messages");
    		
    		Iterator<JSONObject> i=messages.iterator();
    		while(i.hasNext())
    		{
    			JSONObject innerObject = i.next();
    			 Id = (String)innerObject.get("id");
    		}
        	return Id;
    	}
		catch(Exception e)
		{
			log.error("Exception occurred while parsing\n" + e.getMessage());
			Assert.fail("Exception occurred while parsing\n" + e.getMessage());
			return null;
		}
    }
    
	/**
	 * Purpose -- The following method constructs the URL that calls the
	 * Mailinator API to retrieve a specific email body with messageID.
	 * @param -- Email message ID retrieved from getLatestMessageID()
	 * @return -- Email body
	 **/
    public String getEmailTextResponse(String msgId)
    {
    	emailURL="https://api.mailinator.com/api/email?msgid="+msgId+"&token="+token;
    	
    	return this.getResponseFromAPI(emailURL);   	
    }
    
}
