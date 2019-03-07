package Selenium.Driver.Query;

import org.openqa.selenium.WebElement;


public class ToTheLeftOf extends Query {

    public ToTheLeftOf(WebElement parentElement) {
        super(parentElement);
    }

    public ToTheLeftOf(String text) {
        super(text);
    }
}
