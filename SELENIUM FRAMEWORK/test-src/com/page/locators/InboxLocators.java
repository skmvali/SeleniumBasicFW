package com.page.locators;

import org.openqa.selenium.By;

public interface InboxLocators
{
	public static By LOADING = By.xpath(".//span[text() = 'Loading...']");
	public static By EMAILSUBJECT = By.xpath(".//h2[text() = '%s']");
	public static By EMAILBODY=By.className("a3s");
	public static By EMAIL = By.xpath(".//span/b[text() = '%s']");
}
