package com.qa.automation.ui.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.qa.automation.ui.model.Product;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.webdriver;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;

public final class CartPage {

    private final SelenideElement title = $("[data-test=title]");
    private final ElementsCollection items = $$("[data-test=inventory-item]");

    public CartPage shouldBeOpened() {
        webdriver().shouldHave(urlContaining("/cart.html"));
        title.shouldHave(exactText("Your Cart"));
        return this;
    }

    public CartPage shouldContainOnly(Product product) {
        items.shouldHave(size(1));
        SelenideElement item = items.first();
        item.$("[data-test=inventory-item-name]").shouldHave(exactText(product.name()));
        item.$("[data-test=inventory-item-price]").shouldHave(exactText(product.displayedPrice()));
        item.$("[data-test=item-quantity]").shouldHave(exactText("1"));
        return this;
    }
}