package com.nbin.test.ui.config.webdriver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;

/**
 * Created by AjayGoenka on 23/03/2017.
 */
public class WaitConditions {

    public static ExpectedCondition<Boolean> urlContains(final String text) {
        return new ExpectedCondition<Boolean>() {
            private String currentUrl = "";

            @Override
            public Boolean apply(WebDriver driver) {
                currentUrl = driver.getCurrentUrl();
                return currentUrl.contains(text);
            }

            @Override
            public String toString() {
                return String.format("URL to contain \"%s\". Current URL: \"%s\"", text, currentUrl);
            }
        };
    }

    public static ExpectedCondition<Boolean> pageContainsText(final String text) {
        return new ExpectedCondition<Boolean>() {
            private String currentPage = "";

            @Override
            public Boolean apply(WebDriver driver) {
                currentPage = driver.getPageSource();
                return currentPage.contains(text);
            }

            @Override
            public String toString() {
                return String.format("Page to contain \"%s\"", text);
            }
        };
    }

    public static ExpectedCondition<Boolean> pageTitleContains(final String title) {
        return new ExpectedCondition<Boolean>() {
            private String currentPageTitle = "";

            @Override
            public Boolean apply(WebDriver driver) {
                currentPageTitle = driver.getTitle();
                return currentPageTitle.contains(title);
            }

            @Override
            public String toString() {
                return String.format("Page to contain \"%s\". Current Title: \"%s\"", title, currentPageTitle);
            }

        };
    }
}
