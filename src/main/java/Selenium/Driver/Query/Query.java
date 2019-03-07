package Selenium.Driver.Query;

import Selenium.WebElement.TextImpl;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;

public class Query {

    private int x;
    private int y;
    private String elementId;

    public Query (WebElement parentElement) {
        Point location = parentElement.getLocation();
        this.x = location.getX();
        this.y = location.getY();
        this.elementId = parentElement.getAttribute("id");
    }

    public Query (String text) {
        TextImpl parentElement = new TextImpl(text);
        Point location = parentElement.getLocation();
        this.x = location.getX();
        this.y = location.getY();
        this.elementId = parentElement.getAttribute("id");
    }

    int getX() {
        return x;
    }

    int getY() {
        return y;
    }

    public String getElementId() {
        return elementId;
    }
}
