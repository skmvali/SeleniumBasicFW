package com.base;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * This class defines all methods required to initialize IEDriver
 */
public class IeBrowser implements IBrowser {
	private Logger log = Logger.getLogger("IeBrowser");
	String fileSeperator = System.getProperty("file.separator");
	DesiredCapabilities capabilities;

	/**
	 * 
	 * This method is used initiate IE browser
	 * 
	 * @return , returns IE browser driver object
	 */
	public WebDriver init() {
		setCapabilities();
		log.info("Launching IE with default settings");
		WebDriver driver = new InternetExplorerDriver(capabilities);
		return driver;
	}

	/**
	 * Sets the required properties for IEdriver to initialize
	 * 
	 * @return IECapabilities with required properties set
	 */
	private void setCapabilities() {
		System.setProperty("webdriver.ie.driver", getIEDriverPath());
		capabilities = new DesiredCapabilities();
		capabilities.setCapability("ignoreProtectedModeSettings", true);
		capabilities.setCapability("enablePersistentHover", false);
		capabilities.setCapability("nativeEvents", false);
		capabilities.setCapability("ensureCleanSession", true);
		capabilities.setCapability("unexpectedAlertBehaviour", "accept");
	}

	/**
	 * 
	 * This method is used to get IEDriverServer.exe file location
	 * 
	 * @return , returns IEDriverServer.exe file path
	 */
	private String getIEDriverPath() {
		return System.getProperty("user.dir") + fileSeperator + "Resources"
				+ fileSeperator + "Drivers" + fileSeperator
				+ "IEDriverServer.exe";
	}

	/**
	 * This method is used to clear IE browser cache, history, Saved passwords
	 * etc.,
	 */
	public void clearIEBrowserCacheAndHistory() {
		try {
			Process p = Runtime.getRuntime().exec(
					"RunDll32.exe InetCpl.cpl,ClearMyTracksByProcess 4351");
			p.waitFor();
//			Thread.sleep(2000);

		} catch (IOException e) {

			e.printStackTrace();
		} catch (InterruptedException e) {

			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
