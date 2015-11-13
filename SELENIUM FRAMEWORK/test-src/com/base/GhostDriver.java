package com.base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * Implementation of HeadLess driver i.e GhostDriver using PhantomJs
 * There are some issues with PhantomJs latest release,Once the official fixes come then 
 * We need to test the same for GhostDriver,Then only we can run scripts in headless mode using
 * GhostDriver
 *   
 */

public class GhostDriver implements IBrowser {
	private WebDriver driver;
	DesiredCapabilities capabilities = new DesiredCapabilities();
	String fileSeperator = System.getProperty("file.separator");

	/**
	 * This method initiates HeadLess browser and returns the driver object
	 * @return WebDriver type
	 */
	@Override
	public WebDriver init() {
		capabilities.setCapability(
				PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY,
				getDriverPath());
		capabilities.setJavascriptEnabled(true);
		capabilities.setCapability("takesScreenshot", true);
		driver = new PhantomJSDriver(capabilities);
		return driver;
	}

	/**
	 * Method which returns the .exe file for phantomjs
	 * 
	 * @return String -- Phantomjs exe path
	 */
	private String getDriverPath() {
		return System.getProperty("user.dir") + fileSeperator + "phantomjs.exe";

	}

}
