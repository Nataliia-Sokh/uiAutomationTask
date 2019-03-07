package Selenium.Driver.Query;

import org.openqa.selenium.WebElement;


public class Below extends Query {
    
    public Below(WebElement parentElement) {
        super(parentElement);
    }

    public Below(String text) {
        super(text);
    }
}