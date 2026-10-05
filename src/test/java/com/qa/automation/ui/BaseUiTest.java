package com.qa.automation.ui;

import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Tag;

@Tag("ui")
public abstract class BaseUiTest {

    @AfterEach
    void closeBrowser() {
        Selenide.closeWebDriver();
    }
}
