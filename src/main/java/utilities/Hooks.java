package utilities;

import Selenium.SeleniumLibrary;
import cucumber.api.Scenario;
import org.apache.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriverException;

public class Hooks extends SeleniumLibrary {
    Logger logger = Logger.getLogger(Hooks.class);
    Scenario scenario;

    public Hooks(Scenario scenario) {
        this.scenario = scenario;
    }

    public void testTearDown() {
        takeScreenshotOnFailure();
        driver.quit();
    }

    private void takeScreenshotOnFailure() {
        if(scenario.isFailed()) {
            try {
                //Take screenshot
                final byte[] screenshot = getDriver().getScreenshotAs(OutputType.BYTES);
                scenario.embed(screenshot, "image/png");
            } catch (final UnsupportedOperationException e) {
                logger.error(e.getMessage());
            } catch (final WebDriverException e) {
                scenario.write("WARNING: Failed to take screenshot with exception: " + e.getMessage());
            }
        }
    }
}
