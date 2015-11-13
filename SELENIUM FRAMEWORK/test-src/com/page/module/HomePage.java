package com.page.module;

import org.openqa.selenium.WebDriver;

import ru.yandex.qatools.allure.annotations.Step;

import com.page.locators.HomeLocators;
import com.selenium.Dynamic;
import com.selenium.SafeActions;
//import org.testng.Assert;
import com.testng.Assert;


public class HomePage extends SafeActions implements HomeLocators
{
	private WebDriver driver;
	
	//Constructor to define/call methods	 
	public HomePage(WebDriver driver) throws Exception
	{		
		super(driver);
		this.driver = driver;
    } 
	
	/**
	 * Purpose- To verify whether login is successful or not
	 * @throws Exception
	 */
	@Step("Verifying Gmail user name after logging in")
	public void verifyLogin(String sMailId) throws Exception
	{
			boolean bIsGmailUserNameExists = isElementPresent(Dynamic.getNewLocator(GMAIL_USER_BTN,sMailId), LONGWAIT);
			Assert.assertTrue(bIsGmailUserNameExists,"Username is not being displayed after clicking on SignIn button");
	}
	
	/**
	 * Purpose- To click on compose button
	 * @throws Exception
	 */
	@Step("Clicking on Compose button")
	public ComposePage clickComposeButton() throws Exception
	{		
		safeClick(COMPOSE_BTN, SHORTWAIT);	
		return new ComposePage(driver);
	}
	
	/**
	 * Purpose- To navigate to Drafts page
	 * @throws Exception
	 */
	@Step("Clicking on Darfts Link")
	public DraftsPage clickDraftsLink() throws Exception
	{
//		Thread.sleep(1000);
		safeClick(DRAFTS_LINK,SHORTWAIT);
		return new DraftsPage(driver);
	}
	
	/**
	 * Purpose- To navigate to trash page
	 * @throws Exception
	 */
	@Step("Navigating to trash page")
	public TrashPage navigateToTrashPage() throws Exception
	{
		safeClick(MORE_LNK,SHORTWAIT);
		safeClick(TRASH_LNK,SHORTWAIT);
		return new TrashPage(driver);
	}
	
	/**
	 * Purpose- To navigate to contacts page
	 * @throws Exception
	 */
	@Step("Navigating to contacts page")
	public ContactsPage navigateToContactsPage() throws Exception
	{
		safeClick(GMAIL_DRP, MEDIUMWAIT);
		safeClick(CONTACTS, LONGWAIT);
		waitForPageToLoad();
		return new ContactsPage(driver);
	}
	
	/**
	 * Purpose- To navigate to Inbox
	 * @throws Exception
	 */
	@Step("Clicking in Inbox link")
	public InboxPage clickInboxLink() throws Exception
	{
		safeClick(INBOX_LNK, SHORTWAIT);
		return new InboxPage(driver);
	}
	
	/**
	 * Purpose- To verify Details footer link
	 * @throws Exception
	 */
	@Step("Verifying Details footer link")
	public void verifyDetailsFooterLink() throws Exception 
	{
		
//		Thread.sleep(5000);
		safeClick(DETAILS_LNK, SHORTWAIT);
		waitForPageToLoad();
		switchToWindow(1);
		boolean bDetailspage = isElementPresent(ACTIVITYACCOUNT_LNK, SHORTWAIT);
		Assert.assertTrue(bDetailspage,"On clicking Footer link- Details, Account details page is not being displayed");
		driver.close();
		switchToWindow(0);
		
	}

	/**
	 * Purpose to verify terms and privacy footer link
	 * @throws Exception
	 */
	@Step("Verifying Terms and Privacy links")
	public void verifyTermsAndPrivacyLink() throws Exception 
	{
	    safeClick(PRIVACY_LNK,MEDIUMWAIT);	
		waitForPageToLoad();
		switchToWindow(1);
		Assert.assertTrue(isAllLinksOnTermsAndPrivacyPagedisplayed(),"Terms and Privacy page is not being displayed successfully on clicking Terms & Privacy footer link");
		driver.close();
		switchToWindow(0);
	
	}
	
	/**
	 * Purpose- to verify the links on Terms and privacy page
	 * @return
	 * @throws Exception
	 */
	public boolean isAllLinksOnTermsAndPrivacyPagedisplayed() throws Exception
	{
		boolean bFaq = isElementPresent(FAQ_LNK, MEDIUMWAIT);
		boolean bPrivacyPolicy = isElementPresent(PRIVACYPOLICY_LNK,MEDIUMWAIT);
		boolean bOverview = isElementPresent(OVERVIEW_LNK, MEDIUMWAIT);
		boolean bTermsofService = isElementPresent(TERMSOFSERVICE_LNK,MEDIUMWAIT);
		boolean bAllLinks = (bFaq && bPrivacyPolicy && bOverview && bTermsofService);
		return bAllLinks;
	}
	
	/**
	 * Purpose- click on Username and click on logout button
	 * @throws Exception
	 */
	@Step("Clicking on Logout link")
	public void clickLogOutButton(String sMailId) throws Exception 
	{	
		safeClick(Dynamic.getNewLocator(GMAIL_USER_BTN,sMailId), MEDIUMWAIT);
		safeClick(GMAIL_SIGNOUT_BTN,MEDIUMWAIT);
		waitForPageToLoad();
		
	}
	
	/**
	 * Purpose- To verify whether logout is successful or not
	 * @throws Exception
	 */
	@Step("Verifying logout page")
	public LoginPage verifyLogOut() throws Exception 
	{
		boolean bLogout = isElementPresent(GMAIL_SIGNIN_BTN, MEDIUMWAIT);		
		Assert.assertTrue(bLogout,"Sign In button is not being displayed on Login page after clicking on SignOut button");
		return new LoginPage(driver);
	}
}
