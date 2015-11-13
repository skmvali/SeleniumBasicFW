/*************************************** PURPOSE **********************************

 - This class contains Example of how to run data driven tests by using TestNG @DataProvider annotation with XML as the external source
 */
package com.testsuite.dataprovider;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.base.BaseSetup;
import com.datamanager.ConfigManager;
import com.datamanager.XMLManager;
import com.page.module.ContactsPage;
import com.page.module.HomePage;
import com.page.module.LoginPage;
import com.selenium.Sync;

public class DataDrivenTestWithXML extends BaseSetup {

	private LoginPage loginPage;
	private ConfigManager app;
	private HomePage homePage;
	private ContactsPage contactsPage;
	private String xmlFilePath = System.getProperty("user.dir")
			+ "\\Data\\Contacts.xml";
	XMLManager xml = new XMLManager(xmlFilePath);
	/**
	 * xpath should start with parent node and traverse to the node based on
	 * requirement and should not start with '/'
	 */
	private String xPath = "Gmail/credentials/username";

	// Initialize Page parts instances with Web drivers getter method -
	@BeforeClass
	public void BaseClassSetUp() throws Exception {
		loginPage = new LoginPage(getDriver());
		app = new ConfigManager("App");
		getDriver().manage().deleteAllCookies();
	}

	/**
	 * Purpose- Navigate to the URL and login to gmail for every set of data
	 */
	@BeforeMethod
	public void setup() throws Exception {

		// navigate to URl
		getDriver().get(app.getProperty("App.URL"));
		(new Sync(getDriver())).waitForPageToLoad();

		// Login to Gmail application
		loginPage.verifyLoginPage();
		loginPage.enterLoginCredentials(app.getProperty("App.Username"),
				app.getProperty("App.Password"));
		homePage = loginPage.clickSignInButton();
		homePage.verifyLogin(app.getProperty("App.Username"));

		// Navigate to Contacts Page and verify contacts page
		contactsPage = homePage.navigateToContactsPage();
		contactsPage.verifyContactsPage();
	}

	/**
	 * Purpose- This DataProvide method will store the values of all the
	 * children present in all the matched nodes of given XPath from the xml in
	 * Object and pass the data to a @Test method.
	 * 
	 */
	@DataProvider(name = "GetContactsFromXML")
	public Object[][] getContactsToAdd() {
		return xml.getChildrenData(xPath);
	}

	/**
	 * Purpose- This test method declares that its data should be supplied by
	 * the @dataProvider method named 'GetContactsFromXML' Add the contacts red
	 * from the excel and delete the added contact
	 * 
	 * Each set of data will count as single test case. In the final report user
	 * can verify with which set of data the tests are passed/failed
	 * 
	 */
	@Test(dataProvider = "GetContactsFromXML")
	public void addContactsFromXml(String fn, String ln, String n)
			throws Exception {

		// Get Contact list from xml
		contactsPage.addContacts(fn+ln+n);

		// Delete Added Contacts
		homePage=contactsPage.deleteContacts(fn+ln+n);
	}

	/**
	 * Purpose- This method contains the logic for 'Gmail Logout' functionality
	 * in Gmail. It will trigger for every data set for @Test
	 * 
	 * @throws Exception
	 */
	@AfterMethod
	public void testGmailLogout() throws Exception {
		homePage.clickLogOutButton(app.getProperty("App.Username"));
		homePage.verifyLogOut();
	}
}