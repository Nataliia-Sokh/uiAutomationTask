package stepDefinitions;

import cucumber.api.java8.En;
import pageObjects.MainPageImpl;


public class MainPageSampleStepsDefs implements En {

    public MainPageSampleStepsDefs() {


        MainPageImpl mainPage = new MainPageImpl();


        When("^I navigate to the main page \"([^\"]*)\"$", mainPage::navigateMainPage);
        Then("^I select checkbox \"([^\"]*)\"$", mainPage::selectCheckbox);
        Then("^I un-select checkbox \"([^\"]*)\"$", mainPage::unselectCheckbox);
        Then("^I check that checkbox \"([^\"]*)\" is selected$", mainPage::checkIfCheckboxSelected);
        Then("^I check that checkbox \"([^\"]*)\" is not selected$", mainPage::checkIfCheckboxNotSelected);
        Then("^I select the option \"([^\"]*)\" from the \"([^\"]*)\" menu$", mainPage::selectOprtionFromMenu);
        Then("^I select multiple options from the  \"([^\"]*)\"$", mainPage::selectMultipleOptionMenu);
        Then("^I check that \"([^\"]*)\" for \"([^\"]*)\" is \"([^\"]*)\" in the \"([^\"]*)\" table$", mainPage::checkValueInTheTable);
        Then("^I press \"([^\"]*)\" button$", mainPage::pressButton);
        Then("^I check I can see an alert message \"([^\"]*)\"$", mainPage::checkAllertMessage);
        Then("^I accept alert message$", mainPage::acceptAlert);
        Then("^I fill out \"([^\"]*)\" with \"([^\"]*)\"$", mainPage::fillTextbox);
        Then("^I check the \"([^\"]*)\" text field has the \"([^\"]*)\" value$", mainPage::checkTextbox);
        Then("^I click  the \"([^\"]*)\" link$", mainPage::clickTheLink);
        Then("^I check if url is \"([^\"]*)\"$", mainPage::checkCurrecntURL);

    }
}
