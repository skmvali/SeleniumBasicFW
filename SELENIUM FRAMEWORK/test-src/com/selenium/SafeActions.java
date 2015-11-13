/*************************************** PURPOSE **********************************

 - This class contains all UserAction methods
 */

package com.selenium;


import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NoSuchFrameException;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.sikuli.api.DesktopScreenRegion;
import org.sikuli.api.ImageTarget;
import org.sikuli.api.ScreenRegion;
import org.sikuli.api.Target;
import org.sikuli.api.robot.Mouse;
import org.sikuli.api.robot.desktop.DesktopMouse;
import org.sikuli.api.visual.Canvas;
import org.sikuli.api.visual.DesktopCanvas;

import com.datamanager.ConfigManager;
import com.passworddecoder.PasswordDecorder;
import com.testng.Assert;
import com.utilities.ReportSetup;


public class SafeActions extends Sync
{

	//Local WebDriver instance
	WebDriver driver;
	Logger log =Logger.getLogger("SafeActions");
	ConfigManager sys = new ConfigManager();

	//Constructor to initialize the local WebDriver variable with the WebDriver variable that,
	//has been passed from each PageParts Java class
	public SafeActions(WebDriver driver)
	{
		super(driver);
		this.driver=driver;
	}

	/**
	 * Method - Safe Method for User Click, waits until the element is loaded and then performs a click action
	 * @param locator
	 * @param waitTime
	 */
	public void safeClick(By locator, int... optionWaitTime)
	{
		int waitTime = 0;
		try
		{
			waitTime =  getWaitTime(optionWaitTime);
			if(waitUntilClickable(locator, waitTime))
			{
				scrollIntoElementView(locator);
				WebElement element = driver.findElement(locator);
				setHighlight(element);
				element.click();		
				log.info("Clicked on the element " + locator);
			}
			else
			{
				log.error("Unable to find element " + locator + "in time - "+waitTime+" Seconds");
				Assert.fail("Unable to find element " + locator + "in time - "+waitTime+" Seconds");
			}
		}
		catch(StaleElementReferenceException e)
		{
			log.error("Element with " + locator + " is not attached to the page document - StaleElementReferenceException");
			Assert.fail("Element with " + locator + " is not attached to the page document - StaleElementReferenceException");
		}
		catch (NoSuchElementException e)
		{
			log.error("Element " + locator + " was not found in DOM in time - "+waitTime+" Seconds"+" - NoSuchElementException");	
			Assert.fail("Element " + locator + " was not found in DOM in time - "+waitTime+" Seconds"+" - NoSuchElementException");
		}
		catch(Exception e)
		{
			log.error("Element " + locator + " was not clickable" + " - " + e);		
			Assert.fail("Element " + locator + " was not found on the web page");
		}
	}

	/**
	 * Method - Safe Method for User Click using Actions.click, waits until the element is loaded and then performs a click action
	 * @param locator
	 * @param waitTime
	 */

	public void safeActionsClick(By locator,int waitTime)
	{
		try
		{
			if(isElementVisible(locator, waitTime))
			{
				WebElement element=driver.findElement(locator);
				setHighlight(element);
				Actions builder = new Actions(driver);
				builder.moveToElement(element).click().build().perform();
				log.info("Clicked on element " + locator);
			}
			else
			{     
				log.error("Element "+locator+" was not visible to click in time - "+waitTime+" Seconds");
				Assert.fail("Element "+locator+" was not visible to click in time - "+waitTime+" Seconds");
			}
		}
		catch(StaleElementReferenceException e)
		{
			log.error("Element with " + locator + " is not attached to the page document - StaleElementReferenceException");
			Assert.fail("Element with " + locator + " is not attached to the page document - StaleElementReferenceException");
		}
		catch (NoSuchElementException e)
		{
			log.error("Element " + locator + " was not found in DOM in time - "+waitTime+" Seconds"+" - NoSuchElementException"); 
			Assert.fail("Element " + locator + " was not found in DOM in time - "+waitTime+" Seconds"+" - NoSuchElementException");
		}
		catch(Exception e)
		{
			log.error("Unable to click the cursor on " + locator + " - " + e); 
			Assert.fail("Element "+locator+" was not visible on the web page");
		}
	}


	/**
	 * Method - Safe Method for User Double Click, waits until the element is loaded and then performs a double click action
	 * @param locator
	 * @param waitTime
	 */
	public void safeDblClick(By locator, int... optionWaitTime)
	{
		int waitTime=0;
		try
		{
			waitTime =  getWaitTime(optionWaitTime);
			if(waitUntilClickable(locator, waitTime))
			{
				scrollIntoElementView(locator);
				WebElement element = driver.findElement(locator);
				setHighlight(element);
				Actions userAction = new Actions(driver).doubleClick(element);
				userAction.build().perform();
				log.info("Double clicked the element " + locator);
			}
			else
			{			
				log.error("Unable to find element " + locator+" in web page in time - "+waitTime);
				Assert.fail("Unable to find element " + locator+" in web page in time - "+waitTime);
			}
		}
		catch(StaleElementReferenceException e)
		{
			log.error("Element with " + locator + " is not attached to the page document - StaleElementReferenceException");
			Assert.fail("Element with " + locator + " is not attached to the page document - StaleElementReferenceException");
		}
		catch (NoSuchElementException e)
		{
			log.error("Element " + locator + " was not found in DOM in time - "+waitTime+" - NoSuchElementException");	
			Assert.fail("Element " + locator + " was not found in DOM in time - "+waitTime+" - NoSuchElementException");
		}
		catch(Exception e)
		{
			log.error("Element " + locator + " was not clickable" + " - " + e);		
			Assert.fail("Unable to find element " + locator+" in web page");
		}
	}



	/**
	 * Method - Safe Method for User Clear and Type, waits until the element is loaded and then enters some text
	 * @param locator
	 * @param sText
	 * @param waitTime
	 */
	public void safeClearAndType(By locator, String text, int... optionWaitTime)
	{
		int waitTime=0;
		try
		{
			waitTime =  getWaitTime(optionWaitTime);
			if(isElementPresent(locator, waitTime))
			{
				scrollIntoElementView(locator);
				WebElement element=driver.findElement(locator);
				setHighlight(element);
				element.clear();
				element.sendKeys(text);
				log.info("Cleared the field and entered - '"+text+" in the element - " + locator);
			}
			else
			{			
				log.error("Text " + text + " to be entered after " + " clear the field with " + locator + " was not found in DOM in time - "+waitTime);
				Assert.fail("Text " + text + " to be entered after " + " clear the field with " + locator + " was not found in DOM in time - "+waitTime);
			}
		}
		catch(StaleElementReferenceException e)
		{
			log.error("Text " + text + " to be entered after " + " clear the field with " + locator + " is not attached to the page document - StaleElementReferenceException");
			Assert.fail("Text " + text + " to be entered after " + " clear the field with " + locator + " is not attached to the page document - StaleElementReferenceException");
		}
		catch (NoSuchElementException e)
		{
			log.error("Text " + text + " to be entered after " + " clear the field with " + locator + " was not found in DOM in time - "+waitTime+" - NoSuchElementException");	
			Assert.fail("Text " + text + " to be entered after " + " clear the field with " + locator + " was not found in DOM in time - "+waitTime+" - NoSuchElementException");
		}
//		catch(NullPointerException e){
//			log.error("Text provided as null...Please provide text");
//			Assert.fail("Text provided as null...Please provide text");
//		}
		catch(Exception e)
		{
			log.error("Unable to clear and enter '" + text + "' text in field with locator -"+ locator + " - " + e);
			Assert.fail("Text " + text + " to be entered after " + " clear the field with " + locator + " was not found in DOM");
		}
	}

	/**
	 * Method - Safe Method for User Type, waits until the element is loaded and then enters some text
	 * @param locator
	 * @param sText
	 * @param waitTime
	 */
	public void safeType(By locator, String text, int... optionWaitTime)
	{
		int waitTime=0;
		try
		{
			waitTime =  getWaitTime(optionWaitTime);
			if(isElementPresent(locator, waitTime))
			{
				scrollIntoElementView(locator);
				WebElement element=driver.findElement(locator);
				setHighlight(element);
				element.sendKeys(text);
				log.info("Entered - '" + text + " in the element - " + locator);
			}
			else
			{
				log.error("Unable to enter " + text + " in field " + locator+" in time - "+waitTime+" Seconds");
				Assert.fail("Unable to enter " + text + " in field " + locator+" in time - "+waitTime+" Seconds");
			}
		}
		catch(StaleElementReferenceException e)
		{
			log.error("Text " + text + " to be entered in the field with " + locator + " is not attached to the page document - StaleElementReferenceException");
			Assert.fail("Text " + text + " to be entered in the field with " + locator + " is not attached to the page document - StaleElementReferenceException");
		}
		catch (NoSuchElementException e)
		{
			log.error("Text " + text + " to be entered in the field with " + locator + " is not attached to the page document in time - "+waitTime+" - NoSuchElementException");
			Assert.fail("Text " + text + " to be entered in the field with " + locator + " is not attached to the page document in time - "+waitTime+" - NoSuchElementException");
		}
//		catch(NullPointerException e){
//			log.error("some variables may failed to initialize... please check");
//			Assert.fail("some variables may failed to initialize... please check");
//		}
		catch(Exception e)
		{
			log.error("Unable to enter '" + text + "' text in field with locator -"+ locator + " - " + e);
			Assert.fail("Unable to enter '" + text + "' text in field with locator -"+ locator);
		}
	}

	/**
	 * Method - Safe Method to Type Encrypted password, waits until the element is loaded, decrecpt the encrypted password and then enter into the password field
	 * @param locator
	 * @param encryptedText
	 * @param waitTime
	 */
	public void safeTypePassword(By locator, String encryptedText, int... optionWaitTime)
	{
		int waitTime=0;
		try
		{
			waitTime =  getWaitTime(optionWaitTime);
			if(isElementPresent(locator, waitTime))
			{
				scrollIntoElementView(locator);
				WebElement element=driver.findElement(locator);
				setHighlight(element);
				element.sendKeys(PasswordDecorder.passwordDecrypt(encryptedText));
				log.info("Entered - '" + encryptedText + " in the password field - " + locator);
			}
			else
			{
				log.error("Encrypted Text " + encryptedText + " to be entered in the field with " + locator + " is not attached to the page document it time - "+waitTime);
				Assert.fail("Encrypted Text " + encryptedText + " to be entered in the field with " + locator + " is not attached to the page document in time - "+waitTime);
			}
		}
		catch(StaleElementReferenceException e)
		{
			log.error("Password " + encryptedText + " to be entered in the field with " + locator + " is not attached to the page document - StaleElementReferenceException");
			Assert.fail("Password " + encryptedText + " to be entered in the field with " + locator + " is not attached to the page document - StaleElementReferenceException");
		}
		catch (NoSuchElementException e)
		{
			log.error("Password " + encryptedText + " to be entered in the field with " + locator + " was not found in DOM in time - "+waitTime+" - NoSuchElementException");
			Assert.fail("Password " + encryptedText + " to be entered in the field with " + locator + " was not found in DOM in time - "+waitTime+" - NoSuchElementException");
		}
//		catch(NullPointerException e){
//			log.error("some variables may failed to initialize... please check");
//			Assert.fail("some variables may failed to initialize... please check");
//		}
		catch(Exception e)
		{
			log.error("Unable to enter '"  + " - " + encryptedText + "' text in field with locator"+ locator + " - " + e);
			Assert.fail("Unable to enter '" + encryptedText+ "' text in field with locator"+ locator);
		}
	}


	/**
	 * Method - Safe Method for Radio button selection, waits until the element is loaded and then selects Radio button
	 * @param locator
	 * @param waitTime
	 * @return - boolean (returns True when the Radio button is selected else returns false)
	 * @throws Exception
	 */
	public void safeSelectRadioButton(By locator, int... optionWaitTime)
	{
		int waitTime=0;
		try
		{
			waitTime =  getWaitTime(optionWaitTime);
			if(waitUntilClickable(locator, waitTime))
			{
				scrollIntoElementView(locator);
				WebElement element = driver.findElement(locator);
				setHighlight(element);
				element.click();		
				log.info("Clicked on radio button with locator '" + locator + "'' ");
			}
			else			
			{
				log.error("Unable to select Radio button "+locator+" in time - "+waitTime);
				Assert.fail("Unable to select Radio button "+locator+" in time - "+waitTime);
			}
		}
		catch(StaleElementReferenceException e)
		{
			log.error("Element with " + locator + " is not attached to the page document - StaleElementReferenceException");
			Assert.fail("Element with " + locator + " is not attached to the page document - StaleElementReferenceException");
		}
		catch (NoSuchElementException e)
		{
			log.error("Element " + locator + " was not found in DOM in time - "+waitTime+" - NoSuchElementException");	
			Assert.fail("Element " + locator + " was not found in DOM in time - "+waitTime+" - NoSuchElementException");
		}
		catch(Exception e)
		{
			log.error("Unable to click on radio button with locator '" + locator + "' - " + e);		
			Assert.fail("Element " + locator + " was not found in DOM");	
		}	
	}


	/**
	 * Method - Safe Method for checkbox selection, waits until the element is loaded and then selects checkbox
	 * @param locator
	 * @param waitTime
	 * @return - boolean (returns True when the checkbox is selected else returns false)
	 * @throws Exception
	 */
	public void safeCheck(By locator, int... optionWaitTime)
	{
		int waitTime=0;
		try
		{
			waitTime =  getWaitTime(optionWaitTime);
			if(isElementPresent(locator, waitTime))
			{
				scrollIntoElementView(locator);
				WebElement checkBox = driver.findElement(locator);
				setHighlight(checkBox);
				if(checkBox.isSelected())
					log.info("CheckBox " + locator + "is already selected");
				else{
					checkBox.click();
					log.info("Checkbox with locator '" + locator + "'' " + "is selected");
				}
			}
			else
			{			
				log.error("Element " + locator + " was not found in DOM in time - "+waitTime);
				Assert.fail("Element " + locator + " was not found in DOM in time - "+waitTime);
			}
		}
		catch(StaleElementReferenceException e)
		{
			log.error("Element with " + locator + " is not attached to the page document in time - "+waitTime);
			Assert.fail("Element with " + locator + " is not attached to the page document in time - "+waitTime);
		}
		catch (NoSuchElementException e)
		{
			log.error("Element " + locator + " was not found in DOM in time - "+waitTime+" - NoSuchElementException");
			Assert.fail("Element " + locator + " was not found in DOM in time - "+waitTime+" - NoSuchElementException");
		}
		catch(Exception e)
		{
			log.error("Unable to check the checkbox with locator '" + locator + "' - " + e);
			Assert.fail("Element " + locator + " was not found in DOM in time");	
		}	
	}




	/**
	 * Method - Safe Method for checkbox deselection, waits until the element is loaded and then deselects checkbox
	 * @param locator
	 * @param waitTime
	 * @return - boolean (returns True when the checkbox is deselected else returns false)
	 * @throws Exception
	 */
	public void safeUnCheck(By locator, int... optionWaitTime)
	{
		int waitTime=0;
		try
		{
			waitTime =  getWaitTime(optionWaitTime);
			if(isElementPresent(locator, waitTime))
			{
				scrollIntoElementView(locator);
				WebElement checkBox = driver.findElement(locator);
				setHighlight(checkBox);
				if(checkBox.isSelected()){
					checkBox.click();
					log.info("Checkbox with locator '" + locator + "'' " + "is deselected");
				}
				else
					log.info("CheckBox " + locator + "is already deselected");

			}
			else
			{			
				log.error("Element " + locator + " was not found in DOM in time - "+waitTime);
				Assert.fail("Element " + locator + " was not found in DOM in time - "+waitTime);
			}
		}
		catch(StaleElementReferenceException e)
		{
			log.error("Element with " + locator + " is not attached to the page document in time - "+waitTime+" - StaleElementReferenceException");
			Assert.fail("Element with " + locator + " is not attached to the page document in time - "+waitTime+" - StaleElementReferenceException");
		}
		catch (NoSuchElementException e)
		{
			log.error("Element " + locator + " was not found in DOM in time - "+waitTime+" - NoSuchElementException");	
			Assert.fail("Element " + locator + " was not found in DOM in time - "+waitTime+" - NoSuchElementException");
		}
		catch(Exception e)
		{
			log.error("Unable to uncheck the checkbox with locator '" + locator + "' - " + e);
			Assert.fail("Element " + locator + " was not found in DOM in time");	
		}	
	}



	/**
	 * Method - Safe Method for checkbox Selection or Deselection based on user input, waits until the element is loaded and then deselects/selects checkbox
	 * @param locator
	 * @param checkOption
	 * @param waitTime
	 * @throws Exception
	 */
	public void safeCheckByOption(By locator,boolean checkOption, int... optionWaitTime)
	{
		int waitTime=0;
		try
		{
			waitTime =  getWaitTime(optionWaitTime);
			if(isElementPresent(locator, waitTime))
			{
				scrollIntoElementView(locator);
				WebElement checkBox = driver.findElement(locator);
				setHighlight(checkBox);		
				if((checkBox.isSelected()==true && checkOption == false)||(checkBox.isSelected()==false && checkOption == true)){
					checkBox.click();
					log.info("Checkbox with locator '" + locator + "'' " + "is checked or unchecked");
				}
				else{
					log.info("CheckBox " + locator + "is already deselected");
					log.info("CheckBox " + locator + " is already deselected");
				}
			}
			else
			{			
				log.error("Unable to find checkbox " + locator +" in time - "+waitTime);
				Assert.fail("Unable to find checkbox " + locator +" in time - "+waitTime);
			}
		}
		catch(StaleElementReferenceException e)
		{
			log.error("Element with " + locator + " is not attached to the page document - StaleElementReferenceException");
			Assert.fail("Element with " + locator + " is not attached to the page document - StaleElementReferenceException");
		}
		catch (NoSuchElementException e)
		{
			log.error("Element " + locator + " was not found in DOM in time - "+waitTime+" - NoSuchElementException");	
			Assert.fail("Element " + locator + " was not found in DOM in time - "+waitTime+" - NoSuchElementException");
		}
		catch(Exception e)
		{
			log.error("Unable to check or uncheck the checkbox with locator '" + locator + "' - " + e);		
			Assert.fail("Element " + locator + " was not found in DOM");
		}		
	}


	/**
	 * Method - Safe Method for getting checkbox value, waits until the element is loaded and then deselects checkbox
	 * @param locator
	 * @param checkOption
	 * @param waitTime
	 * @return - boolean (returns True when the checkbox is enabled else returns false)
	 * @throws Exception
	 */
	public boolean safeGetCheckboxValue(By locator, int... optionWaitTime)
	{
		int waitTime =  0;
		boolean isSelected = false;
		try
		{
			waitTime =  getWaitTime(optionWaitTime);
			if(isElementPresent(locator, waitTime))
			{				
				scrollIntoElementView(locator);
				WebElement checkBox = driver.findElement(locator);
				setHighlight(checkBox);
				if (checkBox.isSelected())	{	
					isSelected = true; 
					log.info("Checkbox with " + locator + " is checked");
				}
			}
			else
			{			
				log.error("Unable to get the status of checkbox " + locator+" '");
				Assert.fail("Unable to get the status of checkbox " + locator+" '");
			}
		}
		catch(StaleElementReferenceException e)
		{
			log.error("Element with " + locator + " is not attached to the page document - StaleElementReferenceException");
			Assert.fail("Element with " + locator + " is not attached to the page document - StaleElementReferenceException");
		}
		catch (NoSuchElementException e)
		{
			log.error("Element " + locator + " was not found in DOM in time - "+waitTime+" - NoSuchElementException");	
			Assert.fail("Element " + locator + " was not found in DOM in time - "+waitTime+" - NoSuchElementException");
		}
		catch(Exception e)
		{
			log.error("Unable to get the status of checkbox " + locator + " - " + e);	
			Assert.fail("Unable to get the status of checkbox " + locator);	
		}
		return isSelected;
	}


	/**
	 * Purpose- For selecting multiple check boxes at a time
	 * @param waitTime
	 * @param locator
	 * @throws Exception
	 * @functionCall - SelectMultipleCheckboxs(MEDIUMWAIT, By.id("Checkbox1"),By.id("Checkbox2"), By.xpath("checkbox")); u can pass 'N' number of locators at a time
	 */
	public void safeSelectCheckboxes(int waitTime ,By... locator) throws Exception
	{			
		By check = null;
		try
		{
			if(locator.length>0)
			{
				for(By currentLocator:locator)
				{
					check = currentLocator;
					waitUntilClickable(currentLocator, waitTime);
					scrollIntoElementView(currentLocator);
					WebElement checkBox = driver.findElement(currentLocator);
					setHighlight(checkBox);
					if(checkBox.isSelected())
						log.info("CheckBox " + currentLocator + " is already selected");
					else{
						checkBox.click();	
						log.info("Checkbox " + currentLocator + " is selected");
					}
				}
			}
			else
			{
				log.error("Expected atleast one locator as argument to safeSelectCheckboxes function");
				Assert.fail("Expected atleast one locator as argument to safeSelectCheckboxes function");
			}
		}
		catch(StaleElementReferenceException e)
		{
			log.error("Element with locator- " + check + " is not attached to the page document - StaleElementReferenceException");
			Assert.fail("Element with locator- " + check + " is not attached to the page document - StaleElementReferenceException");
		}
		catch (NoSuchElementException e)
		{
			log.error("Element " + check + " was not found in DOM - NoSuchElementException");	
			Assert.fail("Element " + check + " was not found in DOM - NoSuchElementException");
		}
		catch(Exception e)
		{
			log.error("Unable to select checkbox " + check + " - " + e);	
			Assert.fail("Unable to select checkbox " + check);
		}
	}

	/**
	 * Purpose- For deselecting multiple check boxes at a time
	 * @param waitTime
	 * @param locator
	 * @throws Exception
	 * @functionCall - DeselectMultipleCheckboxs(MEDIUMWAIT, By.id("Checkbox1"),By.id("Checkbox2"), By.xpath("checkbox")); u can pass 'N' number of locators at a time
	 */
	public void safeDeselectCheckboxes(int waitTime, By...locator)
	{	
		By check = null;
		try
		{
			if(locator.length>0)
			{		
				for(By currentLocator:locator)
				{
					check = currentLocator;
					waitUntilClickable(currentLocator,  waitTime);
					WebElement checkBox = driver.findElement(currentLocator);
					scrollIntoElementView(currentLocator);
					setHighlight(checkBox);
					if(checkBox.isSelected()){
						checkBox.click();
						log.info("CheckBox " + currentLocator + " is deselected");
					}
					else	
						log.info("CheckBox " + currentLocator + " is already deselected");

				}
			}
			else
			{
				log.error("Expected atleast one locator as argument to safeDeselectCheckboxes function");
				Assert.fail("Expected atleast one locator as argument to safeDeselectCheckboxes function");
			}
		}
		catch(StaleElementReferenceException e)
		{
			log.error("Element with " + check + " is not attached to the page document - StaleElementReferenceException");
			Assert.fail("Element with " + check + " is not attached to the page document - StaleElementReferenceException");
		}
		catch (NoSuchElementException e)
		{
			log.error("Element " + check + " was not found in DOM in time - "+waitTime+" - NoSuchElementException");	
			Assert.fail("Element " + check + " was not found in DOM in time - "+waitTime+" - NoSuchElementException");
		}
		catch(Exception e)
		{
			log.error("Unable to deselect checkbox " + check + " - " + e);	
			Assert.fail("Unable to deselect checkbox " + check);
		}
	}



	/**
	 * Method - Safe Method for User Select option from Drop down by option name, waits until the element is loaded and then selects an option from drop down
	 * @param locator
	 * @param sOptionToSelect
	 * @param waitTime
	 * @return - boolean (returns True when option is selected from the drop down else returns false)
	 * @throws Exception
	 */
	/*public void safeSelectOptionInDropDown(By locator, String optionToSelect, int... optionWaitTime)
	{ 
		try
		{
			List<WebElement> options = Collections.<WebElement>emptyList();
			int waitTime =  getWaitTime(optionWaitTime);
			if(isElementPresent(locator, waitTime))
			{		
				scrollIntoElementView(locator);
				WebElement selectElement = driver.findElement(locator); 
				setHighlight(selectElement);
				Select select = new Select(selectElement); 
				//Get a list of the options 
				options = select.getOptions(); 
				// For each option in the list, verify if it's the one you want and then click it
				if(!options.isEmpty())
				{
					for (WebElement option: options) 
					{ 
						if (option.getText().contains(optionToSelect))
						{ 
							option.click(); 
							log.info("Selected " + option + " from " + locator + " dropdown");
							break; 
						}
					}
				}
			}
			else
			{
				log.error("Unable to select " + optionToSelect + " from " + locator + UtilityMethods.getStackTrace());
				Assert.fail("Unable to select " + optionToSelect + " from " + locator + UtilityMethods.getStackTrace());
			}
		}
		catch(StaleElementReferenceException e)
		{
			log.error("Element with dropdown locator- " + locator + " or option to be selected -'"+optionToSelect+"' webelement is not attached to the page document"+UtilityMethods.getStackTrace());
			Assert.fail("Element with dropdown locator- " + locator + " or option to be selected -'"+optionToSelect+"' webelement is not attached to the page document"+UtilityMethods.getStackTrace());
		}
		catch (NoSuchElementException e)
		{
			log.error("Element with dropdown locator- " + locator + " or option to be selected -'"+optionToSelect+"' webelement was not found in DOM"+UtilityMethods.getStackTrace());	
			Assert.fail("Element with dropdown locator- " + locator + " or option to be selected -'"+optionToSelect+"' webelement was not found in DOM"+UtilityMethods.getStackTrace());
		}
		catch(Exception e)
		{
			log.error("Unable to select " + optionToSelect + " from " + locator + UtilityMethods.getStackTrace());
			Assert.fail("Unable to select " + optionToSelect + " from " + locator + UtilityMethods.getStackTrace());
		}
	}*/


	/**
	 * Method - Defining Selenium locator for working with duplicate elements when only 1 is active at a given time
	 * @param locator
	 * @return
	 * @throws Exception
	 */
	public WebElement getActivelocatorInSet(By locator) throws Exception
	{
		setImplicitWait(IMPLICITWAIT);
		WebElement activeElem = null;
		int activeElemCount = 0;
		try
		{
			ArrayList<WebElement> elems = (ArrayList<WebElement>)driver.findElements(locator);
			for(int i = 0; i < elems.size(); i++)
			{
				if(elems.get(i).isDisplayed())
				{
					if(++activeElemCount>1)
						log.error("More than 1 active visible locator found on page, expecting only 1");
					else
						activeElem = elems.get(i);
				}
			}
		}
		catch(StaleElementReferenceException e)
		{
			log.error("Element with " + locator + " is not attached to the page document - StaleElementReferenceException");
			Assert.fail("Element with " + locator + " is not attached to the page document - StaleElementReferenceException");
		}
		catch (NoSuchElementException e)
		{
			log.error("Element " + locator + " was not found in DOM - NoSuchElementException");	
			Assert.fail("Element " + locator + " was not found in DOM - NoSuchElementException");
		}
		catch(Exception e)
		{
			log.error("Unable to deselect checkbox " + locator + " - " + e);	
			Assert.fail("Unable to deselect checkbox " + locator);
		}
		return activeElem;
	}


	/**
	 * Method - Safe Method for User Select option from Drop down by option index, waits until the element is loaded and then selects an option from drop down
	 * @param locator
	 * @param iIndexofOptionToSelect
	 * @param waitTime
	 * @return - boolean (returns True when option is selected from the drop down else returns false)	
	 * @throws Exception
	 */
	public void safeSelectOptionInDropDownByIndexValue(By locator, int iIndexofOptionToSelect, int... optionWaitTime)
	{
		int waitTime=0;
		try
		{
			waitTime = getWaitTime(optionWaitTime);
			if(isElementPresent(locator, waitTime))
			{
				scrollIntoElementView(locator);
				WebElement selectElement = driver.findElement(locator);
				setHighlight(selectElement);
				Select select = new Select(selectElement);
				select.selectByIndex(iIndexofOptionToSelect);
				log.info("Selected dropdown item by index option:" + iIndexofOptionToSelect + " from " + locator + " dropdown");
			}
			else
			{
				log.error("Unable to select dropdown item by index option:" + iIndexofOptionToSelect + " from " + locator);
				Assert.fail("Unable to select dropdown item by index option:" + iIndexofOptionToSelect + " from " + locator);
			}
		}
		catch(StaleElementReferenceException e)
		{
			log.error("Dropdown item by index option:" + iIndexofOptionToSelect + " with " + locator + " is not attached to the page document - StaleElementReferenceException");
			Assert.fail("Dropdown item by index option:" + iIndexofOptionToSelect + " with " + locator + " is not attached to the page document - StaleElementReferenceException");
		}
		catch(NoSuchElementException e)
		{
			log.error("Dropdown item by index option:" + iIndexofOptionToSelect + " with " + locator + " is not found in DOM in time - "+waitTime+"- NoSuchElementException");
			Assert.fail("Dropdown item by index option:" + iIndexofOptionToSelect + " with " + locator + " is not found in DOM in time - "+waitTime+"- NoSuchElementException");
		}
		catch(Exception e)
		{
			log.error("Unable to select dropdown item by index option:" + iIndexofOptionToSelect + " from " + locator + " dropdown" + " - " + e);
			Assert.fail("Unable to select dropdown item by index option:" + iIndexofOptionToSelect + " from " + locator + " dropdown");
		}
	} 

	/**
	 * Method - Safe Method for User Select option from Drop down by option value, waits until the element is loaded and then selects an option from drop down
	 * @param locator
	 * @param sValueOfOptionToSelect
	 * @param waitTime
	 * @return - boolean (returns True when option is selected from the drop down else returns false)	
	 * @throws Exception
	 */
	public void safeSelectOptionInDropDownByValue(By locator, String sValuefOptionToSelect, int... optionWaitTime)
	{ 
		int waitTime=0;
		try
		{
			waitTime = getWaitTime(optionWaitTime);
			if(isElementPresent(locator, waitTime))
			{
				scrollIntoElementView(locator);
				WebElement selectElement = driver.findElement(locator);
				setHighlight(selectElement);
				Select select = new Select(selectElement);
				select.selectByValue(sValuefOptionToSelect);
				log.info("Selected dropdown item by value option:" + sValuefOptionToSelect + " from " + locator + " dropdown");
			}
			else
			{
				log.error("Unable to select dropdown item by value option:" + sValuefOptionToSelect + " from " + locator);
				Assert.fail("Unable to select dropdown item by value option:" + sValuefOptionToSelect + " from " + locator);
			}
		}
		catch(StaleElementReferenceException e)
		{
			log.error("Dropdown item by value option:" + sValuefOptionToSelect + " with " +  locator + " is not attached to the page document - StaleElementReferenceException");
			Assert.fail("Dropdown item by value option:" + sValuefOptionToSelect + " with " + locator + " is not attached to the page document - StaleElementReferenceException");
		}
		catch (NoSuchElementException e)
		{
			log.error("Dropdown item by value option:" + sValuefOptionToSelect + " with " + locator + " was not found in DOM in time - "+waitTime+" - NoSuchElementException");	
			Assert.fail("Dropdown item by value option:" + sValuefOptionToSelect + " with " + locator + " was not found in DOM in time - "+waitTime+" - NoSuchElementException");
		}
		catch(Exception e)
		{
			log.error("Unable to select dropdown item by value option:" + sValuefOptionToSelect + " from " + locator + " dropdown" + " - " + e);
			Assert.fail("Unable to select dropdown item by value option:" + sValuefOptionToSelect + " from " + locator + " dropdown");
		}
	} 

	/**
	 * Method - Safe Method for User Select option from Drop down by option lable, waits until the element is loaded and then selects an option from drop down
	 * @param locator
	 * @param sVisibleTextOptionToSelect
	 * @param waitTime
	 * @return - boolean (returns True when option is selected from the drop down else returns false)	
	 * @throws Exception
	 */
	public void safeSelectOptionInDropDownByVisibleText(By locator, String sVisibleTextOptionToSelect, int... optionWaitTime)
	{ 
		int waitTime=0;
		try
		{
			waitTime = getWaitTime(optionWaitTime);
			if(isElementPresent(locator, waitTime))
			{
				scrollIntoElementView(locator);
				WebElement selectElement = driver.findElement(locator);
				setHighlight(selectElement);
				Select select = new Select(selectElement);
				select.selectByVisibleText(sVisibleTextOptionToSelect);
				log.info("Selected dropdown item by visible text option:" + sVisibleTextOptionToSelect + " from " + locator + " dropdown");
			}
			else
			{
				log.error("Unable to select dropdown item by visible text option:" + sVisibleTextOptionToSelect + " from " + locator);
				Assert.fail("Unable to select dropdown item by visible text option:" + sVisibleTextOptionToSelect + " from " + locator);
			}
		}
		catch(StaleElementReferenceException e)
		{
			log.error("Dropdown item by visible text option:" + sVisibleTextOptionToSelect + " with " + locator + " is not attached to the page document - StaleElementReferenceException");
			Assert.fail("Dropdown item by visible text option:" + sVisibleTextOptionToSelect + " with " + locator + " is not attached to the page document - StaleElementReferenceException");
		}
		catch (NoSuchElementException e)
		{
			log.error("Dropdown item by visible text option:" + sVisibleTextOptionToSelect + " with " + locator + " was not found in DOM - NoSuchElementException");	
			Assert.fail("Dropdown item by visible text option:" + sVisibleTextOptionToSelect + " with " + locator + " was not found in DOM - NoSuchElementException");
		}
		catch(Exception e)
		{
			log.error("Unable to select dropdown item by visible text option:" + sVisibleTextOptionToSelect + " from " + locator + " dropdown" + " - " + e);
			Assert.fail("Unable to select dropdown item by visible text option:" + sVisibleTextOptionToSelect + " from " + locator + " dropdown");
		}
	} 


	/**
	 * Method - Safe Method for User Select option from list menu, waits until the element is loaded and then selects an option from list menu
	 * @param locator
	 * @param sOptionToSelect
	 * @param waitTime
	 * @throws Exception
	 */
	public void safeSelectListBox(By locator, String sOptionToSelect, int... optionWaitTime)
	{
		int waitTime=0;
		try
		{
			waitTime =  getWaitTime(optionWaitTime);
			List<WebElement> options = Collections.<WebElement>emptyList();
			if(isElementPresent(locator, waitTime))
			{
				//First, get the WebElement for the select tag 
				WebElement selectElement = driver.findElement(locator); 
				setHighlight(selectElement);
				//Then instantiate the Select class with that WebElement 
				Select select = new Select(selectElement); 
				//Get a list of the options 
				options = select.getOptions(); 
				if(!options.isEmpty())
				{
					boolean bExists = false;
					// For each option in the list, verify if it's the one you want and then click it 
					for (WebElement option: options) 
					{ 
						if (option.getText().contains(sOptionToSelect))
						{ 
							option.click(); 
							log.info("Selected Listbox item: " + sOptionToSelect + " from " + locator + " Listbox");
							bExists = true;
							break; 
						}						
					}
					if(!bExists)
					{
						log.error("Unable to select " + sOptionToSelect + " from " + locator);
						Assert.fail("Unable to select " + sOptionToSelect + " from " + locator);
					}
				}
			}
			else
			{
				log.error("List box with locator " + locator +"is not displayed");
				Assert.fail("List box with locator " + locator +"is not displayed");
			}
		}
		catch(StaleElementReferenceException e)
		{			
			log.error("Listbox item: " + sOptionToSelect + " with " + locator + " is not attached to the page document - StaleElementReferenceException");
			Assert.fail("Listbox item: " + sOptionToSelect + " with " + locator + " is not attached to the page document - StaleElementReferenceException");				
		}
		catch (NoSuchElementException e)
		{	    	
			log.error("Listbox item: " + sOptionToSelect + " with " + locator + " was not found in DOM in time - "+waitTime+" - NoSuchElementException");	
			Assert.fail("Listbox item: " + sOptionToSelect + " with " + locator + " was not found in DOM in time - "+waitTime+" - NoSuchElementException");		
		}
		catch(Exception e)
		{
			log.error("Unable to select " + sOptionToSelect + " from " + locator + " Listbox" + " - " + e);
			Assert.fail("Unable to select " + sOptionToSelect + " from " + locator + " Listbox");
		}
	}



	/**
	 * Method - Method to hover on an element based on locator using Actions,it waits until the element is loaded and then hovers on the element
	 * @param locator
	 * @param waitTime
	 * @throws Exception
	 */
	public void mouseHover(By locator,int waitTime)
	{
		try
		{
			if(isElementVisible(locator, waitTime))
			{
				Actions builder = new Actions(driver);
				WebElement HoverElement = driver.findElement(locator);
				builder.moveToElement(HoverElement).build().perform();
				log.info("Hovered on element " + locator);
			}
			else
			{	    
				log.error("Element was not visible to hover ");
				Assert.fail("Element was not visible to hover ");
			}
		}
		catch(StaleElementReferenceException e)
		{
			log.error("Element with " + locator + " is not attached to the page document - StaleElementReferenceException");
			Assert.fail("Element with " + locator + " is not attached to the page document - StaleElementReferenceException");
		}
		catch (NoSuchElementException e)
		{
			log.error("Element " + locator + " was not found in DOM in time - "+waitTime);	
			Assert.fail("Element " + locator + " was not found in DOM in time - "+waitTime);
		}
		catch(Exception e)
		{
			log.error("Unable to hover the cursor on " + locator + " - " + e);	
			Assert.fail("Unable to hover the cursor on " + locator );
		}
	}


	/**
	 * Method - Method to hover on an element based on locator using Actions and click on given option,it waits until the element is loaded and then hovers on the element
	 * @param locator
	 * @param waitTime
	 * @throws Exception
	 */
	public void mouseHoverAndSelectOption(By locator,By byOptionlocator,int waitTime)
	{
		try
		{
			if(isElementPresent(locator, waitTime))
			{
				Actions builder = new Actions(driver);
				WebElement HoverElement = driver.findElement(locator);
				builder.moveToElement(HoverElement).build().perform();
				try {
					builder.wait(4000);
				} catch (InterruptedException e) {
					log.error("Exception occurred while waiting");
				}
				WebElement element = driver.findElement(byOptionlocator);
				element.click();
				log.info("Hovered on element "+locator+" and selected the Option" + byOptionlocator);
			}
			else
			{	    
				log.error("Element "+locator+" was not visible to hover ");
				Assert.fail("Element "+locator+" was not visible to hover ");
			}
		}
		catch(StaleElementReferenceException e)
		{
			log.error("Element with " + locator + "or"+ byOptionlocator +"is not attached to the page document");
			Assert.fail("Element with " + locator + "or"+ byOptionlocator +"is not attached to the page document");
		}
		catch (NoSuchElementException e)
		{
			log.error("Element " + locator + "or"+ byOptionlocator + " was not found in DOM");	
			Assert.fail("Element "+ locator + "or"+ byOptionlocator +" was not found in DOM");
		}
		catch(Exception e)
		{
			log.error("Unable to hover the cursor on " + locator + "or unable to select "+ byOptionlocator +"option" + " - " + e);	
			Assert.fail("Unable to hover the cursor on " + locator + "or unable to select "+ byOptionlocator +"option");
		}
	}

	/**
	 * Method - Method to hover on an element based on locator using JavaScript snippet,it waits until the element is loaded and then hovers on the element
	 * @param locator
	 * @param Choice
	 * @param waitTime
	 * @throws Exception
	 */
	public void mouseHoverJScript(By locator,String Choice,int waitTime)
	{
		try 
		{
			if(isElementPresent(locator, waitTime))
			{
				WebElement HoverElement = driver.findElement(locator);
				String mouseOverScript = "if(document.createEvent){var evObj = document.createEvent('MouseEvents');evObj.initEvent('mouseover', true, false); arguments[0].dispatchEvent(evObj);} else if(document.createEventObject) { arguments[0].fireEvent('onmouseover');}";
				((JavascriptExecutor) driver).executeScript(mouseOverScript, HoverElement);
				// Thread.sleep(4000);	
				log.info("Hovered on element " + locator + " and selected the choice: " + Choice);
			}
			else
			{
				log.error("Element "+locator+" was not visible to hover ");
				Assert.fail("Element "+locator+" was not visible to hover ");
			}
		}
		catch(StaleElementReferenceException e)
		{
			log.error("Element with " + locator + " to be hovered on " + Choice + " is not attached to the page document - StaleElementReferenceException");
			Assert.fail("Element with " + locator + " to be hovered on " + Choice + " is not attached to the page document - StaleElementReferenceException");
		}
		catch (NoSuchElementException e)
		{
			log.error("Element with " + locator + " to be hovered on " + Choice + " was not found in DOM in time - "+waitTime+" - NoSuchElementException");	
			Assert.fail("Element with " + locator + " to be hovered on " + Choice + " was not found in DOM in time - "+waitTime+" - NoSuchElementException");
		}
		catch(Exception e)
		{		    
			log.error("Unable to hover on " + Choice + " element with locator " + locator + " - " + e);
			Assert.fail("Unable to hover on " + Choice + " element with locator " + locator);
		}
	}


	/**
	 * Method - Safe Method for User Click, waits until the element is loaded and then performs a click action
	 * @param locatorToClick
	 * @param locatorToCheck
	 * @param waitTime
	 * @return - boolean (returns True when click action is performed else returns false)
	 * @throws Exception
	 */
	public void safeClick(By locatorToClick,By locatorToCheck, int waitElementToClick,int waitElementToCheck ) throws Exception 
	{
		boolean bResult = false;
		int iAttempts = 0;
		nullifyImplicitWait();
		WebDriverWait wait = new WebDriverWait(driver, waitElementToClick);
		WebDriverWait wait2 = new WebDriverWait(driver,waitElementToCheck);
		while(iAttempts < 3) 
		{

			try 
			{
				wait.until(ExpectedConditions.visibilityOfElementLocated(locatorToClick));
				wait.until(ExpectedConditions.elementToBeClickable(locatorToClick));
				WebElement element = driver.findElement(locatorToClick);

				if(element.isDisplayed())
				{
					setHighlight(element);
					element.click();
					waitForPageToLoad();
					waitForJQueryProcessing(waitElementToCheck);
					wait2.until(ExpectedConditions.visibilityOfElementLocated(locatorToCheck));
					WebElement elementToCheck = driver.findElement(locatorToCheck);
					if(elementToCheck.isDisplayed())
					{
						log.info("Clicked on element " + locatorToClick);
						break;
					}
					else
					{
						Thread.sleep(1000);
						continue;
					}
				}
			} 
			catch(Exception e) 
			{            	
				log.info("Attempt: "+iAttempts +"\n Unable to click on element " + locatorToClick + " - " + e);
			}
			iAttempts++;
		}
		if (!bResult)
		{
			Assert.fail("Unable to click on element " + locatorToClick);
		}
	}







	/**
	 * Purpose- Method For performing drag and drop operations 
	 * @param Sourcelocator,Destinationlocator
	 * @param waitTime
	 * @throws Exception
	 * @function_call - eg: DragAndDrop(By.id(Sourcelocator), By.xpath(Destinationlocator), "MEDIUMWAIT");
	 */
	public void dragAndDrop(By Sourcelocator, By Destinationlocator, int waitTime)
	{
		try
		{
			if(isElementPresent(Sourcelocator, waitTime))
			{
				WebElement source = driver.findElement(Sourcelocator);
				if(isElementPresent(Destinationlocator, waitTime))
				{
					WebElement destination = driver.findElement(Destinationlocator);
					Actions action = new Actions(driver);
					action.dragAndDrop(source, destination).build().perform();
					log.info("Dragged the element "+ Sourcelocator + " and dropped in to " + Destinationlocator);
				}
				else
				{
					log.error("Destination Element with locator "+Destinationlocator+" was not displayed to drop");
					Assert.fail("Destination Element with locator "+Destinationlocator+" was not displayed to drop");
				}
			}
			else
			{
				log.error("Source Element with locator "+Sourcelocator+" was not displayed to drag");
				Assert.fail("Source Element with locator "+Sourcelocator+" was not displayed to drag");
			}
		}
		catch(StaleElementReferenceException e)
		{
			log.error("Element with " + Sourcelocator + "or"+ Destinationlocator +"is not attached to the page document - StaleElementReferenceException");
			Assert.fail("Element with " + Sourcelocator + "or"+ Destinationlocator +"is not attached to the page document - StaleElementReferenceException");
		}
		catch (NoSuchElementException e)
		{
			log.error("Element " + Sourcelocator + "or"+ Destinationlocator + " was not found in DOM in time - "+waitTime+" - NoSuchElementException");	
			Assert.fail("Element " + Sourcelocator + "or"+ Destinationlocator + " was not found in DOM in time - "+waitTime+" - NoSuchElementException");
		}
		catch(Exception e)
		{			  		 
			log.error("Some exception occurred while performing drag and drop operation" + " - " + e);
			Assert.fail("Some exception occurred while performing drag and drop operation");
		}
	}

	/**
	 * 
	 * Purpose- Method For waiting for an alert and acceptng it 
	 *
	 * @param waitTime
	 * @return returns true if alert is displayed and accepted, else returns false
	 */
	public boolean AlertWaitAndAccepted(int waitTime)
	{
		boolean bAlert = false;
		if(isAlertPresent(waitTime))
		{
			try
			{
				Alert alert =driver.switchTo().alert();
				try
				{
					Thread.sleep(2000);
				} 
				catch (InterruptedException exception)
				{					
					log.error("Exception occured while waiting for an alert using thread sleep method ");
				}
				alert.accept();
				log.info("Alert is displayed and accepted successfully");
				bAlert = true;
			}
			catch(NoAlertPresentException e)
			{

				bAlert = false;
				log.error("Alert not present");
			}
		}
		else{
			log.error("Alert not present in time - "+waitTime+" - NoAlertPresentException");
			bAlert=false;
		}
		return bAlert;
	}

	/**
	 * Method: for verifying if accept exists and accepting the alert
	 * @throws Exception
	 */
	public void acceptAlert()
	{
		try
		{
			Alert alert = driver.switchTo().alert();
			String sText = alert.getText();		 
			alert.accept();
			log.info("Accepted the alert:"+ sText);
		}
		catch(NoAlertPresentException e)
		{
			log.error("Alert is not displayed to accept." + " - NoAlertPresentException");
			Assert.fail("Alert is not displayed to accept." + " - NoAlertPresentException");
		}
		catch(Exception e)
		{			
			log.error("Alert is not displayed to accept." + " - "+e);
			Assert.fail("Unable to accept the alert.");
		}
	}

	/**
	 * Method: for verifying if accept exists and accepting the alert
	 * @return - String (returns text present on the Alert)
	 * @throws Exception
	 */
	public String getAlertMessage()
	{
		String sText = null;
		try
		{
			Alert alert = driver.switchTo().alert();
			sText = alert.getText();		 
			log.info("Text present in the alert:"+ sText);
		}
		catch(NoAlertPresentException e)
		{
			log.error("Alert is not displayed to accept." + " - " + e);
			Assert.fail("Alert is not displayed to accept.");
		}
		catch(Exception e)
		{			
			log.error("Unable to read alert message." + " - " + e);
			Assert.fail("Unable to accept the alert.");
		}
		return sText;
	}

	/**
	 * Method: for verifying if accept exists and rejecting/dismissing the alert
	 * @return - boolean (returns True when dismiss action is performed else returns false)
	 * @throws Exception
	 */
	public void dismissAlert() throws Exception
	{
		try
		{
			Alert alert = driver.switchTo().alert();
			String sText = alert.getText();			
			alert.dismiss();	
			log.info("Dismissed the alert:"+ sText);
		}
		catch(NoAlertPresentException e)
		{
			log.error("Alert is not displayed to dismiss.");
			Assert.fail("Alert is not displayed to dismiss.");
		}
		catch(Exception e)
		{			
			log.error("Unable to dismiss the alert." + " - " + e);
			Assert.fail("Unable to accept the alert.");
		}

	}	


	/**
	 * Purpose - To select the context menu option for the given element
	 * @param locator
	 * @param OptionIndex
	 * @param waitTime
	 * @return
	 * @throws Exception
	 */
	public void safeSelectContextMenuOption(By locator, int iOptionIndex, int waitTime)
	{
		try
		{
			if(isElementPresent(locator, waitTime))
			{
				selectContextMenuOption(locator, iOptionIndex);
			}
			else
			{
				log.error("Element with locator "+locator + "is not displayed to perform content menu operation by index: " + iOptionIndex);
				Assert.fail("Element with locator "+locator + "is not displayed to perform content menu operation by index: " + iOptionIndex);
			}
		}
		catch(StaleElementReferenceException e)
		{
			log.error("Context menu option by index: " + iOptionIndex + " for Element with " + locator + " is not attached to the page document");
			Assert.fail("Context menu option by index: " + iOptionIndex + " for Element with " + locator + " is not attached to the page document");
		}
		catch (NoSuchElementException e)
		{
			log.error("Context menu option by index: " + iOptionIndex + " for Element with " + locator + " was not found in DOM");	
			Assert.fail("Context menu option by index: " + iOptionIndex + " for Element with " + locator + " was not found in DOM");
		}
		catch(Exception e)
		{
			log.error("Unable to select context menu option by index: " + iOptionIndex + " for Element with" + locator + " - " + e);
			Assert.fail("Unable to select context menu option by index: " + iOptionIndex + " for Element with" + locator);
		}
	}


	private void selectContextMenuOption(By locator, int iOptionIndex) 
	{		
		WebElement Element = driver.findElement(locator);
		Actions _action = new Actions(driver);
		for (int count=1; count<=iOptionIndex; count++)
		{
			_action.contextClick(Element).sendKeys(Keys.ARROW_DOWN);
		}
		_action.contextClick(Element).sendKeys(Keys.RETURN).build().perform();
	}



	/**
	 * Method: for uploading file
	 * @return - boolean (returns True when upload is successful else returns false)
	 * @throws Exception
	 */
	public boolean uploadFile(By locator, String filePath, int... optionWaitTime) throws Exception
	{
		boolean hasTyped = false;
		try
		{		
			int waitTime =  getWaitTime(optionWaitTime);
			if(isElementPresent(locator, waitTime))
			{
				scrollIntoElementView(locator);
				WebElement element=driver.findElement(locator);
				setHighlight(element);
				element.sendKeys(filePath);
				log.info("Entered - '"+filePath+" in the element - " + locator);
				hasTyped = true;
			}
			else
			{			
				log.error("Element with locator " + locator + " is not displayed");
				Assert.fail("Element with locator " + locator + " is not displayed");
			}
		}
		catch(StaleElementReferenceException e)
		{
			log.error("Element with " + locator +"is not attached to the page document");
			Assert.fail("Element with " + locator +"is not attached to the page document");
		}
		catch (NoSuchElementException e)
		{
			log.error("Element " + locator + " was not found in DOM");	
			Assert.fail("Element "+ locator +" was not found in DOM");
		}
		catch(Exception e)
		{
			log.error("Unable to upload file - "+filePath+" using upload field - "+locator + " - " + e);
			Assert.fail("Unable to upload file - "+filePath+" using upload field - "+locator);
		}
		return hasTyped;			
	}	


	/**
	 * Method: for editing rich text editor which is in frame
	 * @param Framelocator
	 * @param TextEditorlocator
	 * @param Text
	 * @param waitTime
	 * @return - boolean (returns True when editing is successful else return false)
	 * @throws Exception
	 */
	public boolean safeEditRichTextEditor(By Framelocator, By TextEditorlocator, String sText, int waitTime) throws Exception
	{
		try
		{
			editTextEditor(Framelocator, TextEditorlocator, sText, waitTime);
		    return true;
		}
		catch(StaleElementReferenceException e)
		{
			log.error("Element with " + TextEditorlocator +"is not attached to the page document");
			Assert.fail("Element with " + TextEditorlocator +"is not attached to the page document");
			return false;
		}
	    catch (NoSuchElementException e)
		{
			log.error("Element " + TextEditorlocator + " was not found in DOM");	
			Assert.fail("Element "+ TextEditorlocator +" was not found in DOM");
			return false;
		}
		catch(Exception e)	
		{
			log.error("Unable to edit rich text editor " + TextEditorlocator  + " - " + e);
			Assert.fail("Unable to edit rich text editor " + TextEditorlocator);
			return false;
		}
	}


	private void editTextEditor(By frameLocator, By textEditorLocator, String sText, int waitTime) throws Exception 
	{
		if(isElementPresent(frameLocator, waitTime))
		{
			selectFrame(frameLocator,waitTime);
			if(isElementPresent(textEditorLocator, waitTime))
			{
				WebElement element = driver.findElement(textEditorLocator);
				element.click();
				setHighlight(element);
				element.sendKeys(sText);		
				defaultFrame();
			}
			else
			{
				log.error("Rich text editor with locator "+textEditorLocator+" is not displayed");
				Assert.fail("Rich text editor with locator "+textEditorLocator+" is not displayed");
			}
		}
		else
		{
			log.error("Rich text editor Frame with locator "+frameLocator+" is not displayed");
			Assert.fail("Rich text editor Frame with locator "+frameLocator+" is not displayed");
		}
	}


	/**
	 * Method to enter text into the text editor which has no frame (Navigated to the text editor by tab sequence) 
	 * @param sText
	 * @param index
	 * @throws Exception 
	 */
	public void safeEditTextBoxByTabSequence(String sText, int index) throws Exception 
	{
		try
		{
			//Copy the text to be entered into text editor to the clip board
			copyTextToClipboard(sText);			              
			Robot _robot = new Robot();			
			//Navigate to the text editor using tab sequence
			for (int count=1; count<=index; count++)
			{
				_robot.keyPress(KeyEvent.VK_TAB); 
			}			
			//Paste the text(copied to the clip board) on text editor 
			pasteCopiedText(_robot);
			log.info("Text " + sText + " is entered into the text editor");
		}
		catch(Exception e)
		{
			log.error("Unable to edit rich text editor." + " - " + e);
			Assert.fail("Unable to edit rich text editor.");
		}
	}

	/**This method to enter a 'String with alpha numeric only' using robots class
	 * and to enter special character need to customize the method
	 *@parameter caseType 
	  @parameter String;
	  @parameter locator;
	  @parameter wait time;

	 */
	public void safeTypeUsingRobot(By locator,String text,String caseType,int... optionWaitTime)
	{
		try{

			int waitTime = getWaitTime(optionWaitTime);
			if(waitUntilClickable(locator, waitTime))
			{ 
				Robot robot = new Robot();
				scrollIntoElementView(locator);
				WebElement element = driver.findElement(locator);
				setHighlight(element);
				element.click();
				log.info("Clicked on the element " + locator);

				if(caseType.equalsIgnoreCase("lowercase"))
				{
					log.info("Typing string in lowercase");
					for(int i = 0; i < text.length(); i++)
					{
						String letter = Character.toString(text.charAt(i));
						String code = "VK_" + letter;
						Field f = KeyEvent.class.getField(code);
						int keyEvent = f.getInt(null);	       
						robot.keyPress(keyEvent);
						robot.keyRelease(keyEvent);
					}
				}
				if(caseType.equalsIgnoreCase("uppercase"))
				{
					log.info("Typing string in uppercase");
					for(int i = 0; i < text.length(); i++)
					{
						String letter = Character.toString(text.charAt(i));
						String code = "VK_" + letter;
						Field f = KeyEvent.class.getField(code);
						int keyEvent = f.getInt(null);	
						robot.keyPress(KeyEvent.VK_SHIFT);
						robot.keyPress(keyEvent);
						robot.keyRelease(KeyEvent.VK_SHIFT);
						robot.keyRelease(keyEvent);
					}
				}
			}
			log.info("text" +text+ "typed in " +locator);
		}
		catch(StaleElementReferenceException e)
		{
			log.error("Text " + text + " to be entered in field " + locator + " is not attached to the page document");
			Assert.fail("Text " + text + " to be entered in field " + locator + " is not attached to the page document");
		}
		catch (NoSuchElementException e)
		{
			log.error("Text " + text + " to be entered in field " + locator + " was not found in DOM");	
			Assert.fail("Text " + text + " to be entered in field " + locator + " was not found in DOM");
		}

		catch(AWTException e)
		{
			log.error("Unable to type space \t" + " - " + e);
			Assert.fail("Unable to type text using robot the error  message is\t" + " - " + e);
		}
		catch(NullPointerException e){
			log.error("Text provided as null...Please provide text");
			Assert.fail("Text provided as null...Please provide text");
		}
		catch(Exception e)
		{
			log.error("Unable to type space " + " - "  + " - " + e);
			Assert.fail("Unable to type text using robot the error  message is\t" + " - " + e);
		}
	}


	/**
	 * Method to paste the text using key strokes
	 * @param robot
	 */
	private void pasteCopiedText(Robot robot) 
	{
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_CONTROL);
	}


	/**
	 * Method to copy the given text to clip board
	 * @param sText
	 */
	private void copyTextToClipboard(String sText) 
	{
		StringSelection stringSelection = new StringSelection(sText);
		Clipboard _clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
		_clpbrd.setContents(stringSelection, null);
	}

	/**
	 * Method: for uploading file by using Robot class
	 * @return - null
	 * @throws Exception
	 */
	public void uploadFileRobot(By slocator, String sFileLocation, int waitTime)throws Exception
	{
		try
		{
			if(isElementPresent(slocator, waitTime))
			{
				copyTextToClipboard(sFileLocation); 
				scrollIntoElementView(slocator);
				if(isElementClickable(slocator, waitTime))
				{
					Thread.sleep(2000);
					Actions builder = new Actions(driver);

					Action myAction = builder.click(driver.findElement(slocator))
							.release()
							.build();

					myAction.perform();
					Thread.sleep(15000);
					Robot robot = new Robot();
					pasteCopiedText(robot);
					robot.keyPress(KeyEvent.VK_ENTER);
					robot.keyRelease(KeyEvent.VK_ENTER);
					robot.keyPress(KeyEvent.VK_ENTER);
					robot.keyRelease(KeyEvent.VK_ENTER);
					Thread.sleep(5000);
					log.info("File at location " + sFileLocation + " is Uploaded by upload field " + slocator + " using Robot Functionality");
				}
				else
				{
					log.error("Unable to click on element with locator" + slocator+" for uploading a file");
					Assert.fail("Unable to click on element with locator" + slocator+" for uploading a file");
				}
			}
			else
			{
				log.error("Unable to upload file - "+sFileLocation+" using upload field - "+slocator);
				Assert.fail("Unable to upload file - "+sFileLocation+" using upload field - "+slocator);
			}
		}
		catch(StaleElementReferenceException e)
		{
			log.error("Element with " + slocator +"is not attached to the page document");
			Assert.fail("Element with " + slocator +"is not attached to the page document");			
		}
		catch (NoSuchElementException e)
		{
			log.error("Element " + slocator + " was not found in DOM");	
			Assert.fail("Element "+ slocator +" was not found in DOM");			
		}
		catch (Exception e)
		{
			log.error("Unable to upload file - "+sFileLocation+" using upload field - "+slocator + " - " + e);
			Assert.fail("Unable to upload file - "+sFileLocation+" using upload field - "+slocator);
		}

	}

	/**
	 * 
	 * TODO JavaScript method for clicking on an element
	 *
	 * @param locator - locator value by which element is recognized
	 * @param waitTime - Time to wait for an element
	 * @return
	 * @throws Exception
	 */
	public void safeJavaScriptClick(By locator, int waitTime) throws Exception
	{
		try
		{
			if(isElementPresent(locator, waitTime))
			{
				setImplicitWait(waitTime);
				log.info("Clicking on element with " + locator+ " using java script click");
				//waitUntilClickable(locator,waitTime);
				scrollIntoElementView(locator);
				WebElement element = driver.findElement(locator);
				setHighlight(element);
				((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
				nullifyImplicitWait();
				log.info("Clicked on element " + locator);
			}
			else
			{
				log.error("Unable to click on element " + locator );
				Assert.fail("Unable to click on element " + locator );
			}
		}
		catch(StaleElementReferenceException e)
		{
			nullifyImplicitWait();
			log.error("Element with " + locator +"is not attached to the page document - StaleElementReferenceException");
			Assert.fail("Element with " + locator +"is not attached to the page document - StaleElementReferenceException");			
		}
		catch (NoSuchElementException e)
		{
			nullifyImplicitWait();
			log.error("Element " + locator + " was not found in DOM in time - "+waitTime+" - NoSuchElementException");	
			Assert.fail("Element " + locator + " was not found in DOM in time - "+waitTime+" - NoSuchElementException");			
		}
		catch (Exception e)
		{
			nullifyImplicitWait();
			log.error("Element " + locator + " was not found in in the webpage - "+e);
			Assert.fail("Element " + locator + " was not found in in the webpage");
		}
	}

	/**
	 * 
	 * TODO JavaScript method for entering a text in a field
	 *
	 * @param locator - locator value by which text field is recognized
	 * @param sText - Text to be entered in a field
	 * @param waitTime - Time to wait for an element
	 * @return
	 * @throws Exception
	 */
	public void safeJavaScriptType(By locator,String sText, int waitTime) throws Exception
	{
		try
		{
			if(isElementPresent(locator, waitTime))
			{
				scrollIntoElementView(locator);
				WebElement element = driver.findElement(locator);
				setHighlight(element);
				((JavascriptExecutor) driver).executeScript("arguments[0].setAttribute('value', '"+sText+"');",element);
				log.info("Entered text " + sText + " into the field " + locator);
			}
			else
			{
				log.error("Unable to enter " + sText + " in field " + locator );
				Assert.fail("Unable to enter " + sText + " in field " + locator );
			}
		}
		catch(StaleElementReferenceException e)
		{			
			log.error("Text " + sText + " to be entered in field with " + locator +" is not attached to the page document - StaleElementReferenceException");
			Assert.fail("Text " + sText + " to be entered in field with " + locator +" is not attached to the page document - StaleElementReferenceException");
		}
		catch (NoSuchElementException e)
		{	    	
			log.error("Text " + sText + " to be entered in field with " + locator + " was not found in DOM in time - NoSuchElementException");	
			Assert.fail("Text " + sText + " to be entered in field with " + locator + " was not found in DOM in time - NoSuchElementException");			
		}
		catch (Exception e)
		{
			log.error("Unable to enter '" + sText + "' text in field with locator -"+ locator + " - " + e);
			Assert.fail("Unable to enter " + sText + " in field " + locator);
		}
	}

	/**
	 * 
	 * TODO JavaScript Safe Method for Clear and Type
	 *
	 * @param locator - locator value by which text field is recognized
	 * @param sText - Text to be entered in a field
	 * @param waitTime - Time to wait for an element
	 * @return
	 * @throws Exception
	 */
	public void safeJavaScriptClearType(By locator,String sText, int waitTime)
	{
		try
		{
			if(isElementPresent(locator, waitTime))
			{
				scrollIntoElementView(locator);
				WebElement element = driver.findElement(locator);
				setHighlight(element);
				((JavascriptExecutor) driver).executeScript("arguments[0].setAttribute('value', '');",element);
				((JavascriptExecutor) driver).executeScript("arguments[0].setAttribute('value', '"+sText+"');",element);
				log.info("Cleared the field and entered - '" + sText + " in the element - " + locator);
			}
			else
			{
				log.error("Unable to enter " + sText + " in field " + locator);
				Assert.fail("Unable to enter " + sText + " in field " + locator);
			}
		}
		catch(StaleElementReferenceException e)
		{			
			log.error("Text " + sText + " to be entered after " + " clear the field with " + locator + " is not attached to the page document - StaleElementReferenceException");
			Assert.fail("Text " + sText + " to be entered after " + " clear the field with " + locator + " is not attached to the page document - StaleElementReferenceException");
		}
		catch (NoSuchElementException e)
		{	    	
			log.error("Text " + sText + " to be entered after " + " clear the field with " + locator + " was not found in DOM in time - "+waitTime+" - NoSuchElementException");
			Assert.fail("Text " + sText + " to be entered after " + " clear the field with " + locator + " was not found in DOM in time - "+waitTime+" - NoSuchElementException");		
		}
		catch (Exception e)
		{	
			log.error("Unable to enter " + sText + " in field " + locator + " - " + e);
			Assert.fail("Unable to enter " + sText + " in field " + locator);
		}
	}

	/**
	 * 
	 * TODO Safe method to get the attribute value
	 *
	 * @param locator - locator value by which an element is located
	 * @param sAttributeValue - attribute type
	 * @param waitTime - Time to wait for an element
	 * @return - returns the attribute value of type string
	 */
	public String safeGetAttribute(By locator,String sAttributeValue,int waitTime)
	{
		String sValue = null;
		try
		{
			if(isElementPresent(locator, waitTime))
			{
				sValue = driver.findElement(locator).getAttribute(sAttributeValue);
				log.info("Got attribute value of the type " + sAttributeValue + " is: " + sValue);
			}
			else
			{
				log.error("Unable to find locator "+locator+"in time - "+waitTime);
				Assert.fail("Unable to find locator "+locator+"in time - "+waitTime);
			}
		}
		catch(StaleElementReferenceException e)
		{			
			log.error("Element with " + locator +"is not attached to the page document - StaleElementReferenceException");
			Assert.fail("Element with " + locator +"is not attached to the page document - StaleElementReferenceException");			
		}
		catch (NoSuchElementException e)
		{	    	
			log.error("Element " + locator + " was not found in DOM in time - "+waitTime+" - NoSuchElementException");	
			Assert.fail("Element "+ locator +" was not found in DOMin time - "+waitTime+" - NoSuchElementException");			
		}
		catch(Exception e)
		{
			log.error("Unable to get attribute value of type " + sAttributeValue + " from the element "+ locator + " - " + e);		
			Assert.fail("Element " + locator + " was not found in DOM");
		}		
		return sValue;		
	}

	/**
	 * 
	 * TODO Safe method to get text from an element
	 *
	 * @param locator - locator value by which an element is located
	 * @param waitTime - Time to wait for an element
	 * @return - returns the text value from element
	 */
	public String safeGetText(By locator,int waitTime)
	{
		String sValue =null;
		try
		{
			if(isElementPresent(locator, waitTime))
			{
				sValue = driver.findElement(locator).getText();
			}
			else
			{
				Assert.fail("Unable to find element "+ locator+" in time - "+waitTime);
			}

		}
		catch(StaleElementReferenceException e)
		{			
			log.error("Element with " + locator +"is not attached to the page document - StaleElementReferenceException");
			Assert.fail("Element with " + locator +"is not attached to the page document - StaleElementReferenceException");
		}
		catch (NoSuchElementException e)
		{	    	
			log.error("Element " + locator + " was not found in DOM in time - "+waitTime+" - NoSuchElementException");	
			Assert.fail("Element " + locator + " was not found in DOM in time - "+waitTime+" - NoSuchElementException");			
		}
		catch(Exception e)
		{
			log.error("Unable to get the text from the element "+ locator + " - " + e);
			Assert.fail("Unable to find element "+ locator);
		}		
		return sValue;		
	}

	/**
	 * 
	 * TODO scroll method to scroll the page down until expected element is visible	 *
	 * @param locator - locator value by which an element is located
	 * @param waitTime - Time to wait for an element
	 * @return - returns the text value from element
	 */
	public void scrollIntoElementView(By locator)
	{
		try
		{
			//			WebElement element = ;
			((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView();",driver.findElement(locator));
		}
		catch(StaleElementReferenceException e)
		{			
			log.error("Element with " + locator +"is not attached to the page document");
			Assert.fail("Element with " + locator +"is not attached to the page document");			
		}
		catch (NoSuchElementException e)
		{	    	
			log.error("Element " + locator + " was not found in DOM");	
			Assert.fail("Element "+ locator +" was not found in DOM");			
		}
		catch(Exception e)
		{
			log.error("Unable to scroll the page to find "+ locator + " - " + e);
			Assert.fail("Unable to scroll the page to find "+ locator);			
		}
	}

	/**
	 * 
	 * TODO JavaScript Safe method to get the attribute value
	 *
	 * @param locator - locator value by which an element is located
	 * @param sAttributeValue - attribute type
	 * @param waitTime - Time to wait for an element
	 * @return - returns the attribute value of type string
	 */
	public String safeJavaScriptGetAttribute(By locator,String sAttributeValue,int waitTime)
	{
		String sValue ="";
		try
		{
			if(isElementPresent(locator, waitTime))
			{
				final String scriptGetValue = "return arguments[0].getAttribute('"+sAttributeValue+"')";
				WebElement element = driver.findElement(locator);			
				sValue = (String)((JavascriptExecutor) driver).executeScript(scriptGetValue,element);
				log.info("Got attribute value of the type " + sAttributeValue + " is: " + sValue);
			}
			else
			{
				log.error("Unable to find locator "+locator);
				Assert.fail("Unable to find locator "+locator);
			}
		}
		catch(StaleElementReferenceException e)
		{			
			log.error("To get attribute value of type " + sAttributeValue + " for Element with " + locator + " is not attached to the page document - StaleElementReferenceException");
			Assert.fail("To get attribute value of type " + sAttributeValue + " for Element with " + locator + " is not attached to the page document - StaleElementReferenceException");
		}
		catch (NoSuchElementException e)
		{	
			log.error("To get attribute value of type " + sAttributeValue + " for Element with " + locator + " was not found in DOM in time - "+waitTime+" - NoSuchElementException");	
			Assert.fail("To get attribute value of type " + sAttributeValue + " for Element with " + locator + " was not found in DOM in time - "+waitTime+" - NoSuchElementException");
		}
		catch(Exception e)
		{
			log.error("Unable to get attribute value of type " + sAttributeValue + " from the element "+ locator);
			Assert.fail("Unable to get findelement "+locator+"to get attribute " + sAttributeValue +" - " + e);
		}		
		return sValue;
	} 

	/**
	 * 
	 * TODO Safe method to retrieve the option selected in the drop down list 
	 *
	 * @param locator - locator value by which an element is located
	 * @param waitTime - Time to wait for an element
	 * @return - returns the option selected in the drop down list
	 */
	public String safeGetSelectedOptionInDropDown(By locator, int sWaitTime) throws Exception
	{
		String dropDownSelectedValue = null;

		try
		{
			//return getSelectedOptionInDropDown(locator, sWaitTime);
			if(isElementPresent(locator, sWaitTime))
			{
				Select dropDownName = new Select(driver.findElement(locator));
				//setHighlight(driver, dropDownName);
				dropDownSelectedValue = dropDownName.getFirstSelectedOption().getText();
				log.info("Value " +  dropDownSelectedValue + " has been selected from dropdown field " + locator);
			}
			else
			{
				log.error("Dropdown with locator: "+locator+" is not displayed");
				Assert.fail("Dropdown with locator: "+locator+" is not displayed");
			}
		}
		catch(StaleElementReferenceException e)
		{			
			log.error("Element with " + locator +"is not attached to the page document");
			Assert.fail("Element with " + locator +"is not attached to the page document");			
		}
		catch (NoSuchElementException e)
		{	    	
			log.error("Element " + locator + " was not found in DOM");	
			Assert.fail("Element "+ locator +" was not found in DOM");			
		}
		catch(Exception e)
		{
			log.error("Unable to retrieve drop down field value:"+locator + " - " + e);
			Assert.fail("Unable to retrieve drop down field value:"+locator);
		}
		return dropDownSelectedValue;
	}

	/**
	 * 
	 * TODO Safe method to verify whether the element is exists in the list box or not
	 *
	 * @param locator - locator value by which an element is located
	 * @param waitTime - Time to wait for an element
	 * @return - returns 'true' if the mentioned value exists in the list box else returns 'false'
	 * 
	 */	
	public boolean safeVerifyListBoxValue(By locator, String value, int sWaitTime) throws Exception
	{
		boolean isExpected = false;
		try
		{						
			if(isElementPresent(locator, sWaitTime))
			{
				WebElement listBox = driver.findElement(locator);
				java.util.List<WebElement> listBoxItems = listBox.findElements(By.tagName("li"));			            
				for(WebElement item : listBoxItems)
				{
					if(item.getText().equals(value))      		
						isExpected = true;
				}
				log.info("Listbox item: " + value + " is existed in listbox " +  locator);
			}
			else
			{
				log.error("Listbox with locator: "+locator+" is not displayed");
				Assert.fail("Listbox with locator: "+locator+" is not displayed");
			}

		}
		catch(StaleElementReferenceException e)
		{			
			log.error("Listbox item: " + value + " in listbox with" +  locator + " is not attached to the page document");
			Assert.fail("Listbox item: " + value + " in listbox with" +  locator + " is not attached to the page document");			

		}
		catch (NoSuchElementException e)
		{	    	
			log.error("Listbox item: " + value + " in listbox with" +  locator + " was not found in DOM");	
			Assert.fail("Listbox item: " + value + " in listbox with" +  locator + " was not found in DOM");			
		}
		catch(Exception e)
		{
			log.error("Unable to verify Listbox value: " + value + " in listbox field " + locator + " - " + e);
			Assert.fail("Unable to verify Listbox value: " + value + " in listbox field " + locator);
		}
		return isExpected;
	}


	/**
	 * Method for switching to frame using frame id
	 * @param driver
	 * @param frame
	 */
	public void selectFrame(String frame)
	{
		try
		{
			driver.switchTo().frame(frame);
			log.info("Navigated to frame with id " + frame);	
		}
		catch(NoSuchFrameException e)
		{
			log.error("Unable to locate frame with id " + frame);
			Assert.fail("Unable to locate frame with id " + frame );
		}
		catch(Exception e)
		{
			log.error("Unable to navigate to frame with id " + frame + " - " + e);
			Assert.fail("Unable to navigate to frame with id " + frame );
		}
	}


	/**
	 * Method - Method for switching to frame in a frame
	 * @param driver
	 * @param ParentFrame
	 * @param ChildFrame
	 */
	public void selectFrame(String ParentFrame, String ChildFrame)
	{
		try
		{
			driver.switchTo().frame(ParentFrame).switchTo().frame(ChildFrame);
			log.info("Navigated to innerframe with id " + ChildFrame + "which is present on frame with id" + ParentFrame);
		}
		catch(NoSuchFrameException e)
		{
			log.error("Unable to locate frame with id " + ParentFrame+" or "+ ChildFrame);
			Assert.fail("Unable to locate frame with id " + ParentFrame+" or "+ ChildFrame);
		}
		catch(Exception e)
		{
			log.error("Unable to navigate to innerframe with id " + ChildFrame + "which is present on frame with id" + ParentFrame + " - " + e);		
			Assert.fail("Unable to navigate to innerframe with id " + ChildFrame + "which is present on frame with id" + ParentFrame );
		}
	}


	/**
	 * Method - Method for switching to frame using any locator of the frame
	 * @param driver
	 * @param ParentFrame
	 * @param ChildFrame
	 */
	public void selectFrame(By Framelocator, int waitTime)
	{
		try
		{
			if(isElementPresent(Framelocator,waitTime))
			{
				WebElement Frame = driver.findElement(Framelocator);             
				driver.switchTo().frame(Frame);
				log.info("Navigated to frame with locator " + Framelocator);	
			}
			else
			{
				log.error("Unable to navigate to frame with locator " + Framelocator );
				Assert.fail("Unable to navigate to frame with locator " + Framelocator );
			}
		}
		catch(NoSuchFrameException e)
		{
			log.error("Unable to locate frame with locator " + Framelocator);
			Assert.fail("Unable to locate frame with locator " + Framelocator);
		}
		catch(Exception e)
		{
			log.error("Unable to navigate to frame with locator " + Framelocator + " - " + e);
			Assert.fail("Unable to navigate to frame with locator " + Framelocator);
		}
	}


	/**
	 * Method - Method for switching back to webpage from frame
	 * @param driver
	 */
	public void defaultFrame()
	{
		try
		{
			driver.switchTo().defaultContent();
			log.info("Navigated to back to webpage from frame");
		}
		catch(Exception e)
		{
			log.error("unable to navigate back to main webpage from frame" + " - " + e);
			Assert.fail("unable to navigate back to main webpage from frame");
		}
	}


	/**
	 * Method: Gets a UI element attribute value and compares with expected value
	 * @param locator
	 * @param attributeName
	 * @param expected
	 * @param contains
	 * @param waitTime
	 * @return - Boolean (true if matches)
	 */
	public boolean getAttributeValue(By locator, String attributeName, String expected, boolean contains, int... waitTime)
	{
		boolean bvalue = false;
		try
		{
			if(isElementPresent(locator, getWaitTime(waitTime))){
				String sTemp=driver.findElement(locator).getAttribute(attributeName);
				if(!(sTemp.contains(expected)^contains)){
					bvalue = true;
					log.info("Got attribute value " + sTemp + " of type " + attributeName + " from the element "+ locator + " and it is matched with expected " + expected);		
				}
			}
			else{
				log.error("Element " + locator + " was not found in DOM");	
				Assert.fail("Element "+ locator +" was not found in DOM");	
			}

		}
		catch(StaleElementReferenceException e)
		{			
			log.error("Element with " + locator +"is not attached to the page document");
			Assert.fail("Element with " + locator +"is not attached to the page document");			
		}
		catch (NoSuchElementException e)
		{	    	
			log.error("Element " + locator + " was not found in DOM");	
			Assert.fail("Element "+ locator +" was not found in DOM");			
		}
		catch(Exception e)
		{
			log.error("Unable to get attribute value of type " + attributeName + " from the element "+ locator + " - " + e);		
			Assert.fail("Unable to get attribute value of type " + attributeName + " from the element "+ locator);
		}
		return bvalue;
	}


	/**
	 * Method: Gets an UI element attribute value
	 * @param locator
	 * @param attributeName
	 * @param waitTime
	 * @return - String (Attribute Value)
	 */
	public String getAttributeValue(By locator, String attributeName, int... waitTime)
	{
		String sValue=null;
		try
		{
			if(isElementPresent(locator, getWaitTime(waitTime))){
				sValue=driver.findElement(locator).getAttribute(attributeName);
				log.info("Got attribute value of the type " + attributeName + " is: " + sValue);
			}
			else{
				log.error("Element " + locator + " was not found in DOM");	
				Assert.fail("Element "+ locator +" was not found in DOM");	
			}

		}
		catch(StaleElementReferenceException e)
		{	log.error("To get attribute value of type " + attributeName + " for Element with " + locator + " is not attached to the page document");
		Assert.fail("To get attribute value of type " + attributeName + " for Element with " + locator + " is not attached to the page document");
		}
		catch (NoSuchElementException e)
		{	    	
			log.error("To get attribute value of type " + attributeName + " for Element with " + locator + " was not found in DOM");	
			Assert.fail("To get attribute value of type " + attributeName + " for Element with " + locator + " was not found in DOM");			
		}
		catch(Exception e)
		{
			log.error("Unable to get attribute value of type " + attributeName + " from the element "+ locator + " - " + e);		
			Assert.fail("Unable to get attribute value of type " + attributeName + " from the element "+ locator);
		}
		return sValue;
	}


	/**
	@Method Highlights on current working element or locator
	@param Webdriver instance
	@param WebElement
	@return void (nothing)
	 */
	public void setHighlight(WebElement element)
	{
		if(sys.getProperty("HighlightElements").equalsIgnoreCase("true"))
		{
			String attributevalue = "border:3px solid red;";
			JavascriptExecutor executor = (JavascriptExecutor) driver;
			String getattrib = element.getAttribute("style");
			executor.executeScript("arguments[0].setAttribute('style', arguments[1]);", element, attributevalue);
			try 
			{
				Thread.sleep(100);
			} 
			catch (InterruptedException e) 
			{
				log.error("Sleep interrupted - ");
			}
			executor.executeScript("arguments[0].setAttribute('style', arguments[1]);", element, getattrib);
		}       
	}



	/**
	 * Method: Highlights on current working element or locator
	 * @param driver
	 * @param locator
	 * @throws Exception
	 */
	//	public void highlightElement(WebElement element) throws Exception
	//	{
	//		if(sys.getProperty("HighlightElements").equalsIgnoreCase("true"))
	//		{
	//	        String attributevalue="border:3px solid green;";//change border width and color values if required
	//	        JavascriptExecutor executor= (JavascriptExecutor) driver;
	//	        String getattrib=element.getAttribute("style");
	//	        executor.executeScript("arguments[0].setAttribute('style', arguments[1]);", element, attributevalue);
	//	        Thread.sleep(100);
	//	        executor.executeScript("arguments[0].setAttribute('style', arguments[1]);", element, getattrib);
	//		}       
	//    }

	public void jsClickOnElement(String cssSelector)
	{
		try
		{
			JavascriptExecutor js = (JavascriptExecutor) driver;
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append("var x = $(\'"+cssSelector+"\');");
			stringBuilder.append("x.click();");
			js.executeScript(stringBuilder.toString());
		}
		catch(Exception e)
		{
			log.error("some exception occured while trying to click on locator with css:"+cssSelector+" using Jsclick" + " - " + e);
			Assert.fail("some exception occured while trying to click on locator with css:"+cssSelector+" using Jsclick");
		}
	}

	/**
	 * 
	 * Method: To click on certain locator using sikuli
	 *
	 * @param sImagePath
	 * @param sLocatorName
	 * @param waitTime, time in seconds
	 * @Eample safeClickUsingSikuli(SelectFilesImagePath,"SelectFiles button",SHORTWAIT);
	 */
	/*	public void safeClickUsingSikuli(String sImagePath, String sLocatorName,int waitTime)
	{
		try
		{
			Screen screen = new Screen();
			Settings.MinSimilarity = 0.87;
			screen.wait(sImagePath,waitTime);
			screen.click(sImagePath);
		}
		catch(FindFailed e)
		{
			log.error("Unable to click on '"+sLocatorName+"' using sikuli, please check screenshot and image path"+"\n" + " - " + e.getMessage());
			Assert.fail("Unable to click on '"+sLocatorName+"' using sikuli, please check screenshot and image path");
		}
		catch(Exception e)
		{
			log.error("Unable to click on '"+sLocatorName+"' using sikuli, please check screenshot and image path"+"\n" + " - " + e.getMessage());
			Assert.fail("Unable to click on '"+sLocatorName+"' using sikuli, please check screenshot and image path");
		}
	}*/
	public void safeClickUsingSikuli(String sImagePath, String sLocatorName,int waitTime)
	{
		File fw;
		try
		{
			//Thread.sleep(2000);

			fw=new File(sImagePath);
			// Specify an image as the target to find on the screen

			Target imageTarget = new ImageTarget(fw); 
			imageTarget.setMinScore(0.87);
			ScreenRegion s = new DesktopScreenRegion();
			s.wait(imageTarget, waitTime);
			ScreenRegion r = s.find(imageTarget);
			Canvas canvas = new DesktopCanvas();
			canvas.addBox(r).display(1);


			Mouse mouse = new DesktopMouse();
			mouse.click(r.getCenter());

			//bValue = true;           

			log.info("Locator field with name '"+sLocatorName+"' is displayed using sikuli");

		}
		catch(org.sikuli.api.SikuliRuntimeException e)
		{
			log.error("Locator with name '"+sLocatorName+"' is not displayed using sikuli, please check screenshot and image path");
			Assert.fail("Locator with name '"+sLocatorName+"' is not displayed using sikuli, please check screenshot and image path");
		}
		catch(Exception e)
		{
			log.error("Locator with name '"+sLocatorName+"' is not displayed using sikuli, please check screenshot and image path" + " - " + e);
			Assert.fail("Locator with name '"+sLocatorName+"' is not displayed using sikuli, Some exception occured other than FindFailed, please check screenshot and image path");

		}
	}


	/**
	 * 
	 * Method: To move to certain locator using sikuli
	 *
	 * @param sImagePath
	 * @param sLocatorName
	 * @param waitTime, time in seconds
	 * @Example safeMoveMouseUsingSikuli(ChooseFilesImagePath,"Choose Files Text",SHORTWAIT);	
	 */
	/*public void safeMoveMouseUsingSikuli(String sImagePath, String sLocatorName,int waitTime)
	{
		try
		{
			Screen screen = new Screen();
			Settings.MinSimilarity = 0.87;
			screen.wait(sImagePath,waitTime);
			screen.mouseMove(sImagePath);
		}
		catch(FindFailed e)
		{
			log.error("Unable to move to '"+sLocatorName+"' using sikuli, please check screenshot and image path"+"\n" + " - " + e.getMessage());
			Assert.fail("Unable to move to '"+sLocatorName+"' via mouse using sikuli, please check screenshot and image path");
		}
		catch(Exception e)
		{
			log.error("Unable to move to '"+sLocatorName+"' using sikuli, please check screenshot and image path"+"\n" + " - " + e.getMessage());
			Assert.fail("Unable to move to '"+sLocatorName+"' using sikuli, please check screenshot and image path");
		}
	}*/

	/**
	 * 
	 * This method is used to paste the text in required field using sikuli
	 *
	 * @param sImagePath
	 * @param sText
	 * @param sLocatorName
	 * @param waitTime, time in seconds
	 */
	/*public void safePasteTextInFieldUsingSikuli(String sImagePath,String sText,String sLocatorName,int waitTime)
	{
		try
		{
			Screen screen = new Screen();
			Settings.MinSimilarity = 0.87;
			screen.wait(sImagePath,waitTime);
			screen.click(sImagePath);
			Thread.sleep(1000);			
			copyTextToClipboard(sText); 
			Thread.sleep(1000);			
			screen.type("v", KeyModifier.CTRL);
			Thread.sleep(2000);
			log.info("Pasted the text "+sText+" in '"+sLocatorName+"' field using sikuli");
		}
		catch(FindFailed e)
		{
			log.error("Unable to enter/paste "+sText+" text in '"+sLocatorName+"' field using sikuli, please check screenshot and image path"+"\n" + " - " + e.getMessage());
			Assert.fail("Unable to enter/paste "+sText+" text in '"+sLocatorName+"' field using sikuli, please check screenshot and image path");
		}
		catch(Exception e)
		{
			log.error("Unable to enter/paste "+sText+" text in '"+sLocatorName+"' field using sikuli, please check screenshot and image path"+"\n" + " - " + e.getMessage());
			Assert.fail("Unable to enter/paste "+sText+" text in '"+sLocatorName+"' field using sikuli, please check screenshot and image path");
		}
	}*/

	/**
	 * 
	 * This method is used to switch to windows based on provided number.
	 *
	 * @param num , Window number starting at 0
	 */
	public void switchToWindow(int num)
	{
		try
		{
			int numWindow = driver.getWindowHandles().size();
			String[] window = (String[])driver.getWindowHandles().toArray(new String[numWindow]);
			driver.switchTo().window(window[num]);
			log.info("Navigated succesfsully to window with sepcified number: "+num);
		}
		catch(NoSuchWindowException e)
		{
			log.error("Window with sepcified number "+num+" doesn't exists. Please check the window number or wait until the new window appears");
			Assert.fail("Window with sepcified number  "+num+"doesn't exists. Please check the window number or wait until the new window appears");			
		}
		catch(Exception e)
		{
			log.error("Some exception occured while switching to new window with number: "+num + " - " + e);
			Assert.fail("Some exception occured while switching to new window with number: "+num);			
		}
	}

	/**
	 * 
	 * This method is used to switch to windows based on provided window title
	 * @param title, Window title to which
	 * @param, waitTime
	 *
	 **/

	public void switchToWindow(String title) {
		try {
			int numWindows = driver.getWindowHandles().size();
			int i = 0;
			while (numWindows < 2) {
				driver.wait(500);
				if (++i > 21) {
					break;
				}
			}
			if (numWindows >= 2) {

				boolean bFlag=false;
				for (String handle : driver.getWindowHandles()) {
					driver.switchTo().window(handle);
					if (driver.getTitle().contains(title)) {
						log.info("Navigated succesfsully to new windowwith title "+title);
						bFlag = true;
						break;
					}
				}
				if(!bFlag){
					log.error("Window with sepcified titlt "+title+" doesn't exists. Please check the window title or wait until the new window appears");
					Assert.fail("Window with sepcified number  "+title+"doesn't exists. Please check the window title or wait until the new window appears");
				}
			}
			else{
				log.info("Can not switch to new window as there is only one window, wait until the new window appears");
			}
		}
		catch(NoSuchWindowException e)
		{
			log.error("Can not switch to new window ");
			Assert.fail("Can not switch to new window ");			
		}
		catch(Exception e)
		{
			log.error("Some exception occured while switching to new window with number: " + e);
			Assert.fail("Some exception occured while switching to new window with number: ");			
		}
	}

	/**
	 * 
	 * This method is used to switch to windows based on provided unique element locater
	 * @param locater, unique element locater
	 * @param, waitTime
	 *
	 **/

	public void switchToWindow(By locator, int...waitTime) {
		try {
			int numWindows = driver.getWindowHandles().size();
			int i = 0;
			while (numWindows < 2) {
				driver.wait(500);
				if (++i > 21) {
					break;
				}
			}
			if (numWindows >= 2) {
				boolean bFlag = false;
				for (String handle : driver.getWindowHandles()) {
					driver.switchTo().window(handle);
					if (isElementPresent(locator, getWaitTime(waitTime))) {
						log.info("Navigated succesfsully to new window");
						bFlag = true;
						break;
					}
				}
				if (!bFlag) {
					log.error("Window with sepcified locator " + locator + " doesn't exists. Please check the locator or wait until the new window appears" );
					Assert.fail("Window with sepcified locator  " + locator + "doesn't exists. Please check the locator or wait until the new window appears" );
				}
			}
			else{
				log.info("Can not switch to new window as there is only one window, wait until the new window appears");
			}
		}
		catch(NoSuchWindowException e)
		{
			log.error("Can not switch to new window ");
			Assert.fail("Can not switch to new window ");			
		}
		catch(Exception e)
		{
			log.error("Some exception occured while switching to new window with number: " + e);
			Assert.fail("Some exception occured while switching to new window with number: ");			
		}
	}

	/**
	 * 
	 * This method is used to refresh the web page
	 *
	 */
	public void refresh()
	{
		try
		{
			driver.navigate().refresh();			
		}
		catch(Exception e)
		{
			log.error("Some exception occured while refreshing the page, exception message: " + e);
			Assert.fail("Some exception occured while refreshing the page,");			
		}		
	}

	/**
	 * 
	 * This method is used to navigate to back page
	 *
	 */
	public void navigateToBackPage()
	{
		try
		{
			driver.navigate().back();
		}
		catch(Exception e)
		{
			log.error("Some exception occured while navigating to back page, exception message: " + e);
			Assert.fail("Some exception occured while navigating to back page");
		}
	}

	/**
	 * 
	 * This method is used to perform web page forward navigation
	 *
	 */
	public void navigateToForwardPage()
	{
		try
		{
			driver.navigate().forward();
		}
		catch(Exception e)
		{
			log.error("Some exception occured while forwarding to a page, exception message: " + e);
			Assert.fail("Some exception occured while forwarding to a page");
		}
	}

	/**
	 * 
	 * This method is used to retrieve current url
	 *
	 * @return, returns current url
	 */
	public String getCurrentURL()
	{
		String sUrl = null;
		try
		{
			sUrl = driver.getCurrentUrl();
		}
		catch(Exception e)
		{
			log.error("Some exception occured while retriving current url, exception message: " + e);
			Assert.fail("Some exception occured while retriving current url");
		}

		return sUrl;
	}

	/**
	 * 
	 * This method is used to retrieve current web page title
	 *
	 * @return , returns current web page title  
	 */
	public String getTitle()
	{
		String sTitle = null;
		try
		{
			sTitle = driver.getTitle();
		}
		catch(Exception e)
		{
			log.error("Some exception occured while retriving title of the web page, exception message: " + e);
			Assert.fail("Some exception occured while retriving title of the web page");
		}
		return sTitle;
	}

	/**
	 * This method is used to Delete all cookies
	 */
	public void deleteAllCookies()
	{
		try
		{
			driver.manage().deleteAllCookies();
		}
		catch(Exception e)
		{
			log.error("Some exception occured while deleting all cookies, exception message: " + e);
			Assert.fail("Some exception occured while deleting all cookies");
		}
	}

	/**
	 * 
	 * This method is used to retrieve page source of the web page
	 *
	 * @return , returns page source of the web page
	 */
	public String getPageSource()
	{
		String sPageSource = null;
		try
		{
			sPageSource = driver.getPageSource();
		}
		catch(Exception e)
		{
			log.error("Some exception occured while retriving page source, exception message: " + e);
			Assert.fail("Some exception occured while retriving page source");
		}
		return sPageSource;
	}

	/**
	 * 
	 * This method is used to return number of locators found
	 *
	 * @param Locator
	 * @return , returns number of locators
	 */
	public int getLocatorCount(By Locator)
	{
		int iCount = 0;
		try
		{
			iCount=driver.findElements(Locator).size();
		}
		catch(Exception e)
		{
			log.error("Some exception occured while retriving Locator count, exception message: " + e);
			Assert.fail("Some exception occured while retriving Locator count");
		}
		return iCount;
	}

	/**
	 * 
	 * This method is used to return list of WebElements matched by locator 
	 *
	 * @param Locator
	 * @return
	 */
	public List<WebElement> LocatorWebElements(By Locator) 
	{
		List<WebElement> list = null;
		try
		{
			list= driver.findElements(Locator);
		}
		catch(Exception e)
		{
			log.error("Some exception occured while retriving web elements of a locator, exception message: " + e);
			Assert.fail("Some exception occured while retriving web elements of a locator");
		}
		return list;
	}

	/**
	 * This method waits for Specified URL to load and writes page load time to load URL to 'PageLoadTime_Summary.html' file.
	 * @param url - Need to pass the URL to be loaded
	 * @throws Exception
	 */
	public void navigateToURLandRetrivePageLoadTime(String url, int timeOutInSeconds) throws Exception {
		long start = System.currentTimeMillis();
		driver.get(url);
		waitForPageToLoad(timeOutInSeconds);
		long finish = System.currentTimeMillis();
		long totalTime = finish - start;
		ReportSetup.Report_PageLoadTime(url, totalTime);	
	}

	/**
	 * This method is used to return working locator based on the locators which we have passed
	 * @param iWaitTime , Need to pass the time to wait for each locator
	 * @param Locators, Need to pass the list of locators to be verified
	 * @return , returns correct working locator
	 */
	public By selectWorkingLocator(int iWaitTime, By... Locators)
	{
		By WorkingLocator = null;
		boolean bValue = false;
		for(By Locator: Locators)
		{
			if(isElementPresent(Locator, iWaitTime))
			{
				WorkingLocator = Locator;
				bValue = true;
				break;
			}
		}
		if(!bValue)
		{
			log.error("None of the locators are visible");
			Assert.fail("None of the locators are visible");
		}
		return WorkingLocator; 
	}
}
