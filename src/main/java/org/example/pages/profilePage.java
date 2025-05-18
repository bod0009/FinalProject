package org.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class profilePage {
    private WebDriver driver;
    private WebDriverWait wait;

    // Locators
    private By accountIcon = By.cssSelector(".ico-account");
    private By customerInfo = By.cssSelector(".customer-info");
    private By profileLink = By.linkText("Profile");
    private By firstNameField = By.id("FirstName");
    private By saveButton = By.cssSelector(".save-customer-info-button");
    private By successMessage = By.cssSelector(".content");
    private By logoutLink = By.cssSelector(".ico-logout");
    private By loginLink = By.cssSelector(".ico-login");

    public profilePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void navigateToProfile() {
        driver.findElement(accountIcon).click();
    }

    public boolean isProfileInfoDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(customerInfo)).isDisplayed();
    }

    public void clickProfileLink() {
        driver.findElement(profileLink).click();
    }

    public void updateFirstName(String newName) {
        WebElement firstName = driver.findElement(firstNameField);
        firstName.clear();
        firstName.sendKeys(newName);
    }

    public void saveProfileChanges() {
        driver.findElement(saveButton).click();
    }

    public boolean isSuccessMessageDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(successMessage))
                .getText().contains("updated");
    }

    public String getFirstNameValue() {
        return driver.findElement(firstNameField).getAttribute("value");
    }

    public void logout() {
        driver.findElement(logoutLink).click();
    }

    public boolean isLoginLinkDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(loginLink)).isDisplayed();
    }
}