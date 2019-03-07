package Selenium.WebElement;

import Selenium.Driver.Query.Query;
import Selenium.WebElement.Interface.Combobox;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;



public class ComboboxImpl extends ElementImpl implements Combobox {

    private static final List<String> possibleTags = getPossibleTags();
    private static final Logger logger = Logger.getLogger(ComboboxImpl.class.getName());
    private Select select;

    public ComboboxImpl(By by) {
        super(by);
        select = new Select(this);
    }

    public ComboboxImpl(String visibleText) {
        super(possibleTags, visibleText);
        select = new Select(this);
    }

    public ComboboxImpl(Query searchArea) {
        super(possibleTags, searchArea);
        select = new Select(this);
    }

    public ComboboxImpl(WebElement element) {
        super(element);
    }

    @Override
    public void select(String option) {
        //Select option
        String optionSelected = selectOption(option);

        //Make sure option was selected
        assertEquals(option + " was not selected", optionSelected, select.getFirstSelectedOption().getText());
    }

    private void select(String optionGroup, String option) {
        //Get option groups
        List<WebElement> optionGroupEls = this.findElements(By.tagName("optgroup"));
        for (WebElement optGroup : optionGroupEls) {
            //Get the option group you are looking for
            if (optGroup.getAttribute("label").equals(optionGroup)) {
                List<WebElement> groupOptions = optGroup.findElements(By.tagName("option"));
                for (WebElement groupOption : groupOptions) {
                    if (groupOption.getText().trim().toLowerCase().equals(option.toLowerCase())) {
                        String optionValue = groupOption.getAttribute("value");
                        select.selectByValue(optionValue);
                        break;
                    }
                }
                break;
            }
        }
    }

    @Override
    public void multiSelect(List<String> options) {
        List<String> expectedSelectedOptions = new ArrayList<>();

        //Select all the options
        for (String option : options) {
            String selectedOption = selectOption(option);
            expectedSelectedOptions.add(selectedOption);
        }

        //Get all the selected options as strings
        List<String> actualSelectedOptions = select.getAllSelectedOptions().stream().map(WebElement::getText).collect(Collectors.toList());

        //Make sure all options where selected
        assertEquals("Not all options or more than the expected options were selected: " + actualSelectedOptions, expectedSelectedOptions, actualSelectedOptions);
    }

    /**
     * Selects an option from a select web element
     * @param option Visible text
     */
    private String selectOption(String option) {
        String optionToSelect;
        if (option.split(",").length > 1) {
            String parent = option.split(",")[0].trim();
            optionToSelect = option.split(",")[1].trim();

            select(parent, optionToSelect);
            return optionToSelect;
        }
        logger.info("Selecting " + option + " from combobox with id of: " + getId());
        List<WebElement> selectOptions = findElements(By.tagName("option"));
        for (WebElement selectOption : selectOptions) {
            if (selectOption.getText().trim().toLowerCase().equals(option.toLowerCase())) {
                String optionValue = selectOption.getAttribute("value");
                select.selectByValue(optionValue);
                break;
            }
        }
        logger.info(option + " selected");
        waitUntilPageFinishLoading();
        return option;
    }

    @Override
    protected List<WebElement>  getElementsByVisibleText(List<String> elementTags, String visibleText) {
        List<WebElement> returnElements = new ArrayList<>();

        for (String elementTag : elementTags) {
            List<WebElement> comboboxes = getElements(elementTag);
            List<WebElement> labels = driver.findElements(By.tagName("label"));
            for (WebElement combobox : comboboxes) {
                String selectId = combobox.getAttribute("id");
                //Try to get the combobox by label
                for (WebElement label : labels) {
                    String labelForAttribute = label.getAttribute("for");
                    String labelText = label.getText().trim().toLowerCase();
                    if (labelForAttribute.equals(selectId) && labelText.equals(visibleText.toLowerCase())) {
                        returnElements.add(combobox);
                    }
                }
                //Try to get the combobox by option
                if (returnElements.size() == 0) {
                    Select select = new Select(combobox);
                    List<WebElement> comboboxOptions = select.getOptions();
                    returnElements.addAll(comboboxOptions.stream().filter(comboboxOption -> comboboxOption.getText().toLowerCase().trim().equals(visibleText.toLowerCase())).map(comboboxOption -> combobox).collect(Collectors.toList()));
                }
            }
        }

        if (returnElements.size() > 0) {
            return returnElements;
        } else {
            throw new NoSuchElementException("Unable to find ComboBox with text: " + visibleText);
        }
    }

    @Override
    protected WebElement getElementByVisibleText(List<String> elementTags, String visibleText) {
        for (String elementTag : elementTags) {
            List<WebElement> comboboxes = getElements(elementTag);
            List<WebElement> labels = driver.findElements(By.tagName("label"));
            for (WebElement combobox : comboboxes) {
                String selectId = combobox.getAttribute("id");
                //Try to get the combobox by label
                for (WebElement label : labels) {
                    String labelForAttribute = label.getAttribute("for");
                    String labelText = label.getText().trim().toLowerCase();
                    if (labelForAttribute.equals(selectId) && labelText.equals(visibleText.toLowerCase())) {
                        return combobox;
                    }
                }
                //Try to get the combobox by option
                Select select = new Select(combobox);
                List<WebElement> comboboxOptions = select.getOptions();
                for (WebElement option : comboboxOptions) {
                    if (option.getText().toLowerCase().trim().equals(visibleText.toLowerCase())) {
                        return combobox;
                    }
                }
            }
        }

        throw new NoSuchElementException("Unable to find ComboBox with text: " + visibleText);
    }



    private static List<String> getPossibleTags() {
        List<String> tags = new ArrayList<>();
        tags.add("select");

        return tags;
    }
}
