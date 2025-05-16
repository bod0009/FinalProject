package org.example.testcases;

import org.example.base.Base;
import org.example.pages.LoginPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

public class LoginTest extends Base {
    WebDriver driver;
    LoginPage loginPage;

    final String BASE_URL = "https://demo.nopcommerce.com/login";
    final String VALID_EMAIL = "test@example.com";
    final String VALID_PASSWORD = "Test@123";
    final String INVALID_PASSWORD = "wrongpass";

    @BeforeMethod
    public void setup() {
        driver = openBrowser();
        loginPage = new LoginPage(driver);
    }

    @AfterMethod
    public void tearDown() {
        driver.quit();
    }

    @Test
    public void testRedirectAfterLogin() {
        loginPage.navigateToLoginPage(BASE_URL);
        loginPage.enterEmail(VALID_EMAIL);
        loginPage.enterPassword(VALID_PASSWORD);
        loginPage.clickLoginButton();

        SoftAssert soft = new SoftAssert();

        WebElement accountIcon = driver.findElement(By.cssSelector(".ico-account"));
        soft.assertTrue(accountIcon.isDisplayed(), "Account icon not displayed - login may have failed");

        soft.assertTrue(driver.getPageSource().contains("Welcome to our store"), "Welcome message not found");

        soft.assertAll();
    }
}
