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


public class RadioButtonImpl extends ElementImpl implements BooleanElement {

    private static final List<String> possibleTags = getPossibleTags();
    private static final Logger logger = Logger.getLogger(RadioButtonImpl.class.getName());

    public RadioButtonImpl(By by) {
        super(by);
    }

    public RadioButtonImpl(String visibleText) {
        super(possibleTags, visibleText);
    }

    public RadioButtonImpl(String visibleTest, Query searchArea) {
        super(possibleTags, visibleTest, searchArea);
    }

    public RadioButtonImpl(Query searchArea) {
        super(possibleTags, searchArea);
    }

    public RadioButtonImpl(WebElement element) {
        super(element);
    }

    @Override
    public void select() {
        logger.info("Selecting radio button with id of: " + getId());
        if (!isSelected()) {
            click();
        }
        logger.info(getId() + " selected.");
    }

    @Override
    public void unSelect() {
        logger.info("Un-selecting radio button with id of: " + getId());
        if (isSelected()) {
            click();
        }
        logger.info(getId() + " un-selected.");
    }

    @Override
    public void setState(String trueOrFalse) {
        boolean desiredStated = Boolean.parseBoolean(trueOrFalse);
        logger.info("Setting radio button with id of: " + getId() + " to: " + trueOrFalse);
        if (isSelected() != desiredStated) {
            click();
            logger.info("Radio button was set to: " + isSelected());
        } else {
            logger.info("Radio button was already set to: " + desiredStated + " nothing was done");
        }
    }

    @Override
    protected List<WebElement> getElements(String elementTag) {
        //Get all the input elements
        List<WebElement> inputElements = driver.findElements(By.tagName(elementTag));
        //Filter all the elements to get all checkboxes
        List<WebElement> radioBtnElements = inputElements.stream().filter(element -> element.getAttribute("type").equals("radio")).collect(Collectors.toList());

        if (radioBtnElements != null) {
            return radioBtnElements;
        } else {
            throw new NoSuchElementException("Unable to find any radio buttons on the page");
        }
    }

    private static List<String> getPossibleTags() {
        List<String> tags = new ArrayList<>();
        tags.add("input");

        return tags;
    }
}
