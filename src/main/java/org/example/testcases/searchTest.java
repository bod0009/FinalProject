package org.example.testcases;

import org.example.base.Base;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

public class searchTest extends Base {
    WebDriver driver;
    SoftAssert soft;

    final String BASE_URL = "https://demo.nopcommerce.com";
    final String VALID_EMAIL = "test@example.com";
    final String VALID_PASSWORD = "Test@123";
    final String SEARCH_KEYWORD_VALID = "Headphones";
    final String SEARCH_KEYWORD_INVALID = "XYZabc";
    final String CATEGORY = "Electronics";

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

    @Test(priority = 1, description = "TC_POST_003 - Search for a valid product")
    public void testSearchValidProduct() {
        // Search for valid product
        WebElement searchBox = driver.findElement(By.id("small-searchterms"));
        searchBox.sendKeys(SEARCH_KEYWORD_VALID);
        driver.findElement(By.cssSelector(".search-box-button")).click();

        // Verify results
        WebElement resultBlock = driver.findElement(By.cssSelector(".product-grid"));
        soft.assertTrue(resultBlock.isDisplayed(), "Search results not displayed");
        soft.assertTrue(driver.getPageSource().contains(SEARCH_KEYWORD_VALID), 
            "Expected product not found in results");
    }

    @Test(priority = 2, description = "TC_POST_004 - Search for an invalid product")
    public void testSearchInvalidProduct() {
        // Search for invalid product
        WebElement searchBox = driver.findElement(By.id("small-searchterms"));
        searchBox.sendKeys(SEARCH_KEYWORD_INVALID);
        driver.findElement(By.cssSelector(".search-box-button")).click();

        // Verify no results message
        WebElement noResultMessage = driver.findElement(By.cssSelector(".no-result"));
        soft.assertTrue(noResultMessage.isDisplayed(), "No result message not shown");
        soft.assertTrue(noResultMessage.getText().contains("No products were found"), 
            "Incorrect message for invalid search");
    }

    @Test(priority = 3, description = "TC_POST_005 - Filter products by category")
    public void testFilterProductsByCategory() {
        // Navigate to Electronics category
        driver.findElement(By.linkText(CATEGORY)).click();
        
        // Apply price filter
        WebElement sortDropdown = driver.findElement(By.id("products-orderby"));
        sortDropdown.sendKeys("Price: Low to High");
        
        // Wait for results to reload
        try { Thread.sleep(2000); } catch (InterruptedException e) {}
        
        // Verify sorting
        java.util.List<WebElement> prices = driver.findElements(By.cssSelector(".actual-price"));
        for (int i = 0; i < prices.size() - 1; i++) {
            double price1 = Double.parseDouble(prices.get(i).getText().replace("$", ""));
            double price2 = Double.parseDouble(prices.get(i+1).getText().replace("$", ""));
            soft.assertTrue(price1 <= price2, "Products not sorted by price low to high");
        }
    }
}