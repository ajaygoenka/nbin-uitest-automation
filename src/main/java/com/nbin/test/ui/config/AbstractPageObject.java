package com.nbin.test.ui.config;

import com.nbin.test.ui.config.webdriver.SharedWebDriver;
import com.nbin.test.ui.config.webdriver.WaitConditions;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by AjayGoenka on 23/03/2017.
 */
@Component
@Scope("cucumber-glue")
public abstract class AbstractPageObject {

    private static final Logger log = (Logger) LoggerFactory.getLogger(AbstractPageObject.class);

    public String path;
	public static  String CURRENT_URL = "";
    public static  String USER_TYPE   = "existing";
	public static  String USER_NAME   = "alpha";
    protected final WebDriver driver;
    public final int waitTimeOutSeconds;


    public AbstractPageObject() {
        this.path = SharedWebDriver.applicationUrl;
        this.driver = SharedWebDriver.REAL_DRIVER;
        this.waitTimeOutSeconds = SharedWebDriver.waitTime;
    }

    public abstract void   pageShownWithElements() throws InterruptedException;

    public abstract String getCssElementPath(String element_name )throws  ClassNotFoundException, NoSuchFieldException, IllegalAccessException;

    public WebDriver getDriver() {
        return driver;
    }

    public String getPath() {
        return path;
    }

    public int getTimeout() {
        return waitTimeOutSeconds;
    }

    public void goToAndWait() {
        log.info("Going to path: " + path);
        driver.navigate().to(path);
    }

    public void goToAndWait(String query_param){
		path = path + query_param;
		goToAndWait();
	}

    public void ensure_current_url(String url_path) {
        wait_until_true_or_timeout(WaitConditions.urlContains(url_path));
    }

    public boolean is_text_present(String text) {
        log.info("Checking that text " + text + " is present");
        wait_until_true_or_timeout(WaitConditions.pageContainsText(text));
        return true;
    }

    public boolean is_title_present(String title) {
        log.info("Checking that title " + title + " is present");
        wait_until_true_or_timeout(WaitConditions.pageTitleContains(title));
        return true;
    }

    // wait until condition is true or timeout is reached
    protected <V> V wait_until_true_or_timeout(ExpectedCondition<V> isTrue) {
        Wait<WebDriver> wait = new WebDriverWait(this.driver, waitTimeOutSeconds)
                .ignoring(StaleElementReferenceException.class);
        try {
            return wait.until(isTrue);
        } catch (TimeoutException rte) {
            throw new TimeoutException(rte.getMessage());
        }
    }

    public void setText(WebElement element, String text) {
        log.info("Setting text " + text + " on WebElement " + element.toString());
        scrollIntoView(element).clear();
        element.sendKeys(text);
    }

    public String getText(WebElement element) {
        log.info("Getting text from WebElement " + element.toString());
        return element.getText();
    }

    public void submit(WebElement element) {
        element.submit();
    }

    public void click(WebElement element) {
        log.info("Clicking on WebElement " + element.toString());
        scrollIntoView(element).click();
    }

	public void clickWhenClickable(WebElement element) {
		log.info("Clicking and waiting on WebElement " + element.toString());
		WebDriverWait wait = new WebDriverWait(getDriver(), waitTimeOutSeconds);
		wait.until(ExpectedConditions.elementToBeClickable(element));
		scrollIntoView(element);
		element.click();
	}

    public void doubleClick(WebElement element) {
        Actions builder = new Actions(driver);
        builder.doubleClick(element).build().perform();
    }

    public void jsClick(WebElement element) {
        scrollIntoView(element);
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript("arguments[0].click();", element);
    }

    public void setCheckboxTo(WebElement element, boolean checked) {
        try {
            if (element.isEnabled()) {
                if (checked) {
                    if (!isChecked(element)) {
                        click(element);
                    }
                } else {
                    if (isChecked(element)) {
                        click(element);
                    }
                }
            }
        } catch (RuntimeException e) {
            throw new InvalidElementStateException("Checkbox is disabled. You can NOT set it.");
        }
    }
    /**
     * Checks that Checkbox is checked or not
     *
     * @return current Checkbox state - true/false
     */
    public boolean isChecked(WebElement element) {
        return element.isSelected();
    }

    public void selectDropdownByText(WebElement element, String visibleText) {
        log.info("Selecting item " + visibleText + " on Select element " + element.toString());
        Select filterSelect = new Select(element);
        waitForDropdownItems(element);
        filterSelect.selectByVisibleText(visibleText);
    }

    private void waitForDropdownItems(WebElement element) {
        WebDriverWait wait = new WebDriverWait(getDriver(), waitTimeOutSeconds);
        wait.until(ExpectedConditions.elementToBeClickable(element));
        scrollIntoView(element);
    }

    public boolean isElementDisplayed(String element_css_path){
        return isElementFound(find(By.cssSelector(element_css_path)));
    }

    public boolean isElementChecked(String element_css_path){
		wait_until_true_or_timeout(ExpectedConditions.presenceOfElementLocated(By.cssSelector(element_css_path)));//.elementToBeClickable(locator)
		return (driver.findElement(By.cssSelector(element_css_path)).getAttribute("checked")!= null);
	}

    public boolean checkElementVisibility(String element_css_path) {
        boolean found = false;
        for(int i=0;(i<10 && !found); i++) {
            found = driver.findElements(By.cssSelector(element_css_path)).size() >= 1; // checks if element exists ( 1 or more)
            try{ Thread.sleep(1000); } catch(InterruptedException ie){}
        }
        return found;
    }

    protected boolean isElementFound(WebElement element) {
        boolean result = false;
        for (int attempts = 0; attempts < 2000; attempts++) {
            try {
                result = element.isDisplayed();
                break;
            } catch (StaleElementReferenceException e) {
                e.printStackTrace();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    public boolean doesElementExist(By locator) {
        return driver.findElements(locator).size() >= 1; // checks if element exists ( 1 or more)
    }

    public WebElement find(By locator) {
        try {
            wait_until_true_or_timeout(ExpectedConditions.visibilityOfElementLocated(locator));
            return driver.findElement(locator);
        } catch (NoSuchElementException ex) {
            throw new NoSuchElementException(ex.getMessage());
        }
    }

	public List<WebElement> findAll(By locator) throws NoSuchElementException {
		wait_until_true_or_timeout(ExpectedConditions.visibilityOfElementLocated(locator));//.elementToBeClickable(locator)
		return driver.findElements(locator);
	}

    public void notFind(By locator) {
        wait_until_true_or_timeout(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    public WebElement scrollIntoView(WebElement element) {
        if (!element.isDisplayed() || !isElementInViewport(element)) {
            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0," + (element.getLocation().y - 100) + ");", element);
            waitForASetTime(500, TimeUnit.MILLISECONDS);
        }
        return element;
    }

    public void scrollDown() {
        ((JavascriptExecutor) driver).executeScript("window.scrollBy(0,100)", "");
    }

    public void scrollUp() {
        ((JavascriptExecutor) driver).executeScript("window.scrollBy(0,-100)", "");
    }

    private boolean isElementInViewport(WebElement element) {
        return (boolean) ((JavascriptExecutor) driver)
                .executeScript(
                        "var rect     = arguments[0].getBoundingClientRect(); " +
                                "var vWidth   = window.innerWidth || document.documentElement.clientWidth; " +
                                "var vHeight  = window.innerHeight || document.documentElement.clientHeight; " +

                                "return ( rect.right > 0 && rect.bottom > 0 && rect.left < vWidth && rect.top < vHeight " +
                                "&&  arguments[0].contains(document.elementFromPoint(rect.left,  rect.top)) " +
                                "&&  arguments[0].contains(document.elementFromPoint(rect.right, rect.top)) " +
                                "&&  arguments[0].contains(document.elementFromPoint(rect.right, rect.bottom)) " +
                                "&&  arguments[0].contains(document.elementFromPoint(rect.left,  rect.bottom)) " +
                                ");", element);
    }

    public boolean isAlertPresent() {
        try {
            Alert alert = driver.switchTo().alert();
            return true;
        } catch (NoAlertPresentException e) {
            return false;
        }
    }

    public void waitForASetTime(int timeout, TimeUnit timeUnit) {
        try {
            switch (timeUnit) {
                case MILLISECONDS:
                    Thread.sleep(timeout);
                    break;
                case SECONDS:
                    Thread.sleep(timeout * 1000);
                    break;
                case MINUTES:
                    Thread.sleep(timeout * 1000 * 60);
                    break;
                default:
                    break;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

	public String read_ENV_param(String param_name){
		String line = "", value = "";
		String file_path = System.getProperty("user.dir");
		file_path = (file_path.contains("automation"))?file_path.substring(0,file_path.length()-11): file_path;
		file_path = file_path + "/.env"; System.out.println("\nREADING .env FILE for ["+ param_name +"] from PATH: "+ file_path+ "\n");
		try{
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file_path)));
			while((line = br.readLine()) != null) {
				if(line.contains(param_name)) {
					value = line.substring(line.indexOf('=')+1,line.length());
					break;
				}
			}
		}catch(IOException ie) {}
		return  value;
	}

	public String getUserType(String user_name){
		if(user_name.equalsIgnoreCase("Free"))
			return "free";
		else   // if user name is ALPHA, BETA, CHARLIE, DELTA, ECHO
			return "premium";
	}

    public void setCookie(String name, String value, String domain){
        Cookie cookie = new Cookie.Builder(name, value)
                .domain(domain)
                .expiresOn(new Date(new Date().getTime() + 3600*1000))
                .build();

        driver.manage().addCookie(cookie);
    }
}
