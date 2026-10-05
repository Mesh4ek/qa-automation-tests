package com.qa.automation.ui;

import com.qa.automation.config.TestConfig;
import com.qa.automation.ui.model.Product;
import com.qa.automation.ui.model.SortOption;
import com.qa.automation.ui.pages.LoginPage;
import com.qa.automation.ui.pages.ProductsPage;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ProductCartTest extends BaseUiTest {

    @ParameterizedTest(name = "sorted by {0}")
    @EnumSource(SortOption.class)
    void firstProductAfterSortingIsAddedToCart(SortOption sortOption) {
        ProductsPage productsPage = LoginPage.open()
                .loginAs(TestConfig.sauceDemoUser())
                .shouldBeOpened();
        List<Product> productsBeforeSorting = productsPage.products();

        List<Product> sortedProducts = productsPage.sortBy(sortOption).products();

        assertThat(sortedProducts)
                .as("products sorted by '%s'", sortOption.label())
                .containsExactlyInAnyOrderElementsOf(productsBeforeSorting)
                .isSortedAccordingTo(sortOption.expectedOrder());

        Product selectedProduct = sortedProducts.getFirst();

        productsPage.addToCart(selectedProduct)
                .shouldHaveCartBadge(1)
                .openCart()
                .shouldBeOpened()
                .shouldContainOnly(selectedProduct);
    }
}