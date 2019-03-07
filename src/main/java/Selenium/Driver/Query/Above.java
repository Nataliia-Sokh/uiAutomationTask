package Selenium.Driver.Query;

import org.openqa.selenium.WebElement;


public class Above extends Query {

    public Above(WebElement parentElement) {
        super(parentElement);
    }

    public Above(String text) {
        super(text);
    }
}
