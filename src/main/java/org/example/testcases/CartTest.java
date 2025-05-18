package org.example.testcases;

import org.example.base.Base;
import org.example.pages.CartPage;
import org.example.pages.LoginPage;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;
import java.util.Scanner;

public class CartTest extends Base {
    WebDriver driver;
    private SoftAssert soft;
    private LoginPage loginPage;
    private CartPage cartPage;
    final String BASE_URL = "https://demo.nopcommerce.com";
    final String VALID_EMAIL = "test@example.com";
    final String VALID_PASSWORD = "Test@123";
    final String PRODUCT1 = "Laptop";
    final String PRODUCT2 = "Mouse";

    @BeforeMethod
    public void setup() {
        driver = openBrowser();
        soft = new SoftAssert();
        loginPage = new LoginPage(driver);
        cartPage = new CartPage(driver);
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

    @Test(description = "Add product to cart")
    public void testAddProductToCart() {
//        System.out.println("=== قم بتسجيل الدخول يدوياً ثم اضغط Enter في الكونسول للمتابعة ===");
//        new Scanner(System.in).nextLine();
        cartPage.searchForProduct(PRODUCT1);
        cartPage.selectFirstProduct();
        cartPage.addToCart();
        soft.assertTrue(cartPage.isConfirmationMessageDisplayed(), "Confirmation not shown");
    }

    @Test(description = "View cart with multiple products")
    public void testViewCartWithMultipleProducts() {
        // Add first product
        cartPage.searchForProduct(PRODUCT1);
        cartPage.selectFirstProduct();
        cartPage.addToCart();

        // Add second product
        cartPage.searchForProduct(PRODUCT2);
        cartPage.selectFirstProduct();
        cartPage.addToCart();

        cartPage.navigateToCart();
        soft.assertEquals(cartPage.getCartItemCount(), 2, "Items count mismatch");
    }

    @Test(description = "Remove product from cart")
    public void testRemoveProductFromCart() {
        cartPage.searchForProduct(PRODUCT2);
        cartPage.selectFirstProduct();
        cartPage.addToCart();
        cartPage.navigateToCart();

        int initialCount = cartPage.getCartItemCount();
        cartPage.removeFirstItem();
        int newCount = cartPage.getCartItemCount();

        soft.assertTrue(newCount < initialCount, "Item not removed");
    }

    @Test(description = "Increase product quantity in cart")
    public void testIncreaseProductQuantityInCart() {
        cartPage.searchForProduct(PRODUCT1);
        cartPage.selectFirstProduct();
        cartPage.addToCart();
        cartPage.navigateToCart();

        String oldQty = cartPage.getCurrentQuantity();
        cartPage.increaseQuantity();
        String newQty = cartPage.getCurrentQuantity();

        soft.assertNotEquals(newQty, oldQty, "Quantity not changed");
    }

    @Test(description = "Proceed to checkout with items in cart")
    public void testProceedToCheckoutWithFilledCart() {
        cartPage.searchForProduct(PRODUCT1);
        cartPage.selectFirstProduct();
        cartPage.addToCart();
        cartPage.navigateToCart();
        cartPage.proceedToCheckout();

        soft.assertTrue(cartPage.isCheckoutPageDisplayed(), "Checkout page not opened");
    }

    @Test(description = "Try checkout with empty cart")
    public void testCheckoutWithEmptyCart() {
        cartPage.navigateToCart();
        cartPage.emptyCart();
        cartPage.proceedToCheckout();

        soft.assertTrue(cartPage.isEmptyCartMessageDisplayed(), "Empty cart message missing");
    }
}