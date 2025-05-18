package org.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class CartPage {
    private WebDriver driver;
    private WebDriverWait wait;

    // Locators
    private By searchBox = By.id("small-searchterms");
    private By searchButton = By.cssSelector(".search-box-button");
    private By productTitle = By.cssSelector(".product-title a");
    private By addToCartButton = By.id("add-to-cart-button");
    private By confirmationMessage = By.cssSelector(".content");
    private By cartIcon = By.cssSelector(".ico-cart");
    private By cartItems = By.cssSelector(".cart-item-row");
    private By removeButton = By.cssSelector(".remove-btn");
    private By quantityInput = By.cssSelector(".qty-input");
    private By increaseQtyButton = By.cssSelector(".increase-qty");
    private By checkoutButton = By.id("checkout");
    private By emptyCartMessage = By.cssSelector(".no-data");


    public void slowType(WebElement element, String text, int delayInMillis) {
        for (char c : text.toCharArray()) {
            element.sendKeys(String.valueOf(c));
            try {
                Thread.sleep(delayInMillis); // delay بين كل حرف
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public CartPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void searchForProduct(String productName) {
        driver.findElement(searchBox).clear();
        driver.findElement(searchBox).sendKeys(productName);
        driver.findElement(searchButton).click();

    }

    public void selectFirstProduct() {
        driver.findElement(productTitle).click();
    }

    public void addToCart() {
        driver.findElement(addToCartButton).click();
    }

    public boolean isConfirmationMessageDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(confirmationMessage))
                .getText().contains("added to your");
    }

    public void navigateToCart() {
        driver.findElement(cartIcon).click();
    }

    public int getCartItemCount() {
        return driver.findElements(cartItems).size();
    }

    public void removeFirstItem() {
        driver.findElement(removeButton).click();
    }

    public String getCurrentQuantity() {
        return driver.findElement(quantityInput).getAttribute("value");
    }

    public void increaseQuantity() {
        driver.findElement(increaseQtyButton).click();
    }

    public void proceedToCheckout() {
        driver.findElement(checkoutButton).click();
    }

    public boolean isCheckoutPageDisplayed() {
        return driver.getCurrentUrl().contains("checkout");
    }

    public void emptyCart() {
        List<WebElement> removeButtons = driver.findElements(removeButton);
        for (WebElement button : removeButtons) {
            button.click();
            waitShort();
        }
    }

    public boolean isEmptyCartMessageDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(emptyCartMessage))
                .getText().contains("empty");
    }

    private void waitShort() {
        try { Thread.sleep(1000); } catch (InterruptedException e) {}
    }
}