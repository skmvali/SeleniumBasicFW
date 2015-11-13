/*************************************** PURPOSE **********************************

 - This class contains function calls related to Email functionality of the application
*/


package com.testsuite.contactsandfooter;


import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.base.BaseSetup;
import com.datamanager.ConfigManager;
import com.page.module.ContactsPage;
import com.page.module.HomePage;
import com.page.module.LoginPage;
import com.selenium.Sync;
import com.testng.Retry;




//Each TestSuite class must do the following
//1. It should extends the "BaseClass", so that @BeforeClass - BrowserInitialization, 
//	 launch, URL Navigation and @AfterClass - Browser Quit happens automatically
//2. It should create instances for respective PageParts classes along with LoadProperties class
//3. Create @BeforeMethod setup to initalize instances with getDriver(taken from Baseclass)

public class ContactsAndFooters extends BaseSetup
{

//Declaration of respective PageParts instances along with configmanager instance

	LoginPage loginPage;
	ConfigManager app;
	HomePage homePage;
	ContactsPage contactsPage;
	
	
//Initialize Pageparts instances with Webdrivers getter method - getDriver()
	@BeforeMethod
	public void BaseClassSetUp() throws Exception
	{
		loginPage = new LoginPage(getDriver());
		app = new ConfigManager("App");
		getDriver().manage().deleteAllCookies();
		getDriver().get(app.getProperty("App.URL"));
		(new Sync(getDriver())).waitForPageToLoad();
	}
			
	
	//Each test method in TestSuite files must follow the below guidelines
	//1. Each test method be grouped. e.g (groups = {"smoke"})
	//2. Each test method must use ReportHelper.setsTestName("Appropriate name for your test") for Reporting purposes
	//3. Most of the content of test methods should only be self-explanatory function calls

	/**
	 * Purpose- This method contains the logic for 'Check footer links' functionality in Gmail
	 * @throws Exception
	 */
   @Test (groups = "regression", retryAnalyzer=Retry.class, timeOut = 120000)
	public void testGmailFooterLinks() throws Exception
	{				
		//Login to Gmail application		
		loginPage.verifyLoginPage();	
		loginPage.enterLoginCredentials(app.getProperty("App.Username"), app.getProperty("App.Password"));		
		homePage = loginPage.clickSignInButton();
		
		homePage.verifyLogin(app.getProperty("App.Username"));		
		homePage.verifyTermsAndPrivacyLink();		
		homePage.verifyDetailsFooterLink();	
	 
	}
	
	/**
	 * Purpose- This method contains the logic for 'Add and delete contacts' functionality in Gmail
	 * @throws Exception
	 */
	@Test(groups = "regression", retryAnalyzer=Retry.class, timeOut =150000)
	public void test_AddAndDeleteContacts() throws Exception
	{	
		//Login to Gmail application		
		loginPage.verifyLoginPage();	
		loginPage.enterLoginCredentials(app.getProperty("App.Username"), app.getProperty("App.Password"));	
		homePage = loginPage.clickSignInButton();	
		homePage.verifyLogin(app.getProperty("App.Username"));
		
		//Add and delete Contacts after logging in	
		contactsPage = homePage.navigateToContactsPage();		
		contactsPage.verifyContactsPage();		
		contactsPage.addContacts(app.getProperty("App.Contact"));
		
		homePage=contactsPage.deleteContacts(app.getProperty("App.Contact"));
	}
	@AfterMethod
	public void testGmailLogout() throws Exception {
		
		homePage.clickLogOutButton(app.getProperty("App.Username"));
		homePage.verifyLogOut();
	}
	
	
	
	
}