package com.page.locators;

import org.openqa.selenium.By;

public interface TrashLocators 
{
	public static By TRASH_PAGE = By.xpath(".//div[contains(text(),'messages that have been in Trash more than 30 days will be automatically deleted')]");
	public static By LOADING = By.xpath(".//span[text() = 'Loading...']");
}
