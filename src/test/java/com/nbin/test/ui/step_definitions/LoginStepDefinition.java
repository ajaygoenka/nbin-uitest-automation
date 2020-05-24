package com.nbin.test.ui.step_definitions;

import com.nbin.test.ui.config.GlobalProperties;
import com.nbin.test.ui.config.spring.WebTestConfig;
import com.nbin.test.ui.page_objects.home.HomePage;
import com.nbin.test.ui.page_objects.login.LoginPage;
import cucumber.api.Scenario;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static com.nbin.test.ui.config.webdriver.SharedWebDriver.REAL_DRIVER;
import static com.nbin.test.ui.step_definitions.CommonStepDefinition.CURRENT_PAGE;

/**
 * Created by C14488A on 27/03/2018.
 */

public class LoginStepDefinition {

    @Autowired
    public LoginPage loginPage;

    @Autowired
    public HomePage homePage;

    @Autowired
    public GlobalProperties properties;

     public void before() throws Throwable {
        System.out.println("I am in before function");
     }

    @Then("^I am on CompassMemberLogin page$")
    public void i_am_On_Certain_Page() throws Throwable {
         loginPage.pageShownWithElements();
    }

    @And("^I enter \"(.*)\" in field (.*) in Login Page$")
    public void iEnterTextInFieldFieldname(String text, String fieldName) throws Throwable {
        if(fieldName.equalsIgnoreCase("userName"))
            loginPage.enterUserName(text);
        if(fieldName.equalsIgnoreCase("password"))
            loginPage.enterPassword(text);

    }

    @When("^I click the (.*) (tab|link|button) in Login Page$")
    public void iClickTheElement(String element_name, String type) throws Throwable {
        if(type.equalsIgnoreCase("button"))
            loginPage.clickLoginSubmitButton();
           // homePage.changTOEnglish();
        Thread.sleep(500);
    }
}
