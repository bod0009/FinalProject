package org.example.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPage {
    private WebDriver driver;
    private WebDriverWait wait;

    @FindBy(id = "Email")
    private WebElement emailField;

    @FindBy(id = "Password")
    private WebElement passwordField;

    @FindBy(css = ".button-1.login-button")
    private WebElement loginButton;

    @FindBy(linkText = "Forgot password?")
    private WebElement forgotPasswordLink;

    @FindBy(id = "RememberMe")
    private WebElement rememberMeCheckbox;

    @FindBy(css = ".message-error.validation-summary-errors")
    private WebElement errorMessage;

    @FindBy(css = "span.field-validation-error")
    private WebElement fieldValidationError;

    @FindBy(css = "div.topic-block-title h2")
    private WebElement welcomeMessage;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    public void navigateToLoginPage(String url) {
        driver.get(url);
    }

    public void enterEmail(String email) {
        emailField.clear();
        emailField.sendKeys(email);
    }

    public void enterPassword(String password) {
        passwordField.clear();
        passwordField.sendKeys(password);
    }

    public void clickLoginButton() {
        loginButton.click();
    }

    public void clickForgotPasswordLink() {
        forgotPasswordLink.click();
    }

    public void checkRememberMe() {
        if (!rememberMeCheckbox.isSelected()) {
            rememberMeCheckbox.click();
        }
    }

    public String getErrorMessage() {
        try {
            return wait.until(ExpectedConditions.visibilityOf(errorMessage)).getText();
        } catch (Exception e) {
            return wait.until(ExpectedConditions.visibilityOf(fieldValidationError)).getText();
        }
    }

    public boolean isLoginButtonEnabled() {
        return loginButton.isEnabled();
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public String getWelcomeMessage() {
        return wait.until(ExpectedConditions.visibilityOf(welcomeMessage)).getText();
    }
}