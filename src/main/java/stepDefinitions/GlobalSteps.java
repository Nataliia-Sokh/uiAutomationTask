package stepDefinitions;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import utilities.Hooks;
import org.assertj.core.api.SoftAssertions;


public class GlobalSteps {

    public static SoftAssertions softly;

    @Before
    public void beforeScenario(Scenario scenario) {
        softly = new SoftAssertions();
    }

    @After
    public void afterScenario(Scenario scenario) {
        softly.assertAll();
        new Hooks(scenario).testTearDown();
    }

}
