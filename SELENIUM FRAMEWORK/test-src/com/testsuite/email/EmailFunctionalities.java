/*************************************** PURPOSE **********************************

 - This class contains function calls related to Email functionality of the application
 
*/

package com.testsuite.email;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.base.BaseSetup;
import com.page.data.EmailData;
import com.page.data.SendMailData;
import com.page.module.ComposePage;
import com.page.module.DraftsPage;
import com.page.module.HomePage;
import com.page.module.InboxPage;
import com.page.module.LoginPage;
import com.selenium.Sync;
import com.testng.Retry;





//Each TestSuite class must do the following
//1. It should extends the "BaseClass", so that @BeforeClass - BrowserInitialization, 
//	 launch, URL Navigation and @AfterClass - Browser Quit happens automatically
//2. It should create instances for respective PageParts classes along with LoadProperties class
//3. Create @BeforeMethod setup to initalize instances with getDriver(taken from Baseclass)

public class EmailFunctionalities extends BaseSetup
{

//Declaration of respective PageParts instances along with configmanager instance

	LoginPage loginPage;
	HomePage homePage;
	ComposePage composePage;
	DraftsPage draftsPage;
	InboxPage inboxPage;
	SendMailData datafields;
	EmailData emailData;
	
	//Initialize Pageparts instances with Webdrivers getter method - getDriver()
	@BeforeMethod (alwaysRun=true)
	public void BaseClassSetUp() throws Exception
	{
		loginPage = new LoginPage(getDriver());
		datafields = new SendMailData();		
		emailData = new EmailData(); 
		getDriver().manage().deleteAllCookies();
		getDriver().get(emailData.gmailURL);
		(new Sync(getDriver())).waitForPageToLoad();
	}
				
	//Each test method in TestSuite files must follow the below guidelines
	//1. Each test method be grouped. e.g (groups = {"smoke"})
	//2. Most of the content of test methods should only be self-explanatory function calls
	
	/**
	 *  This test login to Gmail with credentials supplied at config.properties file and 
	 *  sends an email(without attachments) to the same email account and
	 *  verifies if email sends successfully by verifying "Email sent successfully" message
	 *  
	 *  This test fails if any of the expected UI object not found on the screen or 
	 *  when test timeout expires or browser crashed or expected verification mismatched (email successfully sent pop up not displayed)
	 *  
	 *  If test fails, it runs one more time to confirm the failure and it is not intermittent
	 * 
	 */
		
	@Test (groups = "regression", retryAnalyzer=Retry.class, timeOut = 120000)
	public void Tc001_testSendingEmailWithSubjectAndBody() throws Exception
	{	
	
		loginPage.verifyLoginPage();
		loginPage.enterLoginCredentials(emailData.gmailUsername, emailData.gmailPassword);			
		homePage = loginPage.clickSignInButton();
		
		composePage = homePage.clickComposeButton();
		composePage.verifyComposePage();
		composePage.enterTo_Subject_BodyFields(datafields);
		composePage.clickSendButton();
		composePage.verifySentMailMessage();

	}
	
	/**
	 * Purpose- This method contains the logic for 'Save Mail to drafts' Functionality in Gmail
	 * @throws Exception
	 */
	@Test (groups = "regression", retryAnalyzer=Retry.class, timeOut = 120000)
	public void Tc002_testSaveEmailWithSubjectAndBody() throws Exception
	{			
		//Login to Gmail application		
		loginPage.verifyLoginPage();		
		loginPage.enterLoginCredentials(emailData.gmailUsername, emailData.gmailPassword);
		
		HomePage homePage = loginPage.clickSignInButton();		
		homePage.verifyLogin(emailData.gmailUsername);
		
		//Save an email to drafts		
		composePage = homePage.clickComposeButton();		
		composePage.verifyComposePage();		
		composePage.enterTo_Subject_BodyFields(datafields);

		homePage = composePage.clickSaveButton();		
		draftsPage = homePage.clickDraftsLink();		
		draftsPage.verifyDraftsPage();
		draftsPage.verifySavedMailInDrafts(emailData.subject);		

	}

	/**
	 * Purpose- This method contains the logic for 'Check Mail in Inbox' Functionality in Gmail
	 * @throws Exception
	 */
	@Test (groups = "regression",retryAnalyzer=Retry.class, timeOut = 120000) //dependsOnMethods={"Tc001_testSendingEmailWithSubjectAndBody"} -> Testng dependency
	public void Tc003_testCheckEmailInInbox() throws Exception
	{		
		hasDependentTestMethodPassed("Tc001_testSendingEmailWithSubjectAndBody");	// self defined dependency...
		
		//Login to Gmail application		
		loginPage.verifyLoginPage();		
		loginPage.enterLoginCredentials(emailData.gmailUsername, emailData.gmailPassword);		

		homePage = loginPage.clickSignInButton();		
		homePage.verifyLogin(emailData.gmailUsername);
		
		//checks the email of specified subject			
		inboxPage = homePage.clickInboxLink();		
		inboxPage.clickMail(emailData.subject);
		inboxPage.verifyOpenedMail(emailData.subject,emailData.body);		
	}


	
	/**
	 * Purpose- This method contains the logic for 'Gmail Logout' functionality in Gmail
	 * @throws Exception
	 */
	@AfterMethod
	public void testGmailLogout() throws Exception
	{
		//Logout from Gmail
		homePage.clickLogOutButton(emailData.gmailUsername);			
		homePage.verifyLogOut();
	}

}
