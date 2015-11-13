package com.page.locators;

import org.openqa.selenium.By;

public interface DraftsLocators 
{
	public static By DRAFT_PAGE = By.xpath(".//font[text()='Draft']");
	public static By SUBJECTLINE = By.xpath(".//span[text()='%s']");
}
