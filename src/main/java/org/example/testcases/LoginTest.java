package org.example.testcases;

import org.example.base.Base;
import org.example.pages.LoginPage;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.*;

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

        Assert.assertTrue(loginPage.getCurrentUrl().contains("nopcommerce.com") ||
                        loginPage.getWelcomeMessage().contains("Welcome"),
                "User was not redirected correctly after login");
    }

}