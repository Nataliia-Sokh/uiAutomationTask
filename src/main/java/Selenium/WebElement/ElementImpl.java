package Selenium.WebElement;

import Selenium.Driver.Query.Query;
import Selenium.Driver.Query.Search;
import Selenium.Driver.SharedDriver;
import Selenium.SeleniumLibrary;
import Selenium.SeleniumProperties;
import com.google.common.base.Strings;
import org.apache.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.internal.Coordinates;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.internal.WrapsDriver;
import org.openqa.selenium.internal.WrapsElement;
import org.openqa.selenium.support.ui.ExpectedCondition;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

class ElementImpl extends SeleniumLibrary implements WebElement, WrapsElement, WrapsDriver, Locatable {

    private String className = new Exception().getStackTrace()[1].getClassName();
    private Logger logger = Logger.getLogger(className);
    protected SharedDriver driver = getDriver();
    protected By by;
    private List<String> possibleTags;
    private String visibleText;
    private Query query;
    private WebElement element;

    /**
     * Returns the first element found by the specified Tag name
     * @param tag tag name
     */
    ElementImpl(String tag) {
        this.by = By.tagName(tag);
    }

    /**
     * Returns the first element found by the specified By locator
     * @param by By locator
     */
    ElementImpl(By by) {
        this.by = by;
    }

    ElementImpl(List<String> possibleTags, String visibleText) {
        this.possibleTags = possibleTags;
        this.visibleText = visibleText;
    }

    ElementImpl(List<String> possibleTags, String visibleText, Query query) {
        this.possibleTags = possibleTags;
        this.visibleText = visibleText;
        this.query = query;
    }

    ElementImpl(List<String> possibleTags, Query query) {
        this.possibleTags = possibleTags;
        this.query = query;
    }

    ElementImpl(By by, Query query) {
        this.by = by;
        this.query = query;
    }

    ElementImpl(WebElement element) {
        this.element = element;
    }

    protected WebElement getElement() {
        waitUntilPageFinishLoading();
        //If the element is already declared try to use that...
        if (this.element != null) {
            try {
                if (this.element.isDisplayed()) {
                    return this.element;
                }
            } catch (Exception e) {
                logger.debug("Issue when trying to reused the same element... getting the element again");
            }
        }

        //Try to get the element using xpath first
        WebElement returnElement = getElementByXpath();
        if (returnElement != null) {
            return returnElement;
        }

        //If you can't use that because of any reason or element is not defined try to get a new element
        if (this.visibleText != null && this.query == null && this.possibleTags != null) {
            this.element =  getElementByVisibleText(possibleTags, visibleText);
        } else if (this.by != null && this.query != null){
            this.element =  getElementByAndSearchArea(this.by, this.query);
        } else if(this.by != null) {
            this.element =  getDriver().findElement(this.by);
        } else if (this.visibleText != null && this.query != null) {
            this.element =  getElementByVisibleTextAndSearchArea(possibleTags, visibleText, query);
        }  else if (this.possibleTags != null && this.query != null) {
            this.element =  getElementBySearchArea(possibleTags, query);
        }

        return this.element;
    }

    private WebElement getElementByXpath() {
        //If by is set up we can use that to get the element.
        if (by == null) {
            try {
                //Set the selenium timeout to 0 to save time
                getDriver().manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
                //Create xpath that ignores case and normalizes spaces
                String xpath = "//*[contains(translate(normalize-space(text()),'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'),'"+visibleText.toLowerCase()+"')]";
                //Get the element
                WebElement element = getDriver().findElement(By.xpath(xpath));
                //Some times the text of the element gets embedded inside span elements... so we have to loop out of it
                while (element.getTagName().equals("span")) {
                    xpath += "/..";
                    element = getDriver().findElement(By.xpath(xpath));
                }

                if (possibleTags.contains(element.getTagName())) {
                    return element;
                }
            } catch (Exception e) {
                logger.info(e.getMessage());
            }
        }

        getDriver().manage().timeouts().implicitlyWait(SeleniumProperties.getImplicitWaitTime(), TimeUnit.SECONDS);
        return null;
    }

    private WebElement getElementByAndSearchArea(By by, Query query) {
        List<WebElement> elements = driver.findElements(by);
        Search search = new Search();
        WebElement element = search.findElement(elements, query);
        if (element != null) {
            return element;
        }

        throw new NoSuchElementException("Unable to find element near the element with getId() of: " + query.getElementId());
    }

    protected List<WebElement> getElementsByVisibleText(List<String> elementTags, String visibleText) {
        List<WebElement> returnElements = new ArrayList<>();
        for (String elementTag : elementTags) {
            List<WebElement> elements = getElements(elementTag);
            for (WebElement element : elements) {
                String valueAttribute;
                try {
                    valueAttribute = element.getAttribute("value").toLowerCase();
                } catch (NullPointerException e) {
                    valueAttribute = null;
                }
                if (Strings.isNullOrEmpty(valueAttribute)) {
                    if (element.getText().toLowerCase().equals(visibleText.toLowerCase())) {
                        returnElements.add(element);
                    }
                } else {
                    if (element.getText().toLowerCase().equals(visibleText.toLowerCase()) || valueAttribute.equals(visibleText.toLowerCase())) {
                        returnElements.add(element);
                    }
                }
            }
        }

        if(returnElements.size() > 0) {
            return returnElements;
        } else {
            throw new NoSuchElementException("Unable to find element with text: " + visibleText);
        }
    }

    protected WebElement getElementByVisibleText(List<String> elementTags, String visibleText) {
        for (String elementTag : elementTags) {
            List<WebElement> elements = getElements(elementTag);
            for (WebElement element : elements) {
                String valueAttribute;
                try {
                    valueAttribute = element.getAttribute("value").toLowerCase();
                } catch (Exception e) {
                    valueAttribute = null;
                }
                try {
                    if (Strings.isNullOrEmpty(valueAttribute)) {
                        if (element.getText().toLowerCase().equals(visibleText.toLowerCase())) {
                            return element;
                        }
                    } else {
                        if (element.getText().toLowerCase().equals(visibleText.toLowerCase()) || valueAttribute.equals(visibleText.toLowerCase())) {
                            return element;
                        }
                    }
                } catch (Exception e){}
            }
        }

        throw new NoSuchElementException("Unable to find element with text: " + visibleText);
    }

    private WebElement getElementBySearchArea(List<String> elementTags, Query query) {
        Search search = new Search();
        for (String elementTag : elementTags) {
            List<WebElement> elements = getElements(elementTag);
            WebElement element = search.findElement(elements, query);
            if (element != null) {
                return element;
            }
        }

        throw new NoSuchElementException("Unable to find element near the element with getId() of: " + query.getElementId());
    }

    private WebElement getElementByVisibleTextAndSearchArea(List<String> elementTags, String visibleText, Query query) {
        Search search = new Search();
        List<WebElement> elements = getElementsByVisibleText(elementTags, visibleText);
        WebElement element = search.findElement(elements, query);
        if (element != null) {
            return element;
        }

        throw new NoSuchElementException("Unable to find element with text: " + visibleText);
    }

    protected List<WebElement> getElements(String elementTag) {
        return driver.findElements(By.tagName(elementTag));
    }

    String getId() {
        if (!Strings.isNullOrEmpty(visibleText)) {
            return visibleText;
        } else if (by != null) {
            return by.toString();
        } else {
            return this.getAttribute("getId()");
        }
    }

    void hover(WebElement element) {
        logger.debug("Hovering over: " + getId() + " element");
        Actions actions = new Actions(getDriver());
        actions.moveToElement(element).build().perform();
        logger.debug("Hovered over: " + getId());
    }

    @Override
    public void click() {
        logger.debug("Trying to click on " + getId());
        getElement().click();
        logger.debug("Clicked on " + getId());
        waitUntilPageFinishLoading();
    }

    @Override
    public void submit() {
        logger.debug("Trying to submit " + getId());
        getElement().submit();
        logger.debug("Submitted " + getId());
        waitUntilPageFinishLoading();
    }

    @Override
    public void sendKeys(CharSequence... charSequences) {
        getElement().clear();
        logger.debug("Trying to type " + charSequences + " to " + getId());
        getElement().sendKeys(charSequences);
        logger.debug("Typed " + charSequences + " to " + getId());
        waitUntilPageFinishLoading();
    }

    @Override
    public void clear() {
        logger.debug("Trying to clear " + getId());
        getElement().clear();
        logger.debug("Cleared " + getId());
        waitUntilPageFinishLoading();
    }

    @Override
    public String getTagName() {
        logger.debug("Trying to get Tag Name for " + getId());
        String tagName = getElement().getTagName();
        logger.debug("Got Tag Name for " + getId() + " = " + tagName);
        return tagName;
    }

    @Override
    public String getAttribute(String s) {
        logger.debug("Trying to get element " + s + " attribute");
        String attributeValue = getElement().getAttribute(s);
        logger.debug("Got " + s + " attribute value = " + attributeValue);
        return attributeValue;
    }

    @Override
    public boolean isSelected() {
        logger.debug("Trying to check if element " + getId() + " is selected");
        boolean isSelected = getElement().isSelected();
        logger.debug("Element" + getId() + " is selected? " + isSelected);
        return isSelected;
    }

    @Override
    public boolean isEnabled() {
        logger.debug("Trying to check if element " + getId() + " is enabled");
        boolean isEnabled = getElement().isEnabled();
        logger.debug("Element" + getId() + " is enabled? " + isEnabled);
        return isEnabled;
    }

    @Override
    public String getText() {
        logger.debug("Trying to get text for " + getId());
        String text = getElement().getText().replace("\n", "");
        logger.debug("Got text for " + getId() + "text= " + text);
        return text;
    }

    @Override
    public List<WebElement> findElements(By by) {
        logger.debug("Trying to find all elements using " + by.toString());
        return getElement().findElements(by);
    }

    @Override
    public WebElement findElement(By by) {
        logger.debug("Trying to find all elements using " + by.toString());
        return getElement().findElement(by);
    }

    @Override
    public boolean isDisplayed() {
        logger.debug("Checking if element exist and is displayed using " + by);

        ExpectedCondition elementIsPresent = new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver driver) {
                try {
                    driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
                    driver.findElement(by);
                    driver.manage().timeouts().implicitlyWait(SeleniumProperties.getImplicitWaitTime(), TimeUnit.SECONDS);
                    return true;
                } catch (Exception e) {
                    return false;
                }
            }};

        try {
            boolean isDisplayed;
            if (by != null) {
                waitUntil(elementIsPresent, 2);
                isDisplayed = driver.findElement(by).isDisplayed();
            } else {
                isDisplayed = getElement().isDisplayed();
            }
            logger.debug("Element " + getId() + " is displayed? " + isDisplayed);
            return isDisplayed;
        } catch (Exception e) {
            logger.debug("Element does not exist");
            return false;
        }

    }

    @Override
    public Point getLocation() {
        logger.debug("Trying to get location of element" + getId());
        Point point = getElement().getLocation();
        logger.debug("Location for " + getId() + " = " + point);
        return point;
    }

    @Override
    public Dimension getSize() {
        logger.debug("Trying to get size of element " + getId());
        Dimension dimension = getElement().getSize();
        logger.debug("Size for " + getId() + " = " + dimension);
        return dimension;
    }

    @Override
    public Rectangle getRect() {
        logger.debug("Trying to get Rectangle of element " + getId());
        Rectangle rectangle = getElement().getRect();
        logger.debug("Rectangle for " + getId() + " = " + rectangle);
        return rectangle;
    }

    @Override
    public String getCssValue(String s) {
        logger.debug("Trying to get " + s + " CSS value of element " + getId());
        String css = getElement().getCssValue(s);
        logger.debug(s + "CSS Value for " + getId() + " = " + css);
        return css;
    }

    @Override
    public <X> X getScreenshotAs(OutputType<X> outputType) throws WebDriverException {
        logger.debug("Taking Screenshot");
        return driver.getScreenshotAs(outputType);
    }

    @Override
    public Coordinates getCoordinates() {
        return null;
    }

    @Override
    public WebDriver getWrappedDriver() {
        return null;
    }

    @Override
    public WebElement getWrappedElement() {
        return null;
    }
}
