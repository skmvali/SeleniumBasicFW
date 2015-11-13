package com.page.module;



import org.openqa.selenium.WebDriver;

import ru.yandex.qatools.allure.annotations.Step;

import com.page.locators.ContactsLocators;
import com.selenium.Dynamic;
import com.selenium.SafeActions;
//import org.testng.Assert;
import com.testng.Assert;


public class ContactsPage extends SafeActions implements ContactsLocators
{
	private WebDriver driver;
	
	//Constructor to define/call methods	 
	public ContactsPage(WebDriver driver) throws Exception
	{		
		super(driver);
		this.driver = driver;
    } 

	
	/**
	 * Purpose- To verify whether contacts page is being displayed or not
	 * @throws Exception
	 */
	@Step("Verifying contacts page")
	public void verifyContactsPage() throws Exception
	{
		switchToWindow(1);
	     waitUntilElementDisappears(LOADING);
		boolean bContactsPage =  isElementPresent(ADDCONTACTS_BTN, MEDIUMWAIT);
		Assert.assertTrue(bContactsPage,"Add contacts button is not being displayed/recognized on contacts page");
	}
	/**
	 * Purpose- To add contacts from contact page
	 * @param sContactName- we pass contact name
	 * @throws Exception
	 */
	@Step("Adding contacts from Contacts page")
	public void addContacts(String sContactName) throws Exception
	{	
		safeActionsClick(ADDCONTACTS_BTN,VERYLONGWAIT);
		if(isElementVisible(CONTACT_TXTAREA,VERYLONGWAIT))
		safeType(CONTACT_TXTAREA, sContactName,VERYLONGWAIT);
		safeClick(CONTACT_TXTAREA,VERYLONGWAIT);
		safeClick(CREATE_BTN,VERYLONGWAIT);
		safeClick(SAVE_BTN,VERYLONGWAIT);
		verifyAddedContacts();
       
        refresh();
  
	}
	
	
	/**
	 * Purpose- To delete the contacts from contact page
	 * @param - sContactName Name of the contact to be deleted
	 * @throws Exception
	 */
	@Step("Deleting contacts from contacts page")
	public HomePage deleteContacts(String sContactName) throws Exception
	{
		waitForPageToLoad();
		safeActionsClick(Dynamic.getNewLocator(TXT,sContactName),VERYLONGWAIT);
		waitForPageToLoad();
		safeActionsClick(MOREACTIONS_LNK,VERYLONGWAIT);
        safeActionsClick(DELETE,VERYLONGWAIT);
     
		verifyDeletedContacts();
		waitForPageToLoad();
	
        driver.close();
        switchToWindow(0);
     
        
	   return new HomePage(driver);
	}
	/**
	 * purpose-to verify whether contact is added or not
	 */
	
	public void verifyAddedContacts()
	{
		boolean b=isElementPresent(CONTACTSSAVED,VERYLONGWAIT);
	Assert.assertTrue(b,"contact is not added");
	}
	/**
	 * purpose-to verify whether contact is deleted or not
	 */
	public void verifyDeletedContacts()
	{
		boolean b=isElementPresent(DELETED_MSG,VERYLONGWAIT);
		Assert.assertTrue(b,"contact is not deleted");
	}
	
	
	
	
}
