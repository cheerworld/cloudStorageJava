package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.io.File;
import java.time.Duration;
import java.util.List;

@SpringBootTest(classes = {CloudStorageApplication.class, TestConfiguration.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

    @LocalServerPort
    public int port;


    @Autowired
    private CredentialService credentialService;

    private static WebDriver driver;

    private ResultPage resultPage;

    private NoteObject noteObject;

    private CredentialObject credentialObject;

    @BeforeAll
    static void beforeAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void beforeEach(TestInfo testInfo) {
        this.driver = new ChromeDriver();
        // Create and log in with the existing user's credentials before specific tests
        if (testInfo.getTags().contains("AddNoteUser")) {
            doMockSignUp("adduser", "user", "adduser", "password");
            doLogIn("adduser", "password");
        }
        if (testInfo.getTags().contains("EditNoteUser")) {
            doMockSignUp("edituser", "user", "edituser", "password");
            doLogIn("edituser", "password");
            noteObject = new NoteObject(driver);
            noteObject.navigateToNotesTab();
            noteObject.addNewNote("Note 1 Title", "Note 1 Description");
            resultPage = new ResultPage(driver);
            resultPage.clickSuccessHomeLink();
            noteObject.navigateToNotesTab();
            noteObject.addNewNote("Note 2 Title", "Note 2 Description");
            resultPage.clickSuccessHomeLink();
        }
        if (testInfo.getTags().contains("DeleteNoteUser")) {
            doMockSignUp("deleteuser", "user", "deleteuser", "password");
            doLogIn("deleteuser", "password");
            noteObject = new NoteObject(driver);
            noteObject.navigateToNotesTab();
            noteObject.addNewNote("Test Note Title", "Test Note Description");
            resultPage = new ResultPage(driver);
            resultPage.clickSuccessHomeLink();
        }

        if (testInfo.getTags().contains("ExistingCredentialsUserForAdd")) {
            doMockSignUp("credentialuserAdd", "user", "credentialuserAdd", "password");
            doLogIn("credentialuserAdd", "password");
        }

        if (testInfo.getTags().contains("ExistingCredentialsUser")) {
            doMockSignUp("credentialuser", "user", "credentialuser", "password");
            doLogIn("credentialuser", "password");
            credentialObject = new CredentialObject(driver);
            credentialObject.navigateToCredentialsTab();
            credentialObject.addNewCredential("http://example1.com", "user1", "password1");
            resultPage = new ResultPage(driver);
            resultPage.clickSuccessHomeLink();
            credentialObject.navigateToCredentialsTab();
            credentialObject.addNewCredential("http://example2.com", "user2", "password2");
            resultPage.clickSuccessHomeLink();
        }

        if (testInfo.getTags().contains("ExistingCredentialsUserForDelete")) {
            doMockSignUp("credentialuser", "user", "credentialuserDelete", "password");
            doLogIn("credentialuserDelete", "password");
            credentialObject = new CredentialObject(driver);
            credentialObject.navigateToCredentialsTab();
            credentialObject.addNewCredential("http://example1.com", "user1", "password1");
            resultPage = new ResultPage(driver);
            resultPage.clickSuccessHomeLink();
            credentialObject.navigateToCredentialsTab();
            credentialObject.addNewCredential("http://example2.com", "user2", "password2");
            resultPage.clickSuccessHomeLink();
        }
    }

    @AfterEach
    public void afterEach() {
        if (this.driver != null) {
            driver.quit();
        }
    }

    @Test
    public void getLoginPage() {
        driver.get("http://localhost:" + this.port + "/login");
        Assertions.assertEquals("Login", driver.getTitle());
    }

    /**
     * PLEASE DO NOT DELETE THIS method.
     * Helper method for Udacity-supplied sanity checks.
     **/
    private void doMockSignUp(String firstName, String lastName, String userName, String password) {
        // Create a dummy account for logging in later.

        // Visit the sign-up page.
        WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(2));
        driver.get("http://localhost:" + this.port + "/signup");
        webDriverWait.until(ExpectedConditions.titleContains("Sign Up"));

        // Fill out credentials
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputFirstName")));
        WebElement inputFirstName = driver.findElement(By.id("inputFirstName"));
        inputFirstName.click();
        inputFirstName.sendKeys(firstName);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputLastName")));
        WebElement inputLastName = driver.findElement(By.id("inputLastName"));
        inputLastName.click();
        inputLastName.sendKeys(lastName);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
        WebElement inputUsername = driver.findElement(By.id("inputUsername"));
        inputUsername.click();
        inputUsername.sendKeys(userName);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
        WebElement inputPassword = driver.findElement(By.id("inputPassword"));
        inputPassword.click();
        inputPassword.sendKeys(password);

        // Attempt to sign up.
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonSignUp")));
        WebElement buttonSignUp = driver.findElement(By.id("buttonSignUp"));
        buttonSignUp.click();

		/* Check that the sign up was successful. 
		// You may have to modify the element "success-msg" and the sign-up 
		// success message below depening on the rest of your code.
		*/
        Assertions.assertTrue(driver.findElement(By.id("success-msg")).getText().contains("You successfully signed up!"));
    }


    /**
     * PLEASE DO NOT DELETE THIS method.
     * Helper method for Udacity-supplied sanity checks.
     **/
    private void doLogIn(String userName, String password) {
        // Log in to our dummy account.
        driver.get("http://localhost:" + this.port + "/login");
        WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(2));

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
        WebElement loginUserName = driver.findElement(By.id("inputUsername"));
        loginUserName.click();
        loginUserName.sendKeys(userName);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
        WebElement loginPassword = driver.findElement(By.id("inputPassword"));
        loginPassword.click();
        loginPassword.sendKeys(password);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-button")));
        WebElement loginButton = driver.findElement(By.id("login-button"));
        loginButton.click();

        webDriverWait.until(ExpectedConditions.titleContains("Home"));

    }

    // Log out from the current session.
    private void doLogOut() {
        driver.findElement(By.id("logout-button")).click();
        WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(2));
        webDriverWait.until(ExpectedConditions.titleContains("Login"));
    }

    /**
     * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the
     * rest of your code.
     * This test is provided by Udacity to perform some basic sanity testing of
     * your code to ensure that it meets certain rubric criteria.
     * <p>
     * If this test is failing, please ensure that you are handling redirecting users
     * back to the login page after a succesful sign up.
     * Read more about the requirement in the rubric:
     * https://review.udacity.com/#!/rubrics/2724/view
     */
    @Test
    public void testRedirection() {
        // Create a test account
        doMockSignUp("Redirection", "Test", "RT", "123");
        WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(5));
        webDriverWait.until(ExpectedConditions.urlToBe("http://localhost:" + this.port + "/login"));
        // Check if we have been redirected to the log in page.
        Assertions.assertEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());
    }

    /**
     * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the
     * rest of your code.
     * This test is provided by Udacity to perform some basic sanity testing of
     * your code to ensure that it meets certain rubric criteria.
     * <p>
     * If this test is failing, please ensure that you are handling bad URLs
     * gracefully, for example with a custom error page.
     * <p>
     * Read more about custom error pages at:
     * https://attacomsian.com/blog/spring-boot-custom-error-page#displaying-custom-error-page
     */
    @Test
    public void testBadUrl() {
        // Create a test account
        doMockSignUp("URL", "Test", "UT", "123");
        doLogIn("UT", "123");

        // Try to access a random made-up URL.
        driver.get("http://localhost:" + this.port + "/some-random-page");
        Assertions.assertFalse(driver.getPageSource().contains("Whitelabel Error Page"));
    }


    /**
     * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the
     * rest of your code.
     * This test is provided by Udacity to perform some basic sanity testing of
     * your code to ensure that it meets certain rubric criteria.
     * <p>
     * If this test is failing, please ensure that you are handling uploading large files (>1MB),
     * gracefully in your code.
     * <p>
     * Read more about file size limits here:
     * https://spring.io/guides/gs/uploading-files/ under the "Tuning File Upload Limits" section.
     */
    @Test
    public void testLargeUpload() {
        // Create a test account
        doMockSignUp("Large File", "Test", "LFT", "123");
        doLogIn("LFT", "123");

        // Try to upload an arbitrary large file
        WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(2));
        String fileName = "upload5m.zip";

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fileUpload")));
        WebElement fileSelectButton = driver.findElement(By.id("fileUpload"));
        fileSelectButton.sendKeys(new File(fileName).getAbsolutePath());

        WebElement uploadButton = driver.findElement(By.id("uploadButton"));
        uploadButton.click();
        try {
            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("success")));
        } catch (org.openqa.selenium.TimeoutException e) {
            System.out.println("Large File upload failed");
        }
        Assertions.assertFalse(driver.getPageSource().contains("HTTP Status 403 – Forbidden"));
    }

    @Test
    public void testHomePageNotAccessibleWithoutLogin() {
        driver.get("http://localhost:" + this.port + "/home");
        Assertions.assertNotEquals("Home", driver.getTitle());
    }

    @Test
    public void testSignUpLoginLogoutFlow() {
        doMockSignUp("Test", "User", "testuser", "password");
        doLogIn("testuser", "password");
        Assertions.assertEquals("Home", driver.getTitle());
        doLogOut();
        driver.get("http://localhost:" + this.port + "/home");
        Assertions.assertNotEquals("Home", driver.getTitle());
    }

    @Test
    @Tag("AddNoteUser")
    public void testCreateNoteForExistingUser() {

        // Navigate to the Notes tab
        noteObject = new NoteObject(driver);
        noteObject.navigateToNotesTab();

        // Add a new note
        noteObject.addNewNote("Test Note Title", "Test Note Description");

        // Navigate from result page to home
        resultPage = new ResultPage(driver);
        resultPage.clickSuccessHomeLink();

        // Navigate to the Notes tab
        noteObject.navigateToNotesTab();

        // Verify that the note is visible in the note list
        WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(2));
        WebElement noteTitleCell = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//th[text()='Test Note Title']")));
        WebElement noteDescriptionCell = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//td[text()='Test Note Description']")));

        Assertions.assertNotNull(noteTitleCell);
        Assertions.assertNotNull(noteDescriptionCell);
        Assertions.assertEquals("Test Note Title", noteTitleCell.getText());
        Assertions.assertEquals("Test Note Description", noteDescriptionCell.getText());
    }

    @Test
    @Tag("EditNoteUser")
    public void testEditNoteForEditNoteUser() {
        // Navigate to the Notes tab
        noteObject = new NoteObject(driver);
        noteObject.navigateToNotesTab();

        // Edit the first existing note (assuming "Note 1 Title" is the first note)
        WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(2));
        WebElement editButton = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//th[text()='Note 1 Title']/preceding-sibling::td/button[@class='btn btn-success']")));
        editButton.click();

        // Change the note data
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));
        WebElement noteTitle = driver.findElement(By.id("note-title"));
        noteTitle.clear();
        noteTitle.sendKeys("Edited Note Title");

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-description")));
        WebElement noteDescription = driver.findElement(By.id("note-description"));
        noteDescription.clear();
        noteDescription.sendKeys("Edited Note Description");

        WebElement saveChangesButton = driver.findElement(By.xpath("//button[text()='Save changes']"));
        saveChangesButton.click();

        // Navigate from result page to home
        resultPage = new ResultPage(driver);
        resultPage.clickSuccessHomeLink();

        // Navigate to the Notes tab
        noteObject.navigateToNotesTab();

        // Verify that the note is updated in the note list
        WebElement editedNoteTitleCell = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//th[text()='Edited Note Title']")));
        WebElement editedNoteDescriptionCell = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//td[text()='Edited Note Description']")));

        Assertions.assertNotNull(editedNoteTitleCell);
        Assertions.assertNotNull(editedNoteDescriptionCell);
        Assertions.assertEquals("Edited Note Title", editedNoteTitleCell.getText());
        Assertions.assertEquals("Edited Note Description", editedNoteDescriptionCell.getText());
    }

    @Test
    @Tag("DeleteNoteUser")
    public void testDeleteNote() {
        // Navigate to the Notes tab
        noteObject = new NoteObject(driver);
        noteObject.navigateToNotesTab();

        WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(2));
        WebElement deleteButton = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//th[text()='Test Note Title']/preceding-sibling::td/a[@class='btn btn-danger']")));
        deleteButton.click();

        // Navigate from result page to home
        resultPage = new ResultPage(driver);
        resultPage.clickSuccessHomeLink();

        // Navigate to the Notes tab
        noteObject.navigateToNotesTab();

        // Verify that the note no longer appears in the note list
        List<WebElement> deletedNoteTitleCells = driver.findElements(By.xpath("//th[text()='Test Note Title']"));
        Assertions.assertTrue(deletedNoteTitleCells.isEmpty());
    }


    @Test
    @Tag("ExistingCredentialsUserForAdd")
    public void testCreateCredential() {

        credentialObject = new CredentialObject(driver);
        // Navigate to the Credentials tab
        credentialObject.navigateToCredentialsTab();

        // Add a new credential
        credentialObject.addNewCredential("http://example.com", "exampleuser", "password");

        // Navigate from result page to home page
        resultPage = new ResultPage(driver);
        resultPage.clickSuccessHomeLink();

        // Navigate back to the Credentials tab
        credentialObject.navigateToCredentialsTab();

        // Verify that the credential is visible in the credential list
        WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(2));
        WebElement credentialUrlCell = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//th[text()='http://example.com']")));
        WebElement credentialUsernameCell = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//td[text()='exampleuser']")));
        WebElement passwordCell = driver.findElement(By.cssSelector("td.passwordText"));
        String passwordText = passwordCell.getText();
        System.out.println("Password Text: " + passwordText);

        Assertions.assertNotNull(credentialUrlCell);
        Assertions.assertNotNull(credentialUsernameCell);
        Assertions.assertNotNull(passwordText);
        Assertions.assertEquals("http://example.com", credentialUrlCell.getText());
        Assertions.assertEquals("exampleuser", credentialUsernameCell.getText());
    }

    @Test
    @Tag("ExistingCredentialsUser")
    public void testEditCredential() {

        credentialObject = new CredentialObject(driver);
        // Navigate to the Credentials tab
        credentialObject.navigateToCredentialsTab();

        // Get the original password text of the first credential
        WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(2));
        WebElement originalPasswordCell = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("td.passwordText")));
        String oldPasswordText = originalPasswordCell.getText();

        // Click the edit button for the first credential
        WebElement editButton = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//th[text()='http://example1.com']/preceding-sibling::td/button[@class='btn btn-success']")));
        editButton.click();

        // Change the credential data
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url")));
        WebElement credentialUrl = driver.findElement(By.id("credential-url"));
        credentialUrl.clear();

        credentialUrl.sendKeys("http://updated-example.com");
        WebElement credentialUsername = driver.findElement(By.id("credential-username"));
        credentialUsername.clear();

        credentialUsername.sendKeys("updateduser");
        WebElement credentialPassword = driver.findElement(By.id("credential-password"));
        credentialPassword.clear();

        credentialPassword.sendKeys("updatedpassword");
        // Ensure the save changes button is clickable before clicking using its ID
        WebElement saveChangesButton = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.id("saveCredentialChanges")));
        saveChangesButton.click();

        // Navigate from result page to home page
        resultPage = new ResultPage(driver);
        resultPage.clickSuccessHomeLink();

        // Navigate back to the Credentials tab
        credentialObject.navigateToCredentialsTab();

        // Verify that the updated credential is visible in the credential list
        WebElement credentialUrlCell = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//th[text()='http://updated-example.com']")));
        WebElement credentialUsernameCell = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//td[text()='updateduser']")));
        WebElement newPasswordCell = driver.findElement(By.cssSelector("td.passwordText"));
        String newPasswordText = newPasswordCell.getText();

        // Old password shouldn't be the same as the new password
        Assertions.assertNotEquals(newPasswordText, oldPasswordText);
        Assertions.assertNotNull(credentialUrlCell);
        Assertions.assertNotNull(credentialUsernameCell);
        Assertions.assertNotNull(newPasswordText);
        Assertions.assertEquals("http://updated-example.com", credentialUrlCell.getText());
        Assertions.assertEquals("updateduser", credentialUsernameCell.getText());
    }

    @Test
    @Tag("ExistingCredentialsUserForDelete")
    public void testDeleteCredential() {

        credentialObject = new CredentialObject(driver);
        // Navigate to the Credentials tab
        credentialObject.navigateToCredentialsTab();

        // Verify that the first credential is visible in the credential list
        WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(2));
        WebElement credentialUrlCell = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//th[text()='http://example1.com']")));
        WebElement credentialUsernameCell = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//td[text()='user1']")));

        Assertions.assertNotNull(credentialUrlCell);
        Assertions.assertNotNull(credentialUsernameCell);

        // Delete the first credential
        WebElement deleteButton = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//th[text()='http://example1.com']/preceding-sibling::td/a[@class='btn btn-danger']")));
        deleteButton.click();

        // Navigate from result page to home page
        resultPage = new ResultPage(driver);
        resultPage.clickSuccessHomeLink();

        // Navigate back to the Credentials tab
        credentialObject.navigateToCredentialsTab();

        // Verify that the credential no longer appears in the credential list
        List<WebElement> deletedCredentialUrlCells = driver.findElements(By.xpath("//th[text()='http://example1.com']"));
        List<WebElement> deletedCredentialUsernameCells = driver.findElements(By.xpath("//td[text()='user1']"));

        Assertions.assertTrue(deletedCredentialUrlCells.isEmpty());
        Assertions.assertTrue(deletedCredentialUsernameCells.isEmpty());
    }



}
