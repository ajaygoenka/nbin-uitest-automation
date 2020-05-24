package com.nbin.test.ui.page_objects;

import com.nbin.test.ui.config.AbstractPageObject;
import org.openqa.selenium.By;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.junit.Assert.assertTrue;
@Component
@Scope("cucumber-glue")
public class MainPage extends AbstractPageObject {

    // Alerts Page elements
    public String memberLogin = "login_btn";


    public MainPage() {

    }

    public void pageShownWithElements() throws InterruptedException {
        assertTrue(find(By.className(memberLogin)).isDisplayed());

    }

    public void goToMemberLoginPage(){
        click(find(By.className(memberLogin)));
    }


    public String getCssElementPath(String element_name) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        return MainPage.class.getField(element_name).get(this).toString();
    }
}
