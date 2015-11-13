package com.testsuite.other;


import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.base.BaseSetup;
import com.datamanager.ConfigManager;
import com.page.module.HomePage;
import com.page.module.LoginPage;
import com.page.module.TrashPage;
import com.selenium.Sync;
import com.testng.Retry;




//Each TestSuite class must do the following
//1. It should extends the "BaseClass", so that @BeforeClass - BrowserInitialization, 
//	 launch, URL Navigation and @AfterClass - Browser Quit happens automatically
//2. It should create instances for respective PageParts classes along with LoadProperties class
//3. Create @BeforeMethod setup to initalize instances with getDriver(taken from Baseclass)

public class OtherLinks extends BaseSetup
{
	//Declaration of respective PageParts instances along with configmanager instance

	 
		LoginPage loginPage;
		ConfigManager app;
		HomePage homePage;
		TrashPage trashPage;
		
	//Initialize Pageparts instances with Webdrivers getter method - getDriver()
		
		@BeforeMethod(alwaysRun=true) 
		public void BaseClassSetUp() throws Exception
		{
			
			
			app = new ConfigManager("App");
			loginPage = new LoginPage(getDriver());
		     homePage = new HomePage(getDriver());
		     getDriver().manage().deleteAllCookies();
		    getDriver().get(app.getProperty("App.URL"));
				(new Sync(getDriver())).waitForPageToLoad();
		
			
		}
				
		
		/**
		 * Purpose- This method contains the logic for 'View Trash Page' functionality in Gmail
		 * @throws Exception
		 */
		@Test(groups = "regression", retryAnalyzer=Retry.class, timeOut = 120000)
		public void testGmailTrash() throws Exception
		{	
					
			loginPage.verifyLoginPage();
			loginPage.enterLoginCredentials(app.getProperty("App.Username"), app.getProperty("App.Password"));
			
			homePage = loginPage.clickSignInButton();
			homePage.verifyLogin(app.getProperty("App.Username"));
			trashPage = homePage.navigateToTrashPage();
		    homePage= trashPage.verifyTrashPage();
			
		}
		@AfterMethod(alwaysRun=true)
		public void testGmailLogout() throws Exception
		{
			//Logout from Gmail
		     homePage.clickLogOutButton(app.getProperty("App.Username"));			
			  homePage.verifyLogOut();
		}


}