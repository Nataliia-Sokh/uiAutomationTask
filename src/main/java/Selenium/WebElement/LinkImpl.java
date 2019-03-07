package Selenium.WebElement;

import Selenium.Driver.Query.Query;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class LinkImpl extends ElementImpl {

    private static final List<String> possibleTags = getPossibleTags();
    private static final Logger logger = Logger.getLogger(ButtonImpl.class.getName());

    public LinkImpl(By by) {
        super(by);
    }

    public LinkImpl(String visibleText) {
        super(possibleTags, visibleText);
    }

    public LinkImpl(String visibleTest, Query searchArea) {
        super(possibleTags, visibleTest, searchArea);
    }

    public LinkImpl(Query searchArea) {
        super(possibleTags, searchArea);
    }

    public LinkImpl(WebElement element) {
        super(element);
    }

    @Override
    protected List<WebElement> getElementsByVisibleText(List<String> elementTags, String visibleText) {
        waitUntilPageFinishLoading();
        List<WebElement> returnElements = new ArrayList<>();
        for (String elementTag : elementTags) {
            List<WebElement> links = getElements(elementTag);
            //Try to get the link by text
            returnElements.addAll(links.stream().filter(link -> link.getText().trim().toLowerCase().equals(visibleText.toLowerCase())).collect(Collectors.toList()));
            //Try to get the link by href
            if (returnElements.size() == 0) {
                for (WebElement link : links) {
                    String url = link.getAttribute("href");
                    if (url != null && url.contains(visibleText.toLowerCase())) {
                        returnElements.add(link);
                    }
                }
            }
        }

        if (returnElements.size() > 0) {
            return returnElements;
        } else {
            throw new NoSuchElementException("Unable to find Link with text: " + visibleText);
        }
    }

    @Override
    protected WebElement getElementByVisibleText(List<String> elementTags, String visibleText) {
        waitUntilPageFinishLoading();
        for (String elementTag : elementTags) {
            List<WebElement> links = getElements(elementTag);
            //Try to get the link by text
            for (WebElement link : links) {
                if (link.getText().toLowerCase().trim().equals(visibleText.toLowerCase())) {
                    return link;
                }
            }
            //Try to get the link by href
            for (WebElement link : links) {
                String url = link.getAttribute("href");
                if (url != null && url.contains(visibleText.toLowerCase())) {
                    return link;
                }
            }
        }

        throw new NoSuchElementException("Unable to find Link with text: " + visibleText);
    }

    public String getUrl() {
        return this.getAttribute("href");
    }

    private static List<String> getPossibleTags() {
        List<String> tags = new ArrayList<>();
        tags.add("a");

        return tags;
    }
}
