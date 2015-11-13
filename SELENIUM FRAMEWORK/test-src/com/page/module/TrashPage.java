package com.page.module;

import org.openqa.selenium.WebDriver;

import ru.yandex.qatools.allure.annotations.Step;

import com.page.locators.TrashLocators;
import com.selenium.SafeActions;
//import org.testng.Assert;
import com.testng.Assert;


public class TrashPage extends SafeActions implements TrashLocators
{
	private WebDriver driver;
	
	//Constructor to define/call methods	 
	public TrashPage(WebDriver driver) throws Exception
	{		
		super(driver);
		this.driver = driver;
    } 


	/**
	 * Purpose- To verify whether trash page is being displayed or not
	 * @throws Exception
	 */
	@Step("Verifying trash page")
	public HomePage verifyTrashPage() throws Exception
	{
		waitUntilElementDisappears(LOADING);
		boolean bTrashPage = isElementPresent(TRASH_PAGE, SHORTWAIT);
		Assert.assertTrue(bTrashPage,"The verification message on trash page is not being displayed on clicking trash link");
		return new HomePage(driver);
	}
}
