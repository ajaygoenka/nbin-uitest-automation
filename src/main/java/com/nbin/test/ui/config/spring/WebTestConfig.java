package com.nbin.test.ui.config.spring;

import com.nbin.test.ui.config.AbstractPageObject;
import com.nbin.test.ui.config.GlobalProperties;
import com.nbin.test.ui.config.properties.WebTestProperties;
import com.nbin.test.ui.config.webdriver.SharedWebDriver;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
@PropertySources({
        @PropertySource(value = {"classpath:properties/${environment}.properties"}),
        @PropertySource(value = {"classpath:properties/global.properties"})
})
@ComponentScan(basePackages = {"com.nbin.test"})
public class WebTestConfig {

    @Bean
    public WebTestProperties properties() {
        return new WebTestProperties();
    }

    @Bean
    public GlobalProperties globalProperties() {
        return new GlobalProperties(properties());
    }

    @Bean
    public SharedWebDriver sharedWebDriver() {
        return new SharedWebDriver(globalProperties());
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfig() {
        return new PropertySourcesPlaceholderConfigurer();
    }

}
