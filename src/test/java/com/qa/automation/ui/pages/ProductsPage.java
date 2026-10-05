package com.qa.automation.ui.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.qa.automation.ui.model.Product;
import com.qa.automation.ui.model.SortOption;

import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.webdriver;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;

public final class ProductsPage {

    private static final String ITEM = "[data-test=inventory-item]";
    private static final String ITEM_NAME = "[data-test=inventory-item-name]";
    private static final String ITEM_PRICE = "[data-test=inventory-item-price]";
    private static final String ADD_TO_CART_BUTTON = "[data-test^=add-to-cart]";
    private static final String REMOVE_BUTTON = "[data-test^=remove]";

    private final SelenideElement title = $("[data-test=title]");
    private final ElementsCollection items = $$(ITEM);
    private final ElementsCollection itemNames = $$(ITEM_NAME);
    private final SelenideElement sortSelect = $("[data-test=product-sort-container]");
    private final SelenideElement activeSortOption = $("[data-test=active-option]");
    private final SelenideElement cartLink = $("[data-test=shopping-cart-link]");
    private final SelenideElement cartBadge = $("[data-test=shopping-cart-badge]");

    public ProductsPage shouldBeOpened() {
        webdriver().shouldHave(urlContaining("/inventory.html"));
        title.shouldHave(exactText("Products"));
        return this;
    }

    public ProductsPage sortBy(SortOption option) {
        sortSelect.selectOptionByValue(option.value());
        activeSortOption.shouldHave(exactText(option.label()));
        return this;
    }

    public List<Product> products() {
        items.shouldHave(sizeGreaterThan(0));
        List<Product> products = new ArrayList<>();
        for (SelenideElement item : items.asFixedIterable()) {
            products.add(Product.fromDisplayed(item.$(ITEM_NAME).text(), item.$(ITEM_PRICE).text()));
        }
        return products;
    }

    public ProductsPage addToCart(Product product) {
        SelenideElement item = itemByName(product.name());
        item.$(ADD_TO_CART_BUTTON).click();
        item.$(REMOVE_BUTTON).shouldBe(visible);
        return this;
    }

    public ProductsPage shouldHaveCartBadge(int itemsCount) {
        cartBadge.shouldHave(exactText(String.valueOf(itemsCount)));
        return this;
    }

    public CartPage openCart() {
        cartLink.click();
        return new CartPage();
    }

    private SelenideElement itemByName(String name) {
        return itemNames.findBy(exactText(name)).ancestor(ITEM);
    }
}