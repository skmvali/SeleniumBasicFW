/*************************************** PURPOSE **********************************

 - This class contains Example of how to run data driven tests by using TestNG @DataProvider annotation with Excel as the external source
 */

package com.testsuite.dataprovider;

import java.io.IOException;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.base.BaseSetup;
import com.datamanager.ConfigManager;
import com.datamanager.ExcelManager;
import com.page.module.ContactsPage;
import com.page.module.HomePage;
import com.page.module.LoginPage;
import com.selenium.Sync;

public class DataDrivenTestWithExcel extends BaseSetup {

	private LoginPage loginPage;
	private ConfigManager app;
	private HomePage homePage;
	private ContactsPage contactsPage;
	private String file = System.getProperty("user.dir") + "\\Data\\Testing.xlsx";

	// Initialize Page parts instances with Web drivers getter method -
	@BeforeClass
	public void BaseClassSetUp() throws Exception {
		loginPage = new LoginPage(getDriver());
		app = new ConfigManager("App");
		getDriver().manage().deleteAllCookies();
	}

	/**
	 * Purpose- Navigate to the URL and login to gmail for every set of data
	 * @Beforemethod will Trigger for each set of data
	 */
	@BeforeMethod(alwaysRun=true)
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
	 * Purpose- This DataProvider method will store all the content red from the
	 * excel and pass the data to a @Test method.
	 */
	@DataProvider(name = "GetContactsFromExcel")
	public String[][] getContactsToAdd() {
		ExcelManager excel = null;
//		try {
			excel = new ExcelManager(file);
			
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		return excel.getExcelSheetData("contacts");
	}

	/**
	 * Purpose- This test method declares that its data should be supplied by
	 * the @dataProvider method named 'GetContactsFromExcel' Add the contacts
	 * red from the excel and delete the added contact
	 * 
	 * Each set of data will count as single test case. In the final report user
	 * can verify with set of data the tests are passed/failed
	 * 
	 * The thumb rule is -> Number of columns in excel = paremeters passing to @test
	 * method
	 * 
	 * The number of columns in excel and parameter count in @test method should
	 * be same number if not tests will failed with 'null pointer exception'.
	 * The parameter usage is optional
	 * 
	 * @throws Exception
	 */
	@Test(dataProvider = "GetContactsFromExcel")
	public void addContactsFromExcel(String contactName, String desc)
			throws Exception {

		// Get Contact list from Excel
		contactsPage.addContacts(contactName);
		// Delete Added Contacts
		homePage=contactsPage.deleteContacts(contactName);
	}

	/**
	 * Purpose- This method contains the logic for 'Gmail Logout' functionality
	 * in Gmail. It will execute for every data set passed for @Test
	 * 
	 * @Aftermethod will Trigger for each set of data
	 * 
	 * @throws Exception
	 */
	@AfterMethod
	public void testGmailLogout() throws Exception {
		homePage.clickLogOutButton(app.getProperty("App.Username"));
		homePage.verifyLogOut();
	}
}