package com.qa.automation.ui.pages;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.qa.automation.config.Credentials;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public final class LoginPage {

    private final SelenideElement usernameInput = $("[data-test=username]");
    private final SelenideElement passwordInput = $("[data-test=password]");
    private final SelenideElement loginButton = $("[data-test=login-button]");
    private final SelenideElement errorMessage = $("[data-test=error]");

    public static LoginPage open() {
        Selenide.open("/");
        return new LoginPage();
    }

    public ProductsPage loginAs(Credentials credentials) {
        submit(credentials);
        return new ProductsPage();
    }

    public LoginPage tryLoginAs(Credentials credentials) {
        submit(credentials);
        return this;
    }

    public LoginPage shouldShowError(String message) {
        errorMessage.shouldBe(visible).shouldHave(exactText(message));
        return this;
    }

    private void submit(Credentials credentials) {
        usernameInput.setValue(credentials.username());
        passwordInput.setValue(credentials.password());
        loginButton.click();
    }
}