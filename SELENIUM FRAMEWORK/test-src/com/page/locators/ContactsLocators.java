package com.page.locators;

import org.openqa.selenium.By;

public interface ContactsLocators
{
	public static By LOADING = By.xpath(".//span[text() = 'Loading...']");
	public static By CONTACTS_PAGE = By.xpath(".//td[contains(text(),'Welcome to Contacts')]");
	public static By ADDCONTACTS_BTN = By.cssSelector("div[data-tooltip='Add new contact']");
	public static By CONTACT_TXTAREA = By.xpath("//input[@placeholder='Enter a name']");
	public static By CREATE_BTN = By.xpath("//div[text() ='Create']");
	public static By SAVE_BTN=By.xpath("//div[text()='Save']");
	public static By BACK_PAGE=By.xpath("//div[@data-tooltip='Back'");
	public static By CONTACTSSAVED=By.xpath("//span[text()='Contact details saved.']");
	public static By UNDO_LNK_I = By.id("link_undo");
    public static By TXT=By.xpath(".//span[text() = '%s']");
	public static By MOREACTIONS_LNK=By.xpath("//div[@aria-label='More actions']");
	public static By DELETE=By.xpath("//div[text()='Delete']");
	public static By MORE_BTN = By.xpath(".//div[text() = 'More']");
	public static By DELETECONTACTS = By.xpath(".//div[text() = 'Delete contact']");
	public static By DELETED_MSG = By.xpath("//span[@class='bofITb' and contains(text(),'has been deleted')]");
	public static By EMAIL = By.xpath(".//div[@aria-label='Email']");
}