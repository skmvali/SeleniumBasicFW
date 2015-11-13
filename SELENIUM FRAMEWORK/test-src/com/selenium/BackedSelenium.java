package com.selenium;


/*Usage
 * BackedSelenium _backed=new BackedSelenium(_driver);
Selenium seleniumRC=_backed.GetSeleniumRC();*/



import org.openqa.selenium.WebDriver;

import com.datamanager.ConfigManager;
import com.thoughtworks.selenium.Selenium;
import com.thoughtworks.selenium.webdriven.WebDriverBackedSelenium;

public class BackedSelenium 
{

	public WebDriver driver;
	ConfigManager app;
	
	//Constructor to initialize LoadProperties class and to assign local webdriver
	public BackedSelenium(WebDriver driver)
	{
		this.driver = driver;
		app = new ConfigManager("App");
	}
	
	public Selenium GetSeleniumRC() throws Exception
	{
		Selenium backed = new WebDriverBackedSelenium(driver, app.getProperty("App.URL"));
		return backed;
	}
}


