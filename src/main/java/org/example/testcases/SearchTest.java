package org.example.testcases;

import org.example.base.Base;
import org.example.pages.LoginPage;
import org.example.pages.SearchPage;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

import java.util.List;

public class SearchTest extends Base {
    private WebDriver driver;
    private SoftAssert soft;
    private LoginPage loginPage;
    private SearchPage searchPage;

    private static final String BASE_URL = "https://demo.nopcommerce.com";
    private static final String VALID_EMAIL = "test@example.com";
    private static final String VALID_PASSWORD = "Test@123";
    private static final String SEARCH_KEYWORD_VALID = "Headphones";
    private static final String SEARCH_KEYWORD_INVALID = "XYZabc";
    private static final String CATEGORY = "Electronics";

    @BeforeMethod
    public void setup() {
        driver = openBrowser();
        soft = new SoftAssert();
        loginPage = new LoginPage(driver);
        searchPage = new SearchPage(driver);
        loginToApplication();
    }

    @AfterMethod
    public void tearDown() {
        soft.assertAll();
        driver.quit();
    }

    private void loginToApplication() {
        driver.get(BASE_URL + "/login");
        loginPage.enterEmail(VALID_EMAIL);
        loginPage.enterPassword(VALID_PASSWORD);
        loginPage.clickLoginButton();
    }

    @Test(priority = 1, description = "TC_POST_003 - Search for a valid product")
    public void testSearchValidProduct() {
        searchPage.searchForProduct(SEARCH_KEYWORD_VALID);

        soft.assertTrue(searchPage.isProductGridDisplayed(), "Search results not displayed");
        soft.assertTrue(driver.getPageSource().contains(SEARCH_KEYWORD_VALID),
                "Expected product not found in results");
    }

    @Test(priority = 2, description = "TC_POST_004 - Search for an invalid product")
    public void testSearchInvalidProduct() {
        searchPage.searchForProduct(SEARCH_KEYWORD_INVALID);

        soft.assertTrue(searchPage.isNoResultMessageDisplayed(), "No result message not shown");
        soft.assertTrue(searchPage.getNoResultMessageText().contains("No products were found"),
                "Incorrect message for invalid search");
    }

    @Test(priority = 3, description = "TC_POST_005 - Filter products by category and price")
    public void testFilterProductsByCategory() {
        searchPage.selectElectronicsCategory();
        searchPage.sortByPriceLowToHigh();
        searchPage.waitForResultsReload();

        List<Double> prices = searchPage.getAllProductPrices();
        for (int i = 0; i < prices.size() - 1; i++) {
            soft.assertTrue(prices.get(i) <= prices.get(i + 1),
                    "Products not sorted by price low to high. " +
                            prices.get(i) + " is not <= " + prices.get(i + 1));
        }
    }
}