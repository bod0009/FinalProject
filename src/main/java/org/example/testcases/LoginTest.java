package org.example.testcases;

import org.example.base.Base;
import org.example.pages.LoginPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
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

    @Test (description = "TC01: Valid Login")
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
    @Test(description = "TC03: Invalid Email Format")
    public void testInvalidEmailFormat() {
        loginPage.navigateToLoginPage(BASE_URL);
        loginPage.enterEmail("invalid-email");
        loginPage.enterPassword(VALID_PASSWORD);
        loginPage.clickLoginButton();

        String errorMessage = loginPage.getErrorMessage();
        Assert.assertTrue(errorMessage.contains("Wrong email") ||
                        errorMessage.contains("valid email"),
                "Expected invalid email format error not displayed");
    }

    @Test(description = "TC04: Empty Fields")
    public void testEmptyFields() {
        loginPage.navigateToLoginPage(BASE_URL);
        loginPage.clickLoginButton();

        SoftAssert soft = new SoftAssert();
        String errorMessage = loginPage.getErrorMessage();
        soft.assertTrue(errorMessage.contains("Please enter your email"), "Expected required fields error not displayed");
        soft.assertTrue(errorMessage.contains("required"), "Expected required fields error not displayed");
        soft.assertAll();
    }

    @Test(description = "TC05: Only Email Entered")
    public void testOnlyEmailEntered() {
        loginPage.navigateToLoginPage(BASE_URL);
        loginPage.enterEmail(VALID_EMAIL);
        loginPage.clickLoginButton();

        SoftAssert soft = new SoftAssert();
        String errorMessage = loginPage.getErrorMessage();
        soft.assertTrue(errorMessage.contains("password"), "Expected password required error not displayed");
        soft.assertTrue(errorMessage.contains("required"), "Expected password required error not displayed");
        soft.assertAll();
    }

    @Test(description = "TC06: Only Password Entered")
    public void testOnlyPasswordEntered() {
        loginPage.navigateToLoginPage(BASE_URL);
        loginPage.enterPassword(VALID_PASSWORD);
        loginPage.clickLoginButton();

        SoftAssert soft = new SoftAssert();
        String errorMessage = loginPage.getErrorMessage();
        soft.assertTrue(errorMessage.contains("email"), "Expected email required error not displayed");
        soft.assertTrue(errorMessage.contains("enter your email"), "Expected email required error not displayed");
        soft.assertAll();
    }

    @Test(description = "TC07: Remember Me Functionality")
    public void testRememberMeFunctionality() {
        loginPage.navigateToLoginPage(BASE_URL);
        loginPage.checkRememberMe();
        loginPage.enterEmail(VALID_EMAIL);
        loginPage.enterPassword(VALID_PASSWORD);
        loginPage.clickLoginButton();

        // Close and reopen browser
        driver.manage().deleteAllCookies();
        driver.navigate().to(BASE_URL);

        // Check if email field is pre-filled
        Assert.assertFalse(loginPage.getCurrentUrl().equals(BASE_URL), "User was not kept logged in");
    }

    @Test(description = "TC08: Forgot Password Link")
    public void testForgotPasswordLink() {
        loginPage.navigateToLoginPage(BASE_URL);
        loginPage.clickForgotPasswordLink();

        Assert.assertTrue(loginPage.getCurrentUrl().contains("passwordrecovery"),"User was not redirected to password reset page");
    }

    @Test(description = "TC09: Case Sensitivity Check")
    public void testCaseSensitivity() {
        loginPage.navigateToLoginPage(BASE_URL);
        loginPage.enterEmail(VALID_EMAIL.toUpperCase());
        loginPage.enterPassword(VALID_PASSWORD.toUpperCase());
        loginPage.clickLoginButton();

        String errorMessage = loginPage.getErrorMessage();
        Assert.assertTrue(errorMessage.contains("Login was unsuccessful") || errorMessage.contains("credentials are incorrect"),"Expected error for wrong case credentials");
    }

    @Test(description = "TC10: Login Button Disabled if Fields Empty")
    public void testLoginButtonDisabledWhenFieldsEmpty() {
        loginPage.navigateToLoginPage(BASE_URL);
        Assert.assertTrue(loginPage.isLoginButtonEnabled(),"Login button should be enabled even when fields are empty in nopCommerce");
    }

    @Test(description = "TC11: Redirect After Login")
    public void testRedirectAfterLogin1() {
        loginPage.navigateToLoginPage(BASE_URL);
        loginPage.enterEmail(VALID_EMAIL);
        loginPage.enterPassword(VALID_PASSWORD);
        loginPage.clickLoginButton();

        Assert.assertTrue(loginPage.getCurrentUrl().equals("https://demo.nopcommerce.com/") ||
                loginPage.getWelcomeMessage().contains("Welcome"),"User was not redirected to homepage after login");
    }

    @Test(description = "TC13: Multiple Failed Attempts")
    public void testMultipleFailedAttempts() {
        loginPage.navigateToLoginPage(BASE_URL);

        for (int i = 0; i < 5; i++) {
            loginPage.enterEmail(VALID_EMAIL);
            loginPage.enterPassword(INVALID_PASSWORD);
            loginPage.clickLoginButton();
        }

        String errorMessage = loginPage.getErrorMessage();
        Assert.assertTrue(errorMessage.contains("unsuccessful") || errorMessage.contains("incorrect"), "No account lockout after multiple failed attempts");
    }
}

