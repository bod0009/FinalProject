package org.example.testcases;

import org.example.base.Base;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

public class profileTest extends Base {
    WebDriver driver;
    SoftAssert soft;

    final String BASE_URL = "https://demo.nopcommerce.com";
    final String VALID_EMAIL = "test@example.com";
    final String VALID_PASSWORD = "Test@123";
    final String NEW_FIRST_NAME = "newname";
    final String PRODUCT_TO_ADD = "Wireless Mouse";

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

    @Test(priority = 1, description = "TC_POST_001 - View user profile information")
    public void testViewUserProfileInformation() {
        driver.findElement(By.cssSelector(".ico-account")).click();
        WebElement accountDetails = driver.findElement(By.cssSelector(".customer-info"));
        soft.assertTrue(accountDetails.isDisplayed(), "User profile info not displayed");
    }

    @Test(priority = 2, description = "TC_POST_002 - Edit user profile information")
    public void testEditUserProfileInformation() {
        driver.findElement(By.cssSelector(".ico-account")).click();
        driver.findElement(By.linkText("Profile")).click();

        WebElement firstName = driver.findElement(By.id("FirstName"));
        firstName.clear();
        firstName.sendKeys(NEW_FIRST_NAME);

        driver.findElement(By.cssSelector(".save-customer-info-button")).click();

        WebElement successMessage = driver.findElement(By.cssSelector(".content"));
        soft.assertTrue(successMessage.getText().contains("updated"), "Profile update message not shown");
        soft.assertTrue(driver.findElement(By.id("FirstName")).getAttribute("value").equals(NEW_FIRST_NAME),
                "First name not updated");
    }

    @Test(priority = 6, description = "TC_POST_006 - Add product to cart from product details page")
    public void testAddProductToCart() {
        searchForProduct(PRODUCT_TO_ADD);
        driver.findElement(By.cssSelector(".product-title a")).click();
        driver.findElement(By.id("add-to-cart-button")).click();

        WebElement confirmation = driver.findElement(By.cssSelector(".content"));
        soft.assertTrue(confirmation.getText().contains("added to your"), "Add to cart confirmation not shown");
    }

    @Test(priority = 12, description = "TC_POST_012 - Logout from website")
    public void testLogout() {
        driver.findElement(By.cssSelector(".ico-logout")).click();
        soft.assertTrue(driver.findElement(By.cssSelector(".ico-login")).isDisplayed(),
                "Login link not visible after logout");
    }

    // Helper methods
    private void searchForProduct(String productName) {
        WebElement searchBox = driver.findElement(By.id("small-searchterms"));
        searchBox.clear();
        searchBox.sendKeys(productName);
        driver.findElement(By.cssSelector(".search-box-button")).click();
    }
}
