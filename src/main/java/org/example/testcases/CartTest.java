package org.example.testcases;

import org.example.base.Base;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;
import java.util.List;

public class CartTest extends Base {
    WebDriver driver;
    SoftAssert soft;

    final String BASE_URL = "https://demo.nopcommerce.com";
    final String VALID_EMAIL = "test@example.com";
    final String VALID_PASSWORD = "Test@123";
    final String PRODUCT1 = "Laptop";
    final String PRODUCT2 = "Mouse";

    @BeforeMethod
    public void setup() {
        driver = openBrowser();
        soft = new SoftAssert();
        loginToApplication();
    }

    @AfterMethod
    public void tearDown() {
        soft.assertAll();
        driver.quit();
    }

    private void loginToApplication() {
        driver.get(BASE_URL + "/login");
        driver.findElement(By.id("Email")).sendKeys(VALID_EMAIL);
        driver.findElement(By.id("Password")).sendKeys(VALID_PASSWORD);
        driver.findElement(By.cssSelector("button.login-button")).click();
    }

    @Test(description = "Add product to cart")
    public void testAddProductToCart() {
        addProductToCart(PRODUCT1);
        WebElement confirmation = driver.findElement(By.cssSelector(".content"));
        soft.assertTrue(confirmation.getText().contains("added to your"), "Confirmation not shown");
    }

    @Test(description = "View cart with multiple products")
    public void testViewCartWithMultipleProducts() {
        addProductToCart(PRODUCT1);
        addProductToCart(PRODUCT2);
        driver.findElement(By.cssSelector(".ico-cart")).click();

        List<WebElement> cartItems = driver.findElements(By.cssSelector(".cart-item-row"));
        soft.assertEquals(cartItems.size(), 2, "Items count mismatch");
    }

    @Test(description = "Remove product from cart")
    public void testRemoveProductFromCart() {
        addProductToCart(PRODUCT2);
        driver.findElement(By.cssSelector(".ico-cart")).click();

        int initialCount = driver.findElements(By.cssSelector(".cart-item-row")).size();
        driver.findElement(By.cssSelector(".remove-btn")).click();
        waitShort();

        int newCount = driver.findElements(By.cssSelector(".cart-item-row")).size();
        soft.assertTrue(newCount < initialCount, "Item not removed");
    }

    @Test(description = "Increase product quantity in cart")
    public void testIncreaseProductQuantityInCart() {
        addProductToCart(PRODUCT1);
        driver.findElement(By.cssSelector(".ico-cart")).click();

        WebElement quantity = driver.findElement(By.cssSelector(".qty-input"));
        String oldQty = quantity.getAttribute("value");
        driver.findElement(By.cssSelector(".increase-qty")).click();
        waitShort();

        String newQty = quantity.getAttribute("value");
        soft.assertNotEquals(newQty, oldQty, "Quantity not changed");
    }

    @Test(description = "Proceed to checkout with items in cart")
    public void testProceedToCheckoutWithFilledCart() {
        addProductToCart(PRODUCT1);
        driver.findElement(By.cssSelector(".ico-cart")).click();
        driver.findElement(By.id("checkout")).click();

        soft.assertTrue(driver.getCurrentUrl().contains("checkout"), "Checkout page not opened");
    }

    @Test(description = "Try checkout with empty cart")
    public void testCheckoutWithEmptyCart() {
        driver.findElement(By.cssSelector(".ico-cart")).click();
        List<WebElement> removeButtons = driver.findElements(By.cssSelector(".remove-btn"));
        for (WebElement button : removeButtons) {
            button.click();
            waitShort();
        }

        driver.findElement(By.id("checkout")).click();
        WebElement emptyCartMessage = driver.findElement(By.cssSelector(".no-data"));
        soft.assertTrue(emptyCartMessage.getText().contains("empty"), "Empty cart message missing");
    }

    private void addProductToCart(String productName) {
        WebElement searchBox = driver.findElement(By.id("small-searchterms"));
        searchBox.clear();
        searchBox.sendKeys(productName);
        driver.findElement(By.cssSelector(".search-box-button")).click();
        driver.findElement(By.cssSelector(".product-title a")).click();
        driver.findElement(By.id("add-to-cart-button")).click();
        waitShort();
    }

    private void waitShort() {
        try { Thread.sleep(1000); } catch (InterruptedException e) {}
    }
}
