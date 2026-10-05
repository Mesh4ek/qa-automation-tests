package com.qa.automation.ui;

import com.qa.automation.config.Credentials;
import com.qa.automation.config.TestConfig;
import com.qa.automation.ui.pages.LoginPage;
import com.qa.automation.ui.pages.ProductsPage;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LoginTest extends BaseUiTest {

    private static final String LOCKED_OUT_USERNAME = "locked_out_user";

    @Test
    void standardUserLandsOnProductsPageWithProducts() {
        ProductsPage productsPage = LoginPage.open()
                .loginAs(TestConfig.sauceDemoUser())
                .shouldBeOpened();

        assertThat(productsPage.products())
                .as("products listed on the Products page")
                .isNotEmpty()
                .allSatisfy(product -> {
                    assertThat(product.name()).as("product name").isNotBlank();
                    assertThat(product.price()).as("price of '%s'", product.name()).isPositive();
                });
    }

    @Test
    void lockedOutUserCannotLogIn() {
        Credentials lockedOutUser = new Credentials(LOCKED_OUT_USERNAME, TestConfig.sauceDemoUser().password());

        LoginPage.open()
                .tryLoginAs(lockedOutUser)
                .shouldShowError("Epic sadface: Sorry, this user has been locked out.");
    }
}