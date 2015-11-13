package com.page.locators;

import org.openqa.selenium.By;

public interface ComposeLocators
{
	public static By LOADING = By.xpath(".//span[text() = 'Loading...']");
	public static By SEND_BTN = By.xpath("//*[text() = 'Send']");
	public static By TO_FIELD = By.xpath(".//*[contains(@name, 'to')][@tabindex=1]");
	public static By SUBJECT_FIELD = By.xpath(".//*[@name='subjectbox']");
    public static By BODYFRAME = By.xpath("//div[@class='Am Al editable LW-avf' and @aria-label='Message Body']"); 
	public static By BODY_AREA_CSS = By.cssSelector(".editable.LW-avf");
	public static By VIEWMSG_LINK = By.id("link_vsm");
	public static By MSG_SENT_MESSAGE = By.xpath(".//div[contains(text(),'Your message has been sent.')]");
	public static By SAVENOW_BTN = By.xpath(".//*[@aria-label='Save & Close']");
	public static By SAVED_BTN = By.xpath(".//div[text() = 'Saved']");
	public static By SAVE_MSG = By.xpath(".//span[text() = 'Draft saved a ']");
}
