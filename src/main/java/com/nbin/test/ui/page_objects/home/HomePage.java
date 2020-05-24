package com.nbin.test.ui.page_objects.home;

import com.nbin.test.ui.config.AbstractPageObject;
import com.nbin.test.ui.page_objects.login.LoginPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.junit.Assert.assertTrue;

@Component
@Scope("cucumber-glue")
public class HomePage extends AbstractPageObject {

    // Alerts Page elements
    public String pageTitle      = "";
    public String englishlogout            = "Sign Out";
    public String francaislogout           = "Fermer la session";
    public String changeLanguageInFrancais = "Français";
    public String changeLanguageInEnglish = "English";

    public boolean language = true ;


    public HomePage() { }

    public void pageShownWithElements() {
    }

    public String getCssElementPath(String element_name) throws  ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        return LoginPage.class.getField(element_name).get(this).toString();
    }


    public void clickLogOutSubmitButton(){
        if(language)
        click(find(By.linkText(englishlogout)));
        else
        click(find(By.linkText(francaislogout)));


    }

    public void clickOnFrancaisLink(){
        language = false;
        System.out.println(find(By.linkText(changeLanguageInFrancais)).getText());
        click(find(By.linkText(changeLanguageInFrancais)));


    }

    public void changTOEnglish() {
        if(driver.findElement(By.linkText(changeLanguageInEnglish)).getText().equalsIgnoreCase("English"));
        click(find(By.linkText(changeLanguageInEnglish)));


    }
}
