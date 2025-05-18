package org.example.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class SearchPage {
    private WebDriver driver;
    private WebDriverWait wait;

    @FindBy(id = "small-searchterms")
    private WebElement searchBox;

    @FindBy(css = ".search-box-button")
    private WebElement searchButton;

    @FindBy(css = ".product-grid")
    private WebElement productGrid;

    @FindBy(css = ".no-result")
    private WebElement noResultMessage;

    @FindBy(linkText = "Electronics")
    private WebElement electronicsCategory;

    @FindBy(id = "products-orderby")
    private WebElement sortDropdown;

    @FindBy(css = ".actual-price")
    private List<WebElement> productPrices;

    public SearchPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    public void searchForProduct(String productName) {
        searchBox.clear();
        searchBox.sendKeys(productName);
        searchButton.click();
    }

    public boolean isProductGridDisplayed() {
        return wait.until(ExpectedConditions.visibilityOf(productGrid)).isDisplayed();
    }

    public boolean isNoResultMessageDisplayed() {
        return wait.until(ExpectedConditions.visibilityOf(noResultMessage)).isDisplayed();
    }

    public String getNoResultMessageText() {
        return noResultMessage.getText();
    }

    public void selectElectronicsCategory() {
        electronicsCategory.click();
    }

    public void sortByPriceLowToHigh() {
        sortDropdown.sendKeys("Price: Low to High");
    }

    public List<Double> getAllProductPrices() {
        return productPrices.stream()
                .map(price -> Double.parseDouble(price.getText().replace("$", "")))
                .toList();
    }

    public void waitForResultsReload() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}