package com.base;



import java.net.URL;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.datamanager.ConfigManager;
//import com.java.customexceptions.CloudNotFoundException;
//import org.testng.Assert;
import com.testng.Assert;
import com.utilities.CloudNotFoundException;


/**
 * This class is used to initialize RemoteDriver on cloud. The cloud instance is of Saucelabs or testingbot, depends
 * on the value set in "Cloud.type" under config.properties 
 */
public class RemoteDriver
{
	ConfigManager sys;
	WebDriver driver;
	private Logger log = Logger.getLogger("RemoteDriver");
	DesiredCapabilities capabilities;
	public RemoteDriver() 
	{
		
	}
	
	
	/**
	 * Gets remote driver by setting URL and capabilities - instance of remote driver of testingbot.com or saucelabs.com
	 * @return WebDriver configured on Saucelabs.com or testingbot.com 
	 * @throws Exception
	 */
	public WebDriver init(String browserType, String browserVersion,
			String OSName, String OSVersion, String session)
 {
		try {
			sys = new ConfigManager();
			Augmenter aug = new Augmenter();

			String cloud = sys.getProperty("Cloud.Host.URL");
			if (StringUtils.isBlank(cloud))
				throw new CloudNotFoundException("Cloud.Host.URL is blank. Please check sys.properties file ");
			driver = new RemoteWebDriver(new URL(cloud),new CapabilityHelper().addCapability(browserType,browserVersion, OSName, OSVersion, session));
			((RemoteWebDriver) driver).setFileDetector(new LocalFileDetector());
			driver = aug.augment(driver);
			log.info("************CLOUD/GRID REMOTEDRIVER DETAILS********************");
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
		return driver;
	}	
}
