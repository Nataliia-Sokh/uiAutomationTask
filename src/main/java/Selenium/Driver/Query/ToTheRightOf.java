package Selenium.Driver.Query;

import org.openqa.selenium.WebElement;

public class ToTheRightOf extends Query {

    public ToTheRightOf(WebElement parentElement) {
        super(parentElement);
    }

    public ToTheRightOf(String text) {
        super(text);
    }
}
