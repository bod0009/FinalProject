package org.example.testcases;

import org.example.pages.RegistrationPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;

public class RegistrationTest {
    private WebDriver driver;
    private RegistrationPage registrationPage;

    @BeforeClass
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("user-data-dir=C:/chrome-automation-profile");

        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().window().maximize();

        driver.get("https://demo.nopcommerce.com/register");
        registrationPage = new RegistrationPage(driver);
    }

    @Test
    public void validRegistrationTest() {
        registrationPage.selectGender("male");
        registrationPage.enterFirstName("Ahmed");
        registrationPage.enterLastName("Hassan");
        registrationPage.enterEmail("ahmed" + System.currentTimeMillis() + "@test.com");
        registrationPage.enterPassword("Test1234");
        registrationPage.enterConfirmPassword("Test1234");

        registrationPage.clickRegister();
        String resultText = registrationPage.getRegistrationResult();
        Assert.assertTrue(resultText.contains("Your registration completed"), "Registration failed!");
    }

    @Test
    public void passwordMismatchTest() {
        registrationPage.selectGender("male");
        registrationPage.enterFirstName("Ahmed");
        registrationPage.enterLastName("Hassan");
        registrationPage.enterEmail("ahmed" + System.currentTimeMillis() + "@test.com");
        registrationPage.enterPassword("Test1234");
        registrationPage.enterConfirmPassword("DifferentPass");

        registrationPage.clickRegister();
        String resultText = registrationPage.getRegistrationResult();
        Assert.assertTrue(resultText.contains("The password and confirmation password do not match."), "Mismatch error not shown!");
    }

    @Test
    public void invalidEmailTest() {
        registrationPage.selectGender("male");
        registrationPage.enterFirstName("Ahmed");
        registrationPage.enterLastName("Hassan");
        registrationPage.enterEmail("invalid-email");
        registrationPage.enterPassword("Test1234");
        registrationPage.enterConfirmPassword("Test1234");

        registrationPage.clickRegister();
        WebElement errorElement = driver.findElement(By.id("Email-error"));
        Assert.assertTrue(errorElement.getText().contains("Wrong email"), "Invalid email error not shown!");
    }

    @Test
    public void missingFirstNameTest() {
        registrationPage.selectGender("male");
        registrationPage.enterFirstName("");
        registrationPage.enterLastName("Hassan");
        registrationPage.enterEmail("ahmed" + System.currentTimeMillis() + "@test.com");
        registrationPage.enterPassword("Test1234");
        registrationPage.enterConfirmPassword("Test1234");

        registrationPage.clickRegister();
        WebElement errorElement = driver.findElement(By.id("FirstName-error"));
        Assert.assertTrue(errorElement.getText().contains("First name is required"), "First name required error not shown!");
    }

    @Test
    public void missingPasswordTest() {
        registrationPage.selectGender("male");
        registrationPage.enterFirstName("Ahmed");
        registrationPage.enterLastName("Hassan");
        registrationPage.enterEmail("ahmed" + System.currentTimeMillis() + "@test.com");
        registrationPage.enterPassword("");
        registrationPage.enterConfirmPassword("");

        registrationPage.clickRegister();
        WebElement errorElement = driver.findElement(By.id("Password-error"));
        Assert.assertTrue(errorElement.getText().contains("Password is required"), "Password required error not shown!");
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
