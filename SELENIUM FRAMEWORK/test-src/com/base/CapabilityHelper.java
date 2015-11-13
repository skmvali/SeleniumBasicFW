package com.base;

import org.apache.log4j.Logger;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.datamanager.ConfigManager;
import com.utilities.CloudNotFoundException;
import com.utilities.UtilityMethods;


/**
 * Different RemoteWebdriver Capabilities can be constructed by varying BrowserName, Version, Platform.
 * These capabilities are needed to define initialize RemoteWebdriver, which in turn runs your tests on SauceLabs.com or Testingbot.com 
 * This Helper class will define several methods to get the capabilities by reading the Cloud.Browser.Name,
 * Cloud.Browser.Platform, Cloud.Browser.Version config parameters. 
 * e.g Capability is 
 * Browser: Firefox
 * Version: 14
 * Platform :XP
 */


public class CapabilityHelper {

	DesiredCapabilities capabilities=new DesiredCapabilities();
	ConfigManager sys;
	private Logger log = Logger.getLogger("CapabilityHelper");
	
	/**
	 * Gets the desiredCapability of respective CloudType(saucelabs or testinbot)
	 * @param CloudType
	 * @return DesiredCapability
	 * @throws CloudNotFoundException 
	 * @throws Exception browserType, browserVersion, OSName, OSVersion, session
	 */
	public DesiredCapabilities addCapability(String browserType, String browserVersion,
			String OSName, String OSVersion, String session) throws CloudNotFoundException
	{
		sys = new ConfigManager();
		String cloudURL=sys.getProperty("Cloud.Host.URL").toLowerCase();
		
		
		if(cloudURL.contains("saucelabs"))
		{
			setCloudCapabulities(browserVersion, session, browserType, OSName, OSVersion);
		}
		
		else if(cloudURL.contains("browserstack"))
		{					
			setCloudCapabulities(browserVersion, session, browserType, OSName, OSVersion);
		}
		
		else if(cloudURL.contains("Testingbot"))
		{					
			setCloudCapabulities(browserVersion, session, browserType, OSName, OSVersion);
		}
		
		else if(UtilityMethods.validateIP(cloudURL.split(":")[1].substring(2)))
		{
			
			setBrowser(browserType);
		}
		else
		{
			log.error("cloud entered does not exist - "+cloudURL);
			throw new CloudNotFoundException(cloudURL);
		}		
		return capabilities;
	}

	/**
	 * 
	 * This method sets the desired capabilities based on browser type
	 *
	 * @param browserType , Need to pass the browser type
	 */
	private void setBrowser(String browserType)
	{
		switch(browserType)
		{
			case "chrome":
				capabilities = DesiredCapabilities.chrome();
				break;
			case "firefox":
				capabilities = DesiredCapabilities.firefox();
				break;
			
			case "iexplore":
				capabilities = DesiredCapabilities.internetExplorer();
				capabilities.setCapability("ignoreProtectedModeSettings", true);
				capabilities.setCapability("enablePersistentHover", false);
				capabilities.setCapability("native_events", false);
				break;				
			case "safari":
				capabilities = DesiredCapabilities.safari();
				break;
			case "opera":	
				capabilities = DesiredCapabilities.opera();
				break;
			default:
				log.error("browser : "+browserType+" is invalid, Launching Firefox as browser of choice..");
				capabilities = DesiredCapabilities.firefox();		
		}
	}
	
	
	/**
	 * Extracted method that does common setup of setting OS/Platform where cloud browser should be launched
	 * @return null
	 */
	private void setOperatingSystem(String OSName, String OSVersion)
	{
		switch (OSName.toLowerCase()) {
		case "windows":
			switch (OSVersion.toLowerCase()){
			case "xp":
				capabilities.setCapability("platform", Platform.XP);
				break;
				
			case "vista":
				capabilities.setCapability("platform", Platform.VISTA);
				break;
				
			case "7":
				capabilities.setCapability("platform", Platform.WINDOWS);
				break;
				
			case "8":
				capabilities.setCapability("platform", Platform.WIN8);
				break;
				
			case "8_1":
				capabilities.setCapability("platform", Platform.WIN8_1);
				break;
			}
			
			
			break;
		case "mac":
			capabilities.setCapability(CapabilityType.PLATFORM, "Mac");
			break;
			
		case "linux":
			capabilities.setCapability("platform", Platform.LINUX);
			break;
			
		case "unix":
			capabilities.setCapability("platform", Platform.UNIX);
			break;
			
		case "any":
			capabilities.setCapability("platform", Platform.ANY);
			break;

		default:
			log.warn("Please select valid platform, contact your cloud providers for valid platform list \n For now executing on cloud default OS");
			capabilities.setCapability("platform", Platform.ANY);
			break;
		}
	}
	
	private void setCloudCapabulities(String browserVersion, String session, String browserType, String OSName, String OSVersion){
		
		
		setBrowser(browserType);
		setOperatingSystem(OSName, OSVersion);
		capabilities.setCapability("version", browserVersion);
		capabilities.setCapability("name", session);
	}
}
