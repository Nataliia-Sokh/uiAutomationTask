package Selenium.WebElement;

import Selenium.Driver.Query.Query;
import Selenium.WebElement.Interface.NavigationBar;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;


public class NavigationBarImpl extends ElementImpl implements NavigationBar {

    private static final List<String> possibleTags = getPossibleTags();
    private static final Logger logger = Logger.getLogger(NavigationBarImpl.class.getName());

    public NavigationBarImpl(){
        super("nav");
    }

    public NavigationBarImpl(By by) {
        super(by);
    }

    public NavigationBarImpl(Query searchArea) {
        super(possibleTags, searchArea);
    }

    public NavigationBarImpl(WebElement element) {
        super(element);
    }

    @Override
    public void select(String optionName) {
        logger.info("Trying to select " + optionName + " from menu nav with id of: " + getId());
        WebElement navOption = getNavOption(this, optionName);
        click(navOption);
        logger.info(optionName + " selected");
    }

    @Override
    public void selectChild(String parentOption, String childOption) {
        logger.debug("Trying to hover over " + parentOption);
        WebElement parentOptionEl = getNavOption(this, parentOption);
        hover(parentOptionEl);
        logger.debug("Hovered over parent option: " + parentOption);
        logger.info("Trying to select child: " + childOption + " from menu parent: " + parentOption);
        WebElement navOption = getNavOption(parentOptionEl, childOption);
        click(navOption);
        logger.info(childOption + " selected");

    }

    private WebElement getNavOption(WebElement parentOption, String optionName) {
        WebElement returnOption = null;
        logger.debug("Trying to find option element: " + optionName);
        List<WebElement> options = parentOption.findElements(By.tagName("li"));
        for (WebElement option : options) {
            WebElement optionLink = option.findElement(By.tagName("a"));
            String currentOptionName = optionLink.getText().toLowerCase().trim();
            if(currentOptionName.equals(optionName.toLowerCase())) {
                returnOption = option;
                logger.debug("found: " + currentOptionName);
                break;
            }
        }

        assertTrue("Nav option " + optionName + " was not found.", returnOption != null);

        return returnOption;
    }

    private void click(WebElement element) {
        WebElement link = element.findElement(By.tagName("a"));
        link.click();
    }

    private static List<String> getPossibleTags() {
        List<String> tags = new ArrayList<>();
        tags.add("nav");
        tags.add("dic");

        return tags;
    }
}
