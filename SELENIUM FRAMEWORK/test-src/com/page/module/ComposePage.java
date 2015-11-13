package com.page.module;

import org.openqa.selenium.WebDriver;

import ru.yandex.qatools.allure.annotations.Step;

import com.datamanager.ConfigManager;
import com.page.data.SendMailData;
import com.page.locators.ComposeLocators;
import com.selenium.SafeActions;
import com.selenium.Sync;
//import org.testng.Assert;
import com.testng.Assert;


public class ComposePage extends SafeActions implements ComposeLocators
{
	private WebDriver driver;
	SendMailData filldata;
	ConfigManager app;
	Sync sync;
	
	//Constructor to define/call methods	 
	public ComposePage(WebDriver driver) throws Exception
	{		
		super(driver);
		this.driver = driver;
		app=new ConfigManager("App");
    } 

	
	/**
	 * Purpose- To verify whether compose a new mail page is being displayed or not
	 * @throws Exception
	 */
	@Step("Verifying compose page")
	public void verifyComposePage() throws Exception 
	{
		waitUntilElementDisappears(LOADING);
		boolean bIsSendButtonExists = isElementPresent(SEND_BTN, MEDIUMWAIT);
		Assert.assertTrue(bIsSendButtonExists,"Sendbutton doesn't exist");
	}
	
	/**
	 * Purpose- To enter email address,subject and mail text in To,Subject and Body fields 
	 * @param filldata
	 * @throws Exception
	 */
	@Step("Entering values into To,Subject and Body Fields")
	public void enterTo_Subject_BodyFields(SendMailData filldata) throws Exception 
	{
		fillSendMaildata(filldata);		
	}
	
	
	/**
	 * Purpose- To click on send button
	 * @throws Exception
	 */
	@Step("Clicking on Send Button")
	public void clickSendButton() throws Exception
	{
		safeClick(SEND_BTN, SHORTWAIT);
	}
	
	/**
	 * Purpose- To verify whether an email is sent successfully or not
	 * @throws Exception
	 */
	@Step("Verifying Sent mail message text")
	public HomePage verifySentMailMessage() throws Exception
	{
		boolean bViewMailLink = isElementPresent(VIEWMSG_LINK, LONGWAIT);
		boolean bSentMsgSuccess = isElementPresent(MSG_SENT_MESSAGE);
		Assert.assertTrue(bViewMailLink,"'View Message' link is not being displayed on clicking 'Send' button");
		Assert.assertTrue(bSentMsgSuccess,"The verification message- 'Your message has been sent' is not being displayed on clicking 'Send' button");
		return new HomePage(driver);
	}
	
	/**
	 * Purpose- To click on send button and to verify saved message,saved button
	 * @throws Exception
	 */
	@Step("Clicking on Save Button")
	public HomePage clickSaveButton() throws Exception
	{
		safeClick(SAVENOW_BTN, SHORTWAIT);
		return new HomePage(driver);
	}
	
	
	/**
	 * Purpose - To fill data in the send mail fields by calling from set and get methods
	 * @param datafields
	 * @throws Exception
	 */
	
	public void fillSendMaildata(SendMailData datafields) throws Exception
	{	
		safeType(TO_FIELD, datafields.getsToAddress(), SHORTWAIT);
		safeType(SUBJECT_FIELD, datafields.getsSubject(), SHORTWAIT);	    
	    safeType(BODYFRAME, datafields.getsMessageBody(), SHORTWAIT);
	    defaultFrame();            
	}
	/**
	 * purpose - To send email with datafields and to set properties to app.properties file
	 *
	 */
	public void fillMaildata(SendMailData datafields) throws Exception
	{
	    fillSendMaildata(datafields);
	    safeClick(SEND_BTN, SHORTWAIT);
	   writeProperty();
	    
       
	}
	
	/**
	 * purpose - to set properties to app.properties file
	 */
	private void writeProperty()
	{
		 app.writeProperty("MAILSUBJECT1", app.getProperty("App.Subject1"));
		 System.out.println("sub1:"+app.getProperty("MAILSUBJECT1"));
	        app.writeProperty("MAILSUBJECT3", app.getProperty("App.Subject"));
	       System.out.println("sub3:"+app.getProperty("MAILSUBJECT3"));
	        app.writeProperty("MAILSUBJECT2", app.getProperty("App.Subject"));
	       System.out.println("sub2:"+app.getProperty("MAILSUBJECT2"));
	}


}

