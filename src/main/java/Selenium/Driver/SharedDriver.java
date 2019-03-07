package Selenium.Driver;

import Selenium.SeleniumProperties;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class SharedDriver extends EventFiringWebDriver {
    private static final Logger logger = Logger.getLogger(SharedDriver.class.getName());
    private static WebDriver REAL_DRIVER;

    private static final Thread CLOSE_THREAD = new Thread() {
        @Override
        public void run() {
            REAL_DRIVER.close();
        }
    };

    static {
        Runtime.getRuntime().addShutdownHook(CLOSE_THREAD);
    }

    SharedDriver(final WebDriver webDriver) {
        super(webDriver);
        REAL_DRIVER = webDriver;
        beforeStart();
    }


    @Override
    public List<WebElement> findElements(By by) {
        this.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
        slowDown();
        List<WebElement> elements = super.findElements(by);
        this.manage().timeouts().implicitlyWait(SeleniumProperties.getImplicitWaitTime(), TimeUnit.SECONDS);
        return elements;
    }

    private void beforeStart() {
        String browser = SeleniumProperties.getDriverProperty();
        if (browser.toLowerCase().equals("saucelabsios") || browser.toLowerCase().equals("saucelabsandroid")) {
            Long implicitTime = Long.parseLong(SeleniumProperties.getProperty("sauceLabs.implicitTime"));
            manage().timeouts().pageLoadTimeout(implicitTime, TimeUnit.SECONDS);
            manage().timeouts().implicitlyWait(implicitTime, TimeUnit.SECONDS);
        } else if(DriverManager.getDriverType().toLowerCase().equals("gridchrome") || DriverManager.getDriverType().toLowerCase().equals("griddriver")){
            manage().deleteAllCookies();
            manage().timeouts().pageLoadTimeout(SeleniumProperties.getImplicitWaitTime(), TimeUnit.SECONDS);
            manage().timeouts().implicitlyWait(SeleniumProperties.getImplicitWaitTime(), TimeUnit.SECONDS);
            // Set exact size since maximize() is broken for headless execution.
            // See: https://bugs.chromium.org/p/chromedriver/issues/detail?id=1901
            manage().window().setSize(new Dimension(1920,1080));
        } else {
            manage().deleteAllCookies();
            manage().timeouts().pageLoadTimeout(SeleniumProperties.getImplicitWaitTime(), TimeUnit.SECONDS);
            manage().timeouts().implicitlyWait(SeleniumProperties.getImplicitWaitTime(), TimeUnit.SECONDS);
            manage().window().maximize();
        }
    }

    private void slowDown() {
        // slow down to human speed. otherwise, test result is very sensitive to time
        try {
            Thread.sleep(350);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
