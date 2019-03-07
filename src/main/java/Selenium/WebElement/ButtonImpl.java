package Selenium.WebElement;

import Selenium.Driver.Query.Query;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class ButtonImpl extends ElementImpl {

    private static final List<String> possibleTags = getPossibleTags();
    private static final Logger logger = Logger.getLogger(ButtonImpl.class.getName());

    public ButtonImpl(By by) {
        super(by);
    }

    public ButtonImpl(String visibleText) {
        super(possibleTags, visibleText);
    }

    public ButtonImpl(String visibleTest, Query searchArea) {
        super(possibleTags, visibleTest, searchArea);
    }

    public ButtonImpl(Query searchArea) {
        super(possibleTags, searchArea);
    }

    public ButtonImpl(WebElement element) {
        super(element);
    }

    @Override
    protected List<WebElement> getElements(String elementTag) {

        logger.debug("Getting all the buttons on the page.");
        List<WebElement> returnElements;

        if (elementTag.equals("input")) {
            List<WebElement> inputElements = driver.findElements(By.tagName(elementTag));
            returnElements = inputElements.stream().filter(element -> element.getAttribute("type").equals("submit") || element.getAttribute("type").equals("button") || element.getAttribute("type").equals("reset") ).collect(Collectors.toList());
        } else {
            returnElements = driver.findElements(By.tagName(elementTag));
        }

        logger.debug("Got all the button on the page with no issues.");
        return returnElements;
    }

    private static List<String> getPossibleTags() {
        List<String> tags = new ArrayList<>();
        tags.add("button");
        tags.add("a");
        tags.add("input");
        tags.add("div");

        return tags;
    }
}
