package com.nbin.test.ui.step_definitions;

/**
 * Created by AjayGoenka on 23/03/2017.
 */
import com.nbin.test.ui.config.GlobalProperties;
import com.nbin.test.ui.config.AbstractPageObject;
import com.nbin.test.ui.config.spring.WebTestConfig;
import com.nbin.test.ui.page_objects.MainPage;
import com.nbin.test.ui.page_objects.home.HomePage;
import cucumber.api.Scenario;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = WebTestConfig.class)
public class CommonStepDefinition {


    public static  AbstractPageObject CURRENT_PAGE;

    @Autowired
    public GlobalProperties properties;

    @Autowired
    public MainPage mainPage;

    @Autowired
    public HomePage homePage;


    @Given("^I am (.*) Compass user$")
    public void iAmLoggedInCompassUser(String user_type) throws Throwable {
        mainPage.USER_TYPE   = user_type; // User type: [new / existing]
        mainPage.goToAndWait();
        //mainPage.ensure_current_url(mainPage.CURRENT_URL);
        //mainPage.pageShownWithElements();
    }

	@Then("^make sure I logged out successfully$")
	public void makeSureILoggedOutSuccessfully() throws Throwable {
       homePage.clickLogOutSubmitButton();
	}

	@When("^I go to (.*) page$")
    public void iGoToAGivenPage(String page_name) throws Throwable {
        if(page_name.equalsIgnoreCase("memberLogin"))
        mainPage.goToMemberLoginPage();

    }



    @When("^I wait for (.*) to complete$") // [10 seconds] or [loading spinner]
    public void i_wait_for_time_to_complete(String param) throws Throwable {
		if(param.contains("seconds")) {
			int waitTimeInSeconds = Integer.parseInt(param.substring(0,param.indexOf('s')-1));
			Thread.sleep(waitTimeInSeconds * 1000);
		}
    }

    @When("^I click the (.*) (tab|link|button) in Home Page to Change in Francais")
    public void iClickTheElement(String element_name, String type) throws Throwable {
        if(type.equalsIgnoreCase("link"))
            if(element_name.equalsIgnoreCase("Francais"))
           homePage.clickOnFrancaisLink();
        Thread.sleep(500);
    }

}
