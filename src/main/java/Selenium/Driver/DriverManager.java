package Selenium.Driver;

import Selenium.SeleniumProperties;
import com.google.common.base.Strings;
import io.github.bonigarcia.wdm.*;
import org.apache.log4j.Logger;
import org.openqa.selenium.NoSuchSessionException;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.*;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.net.URL;

public class DriverManager {
    private static final Logger logger = Logger.getLogger(DriverManager.class.getName());
    private static WebDriver webDriver;
    private static SharedDriver sharedDriver;
    private static String driverType;
    private static String osName = System.getProperty("os.name").toLowerCase();

    /**
     * This method is designed to declared WebDrivers that are not supported by default. Ex. a Remote Driver
     * @param webDriver Selenium WebDriver
     * @return SharedDriver
     */
    private static SharedDriver setSharedDriver(WebDriver webDriver) {
        sharedDriver = new SharedDriver(webDriver);
        return sharedDriver;
    }

    /**
     * Get the current Shared Driver
     * @return SharedDriver
     */
    protected SharedDriver getDriver() {
        try {
            sharedDriver.getWindowHandles();
            return sharedDriver;
        } catch (Exception e) {
            return forceNewDriver();
        }
    }

    /**
     * Create a new Shared Driver
     * @return SharedDriver
     */
    protected SharedDriver forceNewDriver() {
        if (!(driverType == null))
            logger.info("Creating new Webdriver of type: " + driverType);
        sharedDriver = setDriver();
        return sharedDriver;
    }

    private SharedDriver setDriver() {
        //Get value passed using maven -- if not available get the value from the properties file
        driverType = System.getProperty("browser");
        if(Strings.isNullOrEmpty(driverType)) {
            driverType = SeleniumProperties.getProperty("browser");
        }

        if (!Strings.isNullOrEmpty(driverType)) {
            logger.info("Setting driver to: " + driverType);

            switch (driverType.toLowerCase()) {
                case "chrome":
                    return setDriverToChrome();
                case "edge":
                    return setDriverToEdge();
                case "ie":
                case "internet explorer":
                    return setDriverToIE();
                case "safari":
                    return setDriverToSafari();
                case "headless":
                    return setDriverToHeadless();
                case "griddriver":
                case "gridchrome":
                    return setGridDriver();
                default:
                    throw new UnsupportedOperationException(driverType + " is not a supported Web Driver. " +
                            "Please use Chrome, IE, Firefox, Safari or Headless");
            }
        } else {
            throw new NullPointerException("Browser property not found.");
        }
    }

    /**
     * Set the Share Driver as Internet Explorer
     * @return SharedDriver
     */
    private static SharedDriver setDriverToIE() {
        logger.debug("Setting IE Driver");
        InternetExplorerDriverManager.iedriver().setup();
        DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();
        capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
        webDriver = new InternetExplorerDriver();
        return setSharedDriver(webDriver);
    }

    /**
     * Set the Share Driver as Chrome
     * @return SharedDriver
     */
    private static SharedDriver setDriverToChrome() {
        logger.debug("Setting Chrome Driver");
        ChromeDriverManager.chromedriver().setup();
        webDriver = new ChromeDriver();
        return setSharedDriver(webDriver);
    }

    /**
     * Set the Share Driver as Edge
     * @return SharedDriver
     */
    private static SharedDriver setDriverToEdge() {
        logger.debug("Setting Edge Driver");
        EdgeDriverManager.edgedriver().setup();
        webDriver = new EdgeDriver();
        return setSharedDriver(webDriver);
    }


    /**
     * Set the Share Driver as Safari
     * @return SharedDriver
     */
    private static SharedDriver setDriverToSafari() {
        if (!osName.contains("win")) {
            webDriver = new SafariDriver();
            return setSharedDriver(webDriver);
        } else {
            throw new UnsupportedOperationException("Safari not supported on Windows.");
        }
    }

    private static SharedDriver setDriverToHeadless() {
        logger.debug("Setting PhantomJS Driver");
        PhantomJsDriverManager.phantomjs().setup();
        DesiredCapabilities capabilities = DesiredCapabilities.phantomjs();
        capabilities.setJavascriptEnabled(true);
        webDriver = new PhantomJSDriver(capabilities);
        return setSharedDriver(webDriver);
    }

    private static SharedDriver setGridDriver() {
        String browserName = SeleniumProperties.getProperty("grid.browser.name");
        String browserUrl = SeleniumProperties.getProperty("grid.browser.url");
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setBrowserName(browserName);
        capabilities.setPlatform(Platform.LINUX);
        capabilities.setCapability("name", "Remote File Upload using Selenium 2's FileDetectors");
        WebDriver augmentedDriver = null;
        try {
            RemoteWebDriver driver = new RemoteWebDriver(new URL(browserUrl), capabilities);
            driver.setFileDetector(new LocalFileDetector());
            augmentedDriver = new Augmenter().augment(driver);
        } catch (Exception e) {
            Class exceptionClass = e.getClass();
            if (exceptionClass == UnreachableBrowserException.class){
                logger.info("Unable to connect to browser.  Please ensure that remote host is reachable and active." + " UnreachableBrowserException: " + e.getMessage());
            } else {
                e.printStackTrace();
            }
        }
        webDriver = augmentedDriver;
        logger.info("Returning SharedDriver from setGridDriver()");
        return setSharedDriver(webDriver);
    }


    protected void waitUntil(ExpectedCondition expectedCondition) {
        WebDriverWait wait = new WebDriverWait(getDriver(), SeleniumProperties.getImplicitWaitTime());
        wait.until(expectedCondition);
    }

    protected void waitUntil(ExpectedCondition expectedCondition, int waitTimeInSeconds) {
        WebDriverWait wait = new WebDriverWait(getDriver(), waitTimeInSeconds);
        wait.until(expectedCondition);
    }

    protected void waitUntilElementExists(WebElement element) {
        //Wait until the document is ready
        ExpectedCondition elementExist = new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver driver) {
                return element.isDisplayed();
            }};
        waitUntil(elementExist);
    }
    public static String getDriverType() {
        return driverType;
    }
}
