package Selenium.WebElement.Interface;

import java.util.List;
import java.util.Map;


public interface Table extends Element {

    List<Map<String, String>> getAsMaps();
}
