package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class CredentialObject {

	private WebDriver driver;
	private WebDriverWait webDriverWait;

	@FindBy(id = "nav-credentials-tab")
	private WebElement credentialsTab;

	@FindBy(id = "addCredentialBtn")
	private WebElement addCredentialButton;

	@FindBy(id = "credential-url")
	private WebElement credentialUrl;

	@FindBy(id = "credential-username")
	private WebElement credentialUsername;

	@FindBy(id = "credential-password")
	private WebElement credentialPassword;

	@FindBy(id = "saveCredentialChanges")
	private WebElement saveChangesButton;

	@FindBy(css = "button.btn.btn-success")
	private WebElement editCredentialButton;

	@FindBy(css = "a.btn.btn-danger")
	private WebElement deleteCredentialButton;

	public CredentialObject(WebDriver webDriver) {
		this.driver = webDriver;
		this.webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(2));
		PageFactory.initElements(webDriver, this);
	}

	// Method to navigate to the Credentials tab
	public void navigateToCredentialsTab() {
		webDriverWait.until(ExpectedConditions.elementToBeClickable(credentialsTab));
		credentialsTab.click();
	}

	// Method to add a new credential
	public void addNewCredential(String url, String username, String password) {
		webDriverWait.until(ExpectedConditions.elementToBeClickable(addCredentialButton));
		addCredentialButton.click();

		webDriverWait.until(ExpectedConditions.visibilityOf(credentialUrl));
		credentialUrl.click();
		credentialUrl.sendKeys(url);

		webDriverWait.until(ExpectedConditions.visibilityOf(credentialUsername));
		credentialUsername.click();
		credentialUsername.sendKeys(username);

		webDriverWait.until(ExpectedConditions.visibilityOf(credentialPassword));
		credentialPassword.click();
		credentialPassword.sendKeys(password);

		saveChangesButton.click();
	}

	// Method to edit an existing credential
	public void editCredential(String newUrl, String newUsername, String newPassword) {
		webDriverWait.until(ExpectedConditions.elementToBeClickable(editCredentialButton));
		editCredentialButton.click();

		webDriverWait.until(ExpectedConditions.visibilityOf(credentialUrl));
		credentialUrl.click();
		credentialUrl.clear();
		credentialUrl.sendKeys(newUrl);

		webDriverWait.until(ExpectedConditions.visibilityOf(credentialUsername));
		credentialUsername.click();
		credentialUsername.clear();
		credentialUsername.sendKeys(newUsername);

		webDriverWait.until(ExpectedConditions.visibilityOf(credentialPassword));
		credentialPassword.click();
		credentialPassword.clear();
		credentialPassword.sendKeys(newPassword);

		saveChangesButton.click();
	}

	// Method to delete an existing credential
	public void deleteCredential() {
		webDriverWait.until(ExpectedConditions.elementToBeClickable(deleteCredentialButton));
		deleteCredentialButton.click();
	}
}
