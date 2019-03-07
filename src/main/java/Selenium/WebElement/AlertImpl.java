package Selenium.WebElement;

import Selenium.SeleniumLibrary;
import org.apache.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.security.Credentials;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.junit.Assert.assertEquals;

public class AlertImpl extends SeleniumLibrary implements Alert {

    private static final Logger logger = Logger.getLogger(AlertImpl.class.getName());
    private Alert alert;

    /**
     * Get the actual message from the JavaScript alert and compares it with the expected message
     * @param expectedMessage String with the message you expect to get from the alert
     * @throws Exception in case there is an issue getting the alert from the page
     */
    public void verifyMessage(String expectedMessage) throws Exception {
        waitForAlert();
        logger.info("Verifying javascript alert message");
        String actualMessage = alert.getText();
        assertEquals("Alert message does not match", expectedMessage, actualMessage);
        alert.dismiss();
        switchToParentWindow();
        waitUntilPageFinishLoading();
        logger.info("Verified javascript alert message: " + expectedMessage);
    }

    public boolean isDisplayed() {
        try {
            logger.info("Checking if javascript alert is displayed");
            waitUntil(ExpectedConditions.alertIsPresent(), 1);
            logger.info("Alert was displayed");
            return true;
        } catch (TimeoutException e) {
            logger.info("Alert was not displayed");
            return false;
        }
    }

    @Override
    public void dismiss() {
        waitForAlert();
        logger.info("Dismissing javascript alert");
        alert.dismiss();
        logger.info("Alert dismissed");
    }

    @Override
    public void accept() {
        waitForAlert();
        logger.info("Accepting javascript alert");
        alert.accept();
        logger.info("Alert accepted");
    }

    @Override
    public String getText() {
        waitForAlert();
        logger.info("Getting javascript alert text");
        String alertText = alert.getText();
        logger.info("Got alert text: " + alertText);
        return alertText;
    }

    @Override
    public void sendKeys(String s) {
        waitForAlert();
        logger.info("Writing " + s + " on javascript alert");
        alert.sendKeys(s);
        logger.info("Wrote successfully");
    }

    @Override
    public void setCredentials(Credentials credentials) {
        waitForAlert();
        logger.info("Setting credentials on javascript alert");
        alert.setCredentials(credentials);
        logger.info("Credentials set");
    }

    @Override
    public void authenticateUsing(Credentials credentials) {
        waitForAlert();
        logger.info("Authenticating javascript alert");
        alert.authenticateUsing(credentials);
        logger.info("Alert authenticated");
    }

    private void waitForAlert() {
        if (this.alert == null) {
            try {
                logger.debug("Waiting javascript alert to appear on the screen");
                waitUntil(ExpectedConditions.alertIsPresent());
                this.alert = driver.switchTo().alert();
                waitUntilPageFinishLoading();
                logger.info("Alert appeared on the screen");
            } catch (TimeoutException e) {
                throw new TimeoutException("Alert never appeared on the screen");
            }
        }
    }
}
