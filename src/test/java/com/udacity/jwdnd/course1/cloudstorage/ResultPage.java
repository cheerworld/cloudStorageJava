package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class ResultPage {

    @FindBy(id = "successMsg")
    private WebElement successMsg;

    @FindBy(id = "errorMsg")
    private WebElement errorMsg;

    @FindBy(id = "successClickHere")
    private WebElement successHomeLink;

    @FindBy(id = "errorClickHere")
    private WebElement errorHomeLink;

    public ResultPage(WebDriver webDriver) {
        PageFactory.initElements(webDriver, this);
    }

    public boolean isSuccess() {
        return successMsg != null && successMsg.isDisplayed();
    }

    public boolean isError() {
        return errorMsg != null && errorMsg.isDisplayed();
    }

    public void clickSuccessHomeLink() {
        if (successHomeLink != null) {
            successHomeLink.click();
        }
    }

    public void clickErrorHomeLink() {
        if (errorHomeLink != null) {
            errorHomeLink.click();
        }
    }
}

