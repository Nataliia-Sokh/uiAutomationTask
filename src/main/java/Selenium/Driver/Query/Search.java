package Selenium.Driver.Query;

import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;

import java.util.List;


public class Search {

    public WebElement findElement(List<WebElement> elements, Query query) {
        String className = query.getClass().getSimpleName().toLowerCase();
        switch (className) {
            case "below":
                for (WebElement element : elements) {
                    Point elementLocation = element.getLocation();
                    Boolean isWithinRange = checkLocationRange(elementLocation.getX(), query.getX());
                    if (elementLocation.getY() > query.getY() && isWithinRange) {
                        return element;
                    }
                }
                break;
            case "above":
                for (WebElement element : elements) {
                    Point elementLocation = element.getLocation();
                    Boolean isWithinRange = checkLocationRange(elementLocation.getX(), query.getX());
                    if (elementLocation.getY() < query.getY()&& isWithinRange) {
                        return element;
                    }
                }
                break;
            case "totherightof":
                for (WebElement element : elements) {
                    Point elementLocation = element.getLocation();
                    Boolean isWithinRange = checkLocationRange(elementLocation.getY(), query.getY());
                    if (elementLocation.getX() > query.getX()&& isWithinRange) {
                        return element;
                    }
                }
                break;
            case "totheleftof":
                for (WebElement element : elements) {
                    Point elementLocation = element.getLocation();
                    Boolean isWithinRange = checkLocationRange(elementLocation.getY(), query.getY());
                    if (elementLocation.getX() < query.getX()&& isWithinRange) {
                        return element;
                    }
                }
                break;

        }

        return null;
    }

    private boolean checkLocationRange(int value, int bound) {
        int lowerBound = bound - 5;
        int upperBound = bound + 5;

        return lowerBound <= value && value <= upperBound;
    }
}
