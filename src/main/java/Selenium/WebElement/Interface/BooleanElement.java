package Selenium.WebElement.Interface;


public interface BooleanElement extends Element {

    void select();

    void unSelect();

    void setState(String trueOrFalse);
}
