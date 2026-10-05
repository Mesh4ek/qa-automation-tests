package com.qa.automation.ui.model;

import java.util.Comparator;

import static java.util.Comparator.comparing;

public enum SortOption {

    NAME_A_TO_Z("az", "Name (A to Z)", comparing(Product::name)),
    NAME_Z_TO_A("za", "Name (Z to A)", comparing(Product::name).reversed()),
    PRICE_LOW_TO_HIGH("lohi", "Price (low to high)", comparing(Product::price)),
    PRICE_HIGH_TO_LOW("hilo", "Price (high to low)", comparing(Product::price).reversed());

    private final String value;
    private final String label;
    private final Comparator<Product> expectedOrder;

    SortOption(String value, String label, Comparator<Product> expectedOrder) {
        this.value = value;
        this.label = label;
        this.expectedOrder = expectedOrder;
    }

    public String value() {
        return value;
    }

    public String label() {
        return label;
    }

    public Comparator<Product> expectedOrder() {
        return expectedOrder;
    }
}