package Selenium.WebElement;

import Selenium.Driver.Query.Query;
import Selenium.WebElement.Interface.BooleanElement;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertTrue;

public class CheckboxImpl extends ElementImpl implements BooleanElement {

    private static final List<String> possibleTags = getPossibleTags();
    private static final Logger logger = Logger.getLogger(CheckboxImpl.class.getName());

    public CheckboxImpl(By by) {
        super(by);
    }

    public CheckboxImpl(String visibleText) {
        super(possibleTags, visibleText);
    }

    public CheckboxImpl(String visibleTest, Query searchArea) {
        super(possibleTags, visibleTest, searchArea);
    }

    public CheckboxImpl(Query searchArea) {
        super(possibleTags, searchArea);
    }

    public CheckboxImpl(WebElement element) {
        super(element);
    }

    @Override
    public void select() {
        logger.info("Selecting checkbox with id of: " + getId());
        if (!isSelected()) {
            click();
        }
        assertTrue("Checkbox not selected", this.isSelected());
        logger.info(getId() + " selected.");
    }

    @Override
    public void unSelect() {
        logger.info("Un-selecting checkbox with id of: " + getId());
        if (isSelected()) {
            click();
        }
        assertTrue("Checkbox is still selected", !this.isSelected());
        logger.info(getId() + " un-selected.");
    }

    @Override
    public void setState(String trueOrFalse) {
        boolean desiredStated = Boolean.parseBoolean(trueOrFalse);
        logger.info("Setting checkbox with id of: " + getId() + " to: " + trueOrFalse);
        if (isSelected() != desiredStated) {
            click();
            logger.info("Checkbox was set to: " + isSelected());
        } else {
            logger.info("Checkbox was already set to: " + desiredStated + " nothing was done");
        }
    }

    @Override
    protected List<WebElement> getElements(String elementTag) {
        //Get all the input elements
        List<WebElement> inputElements = driver.findElements(By.tagName(elementTag));
        //Filter all the inout elements to get all checkboxes
        List<WebElement> checkboxElements = inputElements.stream().filter(element -> element.getAttribute("type").equals("checkbox")).collect(Collectors.toList());

        if (checkboxElements != null) {
            return checkboxElements;
        } else {
            throw new NoSuchElementException("Unable to find any checkboxes on the page");
        }
    }

    @Override
    protected List<WebElement> getElementsByVisibleText(List<String> elementTags, String visibleText) {
        List<WebElement> returnElements = new ArrayList<>();

        for (String elementTag : elementTags) {
            List<WebElement> elements = getElements(elementTag);
            for (WebElement element : elements) {
                String labelText = getLabelText(element);
                if (visibleText.toLowerCase().equals(labelText)) {
                    returnElements.add(element);
                }
            }
        }

        if (returnElements.size() > 0) {
            return returnElements;
        } else {
            throw new NoSuchElementException("Unable to find element with text: " + visibleText);
        }
    }

    @Override
    protected WebElement getElementByVisibleText(List<String> elementTags, String visibleText) {
        for (String elementTag : elementTags) {
            List<WebElement> elements = getElements(elementTag);
            for (WebElement element : elements) {
                String labelText = getLabelText(element);
                if (visibleText.toLowerCase().equals(labelText)) {
                    return element;
                }
            }
        }

        throw new NoSuchElementException("Unable to find element with text: " + visibleText);
    }

    private String getLabelText(WebElement element) {
        List<WebElement> labels = driver.findElements(By.tagName("label"));
        for (WebElement label : labels) {
            if (label.getAttribute("for").equals(element.getAttribute("id"))){
                return label.getText().toLowerCase().trim();
            }
        }

        return null;
    }

    private static List<String> getPossibleTags() {
        List<String> tags = new ArrayList<>();
        tags.add("input");

        return tags;
    }
}
