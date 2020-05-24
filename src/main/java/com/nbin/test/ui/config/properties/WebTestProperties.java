package com.nbin.test.ui.config.properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Scope("cucumber-glue")
public class WebTestProperties {
    /**
     * Used to retrieve properties and make them available to tests
     */
    @Autowired
    private Environment environment;

    public String getProperty(String input_prop){
        return environment.getProperty(input_prop);
    }

    public String getEnvironment() {
        return System.getProperty("environment");
    }

    public String getBrowser() {
        return System.getProperty("selenium.browser");
    }

}
