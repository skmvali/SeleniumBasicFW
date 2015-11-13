package com.base;

import org.apache.log4j.Logger;

/**
 * Class which provides the run-time object for Browser Classes
 */
public class BrowserProvider {
	private Logger log = Logger.getLogger("BrowserProvider");

	/**
	 * @param browserName
	 * @return browser class's object referring IBrowser Interface
	 * 
	 *         Method which creates the run-time object for Browser Classes
	 *         depending on the input of browserName,If invalid browser name is
	 *         passed, by default it'll set Firefox browser
	 */

	public IBrowser getBrowserInstance(String browserName) {
		if (browserName == null) {
			log.info("The Browser Name is provided as Null,Please provide the correct BrowserName in testNg.xml");
			return null;
		}
		if (browserName.equalsIgnoreCase("chrome")) {
			log.info("The Browser Name is provided as Chrome");
			return new ChromeBrowser();
		} else if (browserName.equalsIgnoreCase("firefox")) {
			log.info("The Browser Name is provided as Firefox");
			return new FirefoxBrowser();

		} else if (browserName.equalsIgnoreCase("iexplore")) {
			log.info("The Browser Name is provided as IE");
			return new IeBrowser();

		} else if (browserName.equalsIgnoreCase("safari")) {
			log.info("The Browser Name is provided as Safari");
			return new SafariBrowser();
		} else if (browserName.equalsIgnoreCase("headless")) {
			log.info("The Browser Name is provided as Headless");
			return new GhostDriver();
		}
		log.error("browser : " + browserName
				+ " is invalid, Launching Firefox as browser of choice..");
		return new FirefoxBrowser();
	}
}