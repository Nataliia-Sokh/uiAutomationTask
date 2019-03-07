package Selenium.WebElement;

import Selenium.Driver.Query.Query;
import com.google.common.base.Strings;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class TextboxImpl extends ElementImpl {

    private static final List<String> possibleTags = getPossibleTags();
    private static final Logger logger = Logger.getLogger(TextboxImpl.class.getName());

    public TextboxImpl(By by) {
        super(by);
    }

    public TextboxImpl(String visibleText) {
        super(possibleTags, visibleText);
    }

    public TextboxImpl(String visibleTest, Query searchArea) {
        super(possibleTags, visibleTest, searchArea);
    }

    public TextboxImpl(Query searchArea) {
        super(possibleTags, searchArea);
    }

    public TextboxImpl(WebElement element) {
        super(element);
    }

    @Override
    protected List<WebElement> getElements(String elementTag) {

        logger.debug("Getting all the text boxes on the page.");
        List<WebElement> returnElements;

        if (elementTag.equals("input")) {
            List<WebElement> inputElements = driver.findElements(By.tagName(elementTag));
            returnElements = inputElements.stream().filter(element -> element.getAttribute("type").equals("text") ||
                    element.getAttribute("type").equals("password") || element.getAttribute("type").equals("url") ||
                    element.getAttribute("type").equals("email") || element.getAttribute("type").equals("tel") ||
                    element.getAttribute("type").equals("search") || element.getAttribute("type").equals("number")).collect(Collectors.toList());
        } else {
            returnElements = driver.findElements(By.tagName(elementTag));
        }

        if (returnElements != null && !returnElements.isEmpty()) {
            logger.debug("Got all the text boxes on the page with no issues.");
            return returnElements;
        } else {
            throw new NoSuchElementException("Unable to find any text boxes on the page");
        }
    }

    @Override
    protected List<WebElement> getElementsByVisibleText(List<String> elementTags, String visibleText){
        List<WebElement> returnElements = new ArrayList<>();
        try {
            return super.getElementsByVisibleText(elementTags, visibleText);
        } catch (NoSuchElementException e) {
            for (String elementTag : elementTags) {
                List<WebElement> elements = getElements(elementTag);
                //Get input elements by place holder text
                returnElements.addAll(elements.stream().filter(element -> element.getText().toLowerCase().equals(visibleText.toLowerCase()) || element.getAttribute("placeholder").toLowerCase().equals(visibleText.toLowerCase())).collect(Collectors.toList()));

                //Get input elements by label
                List<WebElement> labels = getElements("label");
                for (WebElement label : labels) {
                    String labelForAttribute = label.getAttribute("for");
                    if (!Strings.isNullOrEmpty(labelForAttribute) && label.getText().toLowerCase().trim().equals(visibleText.toLowerCase())) {
                        returnElements.add(driver.findElement(By.id(labelForAttribute)));
                    }
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
    protected WebElement getElementByVisibleText(List<String> elementTags, String visibleText){
        for (String elementTag : elementTags) {
            List<WebElement> elements = getElements(elementTag);
            //Get input elements by place holder text
            for (WebElement element : elements) {
                if (element.getText().toLowerCase().trim().equals(visibleText.toLowerCase()) || element.getAttribute("placeholder").toLowerCase().equals(visibleText.toLowerCase())) {
                    return element;
                }
            }

            //Get input elements by label
            List<WebElement> labels = getElements("label");
            for (WebElement label : labels) {
                String labelForAttribute = label.getAttribute("for");
                if (!Strings.isNullOrEmpty(labelForAttribute) && label.getText().toLowerCase().trim().equals(visibleText.toLowerCase())) {
                    return driver.findElement(By.id(labelForAttribute));
                }
            }
        }

        throw new NoSuchElementException("Unable to find element with text: " + visibleText);
    }

    private static List<String> getPossibleTags() {
        List<String> tags = new ArrayList<>();
        tags.add("input");

        return tags;
    }
}
