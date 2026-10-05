package com.qa.automation.ui.model;

import java.math.BigDecimal;

public record Product(String name, BigDecimal price) {

    private static final String CURRENCY_SIGN = "$";

    public static Product fromDisplayed(String name, String displayedPrice) {
        return new Product(name, new BigDecimal(displayedPrice.replace(CURRENCY_SIGN, "").trim()));
    }

    public String displayedPrice() {
        return CURRENCY_SIGN + price.toPlainString();
    }
}