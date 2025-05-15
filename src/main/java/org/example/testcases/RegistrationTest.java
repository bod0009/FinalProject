package org.example.testcases;

import org.example.pages.RegistrationPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;
import java.util.Scanner;

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

        System.out.println();
        new Scanner(System.in).nextLine();

        registrationPage.clickRegister();

        String resultText = registrationPage.getRegistrationResult();
        Assert.assertTrue(resultText.contains("Your registration completed"), "Registration failed!");
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}