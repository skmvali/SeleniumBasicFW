package com.page.module;

import org.openqa.selenium.WebDriver;

import ru.yandex.qatools.allure.annotations.Step;

import com.page.locators.InboxLocators;
import com.selenium.Dynamic;
import com.selenium.SafeActions;
//import org.testng.Assert;
import com.testng.Assert;


public class InboxPage extends SafeActions implements InboxLocators
{
	private WebDriver driver;
	
	
	//Constructor to define/call methods	 
	public InboxPage(WebDriver driver) throws Exception
	{		
		super(driver);
		this.driver = driver;
    } 



	/**
	 * Purpose- To click on email with specified subject
	 * @param sSubject - We pass the Subject of the mail 
	 * @throws Exception
	 */
	@Step("Clicking on email from Inbox page")
	public void clickMail(String sSubject) throws Exception 
	{
		waitUntilElementDisappears(LOADING, MEDIUMWAIT);
		boolean bMail = isElementPresent(Dynamic.getNewLocator(EMAIL, sSubject),MEDIUMWAIT);
		Assert.assertTrue(bMail,"Unread Email with specified subject is not being displayed in Inbox");
		safeClick(Dynamic.getNewLocator(EMAIL, sSubject),MEDIUMWAIT);
	}
	
	/**
	 * Purpose- To verify opened mail with specified subject and body
	 * @param sSubject- we pass the subject of the mail
	 * @param sBody- we pass the body text of the mail
	 * @throws Exception
	 */
	@Step("Verifying opened mail page")
	public HomePage verifyOpenedMail(String sSubject, String sBody) throws Exception
	{
		boolean bSubject = isElementPresent(Dynamic.getNewLocator(EMAILSUBJECT, sSubject), MEDIUMWAIT);
		Assert.assertTrue(bSubject,"Opened email doesn't have the specified subject");
		String emailBody=safeGetText(Dynamic.getNewLocator(EMAILBODY,sBody),SHORTWAIT);
		Assert.assertEquals(emailBody,sBody,"Opened email with specified subject doesn't have the specified body");
		return new HomePage(driver);
	}
}
