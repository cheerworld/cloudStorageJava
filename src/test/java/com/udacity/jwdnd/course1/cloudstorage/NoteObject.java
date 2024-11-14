package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class NoteObject {

    private WebDriver driver;
    private WebDriverWait webDriverWait;

    @FindBy(id = "nav-notes-tab")
    private WebElement notesTab;

    @FindBy(css = "button.btn.btn-info")
    private WebElement addNoteButton;

    @FindBy(id = "note-title")
    private WebElement noteTitle;

    @FindBy(id = "note-description")
    private WebElement noteDescription;

    @FindBy(xpath = "//button[text()='Save changes']")
    private WebElement saveChangesButton;

    @FindBy(css = "a.btn.btn-danger")
    private WebElement deleteNoteButton;

    @FindBy(css = "button.btn.btn-success")
    private WebElement editNoteButton;

    public NoteObject(WebDriver webDriver) {
        this.driver = webDriver;
        this.webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(2));
        PageFactory.initElements(webDriver, this);
    }

    // Method to navigate to the Notes tab
    public void navigateToNotesTab() {
        webDriverWait.until(ExpectedConditions.elementToBeClickable(notesTab));
        notesTab.click();
    }

    // Method to add a new note
    public void addNewNote(String title, String description) {
        webDriverWait.until(ExpectedConditions.elementToBeClickable(addNoteButton));
        addNoteButton.click();

        webDriverWait.until(ExpectedConditions.visibilityOf(noteTitle));
        noteTitle.click();
        noteTitle.sendKeys(title);

        webDriverWait.until(ExpectedConditions.visibilityOf(noteDescription));
        noteDescription.click();
        noteDescription.sendKeys(description);

        saveChangesButton.click();
    }

    // Method to edit an existing note
    public void editNote(String newTitle, String newDescription) {
        webDriverWait.until(ExpectedConditions.elementToBeClickable(editNoteButton));
        editNoteButton.click();

        webDriverWait.until(ExpectedConditions.visibilityOf(noteTitle));
        noteTitle.clear();
        noteTitle.sendKeys(newTitle);

        webDriverWait.until(ExpectedConditions.visibilityOf(noteDescription));
        noteDescription.clear();
        noteDescription.sendKeys(newDescription);

        saveChangesButton.click();
    }


    // Method to delete an existing note
    public void deleteNote() {
        webDriverWait.until(ExpectedConditions.visibilityOf(deleteNoteButton));
        deleteNoteButton.click();
    }
}
