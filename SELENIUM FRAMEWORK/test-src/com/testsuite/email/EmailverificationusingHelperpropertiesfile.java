package com.testsuite.email;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.base.BaseSetup;
import com.datamanager.ConfigManager;
import com.page.data.SendMailData;
import com.page.locators.ComposeLocators;
import com.page.module.ComposePage;
import com.page.module.HomePage;
import com.page.module.LoginPage;
import com.selenium.Sync;
import com.testng.Retry;

public class EmailverificationusingHelperpropertiesfile extends BaseSetup 
{

	LoginPage loginPage;
	HomePage homePage; 
	ComposePage composePage;
	ConfigManager app;
	SendMailData datafields;
	
	@BeforeMethod (alwaysRun=true)
	public void BaseClassSetUp() throws Exception
	{
		loginPage = new LoginPage(getDriver());
		app = new ConfigManager("App");		
		homePage = new HomePage(getDriver());
		//scomposePage=new ComposePage(getDriver());
		datafields = new SendMailData();
        getDriver().get(app.getProperty("App.URL"));
	    (new Sync(getDriver())).waitForPageToLoad();
	}
	
	@Test(groups = "regression", retryAnalyzer=Retry.class, timeOut = 120000) 
	public void SendMailtoOutsider() throws Exception
	
	{
		
		loginPage.verifyLoginPage();
		loginPage.enterLoginCredentials(app.getProperty("App.Username"),app.getProperty("App.Password"));
		homePage = loginPage.clickSignInButton();
		composePage =homePage.clickComposeButton();
	    composePage.verifyComposePage();
	    composePage.fillMaildata(datafields);
	
		
	}
	
	
		/**
		 * Purpose- This method contains the logic for 'Gmail Logout' functionality in Gmail
		 * @throws Exception
		 */
		@AfterMethod(alwaysRun=true)
		public void testGmailLogout() throws Exception
		{
			//Logout from Gmail
			homePage.clickLogOutButton(app.getProperty("App.Username"));			
			homePage.verifyLogOut();
		}
}

		

	
	
	

