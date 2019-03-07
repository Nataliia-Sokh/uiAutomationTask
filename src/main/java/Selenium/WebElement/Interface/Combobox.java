package Selenium.WebElement.Interface;

import java.util.List;


public interface Combobox extends Element {

    void select(String option);
    void multiSelect(List<String> options);
}
