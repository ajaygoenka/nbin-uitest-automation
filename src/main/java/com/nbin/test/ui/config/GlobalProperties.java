package com.nbin.test.ui.config;

import com.nbin.test.ui.config.properties.WebTestProperties;
import com.nbin.test.ui.config.webdriver.BrowserType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

import static org.openqa.selenium.remote.BrowserType.*;

/**
 * Created by AjayGoenka on 23/03/2017.
 */

@Scope("cucumber-glue")
public class GlobalProperties {

    private static final Logger log = (Logger) LoggerFactory.getLogger(GlobalProperties.class);

    @Value("${selenium.browser:}")
    private String browser;

    @Value("${application.base.url:}")
    private String applicationBaseUrl;

    @Value("${application.local.url:}")
    private String applicationLocalUrl;

    @Value("${browser.chrome.path:}")
    private String browserChromePath;

    @Value("${browser.firefox.path:}")
    private String browserFirefoxPath;

    @Value("${browser.ie.path:}")
    private String browserIEPath;

    @Value("${browser.opera.path:}")
    private String browserOperaPath;

    @Value("${selenium.wait.timeout.seconds:}")
    private String timeout;


    public String  environment   = null;

    private WebTestProperties webTestProperties;


    public GlobalProperties(WebTestProperties properties) {
        try {
            log.info("Initialising GlobalProperties class for tests...");
            this.webTestProperties = properties;

            environment = webTestProperties.getEnvironment();
            browser    =  webTestProperties.getBrowser();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }


    public String getApplicationBaseUrl() {
        if(environment.equalsIgnoreCase("prod"))
            return applicationBaseUrl;
        else
            return applicationBaseUrl.replace("{env}", environment);
    }

    public int getSeleniumWaitTimeOutSeconds() {
        return Integer.parseInt(timeout);
    }


    public String getBrowserName() {
        return browser;
    }

    public String getEnvironment() {
        return environment;
    }


    public String getBrowserProperties(BrowserType browser){

        String path = "";
        switch (browser) {
            case CHROME:
                path = System.getProperty("user.dir")+ browserChromePath;
                break;

            case FIREFOX:
                path = System.getProperty("user.dir")+ browserFirefoxPath;
                break;

            case IE:
                path = System.getProperty("user.dir")+ browserIEPath;
                break;

            case OPERA:
                path = System.getProperty("user.dir")+ browserOperaPath;
                break;
        }
        return path ;
    }
}
