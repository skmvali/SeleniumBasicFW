package com.base;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import com.datamanager.ConfigManager;
import com.testng.Assert;

/**
 * This class defines all methods required to initialize ChromeDriver So far
 * only one method is written to initialize with default settings
 */
public class ChromeBrowser implements IBrowser {
	ConfigManager sys = new ConfigManager();
	private Logger log = Logger.getLogger("ChromeBrowser");
	private WebDriver driver;
	String fileSeperator = System.getProperty("file.separator");

	/**
	 * 
	 * This method initiates Chrome browser and returns the driver object
	 *
	 * @return , returns the driver object after initiating Chrome browser
	 */
	public WebDriver init() {
		String UserDataPath = getUserDataPath();
		String ProfileName = getProfileName();
		if (isUserDataDirPresent()) {
			driver = initChromeDriver(UserDataPath, ProfileName);
		} else {
			driver = initChromeDriver();
		}
		return driver;
	}

	/**
	 * 
	 * This method initiates Chrome browser with default profile and returns the
	 * driver object
	 *
	 * @return , returns the driver object after initiating Chrome browser
	 */
	private WebDriver initChromeDriver() {
		log.info("Launching google chrome with new profile..");
		System.setProperty("webdriver.chrome.driver", getDriverPath());
		Map<String, Object> prefs = new HashMap<String, Object>();
		prefs.put("download.default_directory", getDownloadLocation());
		prefs.put("download.prompt_for_download", false);
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--test-type", "start-maximized");
		options.setExperimentalOption("prefs", prefs);
		options.addArguments("--disable-extensions");
		log.info("chrome driver initialized..");
		return new ChromeDriver(options);
	}

	/**
	 * 
	 * This method initiates chrome browser with specified profile
	 *
	 * @param UserDataPath
	 *            , Need to pass the chrome user directory path
	 * @param ProfileName
	 *            , Need to the chrome profile name
	 * @return , returns the driver object after initiating Chrome browser with
	 *         specified profile
	 */
	private WebDriver initChromeDriver(String UserDataPath, String ProfileName) {
		log.info("Launching google chrome with specified profile - "
				+ ProfileName);
		System.setProperty("webdriver.chrome.driver", getDriverPath());
		Map<String, Object> prefs = new HashMap<String, Object>();
		prefs.put("download.default_directory", getDownloadLocation());
		prefs.put("download.prompt_for_download", false);
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--test-type", "start-maximized");
		if (isProfileDirPresent()) {
			log.info("Running with specified chrome profile");
			options.addArguments("user-data-dir=" + UserDataPath);
			options.addArguments("--profile-directory=" + ProfileName);
			log.info("Init chrome driver with custom profile is completed..");
		} else {
			log.info("Specified chrome profile does not exists in 'User Data' folder");
			log.info("Hence Chrome Browser is launched with a new profile..");
		}
		options.setExperimentalOption("prefs", prefs);
		return new ChromeDriver(options);
	}

	/**
	 * 
	 * This method returns the location of chromedriver based on Operating
	 * system he scripts executed
	 * 
	 * 
	 * @return, returns chromedriver.exe file path
	 */
	public String getDriverPath() {

		String chromeLocation = System.getProperty("user.dir") + fileSeperator
				+ "Resources" + fileSeperator + "Drivers" + fileSeperator;

		if (System.getProperty("os.name").toLowerCase().contains("windows"))
			chromeLocation = chromeLocation + "chromedriver.exe";
		else if (System.getProperty("os.name").toLowerCase().contains("mac"))
			chromeLocation = chromeLocation + "chromedriver";

		return chromeLocation;

	}

	/**
	 * Method to retrieve the Chrome 'User Data' path given in Properties file
	 * 
	 * @return - returns chrome user data path
	 * @throws Exception
	 */
	public String getUserDataPath() {
		return sys.getProperty("ChromeUserDirectoryPath");
	}

	/**
	 * Method to retrieve the Chrome 'User Data' path given in Properties file
	 * 
	 * @return - returns chrome user data path
	 * @throws Exception
	 */
	public String getProfileName() {
		return sys.getProperty("ChromeProfileFoldername");
	}

	/**
	 * Method to retrieve the Chrome 'User Data' path given in Properties file
	 * 
	 * @return - returns chrome user data path
	 * @throws Exception
	 */
	public boolean isUserDataDirPresent() {
		String sUserData = getUserDataPath();
		try {
			if (!sUserData.isEmpty()) {
				File UserDataFolder = new File(sUserData);
				return UserDataFolder.exists();
			} else {
				return false;
			}
		} catch (NullPointerException e) {
			log.error("folder does not exists" + sUserData);
			// Assert.fail("folder does not exists"+sUserData);
			return false;
		}
	}

	/**
	 * Method to retrieve the Chrome 'User Data' path given in Properties file
	 * 
	 * @return - returns chrome user data path
	 * @throws Exception
	 */
	public boolean isProfileDirPresent() {
		String profilePath = getUserDataPath() + fileSeperator
				+ getProfileName();
		try {
			if (!profilePath.isEmpty()) {
				File profileFolder = new File(profilePath);
				return profileFolder.exists();
			} else {
				return false;
			}
		} catch (NullPointerException e) {
			log.error("Profile does not exists" + getProfileName());
			Assert.fail("Profile does not exists" + getProfileName());
			return false;
		}
	}

	/**
	 * Method to get file download path location
	 * 
	 * @return - returns file download path
	 * @throws IOException
	 */
	public String getDownloadLocation() {
		String DownloadPath = System.getProperty("user.dir") + fileSeperator
				+ "Downloaded Files";
		return DownloadPath;
	}
}
