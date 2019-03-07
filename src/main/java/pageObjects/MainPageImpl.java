package pageObjects;

import Selenium.SeleniumLibrary;
import Selenium.WebElement.ComboboxImpl;
import Selenium.WebElement.TableImpl;
import cucumber.api.DataTable;
import org.assertj.core.api.SoftAssertions;
import org.junit.Assert;
import stepDefinitions.GlobalSteps;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class MainPageImpl extends SeleniumLibrary {

    /**
     * Returns a SoftAssertions object to check things that you care about, but don't want to stop test
     * execution over if the assertion fails.
     *
     * @return The static SoftAssertions object from GlobalSteps to be checked on Scenario teardown.
     */
    public SoftAssertions softly() {
        return GlobalSteps.softly;
    }

    public void navigateMainPage(String page) {

        getDriver().navigate().to("file:///" + getTestPagePath(page).substring(1));
    }

    private static String getTestPagePath(String fileName) {
        ClassLoader classLoader = MainPageImpl.class.getClassLoader();
        String filePath = "TestPage/" + fileName;
        try {
            return classLoader.getResource(filePath).getPath();
        } catch (NullPointerException e) {
            throw new NullPointerException(fileName + " not found.");
        }
    }

    public void selectCheckbox(String checkboxToSelect) {
        checkbox(checkboxToSelect).select();
    }

    public void unselectCheckbox(String checkboxToUnSelect) {
        checkbox(checkboxToUnSelect).unSelect();
    }

    public void checkIfCheckboxSelected(String checkboxToCheck) {
        Assert.assertTrue("Checkbox " + checkboxToCheck + " is not selected", checkbox(checkboxToCheck).isSelected());
    }

    public void checkIfCheckboxNotSelected(String checkboxToCheck) {
        softly().assertThat(checkbox(checkboxToCheck).isSelected()).as("Checkbox " + checkboxToCheck + " is not selected");
    }

    public void selectOprtionFromMenu(String option, String menu) {
        ComboboxImpl cb = combobox(menu);
        cb.select(option);
    }

    public void selectMultipleOptionMenu(String menu, DataTable dataTable) {
        ComboboxImpl cbMultiple = new ComboboxImpl("Select Multiple");
        List<String> optionsToPick = dataTable.asList(String.class);
        List<String> options = new ArrayList<>();
        for (String option : optionsToPick) {
            options.add(option);
        }
        cbMultiple.multiSelect(options);
    }

    public void checkValueInTheTable(String columnToCheck, String rawToCheck, String expectedValue, String tableName) {
        TableImpl table = table(tableName);
        List<Map<String, String>> htmlTable = table.getAsMaps();

        String actualValue = null;

        for (Map<String, String> raw : htmlTable) {
            for (String value : raw.values()) {
                if (value.contains(rawToCheck)) {
                    actualValue = raw.get(columnToCheck);
                }
            }
        }
        Assert.assertTrue(actualValue.equals(expectedValue));
        System.out.println(htmlTable);
    }

    public void pressButton(String button) {
        button(button).click();
    }

    public void checkAllertMessage(String message) {
        try {
            alert().verifyMessage(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void acceptAlert() {
        alert().accept();
    }

    public void fillTextbox(String textField, String text) {
        textbox(textField).sendKeys(text);
    }

    public void checkTextbox(String textField, String text) {
        Assert.assertTrue("Text field doesn't contain data",!textbox(textField).getText().equals(text));
    }

    public void clickTheLink(String link) {
        link(link).click();
    }

    public void checkCurrecntURL(String url) {
        Assert.assertTrue("URL is wring ",driver.getCurrentUrl().equals(url));
    }

}



