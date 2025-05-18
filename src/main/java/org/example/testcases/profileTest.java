package org.example.testcases;

import org.example.base.Base;
import org.example.pages.LoginPage;
import org.example.pages.profilePage;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

public class profileTest extends Base {
    private WebDriver driver;
    private SoftAssert soft;
    private LoginPage loginPage;
    private profilePage profilePage;

    final String BASE_URL = "https://demo.nopcommerce.com";
    final String VALID_EMAIL = "test@example.com";
    final String VALID_PASSWORD = "Test@123";
    final String NEW_FIRST_NAME = "newname";

    @BeforeMethod
    public void setup() {
        driver = openBrowser();
        soft = new SoftAssert();
        loginPage = new LoginPage(driver);
        profilePage = new profilePage(driver);
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

    @Test(priority = 1, description = "TC_POST_001 - View user profile information")
    public void testViewUserProfileInformation() {
        profilePage.navigateToProfile();
        soft.assertTrue(profilePage.isProfileInfoDisplayed(), "User profile info not displayed");
    }

    @Test(priority = 2, description = "TC_POST_002 - Edit user profile information")
    public void testEditUserProfileInformation() {
        profilePage.navigateToProfile();
        profilePage.clickProfileLink();
        profilePage.updateFirstName(NEW_FIRST_NAME);
        profilePage.saveProfileChanges();

        soft.assertTrue(profilePage.isSuccessMessageDisplayed(), "Profile update message not shown");
        soft.assertEquals(profilePage.getFirstNameValue(), NEW_FIRST_NAME, "First name not updated");
    }

    @Test(priority = 12, description = "TC_POST_012 - Logout from website")
    public void testLogout() {
        profilePage.logout();
        soft.assertTrue(profilePage.isLoginLinkDisplayed(), "Login link not visible after logout");
    }
}