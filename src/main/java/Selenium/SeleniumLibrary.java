package Selenium;

import Selenium.Driver.DriverManager;
import Selenium.Driver.Query.*;
import Selenium.Driver.SharedDriver;
import Selenium.WebElement.*;
import org.apache.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import java.util.ArrayList;
import java.util.List;

public class SeleniumLibrary extends DriverManager {

    private static Logger logger;
    public static SharedDriver driver;

    public static void main(String[] args) {}

    public SeleniumLibrary() {
        logger = Logger.getLogger(SeleniumLibrary.class.getName());
        driver = getDriver();
    }

    /**
     * Initiates the WebDriver and Navigates to the given URL
     * @param url URL you want to navigate to
     */
    protected void load(String url) {
        logger.info("Navigating to " + url);
        try {
            getDriver().get(url);
        } catch (NoSuchSessionException e) {
            logger.info("Caught SessionNotFoundException, attempting to generate new browser session.");
            driver = forceNewDriver();
            getDriver().get(url);
        waitUntilPageFinishLoading();
        logger.info("Navigated to " + driver.getCurrentUrl());
    }}

    /**
     * Switch to the newest opened window
     */
    protected void switchToNewestWindow() {
        logger.debug("Switching window");
        int newestWindowIndex = driver.getWindowHandles().size() - 1;
        List<String> windowsHandles = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().window(windowsHandles.get(newestWindowIndex));
        logger.debug("Switched window to: " + driver.getTitle());
    }

    /**
     * Switch to the Parent (first opened) window
     */
    protected void switchToParentWindow() {
        logger.info("Switching to parent window");
        List<String> windowsHandles = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().window(windowsHandles.get(0));
        logger.info("Switched to parent window titled: " + driver.getTitle());
    }

    protected void waitUntilPageFinishLoading() {
        logger.debug("Waiting for page to load...");

        // slow down to human speed. otherwise, test result is very sensitive to time
        try {
            Thread.sleep(350);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Wait until the document is ready
        ExpectedCondition domIsReady = new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver driver) {
                try {
                    return (Boolean) ((JavascriptExecutor) driver).executeScript("return document.readyState === 'complete'");
                } catch (UnhandledAlertException e) {
                    logger.debug("A JavaScript alert was triggered");
                    return true;
                }
            }};

        waitUntil(domIsReady);

        logger.debug("Page is done loading.");
    }

    protected void press(Keys keys) {
        Actions actions = new Actions(driver);
        actions.sendKeys(keys).build().perform();
    }

    /**
     * Clicks on an webElement using JavaScript
     * @param by
     */
    protected void clickUsingJavaScript(final By by) {
        final JavascriptExecutor executor = driver;
        logger.debug("Clicking on webElement using JavaScript");

        ExpectedCondition elementIsNotStale = new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver ignored) {
                try {
                    List<WebElement> elements = getDriver().findElements(by);
                    WebElement element = elements.get(elements.size() - 1);
                    new Actions(getDriver()).moveToElement(element).perform();
                    executor.executeScript("arguments[0].click()", element);
                    return true;
                } catch (Exception expected) {
                    return false;
                }
            }
        };

        waitUntil(elementIsNotStale);
    }

    //Getters

    //Button Element
    protected ButtonImpl button(By by) {
        return new ButtonImpl(by);
    }
    protected ButtonImpl button(WebElement element) {
        return new ButtonImpl(element);
    }
    protected ButtonImpl button(String visibleText) {
        return new ButtonImpl(visibleText);
    }
    protected ButtonImpl button(String visibleText, Query query) {
        return new ButtonImpl(visibleText, query);
    }
    protected ButtonImpl button(Query query) {
        return new ButtonImpl(query);
    }

    //Checkbox Element
    protected CheckboxImpl checkbox(By by) {
        return new CheckboxImpl(by);
    }
    protected CheckboxImpl checkbox(WebElement element) {
        return new CheckboxImpl(element);
    }
    protected CheckboxImpl checkbox(String visibleText) {
        return new CheckboxImpl(visibleText);
    }
    protected CheckboxImpl checkbox(String visibleText, Query query) {
        return new CheckboxImpl(visibleText, query);
    }
    protected CheckboxImpl checkbox(Query query) {
        return new CheckboxImpl(query);
    }

    //Combobox Element
    protected ComboboxImpl combobox(By by) {
        return new ComboboxImpl(by);
    }
    protected ComboboxImpl combobox(WebElement element) {
        return new ComboboxImpl(element);
    }
    protected ComboboxImpl combobox(String visibleText) {
        return new ComboboxImpl(visibleText);
    }
    protected ComboboxImpl combobox(Query query) {
        return new ComboboxImpl(query);
    }

    //Link Element
    protected LinkImpl link(By by) {
        return new LinkImpl(by);
    }
    protected LinkImpl link(WebElement element) {
        return new LinkImpl(element);
    }
    protected LinkImpl link(String visibleText) {
        return new LinkImpl(visibleText);
    }
    protected LinkImpl link(String visibleText, Query query) {
        return new LinkImpl(visibleText, query);
    }
    protected LinkImpl link(Query query) {
        return new LinkImpl(query);
    }

    //Navigation Bar Element
    protected NavigationBarImpl navigationBar(By by) {
        return new NavigationBarImpl(by);
    }
    protected NavigationBarImpl navigationBar(WebElement element) {
        return new NavigationBarImpl(element);
    }
    protected NavigationBarImpl navigationBar(Query query) {
        return new NavigationBarImpl(query);
    }
    protected NavigationBarImpl navigationBar() {
        return new NavigationBarImpl();
    }

    //Radio Element
    protected RadioButtonImpl radioButton(By by) {
        return new RadioButtonImpl(by);
    }
    protected RadioButtonImpl radioButton(WebElement element) {
        return new RadioButtonImpl(element);
    }
    protected RadioButtonImpl radioButton(String visibleText) {
        return new RadioButtonImpl(visibleText);
    }
    protected RadioButtonImpl radioButton(String visibleText, Query query) {
        return new RadioButtonImpl(visibleText, query);
    }
    protected RadioButtonImpl radioButton(Query query) {
        return new RadioButtonImpl(query);
    }

    //Table Element
    protected TableImpl table(By by) {
        return new TableImpl(by);
    }
    protected TableImpl table(WebElement element) {
        return new TableImpl(element);
    }
    protected TableImpl table(String visibleText) {
        return new TableImpl(visibleText);
    }
    protected TableImpl table(Query query) {
        return new TableImpl(query);
    }

    //Textbox Element
    protected TextboxImpl textbox(By by) {
        return new TextboxImpl(by);
    }
    protected TextboxImpl textbox(WebElement element) {
        return new TextboxImpl(element);
    }
    protected TextboxImpl textbox(String visibleText) {
        return new TextboxImpl(visibleText);
    }
    protected TextboxImpl textbox(String visibleText, Query query) {
        return new TextboxImpl(visibleText, query);
    }
    protected TextboxImpl textbox(Query query) {
        return new TextboxImpl(query);
    }

    //Text
    protected TextImpl text(By by) {
        return new TextImpl(by);
    }
    protected TextImpl text(String text) {
        return new TextImpl(text);
    }
    protected TextImpl text(String text, Query searchArea) {
        return new TextImpl(text, searchArea);
    }

    //Search Queries
    protected ToTheRightOf toTheRightOf(WebElement element) {
        return new ToTheRightOf(element);
    }
    protected ToTheLeftOf toTheLeftOf(WebElement element) {
        return new ToTheLeftOf(element);
    }
    protected Below below(WebElement element) {
        return new Below(element);
    }
    protected Above above(WebElement element) {
        return new Above(element);
    }
    protected ToTheRightOf toTheRightOf(String text) {
        return new ToTheRightOf(text);
    }
    protected ToTheLeftOf toTheLeftOf(String text) {
        return new ToTheLeftOf(text);
    }
    protected Below below(String text) {
        return new Below(text);
    }
    protected Above above(String text) {
        return new Above(text);
    }

    //Alert
    protected AlertImpl alert() {
        return new AlertImpl();
    }
}
