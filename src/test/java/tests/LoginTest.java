package tests;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import base.BaseTest;

import java.util.concurrent.TimeoutException;

import pages.*;

// Sai Bharath Kotha is a good boy

import utils.TestUtil;

public class LoginTest extends BaseTest{
	TestngAllPages loginPage;
    TestUtil util;

    @BeforeClass
    public void setupTest() {
        // Use driver from BaseTest (already initialized there)
        loginPage = new TestngAllPages(driver);
        util = new TestUtil(driver, java.time.Duration.ofSeconds(10));
    }

    @Test(priority = 0)
    public void verifyCorrectUrlOpened() {
        Assert.assertTrue(loginPage.isCorrectUrl(), "Incorrect URL opened!");
    }



    @Test(priority = 1)
    public void verifyLogin() {
        loginPage.clickSignIn();
        loginPage.selectUsername("demouser");
        loginPage.selectPassword("testingisfun99");
        loginPage.util.pause(3);
        loginPage.clickLogin();
        Assert.assertTrue(loginPage.isOrdersVisible(), "Login failed! 'Orders' link not found.");
    }

    @Test(dependsOnMethods = "verifyLogin")
    public void verifyProductAddAndQuantity() {
    	loginPage.util.pause(2);
        loginPage.selectApple();
        loginPage.util.pause(2);
        loginPage.addiPhone11Pro();
        loginPage.util.pause(3);
        loginPage.increaseQuantity();
        loginPage.util.pause(3);
        Assert.assertTrue(loginPage.verifyQuantityIncreased(), "Quantity did not increase!");
    }
    
    @Test(dependsOnMethods = "verifyProductAddAndQuantity")
    public void verifyCartClosedAndOrdersVisible() {
        loginPage.closeCart();
        loginPage.util.pause(2);
        Assert.assertTrue(loginPage.isOrdersVisible(), "'Orders' link not visible after closing cart!");
    }
    
    @Test(dependsOnMethods = "verifyCartClosedAndOrdersVisible")
    public void verifyAddiPhone12Pro() {
        loginPage.util.pause(2);
        loginPage.addiPhone12Pro();
        loginPage.util.pause(3);
        Assert.assertTrue(
                loginPage.verifyiPhone12ProQuantity(1),
                "Quantity did not show as 1 for iPhone 12 Pro!"
            );
    }

    @Test(priority = 3)
    public void verifyCheckout() {
        loginPage.clickCheckout();
        loginPage.util.pause(3);
        Assert.assertTrue(loginPage.isOrderSummaryVisible(), "Checkout failed! Order Summary not visible.");
    }
//
    @Test(priority = 4)
    public void verifyAddressPositive() throws TimeoutException {
        loginPage.enterAddress("Sai", "Kotha", "10th Main road, Kaveri layout", "Banglore", "560037");
        loginPage.util.pause(3);
        Assert.assertTrue(loginPage.isOrderPlaced(), "Order placed successfully!");
    }

//
    @Test(priority = 5)
    public void verifyReceiptAndContinue() {
        loginPage.downloadReceipt();
        loginPage.util.pause(3);
        loginPage.continueShopping();
        Assert.assertTrue(loginPage.isOrdersMenuVisible(), "Continue shopping did not return to orders!");
    }
//
   
    // order check
    
    @Test(priority = 6)
    public void seeorder() {
    	  // Click Orders link
        loginPage.clickOrdersMenu();
        loginPage.util.pause(3);

        // Assert the order was placed
        Assert.assertTrue(loginPage.ordercheck(), "Order was not placed successfully!");
    }
  
    // Adding favorite 
    
    @Test(priority = 7)
    public void verifyAddToFavourites() {
        loginPage.clickLogo();
        loginPage.util.pause(2);
        loginPage.clickFavouriteButton();
        loginPage.util.pause(2);
        loginPage.clickFavouritesLink();
        loginPage.util.pause(2);

        Assert.assertTrue(loginPage.isProductInFavourites("iPhone XR"),
                "Product not found in favourites!");
    }

    // Logout

	@Test(priority = 8)
    public void verifyLogout() {
        loginPage.logout();
        loginPage.util.pause(3);
        Assert.assertTrue(loginPage.isSignInVisible(), "Logout failed! Sign In not visible.");
    }
	


    @AfterClass
    public void tearDown() {
        driver.quit();
    }
}
