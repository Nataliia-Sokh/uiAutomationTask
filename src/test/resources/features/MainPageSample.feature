@TestTask
Feature: In order to check the ability to create and maintain UI automation framework
  as a QA Automation
  I would like to see these tests passed

  Background:
    Given I navigate to the main page "index.html"

  @checkbox
  Scenario: Check the checkbox functionality
    When I select checkbox "Choice A"
    Then I check that checkbox "Choice A" is selected
    Then I un-select checkbox "Choice A"
    Then I check that checkbox "Choice A" is not selected

  @multiselectDropdown
  Scenario: Check drop-down menu function
    When I select the option "Option Group, Option Two" from the "Option One" menu
    Then I select multiple options from the  "Select Multiple"
      | Option Two                 |
      | Option Three               |
      | Option Group, Option Three |

  @table
  Scenario: Check the table cell value
    Then I check that "Table Heading 4" for "Table Cell 11" is "Table Cell 41" in the "Table Caption" table

  @button
  Scenario: Check buttons functionality and alerts
    Then I press "alert" button
    Then I check I can see an alert message "This is a JavaScript Alert!"
    Then I press "alert" button
    Then I accept alert message

  @textInputField
  Scenario: Check input fields
    Then I fill out "Text Input" with "text input"
    Then I fill out "password" with "p@ssw0rd"
    Then I check the "Text Input" text field has the "text input" value

  @pageLink
  Scenario: Check page link
    Then I click  the "Google page link" link
    Then I check if url is "https://www.google.com/"