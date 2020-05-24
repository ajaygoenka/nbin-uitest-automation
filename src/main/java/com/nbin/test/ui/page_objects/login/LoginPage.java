package com.nbin.test.ui.page_objects.login;

import com.nbin.test.ui.config.AbstractPageObject;
import org.openqa.selenium.By;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.junit.Assert.assertTrue;

@Component
@Scope("cucumber-glue")
public class LoginPage extends AbstractPageObject {

    // Alerts Page elements
    public String pageTitle      = "";
    public String userName       = "_58_login";
    public String password       = "_58_password";
    public String loginSubmit    = "LoginSubmit";

    public LoginPage() { }

    public void pageShownWithElements() {
        assertTrue(find(By.name(userName)).isDisplayed());
        assertTrue(find(By.name(password)).isDisplayed());
        assertTrue(find(By.name(loginSubmit)).isDisplayed());
    }

    public String getCssElementPath(String element_name) throws  ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        return LoginPage.class.getField(element_name).get(this).toString();
    }

    public void enterUserName(String name){
        setText(find(By.name(userName)),name);
    }

    public void enterPassword(String pwd){
        setText(find(By.name(password)),pwd);
    }

    public void clickLoginSubmitButton(){
        click(find(By.name(loginSubmit)));

    }
}
