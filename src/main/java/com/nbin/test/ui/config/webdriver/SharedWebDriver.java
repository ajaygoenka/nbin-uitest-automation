package com.nbin.test.ui.config.webdriver;

import com.nbin.test.ui.config.GlobalProperties;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import wiremock.net.minidev.json.JSONArray;
import wiremock.net.minidev.json.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.nio.charset.Charset;

@Scope("cucumber-glue")
public class SharedWebDriver {

    private static final Logger log = (Logger) LoggerFactory.getLogger(SharedWebDriver.class);

    public  static WebDriver REAL_DRIVER;
    public  static String applicationUrl;
    public  static int waitTime;
    private final  Thread    CLOSE_THREAD = new Thread() {
        @Override
        public void run() {
            createConsolidatedTestReport();
            quitGlobalInstance();
        }
    };

    public GlobalProperties properties;

    public SharedWebDriver(GlobalProperties properties) {
        log.info("Initialising SharedWebDriver and Global Properties...");
        this.properties = properties;

        log.info("Setting SharedDriver to use Local Browser");
        REAL_DRIVER = getLocalWebDriver(BrowserType.valueOf(properties.getBrowserName()));
        REAL_DRIVER.manage().window().maximize();
        log.info("Close all Open Thread");
        Runtime.getRuntime().addShutdownHook(CLOSE_THREAD);

        log.info("define application url");
        applicationUrl = properties.getApplicationBaseUrl();

        log.info("define wait time");
        waitTime = properties.getSeleniumWaitTimeOutSeconds();
    }

    private void quitGlobalInstance() {
        log.info("Quitting Global Instance...");
        WebDriver driver = REAL_DRIVER;
        REAL_DRIVER = null;
        if (driver != null) {
            driver.quit();
        }

    }

    public WebDriver getLocalWebDriver(BrowserType browser) {
        if (REAL_DRIVER == null) {
            switch (browser) {
                case CHROME:
                    System.setProperty("webdriver.chrome.driver",getPath(browser));
                    REAL_DRIVER = new ChromeDriver();
                    checkBrowserOS();
                    break;
                case FIREFOX:
                    System.setProperty("webdriver.gecko.driver",getPath(browser));
                    DesiredCapabilities capabilities = DesiredCapabilities.firefox();
                    FirefoxProfile firefoxProfile = new FirefoxProfile();
                    capabilities.setCapability(FirefoxDriver.PROFILE, firefoxProfile);
                    FirefoxOptions firefoxOptions = new FirefoxOptions(capabilities);
                    REAL_DRIVER = new FirefoxDriver(firefoxOptions);

                    checkBrowserOS();
                    break;
                case IE:
                    System.setProperty("webdriver.ie.driver",getPath(browser));
                    DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();
                    ieCapabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
                    ieCapabilities.setCapability(InternetExplorerDriver.ENABLE_PERSISTENT_HOVERING, false);
                    ieCapabilities.setCapability(InternetExplorerDriver.UNEXPECTED_ALERT_BEHAVIOR, UnexpectedAlertBehaviour.ACCEPT);
                    ieCapabilities.setCapability(InternetExplorerDriver.IGNORE_ZOOM_SETTING, true);
                    InternetExplorerOptions opt = new InternetExplorerOptions(ieCapabilities);
                    REAL_DRIVER = new InternetExplorerDriver(opt);
                    checkBrowserOS();
                    break;
                case OPERA:
                    System.setProperty("webdriver.opera.driver",getPath(browser));
                    REAL_DRIVER = new OperaDriver();
                    checkBrowserOS();
                    break;
                case SAFARI:
                    REAL_DRIVER = new SafariDriver();
                    checkBrowserOS();
                    break;
                case EDGE:
                    REAL_DRIVER = new EdgeDriver();
                    checkBrowserOS();
                    break;
            }
        }
        return REAL_DRIVER;
    }

    private void checkBrowserOS() {
        //Get Browser name and version.
        Capabilities caps = ((RemoteWebDriver) REAL_DRIVER).getCapabilities();
        String browserName = caps.getBrowserName();
        String browserVersion = caps.getVersion();

        //Get OS name.
        String os = System.getProperty("os.name").toLowerCase();
        log.info("STARTING TESTS ON OS: " + os + ", BROWSER: " + browserName + " v_"+ browserVersion);
    }

    private void createConsolidatedTestReport(){
        System.out.println("reading file rerun-report.json");
        File rerun_report_file = new File("target/cucumber-reports/rerun-report.json");
        String data_to_append = "[{\"line\":1,\"name\":\"----- RERUN FAILED TEST SCENARIOS REPORT -----\",\"description\":\"\",\"id\":\"rerun-failed-test-scenarios-report-as-follows\",\"keyword\":\"Feature\",\"uri\":\"\",\"tags\":[{\"line\":1,\"name\":\"@ui_home\"}]},";
        if((rerun_report_file.exists()) && (rerun_report_file.length()> 2)) {
            try {
                JSONArray json_arr = (JSONArray) new JSONParser().parse(new FileReader(rerun_report_file));
                StringBuilder builder = new StringBuilder(json_arr.toJSONString());
                builder = builder.deleteCharAt(0);
                builder.insert(0, data_to_append);
                FileUtils.writeStringToFile(rerun_report_file, builder.toString(), Charset.forName("UTF-8"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String getPath(BrowserType browser){
        return properties.getBrowserProperties(browser);
    }

}
