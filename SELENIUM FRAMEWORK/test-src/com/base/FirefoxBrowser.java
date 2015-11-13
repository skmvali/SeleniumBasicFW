package com.base;
import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

//import org.testng.Assert;
import com.testng.Assert;
import com.datamanager.ConfigManager;
/**
 * This class defines all methods required to initialize FireFoxDriver
 * Two methods are written so far - FFDriverDefault and with profile
 */
public class FirefoxBrowser implements IBrowser
{
	ConfigManager sys=new ConfigManager();
	WebDriver driver;
	FirefoxProfile firefoxProfile;
	private Logger log = Logger.getLogger("FirefoxBrowser");
	String fileSeperator = System.getProperty("file.separator");
	
	/**
	 * 
	 * This method is used to initiate firefox browser 
	 *
	 * @return , Returns firefox browser driver object
	 */
	public WebDriver init()
	{	
    	if(isProfilePresent())
    	{
    		log.info("Firefox profile Exists");
    		log.info("Launching firefox with specified profile");
			driver = FirefoxDriver(getProfilePath());
		}
		else
		{
			log.info("Launching firefox with a new profile");
			driver = FirefoxDriver(); 	    
		}
	    return driver;
	}
	
	
	
	
	/**
	 * Returns FirefoxDriver with default profile. This method will also disables the auto update of 
	 * firefox browser to next versions and takes care of accepting untrusted certificates 
	 * @return Webdriver initialized with FirefoxDriver
	 * @throws Exception
	 */
	public WebDriver FirefoxDriver()
	{
		firefoxProfile = new FirefoxProfile();
		setProfilePreferences();
		if(isBinaryPathPresent())
		{	
			File pathToBinary = new File(getBinaryPath());
			FirefoxBinary ffBinary = new FirefoxBinary(pathToBinary);
			driver = new FirefoxDriver(ffBinary, firefoxProfile);
		}
		else
			driver = new FirefoxDriver(firefoxProfile);
        return driver;
	}
	

	/**
	 * Returns FirefoxDriver with specific profile. This method will also disables the auto update of 
	 * firefox browser to next versions 
	 * @param Path where firefox profile is stored
	 * @return Webdriver intialized with Firefoxdriver
	 * @throws Exception
	 */
	
	public WebDriver FirefoxDriver(String profilePath)
	{
		//Initialize firefox browser with FF profile
		firefoxProfile = new FirefoxProfile(new File(profilePath));
		//set the Firefox preference "auto upgrade browser"  to false and to prevent compatibility issues
		setProfilePreferences();
		if(isBinaryPathPresent())
		{	
			File pathToBinary = new File(getBinaryPath());
			FirefoxBinary ffBinary = new FirefoxBinary(pathToBinary);
			driver = new FirefoxDriver(ffBinary, firefoxProfile);
		}
		else
			driver = new FirefoxDriver(firefoxProfile);
       	return driver;
	}
	
	/**
	 * 
	 * This method sets different profile preferences to firefox browser
	 *
	 */
	public void setProfilePreferences()
	{
		firefoxProfile.setAcceptUntrustedCertificates(true);
		firefoxProfile.setPreference("app.update.enabled", false);        
		firefoxProfile.setPreference("browser.download.folderList",2);
		firefoxProfile.setPreference("browser.download.manager.showWhenStarting",false);
		firefoxProfile.setPreference("browser.download.dir",getDownloadLocation());
		firefoxProfile.setPreference("browser.helperApps.neverAsk.openFile", "application/pdf, application/x-pdf, application/acrobat, applications/vnd.pdf, text/pdf, text/x-pdf, application/octet-stream, application/vnd.openxmlformats-officedocument.wordprocessingml.document, application/vnd.openxmlformats-officedocument.spreadsheetml.sheet, application/x-rar-compressed, application/zip");
		firefoxProfile.setPreference("browser.helperApps.neverAsk.saveToDisk","application/pdf, application/x-pdf, application/acrobat, applications/vnd.pdf, text/pdf, text/x-pdf, application/octet-stream, application/vnd.openxmlformats-officedocument.wordprocessingml.document, application/vnd.openxmlformats-officedocument.spreadsheetml.sheet, application/x-rar-compressed, application/zip");
		firefoxProfile.setPreference("browser.helperApps.alwaysAsk.force", false);
		firefoxProfile.setPreference("browser.download.manager.showAlertOnComplete",false);
	}
	

	/**
	 * Method to retrieve the profile path given in Properties file
	 * @return - returns Firefox profile directory
	 * @throws Exception
	 */
	public String getProfilePath()
	{
		return sys.getProperty("FireFoxProfilePath");
			
	}
	
	/**
	 * 
	 * This method is used to verify whether specified firefox profile exists or not
	 *
	 * @return , Returns true if firefox profile exists, else returns false
	 */
	public boolean isProfilePresent()
	{
		String profilePath = getProfilePath();
		try
		{
			if(!profilePath.isEmpty())
			{
				File profileFolder = new File(profilePath);
				return profileFolder.exists();
			}
			else
			{
				return false;
			}
		}
		catch(NullPointerException e)
		{
			log.error("Firefox Profile does not exist - "+profilePath);
			Assert.fail("Firefox Profile does not exist - "+profilePath);
			return false;
		}
	}
	
	/**
	 * Method to get file download path location
	 * @return - returns file download path
	 * @throws IOException
	 */	
	public String getDownloadLocation()
	{
		String DownloadPath= System.getProperty("user.dir")+fileSeperator+"Downloaded Files";
		return DownloadPath;
	}

	/**
	 * 
	 * This method is used to get firefox browser binary path
	 *
	 * @return , return the firefox browser binary path
	 */
	public String getBinaryPath()
	{
		return sys.getProperty("FireFoxBinaryPath");
			
	}
	
	/**
	 * 
	 * This method is used to verify whether specified firefox binary path exists or not 
	 *
	 * @return , Returns true if binary path exists, else returns false
	 */
	public boolean isBinaryPathPresent()
	{
		String binaryPath = getBinaryPath();
		try
		{
			if(!binaryPath.isEmpty())
			{
				File binaryFile = new File(binaryPath);
				return binaryFile.exists();
			}
			else
			{
				return false;
			}
		}
		catch(NullPointerException e)
		{
			log.error("Firefox Profile does not exist - "+binaryPath);
			Assert.fail("Firefox Profile does not exist - "+binaryPath);
			return false;
		}
	}
}
