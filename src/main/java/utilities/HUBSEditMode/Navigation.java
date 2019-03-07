package utilities.HUBSEditMode;

import Selenium.SeleniumLibrary;
import org.apache.log4j.Logger;

public class Navigation extends SeleniumLibrary {

    private Logger logger;

    public Navigation() {
        logger = Logger.getLogger(Navigation.class);
    }

    public String getWindowHandle() {
        return getDriver().getWindowHandle();
    }

    public void closeNewTabAndSwitchToOriginal(String originalHandle) {
        for(String handle : getDriver().getWindowHandles()) {
            if (!handle.equals(originalHandle)) {
                getDriver().switchTo().window(handle);
                getDriver().close();
            }
        }
        getDriver().switchTo().window(originalHandle);
    }
}
